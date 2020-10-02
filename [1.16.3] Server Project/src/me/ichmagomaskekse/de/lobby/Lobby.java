package me.ichmagomaskekse.de.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.properties.Properties;

public class Lobby implements Listener {
	
	
	public Lobby(ServerSystem pl) {
		pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	
	public void setSpawn() {
		/*
		 * Ist aktuell darauf eingestellt, die Location von IchMagOmasKekse als Spawn zu setzen
		 */
		Location loc = Bukkit.getPlayer("IchMagOmasKekse").getLocation();
		if(Filer.setLocation(Filer.lobby_path, "Spawn", loc)) ServerSystem.broadcastMessage(true, "§aEin neuer Lobby-Spawn wurde gesetzt!");
		else  ServerSystem.broadcastMessage(true, "§cEin neuer Lobby-Spawn konnte nicht gesetzt werden!");
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Properties.lobby_spawn != null) e.getPlayer().teleport(Properties.lobby_spawn);
		else ServerSystem.broadcastMessage(true, "Der Lobby-Spawn wurde noch nicht gesetzt!");
		e.setJoinMessage(Properties.joinMessage.replace("$USERNAME$", e.getPlayer().getName()));
	}
	
	@EventHandler
	public void onJoin(PlayerQuitEvent e) {
		e.setQuitMessage(Properties.quitMessage.replace("$USERNAME$", e.getPlayer().getName()));
	}
	
}
