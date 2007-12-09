package org.droiddraw;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

public class TextView extends AbstractWidget {
	int fontSize = 14;
	
	StringProperty text;
	StringProperty fontSz;
	SelectProperty face;
	SelectProperty style;
	
	
	int pad_x = 5;
	int pad_y = 3;
	
	PropertiesPanel p;
	Font f;
	BufferedImage bg;
	
	protected static final String[] propertyNames = 
		new String[] {"android:textSize", "android:textStyle", "android:typeface"};
	
	public TextView(String str) {
		super("TextView");
		
		text = new StringProperty("Text", "android:text", str!=null?str:"");
		fontSz = new StringProperty("Font Size", "android:textSize", fontSize+"sp");
		face = new SelectProperty("Font Face", "android:typeface", new String[] {"plain","sans","serif","monospace"}, 0);
		style = new SelectProperty("Font Style", "android:textStyle", new String[] {"plain", "bold", "italic", "bold_italic"}, 0);
		
		props.add(text);
		props.add(fontSz);
		props.add(face);
		props.add(style);
		buildFont();

		bg = new BufferedImage(1,1,BufferedImage.TYPE_BYTE_GRAY);
		int w = bg.getGraphics().getFontMetrics(f).stringWidth(text.getStringValue());
		setSize(w+5, fontSize+3);
	}

	protected void buildFont() {
		f = new Font(face.getStringValue(),Font.PLAIN,fontSize);
		if (style.getStringValue() != null && style.getStringValue().contains("bold")) {
			f = f.deriveFont(f.getStyle() | Font.BOLD);
		}
		if (style.getStringValue() != null && style.getStringValue().contains("italic")) {
			f = f.deriveFont(f.getStyle() | Font.ITALIC);
		}
	}

	public JPanel getEditorPanel() {
		if (p == null) {
			p = new PropertiesPanel(props, this);
		}
		return p;
	}

	public void apply() {
		if (fontSz.getStringValue() != null) {
			fontSize = (DisplayMetrics.readSize(fontSz));
		}
		buildFont();
		super.apply();
	}
	
	protected int stringLength(String str) {
		if (str == null)
			return 0;
		return bg.getGraphics().getFontMetrics(f).stringWidth(str);
	}
	
	protected int getContentWidth() {
		return stringLength(text.getStringValue())+pad_x;
	}
	
	protected int getContentHeight() {
		return fontSize+pad_y;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.black);
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+2, getY()+fontSize);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Property> getProperties() {
		Vector<Property> ret = (Vector<Property>)props.clone();
		if (face.getStringValue() != null && face.getStringValue().equals("plain"))
			ret.remove(face);
		if (style.getStringValue() != null && style.getStringValue().equals("plain"))
			ret.remove(style);
		return ret;
	}
}
