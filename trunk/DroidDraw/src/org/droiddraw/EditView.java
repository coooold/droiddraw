package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

public class EditView extends TextView {
	BooleanProperty password;
	BooleanProperty numeric;
	BooleanProperty phone;
	BooleanProperty autoText;
	SelectProperty capitalize;
	StringProperty digits;
	
	NineWayImage img;
	
	public static final String[] propertyNames = 
		new String[] {"android:password", "android:capitalize", "android:numeric", "android:phoneNumber","android:autoText","android:digits"};
	
	public EditView(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName="EditText";
		
		pad_x = 18;
		pad_y = 12;
		
		password = new BooleanProperty("Password", "android:password", false);
		capitalize = new SelectProperty("Capitalize", "android:capitalize", new String[] {"sentences", "words"}, 0);
		numeric = new BooleanProperty("Numbers Only", "android:numeric", false);
		phone = new BooleanProperty("Phone Number", "android:phoneNumber", false);
		autoText = new BooleanProperty("Correct Spelling", "android:autoText", false);
		digits = new StringProperty("Valid Characters", "android:digits", "");
		
		props.add(password);
		props.add(numeric);
		props.add(phone);
		props.add(autoText);
		props.add(capitalize);
		props.add(digits);
		
		Image img = ImageResources.instance().getImage("editbox_background_normal.9");
		if (img != null) {
			this.img = new NineWayImage(img, 10, 10);
		}
		apply();
	}
	
	public Vector<Property> getProperties() {
		Vector<Property> ret = super.getProperties();
		if (digits.getStringValue() == null || digits.getStringValue().length() < 1)
			ret.remove(digits);
		return ret;
	}
	
	public int getContentWidth() {
		if (password != null && password.getBooleanValue()) {
			String s = "";
			for (int i=0;i<text.getStringValue().length();i++) 
				s = s+'¥';
			return stringLength(s)+pad_x;
		}
		else {
			return super.getContentWidth();
		}
	}
	
	public void paint(Graphics g) {
		if (img == null) {
			g.setColor(Color.white);
			g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
			g.setColor(Color.darkGray);
			g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		}
		else {
			img.paint(g, getX(), getY(),getWidth(), getHeight());
			g.setColor(Color.darkGray);
		}
		g.setFont(f);
		String s;
		if (password.getBooleanValue()) {
			s = "";
			for (int i=0;i<text.getStringValue().length();i++) 
				s = s+'¥';
		}
		else {
			s = text.getStringValue();
		}
		g.drawString(s, getX()+pad_x/2, getY()+fontSize+pad_y/2-1);
		g.drawLine(getX()+pad_x/2-2, getY()+4, getX()+pad_x/2-2, getY()+fontSize+pad_y/2+1);
	}
}
