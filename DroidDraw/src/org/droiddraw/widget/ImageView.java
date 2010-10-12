package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.ImageProperty;

public class ImageView extends AbstractWidget {
	Image paint;
	BufferedImage img;
	
	ImageProperty src;
	
	public ImageView() {
		super("ImageView");
		paint = ImageResources.instance().getImage("paint");
		src = new ImageProperty("Image Source", "android:src", "");
		addProperty(src);
		apply();
	}
	
	@Override
	protected int getContentHeight() {
		if (img == null)
			return 30;
		else
			return img.getHeight();
	}

	@Override
	protected int getContentWidth() {
		if (img == null)
			return 30;
		else
			return img.getWidth();
	}
	
	@Override
  public void apply() {
		super.apply();
		if (src.getStringValue() != null && src.getStringValue().startsWith("@drawable")) {
			img = AndroidEditor.instance().findDrawable(src.getStringValue());
		}
	}
	
	public void paint(Graphics g) {
		if (img != null) {
			g.drawImage(img, getX(), getY(), getWidth(), getHeight(), null);
		}
		else if (paint != null) {
			g.drawImage(paint, getX(), getY(), getWidth(), getHeight(), null);
		}
	}

}
