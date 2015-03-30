package me.Fizz.listeners;

import me.Fizz.Distance;
import me.Fizz.Server;
import me.Fizz.Stat;
import me.Fizz.Yaml;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import de.slikey.effectlib.effect.TextEffect;
import de.slikey.effectlib.util.ParticleEffect;

public class EntityDamageByEntityListener implements Listener {

	Server plugin;

	public EntityDamageByEntityListener(Server plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Player) {
			if (event.getEntity() instanceof LivingEntity) {
				Player player = (Player) event.getDamager();
				int str = Stat.getPlayerStat(Stat.STR, player);
				Yaml yaml = plugin.getPlayerYaml(player.getUniqueId());
				int level = yaml.getInteger("level");
				double random = Math.random();
				int dex = Stat.getPlayerStat(Stat.DEX, player);
				double crit = (0.8 - ((dex / 150) * 3) / 10) * 100;
				if (random > crit)
					random += 2;
				double add = (random * (level / 8)) - (level / 16);
				event.setDamage(event.getDamage() + (Math.sqrt(str) * 1.3) + add);
				TextEffect effect = new TextEffect(plugin.effectManager);
				Location entLoc = ((LivingEntity) event.getEntity()).getEyeLocation();
				Location loc = player.getLocation();
				Location newLoc = Distance.lookAt(loc, entLoc);
				newLoc.setX(entLoc.getX());
				newLoc.setY(entLoc.getY() + 2);
				newLoc.setZ(entLoc.getZ());
				effect.setLocation(newLoc);
				if (random > 0.8)
					effect.particle = ParticleEffect.FLAME;
				effect.text = Math.round(event.getDamage()) + "";
				effect.delay = 0;
				effect.iterations = 1;
				effect.period = 1;
				effect.start();
			}
		}
	}

}
