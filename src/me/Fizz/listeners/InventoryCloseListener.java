package me.Fizz.listeners;

import me.Fizz.Server;
import me.Fizz.Yaml;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryCloseListener implements Listener {

	public Server plugin;

	public InventoryCloseListener(Server plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getName().equals("Choose stats")) {
			Player player = (Player) event.getPlayer();
			Yaml yaml = plugin.getPlayerYaml(player.getUniqueId());
			int abilityPoints = Integer.parseInt(inv.getItem(4).getItemMeta().getLore().get(0));
			yaml.set("abilityPoints", abilityPoints);
			int dex = Integer.parseInt(inv.getItem(12).getItemMeta().getLore().get(0));
			yaml.set("dex", dex);
			int str = Integer.parseInt(inv.getItem(13).getItemMeta().getLore().get(0));
			yaml.set("str", str);
			int intel = Integer.parseInt(inv.getItem(14).getItemMeta().getLore().get(0));
			yaml.set("intel", intel);
			yaml.save();
		}
	}

}
