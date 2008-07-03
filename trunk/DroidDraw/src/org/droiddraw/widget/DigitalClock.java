package org.droiddraw.widget;

import java.awt.Graphics;
import java.text.DateFormat;
import java.util.Date;

public class DigitalClock extends TextView {
	public DigitalClock() {
		super("HH:MM:SS pm");
		this.tagName = "DigitalClock";
		setDate();
	}
	
	private void setDate() {
		text.setStringValue(DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()));
	}
	
	@Override
  public void paint(Graphics g) {
		setDate();
		super.paint(g);
	}
}
