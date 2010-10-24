package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;

public class ImageButton extends ImageView {
	public static final String TAG_NAME = "ImageButton";
	NineWayImage img;
	Image img_base;
	int cw;
	
	public ImageButton() {
		img_base = null;
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default")) {
			img_base = ImageResources.instance().getImage("def/btn_default_normal.9");
			if (img_base != null) {
				this.img = new NineWayImage(img_base, 10, 10);
			}
			cw = 50;
		}
		else if (theme.equals("light")) {
			img_base = ImageResources.instance().getImage("light/button_background_normal.9");
			if (img_base != null) {
				this.img = new NineWayImage(img_base, 10, 10);
			}
			cw = 50;
		}
		this.setTagName(TAG_NAME);
		apply();
	}

	@Override
	protected int getContentHeight() {
		return cw;
	}

	@Override
	protected int getContentWidth() {
		return cw;
	}

	@Override
	public void paint(Graphics g) {
		if (img != null) {
			img.paint(g, getX(), getY(), getWidth(), getHeight());
		}
		if (super.img != null) {
			g.drawImage(super.img, getX()+10, getY()+10, getWidth()-20, getHeight()-20, null);
		}
		else if (paint != null) {
			g.drawImage(paint, getX()+10, getY()+10, getWidth()-20, getHeight()-20, null);
		}
	}
	
	
}
