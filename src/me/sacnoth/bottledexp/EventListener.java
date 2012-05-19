package me.sacnoth.bottledexp;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.ExpBottleEvent;

public class EventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExpBottleEvent(ExpBottleEvent event) {
		event.setExperience(BottledExp.xpEarn);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEnchantItem(EnchantItemEvent event) {
		if (event.isCancelled()) {
			return;
		}
		Player player = event.getEnchanter();

		int newLevel = player.getLevel() - event.getExpLevelCost();
		int newXP = BottledExp.levelToExp(newLevel);

		float delta = player.getTotalExperience()
				- BottledExp.levelToExp(player.getLevel());
		delta = delta / BottledExp.deltaLevelToExp(player.getLevel())
				* BottledExp.deltaLevelToExp(newLevel);
		newXP += (int) Math.round(delta);

		player.setTotalExperience(0);
		player.setLevel(0);
		player.setExp(0);
		player.giveExp(newXP);
		event.setExpLevelCost(0);
	}
}