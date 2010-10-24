package org.droiddraw.widget;

import java.awt.Graphics;
import java.text.DateFormat;
import java.util.Date;

public class DigitalClock extends TextView {
	
	public static final String TAG_NAME = "DigitalClock";
	
	public DigitalClock() {
		super("HH:MM:SS pm");
		this.setTagName(TAG_NAME);
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
