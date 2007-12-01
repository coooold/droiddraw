package org.droiddraw;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

public class LinearLayout extends AbstractLayout {
	boolean vertical;
	public static int VERTICAL_PADDING = 4;
	
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
		repositionAllWidgets(widgets);
	}

	@Override
	protected void repositionAllWidgets(Vector<Widget> widgets) {
		int y = AndroidEditor.OFFSET_Y;
		int x = 0;
		for (Widget w : widgets) {
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
}
