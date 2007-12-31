package org.droiddraw.property;

public abstract class Property {
	protected String englishName;
	protected String attName;
	protected boolean editable;
	protected boolean def;
	
	public Property(String englishName, String attName, boolean editable) {
		this(englishName, attName, editable, true);
	}
	
	public Property(String englishName, String attName, boolean editable, boolean def) {
		super();
		this.englishName = englishName;
		this.attName = attName;
		this.editable = editable;
		this.def = def;
	}
		
	public boolean getEditable() {
		return editable;
	}
	
	public void setDefaulted(boolean def) {
		this.def = def;
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
	
	public boolean isDefault() {
		if (editable && def)
			return isDefaultInternal();
		else
			return false;
	}
	
	protected abstract boolean isDefaultInternal();
	
	public boolean equals(Object o) {
		if (o instanceof Property) {
			Property prop = (Property)o;
			return prop.getAtttributeName().equals(this.getAtttributeName());
		}
		return false;
	}
}
