package me.ichmagomaskekse.de.gameplay.teleporter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.animation.Animation;
import me.ichmagomaskekse.de.lobby.displays.Display;
import me.ichmagomaskekse.de.lobby.displays.UniversalDisplay;

public abstract class Teleporter {
	
	public TeleporterType teleporter_type = TeleporterType.UNDEFINED;
	public Location spot = new Location(Bukkit.getWorld("world"), 0, 96, 14, 0, -6); //Wo ist die Druckplatte?
	public String destinyDisplayname = "Unbenannter Teleporter"; //Name des Teleporters
	public LinkedList<Float> destinies = null; //ID des Ziel-Teleporters
	public int destiny_index = 0; //Index aus der Liste 'destinies'. Dieser Index gibt an, welcher Teleporter aus der Liste fgewählt wird
	public float destiny_id = 0f;
	public boolean canReceivePassengers = false; //Kann der Teleporter im Moment Spieler empfangen?
	public boolean hasPassengers = false; //Sind Spieler auf der Druckplatte?
	public boolean enabledWhenNoPassengers = false, enabled = true; /* Aktiviert den Teleporter wieder,
	sobald kein Spieler mehr auf der Druckplatte ist.
	Wird verwendet um ein direkten Start des Ziel Teleportes zu verhindern, nachdem man zum Ziel-Teleporter teleportiert wurde */
	private boolean skipEnabledWhenNoPassengers = false;
	public ConcurrentLinkedQueue<Player> passengers = new ConcurrentLinkedQueue<Player>(); //Spieler, die auf der Druckplatte stehen
	public int triggerVoltage = 1; /* Ab welcher Spannung wird der Teleporter getriggert? */
	public int crashVoltage = 15;
	public Display display = null; /* Sichtbares Display zum anzeigen eines Textes
	Wird ebenfalls benutzt, um zu detekten, welche Spieler sich im Teleport-Bereich befinden.*/
	public boolean isTeleporting = false; /* Sagt aus, ob der Teleportprozess gerade läuft, oder nicht */
	
	/* Checker */
	private double range_x = 0.1; //X Range der Hitbox zum triggern des Teleporters
	private double range_y = 0.1; //Y Range der Hitbox zum triggern des Teleporters
	private double range_z = 0.1; //Z Range der Hitbox zum triggern des Teleporters
	
	/* Animation */
	protected Animation animation = null;
	
