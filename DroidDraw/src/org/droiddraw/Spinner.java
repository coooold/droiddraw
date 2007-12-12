package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class Spinner extends AbstractWidget {
	NineWayImage img;
	NineWayImage arrows;
	BooleanProperty onTop;
	
	public Spinner() {
		super("Spinner");
		
		Image i = ImageResources.instance().getImage("spinnerbox_background_focus_yellow.9");
		if (i != null) {
			img = new NineWayImage(i, 10, 10, 28, 10);
			i = ImageResources.instance().getImage("spinnerbox_arrow_middle.9");
			arrows = new NineWayImage(i, 1, 1, 22, 1);
		}
		
		onTop = new BooleanProperty("Selector on Top", "android:drawSelectorOnTop", false);
		props.add(onTop);
		
		apply();
	}
	
	@Override
	protected int getContentHeight() {
		return 24;
	}

	@Override
	protected int getContentWidth() {
		return 90;
	}

	public void paint(Graphics g) {
		if (img != null) {
			img.paint(g, getX(), getY(), getWidth(), getHeight());
			arrows.paint(g, getX(), getY(), getWidth(), getHeight());
		}
		g.setColor(Color.black);
		g.drawString("Spinner", getX()+15, getY()+16);
	}

}
