package org.droiddraw.widget;

import java.util.Vector;

import org.droiddraw.property.Property;

public class FrameLayout extends AbstractLayout {
	public FrameLayout() {
		super("FrameLayout");
	}
	
	@Override
	protected void addEditableProperties(Widget w) {}

	@Override
	public void positionWidget(Widget w) {
		w.setPosition(0, 0);
	}

	@Override
	protected void removeEditableProperties(Widget w) {}

	@Override
	public void repositionAllWidgets() {
		for (Widget w : widgets) {
			w.setPosition(0, 0);
		}
	}

	public void addOutputProperties(Widget w, Vector<Property> properties) {}
}
