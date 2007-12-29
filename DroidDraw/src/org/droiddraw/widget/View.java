package org.droiddraw.widget;

import java.awt.Graphics;

public class View extends AbstractWidget {
	public View() {
		super("View");
	}

	@Override
	protected int getContentHeight() {
		return 0;
	}

	@Override
	protected int getContentWidth() {
		return 0;
	}

	public void paint(Graphics g) {
		g.setColor(background.getColorValue());
		g.fillRect(getX(), getY(), getWidth(), getHeight());
	}
}
