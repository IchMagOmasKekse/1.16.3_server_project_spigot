package me.ichmagomaskekse.de.gameplay.teleporter;

import org.bukkit.Location;

import me.ichmagomaskekse.de.animation.Animation;
import me.ichmagomaskekse.de.lobby.displays.DisplayManager;

public class GeneratorTeleporter extends Teleporter {
	
	
	public GeneratorTeleporter(Location spot) {
		super(TeleporterType.GENERATOR, spot);
		
		destiny_id = 1;
		isTeleporting = false;
		animation = new Animation("animation_8k9aUJ", spot);

	
//		display.setPositionToBottum();
		DisplayManager.registerDisplay(display);
	}

	
	@Override
	public void doAnimation() {
		if(enabled==false) return;
		
		if(isTeleporting && animation.isPlaying == false) {
			animation.play();
		}else {
			
		}		
	}

	@Override
	public void tick() {
		isTeleporting = (!passengers.isEmpty());
		doAnimation();
		if(animation.isFinished) teleport();
	}

	@Override
	public void resetAnimation() {
		animation.resetAnimation();
	}

	@Override
	public void startTeleporprocess() {
		isTeleporting = true;
	}

}
