package org.droiddraw;

public abstract class Property {
	protected String englishName;
	protected String attName;

	public Property(String englishName, String attName) {
		super();
		this.englishName = englishName;
		this.attName = attName;
	}
		
	public String getEnglishName() {
		return englishName;
	}

	public String getAtttributeName() {
		return attName;
	}
	
	public abstract Object getValue();
	
	public boolean equals(Object o) {
		if (o instanceof Property) {
			Property prop = (Property)o;
			return prop.getAtttributeName().equals(this.getAtttributeName());
		}
		return false;
	}
}
