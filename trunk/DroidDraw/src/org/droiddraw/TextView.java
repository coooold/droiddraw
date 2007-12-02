package org.droiddraw;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
	
	PropertiesPanel p;
	Font f;
	
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

		// Heuristic until we get a graphics object...
		setSize(str.length()*8+5, fontSize+3);
	}

	protected void buildFont() {
		f = new Font(face.getStringValue(),Font.PLAIN,fontSize);
		if (style.getStringValue().contains("bold")) {
			f = f.deriveFont(f.getStyle() | Font.BOLD);
		}
		if (style.getStringValue().contains("italic")) {
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
		readWidth();
		buildFont();
	}

	protected void readWidth() {
		String w = width.getStringValue();
		if (w.endsWith("px")) {
			try {
				int width = Integer.parseInt(w.substring(0, w.length()-2));
				setSize(width, fontSize+3);
			} 
			catch (NumberFormatException ex) {}
		}
	}
	
	public void paint(Graphics g) {
		if (width.getStringValue().equals("wrap_content")) {
			int w;
			w = g.getFontMetrics(f).stringWidth(text.getStringValue());
			setSize(w+5, fontSize+3);
		}
		else {
			readWidth();
		}

		g.setColor(Color.black);
		g.setFont(f);
		g.drawString(text.getStringValue(), getX()+2, getY()+fontSize);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<Property> getProperties() {
		Vector<Property> ret = (Vector<Property>)props.clone();
		if (face.getStringValue().equals("plain"))
			ret.remove(face);
		if (style.getStringValue().equals("plain"))
			ret.remove(style);
		return ret;
	}
}
