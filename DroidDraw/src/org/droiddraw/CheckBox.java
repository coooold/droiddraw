package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;


public class CheckBox extends Button {
	public CheckBox(String text) {
		super(text);
		this.tagName = "CheckBox";
	}
	
	public void paint(Graphics g) {
		setSize(g.getFontMetrics(f).stringWidth(text)+24, fontSize+6);

		g.setColor(Color.white);
		g.fillRect(getX()+2, getY()+2, 16, 16);
		
		g.setColor(Color.black);
		g.drawRect(getX()+2, getY()+2, 16, 16);
		g.setFont(f);
		g.drawString(text, getX()+22, getY()+fontSize+2);
	}
}
