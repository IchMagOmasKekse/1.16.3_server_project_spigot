package me.ichmagomaskekse.de.lobby.displays;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import me.ichmagomaskekse.de.properties.Properties;

public class UniversalDisplay extends Display {
	
	public UniversalDisplay(String text, Location loc) {
		super(Properties.lobby_spawn.getWorld().spawn(loc.clone(), ArmorStand.class), text);
	}

	@Override
	public void update() {
		this.display.setGravity(false);
		this.display.teleport(location.clone());
		this.display.setVelocity(this.static_velocity);
	}
	
}
