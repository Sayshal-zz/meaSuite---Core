package me.turt2live.meaSuite.External;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import me.turt2live.meaSuite.plugin.Loader;

public class Zip {

	public Zip(String from, String to, Loader meaSuite) {
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(to));
			zipDir(from, zos, meaSuite);
			zos.close();
		} catch (Exception e) {
			e.printStackTrace();
			meaSuite.api.log(e);
		}
	}

	public void zipDir(String dir2zip, ZipOutputStream zos, Loader meaSuite) {
		try {
			File zipDir = new File(dir2zip);
			String[] dirList = zipDir.list();
			byte[] readBuffer = new byte[2156];
			int bytesIn = 0;
			for (String element : dirList) {
				File f = new File(zipDir, element);
				if (f.isDirectory()) {
					String filePath = f.getPath();
					zipDir(filePath, zos, meaSuite);
					continue;
				}
				FileInputStream fis = new FileInputStream(f);
				ZipEntry anEntry = new ZipEntry(f.getPath());
				zos.putNextEntry(anEntry);
				while ((bytesIn = fis.read(readBuffer)) != -1)
					zos.write(readBuffer, 0, bytesIn);
				fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			meaSuite.api.log(e);
		}
	}
}
