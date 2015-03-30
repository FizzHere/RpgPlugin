package me.Fizz;

import org.bukkit.entity.Player;

import me.Fizz.abilities.Ability;
import me.Fizz.abilities.DoubleJump;
import me.Fizz.abilities.Slam;

public enum Abilities {

	DOUBLE_JUMP(new DoubleJump(40, 0, "Double Jump", Stat.DEX)), SLAM(new Slam(80, 0, "Slam", Stat.STR));

	private Ability a;

	private Abilities(Ability a) {
		this.a = a;
	}

	public long getCurrentCooldown(Player player) {
		return a.getCurrentCooldown(player);
	}

	public String getName() {
		return a.getName();
	}
	
	public Stat getStat() {
		return a.getStat();
	}
	
	public int getRequirement() {
		return a.getRequirement();
	}
	
	public Ability getAbility() {
		return a;
	}

	public boolean cast(Player player) {
		Yaml yaml = Server.plugin.getPlayerYaml(player.getUniqueId());
		if (a.getStat().name().equals("DEX")) {
			a.getStat().setAmount(yaml.getInteger("dex"));
		} else if (a.getStat().name().equals("STR")) {
			a.getStat().setAmount(yaml.getInteger("str"));
		} else if (a.getStat().name().equals("int")) {
			a.getStat().setAmount(yaml.getInteger("intel"));
		}
		return a.cast(player);
	}

}
