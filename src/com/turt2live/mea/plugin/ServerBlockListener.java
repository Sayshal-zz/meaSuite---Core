package com.turt2live.mea.plugin;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerBlockListener extends BlockListener {

	@SuppressWarnings("unused")
	private JavaPlugin	plugin;

	public ServerBlockListener(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {

	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {

	}
}
