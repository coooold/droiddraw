package org.droiddraw;

import java.util.Vector;

public class LinearLayout extends AbstractLayout {
	boolean vertical;
	public static int VERTICAL_PADDING = 2;
	
	public LinearLayout() {
		this(true);
	}
	
	public LinearLayout(boolean vertical) {
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

}
