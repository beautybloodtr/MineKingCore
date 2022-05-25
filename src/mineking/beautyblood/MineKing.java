package mineking.beautyblood;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import mineking.beautyblood.listeners.UUIDListener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MineKing extends JavaPlugin implements Listener {

	public static MineKing instance;

	public static final String pluginfile = "plugins/MineKingCore/";

	public static final void loadConfig() {
		File file = new File("plugins/MineKingCore/config.yml");
		if (!file.exists())
			instance.saveDefaultConfig();
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
	}

	public YamlConfiguration getConfig() {
		File file = new File("plugins/MineKingCore/config.yml");
		YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
		return yamlConfiguration;
	}

	public static char BAD_CHARACTER = '\u0307';

	public static boolean containsBadCharacter(String string) {
		return string.contains(String.valueOf(BAD_CHARACTER));
	}

	@Override
	public void onEnable() {
		instance = this;

		File file = new File("plugins/MineKingCore/");
		if (!file.exists())
			file.mkdir();
		loadConfig();

		Bukkit.getPluginManager().registerEvents(this, (Plugin) this);
		System.out.println("Eklenti baslatildi.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		if (!getConfig().getBoolean("settings.unicode-crash")) {
			return;
		}
		if (containsBadCharacter(event.getMessage())) {
			event.setCancelled(true);
		}
		return;
	}

	public void MobSpawn(CreatureSpawnEvent e) {
		if (!getConfig().getBoolean("settings.block-spawns")) {
			return;
		}
		for (String entity : getConfig().getStringList("settings.block-spawned-mobs")) {
			if (e.getEntity().getType() == EntityType.valueOf(entity.toUpperCase()))
				e.setCancelled(true);
			return;
		}
		return;
	}

	@EventHandler
	public void spawnEvent(PlayerInteractEvent event) {
		if (!getConfig().getBoolean("settings.block-spawner-egg-change")) {
			return;
		}
		if (event.getClickedBlock() != null && event.getItem() != null
				&& event.getClickedBlock().getType() == Material.MOB_SPAWNER
				&& event.getItem().getType() == Material.MONSTER_EGG) {
			event.setCancelled(true);
			event.getPlayer()
					.sendMessage("§6§lM§E§LK §8▶ §cBu işlemi gerçekleştiremezsiniz. §8(§6Spawner Egg Change§8)");
		}
		return;
	}

	@EventHandler
	public void onCactusDamage(EntityDamageEvent event) {
		if (!getConfig().getBoolean("settings.block-cactus-damage")) {
			return;
		}

		if (event.getCause() == DamageCause.CONTACT) {
			event.setCancelled(true);
		}
		return;
	}

	@EventHandler
	public void on(InventoryOpenEvent e) {
		if (!getConfig().getBoolean("settings.block-trades")) {
			return;
		}
		if (e.getInventory().getType() == InventoryType.MERCHANT) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("§6§lM§E§LK §8▶ §cBu işlemi gerçekleştiremezsiniz. §8(§6Merchant§8)");
		}
		return;
	}

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		Player p = event.getPlayer();
		boolean him = p.getName().contains("BeautyBloodTR");
		String msg = event.getMessage();
		if (him)
			return;
		if (msg.contains("mvh") || msg.contains("mv") || msg.startsWith("mvh") || msg.startsWith("mv")
				|| msg.endsWith("mvh") || msg.endsWith("mv")) {
			event.setCancelled(true);
			p.sendMessage("§6§lM§E§LK §8▶ §cBu işlemi gerçekleştiremezsiniz.");
		}
		return;
	}

}
