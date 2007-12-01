package org.droiddraw;

public class StringProperty extends Property {
	String value;
	
	public StringProperty(String englishName, String attName, String defaultValue) {
		super(englishName, attName);
		this.value = defaultValue;
	}

	@Override
	public Object getValue() {
		return getStringValue();
	}
	
	public String getStringValue() {
		return value;
	}
	
	public void setStringValue(String value) {
		this.value = value;
	}
}
