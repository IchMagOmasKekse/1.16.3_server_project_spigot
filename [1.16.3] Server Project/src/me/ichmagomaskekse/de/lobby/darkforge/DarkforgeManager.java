package me.ichmagomaskekse.de.lobby.darkforge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.lobby.Lobby;
import me.ichmagomaskekse.de.lobby.darkforge.inventories.ForgerInv;

public class DarkforgeManager implements Listener {
	
	public HashMap<Integer, Darkforge> darkforges = new HashMap<Integer, Darkforge>();
	public static HashMap<Player, ForgerInv> inventories = new HashMap<Player, ForgerInv>();
	Random ran = new Random();
	
	public DarkforgeManager() {
		ServerSystem.getInstance().getServer().getPluginManager().registerEvents(this, ServerSystem.getInstance());
		Darkforge forge = new Darkforge(Lobby.darkforge_id, new Location(Bukkit.getWorld("world"), 0, 97, 21));
		registerNewDarkforge(forge);
	}
	public boolean closeAll() {
		for(Darkforge d : darkforges.values()) {
			d.close();
		}
		return true;
	}
	public boolean registerNewDarkforge(Darkforge forge) {
		if(darkforges.containsKey(forge.forge_id)) return false;
		else darkforges.put(forge.forge_id, forge);
		return true;
	}
	public boolean deleteDarkforge(int forge_id) {
		if(darkforges.containsKey(forge_id)) {
			darkforges.get(forge_id).close();
			darkforges.remove(forge_id);
		} else return false;
		return true;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null) {
			ItemStack item = e.getItem();
			if(item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(DarkforgeManager.DarkforgeCreator.displayname)) {
				ServerSystem.darkforge_manager.registerNewDarkforge(new Darkforge(ran.nextInt(100), e.getClickedBlock().getLocation().add(0,1,0)));
				e.getPlayer().getWorld().playSound(e.getClickedBlock().getLocation().add(0,1,0), Sound.BLOCK_ANVIL_LAND, 6f, -3f);
			}
		}
	}
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if(e.getView().getTitle().equals("§8Dunkelschmiede")) {
			if(e.getCurrentItem() != null) {
				if(e.getCurrentItem().hasItemMeta()
						&& e.getCurrentItem().getItemMeta().hasDisplayName()
						&& e.getCurrentItem().getItemMeta().getDisplayName().contains(ForgerInv.refresh_item_name_containment)) {
					
					/* Der Spieler versucht zu aktualisieren */
					e.setCancelled(true);
					if(e.getCurrentItem().getItemMeta().hasLore() && e.getCurrentItem().getItemMeta().getLore().contains("§cNicht genug §bSeelen§c!") == false) {
						
						if(inventories.get(((Player)e.getWhoClicked())).refreshEnchants(false))
							inventories.get(((Player)e.getWhoClicked())).useSouls(inventories.get(((Player)e.getWhoClicked())).getRefreshCost());
						
					}else inventories.get(((Player)e.getWhoClicked())).dforge.getLocation().getWorld().playSound(inventories.get(((Player)e.getWhoClicked())).dforge.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 6f, 0f);
				
				
				
				
				
				}else if(e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasDisplayName()) {
					
					/* Checken ob ein Enchantment ausgewählt wurde */
					boolean skip = true;
					for(DarkEnchant en : DarkEnchant.values()) {
						if(en.getDisplayname().equals(e.getCurrentItem().getItemMeta().getDisplayName())) skip = false;
					}
					/* Wenn keins ausgewählt wurde, soll der Vorgang abgebrochen werden */
					if(skip) return;
					e.setCancelled(true);
					ItemStack player_item = e.getClickedInventory().getItem(0);
					ItemMeta player_meta = player_item.getItemMeta();
					
					if(ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).forge_level == 0) {
						ServerSystem.sendMessage(e.getWhoClicked(), "Deine Dunkelschmiede hat kein ausreichendes Level");
						return;
					}
					
					if(player_item.getEnchantments().isEmpty() == true) {
						ServerSystem.sendMessage(e.getWhoClicked(), "Dieses Item benötigt eine beliebige Default-Verzauberung.");
						return;
					}
					
					if(player_meta.hasLore()) {
						for(int i = 0; i != player_meta.getLore().size(); i++) {				
							for(DarkEnchant de : DarkEnchant.values()) {
								for(int l = 0; l != player_meta.getLore().size()-1; l++) {
									if(player_meta.getLore().get(l).contains(de.getLore())) {
										ServerSystem.sendMessage(e.getWhoClicked(), "Dein Item hat bereits eine dunkle Verzauberung");
										return;
									}
									
								}
							}
						}				
					}
					ItemStack item = e.getCurrentItem();
					ItemMeta meta = item.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					int lvl = ran.nextInt(ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).forge_level);
					lvl += 1;
					String level = getRoemischZahl(lvl);
					int souls = ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).souls;
					
					if(ServerSystem.debug) ServerSystem.sendMessage(e.getWhoClicked(), "level: "+lvl);
					
					/* Souls überprüfen und gegebenenfalls Souls-Kosten abziehen */
					if(souls < lvl && souls == 0) {
						ServerSystem.sendMessage(e.getWhoClicked(), "Du hast nicht genügend Seelen gesammelt!");
						return;
					}
					if(souls > 0 && souls < lvl) {
						lvl = souls;
						ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).souls = 0;
					}else if(souls > 0 && souls > lvl) ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).souls -= lvl;
					else if(lvl > 0 && souls == lvl) ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).souls -= lvl;
					
					/* Update Display -> */ ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).soul_display.replaceParamsInDisplayText("{SOULS}", ""+ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).souls);
					
					/*  */
					
					for(int i = 0; i != (DarkEnchant.values().length); i++) {
						if(ServerSystem.debug)ServerSystem.sendMessage(e.getWhoClicked(), "Testing for "+DarkEnchant.values()[i].getCodename()+"...");
						if(meta.hasDisplayName() && meta.getDisplayName().equals(DarkEnchant.values()[i].getDisplayname())) {
							if(ServerSystem.debug)ServerSystem.sendMessage(e.getWhoClicked(), "Test successfull!");
							lore.add(DarkEnchant.values()[i].getLore()+" "+level);
							player_meta.setLore(lore);
							player_item.setItemMeta(player_meta);
							break;
						}
					}
					e.getWhoClicked().getWorld().playSound(inventories.get(((Player)e.getWhoClicked())).dforge.getLocation().add(0.5,0,0.5), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 6f, -3f);
					inventories.get(e.getWhoClicked()).setItemToEnchant(player_item);
