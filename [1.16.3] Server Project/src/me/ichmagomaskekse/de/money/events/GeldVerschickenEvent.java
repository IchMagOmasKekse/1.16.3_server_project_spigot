package me.ichmagomaskekse.de.money.events;

import java.util.UUID;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GeldVerschickenEvent extends Event {
	
	private static HandlerList handlers = new HandlerList();
	private UUID sender = null;
	private UUID getter = null;
	private int anzahl = 0;
	private boolean wasSuccess = false;

	public GeldVerschickenEvent(UUID sender, UUID getter, int anzahl, boolean wasSuccess) {
		this.sender = sender;
		this.getter = getter;
		this.anzahl = anzahl;
		this.wasSuccess = wasSuccess;
	}
	
	public UUID getSender() {
		return sender;
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
