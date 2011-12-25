package me.turt2live.meaSuite.plugin;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

public class ConfigurationWriter {

	private JavaPlugin	plugin;

	public ConfigurationWriter(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	public void write() {
		try {
			File d = this.plugin.getDataFolder();
			d.mkdirs();
			File f2 = new File(this.plugin.getDataFolder() + "/config.yml");
			if (!f2.exists()) {
				f2.createNewFile();
				this.plugin.getConfig().set("meaSuite.downloadDevVersions", "false");
				this.plugin.getConfig().set("meaSuite.prename", "meaSuite");
				this.plugin.getConfig().set("meaSuite.colorVariable", "&");
				this.plugin.getConfig().set("meaSuite.stats.sendStats", true);
				this.plugin.getConfig().set("meaSuite.stats.sendConfig", false);
				// this.plugin.getConfig().set("meaSuite.stats.keepLocal", false);
				this.plugin.getConfig().set("meaSuite.stats.time", 60);
				/*
				 * this.plugin.getConfig().set("meaSuite.SQL.username", "meaCraft"); this.plugin.getConfig().set("meaSuite.SQL.password", "meaCraft"); this.plugin.getConfig().set("meaSuite.SQL.host", "localhost"); this.plugin.getConfig().set("meaSuite.SQL.port", "3306"); this.plugin.getConfig().set("meaSuite.SQL.database", "meaSuite");
				 */
				this.plugin.getConfig().options().header("meaSuite Configuration \n" +
						"'author' is a value used to determine whether or not the configuration has been changed, feel free to o anything with this\n" +
						"'downloadDevVersions' if set to 'true' then dev versions will be downloaded, otherwise only full versions\n" +
						"'prename' is the message displayed before each and every meaSuite message\n" +
						"'colorVariable' is used to determine what you want the color variable to be in messages, for example '$' could be your color variable\n" +
						"'stats: sendStats' is wether or not you want to send usage stats to Turt2Live.\n" +
						"'stats: sendConfig' send the config.yml file (this) with the usage stats\n" +
						"'stats: time' Send the stats every X minutes. Range is 5-120 minutes. \n" +
						"BE WARNED: The longer the send time, the more RAM meaSuite uses. Does not apply if you are not sending stats.\n" +
						"As well: If the number you provide is out of the range (5-120 minutes) then 60 minutes will default.");
				/*
				 * + "'SQL: username' your SQL username\n" + "'SQL: Password' your SQL password\n" + "'SQL: host' SQL Host, usually localhost\n" + "'SQL: database' SQL Database you want to use, MAKE SURE THIS IS CREATED BEFOREHAND!");
				 */
				this.plugin.saveConfig();
			}
			File dir = new File(this.plugin.getDataFolder() + "/temp");
			dir.mkdirs();
			dir = new File(this.plugin.getDataFolder() + "/stats");
			dir.mkdirs();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.plugin.reloadConfig();
	}

}
