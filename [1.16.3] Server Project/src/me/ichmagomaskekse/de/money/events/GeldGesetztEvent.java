package me.ichmagomaskekse.de.money.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GeldGesetztEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	private UUID setter = null;
	private UUID getter = null;
	private int anzahl = 0;
	private boolean wasSuccess = false;

	public GeldGesetztEvent(UUID setter, UUID getter, int anzahl, boolean wasSuccess) {
		this.setter = setter;
		this.getter = getter;
		this.anzahl = anzahl;
		this.wasSuccess = wasSuccess;
	}
	
	public UUID getSetter() {
		return setter;
	}
	public UUID getGetter() {
		return getter;
	}
	public int getAnzahl() {
		return anzahl;
	}
	public boolean wasSuccess() {
		return wasSuccess;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
}
