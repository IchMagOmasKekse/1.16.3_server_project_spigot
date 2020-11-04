package me.ichmagomaskekse.de.money.commands;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.PlayerAtlas;
import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.money.AccountManager;
import me.ichmagomaskekse.de.properties.Properties;

public class MoneyCommands implements CommandExecutor {
	
	public MoneyCommands() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		switch(args.length) {
		case 0: 
			if(cmd.getName().equalsIgnoreCase("money") && hasPermission(sender, "server.money")) {
				if(sender instanceof Player) sender.sendMessage("§aDein Kontostand: §7"+AccountManager.getMoney(((Player)sender).getUniqueId())+Properties.money_symbol);
				else sender.sendMessage("§aDein Kontostand gleicht der Zahl '§fUnendlich§a'");
			}
			break;
		case 1:
			if(args[0].equalsIgnoreCase("help") && hasPermission(sender, "server.money.help")) {
				sendCommandInfo(sender);
			}else if(args[0].equalsIgnoreCase("add") && hasPermission(sender, "server.money.add")) {
				sender.sendMessage(ServerSystem.prefix+"§a/money add [Player] [amount]");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("remove") && hasPermission(sender, "server.money.remove")) {
				sender.sendMessage(ServerSystem.prefix+"§a/money remove [Player] [amount]");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("send") && hasPermission(sender, "server.money.send")) {
				sender.sendMessage(ServerSystem.prefix+"§a/money send [Player] [amount]");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("set") && hasPermission(sender, "server.money.set")) {
				sender.sendMessage(ServerSystem.prefix+"§a/money set [Player] [amount]");
				//-------------------------------------------------------
			}else if(PlayerAtlas.getUUID(args[0]) != null) {
				if(hasPermission(sender, "server.money.other")) {					
					UUID uuid = PlayerAtlas.getUUID(args[0]);
					String name = PlayerAtlas.getPlayername(uuid.toString());
					sender.sendMessage("§aKontostand von §f"+name +"§a: §7"+AccountManager.getMoney(uuid)+Properties.money_symbol);
				}
				//-------------------------------------------------------
			}else sender.sendMessage(ServerSystem.prefix+"§cDieser Spieler hat noch kein Bankkonto auf diesem Server!");
			break;
		case 2:
			if(args[0].equalsIgnoreCase("add") && hasPermission(sender, "server.money.add")) {
				sender.sendMessage(ServerSystem.prefix+"§a/money add [Player] [amount]");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("remove") && hasPermission(sender, "server.money.remove")) {
				sender.sendMessage(ServerSystem.prefix+"§a/money remove [Player] [amount]");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("send") && hasPermission(sender, "server.money.send")) {
				sender.sendMessage(ServerSystem.prefix+"§a/money send [Player] [amount]");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("set") && hasPermission(sender, "server.money.set")) {
				sender.sendMessage(ServerSystem.prefix+"§a/money set [Player] [amount]");
				//-------------------------------------------------------
			}
			break;
		case 3:
			if(args[0].equalsIgnoreCase("add") && hasPermission(sender, "server.money.add")) {
				if(PlayerAtlas.getUUID(args[1]) != null) {
					if(hasPermission(sender, "server.money.add")) {
						UUID uuid = PlayerAtlas.getUUID(args[1]);
						String name = PlayerAtlas.getPlayername(uuid.toString());
						int amount = Integer.parseInt(args[2]);
						if(AccountManager.addMoney(uuid, amount)) {
							sender.sendMessage("§aDu hast §f"+name+" §7"+amount+Properties.money_symbol+" §ahinzugefügt!");
							sender.sendMessage("§aKontostand von §f"+name +"§a: §7"+AccountManager.getMoney(uuid)+Properties.money_symbol);
							if(Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage("§aDu hast eben §7"+amount+Properties.money_symbol+" §avon §f"+sender.getName()+" §aerhalten!");
						}else sender.sendMessage(ServerSystem.prefix+"§cDem Spieler konnte kein Geld hinzugefügt werden");
					}
				}else sender.sendMessage(ServerSystem.prefix+"§cDieser Spieler hat noch kein Bankkonto auf diesem Server!");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("remove") && hasPermission(sender, "server.money.remove")) {
				if(PlayerAtlas.getUUID(args[1]) != null) {
					if(hasPermission(sender, "server.money.remove")) {
						UUID uuid = PlayerAtlas.getUUID(args[1]);
						String name = PlayerAtlas.getPlayername(uuid.toString());
						int amount = Integer.parseInt(args[2]);
						if(AccountManager.removeMoney(uuid, amount)) {
							sender.sendMessage("§aDu hast §f"+name+" §7"+amount+Properties.money_symbol+" §aentfernt!");
							sender.sendMessage("§aKontostand von §f"+name +"§a: §7"+AccountManager.getMoney(uuid)+Properties.money_symbol);
							if(Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage("§aDir wurden eben §7"+amount+Properties.money_symbol+" §avon §f"+sender.getName()+" §aentfernt!");
						}else sender.sendMessage(ServerSystem.prefix+"§cDem Spieler konnte kein Geld entfernt werden");
					}
				}else sender.sendMessage(ServerSystem.prefix+"§cDieser Spieler hat noch kein Bankkonto auf diesem Server!");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("send") && hasPermission(sender, "server.money.send")) {
				if(PlayerAtlas.getUUID(args[1]) != null) {
					if(hasPermission(sender, "server.money.send")) {
						UUID uuid = PlayerAtlas.getUUID(args[1]);
						String name = PlayerAtlas.getPlayername(uuid.toString());
						int amount = Integer.parseInt(args[2]);
						boolean successfull = false;
						if(sender instanceof Player) successfull = AccountManager.sendMoney(((Player)sender).getUniqueId(), uuid, amount);
						else successfull = AccountManager.addMoney(uuid, amount);
						if(successfull) {
							sender.sendMessage("§aDu hast §f"+name+" §7"+amount+Properties.money_symbol+" §agesendet!");
							sender.sendMessage("§aKontostand von §f"+name +"§a: §7"+AccountManager.getMoney(uuid)+Properties.money_symbol);
							if(Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage("§aDu hast eben §7"+amount+Properties.money_symbol+" §avon §f"+sender.getName()+" §aerhalten!");
						}else sender.sendMessage(ServerSystem.prefix+"§cDem Spieler konnte kein Geld gesendet werden");
					}
				}else sender.sendMessage(ServerSystem.prefix+"§cDieser Spieler hat noch kein Bankkonto auf diesem Server!");
				//-------------------------------------------------------
			}else if(args[0].equalsIgnoreCase("set") && hasPermission(sender, "server.money.set")) {
				if(PlayerAtlas.getUUID(args[1]) != null) {
					if(hasPermission(sender, "server.money.set")) {
						UUID uuid = PlayerAtlas.getUUID(args[1]);
						String name = PlayerAtlas.getPlayername(uuid.toString());
						int amount = Integer.parseInt(args[2]);
						boolean successfull = false;
						if(sender instanceof Player) successfull = AccountManager.setMoney(((Player)sender).getUniqueId(), uuid, amount);
						else successfull = AccountManager.removeMoney(uuid, AccountManager.getMoney(uuid));
						if(successfull) {
							sender.sendMessage("§aDu hast §f"+name+"§a's Kontostand auf §7"+amount+Properties.money_symbol+" §agesetzt!");
							sender.sendMessage("§aKontostand von §f"+name +"§a: §7"+AccountManager.getMoney(uuid)+Properties.money_symbol);
							if(Bukkit.getPlayer(uuid) != null) Bukkit.getPlayer(uuid).sendMessage("§aDein Kontostand wurde von §f"+sender.getName()+" §aauf §7"+amount+Properties.money_symbol+" §agesetzt!");
						}else sender.sendMessage(ServerSystem.prefix+"§cDer Kontostand konnte nicht geändert werden");
					}
				}else sender.sendMessage(ServerSystem.prefix+"§cDieser Spieler hat noch kein Bankkonto auf diesem Server!");
				//-------------------------------------------------------
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
		if(sender.isOp()) sender.sendMessage(" §c/money add [Player] [amount] §7Gebe Geld");
		if(sender.isOp()) sender.sendMessage(" §c/money remove [Player] [amount] §7Entferne Geld");
		                  sender.sendMessage(" §a/money request [Player] [amount] §7Frage Geld an");
		                  sender.sendMessage(" §a/money send [Player] [amount] §7Sende Geld");
		if(sender.isOp()) sender.sendMessage(" §c/money set [Player] [amount] §7Setze Geld");
	}
	
	public boolean hasPermission(CommandSender sender, String permission) {
		return ServerSystem.hasPermission(sender, permission);
	}
}
