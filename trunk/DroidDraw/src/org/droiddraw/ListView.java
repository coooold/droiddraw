package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;

public class ListView extends AbstractWidget {
	public ListView() {
		super("ListView");
		props.add(new StringProperty("List Selector", "android:listSelector", ""));
		props.add(new BooleanProperty("Selector on Top", "android:drawSelectorOnTop", false));
		
		props.add(new StringProperty("Entry Array Id.", "android:entries", ""));
		props.add(new SelectProperty("Entry Gravity", "android:gravity", new String[] {"left", "center", "right"}, 0));
		apply();
	}
	
	@Override
	protected int getContentHeight() {
		return 16;
	}

	@Override
	protected int getContentWidth() {
		return 55;
	}

	public void paint(Graphics g) {
		g.setColor(Color.darkGray);
		g.drawString("ListView", getX()+2, getY()+14);
		g.drawRect(getX(), getY(), getWidth(), getHeight());
	}

}
