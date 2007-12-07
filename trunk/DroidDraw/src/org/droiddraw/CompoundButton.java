package org.droiddraw;

public class CompoundButton extends Button {
	public CompoundButton(String text) {
		super(text);
		addProperty(new BooleanProperty("Checked", "android:checked", false));
	}
}
