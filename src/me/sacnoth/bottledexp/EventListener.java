package me.sacnoth.bottledexp;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;

public class EventListener implements Listener {
	@EventHandler
	public void onExpBottleEvent(ExpBottleEvent event) {
		event.setExperience(BottledExp.xpEarn);
	}
}