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
	protected void repositionAllWidgets(Vector<Widget> widgets) {}
	
	public void printStartTag(PrintWriter pw) {
		Hashtable<String,String> atts = new Hashtable<String,String>();
		atts.put("xmlns:android", "http://schemas.android.com/apk/res/android");
		atts.put("android:layout_width", "fill_parent");
		atts.put("android:layout_height", "fill_parent");
		printStartTag(atts, pw);
	}
}
