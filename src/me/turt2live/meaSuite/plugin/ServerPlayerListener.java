package me.turt2live.meaSuite.plugin;

import me.turt2live.meaSuite.statistics.UsageStatistics;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPlayerListener extends PlayerListener {

	@SuppressWarnings("unused")
	private JavaPlugin	plugin;
	private UsageStatistics	stats;

	public ServerPlayerListener(JavaPlugin loader, UsageStatistics s) {
		this.plugin = loader;
		this.stats = s;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		stats.recordStat("onLogin", 1);
	}

	@Override
	public void onPlayerPreLogin(PlayerPreLoginEvent event) {
		stats.recordStat("onPreLogin", 1);
	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {

	}

	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
		stats.recordStat("onCommand", 1);
	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		stats.recordStat("onQuit", 1);
	}

	@Override
	public void onPlayerKick(PlayerKickEvent event) {
		stats.recordStat("onKick", 1);
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
		stats.recordStat("onChat", 1);
	}
}
