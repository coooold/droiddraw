package org.droiddraw.gui;

import java.awt.Graphics;

import org.droiddraw.property.ColorProperty;
import org.droiddraw.widget.Widget;

public abstract class AbstractWidgetPainter implements WidgetPainter {
	public void drawBackground(Widget w, Graphics g) {
		ColorProperty cp = (ColorProperty)w.getPropertyByAttName("android:background");
		if (cp.getColorValue() != null) {
			g.setColor(cp.getColorValue());
			g.fillRect(w.getX()-w.getPadding(Widget.LEFT), 
					   w.getY()-w.getPadding(Widget.TOP), 
					   w.getWidth()+w.getPadding(Widget.LEFT)+w.getPadding(Widget.RIGHT), 
					   w.getHeight()+w.getPadding(Widget.TOP)+w.getPadding(Widget.BOTTOM));
		}
	}

}
