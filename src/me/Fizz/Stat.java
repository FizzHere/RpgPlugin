package me.Fizz;

import org.bukkit.entity.Player;

public enum Stat {

	DEX(0, 0), STR(1, 0), INT(2, 0);

	private int id;
	private int amount;

	private Stat(int id, int amount) {
		this.id = id;
		this.amount = amount;
	}

	public int getId() {
		return id;
	}

	public int getAmount() {
		return amount;
	}

	public Stat setAmount(int i) {
		amount = i;
		return this;
	}

	public static int getPlayerStat(Stat stat, Player player) {
		Yaml yaml = Server.plugin.getPlayerYaml(player.getUniqueId());
		if (stat.name().equals("DEX")) {
			return yaml.getInteger("dex");
		} else if (stat.name().equals("STR")) {
			return yaml.getInteger("str");
		} else if (stat.name().equals("int")) {
			return yaml.getInteger("intel");
		} else {
			return 0;
		}
	}

}
