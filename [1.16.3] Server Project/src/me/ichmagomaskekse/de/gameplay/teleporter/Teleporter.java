package me.ichmagomaskekse.de.gameplay.teleporter;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.ichmagomaskekse.de.animation.Animation;
import me.ichmagomaskekse.de.lobby.displays.Display;
import me.ichmagomaskekse.de.lobby.displays.UniversalDisplay;

public abstract class Teleporter {
	
	public Location spot = new Location(Bukkit.getWorld("world"), 0, 96, 14, 0, -6); //Wo ist die Druckplatte?
	public String name = "Unbenannter Teleporter"; //Name des Teleporters
	public int id = 0; //ID des Teleporters
	public int destiny_id = 1; //ID des Ziel-Teleporters
	public boolean canReceivePassengers = false; //Kann der Teleporter im Moment Spieler empfangen?
	public boolean hasPassengers = false; //Sind Spieler auf der Druckplatte?
	public boolean enabledWhenNoPassengers = false, enabled = true; /* Aktiviert den Teleporter wieder,
	sobald kein Spieler mehr auf der Druckplatte ist.
	Wird verwendet um ein direkten Start des Ziel Teleportes zu verhindern, nachdem man zum Ziel-Teleporter teleportiert wurde */
	public ConcurrentLinkedQueue<Player> passengers = new ConcurrentLinkedQueue<Player>(); //Spieler, die auf der Druckplatte stehen
	public int triggerVoltage = 1; /* Ab welcher Spannung wird der Teleporter getriggert? */
	public int crashVoltage = 15;
	public ArmorStand checker = null; /* Checker ist zum überprüfen, ob ein Spieler den Teleporter benutzen möchte, also im Teleportbereich steht. */
	public Display display = null; /* Sichtbares Display zum anzeigen eines Textes */
	public boolean isTeleporting = false; /* Sagt aus, ob der Teleportprozess gerade läuft, oder nicht */
	
	/* Checker */
	private double range_x = 0.1; //X Range der Hitbox zum triggern des Teleporters
	private double range_y = 0.1; //Y Range der Hitbox zum triggern des Teleporters
	private double range_z = 0.1; //Z Range der Hitbox zum triggern des Teleporters
	
	/* Animation */
	protected Animation animation = null;
	
	public Teleporter(int id, String name, int destiny_id, Location spot) {
		this.id = id;
		this.name = name;
		this.destiny_id = destiny_id;
		this.spot = spot;
		
		checker = getSpot().getWorld().spawn(getSpot().add(0.5,0,0.5), ArmorStand.class);
		checker.setVisible(false);
		checker.setGravity(false);
		checker.setVelocity(new Vector(0,0,0));
		checker.setCollidable(false);
		
		display = new UniversalDisplay(name, getSpot().add(0.5, 0, 0.5));
		
		spot.getBlock().setType(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
		
	}
	
	
	public abstract void tick();
	public abstract void doAnimation();
	public abstract void resetAnimation();
	public abstract void startTeleporprocess();
	
	public void shutdown() {
		/*
		 * Shutdown wird beim ausschalten des Plugins getriggert.
		 * Es löscht alle Entitäten um sie beim Einschalten des Plugins wieder zu erzeugen
		 */
		checker.remove();
		display.disable();
		getSpot().getBlock().setType(Material.AIR);
		resetAnimation();	
	}
	
	public void updatePassengers() {
		ArrayList<Entity> ents = new ArrayList<Entity>();
		for(Entity ent : checker.getNearbyEntities(range_x, range_y, range_z)) {
			if(ent instanceof Player) {
				ents.add(ent);
				if(passengers.contains(((Player)ent)) == false) {
					((Player)ent).playSound(getSpot(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1f, 1f);
					passengers.add(((Player)ent));
					if(passengers.size() == 1) {
						startTeleporprocess();
					}
				}
			}
		}
		if(passengers.isEmpty() == false) {
			for(Player p : passengers) {
				if(ents.contains(p) == false) {
					p.playSound(getSpot(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 1f);
					passengers.remove(p);
					resetAnimation();
				}
			}
			/* Aktiviere den Teleporter, wenn die Funktion 'Aktivieren, wenn keine Passagiere vorhanden sind' aktiviert ist. */
		}else if(enabledWhenNoPassengers) {
			enabled = true;
			enabledWhenNoPassengers = false;
		}
	}
	
	public void checkVoltage() {
		/*
		 * Überprüft die Aktuelle Spannung der Druckplatte. Triggert, wenn die minimale Trigger-Spannung erreicht wurde
		 */
		if(getSpot().getBlock().getType() == Material.LIGHT_WEIGHTED_PRESSURE_PLATE && getSpot().getBlock().getBlockPower() >= triggerVoltage);
		
	}
	
	public void cancel() {
		/*
		 * Bricht den kompletten Vorgang ab, der bisher vom Teleporter angefangen wurde
		 *
		 * ACHTUNG
		 * Diese Methode muss immer aktuell bleiben, um Bugs zu vermeiden!
		 */
		
	}
	public void teleport() {
		/*
		 * Teleportiert alle passenger zum Ziel-Teleporter
		 */
		if(passengers.isEmpty() == true) return;
		Teleporter destiny = TeleporterManager.getTeleporterById(destiny_id);
		for(Player t : passengers) {
			destiny.enabled = false;
			destiny.enabledWhenNoPassengers = true;
			t.teleport(destiny.getSpot().add(0.5,0,0.5));
		}
	}
	
	public Location getSpot() {
		return spot.clone();
	}
	
}
