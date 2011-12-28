package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;
import org.droiddraw.property.StringProperty;

public class ToggleButton extends Button {
	public static final String TAG_NAME = "ToggleButton";
	
	private StringProperty textOn;
	private StringProperty textOff;

	NineWayImage on;
	NineWayImage off;
	
	public ToggleButton(String txtOn, String txtOff) {
		super("");
		this.setTagName(TAG_NAME);

		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default")) {
			Image img_base = ImageResources.instance().getImage("def/btn_toggle_on.9");
			if (img_base != null) {
				this.on = new NineWayImage(img_base, 10, 10);
			}
			img_base = ImageResources.instance().getImage("def/btn_toggle_off.9");
			if (img_base != null) {
				this.off = new NineWayImage(img_base, 10, 10);
			}
		}
		
		textOn = new StringProperty("Text when on", "textOn", "On");
		textOff = new StringProperty("Text when off", "textOff", "Off");

		this.addProperty(textOn);
		this.addProperty(textOff);
	}

	@Override
	public void paint(Graphics g) {
		if (img == null) {
			g.setColor(Color.white);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);

			g.setColor(Color.black);
			g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		}
		else {
			img.paint(g, getX(), getY(), getWidth(), getHeight());
			if (on != null) {
				on.paint(g, getX() + 2, getY() + 10, 10, 5);
			}
			g.setColor(Color.black);
		}
		g.setFont(f);
		g.setColor(textColor.getColorValue());

		drawText(g, textOn.getStringValue(), 0, getHeight()/2+fontSize/2-5, CENTER);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1152130407386044383L;

}
