package me.Fizz;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import me.Fizz.listeners.EntityDamageByEntityListener;
import me.Fizz.listeners.EntityDeathListener;
import me.Fizz.listeners.EntitySpawnListener;
import me.Fizz.listeners.InventoryClickListener;
import me.Fizz.listeners.InventoryCloseListener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import de.slikey.effectlib.EffectLib;
import de.slikey.effectlib.EffectManager;

public class Server extends JavaPlugin implements Listener {

	public static Server plugin;
	public HashMap<UUID, Yaml> players;
	public EffectLib effectLib;
	public EffectManager effectManager;

	@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new InventoryClickListener(this), this);
		pm.registerEvents(new InventoryCloseListener(this), this);
		pm.registerEvents(new EntityDeathListener(this), this);
		pm.registerEvents(new EntityDamageByEntityListener(this), this);
		pm.registerEvents(new EntitySpawnListener(), this);
		pm.registerEvents(Abilities.DOUBLE_JUMP.getAbility(), this);
		pm.registerEvents(Abilities.SLAM.getAbility(), this);
		Server.plugin = this;
		players = new HashMap<UUID, Yaml>();
		for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
			Yaml yaml = getPlayerYaml(op.getUniqueId());
			players.put(op.getUniqueId(), yaml);
		}
		this.effectLib = EffectLib.instance();
		this.effectManager = new EffectManager(this.effectLib);
	}

	public Yaml getPlayerYaml(UUID uuid) {
		if (this.players.containsKey(uuid)) {
			return this.players.get(uuid);
		} else {
			Yaml yaml = this.getPlayerIOFile(uuid);
			this.players.put(uuid, yaml);
			return yaml;
		}
	}

	private Yaml getPlayerIOFile(UUID uuid) {
		return new Yaml(plugin.getDataFolder().getAbsolutePath() + File.separator + "players" + File.separator + uuid + ".yml");
	}

	public void addXp(Player player, int amount) {
		Yaml yaml = getPlayerYaml(player.getUniqueId());
		yaml.add("xp", 0);
		yaml.add("level", 1);
		yaml.save();
		int xp = yaml.getInteger("xp");
		int level = yaml.getInteger("level");
		int ap = yaml.getInteger("abilityPoints");
		xp += amount;
		int nextLevel = (int) ((double) (Math.pow(level, 2) / (double) 2) + 49);
		while (xp >= nextLevel) {
			level++;
			xp -= nextLevel;
			nextLevel = (int) ((double) (Math.pow(level, 2) / (double) 2) + 49);
			player.getWorld().playSound(player.getLocation(), Sound.LEVEL_UP, 2, 1);
			ap += 5;
		}
		float ratio = (float) xp / (float) nextLevel;
		player.setExp(ratio);
		player.setLevel(level);
		yaml.set("xp", xp);
		yaml.set("level", level);
		yaml.set("abilityPoints", ap);
		yaml.save();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (label.equalsIgnoreCase("stats")) {
				Inventory inv = Bukkit.createInventory(player, 27, "Choose stats");
				Yaml yaml = getPlayerYaml(player.getUniqueId());
				yaml.add("dex", 0);
				yaml.add("str", 0);
				yaml.add("intel", 0);
				yaml.add("abilityPoints", 5);
				yaml.save();
				inv.setItem(12, Methods.createItem(new ItemStack(Material.FEATHER), ChatColor.AQUA + "Dexterity", yaml.getInteger("dex") + ""));
				inv.setItem(13, Methods.createItem(new ItemStack(Material.DIAMOND_AXE), ChatColor.DARK_RED + "Strength", yaml.getInteger("str") + ""));
				inv.setItem(14, Methods.createItem(new ItemStack(Material.BLAZE_ROD), ChatColor.DARK_BLUE + "Intelligence", yaml.getInteger("intel") + ""));
				inv.setItem(21, Methods.createItem(new ItemStack(Material.GLOWSTONE_DUST), ChatColor.GREEN + "Increase stat", null));
				inv.setItem(22, Methods.createItem(new ItemStack(Material.GLOWSTONE_DUST), ChatColor.GREEN + "Increase stat", null));
				inv.setItem(23, Methods.createItem(new ItemStack(Material.GLOWSTONE_DUST), ChatColor.GREEN + "Increase stat", null));
				inv.setItem(4, Methods.createItem(new ItemStack(Material.NETHER_STAR), ChatColor.BLUE + "Ability points", yaml.getInteger("abilityPoints") + ""));

				ItemStack i = new ItemStack(Material.PAPER);
				ArrayList<String> a = new ArrayList<String>();
				a.clear();
				double dex = yaml.getInteger("dex");
				double crit = Math.abs(((0.8 - ((dex / 150) * 3) / 10) * 100) - 100);
				a.add("Crit chance: %" + (int)crit);
				ItemMeta meta = i.getItemMeta();
				meta.setDisplayName(ChatColor.AQUA + "Stats");
				meta.setLore(a);
				i.setItemMeta(meta);
				inv.setItem(0, i);

				player.openInventory(inv);
			}
		}

		return false;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(event.getJoinMessage().replace("Nitrousspark", "Fizz"));
		if (player.getName().equals("Nitrousspark")) {
			ScoreboardManager sm = Bukkit.getScoreboardManager();
			Scoreboard board = sm.getMainScoreboard();
			Team fizz = board.getTeam("Blue");
			if (fizz != null)
				fizz.unregister();
			fizz = board.registerNewTeam("Blue");
			fizz.setPrefix(ChatColor.BLUE + "" + ChatColor.BOLD + "Fizz " + ChatColor.RESET);
			fizz.addPlayer(player);
			player.setDisplayName("Fizz");
			player.setPlayerListName("Fizz");
		}
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		event.setQuitMessage(event.getQuitMessage().replace("Nitrousspark", "Fizz"));
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		event.setDeathMessage(event.getDeathMessage().replace(ChatColor.BLUE + "" + ChatColor.BOLD + "Fizz " + ChatColor.RESET + "Nitrousspark", "Fizz"));
	}

}
