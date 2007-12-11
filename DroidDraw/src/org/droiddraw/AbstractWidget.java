package org.droiddraw;
import java.util.Vector;


public abstract class AbstractWidget implements Widget {
	int x, y;
	int width, height;
	String tagName;
	Vector<Property> props;
	protected StringProperty id;
	protected static int widget_num = 0;
	Layout parent;
	
	StringProperty widthProp;
	StringProperty heightProp;
	
	public AbstractWidget(String tagName) {
		this.tagName = tagName;
		this.props = new Vector<Property>();
		this.id = new StringProperty("Id", "id", "@+id/widget"+(widget_num++));
		this.widthProp = new StringProperty("Width", "android:layout_width", "wrap_content");
		this.heightProp = new StringProperty("Height", "android:layout_height", "wrap_content");
		this.props.add(id);
		this.props.add(widthProp);
		this.props.add(heightProp);
		this.parent = null;
	}
	
	public Layout getParent() {
		return parent;
	}
	
	public void setParent(Layout parent) {
		this.parent = parent;
	}
	
	public String getId() {
		return id.getStringValue();
	}

	public Vector<Property> getProperties() {
		return props;
	}
	
	public void addProperty(Property p) {
		if (!props.contains(p)) {
			props.add(p);
		}
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
		this.widthProp.setStringValue(width+"px");
		this.heightProp.setStringValue(height+"px");
		apply();
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
		int off_x = 0;
		int off_y = 0;
		if (parent != null) {
			off_x = parent.getScreenX();
			off_y = parent.getScreenY();
		}
		return (x > this.getX()+off_x && x < this.getX()+off_x+getWidth()
				&& y > this.getY()+off_y && y < this.getY()+getHeight()+off_y);
	}

	public void move(int dx, int dy) {
		setPosition(this.x+dx, this.y+dy);
	}
	
	public String getTagName() {
		return tagName;
	}
	
	protected void readWidthHeight() {
		int w = DisplayMetrics.readSize(widthProp);
		int h = DisplayMetrics.readSize(heightProp);
		if (w < 0) {
			w = getWidth();
		}
		if (h < 0) {
			h = getHeight();
		}
		
		if (widthProp.getStringValue().equals("wrap_content"))
			w = getContentWidth();
		if (heightProp.getStringValue().equals("wrap_content"))
			h = getContentHeight();
		
		if (widthProp.getStringValue().equals("fill_parent")) 
			w = getParent()!=null?getParent().getWidth():AndroidEditor.instance().getScreenX()-AndroidEditor.OFFSET_X;
		if (heightProp.getStringValue().equals("fill_parent"))
			h = getParent()!=null?getParent().getHeight():AndroidEditor.instance().getScreenY()-AndroidEditor.OFFSET_Y;
		
		width = w;
		height = h;
	}
	
	public void apply() {
		readWidthHeight();
		if (getParent() != null) {
			getParent().apply();
		}
	}
	
	public void setSizeInternal(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	protected abstract int getContentWidth();
	protected abstract int getContentHeight();
}
