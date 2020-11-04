package me.ichmagomaskekse.de;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.money.AccountManager;

public class PlayerAtlas {
	
	private static String atlas_path = "plugins/"+Filer.root_dir+"/player_atlas.yml";
	
	public PlayerAtlas(ServerSystem pl) {
		pl.saveResource("player_atlas.yml", false);
	}
	
	public static UUID getUUID(String name) {
		File file = new File(atlas_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		UUID uuid = null;
		
		try {
			uuid = UUID.fromString(cfg.getString(name));
		}catch(NullPointerException ex) {
			uuid = null;
		}
		
		return uuid;
	}
	
	public static String getPlayername(String uuid) {
		File file = new File(atlas_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		return cfg.getString(uuid);
	}
	
	public static void savePlayer(Player p) {
		File file = new File(atlas_path);
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(getPlayername(p.getUniqueId().toString()) == null || getPlayername(p.getUniqueId().toString()).equals(p.getName()) == false) {
			cfg.set(p.getName(), p.getUniqueId().toString());
			cfg.set(p.getUniqueId().toString(), p.getName());
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			AccountManager.addMoney(p.getUniqueId(), 0);
		}
	}
	
}
