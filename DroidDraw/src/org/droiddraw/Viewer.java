package org.droiddraw;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

public class Viewer extends JPanel {
	private static final long serialVersionUID = 1L;
	Dimension d;
	AndroidEditor app;
	ViewerListener vl;
	
	public Viewer(AndroidEditor app) {
		this.app = app;
		this.d = new Dimension(320,240);
		vl = new ViewerListener(app, this);
		addMouseListener(vl);
		addMouseMotionListener(vl);
		addKeyListener(vl);
	}
	
	ViewerListener getListener() {
		return vl;
	}

	public Dimension getPreferredSize() {
		return d;
	}

	public Dimension getMinimumSize() {
		return d;
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		Vector<Widget> widgets = app.getWidgets();
		for (Widget w : widgets) {
			w.paint(g);
		}
		Widget w = app.getSelected();
		if (w != null) {
			g.setColor(Color.black);
			g.drawRect(w.getX(), w.getY(), w.getWidth(), w.getHeight());
		}
	}
}