package org.droiddraw;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public class AbsoluteLayout extends AbstractLayout {
	public AbsoluteLayout() {
		super("AbsoluteLayout");
	}

	@Override
	public void positionWidget(Widget w) {}

	@Override
	public void repositionAllWidgets() {}
	
	public void printStartTag(PrintWriter pw) {
		Hashtable<String,String> atts = new Hashtable<String,String>();
		atts.put("xmlns:android", "http://schemas.android.com/apk/res/android");
		atts.put("android:layout_width", "fill_parent");
		atts.put("android:layout_height", "fill_parent");
		printStartTag(atts, pw);
	}
	

	public void addOutputProperties(Widget w, Vector<Property> properties) {
		properties.add(new StringProperty("X Position","android:layout_x", (w.getX()-getX())+"px"));
		properties.add(new StringProperty("Y Position","android:layout_y", (w.getY()-getY())+"px"));
	}
	
	public void addEditableProperties(Widget w) {}
	public void removeEditableProperties(Widget w) {}
	
	public void apply() {
		super.apply();
	}
}
