package me.turt2live.meaSuite.plugin;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class ServerBlockListener extends BlockListener {

	@SuppressWarnings("unused")
	private Plugin	plugin;

	public ServerBlockListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {

	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {

	}
}
