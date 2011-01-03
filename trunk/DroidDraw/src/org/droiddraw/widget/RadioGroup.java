package org.droiddraw.widget;

import org.droiddraw.property.StringProperty;

public class RadioGroup extends LinearLayout {
	public static final String TAG_NAME = "RadioGroup";
	StringProperty checkedItem;
	public RadioGroup() {
		super();
		checkedItem = new StringProperty("Default Button", "android:checkedButton", "");
		// Defaults are different in RadioGroup *sigh*
		orientation.setDefaultIndex(1);
		orientation.setSelectedIndex(1);
		addProperty(checkedItem);
		this.setTagName(TAG_NAME);
	}
}
