package me.sacnoth.bottledexp;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class BottledExp extends JavaPlugin {
	static Logger log;
	private BottledExpCommandExecutor myExecutor;
	static int xpCost;
	static int xpEarn;
	static String errAmount;
	static String errXP;
	static String langCurrentXP;
	static String langOrder1;
	static String langOrder2;

	public void onEnable() {
		log = this.getLogger();

		myExecutor = new BottledExpCommandExecutor(this);
		getCommand("bottle").setExecutor(myExecutor);

		getServer().getPluginManager()
				.registerEvents(new EventListener(), this);

		Config config = new Config(this);
		config.load();

		log.info("You are now able to fill XP into Bottles");
	}

	public void onDisable() {
		log.info("You are no longer able to fill XP into Bottles");
	}
}
