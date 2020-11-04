package me.ichmagomaskekse.de.gameplay.teleporter;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.ServerSystem;

public class TeleporterManager {
	
	private static BukkitRunnable timer = null;
	private static HashMap<Integer, Teleporter> teleporters = new HashMap<Integer, Teleporter>();
	
	public TeleporterManager() {
		LobbyToBattlefieldTeleporter lb = new LobbyToBattlefieldTeleporter(
				0, //ID
				"§7-> §b§nSafehouse §7<-", //Name
				1, //Ziel
				new Location(Bukkit.getWorld("world"), 0, 96, 14, 0, -6) //Spot
				);
		
		registrateNewTeleporter(lb);
		
		BattlefieldToLobbyTeleporter bl = new BattlefieldToLobbyTeleporter(
				1, //ID
				"§7-> §b§nWerkstatt §7<-", //Name
				0, //Ziel
				new Location(Bukkit.getWorld("world"), 230, 43, 58, -87, 26) //Spot
				);
		
		registrateNewTeleporter(bl);
		
		BossToLobbyTeleporter bbl = new BossToLobbyTeleporter(
				2, //ID
				"§7-> §c§nFlüchten §7<-", //Name
				0, //Ziel
				new Location(Bukkit.getWorld("world"), 184, 32, 54, -87, 26) //Spot
				);
		
		registrateNewTeleporter(bbl);
		
		timer = new BukkitRunnable() {
			@Override public void run() {
				updateTeleporters();
			}
		};
		timer.runTaskTimer(ServerSystem.getInstance(), 0, 1l);
	}
	
	public static void disable() {
		if(teleporters.isEmpty() == false) {
			for(int i : teleporters.keySet()) {
				teleporters.get(i).shutdown();
			}
		}
	}
	
	public static Teleporter getTeleporterById(int id) {
		return teleporters.get(id);
		
	}
	
	private static void updateTeleporters() {
		if(teleporters.isEmpty() == false) {
			for(int i : teleporters.keySet()) {
				teleporters.get(i).updatePassengers();
				teleporters.get(i).checkVoltage();
				teleporters.get(i).tick();
			}
		}
	}
	
	public static void loadAllTeleporters() {
		//TODO:
	}
	
	public static void saveTeleporters() {
		//TODO:
	}
	
	public static boolean registrateNewTeleporter(Teleporter teleporter) {
		if(teleporters.containsKey(teleporter.id)) return false;
		else teleporters.put(teleporter.id, teleporter);
		return true;
	}
	public static boolean unregisterTeleporter(int id) {
		if(teleporters.containsKey(id) == false) return false;
		else teleporters.remove(id);
		return true;
	}
	
}
