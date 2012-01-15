package org.droiddraw.widget;

import java.awt.Color;
import java.awt.Graphics;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.IntProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.property.WidthProperty;

public class GridView extends AbstractWidget {
	
	public static final String TAG_NAME = "GridView";
	
	IntProperty columnCount;
	StringProperty columnWidth;
	WidthProperty hSpacing, vSpacing;
	
	public static final String[] propertyNames = {"android:numColumns", "android:columnWidth", "android:horizontalSpacing", "android:verticalSpacing"};
	
	public GridView() {
		super(TAG_NAME);
		columnCount = new IntProperty("Columns", "android:numColumns", -1);
		columnCount.setIntValue(5);
		columnWidth = new StringProperty("Column Width", "android:columnWidth", "");
		columnWidth.setStringValue("20" + AndroidEditor.instance().getScreenUnit());
		hSpacing = new WidthProperty("Horiz. Spacing", "android:horizontalSpacing", 0);
		vSpacing = new WidthProperty("Vert. Spacing", "android:verticalSpacing", 0);
		
		
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
		int w = removeUnit(columnWidth.getStringValue());
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
			int x = getX()+i*removeUnit(columnWidth.getStringValue());
			g.drawLine(x, getY(), x, getY()+getHeight());
			if (hSpacing.getIntValue() > 0) {
				x += hSpacing.getIntValue();
				g.drawLine(x, getY(), x, getY()+getHeight());
			}
		}
	}
	
	private int removeUnit(String propertyValue){
		int ix = propertyValue.indexOf("px");
		if (ix == -1) {
			ix = propertyValue.indexOf("dp");
		}
		if (ix == -1) {
			ix = propertyValue.indexOf("dip");
		}
		if (ix != -1) {
			propertyValue = propertyValue.substring(0, ix);
		}
		return Integer.parseInt(propertyValue);
	}
}
