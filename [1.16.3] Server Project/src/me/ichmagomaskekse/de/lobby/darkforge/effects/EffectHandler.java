package me.ichmagomaskekse.de.lobby.darkforge.effects;

import java.util.HashMap;

import me.ichmagomaskekse.de.lobby.darkforge.EnchantHandler;

public class EffectHandler {
	/*                      Code   Effect */
	private static HashMap<String, Effect> running_effects = new HashMap<String, Effect>();
	
	/*
	 * Startet einen neuen Effekt
	 */
	public static void startNewEffect(Effect effect) {
		if(isRegistered(effect.getID())) return;
		else if(EnchantHandler.affected_entities.containsKey(effect.target) == false){
			EnchantHandler.affected_entities.put(effect.target, effect);
			running_effects.put(effect.getID(), effect);
		}
	}
	
	/*
	 * Stoppt einen Effekt
	 */
	public static void stopEffect(String id) {
		if(isRegistered(id)) {
			running_effects.get(id).stop();
			running_effects.remove(id);
		}else return;
	}
	
	/*
	 * Stoppt alle Effekte und löscht die Liste
	 */
	public static void stopAll() {
		for(String s : running_effects.keySet()) running_effects.get(s).stop();
		
		running_effects.clear();
	}
	
	/*
	 * Gibt einen laufenden Effekt zurück
	 */
	public static Effect getEffectById(String id) {
		if(isRegistered(id)) return running_effects.get(id);
		else return null;
	}
	
	/*
	 * Gibt zurpck, ob ein Effect mit dieser ID bereits läuft
	 */
	public static boolean isRegistered(String id) {
		return running_effects.containsKey(id);
	}
	
	
}
