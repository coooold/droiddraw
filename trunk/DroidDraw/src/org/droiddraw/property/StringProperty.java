package org.droiddraw.property;

import org.droiddraw.AndroidEditor;

public class StringProperty extends Property {
	String value;
	String defaultValue;
	
	public StringProperty(String englishName, String attName, String defaultValue) {
		this(englishName, attName, defaultValue, true);
		this.defaultValue = defaultValue;
	}
	
	public StringProperty(String englishName, String attName, String defaultValue, boolean editable) {
		super(englishName, attName, editable);
		this.value = defaultValue;
	}

	@Override
	protected boolean isDefaultInternal() {
		return value.equals(defaultValue);
	}
	
	@Override
	public Object getValue() {
		return getStringValue();
	}
	
	public String getStringValue() {
		if (value != null && value.startsWith("@string") && AndroidEditor.instance().getStrings() != null) {
			String key = value.substring(value.indexOf("/")+1);
			String str = AndroidEditor.instance().getStrings().get(key);
			if (str == null)
				str = value;
			return str;
		}
		else {
			return value;
		}
	}
	
	public void setStringValue(String value) {
		this.value = value;
	}

	@Override
	public void setValue(String value) {
		setStringValue(value);
	}
}
