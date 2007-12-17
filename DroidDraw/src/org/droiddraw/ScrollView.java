package org.droiddraw;

public class ScrollView extends FrameLayout {
	StringProperty scrollbar_size;
	StringProperty scrollbar_fade;
	SelectProperty scrollbars;
	
	public ScrollView() {
		this.tagName = "ScrollView";
		scrollbar_size = new StringProperty("Scrollbar Size", "android:scrollbarSize", "");
		scrollbar_fade = new StringProperty("Scrollbar Fade Duration", "android:scrollbarFadeDuration", "");
		scrollbars = new SelectProperty("Scrollbars", "android:scrollbars", new String[] {"none", "horizontal", "vertical"}, 0);
		props.add(scrollbar_size);
		props.add(scrollbar_fade);
		props.add(scrollbars);
	}
}
