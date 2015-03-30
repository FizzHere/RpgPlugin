package me.Fizz;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class Methods {

	public static ItemStack setSkin(ItemStack item, String nick) {
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		meta.setOwner(nick);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(ItemStack i, String name, String lore) {
		ArrayList<String> a = new ArrayList<String>();
		a.clear();
		a.add(lore);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(a);
		i.setItemMeta(meta);
		return i;
	}

	public static ItemStack createColorArmor(ItemStack i, Color c) {
		LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
		meta.setColor(c);
		i.setItemMeta(meta);
		return i;
	}

}
