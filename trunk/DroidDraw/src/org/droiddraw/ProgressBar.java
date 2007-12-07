package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;

public class ProgressBar extends AbstractWidget {
	public static final String[] propertyNames = new String[] {"android:indeterminate", "android:max"};
	public ProgressBar() {
		super("ProgressBar");	
		addProperty(new BooleanProperty("Indeterminate", "android:indeterminate", false));
		addProperty(new StringProperty("Max. Value", "android:max", "100"));
		apply();
	}
	
	@Override
	protected int getContentHeight() {
		return 48;
	}

	@Override
	protected int getContentWidth() {
		return 48;
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.fillOval(getX(), getY(), getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillOval(getX()+getWidth()/4,getY()+getHeight()/4,getWidth()/2,getHeight()/2);
		g.fillOval(getX()+getWidth()/3,getY(),getWidth()/4-3,getHeight()/4-3);
	}

}
