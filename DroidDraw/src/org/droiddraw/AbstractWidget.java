package org.droiddraw;
import java.util.Vector;


public abstract class AbstractWidget implements Widget {
	int x, y;
	int width, height;
	String tagName;
	Vector<Property> props;
	
	public AbstractWidget(String tagName) {
		this.tagName = tagName;
		this.props = new Vector<Property>();
	}

	public Vector<Property> getProperties() {
		return props;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean clickedOn(int x, int y) {
		return (x > this.x && x < this.x+width && y > this.y && y < this.y+height);
	}

	public void move(int dx, int dy) {
		setPosition(this.x+dx, this.y+dy);
	}
	
	public String getTagName() {
		return tagName;
	}
}
