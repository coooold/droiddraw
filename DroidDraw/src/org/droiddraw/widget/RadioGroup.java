package org.droiddraw.widget;

import org.droiddraw.property.StringProperty;

public class RadioGroup extends LinearLayout {
	public static final String TAG_NAME = "RadioGroup";
	StringProperty checkedItem;
	public RadioGroup() {
		super();
		checkedItem = new StringProperty("Default Button", "android:checkedButton", "");
		addProperty(checkedItem);
		this.setTagName(TAG_NAME);
	}
}
