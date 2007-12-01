package org.droiddraw;

import java.util.Vector;

public abstract class AbstractLayout implements Layout {
	protected Vector<Widget> widgets;
	
	public AbstractLayout() {
		widgets = new Vector<Widget>();
	}
	
	public void addWidget(Widget w) {
		widgets.add(w);
		positionWidget(w);
	}
	
	public Vector<Widget> getWidgets() {
		return widgets;
	}

	public void removeWidget(Widget w) {
		widgets.remove(w);
		repositionAllWidgets(widgets);
	}

	public abstract void positionWidget(Widget w);
	protected abstract void repositionAllWidgets(Vector<Widget> widgets);
}