//			e.getWhoClicked().closeInventory();
				}else if(e.getRawSlot() == 0) {
					/* Wenn der Spieler sein Item zum Verzaubern reingelegt hat,
					 * sollen neue Entchantments gewürfelt werden */
					if(inventories.get(e.getWhoClicked()).getItemToEnchant() != null &&
							inventories.get(e.getWhoClicked()).getItemToEnchant().getType().isBlock() == false) {
						inventories.get(e.getWhoClicked()).refreshEnchants(true);
					}

				}
			}
		}
		
	}
	
	public static String getRoemischZahl(int i) {
		switch(i) {
		case 0:
			return "I";
		case 1:
			return "I";
		case 2:
			return "II";
		case 3:
			return "III";
		case 4:
			return "IV";
		case 5:
			return "V";
		case 6:
			return "VI";
		case 7:
			return "VII";
		case 8:
			return "VIII";
		case 9:
			return "IX";
		case 10:
			return "X";
		}
		return "";
	}
	
	public static class DarkforgeCreator {
		
		private static Material material = Material.RESPAWN_ANCHOR;
		private static String displayname = "§fErschaffe §5Dunkelschmiede";
		
		public DarkforgeCreator() {
			
		}
		
		public static ItemStack giveCreator(Player p) {
			ItemStack item = new ItemStack(material);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(displayname);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			item.setItemMeta(meta);
			item.addUnsafeEnchantment(Enchantment.DURABILITY,10);
			
			return item;
		}
		
	}
	
}
