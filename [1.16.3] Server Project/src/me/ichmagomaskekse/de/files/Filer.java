package me.ichmagomaskekse.de.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ichmagomaskekse.de.ServerSystem;

public class Filer {
	
	public static final String root_dir = "ServerSystem";
	public static String config_path = "plugins/"+root_dir+"/config.yml";
	public static String lobby_path = "plugins/"+root_dir+"/lobby_properties.yml";
	public static String permissions_path = "plugins/"+root_dir+"/permissions.yml";
	
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
	
}
