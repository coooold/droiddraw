package org.droiddraw;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public class LinearLayout extends AbstractLayout {
	boolean vertical;
	public static int VERTICAL_PADDING = 4;
	
	private static final String[] VERTICAL_OPTIONS = new String[] {"left", "right","center_horizontal"};
	private static final String[] HORIZONTAL_OPTIONS = new String[] {"top", "bottom", "center_vertical"};
	
	
	public LinearLayout() {
		this(true);
	}
	
	public LinearLayout(boolean vertical) {
		super("LinearLayout");
		this.vertical = vertical;
	}
	
	@Override
	public void positionWidget(Widget w) {
		widgets.remove(w);
		int y = w.getY();
		int ix;
		for (ix = 0;ix < widgets.size() && y > widgets.get(ix).getY(); ix++);
		widgets.add(ix, w);
		repositionAllWidgets();
	}

	@Override
	public void repositionAllWidgets() {
		int y = getY();
		for (Widget w : widgets) {
			int x;
			String gravity = (String)w.getPropertyByAttName("android:layout_gravity").getValue();
			if ("right".equals(gravity))
				x = getX()+getWidth()-w.getWidth();
			else if ("center_horizontal".equals(gravity) || "center".equals(gravity)) 
				x = getX()+getWidth()/2-w.getWidth()/2;
			else
				x = getX();
			w.setPosition(x, y);
			if (vertical)
				y+=w.getHeight()+VERTICAL_PADDING;
			else
				x+=w.getWidth();
		}
	}

	public void printStartTag(PrintWriter pw) {
		Hashtable<String,String> atts = new Hashtable<String,String>();
		atts.put("xmlns:android", "http://schemas.android.com/apk/res/android");
		atts.put("android:orientation", vertical?"vertical":"horizontal");
		atts.put("android:layout_width", "fill_parent");
		atts.put("android:layout_height", "fill_parent");
		printStartTag(atts, pw);
	}

	public void addOutputProperties(Widget w, Vector<Property> properties) {}

	public void addEditableProperties(Widget w) {
		w.addProperty(new StringProperty("Linear Layout Weight", "android:layout_weight", "0"));
		w.addProperty(new SelectProperty("Layout Gravity", "android:layout_gravity", vertical?VERTICAL_OPTIONS:HORIZONTAL_OPTIONS, 0));
	}
	
	public void removeEditableProperties(Widget w) {
		w.removeProperty(new StringProperty("", "android:layout_weight", ""));
		w.removeProperty(new SelectProperty("", "android:layout_gravity", new String[] {""}, 0));
	}
}
