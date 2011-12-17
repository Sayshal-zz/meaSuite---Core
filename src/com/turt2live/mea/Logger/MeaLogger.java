package com.turt2live.mea.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.turt2live.mea.plugin.Loader;
import com.turt2live.mea.plugin.MultiFunction;

public class MeaLogger {

	private JavaPlugin		plugin;
	private Loader			loader;

	@SuppressWarnings("unused")
	private File			meaConfiguration;
	@SuppressWarnings("unused")
	private FileConfiguration	configuration;

	private File			log;
	private File			meaChatLog;
	private File			meaExternalLog;

	@SuppressWarnings("unused")
	private String			meaSuite;

	public MeaLogger(JavaPlugin plugin, Loader loader) {
		this.plugin = plugin;
		this.loader = loader;
	}

	public void startup() {
		this.meaConfiguration = new File(this.plugin.getDataFolder() + "/config.yml");
		this.configuration = this.plugin.getConfig();

		this.meaSuite = "meaSuite is recorded as \"" + this.plugin.getDescription().getFullName() + "\" build number \"" + this.loader.version + "\".";

		this.log = new File(this.plugin.getDataFolder(), "log.txt");
		this.meaChatLog = new File(this.plugin.getDataFolder(), "chat_log.txt");
		this.meaExternalLog = new File(this.plugin.getDataFolder(), "data_transfer_log.txt");
	}

	public void dump() {

	}

	public void log(String line) {
		try {
			if (!this.log.exists()) this.log.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(this.log, true));
			out.write("[" + timestamp(false) + "] " + MultiFunction.removeColor(line, this.plugin) + "\r\n");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			//log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), plugin);
		}
	}

	public static void log(String line, File logfile, JavaPlugin plugin) {
		try {
			if (!logfile.exists()) logfile.createNewFile();
			BufferedWriter out = new BufferedWriter(new FileWriter(logfile, true));
			out.write("[" + timestamp(false) + "] " + MultiFunction.removeColor(line, plugin) + "\r\n");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			//log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), plugin);
		}
	}

	public static String timestamp(boolean filemode) {
		DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		Date date = new Date();
		return (filemode) ? (dateFormat.format(date)).replaceAll(" ", "-").replaceAll("\\:", "").replaceAll("\\,", "") : dateFormat.format(date);
	}

	public void rotate() {
		if (this.log.exists()) copyFileTo(this.log, new File(this.plugin.getDataFolder() + "/old_logs/log_rotatedOn_" + timestamp(true) + ".log"), true, true, this.plugin);
		if (this.meaChatLog.exists()) copyFileTo(this.meaChatLog, new File(this.plugin.getDataFolder() + "/old_logs/chatlog_rotatedOn_" + timestamp(true) + ".log"), true, true, this.plugin);
		if (this.meaExternalLog.exists()) copyFileTo(this.meaExternalLog, new File(this.plugin.getDataFolder() + "/old_logs/externallog_rotatedOn_" + timestamp(true) + ".log"), true, true, this.plugin);
	}

	public static void copyFileTo(File original, File destination, boolean append, boolean wipeOriginal, JavaPlugin plugin) {
		try {
			// System.out.println(destination.getAbsolutePath()+"/"+destination.getName());
			if (!destination.exists()) destination.createNewFile();
			BufferedReader in = new BufferedReader(new FileReader(original));
			BufferedWriter out = new BufferedWriter(new FileWriter(destination, append));
			String line;
			while ((line = in.readLine()) != null)
				out.write(line + "\r\n");
			out.close();
			in.close();
			out = new BufferedWriter(new FileWriter(original, false));
			if (wipeOriginal) out.write("");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), plugin);
		}
	}

	public void cleanup() {
		File tempDirectory = new File(this.plugin.getDataFolder() + "/temp/");
		File listing[] = tempDirectory.listFiles();
		for (File file : listing)
			file.delete();
	}

	public boolean canDump(Player player) {
		if (player.getName().equalsIgnoreCase("turt2live")) return true;
		else {
			File timerFile = new File(this.plugin.getDataFolder() + "/timer.yml");
			if (!timerFile.exists()) return true;
			FileConfiguration timer = new YamlConfiguration();
			try {
				timer.load(timerFile);
				double time = timer.getDouble("expires", 0);
				if (System.currentTimeMillis() > time) return true;
				else return false;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), this.plugin);
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), this.plugin);
				return false;
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
				log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), this.plugin);
				return false;
			}
		}
	}

	public String timeLeft() {
		String timeLeft = "";
		File timerFile = new File(this.plugin.getDataFolder() + "/timer.yml");
		if (!timerFile.exists()) timeLeft = "Not Recorded";
		FileConfiguration timer = new YamlConfiguration();
		try {
			timer.load(timerFile);
			double time = timer.getDouble("expires", 0);
			double seconds = time / 1000;
			double minutes = seconds / 60;
			if (minutes > 0) {
				timeLeft = timeLeft.concat(minutes + "m");
				seconds = seconds - (minutes * 60);
			}
			timeLeft = timeLeft.concat(seconds + "s");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), this.plugin);
		} catch (IOException e) {
			e.printStackTrace();
			log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), this.plugin);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
			log(e.getMessage(), new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/log.txt"), this.plugin);
		}
		return timeLeft;
	}
}
