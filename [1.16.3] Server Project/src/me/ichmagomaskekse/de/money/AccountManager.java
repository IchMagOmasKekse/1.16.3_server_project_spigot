package me.ichmagomaskekse.de.money;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.money.events.GeldAuszahlEvent;
import me.ichmagomaskekse.de.money.events.GeldEinzahlEvent;
import me.ichmagomaskekse.de.money.events.GeldVerschickenEvent;
import me.ichmagomaskekse.de.properties.Properties;

public class AccountManager {
	
	private static String home_path = "plugins/"+Filer.root_dir+"/Banking/";
	
	
	public static int getMoney(UUID uuid) {
		File file = new File(home_path+"accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		if(file.exists()) return cfg.getInt(uuid.toString()+".Money");
		else return 0;
	}
	
	public static boolean addMoney(UUID uuid, int anzahl) {
		File file = new File(home_path+"accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int current_money = 0;
		try{
			current_money = getMoney(uuid);
		}catch(Exception ex) {
			current_money = 0;
		}
		
		if(current_money+anzahl > Properties.moneyMaximum || current_money+anzahl < Properties.moneyMinimum) {
			Bukkit.getPluginManager().callEvent(new GeldEinzahlEvent(uuid, anzahl, false));
			return false;
		}
		
		cfg.set(uuid.toString()+".Money", current_money+anzahl);
		try {
			cfg.save(file);
			Bukkit.getPluginManager().callEvent(new GeldEinzahlEvent(uuid, anzahl, true));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getPluginManager().callEvent(new GeldEinzahlEvent(uuid, anzahl, false));
			return false;
		}
	}
	
	public static boolean subtractMoney(UUID uuid, int anzahl) {
		File file = new File(home_path+"accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int current_money = 0;
		try{
			current_money = getMoney(uuid);
		}catch(Exception ex) {
			current_money = 0;
		}
		if(current_money-anzahl < 1) return false;
		
		if(current_money-anzahl > Properties.moneyMaximum || current_money-anzahl < Properties.moneyMinimum) {
			Bukkit.getPluginManager().callEvent(new GeldAuszahlEvent(uuid, anzahl, false));
			return false;
		}
		
		cfg.set(uuid.toString()+".Money", current_money-anzahl);
		try {
			cfg.save(file);
			Bukkit.getPluginManager().callEvent(new GeldAuszahlEvent(uuid, anzahl, true));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getPluginManager().callEvent(new GeldAuszahlEvent(uuid, anzahl, false));
			return false;
		}
	}
	
	public static boolean removeMoney(UUID uuid, int anzahl) {
		File file = new File(home_path+"accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		int current_money = 0;
		current_money = getMoney(uuid);
		
		if(current_money-anzahl > Properties.moneyMaximum || current_money-anzahl < Properties.moneyMinimum) return false;
		
		cfg.set(uuid.toString()+".Money", current_money-anzahl);
		try {
			cfg.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean setMoney(UUID setter, UUID getter, int neuer_kontostand) {
		File file = new File(home_path+"accounts.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(neuer_kontostand > Properties.moneyMaximum || neuer_kontostand < Properties.moneyMinimum) {
			Bukkit.getPluginManager().callEvent(new GeldVerschickenEvent(setter, getter, neuer_kontostand, false));
			return false;
		}
		
		cfg.set(getter.toString()+".Money", neuer_kontostand);
		try {
			cfg.save(file);
			Bukkit.getPluginManager().callEvent(new GeldVerschickenEvent(setter, getter, neuer_kontostand, true));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			Bukkit.getPluginManager().callEvent(new GeldVerschickenEvent(setter, getter, neuer_kontostand, false));
			return false;
		}
	}
	
	private static boolean wasSuccessful = false;
	public static boolean sendMoney(UUID sender, UUID getter, int anzahl) {
		if(anzahl <= 0) {
			Bukkit.getPluginManager().callEvent(new GeldVerschickenEvent(sender, getter, anzahl, false));
			return false;
		}
		wasSuccessful = addMoney(sender, -anzahl);
		if(wasSuccessful == false)return wasSuccessful;
		wasSuccessful = addMoney(getter, anzahl);
		if(wasSuccessful == false) return wasSuccessful;	
		Bukkit.getPluginManager().callEvent(new GeldVerschickenEvent(sender, getter, anzahl, wasSuccessful));
		return wasSuccessful;
	}
	
	public static boolean requestMoney(UUID sender, UUID getter) {
		return true;
	}
	
}
