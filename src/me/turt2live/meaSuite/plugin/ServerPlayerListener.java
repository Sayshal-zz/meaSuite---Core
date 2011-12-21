package me.turt2live.meaSuite.plugin;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerPlayerListener extends PlayerListener {

	@SuppressWarnings("unused")
	private JavaPlugin	plugin;

	public ServerPlayerListener(JavaPlugin loader) {
		this.plugin = loader;
	}

	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {

	}

	@Override
	public void onPlayerMove(PlayerMoveEvent event) {

	}

	@Override
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

	}

	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {

	}

	@Override
	public void onPlayerKick(PlayerKickEvent event) {

	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {

	}
}
