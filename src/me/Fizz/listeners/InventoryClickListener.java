package me.Fizz.listeners;

import java.util.Arrays;

import me.Fizz.Abilities;
import me.Fizz.ParticleEffect;
import me.Fizz.Server;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryClickListener implements Listener {

	public Server plugin;

	public InventoryClickListener(Server plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getInventory();
		if (event.getCurrentItem() == null)
			return;
		if (event.getCurrentItem().getType() == Material.AIR)
			return;
		if (inv.getName().equals("Choose stats")) {
			event.setCancelled(true);
			if (ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()).equals("Increase stat")) {
				ItemStack ability = inv.getItem(event.getSlot() - 9);
				ItemStack abilityPoints = inv.getItem(4);
				ItemMeta ap = abilityPoints.getItemMeta();
				int amount = Integer.parseInt(ap.getLore().get(0));
				ItemMeta meta = ability.getItemMeta();
				if (amount > 0) {
					int newLevel = Integer.parseInt(meta.getLore().get(0)) + 1;
					meta.setLore(Arrays.asList(newLevel + ""));
					ability.setItemMeta(meta);
					int apNew = Integer.parseInt(ap.getLore().get(0)) - 1;
					ap.setLore(Arrays.asList(apNew + ""));
					abilityPoints.setItemMeta(ap);
					for (Abilities a : Abilities.values()) {
						if (ChatColor.stripColor(ability.getItemMeta().getDisplayName()).equalsIgnoreCase("dexterity")) {
							if (a.getStat().name().equalsIgnoreCase("dex")) {
								if (a.getRequirement() == Integer.parseInt(meta.getLore().get(0))) {
									event.getWhoClicked().sendMessage(ChatColor.GREEN + "You unlocked " + a.getName() + "!");
									event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.LEVEL_UP, 1, 1);
									ParticleEffect.ENCHANTMENT_TABLE.display(0, 0, 0, 1, 50, event.getWhoClicked().getEyeLocation().add(0, 1, 0), 100);
								}
							}
						}
						if (ChatColor.stripColor(ability.getItemMeta().getDisplayName()).equalsIgnoreCase("intelligence")) {
							if (a.getStat().name().equalsIgnoreCase("int")) {
								if (a.getRequirement() == Integer.parseInt(meta.getLore().get(0))) {
									event.getWhoClicked().sendMessage(ChatColor.GREEN + "You unlocked " + a.getName() + "!");
									event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.LEVEL_UP, 1, 1);
									ParticleEffect.ENCHANTMENT_TABLE.display(0, 0, 0, 1, 50, event.getWhoClicked().getEyeLocation().add(0, 1, 0), 100);
								}
							}
						}
						if (ChatColor.stripColor(ability.getItemMeta().getDisplayName()).equalsIgnoreCase("strength")) {
							if (a.getStat().name().equalsIgnoreCase("str")) {
								if (a.getRequirement() == Integer.parseInt(meta.getLore().get(0))) {
									event.getWhoClicked().sendMessage(ChatColor.GREEN + "You unlocked " + a.getName() + "!");
									event.getWhoClicked().getWorld().playSound(event.getWhoClicked().getLocation(), Sound.LEVEL_UP, 1, 1);
									ParticleEffect.ENCHANTMENT_TABLE.display(0, 0, 0, 1, 50, event.getWhoClicked().getEyeLocation().add(0, 1, 0), 100);
								}
							}
						}
					}
				}
				if (ChatColor.stripColor(ability.getItemMeta().getDisplayName()).equalsIgnoreCase("dexterity")) {
					// default 0.2
					float speed = (float) ((double) ((double) Integer.parseInt(meta.getLore().get(0)) / (double) 150) / (double) 10);
					if (speed > 0.1)
						speed = (float) 0.1;
					speed += 0.2;
					((Player) event.getWhoClicked()).setWalkSpeed(speed);
				}
			}
		}
	}

}
