package me.sacnoth.bottledexp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
	private BottledExp plugin;

	public Config(BottledExp plugin) {
		this.plugin = plugin;
	}

	public void load() {
		final FileConfiguration config = plugin.getConfig();

		config.addDefault("bottle.xpCost", 10);
		BottledExp.xpCost = config.getInt("bottle.xpCost");
		config.set("bottle.xpCost", BottledExp.xpCost);

		config.addDefault("bottle.xpEarn", 10);
		BottledExp.xpEarn = config.getInt("bottle.xpEarn");
		config.set("bottle.xpEarn", BottledExp.xpEarn);

		config.addDefault("language.errAmount",
				"The amount has to be a number!");
		BottledExp.errAmount = config.getString("language.errAmount");
		config.set("language.errAmount", BottledExp.errAmount);

		config.addDefault("language.errXP", "You don't have enough XP!");
		BottledExp.errXP = config.getString("language.errXP");
		config.set("language.errXP", BottledExp.errXP);

		config.addDefault("language.currentXP", "You currently have");
		BottledExp.langCurrentXP = config.getString("language.currentXP");
		config.set("language.currentXP", BottledExp.langCurrentXP);

		config.addDefault("language.order1", "You have ordered");
		BottledExp.langOrder1 = config.getString("language.order1");
		config.set("language.order1", BottledExp.langOrder1);

		config.addDefault("language.order2", "bottles");
		BottledExp.langOrder2 = config.getString("language.order2");
		config.set("language.order2", BottledExp.langOrder2);

		config.addDefault("language.refund", "Refund issued! Amount");
		BottledExp.langRefund = config.getString("language.refund");
		config.set("language.refund", BottledExp.langRefund);

		if (BottledExp.xpEarn > BottledExp.xpCost) {
			BottledExp.log
					.warning("Players earn more XP through XP bottles than they cost!");
		}

		plugin.saveConfig();
	}

	public void reload(CommandSender sender) {
		plugin.reloadConfig();
		final FileConfiguration config = plugin.getConfig();

		BottledExp.xpCost = config.getInt("bottle.xpCost");
		BottledExp.xpEarn = config.getInt("bottle.xpEarn");
		BottledExp.errAmount = config.getString("language.errAmount");
		BottledExp.errXP = config.getString("language.errXP");
		BottledExp.langCurrentXP = config.getString("language.currentXP");
		BottledExp.langOrder1 = config.getString("language.order1");
		BottledExp.langOrder2 = config.getString("language.order2");

		if (BottledExp.xpEarn > BottledExp.xpCost) {
			sender.sendMessage(ChatColor.RED
					+ "Players earn more XP through XP bottles than they cost!");
		}

		sender.sendMessage(ChatColor.YELLOW + "Cost: " + BottledExp.xpCost);
		sender.sendMessage(ChatColor.YELLOW + "Earn: " + BottledExp.xpEarn);
	}
}