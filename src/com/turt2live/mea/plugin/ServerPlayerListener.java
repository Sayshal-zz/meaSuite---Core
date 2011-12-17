/*
 * meaSuite is copyright 2011/2012 of Turt2Live Programming and Sayshal Productions Modifications of the code, or any use of the code must be preauthorized by Travis Ralston (Original author) before any modifications can be used. If any code is authorized for use, this header must retain it's original state. The authors (Travis Ralston and Tyler Heuman) can request your code at any time. Upon code request you have 24 hours to present code before we will ask you to not use our code. Contact information: Travis Ralston email: minecraft@turt2live.com Tyler Heuman email: contact@sayshal.com
 */
package com.turt2live.mea.plugin;

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
