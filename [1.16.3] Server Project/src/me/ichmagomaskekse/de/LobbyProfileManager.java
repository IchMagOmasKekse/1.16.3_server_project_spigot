package me.ichmagomaskekse.de;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LobbyProfileManager {
	
	
	
	public static class LobbyProfile {
		
		private Player host = null;
		
		/* JumpPad Settings */
		private Material lastBlock = Material.AIR;
		private Material currentBlock = Material.AIR;
		
		public LobbyProfile(Player host) {
			this.host = host;
		}
		
		public Player getHost() {
			return host;
		}
		
		public void updateLastBlock(Material mat) {
			this.lastBlock = mat;
		}
		
		public void updateCurrentBlock(Material mat) {
			this.currentBlock = mat;
		}
		
		public Material getLastBlock() {
			if(lastBlock == null) return Material.AIR;
			else return lastBlock;
		}
		
		public Material getCurrentBlock() {
			if(currentBlock == null) return Material.AIR;
			else return currentBlock;
		}
		
	}
	
}
