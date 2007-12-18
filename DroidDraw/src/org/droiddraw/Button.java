package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;


public class Button extends TextView {
	NineWayImage img;
	
	public Button(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName = "Button";
		
		pad_x = 30;
		pad_y = 10;
	
		Image img = ImageResources.instance().getImage("button_background_normal.9");
		if (img != null) {
			this.img = new NineWayImage(img, 10, 10);
		}
		apply();
	}

	public void apply() {
		super.apply();
		this.baseline = fontSize+2;
	}
	
	public void paint(Graphics g) {
		if (img == null) {
			g.setColor(Color.white);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
			
			g.setColor(Color.black);
			g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		}
		else {
			img.paint(g, getX(), getY(), getWidth(), getHeight());
			g.setColor(Color.black);
		}
		g.setFont(f);
		int w = g.getFontMetrics(f).stringWidth(text.getStringValue());
		g.setColor(textColor.getColorValue());
		g.drawString(text.getStringValue(), getX()+getWidth()/2-w/2, getY()+fontSize+2);
	}
}
