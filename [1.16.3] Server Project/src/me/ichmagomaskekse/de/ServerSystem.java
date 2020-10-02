package me.ichmagomaskekse.de;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.lobby.Lobby;
import me.ichmagomaskekse.de.lobby.commands.LobbyCommands;
import me.ichmagomaskekse.de.money.AccountManager;
import me.ichmagomaskekse.de.money.MoneyListener;
import me.ichmagomaskekse.de.money.commands.MoneyCommands;
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
		AccountManager.addMoney(Bukkit.getPlayer("IchMagOmasKekse").getUniqueId(), 12);
		broadcastMessage(true, "Dein Kontostand: "+AccountManager.getMoney(Bukkit.getPlayer("IchMagOmasKekse").getUniqueId()));
		
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
		new MoneyListener(this);
		
		this.getCommand("lobby").setExecutor(new LobbyCommands());
		this.getCommand("setlobby").setExecutor(new LobbyCommands());
		this.getCommand("money").setExecutor(new MoneyCommands());
	}
	
	public static void broadcastMessage(boolean onlyOps, String... s) {
		for(String msg : s) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(onlyOps) { if(p.isOp()) p.sendMessage(prefix+msg); }
				else p.sendMessage(prefix+msg);
			}
		}
	}
	
	public static boolean hasPermission(CommandSender sender, String permission) {
		if(sender.hasPermission(permission)) return true;
		else sender.sendMessage(ServerSystem.prefix+Properties.noPermission);
		return false;
	}
	
}
