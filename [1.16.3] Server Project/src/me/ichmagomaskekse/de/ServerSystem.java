package me.ichmagomaskekse.de;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.lobby.Lobby;
import me.ichmagomaskekse.de.motd.MOTDManager;
import me.ichmagomaskekse.de.properties.Properties;

public class ServerSystem extends JavaPlugin {
	
	private static ServerSystem pl = null;
	public static ServerSystem getInstance() {
		return pl;
	}
	
	public static String prefix = "§6ServerSystem  §7";
	public static Lobby lobby = null;
	
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
		
		super.onDisable();
	}
	
	public void preInit() {
		new Filer(true);
		
		new Properties(this);
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		lobby = new Lobby(this);
		
		new MOTDManager(this);
	}
	
	public static void broadcastMessage(boolean onlyOps, String... s) {
		for(String msg : s) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(onlyOps) { if(p.isOp()) p.sendMessage(prefix+msg); }
				else p.sendMessage(prefix+msg);
			}
		}
	}
	
}
