package org.droiddraw.widget;

public class Ticker extends FrameLayout {
	public Ticker() {
		this.tagName = "Ticker";
		apply();
	}

	@Override
	public void addWidget(Widget w) {
		if (widgets.size() == 0)
			super.addWidget(w);
	}
}
