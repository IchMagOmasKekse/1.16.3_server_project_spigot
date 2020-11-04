package me.ichmagomaskekse.de.lobby.displays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.lobby.Lobby;
import me.ichmagomaskekse.de.properties.Properties;

public class DarkforgeInfoDisplay extends Display {

	public DarkforgeInfoDisplay() {
		super(Properties.lobby_spawn.getWorld().spawn(new Location(Bukkit.getWorld("world"), 0.5, 97.5, 8.1), ArmorStand.class), "§5Dunkelschmieden-Level: §f{DARKFORGE_LEVEL}");
		show();
	}

	@Override
	public void update() {
		replaceParamsInDisplayText("{DARKFORGE_LEVEL}", ""+ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).forge_level+"("+ServerSystem.darkforge_manager.darkforges.get(Lobby.darkforge_id).getCurrent_obsidians()+" Obsidian)");
	}
	
}
