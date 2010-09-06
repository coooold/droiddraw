package org.droiddraw.property;

public class WidthProperty extends StringProperty {
	public WidthProperty(String name, String attName, String defaultValue) {
		super(name, attName, defaultValue);
	}
	
	public Object getValue() {
		String val = getStringValue();
		try {
			// This is kind of hacky, try to parse into an int, if that works, append px.
			int value = Integer.parseInt(val);
			return value + "px";
		} catch (Exception ignore) {
		}
		return val;
	}

}
