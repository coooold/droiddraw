package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;


public class EditView extends TextView {
	public EditView(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName="EditView";
	}
	
	public void paint(Graphics g) {
		setSize(g.getFontMetrics(f).stringWidth(text)+12, fontSize+6);

		g.setColor(Color.white);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		
		g.setColor(Color.darkGray);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		g.setFont(f);
		g.drawString(text, getX()+8, getY()+fontSize+2);
		g.drawLine(getX()+7, getY()+2, getX()+7, getY()+fontSize+2);
	}
}
