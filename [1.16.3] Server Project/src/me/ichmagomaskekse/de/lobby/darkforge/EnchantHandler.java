package me.ichmagomaskekse.de.lobby.darkforge;

import java.util.HashMap;
import java.util.List;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftTippedArrow;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.lobby.darkforge.effects.ArrowStormEffect;
import me.ichmagomaskekse.de.lobby.darkforge.effects.Effect;
import me.ichmagomaskekse.de.lobby.darkforge.effects.Effect.EffectType;
import me.ichmagomaskekse.de.lobby.darkforge.effects.EffectHandler;
import me.ichmagomaskekse.de.lobby.darkforge.effects.FreezeEffect;

public class EnchantHandler implements Listener {
	
	public static HashMap<LivingEntity, Effect> affected_entities = new HashMap<LivingEntity, Effect>();
	
	public EnchantHandler() {
		ServerSystem.getInstance().getServer().getPluginManager().registerEvents(this, ServerSystem.getInstance());
	}
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {			
			if(affected_entities.containsKey(e.getPlayer()) && affected_entities.get(e.getPlayer()) instanceof ArrowStormEffect) {
				ArrowStormEffect ase = (ArrowStormEffect) affected_entities.get(e.getPlayer());
				ase.shootRandomProjectile(e.getPlayer().getLocation().getDirection());
			}
		}
	}
	@EventHandler
	public void onTeleport(EntityTeleportEvent e) {
		if(affected_entities.containsKey(((LivingEntity)e.getEntity())) &&
				affected_entities.get(((LivingEntity)e.getEntity())) instanceof FreezeEffect)
			e.setCancelled(true);
	}
	@EventHandler
	public void onShoot(ProjectileLaunchEvent e) {
		if(e.getEntity().getShooter() instanceof LivingEntity) {
			if(affected_entities.containsKey((LivingEntity)e.getEntity().getShooter()) &&
					affected_entities.get(((LivingEntity)e.getEntity().getShooter())) instanceof FreezeEffect)
					e.setCancelled(true);			
		}
	}
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof ArmorStand && e.getEntity().getCustomName() != null && e.getEntity().getCustomName().contains(EffectType.FREEZE.getCodename())) {
			/* Wenn ein IceCube vom FreezeEffect geschlagen wird, soll etwas passieren */
			e.setCancelled(true);
			ArmorStand a = (ArmorStand) e.getEntity();
			String id = a.getCustomName().split(":")[1];
			if(EffectHandler.getEffectById(id) != null && EffectHandler.getEffectById(id) instanceof FreezeEffect)
				((FreezeEffect)EffectHandler.getEffectById(id)).destroyArmorStand(a);
			
		}else if(e.getEntity() instanceof LivingEntity &&
				!(e.getEntity() instanceof ArmorStand) &&
				!(e.getEntity() instanceof Projectile) &&
				!(e.getEntity() instanceof CraftTippedArrow)) {
			LivingEntity damager = null;
			try{
				damager = (LivingEntity) e.getDamager();
			}catch(ClassCastException ex) {
				return;
			}
			
			if(e.getCause() == DamageCause.ENTITY_ATTACK) {				
				if(damager.getEquipment().getItemInMainHand() != null) {
					ItemStack item = damager.getEquipment().getItemInMainHand();
					if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
						List<String> lore = item.getItemMeta().getLore();
						
						if(lore.get(lore.size()-1).contains(DarkEnchant.FREEZE.getLore())) {
							e.setCancelled(true);
							((LivingEntity)e.getEntity()).damage(e.getDamage());
							FreezeEffect fe = new FreezeEffect(damager, ((LivingEntity)e.getEntity()), item);
							affected_entities.put(((LivingEntity)e.getEntity()), fe);
							EffectHandler.startNewEffect(fe);
							return;
						}else if(lore.get(lore.size()-1).contains(DarkEnchant.CONDUCTIVITY.getLore())) {
							return;
						}else if(lore.get(lore.size()-1).contains(DarkEnchant.DARKNESS.getLore())) {
							e.setCancelled(true);

							ArrowStormEffect fe = new ArrowStormEffect(((LivingEntity)e.getEntity()), ((LivingEntity)e.getEntity()), item);
							affected_entities.put(((LivingEntity)e.getEntity()), fe);
							EffectHandler.startNewEffect(fe);
							return;
						}else if(lore.get(lore.size()-1).contains(DarkEnchant.INTELLI_BEAST.getLore())) {
							return;
						}else if(lore.get(lore.size()-1).contains(DarkEnchant.LOYALITY.getLore())) {
							return;
						}else if(lore.get(lore.size()-1).contains(DarkEnchant.PICKPOCKET.getLore())) {
							return;
						}
					}
				}
			}
		}
	}
	
}
