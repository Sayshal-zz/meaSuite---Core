package me.turt2live.meaSuite.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import me.turt2live.meaSuite.External.Zip;
import me.turt2live.meaSuite.Logger.MeaLogger;
import me.turt2live.meaSuite.grapher.Grapher;
import me.turt2live.meaSuite.plugin.Loader;

import org.bukkit.plugin.java.JavaPlugin;

public class UsageStatistics {

	private JavaPlugin	plugin;
	private Loader		meaSuite;
	private File		directory;

	public UsageStatistics(JavaPlugin p, Loader l) {
		this.plugin = p;
		this.meaSuite = l;
		directory = new File(plugin.getDataFolder() + "/temp/stats");
		directory.mkdirs();
	}

	public void flush() {
		for (File file : directory.listFiles())
			if (file.isFile()) file.delete();
	}

	public void recordStat(String statname, int amount) {
		File file = new File(directory + "/" + statname + "_" + getMinute() + ".stat");
		if (file.exists()) try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ((line = in.readLine()) != null)
				amount = amount + Integer.parseInt(line.replaceAll("\\r", "").replaceAll("\\n", ""));
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			meaSuite.api.log(e);
		}
		else try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			meaSuite.api.log(e);
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(file, false));
			out.write(amount);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			meaSuite.api.log(e);
		}
	}

	private int getMinute() {
		Calendar cal = new GregorianCalendar();
		return cal.get(Calendar.MINUTE);
	}

	public void start() {
		Runnable thread = new Runnable() {
			@Override
			public void run() {
				try {
					while (true) {
						int interval = plugin.getConfig().getInt("meaSuite.stats.time");
						boolean run = plugin.getConfig().getBoolean("meaSuite.stats.send");
						Thread.sleep(interval * 60 * 1000);
						if (run) {
							File to = new File(directory + "/send/");
							// Create graph
							Grapher graph = new Grapher(plugin, meaSuite, directory);
							graph.createGraph("Logins vs PreLogins");
							// graph.addSeries(dataX, dataY, "login");
							// graph.addSeries(dataX, dataY, "prelogin");
							File g = graph.generate();
							graph.clear();
							to.mkdirs();
							MeaLogger.copyFileTo(g, to, false, false, meaSuite);
							// Write HTML file
							// TODO: Write it
							// Zip it
							@SuppressWarnings("unused")
							Zip zip = new Zip(to + "/", to + "/pack.zip", meaSuite);
							// Send it
							// TODO: Send it
							// Cleanup
							// TODO: Cleanup
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					meaSuite.api.log(e);
				}
			}
		};
		Thread th = new Thread(thread);
		th.start();
	}

}
