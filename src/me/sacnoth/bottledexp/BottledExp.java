package me.sacnoth.bottledexp;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class BottledExp extends JavaPlugin {
	static Logger log;
	private BottledExpCommandExecutor myExecutor;
	static int xpCost;
	static int xpEarn;
	static boolean usePermissions = false;
	static PermissionManager permissions;
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
		
		if (Bukkit.getServer().getPluginManager().isPluginEnabled("PermissionsEx")) {
			usePermissions = true;
			permissions = PermissionsEx.getPermissionManager();
			log.info("Using Permissions!");
		}
	}

	public void onDisable() {
		log.info("You are no longer able to fill XP into Bottles");
	}
	
	public static boolean checkPermission(String node, Player player) {
		if (usePermissions) {
			if (permissions.has(player, node)) {
				return true;
			}
			player.sendMessage(ChatColor.RED + "You don't have permission to do this!");
			return false;
		}
		return true;
	}
}
