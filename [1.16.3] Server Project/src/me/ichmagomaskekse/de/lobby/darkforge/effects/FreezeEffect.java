package me.ichmagomaskekse.de.lobby.darkforge.effects;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import me.ichmagomaskekse.de.Code;
import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.lobby.darkforge.EnchantHandler;

public class FreezeEffect extends Effect {
	
	private ArrayList<ArmorStand> stands_to_destroy = new ArrayList<ArmorStand>();
	private Sound ice_break_sound = Sound.BLOCK_GLASS_BREAK;
	
	public FreezeEffect(LivingEntity damager, LivingEntity target, ItemStack weapon) {
		super(EffectType.FREEZE, damager, target, weapon);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void play() {
		target.getWorld().playSound(target.getEyeLocation(), ice_break_sound, 6f, 0f);

		target.setVelocity(new Vector(0,-10,0));
		target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, (int)duration, 2, true, true, true));
		target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)duration, 100, true, true, true));
		
		
		timer = new BukkitRunnable() {
			
			int amount_armorstand = ((int)(target.getBoundingBox().getHeight()+1)*7);
			final ArrayList<ArmorStand> stands = new ArrayList<ArmorStand>();
			final float yaw = target.getLocation().clone().getYaw();
			final float pitch = target.getLocation().clone().getPitch();
			
			@Override
			public void run() {
				if(durationLeft == duration) {
					durationLeft-=1;
					/* Wenn der Effekt gesartet wurde */
					if(stands.isEmpty() == false) for(ArmorStand s : stands)s.remove();
					
					for(int i = 0; i < amount_armorstand; i++) {
						
						ArmorStand temp = target.getWorld().spawn(target.getEyeLocation().clone().add(0,-(1.5+(Code.random.nextInt(i+1)*0.15)),0), ArmorStand.class);
						
						temp.setVelocity(static_velo);
						temp.setGravity(false);
						temp.setSmall(false);
						temp.getEquipment().setHelmet(new ItemStack(Material.ICE));
						temp.setHeadPose(new EulerAngle(random.nextInt(360), random.nextInt(360), random.nextInt(360)));
						temp.setInvisible(true);
						temp.setInvulnerable(false);
						temp.setCollidable(false);
						temp.setCustomName(EffectType.FREEZE.getCodename()+":"+getID());
						temp.setCustomNameVisible(false);
						stands.add(temp);
					}
				}else if(durationLeft <= 0 || target.isDead()) {
					
					/* Wenn der Effekt die Wirkung verliert */
					
					for(ArmorStand a : stands) {
						if(a.isDead() == false) killArmorStand(a, false);
						a.remove();
					}
					target.getWorld().playSound(target.getEyeLocation(), ice_break_sound, 2f, 6f);
					
					/*Check for IceCubes*/
					boolean skip = false;
					for(ArmorStand a : stands) if(a.isDead() == false) break;
					if(skip) EffectHandler.stopEffect(getID());
					/*------------------------*/
					
					target.setGravity(true);
					target.removePotionEffect(PotionEffectType.BLINDNESS);
					target.removePotionEffect(PotionEffectType.SLOW);
					EffectHandler.stopEffect(getID());
					cancel();
					return;
				}else {
					/* Während der Effekt wirkt */
					if(stands_to_destroy.isEmpty() == false) {
						for(ArmorStand a : stands_to_destroy) {
							killArmorStand(a, true);
							stands.remove(a);
						}
						stands_to_destroy.clear();
						if(stands.isEmpty()) stop();
					}
					if(target instanceof Player == false) {
						target.setVelocity(new Vector(0,-50,0));
						target.setRotation(yaw, pitch);
					}
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
		if(timer != null && timer.isCancelled()==false) timer.cancel();
		for(Entity e : target.getWorld().getEntities()) {
			if(e instanceof ArmorStand) {
				if(e.getCustomName() != null && e.getCustomName().contains(getID())) e.remove();
			}
		}
		
		EnchantHandler.affected_entities.remove(target, this);
		if(target != null) {
			target.setGravity(true);
			target.removePotionEffect(PotionEffectType.BLINDNESS);
			target.removePotionEffect(PotionEffectType.SLOW);
		}
	}
	
	public void destroyArmorStand(ArmorStand a) {
		stands_to_destroy.add(a);
	}
	
	private void killArmorStand(ArmorStand a, boolean withSound) {
		a.getWorld().spawnParticle(Particle.SNOW_SHOVEL,
				a.getLocation().getX(),
				a.getLocation().getY()+a.getBoundingBox().getHeight()+(Code.random.nextInt(11)*0.1),
				a.getLocation().getZ(),
				0, 0, 0, 50, 0);
		
		if(withSound)a.getWorld().playSound(a.getEyeLocation(), ice_break_sound, 2f, 6f);
		
		a.remove();
	}

}
