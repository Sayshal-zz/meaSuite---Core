package me.turt2live.meaSuite.Hook;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.turt2live.meaSuite.Logger.MeaLogger;

public class MeaHook {

	// Config crap
	private JavaPlugin			plugin;

	// Plugins
	protected boolean			Factions	= false;
	protected boolean			mcMMO		= false;
	protected boolean			CommandBook	= false;
	protected boolean			mChatSuite	= false;

	// Hooks
	protected CommandBookHook	CommandBook_hook;
	protected FactionsHook		Factions_hook;
	protected mcMMOHook			mcMMO_hook;
	protected mChatSuiteHook	mChatSuite_hook;

	public MeaHook(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void startup() {
		System.out.println("[meaHook] Loading...");
		MeaLogger.log("[meaHook] Loading...", new File(System.getProperty("user.dir") + "/plugins/meaSuite/log.txt"), this.plugin);
		// Note : Sayshal outsmarted me here >.>
		if (pluginExists("mcMMO")) {
			this.mcMMO_hook = new mcMMOHook(this);
			this.mcMMO = true;
		}
		System.out.println("[meaHook] mcMMO: " + this.mcMMO);
		MeaLogger.log("[meaHook] mcMMO: " + this.mcMMO, new File(System.getProperty("user.dir") + "/plugins/meaSuite/log.txt"), this.plugin);
		if (pluginExists("Factions")) {
			this.Factions_hook = new FactionsHook(this);
			this.Factions = true;
		}
		System.out.println("[meaHook] Factions: " + this.Factions);
		MeaLogger.log("[meaHook] Factions: " + this.Factions, new File(System.getProperty("user.dir") + "/plugins/meaSuite/log.txt"), this.plugin);
		if (pluginExists("CommandBook")) {
			this.CommandBook_hook = new CommandBookHook(this);
			this.CommandBook = true;
		}
		System.out.println("[meaHook] CommandBook: " + this.CommandBook);
		MeaLogger.log("[meaHook] CommandBook: " + this.CommandBook, new File(System.getProperty("user.dir") + "/plugins/meaSuite/log.txt"), this.plugin);
		if (pluginExists("mChat")) System.out.println("[meaHook] mChat is no longer used! Get mChatSuite!");
		if (pluginExists("mChatSuite")) {
			this.mChatSuite_hook = new mChatSuiteHook(this);
			this.mChatSuite = true;
		}
		System.out.println("[meaHook] mChatSuite: " + this.mChatSuite);
		MeaLogger.log("[meaHook] mChatSuite: " + this.mChatSuite, new File(System.getProperty("user.dir") + "/plugins/meaSuite/log.txt"), this.plugin);
		System.out.println("[meaHook] Loaded!");
		MeaLogger.log("[meaHook] Loaded!", new File(System.getProperty("user.dir") + "/plugins/meaSuite/log.txt"), this.plugin);
	}

	private boolean pluginExists(String name) {
		PluginManager pm = Bukkit.getServer().getPluginManager();
		return (pm.getPlugin(name) != null) ? true : false;
	}

	public void onJoin(String player, String source) {

	}

	public void onLeave(String player, String source, boolean kicked, String kickmessage) {

	}
	public void onMessage(String player, String source, String message) {

	}

	public void onCommand(String player, String command) {

	}

	public void send(String message, String source) {

	}

	public void send(String message, String source, boolean meaOnlyMode) {

	}

	public String addColor(String message) {
		if (mChatSuite) mChatSuite_hook.addColor(message);
		return message;
	}

	public String getNode(String node) {
		return this.plugin.getConfig().getString("meaHook." + node);
	}

	/** BELOW HERE IS JUST CONTROL STUFF FOR WHEN I SCREW UP **/

	public void onCommand(Player player, String command) {
		onCommand(player.getName(), command);
	}

	public void onLeave(Player player, String source, boolean kicked, String kickmessage) {
		onLeave(player.getName(), source, kicked, kickmessage);
	}

	public void onJoin(Player player, String source) {
		onJoin(player.getName(), source);
	}

	public void onMessage(Player player, String source, String message) {
		onMessage(player.getName(), source, message);
	}


}
