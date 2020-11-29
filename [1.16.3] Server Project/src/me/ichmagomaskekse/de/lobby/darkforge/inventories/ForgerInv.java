package me.ichmagomaskekse.de.lobby.darkforge.inventories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.Code;
import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.lobby.darkforge.DarkEnchant;
import me.ichmagomaskekse.de.lobby.darkforge.Darkforge;

public class ForgerInv {
	
	public static LinkedList<Material> enchantable_items = new LinkedList<Material>();
	public static HashMap<ItemStack, DarkEnchant[]> possibilities = new HashMap<ItemStack, DarkEnchant[]>();	
	
	private Inventory inv = null;
	private Player player = null;
	private InventoryType type = InventoryType.HOPPER;
	private String inv_title = "§8Dunkelschmiede";
	public ItemStack item_to_enchant = null;
	public Darkforge dforge = null;
	private ItemStack refresh_item = null;
	private int refresh_cost = 10;
	public static String refresh_item_name_containment = "Aktualisieren";
	private boolean loadedAllPossibilities = false;
	public String selected_code = "";
	
	public BukkitRunnable updater = null;
	
	public ForgerInv(Darkforge dforge, Player player) {
		this.dforge = dforge;
		this.player = player;
		
		enchantable_items.add(Material.CROSSBOW);
		enchantable_items.add(Material.BOW);
		enchantable_items.add(Material.TRIDENT);
		
		enchantable_items.add(Material.WOODEN_SWORD);
		enchantable_items.add(Material.WOODEN_AXE);
		
		enchantable_items.add(Material.STONE_SWORD);
		enchantable_items.add(Material.STONE_AXE);
		
		enchantable_items.add(Material.IRON_SWORD);
		enchantable_items.add(Material.IRON_AXE);
		
		enchantable_items.add(Material.GOLDEN_SWORD);
		enchantable_items.add(Material.GOLDEN_AXE);
		
		enchantable_items.add(Material.DIAMOND_SWORD);
		enchantable_items.add(Material.DIAMOND_AXE);
		
		enchantable_items.add(Material.NETHERITE_SWORD);
		enchantable_items.add(Material.NETHERITE_AXE);
	}
	
	public void openInventory(Player p) {
		inv = Bukkit.createInventory(null, type, inv_title);
		 
		 updater = new BukkitRunnable() {
			 boolean loaded = false;
			@Override
			public void run() {
				if(inv.getItem(0) != null && enchantable_items.contains(inv.getItem(0).getType())) {
					/* Entchantments werden gesetzt, wenn Item zum Verzaubern vorhanden ist */						
					if(loaded == false) {
						loaded = true;
						refreshEnchants(true);
					}
				}else {
					/* Platzhalter werden platziert, wenn kein Item zum Verzaubern vorhanden ist */
					refreshEnchants(true);
					possibilities.remove(getItemToEnchant());
					loadedAllPossibilities = false;
					loaded = false;
				}
			}
		};
		updater.runTaskTimer(ServerSystem.getInstance(), 0l, 1l);
		
		p.openInventory(inv);
	}
	
	public void cancel() {
		updater.cancel();
	}
	
	public void loadAllPossibilitiesOfPlayerInventoryItems() {
		if(loadedAllPossibilities == true) return;
		ArrayList<ItemStack> content = new ArrayList<ItemStack>();
		for(ItemStack i : player.getInventory().getContents()) content.add(i);
		content.add(player.getOpenInventory().getItem(0));
		
		for(ItemStack item : content) {
			if(item != null && enchantable_items.contains(item.getType()) && possibilities.containsKey(item) == false) {
				if(Filer.hasEnchantPossibilities(player, item)) {
					possibilities.put(item, Filer.getEnchantPossibilities(player, item));
				}
			}
		}
		
		loadedAllPossibilities = true;
	}
	
	public boolean refreshEnchants(boolean for_free) {
		
		/* Checken ob das Item DarkEnchant-Tauglich ist */
		if(checkAllDependencies()) {
			/* Checken ob genügend Seelen vorhanden sind */
			if(for_free == false) {
				if(dforge.souls < refresh_cost) {
					ServerSystem.sendMessage(player, "Du hast nicht genug Seelen!");
					return false;
				}
			}
			
			/* Die 3 Platzhalter aus dem Inventar löschen */
			inv.setItem(1, null);
			inv.setItem(2, null);
			inv.setItem(3, null);
			
			/* Checken, ob die Possobilities des Items geladen wurde.
			 * Falls nicht, sollen sie geladen werden. */
			if(getItemToEnchant() != null &&
					possibilities.containsKey(getItemToEnchant()) == false) loadAllPossibilitiesOfPlayerInventoryItems();
			
			
			if(possibilities.containsKey(getItemToEnchant())) {
				/* Wenn die Possibilities von dem Item bereits vorhanden sind, werden diese nun geladen */
				int index = 1;
				DarkEnchant[] enchants = possibilities.get(getItemToEnchant());
				for(DarkEnchant d : enchants) {
					inv.setItem(index, new ItemBuilder().getEnchantmentItem(d));
					index++;
				}
			}else {
				/* Wenn es bisher keine Possibilities für dieses Item gab, werden neue zufällig erstellt und gespeichert */
				String[] enchants = {DarkEnchant.UNDEFINED.toString(), DarkEnchant.UNDEFINED.toString(), DarkEnchant.UNDEFINED.toString()};
				
				int id = Code.random.nextInt(DarkEnchant.values().length);
				for(int i = 0; i != 3; i++) {
					while(inv.contains((new ItemBuilder().getEnchantmentItem(DarkEnchant.values()[id]))) && id == 0 ||
							id == 0 ||
							inv.contains((new ItemBuilder().getEnchantmentItem(DarkEnchant.values()[id]))))	id = Code.random.nextInt(DarkEnchant.values().length);		
					inv.setItem(i+1, new ItemBuilder().getEnchantmentItem(DarkEnchant.values()[id]));
					enchants[i] = DarkEnchant.values()[id].toString();
					id = 0;
				}
				Filer.saveNewEnchantPossibilities(player, getItemToEnchant(), enchants);
				possibilities.put(getItemToEnchant(), Filer.getEnchantPossibilities(player, getItemToEnchant()));
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
		}else {
			if(inv.getItem(0) == null || enchantable_items.contains(inv.getItem(0).getType()) == false) {
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
			}
			return false;
		}
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
	
	public boolean checkAllDependencies() {
		if(getItemToEnchant() == null) return false;
		if(getItemToEnchant().getEnchantments().isEmpty() == true) {
			ServerSystem.sendMessage(player, "Dieses Item benötigt eine beliebige Default-Verzauberung.");
			return false;
		}
		return true;
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
