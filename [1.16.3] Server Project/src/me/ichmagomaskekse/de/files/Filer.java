package me.ichmagomaskekse.de.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.ichmagomaskekse.de.Code;
import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.lobby.darkforge.DarkEnchant;
import me.ichmagomaskekse.de.lobby.darkforge.DarkforgeManager;

public class Filer {
	
	public static final String root_dir = "ServerSystem";
	public static String config_path = "plugins/"+root_dir+"/config.yml";
	public static String lobby_path = "plugins/"+root_dir+"/lobby_properties.yml";
	public static String permissions_path = "plugins/"+root_dir+"/permissions.yml";
	public static String enchant_possibilities = "plugins/"+root_dir+"/enchant_possibilities.yml";
	
	public Filer(boolean overwrite_config) {
		createConfig(overwrite_config);
		ServerSystem.getInstance().saveResource("permissions.yml", true);
	}
	
	public static Location getLocation(String file_path, String categoryName) {
		File file = new File(file_path);
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			double x,y,z;
			float yaw, pitch;
			String world;
			
			x = cfg.getDouble(categoryName+".X");
			y = cfg.getDouble(categoryName+".Y");
			z = cfg.getDouble(categoryName+".Z");
			
			yaw = (int) cfg.getInt(categoryName+".Yaw");
			pitch = (int) cfg.getInt(categoryName+".Pitch");
			
			world = cfg.getString(categoryName+".World");
			try {
				return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
			}catch(NullPointerException ex) {
				return null;
			}
		}else return null;
	}
	
	public static boolean setLocation(String file_path, String categoryName, Location loc) {
		File file = new File(file_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set(categoryName+".X", loc.getX());
		cfg.set(categoryName+".Y", loc.getY());
		cfg.set(categoryName+".Z", loc.getZ());
		cfg.set(categoryName+".Yaw", loc.getYaw());
		cfg.set(categoryName+".Pitch", loc.getPitch());
		cfg.set(categoryName+".World", loc.getWorld().getName());
		
		try {
			cfg.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static boolean setSouls(int souls) {
		File file = new File(lobby_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("Souls", souls);
		
		try {
			cfg.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static int getSouls() {
		File file = new File(lobby_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		return cfg.getInt("Souls");
	}
	
	/*
	 * Liest einen String aus einer Datei und ersetzt & mit §
	 */
	public static String readString(String file_path, String categoryName) {
		File file = new File(file_path);
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			return (cfg.getString(categoryName).replace("&", "§"));
		}else return "FILE_DOES_NOT_EXIST";
	}
	
	public static int readInteger(String file_path, String categoryName) {
		File file = new File(file_path);
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			return (cfg.getInt(categoryName));
		}else return -9999;
	}
	
	public static boolean readBoolean(String file_path, String categoryName) {
		File file = new File(file_path);
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			return (cfg.getBoolean(categoryName));
		}else return false;
	}
	
	/*
	 * Erstellt die config.yml wenn sie nicht existiert
	 * Replacements:
	 *   $NEWLINE$ = in einer neuen Zeile schreiben
	 *   $USERNAME$ = der Username des Spielers */
	public static boolean createConfig(boolean overwrite) {
		File file = new File(config_path);
		if(file.exists() == false || overwrite) {
			ServerSystem.getInstance().saveResource("config.yml", true);
			return true;
		}else return true;
	}
	
	/*
	 * Speichert ein Item mit den dazugehörigen Verzauberungs-Möglichkeiten der DarkForge
	 */
	private static final int blocks = 4, blocksize = 3;
	public static boolean saveNewEnchantPossibilities(Player player, ItemStack item, String[] enchants) {
		File file = new File(enchant_possibilities);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		/* codes beinhaltet alle Codes, die jemals an ein Item vergeben wurden, die er versucht hat zu entchanten */
		ArrayList<String> codes = null;
		ArrayList<String> old_codes = new ArrayList<String>();
		if(file.exists()) codes = (ArrayList<String>) cfg.getStringList(player.getUniqueId().toString());
		else codes = new ArrayList<String>();
		
		String code_of_item = "";
		if(codes.isEmpty()) {
			code_of_item = Code.getRandomCode(blocks, blocksize);
			codes.add(code_of_item);
			cfg.set(player.getUniqueId().toString(), codes);
		} else {
			for(String s : codes) {
				if(cfg.isItemStack(s+".Item") == false || cfg.getItemStack(s+".Item") == null) old_codes.add(s);/* Nichtmehr verwendete Codes werden aus der Liste gelöscht.
			Dies könnte man als eine Art 'Reinigung' oder 'Ordnungschaffen' betiteln */
				else if(isSameItem(cfg.getItemStack(s+".Item"), item)) {
					code_of_item = s;
				}
			}
			
			if(old_codes.isEmpty() == false) for(String s : old_codes) codes.remove(s);
			
			if(code_of_item.equals("")) {
				code_of_item = Code.getRandomCode(blocks, blocksize);
				codes.add(code_of_item);
				cfg.set(player.getUniqueId().toString(), codes);
			}
		}
		
		
		cfg.set(code_of_item+".Item", item);
		cfg.set(code_of_item+".DarkEnchants", enchants);
		try {
			cfg.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * Gibt zurück, ob ein Item bereits in die DarkForge gelegt wurde
	 */
	public static boolean hasEnchantPossibilities(Player player, ItemStack item) {
		File file = new File(enchant_possibilities);
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.getStringList(player.getUniqueId().toString()).isEmpty()) return false;
			else {
				for(String s : cfg.getStringList(player.getUniqueId().toString())) {
					
					if(cfg.isItemStack(s+".Item") && isSameItem(cfg.getItemStack(s+".Item"), item)) {
						DarkforgeManager.inventories.get(player).selected_code = s;
						return true;
					}
				}
			}
		}else return false;
		return false;
	}
	
	/*
	 * Gibt alle Verzauberungs-Möglichkeiten eines Items zurück, sofern dieses bereits in die DarkForge gelegt wurde
	 */
	public static DarkEnchant[] getEnchantPossibilities(Player player, ItemStack item) {
		File file = new File(enchant_possibilities);
		if(file.exists()) {
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			if(cfg.getStringList(player.getUniqueId().toString()).isEmpty()) return null;
			else {
				final ArrayList<String> codes = (ArrayList<String>) cfg.getStringList(player.getUniqueId().toString());
				DarkEnchant[] enchants = {DarkEnchant.UNDEFINED, DarkEnchant.UNDEFINED, DarkEnchant.UNDEFINED};
				
				int index = 0;
				for(String s : codes) {
					if((cfg.isItemStack(s+".Item") && isSameItem(cfg.getItemStack(s+".Item"), item)) &&
							cfg.getStringList(s+".DarkEnchants").isEmpty() == false) {
						
						ArrayList<String> en = (ArrayList<String>) cfg.getStringList(s+".DarkEnchants");
						for(String name : en) {
							enchants[index] = DarkEnchant.valueOf(name);						
							index++;
						}
					}
				}
				return enchants.clone();
			}
		}else return null;
	}
	
	public static boolean isSameItem(ItemStack i1, ItemStack i2) {
		boolean same = false;
		
		if(i1.getType() == i2.getType()) same = true; else return false;
//		if(i1.getData() == i2.getData()) same = true; else return false;
		if(i1.getAmount() == i2.getAmount()) same = true; else return false;
		
		if(i1.hasItemMeta() && i2.hasItemMeta()) same = true; else return false;
		if(i1.getItemMeta().hasDisplayName() && i2.getItemMeta().hasDisplayName()) {
			same = true;
			if(i1.getItemMeta().getDisplayName().equals(i2.getItemMeta().getDisplayName())) same = true; else return false;
		}
		if(i1.getItemMeta().hasLore() && i2.getItemMeta().hasLore()) {
			same = true; 
			if(i1.getItemMeta().getLore().size() == i2.getItemMeta().getLore().size()) same = true; else return false;
			for(int i = 0; i != i1.getItemMeta().getLore().size(); i++) {
				if(i1.getItemMeta().getLore().get(i).equals(i2.getItemMeta().getLore().get(i)) == false) return false;
				else same = true;
			}
		}
		if(i1.getEnchantments().size() > 0) {
			if(i2.getEnchantments().size() > 0) {
				for(Enchantment e : i1.getEnchantments().keySet()) {
					if(i2.getEnchantments().containsKey(e)) {
						if(i1.getEnchantments().get(e) == i2.getEnchantments().get(e)) same = true;
						else return false;
					} else return false;
				}
			}else return false;
		}
		return same;
	}

}
