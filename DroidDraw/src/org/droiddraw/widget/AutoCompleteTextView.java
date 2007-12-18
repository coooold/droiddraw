package org.droiddraw.widget;

import org.droiddraw.property.StringProperty;

public class AutoCompleteTextView extends EditView {
	public AutoCompleteTextView(String txt) {
		super(txt);
		this.tagName = "AutoCompleteTextView";
		
		props.add(new StringProperty("Completion Hint", "android:completionHint", ""));
		props.add(new StringProperty("Hint Size", "android:completionHintSize", ""));
		props.add(new StringProperty("Completion Threshold", "android:completionThreshold", ""));

		apply();
	}
}
