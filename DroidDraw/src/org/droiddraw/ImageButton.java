package org.droiddraw;

import java.awt.Graphics;
import java.awt.Image;

public class ImageButton extends ImageView {
	NineWayImage img;
	
	public ImageButton() {
		Image i = ImageResources.instance().getImage("button_background_normal.9");
		if (i != null) {
			this.img = new NineWayImage(i, 10, 10);
		}
		apply();
	}

	@Override
	protected int getContentHeight() {
		return 50;
	}

	@Override
	protected int getContentWidth() {
		return 50;
	}

	@Override
	public void paint(Graphics g) {
		if (img != null) {
			img.paint(g, getX(), getY(), getWidth(), getHeight());
		}
		if (paint != null) {
			g.drawImage(paint, getX()+10, getY()+10, getWidth()-20, getHeight()-20, null);
		}
	}
	
	
}
