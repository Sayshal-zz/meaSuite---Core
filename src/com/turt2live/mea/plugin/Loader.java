/*
 * meaSuite is copyright 2011/2012 of Turt2Live Programming and Sayshal Productions Modifications of the code, or any use of the code must be preauthorized by Travis Ralston (Original author) before any modifications can be used. If any code is authorized for use, this header must retain it's original state. The authors (Travis Ralston and Tyler Heuman) can request your code at any time. Upon code request you have 24 hours to present code before we will ask you to not use our code. Contact information: Travis Ralston email: minecraft@turt2live.com Tyler Heuman email: contact@sayshal.com
 */
package com.turt2live.mea.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.turt2live.mea.External.Download;
import com.turt2live.mea.Logger.MeaLogger;

public class Loader extends JavaPlugin {

	public JavaPlugin			plugin				= this;
	public final Logger			logger				= Logger.getLogger("Minecraft");
	public ServerPlayerListener	playerListener;
	public ServerBlockListener	blockListener;
	public int					version				= 529;
	private boolean				updateBroadcasted	= false;
	private MeaLogger			meaLog;

	@Override
	public void onEnable() {
		long start = System.currentTimeMillis();
		System.out.println("[" + this.plugin.getDescription().getFullName() + "] Loading!");
		meaLog = new MeaLogger(this, this);
		Runnable events = new Runnable() {
			@Override
			public void run() {
				while (!Loader.this.plugin.isEnabled())
					;
				@SuppressWarnings("unused")
				PluginManager pm = Bukkit.getServer().getPluginManager();
				// pm.registerEvent(Event.Type.PLAYER_JOIN, Loader.this.playerListener, Event.Priority.Normal, Loader.this.plugin);
				// pm.registerEvent(Event.Type.PLAYER_MOVE, Loader.this.playerListener, Event.Priority.Normal, Loader.this.plugin);
				// pm.registerEvent(Event.Type.PLAYER_COMMAND_PREPROCESS, Loader.this.playerListener, Event.Priority.Normal, Loader.this.plugin);
				// pm.registerEvent(Event.Type.PLAYER_QUIT, Loader.this.playerListener, Event.Priority.Normal, Loader.this.plugin);
				// pm.registerEvent(Event.Type.PLAYER_KICK, Loader.this.playerListener, Event.Priority.Normal, Loader.this.plugin);
				// pm.registerEvent(Event.Type.PLAYER_CHAT, Loader.this.playerListener, Event.Priority.Highest, Loader.this.plugin); // High = ran last, mChatSuite override
				// Block events
				// pm.registerEvent(Event.Type.BLOCK_PLACE, Loader.this.blockListener, Event.Priority.Normal, Loader.this.plugin);
				// pm.registerEvent(Event.Type.BLOCK_BREAK, Loader.this.blockListener, Event.Priority.Normal, Loader.this.plugin);
				System.out.println("[meaSuite] Event Handlers Loaded");
				Loader.this.meaLog.log("[meaSuite] Event Handlers Loaded");
			}
		};
		Thread eventsThread = new Thread(events);
		eventsThread.start();
		// Update check
		Runnable update = new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						boolean isDev = false;
						if (getNode("meaSuite.downloadDevVersions").equalsIgnoreCase("true")) isDev = true;
						BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://68.148.10.71/mc/plugins/version.txt").openStream()));
						String line;
						while ((line = in.readLine()) != null) {
							int v = Integer.parseInt(line);
							if (v > Loader.this.version) {
								if (!Loader.this.updateBroadcasted) {
									@SuppressWarnings("unused")
									Download download = new Download(new URL("http://68.148.10.71/mc/plugins/meaSuite.jar"), System.getProperty("user.dir") + "/plugins/meaSuite.jar", true, Loader.this.plugin);
									Loader.this.updateBroadcasted = true;
									Loader.this.meaLog.log("Downloaded meaSuite.jar build " + v + " (Current Version " + Loader.this.version + ")");
								}
								Loader.this.meaLog.log("** meaSuite build " + v + " available (Current Build: " + Loader.this.version + ")! Restart server to use it. (For changes type: /mea changelog)");
								System.err.println("** meaSuite build " + v + " available (Current Build: " + Loader.this.version + ")! Restart server to use it. (For changes type: /mea changelog)");
								Bukkit.getServer().broadcastMessage(ChatColor.DARK_GREEN + "** meaSuite build " + v + " available (Current Build: " + Loader.this.version + ")! Restart server to use it.");
							} else if (isDev) {
								BufferedReader dev = new BufferedReader(new InputStreamReader(new URL("http://68.148.10.71/mc/plugins/version_dev.txt").openStream()));
								String line2;
								while ((line2 = dev.readLine()) != null) {
									int vDev = Integer.parseInt(line2);
									if (vDev > Loader.this.version) {
										if (!Loader.this.updateBroadcasted) {
											@SuppressWarnings("unused")
											Download download = new Download(new URL("http://68.148.10.71/mc/plugins/meaSuite.jar"), System.getProperty("user.dir") + "/plugins/meaSuite.jar", true, Loader.this.plugin);
											Loader.this.updateBroadcasted = true;
											Loader.this.meaLog.log("Downloaded meaSuite.jar DEV build " + v + " (Current Version " + Loader.this.version + ")");
										}
										Loader.this.meaLog.log("** meaSuite DEV build " + v + " available (Current Build: " + Loader.this.version + ")! Restart server to use it. (For changes type: /mea changelog)");
										System.err.println("** meaSuite DEV build " + v + " available (Current Build: " + Loader.this.version + ")! Restart server to use it. (For changes type: /mea changelog)");
										Bukkit.getServer().broadcastMessage(ChatColor.DARK_GREEN + "** meaSuite DEV build " + v + " available (Current Build: " + Loader.this.version + ")! Restart server to use it.");
									}
								}
								dev.close();
							}
						}
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
						Loader.this.meaLog.log(e.getMessage());
					}
					try {
						if (!Loader.this.updateBroadcasted) Thread.sleep(1000);
						else Thread.sleep(30000);
					} catch (Exception e) {}
				}
			}
		};
		Thread updateThread = new Thread(update);
		updateThread.start();
		long end = System.currentTimeMillis();
		double time = (end - start) / 1000;
		System.out.println("[" + this.plugin.getDescription().getFullName() + "] Loaded in " + time + "s!");
		this.meaLog.log("[" + this.plugin.getDescription().getFullName() + "] Loaded in " + time + "s!");
	}

	@Override
	public void onDisable() {
		System.out.println("[" + this.plugin.getDescription().getName() + "] Cleaning our plates...");
		this.meaLog.rotate();
		this.meaLog.cleanup();
		System.out.println("[" + this.plugin.getDescription().getFullName() + "] Plates Cleaned! Please drip dry. (Plugin Disabled or Unloaded)");
		this.meaLog.log("[" + this.plugin.getDescription().getFullName() + "] Disabled or Unloaded!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd, String[] args) {
		try {
			if (sender instanceof Player) System.out.println();
			else System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
			this.meaLog.log(e.getMessage());
		}
		return false;
	}

	@SuppressWarnings("unused")
	private void reloadSelf() {
		this.meaLog.log("Reload complete.");
	}

	@SuppressWarnings("static-access")
	public static String getNode(String node) {
		FileConfiguration config = new YamlConfiguration().loadConfiguration(new File(System.getProperty("user.dir") + "/plugins/meaSuite/config.yml"));
		return config.getString(node);
	}
}
