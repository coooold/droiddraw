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
		for (Widget w : widgets) {
			w.setSizeInternal(widths.get(ix)-w.getPadding(LEFT)-w.getPadding(RIGHT), w.getHeight());
			ix++;
		}
		repositionAllWidgetsInternal();
	}
}
