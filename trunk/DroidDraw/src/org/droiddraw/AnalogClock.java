package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Calendar;
import java.util.Date;

public class AnalogClock extends AbstractWidget {
	public AnalogClock() {
		super("AnalogClock");
		apply();
	}

	@Override
	protected int getContentHeight() {
		return 140;
	}

	@Override
	protected int getContentWidth() {
		return 140;
	}

	private void drawAngleLine(Graphics g, double angle, double scale) {
		int dx = (int)(Math.cos(angle)*getWidth()*scale);
		int dy = (int)(Math.sin(angle)*getWidth()*scale);
		int cx = getX()+getWidth()/2;
		int cy = getY()+getHeight()/2;
		g.drawLine(cx, cy, cx+dx, cy-dy);
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(getX(), getY(), getWidth(), getHeight());
		g.setColor(Color.black);
		g.drawOval(getX(), getY(), getWidth(), getHeight());
		
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		int hrs = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		
		drawAngleLine(g, Math.toRadians(90-(hrs+min/60.0)*30), 0.25);
		drawAngleLine(g, Math.toRadians(90-min*6), 0.45);
		
	}
}
