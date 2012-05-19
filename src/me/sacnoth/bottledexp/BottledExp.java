package me.sacnoth.bottledexp;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
	static String langRefund;
	static String langItemConsumer;
	static boolean settingUseItems;
	static int settingConsumedItem;
	static Config config;

	public void onEnable() {
		log = this.getLogger();

		myExecutor = new BottledExpCommandExecutor(this);
		getCommand("bottle").setExecutor(myExecutor);

		getServer().getPluginManager()
				.registerEvents(new EventListener(), this);

		config = new Config(this);
		config.load();

		log.info("You are now able to fill XP into Bottles");

		if (Bukkit.getServer().getPluginManager()
				.isPluginEnabled("PermissionsEx")) {
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
			player.sendMessage(ChatColor.RED
					+ "You don't have permission to do this!");
			return false;
		}
		return true;
	}

	public static int levelToExp(int level) {
		return (int) Math.round(1.75 * Math.pow(level, 2) + 5 * level);
	}

	public static int deltaLevelToExp(int level) {
		return (int) Math.round(3.5 * level + 3.25);
	}

	public static int getPlayerExperience(Player player) {
		double userLevel = player.getLevel() + player.getExp();
		int altExp = (int) Math.round(1.75D * Math.pow(userLevel, 2.0D) + 5.0D
				* userLevel);
		int bukkitExp = player.getTotalExperience();
		if (bukkitExp > altExp + 3) {
			BottledExp.log.info("Updating XP for player: " + player.getName());
			player.setTotalExperience(0);
			player.setLevel(0);
			player.setExp(0);
			player.giveExp(altExp);
			return altExp;
		}
		return bukkitExp;
	}

	public static boolean consumeItem(Player player, int itemID, int amount) {
		PlayerInventory inventory = player.getInventory();

		if (inventory.contains(itemID, amount)) {
			ItemStack items = new ItemStack(itemID, amount);
			inventory.removeItem(items);
			return true;
		} else {
			return false;
		}
	}

	public static int countItems(Player player, int itemID) {
		PlayerInventory inventory = player.getInventory();

		int amount = 0;
		ItemStack curItem;
		for (int slot = 0; slot < inventory.getSize(); slot++) {
			curItem = inventory.getItem(slot);
			if (curItem != null && curItem.getTypeId() == itemID)
				amount += curItem.getAmount();
		}
		return amount;
	}
}
