package me.turt2live.meaSuite.API;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijikokun.register.payment.Methods;

public class EconomyHook {

	private JavaPlugin	meaSuite;
	private MeaAPI		api;

	public EconomyHook(JavaPlugin plugin, MeaAPI api) {
		meaSuite = plugin;
		this.api = api;
	}

	public boolean isLoaded() {
		return Methods.hasMethod();
	}

	public Account setPlayer(Player player) {
		return new Account(player, meaSuite, api);
	}
}
