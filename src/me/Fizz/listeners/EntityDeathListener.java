package me.Fizz.listeners;

import me.Fizz.Server;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityDeathListener implements Listener {

	private Server plugin;

	public EntityDeathListener(Server plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		event.setDroppedExp(0);
		if (entity.getKiller() instanceof Player) {
			Player player = entity.getKiller();
			plugin.addXp(player, (int) entity.getMaxHealth());
		}
	}

}
