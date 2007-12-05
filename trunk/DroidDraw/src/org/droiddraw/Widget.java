package org.droiddraw;
import java.awt.Graphics;
import java.util.Vector;

import javax.swing.JPanel;

public interface Widget {
	public boolean clickedOn(int x, int y);
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void setPosition(int x, int y);
	public void move(int dx, int dy);
	public void paint(Graphics g);
	public JPanel getEditorPanel();
	public void apply();
	public Vector<Property> getProperties();
	public void setPropertyByAttName(String attName, String value);
	public Property getPropertyByAttName(String attName);
	public void removeProperty(Property p);
	public String getTagName();
	public String getId();
}