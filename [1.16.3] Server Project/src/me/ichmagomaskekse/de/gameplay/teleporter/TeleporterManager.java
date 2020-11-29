package me.ichmagomaskekse.de.gameplay.teleporter;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.ServerSystem;

public class TeleporterManager implements Listener {
	
	private static BukkitRunnable timer = null;
	private static HashMap<Float, Teleporter> teleporters = new HashMap<Float, Teleporter>();

	
	public TeleporterManager() {
		ServerSystem.getInstance().getServer().getPluginManager().registerEvents(this, ServerSystem.getInstance());
		
		WerkstattTeleporter wt = new WerkstattTeleporter(new Location(Bukkit.getWorld("world"), 0, 96, 14, 0, -6));
		registrateNewTeleporter(wt);
		
		SafehouseTeleporter st = new SafehouseTeleporter(new Location(Bukkit.getWorld("world"), 230, 43, 58, -87, 26));
		registrateNewTeleporter(st);
		
		GeneratorTeleporter gt = new GeneratorTeleporter(new Location(Bukkit.getWorld("world"), 184, 32, 54, -87, 26));
		registrateNewTeleporter(gt);
		
		TestfieldTeleporter tt = new TestfieldTeleporter(new Location(Bukkit.getWorld("world"), 326, 33, 54, 180, 3.2f));
		registrateNewTeleporter(tt);
		
		timer = new BukkitRunnable() {
			@Override public void run() {
				updateTeleporters();
			}
		};
		timer.runTaskTimer(ServerSystem.getInstance(), 0, 1l);
	}
	
	public static void disable() {
		if(teleporters.isEmpty() == false) {
			for(float i : teleporters.keySet()) {
				teleporters.get(i).shutdown();
			}
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractAtEntityEvent e) {
		if(e.getRightClicked() instanceof ArmorStand) {
			for(float f : teleporters.keySet()) {
				Teleporter t = teleporters.get(f);
				if(e.getRightClicked() == t.getDisplay().getDisplay()) {
					t.nextDestiny();
					e.getPlayer().getWorld().playSound(e.getRightClicked().getLocation(), Sound.UI_BUTTON_CLICK, 6f, 0f);
				}
			}
		}
	}

	public static Teleporter getTeleporterById(float address) {
		return teleporters.get(address);
		
	}
	
	private static void updateTeleporters() {
		if(teleporters.isEmpty() == false) {
			for(float i : teleporters.keySet()) {
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
		if(teleporters.containsKey(teleporter.getAddress())) return false;
		else teleporters.put(teleporter.getAddress(), teleporter);
		return true;
	}
	public static boolean unregisterTeleporter(float address) {
		if(teleporters.containsKey(address) == false) return false;
		else teleporters.remove(address);
		return true;
	}
	
}
