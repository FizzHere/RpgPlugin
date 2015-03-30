package me.Fizz.abilities;

import me.Fizz.Abilities;
import me.Fizz.Cooldowns;
import me.Fizz.ParticleEffect;
import me.Fizz.Stat;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJump extends Ability implements Listener {

	public DoubleJump(int requirement, int cooldown, String name, Stat stat) {
		super(requirement, cooldown, name, stat);
	}

	@Override
	public boolean cast(Player player) {
		if (Cooldowns.tryCooldown(player, name, cooldown)) {
			player.setVelocity(player.getLocation().getDirection().multiply(0.3).setY(0.8));
			ParticleEffect.FIREWORKS_SPARK.display(0, 0, 0, (float) 0.1, 20, player.getLocation().add(0, 1.2, 0), 100);
			player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_WINGS, (float) 0.5, 1);
		}
		return true;
	}

	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if (player.getGameMode() != GameMode.CREATIVE) {
			if (Stat.getPlayerStat(Stat.DEX, player) >= requirement) {
				Abilities.DOUBLE_JUMP.cast(player);
				player.setFlying(false);
				player.setAllowFlight(false);
			}
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (Stat.getPlayerStat(Stat.DEX, player) >= requirement) {
			if (player.getLocation().add(0, -0.1, 0).getBlock().getType().isSolid()) {
				player.setFlying(false);
				player.setAllowFlight(true);
			}
		}
	}

}
