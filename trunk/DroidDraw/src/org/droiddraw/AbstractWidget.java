package org.droiddraw;
import java.util.Vector;


public abstract class AbstractWidget implements Widget {
	int x, y;
	int width, height;
	String tagName;
	Vector<Property> props;
	protected StringProperty id;
	protected static int widget_num = 0;
	
	public AbstractWidget(String tagName) {
		this.tagName = tagName;
		this.props = new Vector<Property>();
		this.id = new StringProperty("Id", "id", "@+id/widget"+(widget_num++));
		this.props.add(id);
	}
	
	public String getId() {
		return id.getStringValue();
	}

	public Vector<Property> getProperties() {
		return props;
	}
	

	public void removeProperty(Property p) {
		props.remove(p);
	}
	
	public Property getPropertyByAttName(String attName) {
		for (Property prop : props) {
			if (prop.getAtttributeName().equals(attName)) {
				return prop;
			}
		}
		return null;
	}
	

	public void setPropertyByAttName(String attName, String value) {
		Property p = getPropertyByAttName(attName);
		if (p != null) {
			p.setValue(value);
		}
		else {
			props.add(new StringProperty(attName, attName, value));
		}
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
