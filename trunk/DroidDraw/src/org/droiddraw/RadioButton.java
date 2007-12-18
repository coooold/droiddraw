package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class RadioButton extends CompoundButton {
	public RadioButton(String text) {
		super(text);
		this.tagName = "RadioButton";
		
		pad_x = 24;
		pad_y = 6;
		
		apply();
	}

	@Override
	public void paint(Graphics g) {
		Image img = ImageResources.instance().getImage("radiobutton_off_background");
		if (img == null) {
			g.setColor(Color.white);
			g.fillOval(getX()+2, getY()+2, 16, 16);
		
			g.setColor(Color.black);
			g.drawOval(getX()+2, getY()+2, 16, 16);
		
			if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
				g.fillOval(getX()+6,getY()+6,8,8);
			}
		}
		else {
			if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
				img = ImageResources.instance().getImage("radiobutton_on_background");
			}
			g.drawImage(img, getX(), getY(), null);
			g.setColor(Color.black);
		}
		g.setColor(textColor.getColorValue());
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+22, getY()+fontSize+2);
	}
}
