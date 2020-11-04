package me.ichmagomaskekse.de.lobby.darkforge.effects;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.geometric.Circle;
import me.ichmagomaskekse.de.lobby.darkforge.EnchantHandler;

public class ArrowStormEffect extends Effect {
	
	final ArrayList<Entity> projectiles = new ArrayList<Entity>();
	final ArrayList<Entity> shooted = new ArrayList<Entity>();
	private Sound ice_break_sound = Sound.BLOCK_GLASS_BREAK;
	int amount_projectiles = 11;
	final Circle circle = new Circle(5);
	
	public ArrowStormEffect(LivingEntity damager, LivingEntity target, ItemStack weapon) {
		super(EffectType.ARROW_STORM, damager, target, weapon);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void play() {
		target.getWorld().playSound(target.getEyeLocation(), ice_break_sound, 6f, 0f);
		
		timer = new BukkitRunnable() {
			
			double offset = 0.0d;
			@Override
			public void run() {
				if(durationLeft == duration) {
					durationLeft-=1;
					/* Wenn der Effekt gesartet wurde */
					if(projectiles.isEmpty() == false) for(Entity s : projectiles)s.remove();
					
					summonProjectiles();
//					searchNewProjectile(20, 4, 20);
				}else if(durationLeft <= 0 || target.isDead()) {
					
					/* Wenn der Effekt die Wirkung verliert */
					if(projectiles.isEmpty() == false) {
						for(Entity p : projectiles) {
							p.setGravity(true);
							p.getWorld().spawnParticle(Particle.FLASH,
									p.getLocation().getX(),
									p.getLocation().getY()+p.getBoundingBox().getHeight(),
									p.getLocation().getZ(),
									0, 0, 0, 1, 0);
							p.remove();
						}
					}
					
					EffectHandler.stopEffect(getID());
					cancel();
					return;
				}else {
					/* Während der Effekt wirkt */
//					if(projectiles.size() < amount_projectiles) {
//						searchNewProjectile(1, 3, 1);
//					}
					
					double angle = (360 / projectiles.size());
					int index = 1;
					for(Entity p : projectiles) {
						p.setGravity(false);
						

						Vector v = circle.getLocationAtAngle(target.getEyeLocation().add(0,1,0), ((angle*index+offset)));
						p.teleport(new Location(target.getWorld(), v.getX(), v.getY(), v.getZ()));
						
						p.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME,
								p.getLocation().getX(),
								p.getLocation().getY(),
								p.getLocation().getZ(),
								0, 0, 0, 1, 0);
						p.setFireTicks(10);
						index++;
					}
					offset+=0.01d;
					if(offset >= 25.0d) offset = 0.0d;
					durationLeft-=1;
					
					/*---------------------------*/
				}
				
			}
		};
		timer.runTaskTimer(ServerSystem.getInstance(), 0l, 1l);
	}

	@Override
	public void stop() {
		durationLeft = 0;
		if(projectiles.isEmpty() == false) {
			for(Entity p : projectiles) {
				p.setGravity(true);
				p.remove();
			}
		}
		
		
		if(damager != null) {
			EnchantHandler.affected_entities.remove(target, this);
		}
	}
	
	public void searchNewProjectile(double x, double y, double z) {
		for(Entity entity : target.getNearbyEntities(x, y, z)) {
			if(entity instanceof Entity && shooted.contains(entity) == false && entity instanceof Arrow == false) {
				projectiles.add(entity);
				circle.durchmesser = (projectiles.size() / (amount_projectiles / 0.5));
				circle.radius =  circle.durchmesser / 2;
				if(projectiles.size() >= amount_projectiles) break;
			}
		}
	}
	
	public void shootRandomProjectile(Vector v) {
		Entity ent = projectiles.get(0);
		shooted.add(ent);
		projectiles.remove(ent);
		
		ent.teleport(target.getEyeLocation().add(0,5,0));
		ent.setGravity(true);
		ent.setVelocity(v.multiply(1.2));
		if(projectiles.isEmpty()) stop();
	}
	
	public void summonProjectiles() {
		Location loc = target.getEyeLocation().clone().add(0,5,0);
		for(int i = 0; i != amount_projectiles; i++) {
			ArmorStand a = loc.getWorld().spawn(loc.clone(), ArmorStand.class);
			
			a.setGravity(false);
			a.setVelocity(static_velo);
			a.setVisible(false);
			a.setSmall(true);
			a.getEquipment().setHelmet(new ItemStack(Material.STONE));
			projectiles.add(a);
		}
	}
	
}
