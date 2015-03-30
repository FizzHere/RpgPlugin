package me.Fizz.abilities;

import java.util.HashMap;
import java.util.UUID;

import me.Fizz.Abilities;
import me.Fizz.Cooldowns;
import me.Fizz.Distance;
import me.Fizz.ParticleEffect;
import me.Fizz.Server;
import me.Fizz.Stat;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Slam extends Ability implements Listener {

	private HashMap<UUID, Double> map = new HashMap<UUID, Double>();

	public Slam(int requirement, int cooldown, String name, Stat stat) {
		super(requirement, cooldown, name, stat);
	}

	@Override
	public boolean cast(Player player) {
		if (Stat.getPlayerStat(Stat.STR, player) >= requirement) {
			if (Cooldowns.tryCooldown(player, name, cooldown)) {
				ParticleEffect.DRIP_LAVA.display((float) 0.4, 0, (float) 0.4, 0, 40, player.getLocation(), 100);
				player.setVelocity(player.getLocation().getDirection().multiply(0.3).setY(0.6));
				new BukkitRunnable() {
					@Override
					public void run() {
						player.setVelocity(player.getLocation().getDirection().multiply(0.3).setY(-0.6));
						map.put(player.getUniqueId(), player.getLocation().getY());
					}
				}.runTaskLater(Server.plugin, 8L);
				if (player.getItemInHand().getAmount() > 1)
					player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
				else
					player.setItemInHand(null);
			}
		}
		return true;
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (player.getLocation().add(0, -0.1, 0).getBlock().getType().isSolid()) {
			if (map.containsKey(player.getUniqueId())) {
				double amp = map.get(player.getUniqueId()) - player.getLocation().getY();
				if (amp < 0)
					amp = 0;
				if (Math.pow(amp, 3) / 5 > Math.sqrt(amp) * 10)
					amp = Math.sqrt(amp) * 10;
				else
					amp = Math.pow(amp, 3) / 5;
				for (Entity e : Distance.getEntitiesInRadius(player, 5)) {
					if (e instanceof LivingEntity) {
						((LivingEntity) e).damage(amp + Stat.getPlayerStat(Stat.STR, player) / 8, player);
						ParticleEffect.CRIT.display(0, 0, 0, 1, 20, ((LivingEntity) e).getLocation().add(0, 0.5, 0), 100);
						e.getWorld().playSound(e.getLocation(), Sound.ITEM_BREAK, 1, 1);
					}
				}
				player.getWorld().playSound(player.getLocation(), Sound.ITEM_BREAK, 1, 1);
				ParticleEffect.SMOKE_LARGE.display(2, 0, 2, 0, 100, player.getLocation(), 100);
				map.remove(player.getUniqueId());
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.getItemInHand().getType() == Material.IRON_INGOT) {
			if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Abilities.SLAM.cast(player);
			}
		}
	}

}
