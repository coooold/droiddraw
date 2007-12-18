package org.droiddraw.property;

import java.awt.Color;

import org.droiddraw.AndroidEditor;

public class ColorProperty extends StringProperty {
	Color c;
	public ColorProperty(String englishName, String attName, Color defaultValue) {
		super(englishName, attName, makeColor(defaultValue));
		this.c = defaultValue;
	}

	public void setStringValue(String col) {
		if (col == null || col.length() == 0) {
			setColorValue(null);
		}
		else if (col.startsWith("@")) {
			int ix = col.indexOf("/");
			String name = col.substring(ix+1);
			if (AndroidEditor.instance().getColors() != null)
				setColorValue(AndroidEditor.instance().getColors().get(name));
			super.setStringValue(col);
		}
		else {
			try {
				setColorValue(parseColor(col));
			}
			catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static Color parseColor(String col) {
		int a, r, g, b;
		switch (col.length()) {
		case 4:
			a = 255;
			r = readHex(col.substring(1,2));
			g = readHex(col.substring(2,3));
			b = readHex(col.substring(3));
			break;
		case 5:
			a = readHex(col.substring(1,2));
			r = readHex(col.substring(2,3));
			g = readHex(col.substring(3,4));
			b = readHex(col.substring(4));
			break;
		case 7:
			a = 255;
			r = readHex(col.substring(1,3));
			g = readHex(col.substring(3,5));
			b = readHex(col.substring(5));
			break;
		case 9:
			a = readHex(col.substring(1,3));
			r = readHex(col.substring(3,5));
			g = readHex(col.substring(5,7));
			b = readHex(col.substring(7));
			break;
		default:
			throw new NumberFormatException(col);
		}
		return new Color(r,g,b,a);
	}
	
	public Color getColorValue() {
		return c;
	}
	
	public void setColorValue(Color c) {
		this.c = c;
		super.setStringValue(makeColor(this.c));
	}
	
	public static final int readHex(String in) {
		return Integer.parseInt(in, 16);
	}
	
	public static final String makeColor(java.awt.Color c) {
		if (c == null)
			return "";
		return "#"+hexString(c.getAlpha())+
				hexString(c.getRed())+
				hexString(c.getGreen())+
				hexString(c.getBlue());
	}
	
	public static final String hexString(int c) {
		String res = Integer.toHexString(c);
		if (res.length() == 1) {
			res = "0"+res;
		}
		return res;
	}
}
