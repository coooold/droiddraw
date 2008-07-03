package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;

public class TimePicker extends AbstractWidget {
	Image up_arrow;
	Image down_arrow;
	Image btn;
	NineWayImage img;
	
	public TimePicker() {
		super("TimePicker");
		up_arrow = ImageResources.instance().getImage("light/arrow_up_float");
		down_arrow = ImageResources.instance().getImage("light/arrow_down_float");
	
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default")) {
			btn = ImageResources.instance().getImage("def/btn_default_normal.9");
			img = new NineWayImage(btn, 10, 10);
		}
		apply();
	}
	
	@Override
  public void apply() {
		super.apply();
		this.baseline = 22;
	}
	
	@Override
	protected int getContentHeight() {
		if (btn != null)
			return btn.getHeight(null);
		else
			return 34;
	}

	@Override
	protected int getContentWidth() {
		if (btn != null)
			return 110;
		else
			return 95;
	}

	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.drawString("12:15 PM", getX(), getY()+22);
		if (up_arrow != null) {
			g.drawImage(up_arrow, getX()+20, getY(), null);
			g.drawImage(down_arrow, getX()+20, getY()+24, null);
		}
		if (btn != null) {
			img.paint(g, getX()+60, getY(), 50, 50);
			g.drawString("Set", getX()+75, getY()+25);
		}
	}

}
