package org.droiddraw.gui;

import java.awt.Graphics;

import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

public class ScrollViewPainter extends LayoutPainter {
	NineWayImage field;
	NineWayImage bar;
	
	public ScrollViewPainter() {
		field = new NineWayImage(ImageResources.instance().getImage("scrollfield.9"), 2, 4);
		bar = new NineWayImage(ImageResources.instance().getImage("scrollbar.9"), 1, 6);
		
	}
	
	
	public void paint(Widget wx, Graphics g) {
		Layout l = (Layout)wx;
		super.paint(l, g);
		field.paint(g, l.getX()+l.getWidth()-10, l.getY(), 10, l.getHeight());
		bar.paint(g, l.getX()+l.getWidth()-10, l.getY(), 10, (l.getHeight() < 50)?l.getHeight():50);
	}
}
