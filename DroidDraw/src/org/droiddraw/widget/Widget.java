package org.droiddraw.widget;
import java.awt.Graphics;
import java.util.Vector;

import org.droiddraw.property.Property;

public interface Widget {
	public boolean clickedOn(int x, int y);
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void setPosition(int x, int y);
	public void setSize(int width, int height);
	public void setSizeInternal(int w, int h);
	public void move(int dx, int dy);
	public void paint(Graphics g);
	public void apply();
	public Vector<Property> getProperties();
	public void setPropertyByAttName(String attName, String value);
	public Property getPropertyByAttName(String attName);
	public void addProperty(Property p);
	public void removeProperty(Property p);
	public String getTagName();
	public String getId();
	public Layout getParent();
	public void setParent(Layout w);
	public int getBaseline();
}