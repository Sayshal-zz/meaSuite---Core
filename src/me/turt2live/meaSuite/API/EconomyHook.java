package me.turt2live.meaSuite.API;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijikokun.register.payment.Methods;

public class EconomyHook {

	private Plugin	meaSuite;
	private MeaAPI		api;

	public EconomyHook(Plugin plugin, MeaAPI api) {
		meaSuite = plugin;
		this.api = api;
		Methods.getMethod();
	}

	public boolean isLoaded() {
		Methods.getMethod();
		return Methods.hasMethod();
	}

	public Account setPlayer(Player player) {
		return new Account(player, meaSuite, api);
	}
}
