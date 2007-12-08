package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;

public class RadioButton extends CompoundButton {
	public RadioButton(String text) {
		super(text);
		this.tagName = "RadioButton";
		
		setSize(stringLength(text)+24, fontSize+6);
		pad_x = 24;
		pad_y = 6;
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(getX()+2, getY()+2, 16, 16);
		
		g.setColor(Color.black);
		g.drawOval(getX()+2, getY()+2, 16, 16);
		
		if ("true".equals(this.getPropertyByAttName("android:checked").getValue())) {
			g.fillOval(getX()+6,getY()+6,8,8);
		}
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+22, getY()+fontSize+2);
	}
}
