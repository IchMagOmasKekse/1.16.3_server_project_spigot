package me.ichmagomaskekse.de.money;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.money.events.GeldAuszahlEvent;
import me.ichmagomaskekse.de.money.events.GeldEinzahlEvent;
import me.ichmagomaskekse.de.money.events.GeldGesetztEvent;
import me.ichmagomaskekse.de.money.events.GeldVerschickenEvent;

public class MoneyListener implements Listener {
	
	
	public MoneyListener(ServerSystem pl) {
		//pl.getServer().getPluginManager().registerEvents(this, pl);
	}
	
	@EventHandler
	public void onMoneyAdd(GeldEinzahlEvent e) {
		Player p = Bukkit.getPlayer(e.getGetter());
		if(e.wasSuccess()) {
			if(e.getAnzahl() > 0) p.sendMessage("§b[GeldEinzahlEvent] §7Dein Kontostand hat sich verändert §7-> §2+"+e.getAnzahl());
			else p.sendMessage("§b[GeldEinzahlEvent] §7Dein Kontostand hat sich verändert §7-> §c"+e.getAnzahl());
			
			p.sendMessage("§b[GeldEinzahlEvent] §7Kontostand: §f"+AccountManager.getMoney(e.getGetter()));
		}else {
			p.sendMessage("§b[GeldEinzahlEvent] §cDein Transfer-Vorgang konnte nicht abgeschlossen werden");
		}
	}
	@EventHandler
	public void onMoneySubtract(GeldAuszahlEvent e) {
		Player p = Bukkit.getPlayer(e.getGetter());
		if(e.wasSuccess()) {
			if(e.getAnzahl() > 0) p.sendMessage("§b[GeldAuszahlEvent] §7Dein Kontostand hat sich verändert §7-> §2+"+e.getAnzahl());
			else p.sendMessage("§b[GeldAuszahlEvent] §7Dein Kontostand hat sich verändert §7-> §c"+e.getAnzahl());
			
			p.sendMessage("§b[GeldAuszahlEvent] §7Kontostand: §f"+AccountManager.getMoney(e.getGetter()));
		}else {
			p.sendMessage("§b[GeldAuszahlEvent] §cDein Transfer-Vorgang konnte nicht abgeschlossen werden");
		}
	}
	@EventHandler
	public void onVerschicken(GeldVerschickenEvent e) {
		Bukkit.broadcastMessage("§bGeldVerschickenEvent §7hat ausgelöst!");
	}
	@EventHandler
	public void onSetzen(GeldGesetztEvent e) {
		Bukkit.broadcastMessage("§bGeldGesetztEvent §7hat ausgelöst!");
	}
}
