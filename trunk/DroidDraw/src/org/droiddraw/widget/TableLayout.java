package org.droiddraw.widget;

import java.util.StringTokenizer;
import java.util.Vector;

import org.droiddraw.property.StringProperty;

public class TableLayout extends LinearLayout {
	Vector<Integer> max_widths;
	Vector<Integer> stretchColumns;
	StringProperty stretch;
	
	public TableLayout() {
		this.tagName = "TableLayout";
		this.max_widths = new Vector<Integer>();
	
		this.stretch = new StringProperty("Stretchable Column", "android:stretchColumns", "");
		props.add(stretch);
		
		this.stretchColumns = new Vector<Integer>();
		apply();
	}

	protected void calculateMaxWidths() {
		max_widths.clear();
		for (Widget wt : widgets) {
			if (wt instanceof TableRow) {
				int ix = 0;
				TableRow row = (TableRow)wt;
				for (Widget w : row.getWidgets()) {
					if (w.getPropertyByAttName("android:layout_column") != null) {
						ix = Integer.parseInt(w.getPropertyByAttName("android:layout_column").getValue().toString());
					}
					w.apply();
					int wd = w.getPadding(Widget.LEFT)+w.getWidth()+w.getPadding(Widget.RIGHT);
					if (ix >= max_widths.size()) {
						while (max_widths.size() < ix) {
							max_widths.add(0);
						}
						max_widths.add(wd);
					}
					else if (max_widths.get(ix) < wd) {
						max_widths.set(ix, wd);
					}
					ix++;
				}
			}
		}
		int total = 0;
		for (int i : max_widths) {
			total += i;
		}
		int extra = getWidth()-total;
		if (extra > 0 && stretchColumns.size() > 0) {
			int share = extra/stretchColumns.size();
			for (int col : stretchColumns) {
				if (col < max_widths.size())
					max_widths.set(col, max_widths.get(col)+share);
			}
		}
	}
	
	@Override
  public void apply() {
		if (stretch != null) {
			String cols = stretch.getStringValue();
			stretchColumns.clear();
			if (cols != null) {
				StringTokenizer toks = new StringTokenizer(cols, ",");
				while (toks.hasMoreTokens()) {
					stretchColumns.add(new Integer(toks.nextToken()));
				}
			}
		}
		super.apply();
	}
	
	@Override
  public void resizeForRendering() {
		calculateMaxWidths();
		for (Widget w : widgets) {
			if (w instanceof TableRow) {
				((TableRow)w).setWidths(max_widths);
			}
			else {
				w.setSizeInternal(getWidth()-w.getPadding(Widget.LEFT)-w.getPadding(Widget.RIGHT),
								  w.getHeight());
				if (w instanceof Layout) {
					((Layout)w).resizeForRendering();
				}
			}
		}
	}
}
