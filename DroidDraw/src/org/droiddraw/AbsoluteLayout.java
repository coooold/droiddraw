package org.droiddraw;

import java.util.Vector;

public class AbsoluteLayout extends AbstractLayout {
	public AbsoluteLayout() {
		super("AbsoluteLayout");
	}

	@Override
	public void positionWidget(Widget w) {
		apply();
	}

	@Override
	public void repositionAllWidgets() {
		apply();
	}

	public void addOutputProperties(Widget w, Vector<Property> properties) {
		properties.add(new StringProperty("X Position","android:layout_x", w.getX()+"px"));
		properties.add(new StringProperty("Y Position","android:layout_y", w.getY()+"px"));
	}
	
	public void addEditableProperties(Widget w) {}
	public void removeEditableProperties(Widget w) {}
	
	public void apply() {
		super.apply();
	}
}
