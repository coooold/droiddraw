package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

public class EditView extends TextView {
	BooleanProperty password;
	BooleanProperty numeric;
	BooleanProperty phone;
	BooleanProperty autoText;
	SelectProperty capitalize;
	StringProperty digits;
	
	public EditView(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName="EditText";
		
		setSize(txt.length()*8+12, fontSize+6);
		
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
	}
	

	@SuppressWarnings("unchecked")
	public Vector<Property> getProperties() {
		Vector<Property> ret = super.getProperties();
		if (digits.getStringValue().length() < 1)
			ret.remove(digits);
		return ret;
	}
	
	public void paint(Graphics g) {
		if (width.getStringValue().equals("wrap_content"))
			setSize(g.getFontMetrics(f).stringWidth(text.getStringValue())+12, fontSize+6);

		g.setColor(Color.white);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		
		g.setColor(Color.darkGray);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+8, getY()+fontSize+2);
		g.drawLine(getX()+7, getY()+2, getX()+7, getY()+fontSize+2);
	}
}
