package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

public class TimePicker extends AbstractWidget {
	Image up_arrow;
	Image down_arrow;
	
	public TimePicker() {
		super("TimePicker");
		up_arrow = ImageResources.instance().getImage("arrow_up_float");
		down_arrow = ImageResources.instance().getImage("arrow_down_float");
		apply();
	}
	
	public void apply() {
		super.apply();
		this.baseline = 22;
	}
	
	@Override
	protected int getContentHeight() {
		return 34;
	}

	@Override
	protected int getContentWidth() {
		return 65;
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawString("12:15 PM", getX(), getY()+22);
		if (up_arrow != null) {
			g.drawImage(up_arrow, getX()+20, getY(), null);
			g.drawImage(down_arrow, getX()+20, getY()+24, null);
		}
	}

}
