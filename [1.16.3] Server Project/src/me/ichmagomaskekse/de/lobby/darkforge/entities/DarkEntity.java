package me.ichmagomaskekse.de.lobby.darkforge.entities;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import me.ichmagomaskekse.de.lobby.darkforge.effects.Effect;

public abstract class DarkEntity {
	
	protected Entity entity = null;
	protected Entity parent = null;
	protected Effect effect = null;
	protected Location location = null;
	
	public DarkEntity(Entity entity, Entity parent, Location location) {
		this.entity = entity;
		this.parent = parent;
		this.location = location;
	}
	
	public abstract void destroy();
	public abstract void remove();
	public abstract void update();
	public abstract void setLightlevel(double lightlevel);
	public abstract void setSanity(double sanity);
	
	
	
}
