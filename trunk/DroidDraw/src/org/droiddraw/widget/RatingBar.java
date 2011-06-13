package org.droiddraw.widget;

import java.awt.Graphics;
import java.awt.Image;

import org.droiddraw.gui.ImageResources;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.FloatProperty;
import org.droiddraw.property.IntProperty;

public class RatingBar extends ProgressBar {
	Image star;
	IntProperty numStars;
	BooleanProperty indicator;
	//FloatProperty rating;
	//FloatProperty stepSize;
	
	public static final String TAG_NAME = "RatingBar";
	public static final String[] propertyNames = new String[] { "android:numStars",
																"android:isIndicator",
																//"android:rating",
																//"android:stepSize"
															  };
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3214432742146876735L;

	public RatingBar() {
		setTagName(TAG_NAME);
		star = ImageResources.instance().getImage("rate_star_med_on");
		numStars = new IntProperty("Number of Stars", "android:numStars", 5);
		//rating = new FloatProperty("Rating", "android:rating", 5.0f);
		//stepSize = new FloatProperty("Step Size", "android:stepSize", 1.0f);
		indicator = new BooleanProperty("User Editable", "android:isIndicator", true);
		addProperty(numStars);
		addProperty(indicator);
		//addProperty(rating);
		//addProperty(stepSize);
	}
	
	@Override
	public void paint(Graphics g) {
		int width = 42;
		for (int i = 0; i < numStars.getIntValue(); ++i) {
			g.drawImage(star, getX()  + width * i, getY(), width, getHeight(), null);
		}
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("Not yet...");
	}

	@Override
	protected int getContentHeight() {
		return 44;
	}

	@Override
	protected int getContentWidth() {
		int stars = 5;
		if (numStars != null) {
			stars = numStars.getIntValue();
		}
		return 42 * stars;
	}
	
	

}
