package me.ichmagomaskekse.de.lobby.darkforge.effects;

import java.util.Random;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.ichmagomaskekse.de.Code;

public abstract class Effect {
	
	protected String id = "unknown";
	protected long duration = 0l;
	protected long durationLeft = 0l;
	protected int level = 0;
	protected LivingEntity target = null;
	protected LivingEntity damager = null;
	protected ItemStack weapon = null;
	protected EffectType type = EffectType.UNDEFINED;
	protected BukkitRunnable timer = null;
	
	protected Vector static_velo = new Vector(0,0,0);
	
	protected Random random = new Random();
	
	public Effect(EffectType type, LivingEntity damager, LivingEntity target, ItemStack weapon) {
		this.id = Code.getRandomCode(3, 10);
		this.damager = damager;
		this.target = target;
		this.weapon = weapon;
		setEffectType(type);
		play();
	}
	
	public abstract void play();
	
	public abstract void stop();
	
	
	public void setEffectType(EffectType type) {
		this.duration = type.getDuration();
		this.durationLeft = duration;
		this.type = type;
	}
	
	public String getID() { return id; }
	
	public static enum EffectType {
		
		UNDEFINED("undefined_effect", "Undefiniert", 60*20l),
		DARKNESS("darkness_effect", "Dunkelheit", 4*20l),
		FREEZE("freeze_effect", "Einfrieren", 10*20l),
		ARROW_STORM("arrow_storm_effect", "Pfeilhagel", 60*20l);
		
		String codename, displayname;
		long duration;
		
		private EffectType(String codename, String displayname, long duration) {
			this.codename = codename;
			this.displayname = displayname;
			this.duration = duration;
		}
		
		public String getCodename() {
			return codename;
		}
		public String getDisplayname() {
			return displayname;
		}
		public long getDuration() {
			return duration;
		}
		
		
		
	}
	
}
