package me.ichmagomaskekse.de.lobby;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.ichmagomaskekse.de.PlayerAtlas;
import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.gameplay.teleporter.TeleporterManager;
import me.ichmagomaskekse.de.lobby.darkforge.Darkforge;
import me.ichmagomaskekse.de.lobby.darkforge.DarkforgeManager;
import me.ichmagomaskekse.de.lobby.darkforge.inventories.ForgerInv;
import me.ichmagomaskekse.de.lobby.displays.DisplayManager;
import me.ichmagomaskekse.de.lobby.displays.UniversalDisplay;
import me.ichmagomaskekse.de.properties.Properties;

public class Lobby implements Listener {
	
	private ArrayList<Player> cooldown = new ArrayList<Player>();
	public Player player = null;
	public static int darkforge_id = DisplayManager.createNewId();
	
	public Lobby(ServerSystem pl) {
		pl.getServer().getPluginManager().registerEvents(this, pl);
		if(Bukkit.getPlayer("IchMagOmasKekse") != null)player = Bukkit.getPlayer("IchMagOmasKekse");
		if(Bukkit.getPlayer("DaumenRunter") != null)player = Bukkit.getPlayer("DaumenRunter");
		
		/* Laherhallen Display erzeugen */
		UniversalDisplay ud = new UniversalDisplay("§fStatus: §2Intakt", new Location(Bukkit.getWorld("world"), 249.5, 42, 57.5));
		DisplayManager.registerDisplay(ud);
		ud = new UniversalDisplay("§3Lagerhalle", new Location(Bukkit.getWorld("world"), 249.5, 42.5, 57.5));
		DisplayManager.registerDisplay(ud);
		
		new TeleporterManager();
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
	public void onClick(PlayerInteractEvent e) {
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.RESPAWN_ANCHOR) {
			if(e.getClickedBlock().getRelative(BlockFace.NORTH).getType() == Material.WATER &&
					e.getClickedBlock().getRelative(BlockFace.EAST).getType() == Material.WATER &&
					e.getClickedBlock().getRelative(BlockFace.SOUTH).getType() == Material.WATER &&
					e.getClickedBlock().getRelative(BlockFace.WEST).getType() == Material.WATER) {
				
			}else {
				for(Darkforge d : ServerSystem.darkforge_manager.darkforges.values()) {
					Location loc = d.getLocation();
					Material material = d.getMaterial();
					if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
						if(e.getClickedBlock().getLocation().getX() == loc.getX() &&
								e.getClickedBlock().getLocation().getY() == loc.getY() &&
								e.getClickedBlock().getLocation().getZ() == loc.getZ() && e.getClickedBlock().getType() == material) {
							
							if(cooldown.contains(e.getPlayer())) {
								cooldown.remove(e.getPlayer());
								return;
							}else cooldown.add(e.getPlayer());
							ForgerInv inv = new ForgerInv(d, e.getPlayer());

							inv.setItemToEnchant(new ItemStack(Material.AIR));
							if(DarkforgeManager.inventories.containsKey(e.getPlayer()) == false)DarkforgeManager.inventories.put(e.getPlayer(), inv);
							inv.openInventory(e.getPlayer());
							
						}
					}
				}
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if(e.getView().getTitle().equals("§8Dunkelschmiede")) {
			if(DarkforgeManager.inventories.get(e.getPlayer()) != null && 
					DarkforgeManager.inventories.get(e.getPlayer()).getItemToEnchant() != null)
				e.getPlayer().getInventory().addItem(DarkforgeManager.inventories.get(e.getPlayer()).getItemToEnchant());
			DarkforgeManager.inventories.get(e.getPlayer()).cancel();
			DarkforgeManager.inventories.remove(e.getPlayer());
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if(Properties.lobby_spawn != null) e.getPlayer().teleport(Properties.lobby_spawn);
		else ServerSystem.broadcastMessage(true, "Der Lobby-Spawn wurde noch nicht gesetzt!");
		e.setJoinMessage(Properties.joinMessage.replace("$USERNAME$", e.getPlayer().getName()));
		PlayerAtlas.savePlayer(e.getPlayer());
		
		player = e.getPlayer();
	}
	
	@EventHandler
	public void onJoin(PlayerQuitEvent e) {
		e.setQuitMessage(Properties.quitMessage.replace("$USERNAME$", e.getPlayer().getName()));
	}
	
}
