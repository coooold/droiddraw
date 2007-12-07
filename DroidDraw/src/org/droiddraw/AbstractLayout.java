package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintWriter;
import java.util.Vector;

public abstract class AbstractLayout extends AbstractWidget implements Layout {
	protected Vector<Widget> widgets;
	
	public AbstractLayout(String tagName) {
		super(tagName);
		this.widgets = new Vector<Widget>();
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

	protected void printStartTag(java.util.Hashtable<String,String> atts, PrintWriter pw) 
	{
		pw.println("<"+tagName);
		for (String key : atts.keySet()) {
			pw.println(key+"=\""+atts.get(key)+"\"");
		}
		pw.println(">");
	}

	public void printEndTag(PrintWriter pw) {
		pw.println("</"+tagName+">");
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.lightGray);
		g.drawString(tagName, getX()+2, getY()+15);
		g.drawRect(getX(), getY(), getWidth(), getHeight());
		for (Widget w : widgets) {
			w.paint(g);
		}
	}
	
	public int getContentHeight() {
		int minY = AndroidEditor.instance().getScreenY();
		int maxY = 0;
		
		for (Widget w : widgets) {
			if (w.getY() < minY)
				minY = w.getY();
			if (w.getY()+w.getHeight() > maxY)
				maxY = w.getY()+w.getHeight();
		}
		if (maxY < minY)
			return 20;
		else
			return maxY-minY;
	}

	public int getContentWidth() {
		int minX = AndroidEditor.instance().getScreenX();
		int maxX = 0;
		
		for (Widget w : widgets) {
			if (w.getX() < minX)
				minX = w.getX();
			if (w.getX()+w.getWidth() > maxX)
				maxX = w.getX()+w.getWidth();
		}
		if (maxX < minX)
			return 100;
		else
			return maxX-minX;
	}
	
	public abstract void positionWidget(Widget w);
	public abstract void repositionAllWidgets();
	protected abstract void addEditableProperties(Widget w);
	protected abstract void removeEditableProperties(Widget w);
}
