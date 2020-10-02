package me.ichmagomaskekse.de.money.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.money.AccountManager;

public class MoneyCommands implements CommandExecutor {
	
	public MoneyCommands() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		switch(args.length) {
		case 0: 
			if(cmd.getName().equalsIgnoreCase("money") && hasPermission(sender, "server.money")) {
				if(sender instanceof Player) sender.sendMessage("§aDein Kontostand: §7"+AccountManager.getMoney(((Player)sender).getUniqueId()));
				else sender.sendMessage("§aDein Kontostand gleicht der Zahl '§fUnendlich§a'");
			}
			break;
		case 1:
			if(args[0].equalsIgnoreCase("help") && hasPermission(sender, "server.money.help")) {
				sendCommandInfo(sender);
			}
			break;
		default:
			
			break;
		}
		return false;
	}
	
	public void sendCommandInfo(CommandSender sender) {
		sender.sendMessage("");
		sender.sendMessage("§aMoney-Commands§7-----------------------------------"); //40 : 31 -> 40 : 82
		sender.sendMessage("§cOP-Only §aNormal-Player");
		sender.sendMessage(" §a/money help §7Command-Hilfe");
		sender.sendMessage(" §a/money [Player] §7Siehe Geld ein");
		if(sender.isOp()) sender.sendMessage(" §c/money add [Player] amount §7Gebe Geld");
		if(sender.isOp()) sender.sendMessage(" §c/money remove [Player] amount §7Entferne Geld");
		sender.sendMessage(" §a/money request [Player] amount §7Frage Geld an");
		sender.sendMessage(" §a/money send [Player] amount §7Sende Geld");
		if(sender.isOp()) sender.sendMessage(" §c/money set [Player] amount §7Setze Geld");
	}
	
	public boolean hasPermission(CommandSender sender, String permission) {
		return ServerSystem.hasPermission(sender, permission);
	}
}
