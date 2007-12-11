package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


public class CheckBox extends CompoundButton {
	public CheckBox(String text) {
		super(text);
		this.tagName = "CheckBox";

		pad_x = 24;
		pad_y = 6;
		apply();
	}
	
	public void paint(Graphics g) {
		Image img = ImageResources.instance().getImage("checkbox_off_background");
		if (img == null) {
			g.setColor(Color.white);
			g.fillRect(getX()+2, getY()+2, 16, 16);
		
			g.setColor(Color.black);
			g.drawRect(getX()+2, getY()+2, 16, 16);
		
			if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
				g.drawLine(getX()+2, getY()+2, getX()+18, getY()+18);
				g.drawLine(getX()+2, getY()+18, getX()+18, getY()+2);
			}
		}
		else {
			if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
				img = ImageResources.instance().getImage("checkbox_on_background");
			}
			g.drawImage(img, getX(), getY(), null);
			g.setColor(Color.black);
		}
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+22, getY()+fontSize+2);
	}
}
