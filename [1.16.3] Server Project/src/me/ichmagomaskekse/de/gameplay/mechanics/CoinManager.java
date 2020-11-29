package me.ichmagomaskekse.de.gameplay.mechanics;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.Code;
import me.ichmagomaskekse.de.ServerSystem;

@SuppressWarnings("deprecation")
public class CoinManager implements Listener {
	
	public static final Material coin_space_holder_material = Material.SCUTE;
	public static final Material coin_material = Material.BOWL;
	public static int coin_slot = 9;
	private BukkitRunnable updater = null;
	private static HashMap<Player, Integer> coins = new HashMap<Player, Integer>();
	
	public CoinManager() {
		ServerSystem.getInstance().getServer().getPluginManager().registerEvents(this, ServerSystem.getInstance());;
		updater = new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) {
					update(p);
//					if(p.getInventory().getItem(coin_slot) == null)
//						p.getInventory().setItem(coin_slot, getCoinPlaceholderItemStack());
//					else if(p.getInventory().getItem(coin_slot).getType() == coin_space_holder_material){
//					}
				}
			}
		};
		updater.runTaskTimer(ServerSystem.getInstance(), 0l, 1l);
	}
	
	public void update(Player p) {
		/* UPDATE(); IST SEHR VERBUGGT! */
//		ServerSystem.broadcastMessage(true, "CoinManager.updater(Player p); muss gefixt werden!");
		if(coins.containsKey(p) == false) return;
		
		if(p.getInventory().getItem(coin_slot).getType() == coin_space_holder_material) {
			p.getInventory().setItem(coin_slot, getCoinItemStack(1));
		}
		int c = coins.get(p);
		coins.remove(p);
		for(int i = 0; i != c+1; i++) {
			p.getInventory().addItem(getCoinItemStack(1));
		}
		
	}
	
	public static void addCoin(Player player, int amount) {
		int a = 0;
		if(coins.containsKey(player)) {
			a = coins.get(player);
			coins.remove(player);
		}
		coins.put(player, a+amount);
	}
	
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e) {
		if(e.getItem().getItemStack().getType() == coin_material) {
			addCoin(e.getPlayer(), e.getItem().getItemStack().getAmount());
			e.getPlayer().getWorld().playSound(e.getItem().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 6f, 3f);
			e.setCancelled(true);
			e.getItem().remove();
		}
	}
	
	@EventHandler
	public void onMonsterDeath(EntityDeathEvent e) {
		if(Code.random.nextInt(100) < 50) e.getDrops().add(getCoinItemStack(1));
	}
	
	/*
	 * Dieses Item kann von Mobs gedroppt werden
	 */
	public static ItemStack getCoinItemStack(int amount) {
		ItemStack coin = new ItemStack(coin_material);
		ItemMeta meta = coin.getItemMeta();
		meta.setDisplayName("§eMünze");
		coin.setItemMeta(meta);
		coin.setAmount(amount);
		return coin;
	}
	
	/*
	 * Dieses Item wird im Inventar dauerhaft an einem Spot gehalten und kann nicht bewegt werden
	 */
	public static ItemStack getCoinPlaceholderItemStack() {
		ItemStack item = new ItemStack(coin_space_holder_material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§fDein Münzstapel ist leer");
		item.setItemMeta(meta);
		return item;
	}
	
}
