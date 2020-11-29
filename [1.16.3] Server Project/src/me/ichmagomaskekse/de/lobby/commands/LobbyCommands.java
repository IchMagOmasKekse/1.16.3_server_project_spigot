package me.ichmagomaskekse.de.lobby.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.files.Filer;
import me.ichmagomaskekse.de.properties.Properties;

public class LobbyCommands implements CommandExecutor {
	
	public LobbyCommands() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender;
			
			switch(args.length) {
			case 0:
				if(cmd.getName().equalsIgnoreCase("lobby") && hasPermission(p, "server.lobby.teleport")) {
					p.teleport(Properties.lobby_spawn);
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1f, 6f);
				}else if(cmd.getName().equalsIgnoreCase("setlobby") && hasPermission(p, "server.lobby.set")) {
					if(Filer.setLocation(Filer.lobby_path, "Spawn", p.getLocation())) {
						p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_FALL, 1f, -3f);
						ServerSystem.broadcastMessage(true, "Ein neuer Lobby-Spawn wurde von §b"+p.getName()+" §7gesetzt!");
					} else ServerSystem.broadcastMessage(true, p.getName()+" §7hat versucht einen neuen Lobby-Spawn zu setzen. Versuch ist §cfehlgeschlagen§7!");
				}
				break;
			case 1:
				if(cmd.getName().equalsIgnoreCase("lobby") && args[0].equalsIgnoreCase("help") && hasPermission(p, "server.lobby.help")) {
					sendCommandInfo(p);
				}else if(cmd.getName().equalsIgnoreCase("lobby") && args[0].equalsIgnoreCase("2") && hasPermission(p, "server.lobby.2")) {
					World w2 = Bukkit.getWorld("world2");
					p.teleport(w2.getSpawnLocation());
				}
				break;
			}
		}else sender.sendMessage(ServerSystem.prefix+"§cDieser Befehl ist nur für Spieler!");
		
		return false;
	}
	
	public void sendCommandInfo(Player p) {
		p.sendMessage("");
		     p.sendMessage("§eCommand-Hilfe§7------------------------------------");
		p.sendMessage("§cOP-Only §eNormal-Player");
		p.sendMessage(" §e/lobby §7Teleportiere dich zur Lobby");
		if(p.isOp()) p.sendMessage(" §c/setlobby §7Setze den Lobby-Spawn");
	}
	
	public boolean hasPermission(CommandSender sender, String permission) {
		return ServerSystem.hasPermission(sender, permission);
	}
	
}
