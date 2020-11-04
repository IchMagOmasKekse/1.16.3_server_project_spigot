package me.ichmagomaskekse.de.lobby.darkforge.inventories;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.ichmagomaskekse.de.Code;
import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.lobby.darkforge.DarkEnchant;
import me.ichmagomaskekse.de.lobby.darkforge.Darkforge;

public class ForgerInv {
	
	private Inventory inv = null;
	private Player player = null;
	private InventoryType type = InventoryType.HOPPER;
	private String inv_title = "§8Dunkelschmiede";
	public ItemStack item_to_enchant = null;
	public Darkforge dforge = null;
	private ItemStack refresh_item = null;
	private int refresh_cost = 10;
	public static String refresh_item_name_containment = "Aktualisieren";
	
	public ForgerInv(Darkforge dforge, Player player) {
		this.dforge = dforge;
		this.player = player;
	}
	
	public void openInventory(Player p) {
		inv = Bukkit.createInventory(null, type, inv_title);
		inv.addItem(item_to_enchant);
		 refreshEnchants(true);
		
		p.openInventory(inv);
	}
	
	public boolean refreshEnchants(boolean for_free) {
		if(inv.getItem(0) == null || inv.getItem(0).getType() == Material.AIR) {
			ItemStack placeholder = new ItemStack(Material.BARRIER);
			ItemMeta meta = placeholder.getItemMeta();
			meta.setDisplayName("§cLege das zu verzaubernde");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§cItem in den Slot ganz links.");
			meta.setLore(lore);
			placeholder.setItemMeta(meta);
			inv.setItem(1, placeholder);
			inv.setItem(2, placeholder);
			inv.setItem(3, placeholder);
			inv.setItem(4, placeholder);
			
			return false;
		}
		if(for_free == false) {
			if(dforge.souls < refresh_cost) {
				ServerSystem.sendMessage(player, "Du hast nicht genug Seelen!");
				return false;
			}
		}
		inv.setItem(1, null);
		inv.setItem(2, null);
		inv.setItem(3, null);
		int id = Code.random.nextInt(DarkEnchant.values().length);
		for(int i = 0; i != 3; i++) {
			while(inv.contains(new ItemBuilder().getEnchantmentItem(DarkEnchant.values()[id])))	id = Code.random.nextInt(DarkEnchant.values().length);		
			inv.setItem(i+1, new ItemBuilder().getEnchantmentItem(DarkEnchant.values()[id]));
		}
		
		/* Refresh Item updaten */
		refresh_item = new ItemStack(Material.ENDER_CHEST);
		ItemMeta meta = refresh_item.getItemMeta();
		ArrayList<String> lore = new ArrayList<String>();
		if(dforge.souls >= refresh_cost) {
			meta.setDisplayName("§f"+refresh_item_name_containment);
			lore.add("§9Für §b"+refresh_cost+" Seelen §9aktualisieren");
		} else {
			meta.setDisplayName("§8"+refresh_item_name_containment);
			lore.add("§cNicht genug §bSeelen§c!");
		}
		meta.setLore(lore);
		refresh_item.setItemMeta(meta);
		inv.setItem(4, null);
		inv.setItem(4, refresh_item);
		return true;
	}
	
	public void useSouls(int amount) {
		dforge.useSouls(amount);
	}
	
	public void setItemToEnchant(ItemStack item) {
		this.item_to_enchant = item;
	}
	
	public ItemStack getItemToEnchant() {
		return inv.getItem(0);
	}
	
	public ItemStack getRefreshItemFromInventory() {
		return inv.getItem(4);
	}
	
	public int getRefreshCost() {
		return refresh_cost;
	}
	
	public class ItemBuilder {
		
		public ItemStack getEnchantmentItem(DarkEnchant de) {
			ItemStack item = new ItemStack(de.getMaterial());
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(de.getDisplayname());
			switch(de) {
			case PICKPOCKET:
				
				break;
			case DARKNESS:
				
				break;
			case LOYALITY:
				
				break;
			case CONDUCTIVITY:
				
				break;
			case INTELLI_BEAST:
				
				break;
				default:
					break;
			}
			
			item.setItemMeta(meta);
			
			return item;
		}
		
	}
	
}
