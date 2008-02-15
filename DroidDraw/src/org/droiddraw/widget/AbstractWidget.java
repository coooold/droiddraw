package org.droiddraw.widget;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.ColorProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.util.DisplayMetrics;


public abstract class AbstractWidget implements Widget {
	int x, y;
	int[] padding;
	
	int baseline;
	int width, height;
	String tagName;
	Vector<Property> props;
	PropertyChangeListener listener;
	
	protected StringProperty id;
	protected static int widget_num = 0;
	Layout parent;
	
	StringProperty widthProp;
	StringProperty heightProp;
	StringProperty pad;
	SelectProperty visibility;
	ColorProperty background;
	
	public AbstractWidget(String tagName) {
		this.tagName = tagName;
		this.props = new Vector<Property>();
		this.id = new StringProperty("Id", "android:id", "");
		this.id.setStringValue("@+id/widget"+(widget_num++));
		this.widthProp = new StringProperty("Width", "android:layout_width", "");
		this.widthProp.setStringValue("wrap_content");
		this.heightProp = new StringProperty("Height", "android:layout_height", "");
		this.heightProp.setStringValue("wrap_content");
		this.background = new ColorProperty("Background Color", "android:background", null);
		this.pad = new StringProperty("Padding", "android:padding", "0px");
		this.visibility = new SelectProperty("Visible", "android:visibility", new String[] {"visible", "invisible", "gone"}, 0);
		this.padding = new int[4];
		
		this.props.add(id);
		this.props.add(widthProp);
		this.props.add(heightProp);
		this.props.add(background);
		this.props.add(pad);
		this.props.add(visibility);
		this.baseline = 14;
		
		this.parent = null;
	}
	
	public void setPropertyChangeListener(PropertyChangeListener l) {
		this.listener = l;
	}
	
	public Layout getParent() {
		return parent;
	}
	
	public void setParent(Layout parent) {
		this.parent = parent;
		((AbstractWidget)parent).parentTest(this);
	}
	
	public void parentTest(Widget w) {
		try {
			if (w == this)
				throw new IllegalArgumentException("BAD!");
		}
		catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
		if (getParent() != null) {
			((AbstractWidget)getParent()).parentTest(w);
		}
	}
	
	public String getId() {
		return id.getStringValue();
	}

	public void setId(String id) {
		this.id.setStringValue(id);
	}
	
	public Vector<Property> getProperties() {
		return props;
	}
	
	public void addProperty(Property p) {
		if (!props.contains(p)) {
			props.add(p);
		}
		if (listener != null)
			listener.propertyChange(new PropertyChangeEvent(this, "properties", null, props));
	}
	
	public void removeProperty(Property p) {
		props.remove(p);
		if (listener != null)
			listener.propertyChange(new PropertyChangeEvent(this, "properties", null, props));
	}
	
	public Property getPropertyByAttName(String attName) {
		for (Property prop : props) {
			if (prop.getAtttributeName().equals(attName)) {
				return prop;
			}
		}
		return null;
	}
	
	public boolean propertyHasValueByAttName(String attName, Object value) {
		for (Property prop : props) {
			if (prop.getAtttributeName().equals(attName) && prop.getValue().equals(value)) {
				return true;
			}
		}
		return false;
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

	public void setWidth(int width) {
		this.widthProp.setStringValue(width+"px");
		apply();
	}
	
	public void setHeight(int height) {
		this.heightProp.setStringValue(height+"px");
		apply();
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
		
		try {
			setPadding(DisplayMetrics.readSize(pad.getStringValue()));	
		} catch (NumberFormatException ex) {}
		
		if (widthProp.getStringValue().equals("wrap_content")) {
			w = getContentWidth();
		}
		if (heightProp.getStringValue().equals("wrap_content")) {
			h = getContentHeight();
		}
		
		if (widthProp.getStringValue().equals("fill_parent")) {
			if (getParent() != null) {
				StringProperty prop = (StringProperty)parent.getPropertyByAttName("android:layout_width");
				if (prop.getStringValue().equals("wrap_content"))
					w = getContentWidth();
				else
					w = getParent().getWidth();
			}
			else {
				w = AndroidEditor.instance().getScreenX()-AndroidEditor.OFFSET_X;
			}
			w = w-getX()-padding[RIGHT];
		}
		if (heightProp.getStringValue().equals("fill_parent")) {
			if (getParent() != null) {
				StringProperty prop = (StringProperty)parent.getPropertyByAttName("android:layout_height");
				if (prop.getStringValue().equals("wrap_content"))
					h = getContentHeight();
				else
					h = getParent().getHeight();
			}
			else {
				h = AndroidEditor.instance().getScreenY();
			}
			h = h-getY()-padding[BOTTOM];
		}

		width = w;
		height = h;
	}
	
	public void apply() {
		readWidthHeight();
		if (getParent() == null) {
			setPosition(getPadding(LEFT)+AndroidEditor.OFFSET_X, getPadding(TOP)+AndroidEditor.OFFSET_Y);
		}

		//if (getParent() != null) {
		//	getParent().repositionAllWidgets();
		//}
	}
	
	public void setSizeInternal(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public int getBaseline() {
		return baseline;
	}
	
	public void drawBackground(Graphics g) {
		if (background.getColorValue() != null) {
			g.setColor(background.getColorValue());
			g.fillRect(getX()-getPadding(LEFT), getY()-getPadding(TOP), getWidth()+getPadding(LEFT)+getPadding(RIGHT), getHeight()+getPadding(TOP)+getPadding(BOTTOM));
		}
	}

	public void setPadding(int pad) {
		setPadding(pad, TOP);
		setPadding(pad, LEFT);
		setPadding(pad, BOTTOM);
		setPadding(pad, RIGHT);
	}
	
	public void setPadding(int pad, int which) {
		padding[which] = pad;
	}
	
	public int getPadding(int which) {
		return padding[which];
	}
	
	public boolean isVisible() {
		return visibility.getStringValue().equals("visible");
	}
	
	protected abstract int getContentWidth();
	protected abstract int getContentHeight();
}
