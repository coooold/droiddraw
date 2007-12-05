package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;


public class Button extends TextView {
	public Button(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName = "Button";
		setSize(stringLength(txt)+16, fontSize+6);
		
		pad_x = 16;
		pad_y = 6;
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		
		g.setColor(Color.black);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		g.setFont(f);
		int w = g.getFontMetrics(f).stringWidth(text.getStringValue());
		g.drawString(text.getStringValue(), getX()+getWidth()/2-w/2, getY()+fontSize+2);
	}
}
