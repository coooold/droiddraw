package org.droiddraw;

import java.util.Vector;

public interface Layout {
	public void addWidget(Widget w);
	public Vector<Widget> getWidgets();
	public void removeWidget(Widget w);
	public void positionWidget(Widget w);
}
