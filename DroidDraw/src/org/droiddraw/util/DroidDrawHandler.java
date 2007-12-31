package org.droiddraw.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.droiddraw.AndroidEditor;
import org.droiddraw.widget.AbsoluteLayout;
import org.droiddraw.widget.AnalogClock;
import org.droiddraw.widget.AutoCompleteTextView;
import org.droiddraw.widget.Button;
import org.droiddraw.widget.CheckBox;
import org.droiddraw.widget.DigitalClock;
import org.droiddraw.widget.EditView;
import org.droiddraw.widget.FrameLayout;
import org.droiddraw.widget.ImageButton;
import org.droiddraw.widget.ImageView;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.LinearLayout;
import org.droiddraw.widget.ListView;
import org.droiddraw.widget.ProgressBar;
import org.droiddraw.widget.RadioButton;
import org.droiddraw.widget.RadioGroup;
import org.droiddraw.widget.RelativeLayout;
import org.droiddraw.widget.ScrollView;
import org.droiddraw.widget.Spinner;
import org.droiddraw.widget.TableLayout;
import org.droiddraw.widget.TableRow;
import org.droiddraw.widget.TextView;
import org.droiddraw.widget.Ticker;
import org.droiddraw.widget.TimePicker;
import org.droiddraw.widget.View;
import org.droiddraw.widget.Widget;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DroidDrawHandler extends DefaultHandler {
	Vector<String> all_props;

	Stack<Vector<String>> layout_props;
	Stack<Layout> layoutStack;
	
	public DroidDrawHandler() {
		all_props = new Vector<String>();
		all_props.add("android:layout_width");
		all_props.add("android:layout_height");
		all_props.add("android:background");
		all_props.add("android:padding");
		all_props.add("id");
		all_props.add("android:visibility");
		
		layout_props = new Stack<Vector<String> >();
		layoutStack = new Stack<Layout>();
	}
	
	protected boolean isLayout(String name) {
		return name.endsWith("Layout") || name.equals("RadioGroup") || name.equals("Ticker") || name.equals("TableRow") || name.equals("ScrollView");
	}
	
	@Override
	public void startElement(String uri, String lName, String qName, Attributes atts) 
		throws SAXException 
	{
		if (isLayout(qName)) {
			Layout l = null;
			Vector<String> l_props = new Vector<String>();
			if (qName.equals("AbsoluteLayout")) 
				l = new AbsoluteLayout();
			else if (qName.equals("LinearLayout") || (qName.equals("RadioGroup"))) {
				if (qName.equals("LinearLayout"))
					l = new LinearLayout();
				else if (qName.equals("RadioGroup")) {
					l = new RadioGroup();
					l.setPropertyByAttName("android:checkedButton", atts.getValue("android:checkedButton"));
				}
				if (atts.getValue("android:orientation") == null) {
					l.setPropertyByAttName("android:orientation", "horizontal");
				}
				else {
					l.setPropertyByAttName("android:orientation", atts.getValue("android:orientation"));
				}
				l_props.add("android:layout_gravity");
				l_props.add("android:layout_weight");
			}
			else if (qName.equals("RelativeLayout"))  {
				l = new RelativeLayout();
				for (int i=0;i<RelativeLayout.propNames.length;i++) {
					l_props.add(RelativeLayout.propNames[i]);
				}
			}
			else if (qName.equals("FrameLayout")) {
				l = new FrameLayout();
			}
			else if (qName.equals("TableLayout")) {
				l = new TableLayout();
				l.setPropertyByAttName("android:stretchColumns", atts.getValue("android:stretchColumns"));
			}
			else if (qName.equals("TableRow")) {
				l = new TableRow();
				l_props.add("android:layout_column");
			}
			else if (qName.equals("Ticker")) {
				l = new Ticker();
			}
			else if (qName.equals("ScrollView")) {
				l = new ScrollView();
			}
			if (layoutStack.size() == 0) {
				l.setPosition(AndroidEditor.OFFSET_X, AndroidEditor.OFFSET_Y);
				for (String prop : all_props) {
					if (atts.getValue(prop) != null) {
						l.setPropertyByAttName(prop, atts.getValue(prop));	
					}
				}
				l.apply();
				AndroidEditor.instance().setLayout(l);
			}
			else {
				addWidget(l, atts);
			}
			layoutStack.push(l);
			layout_props.push(l_props);
		}
		else if (layoutStack.size() == 0) {
			throw new SAXException("Error, no Layout!");
		}
		else {
			Widget w = null;
			if (qName.equals("Button")) {
				String txt = atts.getValue("android:text");
				Button b = new Button(txt);
				w = b;
			}
			else if (qName.equals("TextView")) {
				String txt = atts.getValue("android:text");
				w = new TextView(txt);
			}
			else if (qName.equals("EditText")) {
				String txt = atts.getValue("android:text");
				EditView et = new EditView(txt);
				for (int i=0;i<EditView.propertyNames.length;i++) {
					et.setPropertyByAttName(EditView.propertyNames[i], atts.getValue(EditView.propertyNames[i]));
				}
				w = et;
			}
			else if (qName.equals("CheckBox") || qName.equals("RadioButton")) {
				String txt = atts.getValue("android:text");
				if (qName.equals("CheckBox"))
					w = new CheckBox(txt);
				else if (qName.equals("RadioButton"))
					w = new RadioButton(txt);
				w.setPropertyByAttName("android:checked", atts.getValue("android:checked"));
			}
			else if (qName.equals("DigitalClock")) {
				w = new DigitalClock();
			}
			else if (qName.equals("AnalogClock")) {
				w = new AnalogClock();
			}
			else if (qName.equals("TimePicker")) {
				w = new TimePicker();
			}
			else if (qName.equals("ListView")) {
				w = new ListView();
			}
			else if (qName.equals("Spinner")) {
				w = new Spinner();
			}
			else if (qName.equals("AutoCompleteTextView")) {
				w = new AutoCompleteTextView("AutoComplete");
			}
			else if (qName.equals("ImageButton")) {
				w = new ImageButton();
			}
			else if (qName.equals("ImageView")) {
				w = new ImageView();
			}
			else if (qName.equals("ProgressBar")) {
				w = new ProgressBar();
				for (int i=0;i<ProgressBar.propertyNames.length;i++) {
					w.setPropertyByAttName(ProgressBar.propertyNames[i], 
							atts.getValue(ProgressBar.propertyNames[i]));
				}
			}
			else if (qName.equals("View")) {
				w = new View();
			}
			if (w != null) {
				addWidget(w, atts);
			}
		}
	}
	
	
	protected void addWidget(Widget w, Attributes atts) {
		if (w instanceof TextView) {
			for (int i=0;i<TextView.propertyNames.length;i++) {
				w.setPropertyByAttName(TextView.propertyNames[i], atts.getValue(TextView.propertyNames[i]));
			}
		}
		int x = DisplayMetrics.readSize(atts.getValue("android:layout_x"));
		int y = DisplayMetrics.readSize(atts.getValue("android:layout_y"));
	
		for (String prop : all_props) {
			if (atts.getValue(prop) != null) {
				w.setPropertyByAttName(prop, atts.getValue(prop));	
			}
		}
		for (String prop : layout_props.peek()) {
			if (atts.getValue(prop) != null) {
				w.setPropertyByAttName(prop, atts.getValue(prop));	
			}
		}
		Layout layout = layoutStack.peek();
		if (layout instanceof LinearLayout) {
			w.setPosition(-1,-1);
		}
		else
			w.setPosition(x, y);
		w.apply();
		layout.addWidget(w);
	}
	
	
	@Override
	public void endElement(String ns, String lName, String qName) 
	{
		if (isLayout(qName)) {
			layout_props.pop();
			layoutStack.pop();
		}
	}
	
	public static void loadFromString(String content) 
		throws SAXException, ParserConfigurationException, IOException
	{
		load(new InputSource(new StringReader(content)));
	}
	
	
	public static void load(File f) 
	throws SAXException, ParserConfigurationException, IOException, FileNotFoundException
	{
		load(new FileReader(f));
	}

	public static void load(Reader r) 
	throws SAXException, ParserConfigurationException, IOException
	{
		load(new InputSource(r));
	}
	
	public static void load(InputSource in) 
		throws SAXException, ParserConfigurationException, IOException
	{
		DroidDrawHandler ddh = new DroidDrawHandler();
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(in, ddh);
	}
}
