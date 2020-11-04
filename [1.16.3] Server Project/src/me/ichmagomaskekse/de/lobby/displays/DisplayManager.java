package me.ichmagomaskekse.de.lobby.displays;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.ServerSystem;

public class DisplayManager extends BukkitRunnable {
	
	public static Random ran = new Random();
	public static ConcurrentHashMap<Integer, Display> displays = new ConcurrentHashMap<Integer, Display>();
	private static boolean loaded = false;
	
	public DisplayManager() {
		loadDisplays();
		runTaskTimer(ServerSystem.getInstance(), 0, 10l);
	}
	
	@Override
	public void run() {
		for(int i : displays.keySet()) {
			displays.get(i).update();
		}
	}
	
	public static boolean registerDisplay(Display display) {
		if(displays.containsKey(display.id)) return false;
		else displays.put(display.id, display);
		return true;
	}
	
	public static void loadDisplays() {
		if(loaded) return;
		
		Display display = new MoneyDisplay();
		displays.put(display.id, display);
		
		display = new DarkforgeInfoDisplay();
		displays.put(display.id, display);
		
		loaded = true;
	}
	
	public static void removeAll() {
		for(Display d : displays.values()) d.disable();
		
		loaded = false;
	}
	
	public static int createNewId() {
		return ran.nextInt(1000);
	}
	
}
