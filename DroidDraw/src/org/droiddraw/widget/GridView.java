package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;

import org.droiddraw.property.IntProperty;

public class GridView extends AbstractWidget {
	IntProperty columnCount;
	IntProperty columnWidth;
	IntProperty hSpacing, vSpacing;
	
	public static final String[] propertyNames = {"android:numColumns", "android:columnWidth", "android:horizontalSpacing", "android:verticalSpacing"};
	
	public GridView() {
		super("GridView");
		columnCount = new IntProperty("Columns", "android:numColumns", -1);
		columnCount.setIntValue(5);
		columnWidth = new IntProperty("Column Width", "android:columnWidth", 0);
		columnWidth.setIntValue(20);
		hSpacing = new IntProperty("Horiz. Spacing", "android:horizontalSpacing", 0);
		vSpacing = new IntProperty("Vert. Spacing", "android:verticalSpacing", 0);
		
		
		addProperty(columnCount);
		addProperty(columnWidth);
		addProperty(hSpacing);
		addProperty(vSpacing);
		
		apply();
	}
		
	@Override
	protected int getContentHeight() {
		return 50;
	}

	@Override
	protected int getContentWidth() {
		int cols = columnCount.getIntValue();
		int w = columnWidth.getIntValue();
		if (cols*w > 50) {
			return cols*w;
		}
		else
			return 50;
	}

	public void paint(Graphics g) {
		drawBackground(g);
		g.setColor(Color.darkGray);
		g.drawString("GridView", getX()+3, getY()+16);
		
		g.setColor(Color.lightGray);
		for (int i=1;i<columnCount.getIntValue();i++) {
			int x = getX()+i*columnWidth.getIntValue();
			g.drawLine(x, getY(), x, getY()+getHeight());
			if (hSpacing.getIntValue() > 0) {
				x += hSpacing.getIntValue();
				g.drawLine(x, getY(), x, getY()+getHeight());
			}
		}
	}
}
