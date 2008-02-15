package org.droiddraw.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

public class LayoutPainter extends AbstractWidgetPainter {

	public void paint(Widget wx, Graphics g) {
		Layout l = (Layout)wx;
		Graphics2D g2d = (Graphics2D)g;
		drawBackground(l, g);
		g2d.translate(l.getX(), l.getY());
		
		g.setColor(Color.lightGray);
		if (l.getWidgets().size() == 0) {
			g.drawString(l.getTagName(), 2, 15);
		}	
		g.drawRect(0, 0, l.getWidth(), l.getHeight());
		for (Widget w : l.getWidgets()) {
			if (w.isVisible()) {
				WidgetPainter wp = WidgetRegistry.getPainter(w.getClass());
				if (wp != null) {
					wp.paint(w, g);
				}
				else {
					w.paint(g);
				}
			}
		}
		g2d.translate(-l.getX(),-l.getY());
	}

}
