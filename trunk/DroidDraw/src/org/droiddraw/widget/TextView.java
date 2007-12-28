package org.droiddraw.widget;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import org.droiddraw.AndroidEditor;
import org.droiddraw.gui.PropertiesPanel;
import org.droiddraw.property.ColorProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.util.DisplayMetrics;

public class TextView extends AbstractWidget {
	int fontSize = 14;
	
	StringProperty text;
	StringProperty fontSz;
	SelectProperty face;
	SelectProperty style;
	ColorProperty textColor;
	
	int pad_x = 6;
	int pad_y = 4;
	
	PropertiesPanel p;
	Font f;
	BufferedImage bg;
	
	public static final String[] propertyNames = 
		new String[] {"android:textSize", "android:textStyle", "android:typeface", "android:textColor"};
	
	public TextView(String str) {
		super("TextView");
		
		text = new StringProperty("Text", "android:text", str!=null?str:"");
		fontSz = new StringProperty("Font Size", "android:textSize", fontSize+"sp");
		face = new SelectProperty("Font Face", "android:typeface", new String[] {"normal","sans","serif","monospace"}, 0);
		style = new SelectProperty("Font Style", "android:textStyle", new String[] {"normal", "bold", "italic", "bold_italic"}, 0);
		textColor = new ColorProperty("Text Color", "android:textColor", Color.black);
		
		props.add(text);
		props.add(fontSz);
		props.add(face);
		props.add(style);
		props.add(textColor);
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
		if (fontSz.getStringValue() != null && fontSz.getStringValue().length() > 0) {
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
		int l = stringLength(text.getStringValue())+pad_x;
		if (l > AndroidEditor.instance().getScreenX())
			l = AndroidEditor.instance().getScreenX()-getX();
		return l;
	}
	
	protected int getSplit(String txt) {
		int ix = 1;
		int w = AndroidEditor.instance().getScreenX();
		while (ix <=txt.length() && stringLength(txt.substring(0, ix)) < w) {
			ix++;
		}
		return ix-1;
	}
	
	protected int getContentHeight() {
		int h = fontSize+pad_y;
		String txt = text.getStringValue();
		int l = stringLength(txt)+pad_x;
		while (l > AndroidEditor.instance().getScreenX()) {
			int split = getSplit(txt);
			txt = txt.substring(split);
			l = stringLength(txt)+pad_x;
			h += fontSize+1;
		}
		return h;
	}
	
	public void paint(Graphics g) {
		drawBackground(g);
		if (text.getStringValue() != null) {
			Color c = textColor.getColorValue();
			if (c == null)
				c = Color.black;
			g.setColor(c);
			g.setFont(f);
			
			int h = fontSize+pad_y/2;
			String txt = text.getStringValue();
			int l = stringLength(txt)+pad_x;
			int w = AndroidEditor.instance().getScreenX();
			while (l >= w && h < getHeight()) {
				int split = getSplit(txt);
				g.drawString(txt.substring(0, split), getX()+pad_x/2, getY()+h);
				txt = txt.substring(split);
				l = stringLength(txt)+pad_x;
				h += fontSize+1;
			}
			if (h < getHeight())
				g.drawString(txt.substring(0, getSplit(txt)), getX()+pad_x/2, getY()+h);
		}
	}
	
	public Vector<Property> getProperties() {
		return props;
	}
}
