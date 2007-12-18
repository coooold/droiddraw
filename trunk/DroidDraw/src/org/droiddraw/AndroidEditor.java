package org.droiddraw;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;


public class AndroidEditor {
	public static enum ScreenMode {QVGA_LANDSCAPE, QVGA_PORTRAIT, HVGA_LANDSCAPE, HVGA_PORTRAIT};

	Layout layout;
	Widget selected;
	Viewer viewer;
	ScreenMode screen;
	int sx, sy;
	PropertiesPanel pp;
	Hashtable<String, String> strings;
	Hashtable<String, Color> colors;
	
	public static int OFFSET_X = 0;
	public static int OFFSET_Y = 48;
	
	private static AndroidEditor inst;
	
	private AndroidEditor() {
		this(ScreenMode.QVGA_LANDSCAPE);
	}
	
	private AndroidEditor(ScreenMode mode) {
		setScreenMode(mode);
		this.pp = new PropertiesPanel();
		this.colors = new Hashtable<String, Color>();
		colors.put("black", Color.black);
		colors.put("darkgray", Color.darkGray);
		colors.put("gray", Color.gray);
		colors.put("lightgray", Color.lightGray);
		colors.put("red", Color.red);
		colors.put("green", Color.green);
		colors.put("blue",Color.blue);
		colors.put("yellow", Color.yellow);
		colors.put("cyan", Color.cyan);
		colors.put("magenta", Color.magenta);
		colors.put("white", Color.white);
	}

	public PropertiesPanel getPropertiesPanel() {
		return pp;
	}
	
	public Hashtable<String, String> getStrings() {
		return strings;
	}

	public void setStrings(Hashtable<String, String> strings) {
		this.strings = strings;
	}

	public Hashtable<String, Color> getColors() {
		return colors;
	}

	public void setColors(Hashtable<String, Color> colors) {
		for (String key : colors.keySet()) {
			colors.put(key, colors.get(key));
		}
	}

	public static AndroidEditor instance() {
		if (inst == null)
			inst = new AndroidEditor();
		return inst;
	}
	
	public ScreenMode getScreenMode() {
		return screen;
	}
	
	public void setScreenMode(ScreenMode mode) {
		this.screen = mode;
		if (screen == ScreenMode.QVGA_LANDSCAPE) {
			sx = 320;
			sy = 240;
		}
		else if (screen == ScreenMode.QVGA_PORTRAIT) {
			sx = 240;
			sy = 320;
		}
		else if (screen == ScreenMode.HVGA_LANDSCAPE) {
			sx = 480;
			sy = 320;
		}
		else if (screen == ScreenMode.HVGA_PORTRAIT) {
			sx = 320;
			sy = 480;
		}
		if (this.getLayout() != null) {
			this.getLayout().apply();
			for (Widget w : this.getLayout().getWidgets()) {
				w.apply();
			}
			this.getLayout().repositionAllWidgets();
		}
	}
	
	public int getScreenX() {
		return sx;
	}
	
	public int getScreenY() {
		return sy;
	}
	
	public void setViewer(Viewer v) {
		this.viewer = v;
		this.pp.setViewer(v);
	}
	
	public void setIdsFromLabels() {
		setIdsFromLabels(layout);
	}
	
	public void setIdsFromLabels(Layout l) {
		for (Widget w : l.getWidgets()) {
			if (w instanceof Layout) {
				setIdsFromLabels((Layout)w);
			}
			else {
				Property p = w.getPropertyByAttName("android:text");
				if (p != null) {
					((AbstractWidget)w).setId("@+id/"+((StringProperty)p).getStringValue());
				}
			}
		}
	}
	
	public void setLayout(Layout l) {
		if (this.layout != null) {
			Vector<Widget> widgets = layout.getWidgets();
			for (Widget w : widgets) {
				l.addWidget(w);
			}
			this.layout.removeAllWidgets();
		}
		this.layout = l;
		if (selected == null) {
			pp.setProperties(l.getProperties(), l);
		}
	}
	
	public Layout getLayout() {
		return layout;
	}
	
	public Widget getSelected() {
		return selected;
	}

	public void select(Widget w) {
		if (w == layout) {
			selected = null;
			pp.setProperties(layout.getProperties(), w);
		}
		else {
			selected = w;
		}
		if (w != null) {
			pp.setProperties(w.getProperties(), w);
		}
		pp.validate();
		pp.repaint();
	}

	public void removeWidget(Widget w) {
		w.getParent().removeWidget(w);
		if (selected == w) {
			selected = null;
		}
	}
	
	public void removeAllWidgets() {
		layout.removeAllWidgets();
		selected = null;
	}
	
	public Layout findLayout(int x, int y) {
		return findLayout(layout, x, y);
	}
	
	protected Layout findLayout(Layout l, int x, int y) {
		for (Widget w : l.getWidgets()) {
			if (w.clickedOn(x, y) && w instanceof Layout) {
				return findLayout((Layout)w, x, y);
			}
		}
		return l;
	}
	
	public Widget findWidget(int x, int y) {
		return findWidget(layout, x, y);
	}
	
	public Widget findWidget(Layout l, int x, int y) {
		for (Widget w : l.getWidgets()) {
			if (w.clickedOn(x, y)) {
				if (w instanceof Layout) {
					return findWidget((Layout)w, x, y);
				}
				return w;
			}
		}
		return l;
	}
	
	public void selectWidget(int x, int y) {
		Widget res = findWidget(x, y);
		if (res == selected) {
			selected = null;
		}
		else {
			this.select(res);
		}
	}

	public void generate(PrintWriter pw) {
		pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		generateWidget(layout, pw);
		pw.flush();
	}


	@SuppressWarnings("unchecked")
	protected void generateWidget(Widget w, PrintWriter pw) {
		pw.println("<"+w.getTagName());
		Vector<Property> props = (Vector<Property>)w.getProperties().clone();
		if (w != layout)
			((Layout)w.getParent()).addOutputProperties(w, props);
		for (Property prop : props) {
			if (prop.getValue() != null && prop.getValue().toString().length() > 0) {
				pw.println(prop.getAtttributeName()+"=\""+prop.getValue()+"\"");
			}
		}
		pw.println(">");
		if (w instanceof Layout) {
			for (Widget wt : ((Layout)w).getWidgets()) {
				generateWidget(wt, pw);
			}
		}
		pw.println("</"+w.getTagName()+">");
	}
}
