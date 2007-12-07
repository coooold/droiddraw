package org.droiddraw;

public class BooleanProperty extends Property {
	boolean value;
	
	public BooleanProperty(String englishName, String attName, boolean defaultValue) {
		super(englishName, attName);
		this.value = defaultValue;
	}

	@Override
	public Object getValue() {
		if (value)
			return "true";
		else
			return "false";
	}
	
	public boolean getBooleanValue() {
		return value;
	}
	
	public void setBooleanValue(boolean b) {
		this.value = b;
	}

	@Override
	public void setValue(String value) {
		if ("true".equals(value)) {
			this.value = true;
		}
		else if ("false".equals(value)) {
			this.value = false;
		}
	}
}
