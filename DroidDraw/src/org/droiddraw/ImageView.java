package org.droiddraw;

import java.awt.Graphics;
import java.awt.Image;

public class ImageView extends AbstractWidget {
	Image paint;
	
	public ImageView() {
		super("ImageView");
		paint = ImageResources.instance().getImage("paint");
		apply();
	}
	
	@Override
	protected int getContentHeight() {
		return 30;
	}

	@Override
	protected int getContentWidth() {
		return 30;
	}

	public void paint(Graphics g) {
		if (paint != null) {
			g.drawImage(paint, getX(), getY(), getWidth(), getHeight(), null);
		}
	}

}
