package me.turt2live.meaSuite.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import me.turt2live.meaSuite.API.MeaAPI;
import me.turt2live.meaSuite.API.MeaAPI.VERSION;
import me.turt2live.meaSuite.Logger.MeaLogger;
import me.turt2live.meaSuite.statistics.UsageStatistics;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Loader extends JavaPlugin {

	public Plugin			plugin				= this;
	public final Logger			logger				= Logger.getLogger("Minecraft");
	public ServerPlayerListener	playerListener;
	public ServerBlockListener	blockListener;
	public int					version				= 0;
	private MeaLogger			meaLog;
	private UsageStatistics		stats;

	public MeaAPI				api;

	@Override
	public void onEnable() {
		long start = System.currentTimeMillis();
		System.out.println("[" + this.plugin.getDescription().getFullName() + "] Loading!");
		ConfigurationWriter configwriter = new ConfigurationWriter(this);
		configwriter.write();
		meaLog = new MeaLogger(this, this);
		meaLog.log("[" + this.plugin.getDescription().getFullName() + "] Loading!");
		api = new MeaAPI();
		version = VERSION.CORE;
		stats = new UsageStatistics(this, this);
		meaLog.log("Flushing Usage Statistics...");
		stats.flush();
		meaLog.log("Usage Statistics Flushed!");
		stats.start();
		Runnable events = new Runnable() {
			@Override
			public void run() {
				while (!plugin.isEnabled())
					;
				playerListener = new ServerPlayerListener(plugin, stats);
				PluginManager pm = Bukkit.getServer().getPluginManager();
				pm.registerEvent(Event.Type.PLAYER_JOIN, Loader.this.playerListener, Event.Priority.Normal, plugin);
				pm.registerEvent(Event.Type.PLAYER_MOVE, Loader.this.playerListener, Event.Priority.Normal, plugin);
				pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, Loader.this.playerListener, Event.Priority.Normal, plugin);
				pm.registerEvent(Event.Type.PLAYER_QUIT, Loader.this.playerListener, Event.Priority.Normal, plugin);
				pm.registerEvent(Event.Type.PLAYER_KICK, Loader.this.playerListener, Event.Priority.Normal, plugin);
				pm.registerEvent(Event.Type.PLAYER_CHAT, Loader.this.playerListener, Event.Priority.Normal, plugin);
				pm.registerEvent(Event.Type.PLAYER_PRELOGIN, Loader.this.playerListener, Event.Priority.Normal, plugin);
				System.out.println("[meaSuite] Event Handlers Loaded");
				meaLog.log("[meaSuite] Event Handlers Loaded");
			}
		};
		Thread eventsThread = new Thread(events);
		eventsThread.start();
		// TODO: Update Check
		long end = System.currentTimeMillis();
		double time = (end - start) / 1000;
		System.out.println("[" + this.plugin.getDescription().getFullName() + "] Loaded in " + time + "s!");
		this.meaLog.log("[" + this.plugin.getDescription().getFullName() + "] Loaded in " + time + "s!");
	}

	@Override
	public void onDisable() {
		System.out.println("[" + this.plugin.getDescription().getName() + "] Cleaning our plates...");
		this.meaLog.log("[" + this.plugin.getDescription().getFullName() + "] Disabled or Unloaded!");
		this.meaLog.rotate();
		this.meaLog.cleanup();
		System.out.println("[" + this.plugin.getDescription().getFullName() + "] Plates Cleaned! Please drip dry. (Plugin Disabled or Unloaded)");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
		try {
			if (sender instanceof Player) {
				if (cmd.equalsIgnoreCase("mea") && ((Player) sender).hasPermission("meaSuite.mea")) {
					if (args.length > 0) {
						if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl") && ((Player) sender).hasPermission("meaSuite.reload")) {
							reloadSelf();
							((Player) sender).sendMessage(MultiFunction.getPre(this) + " Reloaded.");
						} else if (args[0].equalsIgnoreCase("changelog")) {
							BufferedReader clog = new BufferedReader(new InputStreamReader(new URL("http://68.148.10.71/mc/plugins/version_dev.txt").openStream()));
							String line;
							while ((line = clog.readLine()) != null)
								((Player) sender).sendMessage(MultiFunction.getPre(plugin) + " " + line);
							clog.close();
						} else if (!((Player) sender).hasPermission("meaSuite.reload")) ((Player) sender).sendMessage(MultiFunction.getPre(this) + " You can't do that!");
						else ((Player) sender).sendMessage(MultiFunction.getPre(this) + " Unknown command");
					} else ((Player) sender).sendMessage(MultiFunction.getPre(this) + " Version " + getDescription().getVersion());
				} else ((Player) sender).sendMessage(MultiFunction.getPre(this) + " You can't do that!");
			} else if(cmd.equalsIgnoreCase("mea")) if(args.length>0){
				if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
					reloadSelf();
					System.out.println("[meaSuite] Reloaded.");
				} else System.out.println("[meaSuite] Unknown command");
			} else System.out.println("[meaSuite] Version "+getDescription().getVersion());
		} catch (Exception e) {
			e.printStackTrace();
			this.meaLog.log(e.getMessage());
		}
		return false;
	}

	private void reloadSelf() {
		reloadConfig();
		this.meaLog.log("Reload complete.");
	}

	public static String getNode(String node) {
		FileConfiguration config = new YamlConfiguration();
		try {
			config.load(new File(System.getProperty("user.dir") + "/plugins/meaSuite/config.yml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			return "";
		}
		return config.getString(node);
	}
}
