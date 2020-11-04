package me.ichmagomaskekse.de.lobby.displays;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.money.AccountManager;
import me.ichmagomaskekse.de.properties.Properties;

public class MoneyDisplay extends Display {

	public MoneyDisplay() {
		super(Properties.lobby_spawn.getWorld().spawn(new Location(Bukkit.getWorld("world"), 0.5, 97, 8.1), ArmorStand.class), "§6Coins: §f{COINS}");
		show();
	}

	@Override
	public void update() {
		replaceParamsInDisplayText("{COINS}", AccountManager.getMoney(ServerSystem.lobby.player.getUniqueId())+Properties.money_symbol);
	}
	
}
