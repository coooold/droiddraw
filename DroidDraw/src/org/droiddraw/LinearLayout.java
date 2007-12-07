package org.droiddraw;

import java.util.Vector;

public class LinearLayout extends AbstractLayout {
	SelectProperty orientation;
	public static int VERTICAL_PADDING = 4;
	public static int HORIZONTAL_PADDING = 4;
	
	private static final String[] OPTIONS = new String[] {"left", "right","center_horizontal", "top", "bottom", "center_vertical"};
	
	
	public LinearLayout() {
		this(true);
	}
	
	public LinearLayout(boolean vertical) {
		super("LinearLayout");
		this.orientation = new SelectProperty("Orientation", "android:orientation", new String[] {"horizontal", "vertical"}, 0);
		addProperty(orientation);
	}
		
	@Override
	public void positionWidget(Widget w) {
		widgets.remove(w);
		boolean vertical = "vertical".equals(orientation.getStringValue());
		if (vertical) {
			int y = w.getY();
			if (y >= 0) {
				int ix;
				for (ix = 0;ix < widgets.size() && y > widgets.get(ix).getY(); ix++);
				widgets.add(ix, w);
			}
			else {
				widgets.add(w);
			}
		}
		else {
			int x = w.getX();
			if (x >= 0) {
				int ix;
				for (ix = 0;ix < widgets.size() && x > widgets.get(ix).getX(); ix++);
				widgets.add(ix, w);
			}
			else {
				widgets.add(w);
			}
		}
		repositionAllWidgets();
	}

	@Override
	public void repositionAllWidgets() {
		boolean vertical = "vertical".equals(orientation.getStringValue());
		int y = 0;
		int x = 0;
		
		for (Widget w : widgets) {
			String gravity = (String)w.getPropertyByAttName("android:layout_gravity").getValue();
			if (vertical) {
				if ("right".equals(gravity))
					x = getWidth()-w.getWidth();
				else if ("center_horizontal".equals(gravity) || "center".equals(gravity)) 
					x = getWidth()/2-w.getWidth()/2;
				else
					x = 0;
			}
			else {
				if ("bottom".equals(gravity))
					y = getHeight()-w.getHeight();
				else if ("center_vertical".equals(gravity) || "center".equals(gravity))
					y = (getHeight()-w.getHeight())/2;
				else
					y = 0;
			}
			w.setPosition(x, y);
			if (vertical)
				y+=w.getHeight()+VERTICAL_PADDING;
			else {
				x+=w.getWidth()+HORIZONTAL_PADDING;
			}
		}
	}

	public void addOutputProperties(Widget w, Vector<Property> properties) {}

	public void addEditableProperties(Widget w) {
		w.addProperty(new StringProperty("Linear Layout Weight", "android:layout_weight", "0"));
		w.addProperty(new SelectProperty("Layout Gravity", "android:layout_gravity", OPTIONS, 0));
	}
	
	public void removeEditableProperties(Widget w) {
		w.removeProperty(new StringProperty("", "android:layout_weight", ""));
		w.removeProperty(new SelectProperty("", "android:layout_gravity", new String[] {""}, 0));
	}
}
