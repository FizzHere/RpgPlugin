package me.Fizz.listeners;

import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

public class EntitySpawnListener implements Listener {

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			Location loc = entity.getLocation();
			Biome biome = entity.getWorld().getBiome(loc.getBlockX(), loc.getBlockZ());
			double distance = entity.getLocation().distance(entity.getWorld().getSpawnLocation());
			double start = entity.getMaxHealth();
			if (biome.name().equals("HELL"))
				entity.setMaxHealth(distance * (0.1 * start + 15) + 100);
			else
				entity.setMaxHealth(distance / (0.1 / start + 15) + 10);
			entity.setHealth(entity.getMaxHealth());
		}
	}

}
