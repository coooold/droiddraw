package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;


public class CheckBox extends CompoundButton {
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
		
		if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
			System.out.println("foo");
			g.drawLine(getX()+2, getY()+2, getX()+18, getY()+18);
			g.drawLine(getX()+2, getY()+18, getX()+18, getY()+2);
		}
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+22, getY()+fontSize+2);
	}
}
