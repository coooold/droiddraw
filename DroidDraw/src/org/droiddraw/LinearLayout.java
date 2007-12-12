package org.droiddraw;

import java.util.Vector;

public class LinearLayout extends AbstractLayout {
	SelectProperty orientation;
	public static int VERTICAL_PADDING = 2;
	public static int HORIZONTAL_PADDING = 2;

	private static final String[] OPTIONS = new String[] {"left", "right","center_horizontal", "top", "bottom", "center_vertical"};


	public LinearLayout() {
		this(true);
	}

	public LinearLayout(boolean vertical) {
		super("LinearLayout");
		this.orientation = new SelectProperty("Orientation", "android:orientation", new String[] {"horizontal", "vertical"}, 1);
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
		Vector<Widget> with_weight = new Vector<Widget>();
		int share = 0;
		int max_base = 0;
		
		for (Widget w : widgets) {
			w.apply();
			if (!vertical) {
				if (! (w instanceof Layout) && w.getBaseline() > max_base) {
					max_base = w.getBaseline();
				}
			}
			
			StringProperty prop = (StringProperty)w.getPropertyByAttName("android:layout_weight");
			if (prop != null && "1".equals(prop.getStringValue()))
				with_weight.add(w);
			if (vertical)
				y += w.getHeight();
			else
				x += w.getWidth();
		}
		if (with_weight.size() > 0) {
			if (vertical) {
				int extra = getHeight()-y;
				share = extra/with_weight.size();
			}
			else {
				int extra = getWidth()-x;
				share = extra/with_weight.size();
			}
		}
		y=0;
		x=0;
		for (Widget w : widgets) {
			String gravity = "left";
			boolean weight = false;
			StringProperty prop = (StringProperty)w.getPropertyByAttName("android:layout_gravity");
			if (prop != null)
				gravity = prop.getStringValue();
			prop = (StringProperty)w.getPropertyByAttName("android:layout_weight");
			if (prop != null && "1".equals(prop.getStringValue()))
				weight = true;
			
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
				else  {
					if (w instanceof Layout) {
						y = 0;
					}
					else {
						y = max_base-w.getBaseline();
					}
				}
			}
			w.setPosition(x, y);
			if (vertical) {
				if (weight) {
					w.setSizeInternal(w.getWidth(), w.getHeight()+share);
				}					
				y+=w.getHeight();
			}
			else {
				if (weight) {
					w.setSizeInternal(w.getWidth()+share, w.getHeight());
				}
				x+=w.getWidth();
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
