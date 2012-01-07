package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.StringProperty;

public class MapView extends AbstractWidget {
	static BufferedImage map = null;
	public static final String TAG_NAME = "MapView";
	
	BooleanProperty clickable = new BooleanProperty("Clickable", "android:clickable", true);
	StringProperty apiKey = new StringProperty("API Key", "android:apiKey", "none");
	
	public MapView() {
		super(TAG_NAME);
		clickable = new BooleanProperty("Clickable", "android:clickable", true);
		apiKey = new StringProperty("API Key", "android:apiKey", "none");
		clickable.setDefaulted(false);
		apiKey.setDefaulted(false);
		addProperty(clickable);
		addProperty(apiKey);
		widthProp.setStringValue("fill_parent");
		heightProp.setStringValue("fill_parent");
		apply();
		if (map == null) {
			Image img = ImageResources.instance().getImage("mapview");
			map = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
			map.getGraphics().drawImage(img, 0, 0, null);
		}
	}
	
	@Override
	protected int getContentHeight() {
		return 100;
	}

	@Override
	protected int getContentWidth() {
		return 100;
	}

	public void paint(Graphics g) {
		g.drawImage(map, getX(), getY(), getWidth(), getHeight(), null);
	}

}
