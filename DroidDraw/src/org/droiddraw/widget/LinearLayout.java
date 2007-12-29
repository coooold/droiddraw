package org.droiddraw.widget;

import java.util.Vector;

import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;

public class LinearLayout extends AbstractLayout {
	SelectProperty orientation;
	boolean vertical;
	int max_base;
	
	public static int VERTICAL_PADDING = 2;
	public static int HORIZONTAL_PADDING = 2;

	int share;
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
	public void apply() {
		super.apply();
		if (orientation != null)
			vertical = "vertical".equals(orientation.getStringValue());
	}

	
	
	@Override
	public void addWidget(Widget w) {
		if (!vertical) {
			if (! (w instanceof Layout) && w.getBaseline() > max_base) {
				max_base = w.getBaseline();
			}
		}
		super.addWidget(w);
	}

	@Override
	public void positionWidget(Widget w) {
		widgets.remove(w);
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
		for (Widget w : widgets) {
			w.apply();
		}
		repositionAllWidgetsInternal();
	}

	protected void repositionAllWidgetsInternal() {
		int y = 0;
		int x = 0;
		Vector<Widget> with_weight = new Vector<Widget>();
		int max_base = 0;

		for (Widget w : widgets) {
			if (!vertical) {
				if (! (w instanceof Layout) && w.getBaseline() > max_base) {
					max_base = w.getBaseline();
				}
			}

			StringProperty prop = (StringProperty)w.getPropertyByAttName("android:layout_weight");
			if (prop != null && "1".equals(prop.getStringValue()))
				with_weight.add(w);
			if (vertical)
				y +=w.getPadding(TOP)+w.getHeight()+w.getPadding(BOTTOM);
			else
				x += w.getPadding(LEFT)+w.getWidth()+w.getPadding(RIGHT);
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
			StringProperty prop = (StringProperty)w.getPropertyByAttName("android:layout_gravity");
			if (prop != null)
				gravity = prop.getStringValue();

			if (vertical) {
				int width_w_pad = (w.getPadding(Widget.LEFT)+w.getWidth()+w.getPadding(Widget.RIGHT));
				if ("right".equals(gravity))
					x = getWidth()-width_w_pad;
				else if ("center_horizontal".equals(gravity) || "center".equals(gravity)) 
					x = getWidth()/2-width_w_pad/2;
				else
					x = w.getPadding(Widget.LEFT);
			}
			else {
				int height_w_pad = (w.getPadding(Widget.TOP)+w.getHeight()+w.getPadding(Widget.BOTTOM));
				if ("bottom".equals(gravity))
					y = getHeight()-height_w_pad;
				else if ("center_vertical".equals(gravity) || "center".equals(gravity))
					y = (getHeight()-height_w_pad)/2;
				else  {
					if (w instanceof Layout) {
						y = w.getPadding(Widget.TOP);
					}
					else {
						y = max_base-w.getBaseline()+w.getPadding(TOP);
					}
				}
			}
			if (vertical) {
				y+=w.getPadding(Widget.TOP);
			}
			else {
				x+=w.getPadding(Widget.LEFT);
			}
			w.setPosition(x, y);
			if (vertical) {			
				y+=w.getHeight()+w.getPadding(Widget.BOTTOM);
			}
			else {
				x+=w.getWidth()+w.getPadding(Widget.RIGHT);
			}	
		}
	}

	@Override
	public void resizeForRendering() {
		boolean vertical = "vertical".equals(orientation.getStringValue());

		for (Widget w : widgets) {
			if (w instanceof Layout) {
				((Layout)w).resizeForRendering();
			}
			StringProperty prop = (StringProperty)w.getPropertyByAttName("android:layout_weight");
			boolean weight = (prop != null && "1".equals(prop.getStringValue()));
			if (weight) {
				if (vertical)
					w.setSizeInternal(w.getWidth(), w.getHeight()+share);
				else
					w.setSizeInternal(w.getWidth()+share, w.getHeight());
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
