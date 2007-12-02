package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;


public class Button extends TextView {
	public Button(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName = "Button";
		setSize(txt.length()*8+16, fontSize+6);
	}

	public void paint(Graphics g) {
		if (width.getStringValue().equals("wrap_content"))
			setSize(g.getFontMetrics(f).stringWidth(text.getStringValue())+16, fontSize+6);

		g.setColor(Color.white);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		
		g.setColor(Color.black);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+8, getY()+fontSize+2);
	}
}
