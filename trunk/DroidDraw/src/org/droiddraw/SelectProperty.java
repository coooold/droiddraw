package org.droiddraw;

public class SelectProperty extends StringProperty {
	String[] options;
	
	public SelectProperty(String englishName, String attName, String[] options, int default_ix) {
		super(englishName, attName, options[default_ix]);
		this.options = options;
	}
	
	public String[] getOptions() {
		return options;
	}
	
	public void setSelectedIndex(int ix) {
		setStringValue(options[ix]);
	}
	
}
