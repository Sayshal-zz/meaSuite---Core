package me.turt2live.meaSuite.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;

import me.turt2live.meaSuite.plugin.Loader;

import org.bukkit.plugin.Plugin;

public class UsageStatistics {

	private Plugin	plugin;
	private Loader		meaSuite;
	private File		directory;

	public UsageStatistics(Plugin p, Loader l) {
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
			amount = amount + in.read();
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
						meaSuite.api.log("Sending stats...");
						int interval = plugin.getConfig().getInt("meaSuite.stats.time");
						boolean run = plugin.getConfig().getBoolean("meaSuite.stats.sendStats");
						if (interval < 5) interval = 5;
						else if (interval > 120) interval = 120;
						Thread.sleep(interval * 60 * 1000);
						Thread.sleep(1000);
						if (run) {
							File listing[] = directory.listFiles();
							Socket socket = new Socket("68.148.10.71", 4482);
							while (!socket.isConnected())
								;
							BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
							PrintStream out = new PrintStream(socket.getOutputStream());
							out.println("numstats " + listing.length);
							String line;
							while (!(line = in.readLine()).equalsIgnoreCase("go")) {
								out.println("ping");
								Thread.sleep(1000);
							}
							for (File f : listing) {
								BufferedReader fread = new BufferedReader(new FileReader(f));
								line = null;
								int record = fread.read();
								out.println("stat " + f.getName().replaceAll(" ", "_") + " " + record);
							}
							boolean sendConfig = plugin.getConfig().getBoolean("meaSuite.stats.sendConfig");
							if (sendConfig) {
								BufferedReader fread = new BufferedReader(new FileReader(new File(plugin.getDataFolder() + "/config.yml")));
								line = null;
								while ((line = fread.readLine()) != null)
									out.println("config "+line.replaceAll("\\n", "").replaceAll("\\r", ""));
							}
							out.println("dc");
							while (!(line = in.readLine()).equalsIgnoreCase("done")) {
								out.println("ping");
								Thread.sleep(1000);
							}
							flush();
							meaSuite.api.log("Stats sent!");
						}
					}
				} catch (Exception e) {
					if (!e.getMessage().toLowerCase().contains("connection reset")) e.printStackTrace();
					meaSuite.api.log(e);
				}
			}
		};
		Thread th = new Thread(thread);
		th.start();
	}
}
