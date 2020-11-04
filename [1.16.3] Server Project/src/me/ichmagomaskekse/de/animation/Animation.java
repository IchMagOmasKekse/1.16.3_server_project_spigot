package me.ichmagomaskekse.de.animation;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.ichmagomaskekse.de.ServerSystem;
import me.ichmagomaskekse.de.files.Filer;

public class Animation {
	
	private String filename = "";
	private String anima_name = "";
	private Location origin = null;
	private int id = 0;
	private int amount_of_steps = 0;
	
	public boolean isPlaying= false;
	public boolean isResetted= false;
	public boolean isFinished= false;
	private ArrayList<AnimaStep> step_list = new ArrayList<AnimaStep>();
	
	public Animation(String filename, Location origin) {
		this.filename = filename;
		this.origin = origin.clone();
		loadSteps();
	}
	
	public void loadSteps() {
		File file = new File("plugins/"+Filer.root_dir+"/Animations/"+filename+".anima");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		
		anima_name = cfg.getString("Name");
		id = cfg.getInt("ID");
		amount_of_steps = cfg.getInt("Steps");
		AnimaStep as = null;
		for(int i = 1; i != amount_of_steps+1; i++) {
			as = new AnimaStep(i,
					cfg.getDouble("Step "+i+".Location.XOffset"),
					cfg.getDouble("Step "+i+".Location.YOffset"),
					cfg.getDouble("Step "+i+".Location.ZOffset"),
					cfg.getDouble("Step "+i+".Speed"),
					Material.valueOf(cfg.getString("Step "+i+".Material")),
					origin,
					this);
			step_list.add(as);
		}
	}
	
	public void play() {
		if(isFinished == false) {			
			isPlaying = true;
			isResetted = false;
			
			step_list.get(0).start();
		}
	}
	
	public void resetAnimation() {
		for(AnimaStep step : step_list) {
			step.reset();
		}
		isPlaying = false;
		isResetted = true;
		isFinished = false;
	}
	
	public String getAnimaname() { return anima_name; }
	public int getID() { return id; }
	
	public class AnimaStep {
		
		private int id = 0;
		private double xoff, yoff, zoff, duration;
		private Material mat = Material.BEDROCK;
		private boolean isFinished = false, isRunning = false;
		private Location origin = null, position = null;
		private BukkitRunnable timer = null;
		private Animation animation = null;
		
		private Material prev_type = null;
		private BlockData prev_data = null;
		private Biome prev_biome = null;
		
		public AnimaStep(int id, double xoff, double yoff, double zoff, double duration, Material mat, Location origin, Animation animation) {
			this.id = id;
			this.xoff = xoff;
			this.yoff = yoff;
			this.zoff = zoff;
			this.mat = mat;
			this.duration = duration;
			this.origin = origin.clone();
			this.position = origin.clone().add(xoff, yoff, zoff);
			this.prev_type = position.clone().getBlock().getType();
			this.prev_data = position.clone().getBlock().getBlockData();
			this.prev_biome = position.clone().getBlock().getBiome();
			this.animation = animation;
		}
		
		public void start() {
			if(isRunning == false && isFinished) return;
			else if(isRunning == false) {
				isRunning = true;
				position.getBlock().setType(mat);
				position.getBlock().setBlockData(mat.createBlockData());
				position.getWorld().playSound(position.clone(), Sound.BLOCK_PISTON_EXTEND, 6f, 6f);
				
				timer = new BukkitRunnable() {
					
					@Override
					public void run() {
						isFinished = true;
						if((id) != animation.amount_of_steps) animation.step_list.get(id).start();
						else animation.isFinished = true;
						cancel();
					}
				};
				timer.runTaskLater(ServerSystem.getInstance(), (long)(duration * 20));
			}
		}
		
		public Location getLocation() {
			return position.clone();
		}
		public Location getOrigin() {
			return origin.clone();
		}
		
		public void reset() {
			if(prev_type != null) {
				position.clone().getBlock().setType(prev_type);
				position.clone().getBlock().setBlockData(prev_data);
				position.clone().getBlock().setBiome(prev_biome);
				isFinished = false;
				isRunning = false;
				position = origin.clone().add(xoff, yoff, zoff);
				this.prev_type = position.clone().getBlock().getType();
				this.prev_data = position.clone().getBlock().getBlockData();
				this.prev_biome = position.clone().getBlock().getBiome();
				if(timer != null && timer.isCancelled() == false)timer.cancel();
			}
		}
		
	}
	
}
