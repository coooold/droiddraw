package org.droiddraw.widget;

import java.util.Vector;

public class TableRow extends LinearLayout {
	public TableRow() {
		this.tagName = "TableRow";
		this.orientation.setStringValue("horizontal");
		this.orientation.setEditable(false);
		this.widthProp.setStringValue("fill_parent");
		this.widthProp.setEditable(false);
		
		apply();
	}
	
	public void positionWidget(Widget w) {
		super.positionWidget(w);
		if (parent instanceof TableLayout)
			parent.positionWidget(this);
	}
	
	public void setWidths(Vector<Integer> widths) {
		int ix = 0;
		int width = 0;
		for (Widget w : widgets) {
			w.setPosition(width, w.getY());
			w.setSizeInternal(widths.get(ix), w.getHeight());
			width += widths.get(ix);
			ix++;
		}
	}
}
