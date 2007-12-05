package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;


public class CheckBox extends Button {
	public CheckBox(String text) {
		super(text);
		this.tagName = "CheckBox";

		setSize(stringLength(text)+24, fontSize+6);
		pad_x = 24;
		pad_y = 6;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(getX()+2, getY()+2, 16, 16);
		
		g.setColor(Color.black);
		g.drawRect(getX()+2, getY()+2, 16, 16);
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+22, getY()+fontSize+2);
	}
}
