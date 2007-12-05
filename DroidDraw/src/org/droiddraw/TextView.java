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
	StringProperty width;
	StringProperty height;
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
		
		text = new StringProperty("Text", "android:text", str);
		fontSz = new StringProperty("Font Size", "android:textSize", fontSize+"");
		width = new StringProperty("Width", "android:layout_width", "wrap_content");
		height = new StringProperty("Height", "android:layout_height", "wrap_content");
		face = new SelectProperty("Font Face", "android:typeface", new String[] {"plain","sans","serif","monospace"}, 0);
		style = new SelectProperty("Font Style", "android:textStyle", new String[] {"plain", "bold", "italic", "bold_italic"}, 0);
		
		props.add(text);
		//props.add(fontSz);
		props.add(width);
		props.add(height);
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
		fontSize = Integer.parseInt(fontSz.getStringValue());
		readWidthHeight();
		buildFont();
	}
	
	protected int stringLength(String str) {
		return bg.getGraphics().getFontMetrics(f).stringWidth(str);
	}
	
	protected void readWidthHeight() {
		int w = readSize(width);
		int h = readSize(height);
		if (w < 0) {
			w = getWidth();
		}
		if (h < 0) {
			h = getHeight();
		}
		
		if (width.getStringValue().equals("wrap_content"))
			w = stringLength(text.getStringValue())+pad_x;

		if (height.getStringValue().equals("wrap_content"))
			h = fontSize+pad_y;
		
		if (width.getStringValue().equals("fill_parent")) 
			w = AndroidEditor.instance().getScreenX()-AndroidEditor.OFFSET_X;
		if (height.getStringValue().equals("fill_parent"))
			h = AndroidEditor.instance().getScreenY()-AndroidEditor.OFFSET_Y;
		
		setSize(w, h);
	}

	protected int readSize(StringProperty prop) 
	{
		int size = -1;
		String w = prop.getStringValue();
		if (w.endsWith("px")) {
			try {
				size = Integer.parseInt(w.substring(0, w.length()-2));
			} 
			catch (NumberFormatException ex) {}
		}
		return size;
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
