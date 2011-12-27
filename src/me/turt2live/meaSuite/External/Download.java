package me.turt2live.meaSuite.External;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import me.turt2live.meaSuite.API.MeaAPI;

import org.bukkit.plugin.Plugin;

public class Download {
	public static double	downloadSize		= 0;
	public static double	downloadAmount		= 0;
	public static int		lastDownloadPercent	= 0;

	public static String getDownloadStatus() {
		double pert = (downloadAmount / downloadSize) * 100;
		int percent = (int) pert;
		return "Downloaded " + downloadAmount + " bytes of " + downloadSize + " bytes. " + percent + "%";
	}

	public static int getDownloadPercent() {
		return (int) ((downloadAmount / downloadSize) * 100);
	}

	public int getDownloadStatusInt() {
		if (downloadAmount == downloadSize) return 1;
		else return 0;
	}

	public static int getFileSize() throws Exception {
		FileInputStream fstream = new FileInputStream("filesize.txt");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		int retValue = 0;
		while ((strLine = br.readLine()) != null)
			retValue = Integer.parseInt(strLine);
		in.close();
		return retValue;
	}

	public Download(URL url, String filename, boolean println, Plugin plugin) throws Exception {
		URLConnection con;
		DataInputStream dis;
		FileOutputStream fos;
		byte[] fileData;
		try {
			con = url.openConnection();
			dis = new DataInputStream(con.getInputStream());
			fileData = new byte[con.getContentLength()];
			downloadSize = fileData.length;
			int t1 = 0;
			for (int x = 0; x < fileData.length; x++) {
				fileData[x] = dis.readByte();
				if (t1 == 0) t1 = 1;
				downloadAmount = x + 1;
				if (getDownloadPercent() == 100) {
					String parts[] = filename.split("\\/");
					String file = parts[parts.length - 2];
					if (println) {
						System.out.println(file + " :: " + getDownloadStatus());
						MeaAPI api = new MeaAPI();
						api.log(file + " :: " + getDownloadStatus(), new File(plugin.getDataFolder(), "data_transfer_log.txt"));
					}
					lastDownloadPercent = getDownloadPercent();
				}
			}
			dis.close();
			fos = new FileOutputStream(new File(filename));
			fos.write(fileData);
			fos.close();
		} catch (MalformedURLException m) {
			System.out.println(m);
		} catch (IOException io) {
			System.out.println(io);
		}
	}
}
