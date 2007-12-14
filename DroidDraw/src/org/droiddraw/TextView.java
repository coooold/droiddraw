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
	
	
	int pad_x = 6;
	int pad_y = 4;
	
	PropertiesPanel p;
	Font f;
	BufferedImage bg;
	
	protected static final String[] propertyNames = 
		new String[] {"android:textSize", "android:textStyle", "android:typeface"};
	
	public TextView(String str) {
		super("TextView");
		
		text = new StringProperty("Text", "android:text", str!=null?str:"");
		fontSz = new StringProperty("Font Size", "android:textSize", fontSize+"sp");
		face = new SelectProperty("Font Face", "android:typeface", new String[] {"normal","sans","serif","monospace"}, 0);
		style = new SelectProperty("Font Style", "android:textStyle", new String[] {"normal", "bold", "italic", "bold_italic"}, 0);
		
		props.add(text);
		props.add(fontSz);
		props.add(face);
		props.add(style);
		buildFont();

		bg = new BufferedImage(1,1,BufferedImage.TYPE_BYTE_GRAY);
		apply();
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
		this.baseline = fontSize+pad_y/2;
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
		if (text.getStringValue() != null) {
			g.setColor(Color.black);
			g.setFont(f);
			g.drawString(text.getStringValue(), getX()+pad_x/2, getY()+fontSize+pad_y/2);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Property> getProperties() {
		return props;
	}
}
