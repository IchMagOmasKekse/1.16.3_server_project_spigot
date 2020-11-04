package me.ichmagomaskekse.de.lobby.darkforge;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.lobby.displays.Display;
import me.ichmagomaskekse.de.lobby.displays.UniversalDisplay;

public class Darkforge implements Listener {
	
	public int forge_id = 0;
	public int forge_level = 0;
	
	private Location loc = null;
	private Material material = Material.RESPAWN_ANCHOR;
	private int anchor_charge = 3;
	private int current_obsidians = 0; //Beeinflusst die Stärke der Verzauberung
	private Item display = null;
	private Vector vector = new Vector(0,0,0);
	public boolean show_obsidian_range = false;
	public Location composter_loc = null;
	
	/* Souls */
	public Display soul_display = null;
	public int souls = 0;
	public String soul_displayname = "§bSeele";
	
	private BukkitRunnable updater = null;
	
	public Darkforge(int forge_id, Location loc) {
		ServerSystem.getInstance().getServer().getPluginManager().registerEvents(this, ServerSystem.getInstance());
		this.forge_id = forge_id;
		this.loc = loc;
		this.composter_loc = new Location(loc.getWorld(), 0, 97, 24);
		composter_loc.getBlock().setType(Material.COMPOSTER);
		composter_loc.getBlock().setBlockData(Material.COMPOSTER.createBlockData());
		souls = Filer.getSouls();
		soul_display = new UniversalDisplay("§b{SOULS} Seelen", composter_loc.clone().add(0.5,0.5,0.5));
		soul_display.replaceParamsInDisplayText("{SOULS}", souls+"");
		setup();
	}
	
	public void setup() {
		loc.getBlock().setType(material);
		loc.getBlock().setBlockData(material.createBlockData());
		display = loc.getWorld().dropItem(loc.clone().add(0.5,1.1,0.5), new ItemStack(Material.CRYING_OBSIDIAN));
		display.setGravity(false);
		display.setVelocity(vector);
		display.setPickupDelay(Integer.MAX_VALUE);
		display.setCustomName("§5Dunkelschmiede");
		display.setCustomNameVisible(true);
		
		updater = new BukkitRunnable() {
			
			@Override
			public void run() {
				current_obsidians = checkForObsidians();
				forge_level = (int)(current_obsidians / 2.2) /* 2.2 weil es 22 Obsidians möglich sind und diese 22 müssen auf 10 Level verteilt werden. 22 / 10 = 2.2 */;
				display.setGravity(false);
				display.setVelocity(vector);
				display.setTicksLived(1);
				display.setPickupDelay(Integer.MAX_VALUE);
				display.teleport(loc.clone().add(0.5,1.2,0.5));
				if(loc.getBlock() instanceof RespawnAnchor) {
					((RespawnAnchor)loc.getBlock()).setCharges(anchor_charge);
				}
			}
		};
		updater.runTaskTimer(ServerSystem.getInstance(), 0, 10l);
	}
	
	public void close() {
		updater.cancel();
		display.remove();
		soul_display.disable();
		loc.getBlock().setType(Material.AIR);
		Filer.setSouls(souls);
	}
	
	
	/*
	 * Diese Methode zählt die Obsidians,
	 * die um der Dunkelschmiede herum sind,
	 * wie der Zaubertisch die Bücherregale
	 */
	public int checkForObsidians() {
		int x,y,z,obsidians;
		
		obsidians = 0;
		for(y = -1; y != 4; y++) {
			for(x = -2; x != 3; x++) {
				for(z = -2; z != 3; z++) {
					if(loc.clone().add(x,y,z).getBlock().getType() == Material.CRYING_OBSIDIAN) {
						obsidians++;
					}else {
						if(show_obsidian_range) Bukkit.getWorld("world").spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(x+0.5,y+0.5,z+0.5), 0, 0, 0, 1);						
					}
				}
			}
			
		}
		return obsidians;
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		/* Seelen auffüllen */
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK) {			
			if(e.getClickedBlock().getType() == Material.COMPOSTER) {
				if(e.getItem().getType() == Material.TWISTING_VINES) {
					if(e.getItem() != null && 
							e.getItem().hasItemMeta() &&
							e.getItem().getItemMeta().hasDisplayName() &&
							e.getItem().getItemMeta().getDisplayName().equals(soul_displayname) || e.getPlayer().getGameMode() == GameMode.CREATIVE) {
						souls++;
						soul_display.replaceParamsInDisplayText("{SOULS}", souls+"");
						Filer.setSouls(souls);
						if(e.getPlayer().getGameMode() != GameMode.CREATIVE) {
							if(e.getItem().getAmount()-1 <= 0) e.getItem().setAmount(0);
							else e.getItem().setAmount(e.getItem().getAmount()-1);
						}
					}
					e.setCancelled(true);
				}
			}
		}
	}

	public void useSouls(int amount) {
		souls-=amount;
		soul_display.replaceParamsInDisplayText("{SOULS}", souls+"");
	}
	
	public Location getLocation() {
		return loc.clone();
	}

	public void setLoc(Location loc) {
		this.loc = loc.clone();
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public int getAnchor_charge() {
		return anchor_charge;
	}

	public void setAnchor_charge(int anchor_charge) {
		this.anchor_charge = anchor_charge;
	}

	public int getCurrent_obsidians() {
		return current_obsidians;
	}

	public void setCurrent_obsidians(int current_obsidians) {
		this.current_obsidians = current_obsidians;
	}

	public Item getDisplay() {
		return display;
	}

	public void setDisplay(Item display) {
		this.display = display;
	}
	
	
	
}
