package org.droiddraw.property;

public abstract class Property {
	protected String englishName;
	protected String attName;
	protected boolean editable;
	
	public Property(String englishName, String attName, boolean editable) {
		super();
		this.englishName = englishName;
		this.attName = attName;
		this.editable = editable;
	}
		
	public boolean getEditable() {
		return editable;
	}
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public String getEnglishName() {
		return englishName;
	}

	public String getAtttributeName() {
		return attName;
	}
	
	public abstract Object getValue();
	public abstract void setValue(String value);
	
	public boolean equals(Object o) {
		if (o instanceof Property) {
			Property prop = (Property)o;
			return prop.getAtttributeName().equals(this.getAtttributeName());
		}
		return false;
	}
}
