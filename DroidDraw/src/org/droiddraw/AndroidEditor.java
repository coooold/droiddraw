package org.droiddraw;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.droiddraw.gui.PropertiesPanel;
import org.droiddraw.gui.Viewer;
import org.droiddraw.property.Property;
import org.droiddraw.property.StringProperty;
import org.droiddraw.util.ArrayHandler;
import org.droiddraw.util.ColorHandler;
import org.droiddraw.util.StringHandler;
import org.droiddraw.widget.AbstractWidget;
import org.droiddraw.widget.CheckBox;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;
import org.xml.sax.SAXException;


public class AndroidEditor {
	public static enum ScreenMode {QVGA_LANDSCAPE, QVGA_PORTRAIT, HVGA_LANDSCAPE, HVGA_PORTRAIT};

	Layout layout;
	Widget selected;
	Viewer viewer;
	ScreenMode screen;
	int sx, sy;
	PropertiesPanel pp;
	File stringFile = null;
	Hashtable<String, String> strings;
	File colorFile = null;
	Hashtable<String, Color> colors;
	File arrayFile = null;
	Hashtable<String, Vector<String>> arrays;
	
	File drawable_dir;
	URLOpener opener;
	
	String theme;
	
	
	public static int OFFSET_X = 0;
	public static int OFFSET_Y = 48;
	
	private static AndroidEditor inst;
	
	private AndroidEditor() {
		this(ScreenMode.HVGA_PORTRAIT);
	}
	
	private AndroidEditor(ScreenMode mode) {
		setScreenMode(mode);
		this.pp = new PropertiesPanel();
		this.colors = new Hashtable<String, Color>();
		this.strings = new Hashtable<String, String>();
		this.arrays = new Hashtable<String, Vector<String>>();
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

	public void setDrawableDirectory(File dir) {
		this.drawable_dir = dir;
	}
	
	public File getDrawableDirectory() {
		return this.drawable_dir;
	}
	
	public BufferedImage findDrawable(String src) {
		if (this.getDrawableDirectory() == null) {
			return null;
		}
		int ix = src.indexOf("/");
		String file = src.substring(ix+1);
		System.out.println("Looking for: "+file);
		File f = new File(this.getDrawableDirectory(), file+".png"); 
		if (!f.exists()) {
			try {
				System.out.println(f.getCanonicalPath()+" doesn't exist!");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			f = new File(this.getDrawableDirectory(), file+".bmp");
		}
		if (!f.exists()) {
			f = new File(this.getDrawableDirectory(), file+".jpg");
		}
		if (!f.exists()) {
			return null;
		}
		try {
			System.out.println("Reading in: "+f.getCanonicalPath());
			return ImageIO.read(f);
		}
		catch (IOException ex) {
			error(ex);
		}
		return null;
	}
	
	public Hashtable<String, String> getStrings() {
		return strings;
	}

	public void setStrings(Hashtable<String, String> strings) {
		this.strings = strings;
	}

	public File getStringFile() {
		return stringFile;
	}
	
	public void setStrings(File f) {
		try { 
			setStrings(StringHandler.load(new FileInputStream(f)));
			stringFile = f;
		}
		catch (IOException ex) {
			error(ex);
		}
		catch (SAXException ex) {
			error(ex);
		}
		catch (ParserConfigurationException ex) {
			error(ex);
		}
	}
	
	public File getColorFile() {
		return colorFile;
	}
	
	public void setColors(File f) {
		try { 
			setColors(ColorHandler.load(new FileInputStream(f)));
			colorFile = f;
		}
		catch (IOException ex) {
			error(ex);
		}
		catch (SAXException ex) {
			error(ex);
		}
		catch (ParserConfigurationException ex) {
			error(ex);
		}
	}
	
	
	public Hashtable<String, Color> getColors() {
		return colors;
	}

	public void error(String message) {
		JOptionPane.showMessageDialog(viewer, message, "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	public void error(Exception ex) {
		error(ex.getMessage());
		ex.printStackTrace();
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
		setLayout(l, true);
	}
	
	public void setLayout(Layout l, boolean fill) {
		if (fill) {
			l.setPropertyByAttName("android:layout_width", "fill_parent");
			l.setPropertyByAttName("android:layout_height", "fill_parent");
		}
		
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
		if (w != null) {
			w.getParent().removeWidget(w);
			if (selected == w) {
				selected = null;
			}
		}
	}
	
	public void removeAllWidgets() {
		layout.removeAllWidgets();
		selected = null;
	}
	
	public Vector<Layout> findLayouts(int x, int y) {
		return findLayout(layout, x, y);
	}
	
	protected Vector<Layout> findLayout(Layout l, int x, int y) {
		Vector<Layout> res = new Vector<Layout>();
		if (l.clickedOn(x, y)) {
			for (Widget w : l.getWidgets()) {
				if (w instanceof Layout) {
					Vector<Layout> tmp = findLayout((Layout)w, x, y);
					for (Layout lt : tmp) {
						res.add(lt);
					}
				}
			}
			res.add(l);
		}
		return res;
	}
	
	public Vector<Widget> findWidgets(int x, int y) {
		return findWidgets(layout, x, y);
	}
	
	public Vector<Widget> findWidgets(Layout l, int x, int y) {
		Vector<Widget> res = new Vector<Widget>();
		for (Widget w : l.getWidgets()) {
			if (w.clickedOn(x, y)) {
				if (w instanceof Layout) {
					Vector<Widget> tmp = findWidgets((Layout)w, x, y);
					for (Widget wt : tmp) 
						res.add(wt);
				}
				res.add(w);
			}
		}
		return res;
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
			if (prop.getValue() != null && prop.getValue().toString().length() > 0 && !prop.isDefault()) {
				// Work around an android bug... *sigh*
				if (w instanceof CheckBox && prop.getAtttributeName().equals("android:padding"))
					continue;
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
	
	public void setURLOpener(URLOpener open) {
		this.opener = open;
	}

	public URLOpener getURLOpener() {
		return opener;
	}

	public File getArrayFile() {
		return arrayFile;
	}

	public void setArrayFile(File arrayFile) {
		this.arrayFile = arrayFile;
	}

	public Hashtable<String, Vector<String>> getArrays() {
		return arrays;
	}

	public void setArrays(Hashtable<String, Vector<String>> arrays) {
		this.arrays = arrays;
	}
	
	public void setArrays(File f) {
		setArrayFile(f);
		try {
			setArrays(ArrayHandler.load(new FileInputStream(f)));
		}
		catch (IOException ex) {
			error(ex);
		}
		catch (SAXException ex) {
			error(ex);
		}
		catch (ParserConfigurationException ex) {
			error(ex);
		}
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}
}
