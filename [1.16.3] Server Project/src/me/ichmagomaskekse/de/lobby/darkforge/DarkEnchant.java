package me.ichmagomaskekse.de.lobby.darkforge;

import org.bukkit.Material;

public enum DarkEnchant {
	
	PICKPOCKET("pickpocket", "§7Seelenfresser", 1, 10, 0, "§9Seelenfresser", Material.DIAMOND),
	DARKNESS("darkness", "§8Dunkelheit", 1, 3, 1, "§9Dunkelheit", Material.GILDED_BLACKSTONE),
	LOYALITY("loyality", "§6Treue", 1, 1, 2, "§9Treue", Material.TOTEM_OF_UNDYING),
	CONDUCTIVITY("conductivity", "§cLeitfähigkeit", 1, 5, 3, "§9Leitfähigkeit", Material.REDSTONE),
	INTELLI_BEAST("intelli_beast", "§aIntelligenz-Bestie", 1, 3, 4, "§9Intelligenz-Bestie", Material.NETHER_STAR),
	FREEZE("freeze", "§bKälte", 1, 10, 5, "§9Kälte", Material.ICE);
	
	String codename, displayname, lore;
	int minLevel, maxLevel, id;
	Material material;
	
	DarkEnchant(String codename, String displayname, int minLevel, int maxLevel, int id, String lore, Material material) {
		this.codename = codename;
		this.displayname = displayname;
		this.minLevel = minLevel;
		this.maxLevel = maxLevel;
		this.id = id;
		this.lore = lore;
		this.material = material;
	}

	public String getLore() {
		return lore;
	}
	
	public String getCodename() {
		return codename;
	}

	public String getDisplayname() {
		return displayname;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public int getId() {
		return id;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	
	
}
