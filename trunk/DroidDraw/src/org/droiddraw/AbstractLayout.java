package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

public abstract class AbstractLayout extends AbstractWidget implements Layout {
	protected Vector<Widget> widgets;
	
	public AbstractLayout(String tagName) {
		super(tagName);
		this.widgets = new Vector<Widget>();
		addProperty(new StringProperty("xmlns", "xmlns:android", "http://schemas.android.com/apk/res/android", false));
		apply();
	}
	
	public String toString() {
		return tagName;
	}
	
	public void addWidget(Widget w) {
		widgets.add(w);
		w.setParent(this);
		addEditableProperties(w);
		positionWidget(w);
		apply();
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
		g2d.translate(getX(), getY());
		
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.lightGray);
		g.drawString(tagName, 2, 15);
		g.drawRect(0, 0, getWidth(), getHeight());
		for (Widget w : widgets) {
			w.paint(g);
		}
		g2d.translate(-getX(),-getY());
	}
	
	
	public int getContentWidth() {
		if (widgets.size() > 0) {
			int maxX = 0;
			for (Widget w : widgets) {
				if (w.getX()+w.getWidth() > maxX)
					maxX = w.getX()+w.getWidth();
			}
			return maxX+10;
		}
		else
			return 100;
	}
	
	public int getContentHeight() {
		if (widgets.size() > 0) {
			int maxY = 0;
			for (Widget w : widgets) {
				if (w.getY()+w.getHeight() > maxY)
					maxY = w.getY()+w.getHeight();
			}
			return maxY;
		}
		else
			return 20;
	}
	

	@Override
	public void setPosition(int x, int y) {
		super.setPosition(x, y);
		repositionAllWidgets();
		apply();
	}

	public abstract void positionWidget(Widget w);
	public abstract void repositionAllWidgets();
	protected abstract void addEditableProperties(Widget w);
	protected abstract void removeEditableProperties(Widget w);
	
	public int getScreenX() {
		if (parent != null) {
			return ((Layout)parent).getScreenX()+getX();
		}
		else {
			return getX();
		}
	}
	
	public int getScreenY() {
		if (parent != null) {
			return ((Layout)parent).getScreenY()+getY();
		}
		else {
			return getY();
		}
	}
}
