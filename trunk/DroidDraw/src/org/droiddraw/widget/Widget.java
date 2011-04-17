package org.droiddraw.widget;
import java.awt.Graphics;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.Vector;

import org.droiddraw.property.Property;

public interface Widget extends Cloneable, Serializable {
	public static final int TOP = 0;
	public static final int LEFT = 1;
	public static final int BOTTOM = 2;
	public static final int RIGHT = 3;
	
	public boolean clickedOn(int x, int y);
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void setPosition(int x, int y);
	public void setSize(int width, int height);
	public void setWidth(int width);
	public void setHeight(int height);
	public void setSizeInternal(int w, int h);
	public void move(int dx, int dy);
	public void paint(Graphics g);
	public void apply();
	public Vector<Property> getProperties();
	public void setPropertyByAttName(String attName, String value);
	public Property getPropertyByAttName(String attName);
	public boolean propertyHasValueByAttName(String attName, Object value);
	public void addProperty(Property p);
	public void removeProperty(Property p);
	public void setPropertyChangeListener(PropertyChangeListener l);
	
	public String getTagName();
	public String getId();
	public Layout getParent();
	public void setParent(Layout w);
	public int getBaseline();
	public int getPadding(int which);
	public void setPadding(int pad);
	public void setPadding(int pad, int which);
	public boolean isVisible();
	
	public int getMargin(int which);
	
	public Widget copy();
}