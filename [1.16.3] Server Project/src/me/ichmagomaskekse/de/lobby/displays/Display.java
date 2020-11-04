package me.ichmagomaskekse.de.lobby.displays;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.util.Vector;

public abstract class Display {
	
	public int id = 0;
	protected ArmorStand display = null;
	protected Location location = null;
	protected String text = "";
	protected String current_text = "";
	protected Vector static_velocity = new Vector(0,0,0);
	protected double usedXOffset = 0d;
	protected double usedYOffset = 0d;
	protected double usedZOffset = 0d;
	
	public Display(ArmorStand display, String text) {
		display.setGravity(false);
		display.setVelocity(static_velocity);
		display.setCollidable(false);
		display.setBasePlate(false);
		display.setCustomNameVisible(true);
		display.setCustomName(text);
		display.setRemoveWhenFarAway(false);
		display.setVisible(false);
		display.setSmall(true);
		this.display = display;
		this.location = display.getLocation();
		this.text = text;
		this.current_text = text;
		this.id = DisplayManager.createNewId();
	}
	
	public void disable() {
		display.remove();
	}
	
	public void replaceParamsInDisplayText(String param, String value) {
		display.setCustomName(text.replace(param, value));
		current_text = display.getCustomName();
	}
	
	public abstract void update();
	
	public void hide() {
		display.setCustomNameVisible(false);
	}
	public void show() {
		display.setCustomNameVisible(true);
		display.setCustomName(current_text);
	}
	
	public void setPositionToTop() {
		if(display.isSmall()) {
			usedXOffset = 0;
			usedYOffset = 0.7;
			usedZOffset = 0;
			location.add(usedXOffset, usedYOffset, usedZOffset);
		}else {
			usedXOffset = 0;
			usedYOffset = 1.7;
			usedZOffset = 0;
			location.add(usedXOffset, usedYOffset, usedZOffset);
		}
		display.teleport(location);
	}
	
	public void setPositionToMiddle() {
		usedXOffset = (usedXOffset * (-1));
		usedYOffset = (usedYOffset * (-1));
		usedZOffset = (usedZOffset * (-1));
		location.add(usedXOffset, usedYOffset, usedZOffset);
		usedXOffset = 0;
		usedYOffset = 0;
		usedZOffset = 0;
		display.teleport(location);
	}
	
	public void setPositionToBottum() {
		if(display.isSmall()) {
			usedXOffset = 0;
			usedYOffset = -0.7;
			usedZOffset = 0;
			location.add(usedXOffset, usedYOffset, usedZOffset);
		}else {
			usedXOffset = 0;
			usedYOffset = -1.7;
			usedZOffset = 0;
			location.add(usedXOffset, usedYOffset, usedZOffset);
		}
		display.teleport(location);
	}
	
}
