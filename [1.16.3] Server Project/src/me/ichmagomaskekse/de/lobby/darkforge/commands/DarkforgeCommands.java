package me.ichmagomaskekse.de.lobby.darkforge.commands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.geometric.Circle;
import me.ichmagomaskekse.de.lobby.Lobby;
import me.ichmagomaskekse.de.lobby.darkforge.DarkEnchant;
import me.ichmagomaskekse.de.lobby.darkforge.DarkforgeManager;
import me.ichmagomaskekse.de.lobby.darkforge.EnchantHandler;
import me.ichmagomaskekse.de.lobby.darkforge.effects.ArrowStormEffect;
import me.ichmagomaskekse.de.lobby.darkforge.effects.EffectHandler;
import me.ichmagomaskekse.de.lobby.darkforge.effects.FreezeEffect;

public class DarkforgeCommands implements CommandExecutor {
	
	public DarkforgeCommands() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			switch(args.length) {
			case 0:
				if(ServerSystem.hasPermission(p, "server.darkforge.create")) {
					p.getInventory().addItem(DarkforgeManager.DarkforgeCreator.giveCreator(p));
				}
				break;
			case 1:
				if(args[0].equals("range") && ServerSystem.hasPermission(p, "server.darkforge.showrange")) {
					ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).show_obsidian_range =
							!ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).show_obsidian_range;
					p.getWorld().playSound(p.getLocation(), Sound.ENTITY_TURTLE_EGG_HATCH, 1f, 1f);
				}else if(args[0].equals("monster") && ServerSystem.hasPermission(p, "server.darkforge.monster")) {
					Zombie z = p.getWorld().spawn(p.getLocation(), Zombie.class);
					ItemStack a = new ItemStack(Material.WOODEN_SWORD);
					ItemMeta meta = a.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					
					
					lore.add(DarkEnchant.FREEZE.getLore()+" "+DarkforgeManager.getRoemischZahl(10));
					meta.setLore(lore);
					a.setItemMeta(meta);
					
					z.getEquipment().setItemInMainHand(a);
				}else if(args[0].equals("fe") && ServerSystem.hasPermission(p, "server.darkforge.freeze_effect")) {
					EffectHandler.startNewEffect(new FreezeEffect(p, p, new ItemStack(Material.AIR)));
				}else if(args[0].equals("ase") && ServerSystem.hasPermission(p, "server.darkforge.arrow_storm_effect")) {
					EffectHandler.startNewEffect(new ArrowStormEffect(p, p, new ItemStack(Material.AIR)));
				}else if(args[0].equals("spawnarrows") && ServerSystem.hasPermission(p, "server.darkforge.spawnarrows")) {
					Circle circle = new Circle(3);
					for(int i = 0; i != 120; i ++) {
						Vector v = circle.getLocationAtAngle(p.getLocation(), i);
						ArmorStand a = p.getWorld().spawn(new Location(p.getWorld(), v.getX(),v.getY()+3,v.getZ()), ArmorStand.class);
						a.getEquipment().setHelmet(new ItemStack(Material.TNT));
						a.setVisible(false);
						a.setGravity(false);
						a.setVelocity(new Vector(0,0,0));
						a.setRotation(0, 90);
					}
				}else if(args[0].equals("stop") && ServerSystem.hasPermission(p, "server.darkforge.stop")) {
					if(EnchantHandler.affected_entities.containsKey(p)) {
						EnchantHandler.affected_entities.get(p).stop();
						EnchantHandler.affected_entities.remove(p);
					}
				}
				break;
				
				default:
					
					break;
			}
		}else sender.sendMessage(ServerSystem.prefix+"Dieser Befehl is nur für Spieler!");
		
		return false;
	}
	
}
