package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;

import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;

public class ListView extends AbstractWidget {
	
	public static final String TAG_NAME = "ListView";
	
	public ListView() {
		super(TAG_NAME);
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
