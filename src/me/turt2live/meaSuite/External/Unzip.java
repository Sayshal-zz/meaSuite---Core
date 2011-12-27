package me.turt2live.meaSuite.External;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import me.turt2live.meaSuite.Logger.MeaLogger;

import org.bukkit.plugin.Plugin;

public class Unzip {

	private File	log	= new File(System.getProperty("user.dir") + "/plugins/meaSuite/meaLogger/data_transfer.txt");

	public static final void copyInputStream(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len;
		while ((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		in.close();
		out.close();
	}

	public Unzip(String arg0, String path, Plugin plugin) {
		@SuppressWarnings("rawtypes")
		Enumeration entries;
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(arg0);
			entries = zipFile.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();

				if (entry.isDirectory()) {
					(new File(path + entry.getName())).mkdir();
					MeaLogger.log("Unzip: " + path + " :: " + entry.getName(), this.log, plugin);
					continue;
				}
				copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(path + entry.getName())));
			}

			zipFile.close();
		} catch (IOException ioe) {
			System.err.println("Unhandled exception:");
			ioe.printStackTrace();
			return;
		}
	}

}