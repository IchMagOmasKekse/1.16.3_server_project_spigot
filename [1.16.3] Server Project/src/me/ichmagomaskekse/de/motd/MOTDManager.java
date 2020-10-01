package me.ichmagomaskekse.de.motd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.properties.Properties;

public class MOTDManager implements Listener {
	
	public MOTDManager(ServerSystem pl) {
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent e) {
		e.setMotd(Properties.motd);
		if(Properties.activateFakeSlots) e.setMaxPlayers(Properties.fakeSlots);
	}
	
}
