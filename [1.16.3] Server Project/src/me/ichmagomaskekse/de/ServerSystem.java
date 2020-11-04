package me.ichmagomaskekse.de;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.gameplay.teleporter.TeleporterManager;
import me.ichmagomaskekse.de.lobby.Lobby;
import me.ichmagomaskekse.de.lobby.commands.LobbyCommands;
import me.ichmagomaskekse.de.lobby.darkforge.DarkforgeManager;
import me.ichmagomaskekse.de.lobby.darkforge.EnchantHandler;
import me.ichmagomaskekse.de.lobby.darkforge.commands.DarkforgeCommands;
import me.ichmagomaskekse.de.lobby.darkforge.effects.EffectHandler;
import me.ichmagomaskekse.de.lobby.displays.DisplayManager;
import me.ichmagomaskekse.de.money.MoneyListener;
import me.ichmagomaskekse.de.money.commands.MoneyCommands;
import me.ichmagomaskekse.de.motd.MOTDManager;
import me.ichmagomaskekse.de.properties.Properties;

public class ServerSystem extends JavaPlugin {
	
	private static ServerSystem pl = null;
	public static ServerSystem getInstance() {
		return pl;
	}
	
	public static String prefix = "§5Fight The Darkness §7| ";
	public static Lobby lobby = null;
	public static DarkforgeManager darkforge_manager = null;
	public static boolean debug = false;
	
	@Override
	public void onEnable() {
		pl = this;
		
		preInit();
		init();
		postInit();
		
		this.getLogger().info("ServerSystem ist startklar!");
		broadcastMessage(true, "ServerSystem ist Startklar!");
				
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		darkforge_manager.closeAll();
		DisplayManager.removeAll();
		TeleporterManager.disable();
		EffectHandler.stopAll();
		super.onDisable();
	}
	
	public void preInit() {
		new Filer(false);
		
		new Properties(this);
		new PlayerAtlas(this);
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		lobby = new Lobby(this);
		
		new MOTDManager(this);
		new MoneyListener(this);
		
		new DisplayManager();
		
		darkforge_manager = new DarkforgeManager();
		new EnchantHandler();
		
		this.getCommand("lobby").setExecutor(new LobbyCommands());
		this.getCommand("setlobby").setExecutor(new LobbyCommands());
		this.getCommand("money").setExecutor(new MoneyCommands());
		this.getCommand("darkforge").setExecutor(new DarkforgeCommands());
	}
	
	public static void broadcastMessage(boolean onlyOps, String... s) {
		for(String msg : s) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(onlyOps) { if(p.isOp()) p.sendMessage(prefix+msg); }
				else p.sendMessage(prefix+msg);
			}
		}
	}
	
	public static void sendMessage(HumanEntity player, String... s) {
		sendMessage((Player)player, s);
	}
	public static void sendMessage(Player player, String... s) {
		for(String msg : s) {
			player.sendMessage(prefix+msg);
		}
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		if(sender.hasPermission(permission)) return true;
		else sender.sendMessage(ServerSystem.prefix+Properties.noPermission);
		return false;
	}
	
	
	
}
