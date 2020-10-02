package me.ichmagomaskekse.de.properties;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.files.Filer;

public class Properties {
	
	private BukkitRunnable updater = null;
	private int updateDelay = 10; //Sekunden
	
	/* Alle Einstellungen die in dem Plugin benötigt werden */
	public static String motd, joinMessage, quitMessage, noPermission;
	public static int fakeSlots;
	public static boolean activateFakeSlots;
	public static Location lobby_spawn;
	
	public Properties(ServerSystem pl) {
		update();
		
		updater = new BukkitRunnable() {
			
			@Override
			public void run() {
				update();
			}
		};
		updater.runTaskTimer(pl, 0, updateDelay*20);
	}
	
	/*
	 * Aktualisiert alle Variablen(Fields).
	 * Die Methode MUSS immer auf dem neusten Stand gehalten werden!
	 */
	public void update() {
		motd = Filer.readString(Filer.config_path, "motd").replace("$NEWLINE$", "\n");
		joinMessage = Filer.readString(Filer.config_path, "joinMessage");
		quitMessage = Filer.readString(Filer.config_path, "quitMessage");
		noPermission = Filer.readString(Filer.config_path, "noPermission");
		
		fakeSlots = Filer.readInteger(Filer.config_path, "fakeSlots");
		
		activateFakeSlots = Filer.readBoolean(Filer.config_path, "activateFakeSlots");
		
		lobby_spawn = Filer.getLocation(Filer.lobby_path, "Spawn");
	}
	
}
