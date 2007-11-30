package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintWriter;

import javax.swing.JPanel;


public class Button extends TextView {
	public Button(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName = "Button";
	}

	public void paint(Graphics g) {
		setSize(g.getFontMetrics(f).stringWidth(text)+16, fontSize+6);

		g.setColor(Color.white);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		
		g.setColor(Color.black);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		g.setFont(f);
		g.drawString(text, getX()+8, getY()+fontSize+2);
	}
}
