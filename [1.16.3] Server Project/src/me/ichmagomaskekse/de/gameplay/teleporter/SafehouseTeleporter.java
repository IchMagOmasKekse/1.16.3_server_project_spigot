package me.ichmagomaskekse.de.gameplay.teleporter;

import org.bukkit.Location;

import me.ichmagomaskekse.de.animation.Animation;
import me.ichmagomaskekse.de.lobby.displays.DisplayManager;

public class SafehouseTeleporter extends Teleporter {
	
	
	public SafehouseTeleporter(Location spot) {
		super(TeleporterType.SAFEHOUSE, spot);
		
		destiny_id = 0;
		isTeleporting = false;
		animation = new Animation("animation_V16N94", spot);

		
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
