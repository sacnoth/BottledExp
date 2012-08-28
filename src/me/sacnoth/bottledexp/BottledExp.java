package me.sacnoth.bottledexp;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class BottledExp extends JavaPlugin {
	static Logger log;
	private BottledExpCommandExecutor myExecutor;
	static int xpCost;
	static int xpEarn;
	static boolean usePermissions = false;
	static boolean useVaultEcon = true;
	static boolean useVaultPermissions = false;
	static PermissionManager pexPermissions;
	static Permission vaultPermissions;
	static String errAmount;
	static String errXP;
	static String errMoney;
	static String langCurrentXP;
	static String langOrder1;
	static String langOrder2;
	static String langRefund;
	static String langItemConsumer;
	static String langMoney;
	static boolean settingUseItems;
	static int settingConsumedItem;
	static int amountConsumed;
	static double moneyCost;
	static Config config;
	public static Economy economy = null;

	public void onEnable() {
		log = this.getLogger();

		myExecutor = new BottledExpCommandExecutor(this);
		getCommand("bottle").setExecutor(myExecutor);

		getServer().getPluginManager()
				.registerEvents(new EventListener(), this);

		config = new Config(this);
		config.load();

		if (!setupEconomy()) {
			log.info("Vault not found - Disabeling economy capabilities.");
			useVaultEcon = false;
		}

		log.info("You are now able to fill XP into Bottles");

		if (Bukkit.getServer().getPluginManager()
				.isPluginEnabled("PermissionsEx")) {
			pexPermissions = PermissionsEx.getPermissionManager();
			usePermissions = true;
			log.info("Using PermissionsEx!");
		} else if (Bukkit.getServer().getPluginManager()
				.isPluginEnabled("Vault")) {
			setupPermissions();
			useVaultPermissions = true;
			log.info("Using " + vaultPermissions.getName() + " via Vault.");
		} else {
			log.warning("Neither PEX nor Vault found, BottledExp will not work properly!");
		}
	}

	public void onDisable() {
		log.info("You are no longer able to fill XP into Bottles");
	}
	
	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> permissionProvider = getServer()
				.getServicesManager().getRegistration(
						net.milkbowl.vault.permission.Permission.class);
		if (permissionProvider != null) {
			vaultPermissions = permissionProvider.getProvider();
		}
		return (vaultPermissions != null);
	}

	public static boolean checkPermission(String node, Player player) {
		if (usePermissions) {
			if (pexPermissions.has(player, node)) {
				return true;
			}
			player.sendMessage(ChatColor.RED
					+ "You don't have permission to do this!");
			return false;
		} else if (useVaultPermissions && vaultPermissions.isEnabled()) {
			if (vaultPermissions.playerHas(player.getWorld(), player.getName(),
					node)) {
				return true;
			}
			player.sendMessage(ChatColor.RED
					+ "You don't have permission to do this!");
			return false;
		}
		player.sendMessage(ChatColor.RED
				+ "Neither PEX nor Vault found, BottledExp will not work properly!");
		return false;
	}

	public static int levelToExp(int level) {
		if (level <= 15)
		{
			return 17 * level;
		}
		else if (level <= 30)
		{
			return (3*level*level/2)-(59*level/2)+360;
		}
		else
		{
			return (7*level*level/2)-(303*level/2)+2220;
		}
	}

	public static int deltaLevelToExp(int level) {
		if (level <= 15)
		{
			return 17;
		}
		else if (level <= 30)
		{
			return 3 * level - 31;
		}
 else {
			return 7 * level - 155;
		}
	}

	public static int getPlayerExperience(Player player) {
		int bukkitExp = player.getTotalExperience();
		return bukkitExp;
	}

	public static boolean checkInventory(Player player, int itemID, int amount) {
		PlayerInventory inventory = player.getInventory();

		if (inventory.contains(itemID, amount)) {
			return true;
		} else {
			return false;
		}
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

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer()
				.getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		economy = rsp.getProvider();
		return economy != null;
	}

	public static double getBalance(Player player) {
		return BottledExp.economy.getBalance(player.getName());
	}

	public static void withdrawMoney(Player player, double price) {
		BottledExp.economy.withdrawPlayer(player.getName(), price);
	}
}
