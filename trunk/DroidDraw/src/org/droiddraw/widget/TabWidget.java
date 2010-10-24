package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;
import org.droiddraw.property.StringProperty;

/**
 * The TabWidget.java class will be used by the TabHost.java widget
 * via a LinearLayout widget.
 * 
 * @author rey malahay  <a href="mailto:reymalahay@gmail.com">Rey Malahay</a>
 *
 */
public class TabWidget extends AbstractWidget {
	
	private Image tab;
	private NineWayImage img;
	public static final String IMAGE_NAME = "def/tab_selected.9";
	public static final String TAG_NAME = "TabWidget";
	private static final String ANDROID_ID = "@android:id/tabs";	/* This is the id that is displayed in the final xml file. */

	/**
	 * Default constructor
	 */
	public TabWidget() {
		super(TAG_NAME);
		((StringProperty)this.getProperties().get(this.getProperties().indexOf(this.id))).setStringValue(ANDROID_ID);
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default")) {
			tab = ImageResources.instance().getImage(IMAGE_NAME);
			img = new NineWayImage(tab, 10, 10);
		}
		apply();
	}
	
	public void apply() {
		super.apply();
		this.baseline = 22;
	}
	
	public void paint(Graphics g) {
		if (tab == null) {
			g.setColor(Color.white);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
			
			g.setColor(Color.black);
			g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		}
		else {
			img.paint(g, getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.black);
			g.drawString("Tab Widget", getX(), getY() + 12);
		}
	}

	@Override
	protected int getContentHeight() {
		if (tab != null)
			return tab.getHeight(null);
		else
			return 10;
	}

	@Override
	protected int getContentWidth() {
		return (tab != null) ? 110 : 95;
	}
}