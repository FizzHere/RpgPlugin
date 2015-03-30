package me.Fizz.abilities;

import me.Fizz.Cooldowns;
import me.Fizz.Server;
import me.Fizz.Stat;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Ability implements Listener {

	int requirement;
	int cooldown;
	String name;
	Stat stat;

	Server plugin;

	public Ability(int requirement, int cooldown, String name, Stat stat) {
		this.requirement = requirement;
		this.cooldown = cooldown;
		this.name = name;
		this.stat = stat;
	}

	public String getName() {
		return name;
	}

	public long getCurrentCooldown(Player player) {
		return Cooldowns.getCooldown(player, name);
	}

	public boolean cast(Player player) {
		return false;
	}

	public Stat getStat() {
		return stat;
	}

	public void setStat(Stat stat) {
		this.stat = stat;
	}
	
	public int getRequirement() {
		return this.requirement;
	}

}
