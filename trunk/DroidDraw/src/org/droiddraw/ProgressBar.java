package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class ProgressBar extends AbstractWidget {
	public static final String[] propertyNames = new String[] {"android:indeterminate", "android:max"};
	BooleanProperty indeterminate;
	Image base;
	Image dot;
	Image indet;
	
	public ProgressBar() {
		super("ProgressBar");	
		indeterminate = new BooleanProperty("Indeterminate", "android:indeterminate", false);
		addProperty(indeterminate);
		addProperty(new StringProperty("Max. Value", "android:max", "100"));
		apply();
	
		base = ImageResources.instance().getImage("progress_circular_background");
		dot = ImageResources.instance().getImage("progress_particle");
		indet = ImageResources.instance().getImage("progress_circular_indeterminate");
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
		if (base == null) {
			g.setColor(Color.black);
			g.fillOval(getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.white);
			g.fillOval(getX()+getWidth()/4,getY()+getHeight()/4,getWidth()/2,getHeight()/2);
			g.fillOval(getX()+getWidth()/3,getY(),getWidth()/4-3,getHeight()/4-3);
		}
		else {
			g.drawImage(base, getX(), getY(), getWidth(), getHeight(), null);
			if (indeterminate.getBooleanValue()) {
				g.drawImage(indet, getX(), getY(), getWidth(), getHeight(), null);				
			}
			else {
				g.drawImage(dot, getX(), getY(), getWidth(), getHeight(), null);
			}
		}
	}
}
