package me.turt2live.meaSuite.grapher;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import me.turt2live.meaSuite.API.MeaAPI;
import me.turt2live.meaSuite.plugin.Loader;

import org.bukkit.plugin.java.JavaPlugin;

public class Grapher {

	@SuppressWarnings("unused")
	private JavaPlugin			plugin;
	private Loader				loader;
	private File				directory;

	private BufferedImage		image;
	private Graphics2D			g;

	private Vector<Double[]>	dataX		= new Vector<Double[]>();
	private Vector<Double[]>	dataY		= new Vector<Double[]>();
	private Vector<String>		dataLabel	= new Vector<String>();

	public Grapher(JavaPlugin p, Loader l, File d) {
		this.plugin = p;
		this.loader = l;
		this.directory = d;
		image = new BufferedImage(800, 300, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
	}

	public void createGraph(String title) {
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, 800, 300);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(10, 10, 780, 280);
		g.setColor(Color.GRAY);
		g.fillRect(20, 20, 760, 35);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 26));
		g.drawString(title, 25, 45);
		g.setColor(Color.GRAY);
		g.fillRect(20, 60, 760, 220);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(40, 80, 720, 180);
	}

	public Double[] convertSeries(Double series[]) {
		Double returnSeries[] = new Double[series.length];
		Double max = Double.MIN_VALUE;
		Double min = Double.MAX_VALUE;
		for (Double d : series)
			if (d > max) max = d;
			else if (d < min) min = d;
		for (int i = 0; i < series.length; i++) {
			Double d = series[i];
			d = (d - min) / (max - min);
			d = d * 100;
			returnSeries[i] = d;
		}
		return returnSeries;
	}

	public void addSeries(Double dataX[], Double dataY[], String name, boolean convertX, boolean convertY) {
		if (convertX) dataX = convertSeries(dataX);
		if (convertY) dataY = convertSeries(dataY);
		this.dataX.add(dataX);
		this.dataY.add(dataY);
		dataLabel.add(name);
	}

	public void addSeries(Double dataX[], Double dataY[], String name) {
		addSeries(dataX, dataY, name, false, false);
	}

	public File generate() {
		File output = new File(directory + "/chart_" + MeaAPI.timestamp(true) + ".png");
		int scaleX[] = { 0, 10, 20, 30, 40, 50 };
		int scaleY[] = { 0, 20, 40, 60, 80, 100 };
		int gpx[] = { 40, 720 };
		int gpy[] = { 80, 180 };
		// Draw scales
		// Draw X-Scale (Also draws lines)
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 14));
		int sx = (gpx[1] - gpx[0]) / (scaleX.length - 1);
		for (int i = 0; i < scaleX.length; i++) {
			String label = scaleX[i] + "";
			g.drawString(label, gpx[0] + (sx * i) + 5, gpy[1] + 95);
			g.drawLine(gpx[0] + (sx * i) + 10, gpy[1] + 80, gpx[0] + (sx * i) + 10, gpy[0]);
		}
		// Draw Y-Scale (Also draws lines)
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 14));
		int sy = ((gpy[1] - gpy[0]) / (scaleY.length - 1)) + 14;
		for (int i = 0; i < scaleY.length; i++) {
			String label = scaleY[i] + "";
			g.drawString(label, gpx[0] - 20, gpy[1] - (sy * i) + 80);
			g.drawLine(gpx[0], gpy[1] - (sy * i) + 75, gpx[1] + 40, gpy[1] - (sy * i) + 75);
		}
		for (int s = 0; s < ((dataX.size() >= dataY.size()) ? ((dataX.size() >= dataLabel.size()) ? dataX.size() : ((dataLabel.size() >= dataY.size()) ? dataLabel.size() : dataY.size())) : ((dataY.size() >= dataLabel.size()) ? dataY.size() : dataLabel.size())); s++) {
			Color seriesColor[] = { Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.RED, Color.YELLOW };
			Double dx[] = dataX.get(s);
			Double dy[] = dataY.get(s);
			// Find all points
			Vector<Point> points = new Vector<Point>();
			for (int i = 0; i < ((dx.length <= dy.length) ? dx.length : dy.length); i++) {
				double x = 0;
				double y = 0;
				double msx = scaleX[0];
				double msy = scaleY[0];
				double masx = scaleX[scaleX.length - 1];
				double masy = scaleY[scaleY.length - 1];
				x = ((dx[i] - msx) / (masx - msx));
				double nx = (x * gpx[1]) + msx;
				y = ((dy[i] - msy) / (masy - msy));
				y = 1 - y;
				double ny = (y * gpy[1]) + msy;
				points.add(new Point((int) (nx + 38), (int) (ny + 78)));
			}
			// Draw points (as a line graph)
			if (s >= seriesColor.length) {
				int ts = (seriesColor.length - 1) - s;
				g.setColor(seriesColor[ts]);
			} else g.setColor(seriesColor[s]);
			g.setStroke(new BasicStroke(3F));
			for (int i = 0; i < points.size() - 1; i++)
				g.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y);
		}
		try {
			ImageIO.write(image, "png", output);
		} catch (IOException e) {
			e.printStackTrace();
			loader.api.log(e);
		}
		return output;
	}

	public void clear() {
		image = new BufferedImage(800, 300, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
	}
}
