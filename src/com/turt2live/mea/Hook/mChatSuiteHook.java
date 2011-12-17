package com.turt2live.mea.Hook;

import java.util.List;

import net.D3GN.MiracleM4n.mChatSuite.mChatAPI;
import net.D3GN.MiracleM4n.mChatSuite.mChatSuite;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class mChatSuiteHook {

	@SuppressWarnings("unused")
	private MeaHook		hook;
	private mChatAPI	mChat;

	@SuppressWarnings("static-access")
	public mChatSuiteHook(MeaHook hook) {
		this.hook = hook;
		this.mChat = new mChatSuite().API;
	}

	public String getGroup(Player player) {
		return this.mChat.addColour(this.mChat.getPrefix(player));
	}

	public String getGroup(String player) {
		List<Player> matches = Bukkit.matchPlayer(player);
		if (matches.size() > 0) return this.mChat.addColour(this.mChat.getPrefix(matches.get(0)));
		return "Dunno";
	}
}
