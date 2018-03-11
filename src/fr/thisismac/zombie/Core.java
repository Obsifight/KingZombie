package fr.thisismac.zombie;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import com.earth2me.essentials.Essentials;

import net.ess3.api.IEssentials;

public class Core extends JavaPlugin {

	public static Core INSTANCE;
	private Location spawnLocation;
	boolean spawning;

	public void onEnable() {
		INSTANCE = this;
		getLogger().info("Starting ..");
		getCommand("zombie").setExecutor(new ZombieCommand());
		getServer().getPluginManager().registerEvents(new Listeners(), this);
	}

	public void spawnZombie(Location loc) {
		loc.getChunk().load();
		spawnLocation = loc;
		spawning = true;

		Entity zombie = getServer().getWorlds().get(0).spawnEntity(loc, EntityType.KING_ZOMBIE);
		zombie.setMetadata("event", new FixedMetadataValue(this, 1));

		getServer().broadcastMessage(
				ChatColor.translateAlternateColorCodes('&', "&4[&6KingZombie&4] &eUn KingZombie est apparu en &6X : "
						+ spawnLocation.getBlockX() + "&e et&6 Z : " + spawnLocation.getBlockZ()));
		getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
				"&4[&6KingZombie&4] &eVous pouvez également vous y rendre avec &6 /warp kingzombie"));
	}

	public Location getSpawnLoc() {
		return spawnLocation;
	}

	public boolean isSpawned() {
		return spawning;
	}

	public void dropStuff(Location location, boolean dead) {
		Location trueLoc = location;
		Random ran = new Random();

		getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', 
				"&4[&6KingZombie&4] &eLe KingZombie a été vaincu, bravo au participant de l'event !"));

		for (ItemStack item : KitExtractor.getKit("kingzombie_dead")) {
			location.setX(location.getX() + ran.nextInt(10) - 5);

			location.setZ(location.getZ() + ran.nextInt(10) - 5);
			getServer().getWorlds().get(0).dropItem(location, item);

			location = trueLoc;
		}
	}

	public void setWarp(String warpName, Location loc) {
		final IEssentials ess = JavaPlugin.getPlugin(Essentials.class);

		try {
			ess.getWarps().setWarp(warpName, loc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
