package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

import org.droiddraw.property.StringProperty;

public abstract class AbstractLayout extends AbstractWidget implements Layout {
	protected Vector<Widget> widgets;
	
	public AbstractLayout(String tagName) {
		super(tagName);
		this.widgets = new Vector<Widget>();
		addProperty(new StringProperty("xmlns", "xmlns:android", "http://schemas.android.com/apk/res/android", false));
		//background.setColorValue(Color.white);
		apply();
	}
	
	public String toString() {
		return tagName;
	}
	
	public void addWidget(Widget w) {
		assert (w != this);
		widgets.add(w);
		w.setParent(this);
		addEditableProperties(w);
		positionWidget(w);
		this.readWidthHeight();
		if (getParent() != null) {
			getParent().repositionAllWidgets();
		}
	}
	
	public Vector<Widget> getWidgets() {
		return widgets;
	}

	public void removeWidget(Widget w) {
		widgets.remove(w);
		removeEditableProperties(w);
		repositionAllWidgets();
	}
	
	public void removeAllWidgets() {
		for (Widget w : widgets) {
			removeEditableProperties(w);
		}
		widgets.clear();
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
	
		drawBackground(g);
		g2d.translate(getX(), getY());
		
		g.setColor(Color.black);
		if (widgets.size() == 0) {
			g.drawString(tagName, 2, 15);
		}	
		g.drawRect(0, 0, getWidth(), getHeight());
		for (Widget w : widgets) {
			if (w.isVisible())
				w.paint(g);
		}
		g2d.translate(-getX(),-getY());
	}
	
	
	public int getContentWidth() {
		if (widgets.size() > 0) {
			int maxX = 0;
			for (Widget w : widgets) {
				/*LEFT padding already in X value*/ 
				//w.apply();
				int width_w_pad = w.getWidth()+w.getPadding(RIGHT);
				if (w.getX()+width_w_pad > maxX)
					maxX = w.getX()+width_w_pad;
			}
			return maxX;
		}
		else
			return 100;
	}
	
	public int getContentHeight() {
		if (widgets.size() > 0) {
			int maxY = 0;
			for (Widget w : widgets) {
				/*TOP padding already in Y value*/ 
				int height_w_pad = w.getHeight()+w.getPadding(BOTTOM);
				if (w.getY()+height_w_pad > maxY)
					maxY = w.getY()+height_w_pad;
			}
			return maxY;
		}
		else
			return 20;
	}
	

	/*
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		//repositionAllWidgets();
		//apply();
	}
	*/
	
	public abstract void positionWidget(Widget w);
	public abstract void repositionAllWidgets();
	
	public int getScreenX() {
		if (parent != null && parent != this) {
			return ((Layout)parent).getScreenX()+getX();
		}
		else {
			return getX();
		}
	}
	
	public int getScreenY() {
		if (parent != null && parent != this) {
			return ((Layout)parent).getScreenY()+getY();
		}
		else {
			return getY();
		}
	}
	
	public void resizeForRendering() {
		for (Widget w : widgets) {
			if (w instanceof Layout) {
				((Layout)w).resizeForRendering();
			}
		}
	}
	
	public void clearRendering() {
		for (Widget w : widgets) {
			w.apply();
			if (w instanceof Layout) {
				((Layout)w).clearRendering();
			}
		}
	}
	
	public boolean containsWidget(Widget w) {
		for (Widget wt : widgets) {
			if (wt.equals(w)) {
				return true;
			}
			else if (wt instanceof Layout) {
				if (((Layout)wt).containsWidget(w)) {
					return true;
				}
			}
		}
		return false;
	}
}
