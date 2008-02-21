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
	public static final int START = 0;
	public static final int CENTER = 1;
	public static final int END = 2;

	int fontSize = 14;

	StringProperty text;
	StringProperty fontSz;
	SelectProperty face;
	SelectProperty style;
	SelectProperty align;
	ColorProperty textColor;

	int pad_x = 6;
	int pad_y = 4;

	PropertiesPanel p;
	Font f;
	BufferedImage bg;

	boolean osx;

	public static final String[] propertyNames = 
		new String[] {"android:textSize", "android:textStyle", "android:typeface", "android:textColor"};

	public TextView(String str) {
		super("TextView");

		text = new StringProperty("Text", "android:text", "");
		if (str != null) {
			text.setStringValue(str);
		}
		fontSz = new StringProperty("Font Size", "android:textSize", fontSize+"sp");
		face = new SelectProperty("Font Face", "android:typeface", new String[] {"normal","sans","serif","monospace"}, 0);
		style = new SelectProperty("Font Style", "android:textStyle", new String[] {"normal", "bold", "italic", "bold|italic"}, 0);
		textColor = new ColorProperty("Text Color", "android:textColor", null);
		align = new SelectProperty("Text Alignment", "android:textAlign", new String[] {"end","center","start"}, 2);
		props.add(text);
		props.add(fontSz);
		props.add(face);
		props.add(style);
		props.add(textColor);
		props.add(align);

		osx = (System.getProperty("os.name").toLowerCase().contains("mac os x"));
		buildFont();

		bg = new BufferedImage(1,1,BufferedImage.TYPE_BYTE_GRAY);
		apply();
	}

	protected void buildFont() {
		if (osx)
			f = new Font("Arial", Font.PLAIN, fontSize);
		else
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
		super.apply();
		if (fontSz.getStringValue() != null && fontSz.getStringValue().length() > 0) {
			fontSize = (DisplayMetrics.readSize(fontSz));
		}
		buildFont();
		this.readWidthHeight();
		this.baseline = fontSize+pad_y/2;
	}

	protected Vector<String> buildLineBreaks(String textVal) {
		Vector<String> res = new Vector<String>();
		String str = textVal;
		int ix;
		do {
			ix = str.indexOf('\n');
			String txt = str;
			if (ix != -1) { // && (ix1 == -1 || ix1 > ix2)) {
				txt = str.substring(0, ix);
				str = str.substring(ix+1);
			}
			int width = getWidth();
			if (width < 0) {
				res.add(txt);
				return res;
			}

			int l = stringLength(txt);
			while (l > width) {
				int bk = 1;
				while (stringLength(txt.substring(0,bk)) < width) bk++;
				bk--;
				if (bk == 0) {
					return res;
				}
				String sub = txt.substring(0, bk);
				res.add(sub);
				txt = txt.substring(bk);
				l = stringLength(txt);
			}
			res.add(txt);
		} while (ix != -1);
		return res;
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

	protected int getContentHeight() {
		Vector<String> texts = buildLineBreaks(text.getStringValue());
		if (texts.size() == 0) return fontSize+pad_y;
		int h = texts.size()*(fontSize+1)+pad_y;
		return h;
	}

	protected void drawText(Graphics g, int x, int h) {
		int aln = START;
		if (align.getStringValue().equals("end")) {
			aln = END;
		}
		else if (align.getStringValue().equals("center")) {
			aln = CENTER;
		}
		this.drawText(g, x, h, aln);
	}

	protected void drawText(Graphics g, int dx, int h, int align) {
		int x = 0;
		for (String s : buildLineBreaks(text.getStringValue())) {
			int l = stringLength(s);
			if (align == END) {
				x = getX()+getWidth()-l-pad_x/2+dx;
			}
			else if (align == CENTER) {
				x = getX()+getWidth()/2-l/2+dx;
			}	
			else {
				x = getX()+pad_x/2+dx;
			}	
			g.drawString(s, x, getY()+h);
			h += fontSize+1;
			if (h > getHeight())
				break;
		}
	}

	protected void setTextColor(Graphics g) {
		Color c = textColor.getColorValue();
		String theme = AndroidEditor.instance().getTheme();
		Color def = null;
		if (theme == null || theme.equals("default")) {
			def = Color.white;
		}
		else if (theme.equals("light")) {
			def = Color.black;
		}
		if (c == null)
			c = def;
		g.setColor(c);
	}
	
	public void paint(Graphics g) {
		drawBackground(g);

		if (text.getStringValue() != null) {
			setTextColor(g);
			g.setFont(f);

			int h = fontSize+pad_y/2;
			drawText(g, 0, h);
		}
	}

	public Vector<Property> getProperties() {
		return props;
	}
}