	public Teleporter(TeleporterType type, Location spot) {
		this.teleporter_type = type;
		this.destinies = type.getTargets();
		if(setDestiny(0) == false) ServerSystem.broadcastMessage(true,
				"§4§lERROR: §cServerSystem[Teleporter.class{Constructor setDestiny();}]"
				+ "\nDie festgelegten Ziele beinhalten nicht den hier zum destiny_id festgelegtem Ziel."
				+ "\n§7§nDeveloper Hilfe erforderlich!"
				+ "\n§f---------------------------------------------");
		this.spot = spot;
		
		if(destinyDisplayname.equals("")) display = new UniversalDisplay("§cKeine Verbindung", getSpot().add(0.5, 0, 0.5));
		else display = new UniversalDisplay("§7-> "+destinyDisplayname+" §7<-", getSpot().add(0.5, 0, 0.5));
		spot.getBlock().setType(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
		
	}
	
	
	public abstract void tick();
	public abstract void doAnimation();
	public abstract void resetAnimation();
	public abstract void startTeleporprocess();
	
	public boolean setDestiny(int destiny_index) {
		if(destinies.size() < destiny_index) return false;
		else {
			this.destiny_index = destiny_index;
			this.destiny_id = destinies.get(destiny_index);
			/* Update den Displaytext */
			this.destinyDisplayname = TeleporterType.getByAddress(destinies.get(destiny_index)).getDisplayname();
			return true;
		}
	}
	
	public void shutdown() {
		/*
		 * Shutdown wird beim ausschalten des Plugins getriggert.
		 * Es löscht alle Entitäten um sie beim Einschalten des Plugins wieder zu erzeugen
		 */
		display.disable();
		getSpot().getBlock().setType(Material.AIR);
		resetAnimation();	
	}
	
	public void updatePassengers() {
		ArrayList<Entity> ents = new ArrayList<Entity>();
		
		if(skipEnabledWhenNoPassengers == false) {			
			for(Entity ent : display.getDisplay().getNearbyEntities(range_x, range_y, range_z)) {
				if(ent instanceof Player) {
					ents.add(ent);
					if(passengers.contains(((Player)ent)) == false) {
						((Player)ent).playSound(getSpot(), Sound.BLOCK_NOTE_BLOCK_GUITAR, 1f, 1f);
						passengers.add(((Player)ent));
						if(passengers.size() == 1 && enabled == true && enabledWhenNoPassengers == false && skipEnabledWhenNoPassengers == false) {
							startTeleporprocess();
						}
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
		}else if(passengers.isEmpty() && enabledWhenNoPassengers && !skipEnabledWhenNoPassengers) {
			enabled = true;
			enabledWhenNoPassengers = false;
		}
		
		if(enabledWhenNoPassengers && skipEnabledWhenNoPassengers) skipEnabledWhenNoPassengers = false;
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
		if(enabled == true && enabledWhenNoPassengers == false && skipEnabledWhenNoPassengers == false) {			
			if(passengers.isEmpty() == true) return;
			Teleporter destiny = TeleporterManager.getTeleporterById(destinies.get(destiny_index));
			for(Player t : passengers) {
				destiny.enabled = false;
				destiny.enabledWhenNoPassengers = true;
				t.teleport(destiny.getSpot().add(0.5,0,0.5));
				destiny.skipEnabledWhenNoPassengers = true;
			}
		}
	}
	
	public void nextDestiny() {
		/*
		 * Diese Methode rotiert die Destinies und wählt das nächste Ziel aus
		 */
		destiny_index+=1;
		if(destiny_index == (destinies.size())) destiny_index = 0;
		setDestiny(destiny_index);
		display.updateText("§7-> "+destinyDisplayname+" §7<-");
		
	}
	
	public Location getSpot() {
		return spot.clone();
	}
	public float getAddress() {
		return teleporter_type.getAddress();
	}
	public Display getDisplay() {
		return this.display;
	}
	
	public static enum TeleporterType {
		UNDEFINED(  0f, "undefined", "Undefiniert"),
		WERKSTATT(1.0f, "werkstatt", "§3Werkstatt"),
		SAFEHOUSE(2.0f, "safehouse", "§aSafehouse"),
		GENERATOR(3.0f, "generator", "§cGenerator"),
		TESTFIELD(4.0f, "testfield", "§eTestgelände");
		
		private float address = 0;
		private String codename, displayname;
		private LinkedList<Float> targets;
		
		TeleporterType(float address, String codename, String displayname) {
			this.address = address;
			this.codename = codename;
			this.displayname = displayname;
		}
		
		public float getAddress() {
			return address;
		}
		public String getCodename() {
			return codename;
		}
		public String getDisplayname() {
			return displayname;
		}
		@SuppressWarnings("unchecked")
		public LinkedList<Float> getTargets() {
			if(targets == null) targets = reloadTargets(codename);
			if(targets.contains(this.getAddress())) targets.remove(this.getAddress());
			return (LinkedList<Float>) targets.clone();
		}
		
		public static LinkedList<Float> reloadTargets(String codename) {
			/* Hier werden die Ziele definiert, zu dem jeder einzelne Teleporter sich teleportieren kann */
			LinkedList<Float> targets = new LinkedList<Float>();
			switch(codename) {
			case "undefined":
				targets.add(TeleporterType.UNDEFINED.getAddress());
				break;
			case "generator":
				targets.add(SAFEHOUSE.getAddress());
				break;
			case "safehouse":
				targets.add(WERKSTATT.getAddress());
				targets.add(GENERATOR.getAddress());
				break;
			case "testfield":
				targets.add(WERKSTATT.getAddress());
				targets.add(GENERATOR.getAddress());
				targets.add(SAFEHOUSE.getAddress());
				break;
			case "werkstatt":
				targets.add(SAFEHOUSE.getAddress());
				targets.add(GENERATOR.getAddress());
				targets.add(TESTFIELD.getAddress());
				break;
			}
			return targets;
		}
		
		public static TeleporterType getByAddress(float address) {
			for(TeleporterType type : TeleporterType.values()) {
				if(type.getAddress() == address) return type;
			}
			return TeleporterType.UNDEFINED;
		}
		
	}
	
}