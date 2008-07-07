package org.droiddraw.widget;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Vector;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.NineWayImage;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;

public class EditView extends TextView {
	BooleanProperty password;
	BooleanProperty numeric;
	BooleanProperty phone;
	BooleanProperty autoText;
	SelectProperty capitalize;
	StringProperty digits;
	
	NineWayImage img;
	Image img_base;
	
  	public static final String[] propertyNames = 
		new String[] {"android:password", "android:capitalize", "android:numeric", "android:phoneNumber","android:autoText","android:digits"};
	
	public EditView(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName="EditText";
		
		
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
		
		img_base = null;
		
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default")) {
			fontSz.setStringValue("18sp");
			fontSize = 18;
			img_base = ImageResources.instance().getImage("def/textfield.9");
			if (img_base != null) {
				this.img = new NineWayImage(img_base, 10, 10);
			}
			pad_x = 20;
			pad_y = 0;
		}
		else if (theme.equals("light")) {
			img_base = ImageResources.instance().getImage("light/editbox_background_normal.9");
			if (img_base != null) {
				this.img = new NineWayImage(img_base, 10, 10);
			}
			pad_x = 18;
			pad_y = 0;
		}
		
		apply();
	}
	
	@Override
  public Vector<Property> getProperties() {
		Vector<Property> ret = super.getProperties();
		if (digits.getStringValue() == null || digits.getStringValue().length() < 1)
			ret.remove(digits);
		return ret;
	}
	
	@Override
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
	
	@Override
  public int getContentHeight() {
		//int sup = super.getContentWidth();
		//if (sup > fontSize) {
		//	return sup;
		//}
		//else {
		if (img_base != null) {
			String theme = AndroidEditor.instance().getTheme();
			if (theme == null || theme.equals("default"))
				return img_base.getHeight(null)-5;
			else if (theme.equals("light"))
				return img_base.getHeight(null)-5;
		}
		return fontSize;
		//}
	}
	
	@Override
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
		g.setColor(textColor.getColorValue());
		//g.drawString(s, getX()+pad_x/2, getY()+fontSize+pad_y/2-1);
		this.drawText(g, 0, (fontSize+getHeight())/2-1);
		g.setColor(Color.black);
		g.fillRect(getX()+pad_x/2-4, getY()+(getHeight()-fontSize)/2-3, 1, fontSize+5);
	}
}
