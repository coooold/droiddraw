package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.IntProperty;

public class Gallery extends AbstractWidget {
	
	public static final String TAG_NAME = "Gallery";
	public static String[] propertyNames = {"android:spacing", "android:animationDuration"};
	
	IntProperty spacing;
	IntProperty animationDuration;
	
	Image paint;
	public Gallery() {
		super(TAG_NAME);
		paint = ImageResources.instance().getImage("paint");
	
		animationDuration = new IntProperty("Anim. Duration", "android:animationDuration", 200);
		spacing = new IntProperty("Spacing", "android:spacing", 5);
		
		addProperty(animationDuration);
		addProperty(spacing);
		apply();
	}
	
	@Override
	protected int getContentHeight() {
		return 100;
	}

	@Override
	protected int getContentWidth() {
		return 200;
	}

	public void paint(Graphics g) {
		int w1 = getWidth()/4;
		int w2 = getWidth()/3;
		int off_x = (getWidth()-w1*2-w2-spacing.getIntValue()*2)/2;
		
		if (paint != null) {
			g.drawImage(paint, getX()+off_x, getY(), w1, w1, null);
			g.drawImage(paint, getX()+off_x+spacing.getIntValue()+w1, getY(), w2, w2, null);
			g.drawImage(paint, getX()+off_x+spacing.getIntValue()*2+w1+w2, getY(), w1, w1, null);
		}
		g.setColor(Color.black);
		g.drawString("Gallery", getX()+getWidth()/2-25, getY()+getHeight()-14);
	}

}
