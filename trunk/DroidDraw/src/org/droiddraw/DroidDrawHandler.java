package org.droiddraw;

import java.io.IOException;
import java.io.StringReader;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DroidDrawHandler extends DefaultHandler {
	boolean layout = false;
	Vector<String> all_props;
	
	public DroidDrawHandler() {
		all_props = new Vector<String>();
		all_props.add("android:layout_width");
		all_props.add("android:layout_height");
		all_props.add("id");
	}
	
	@Override
	public void startElement(String uri, String lName, String qName, Attributes atts) 
		throws SAXException 
	{
		if (qName.equals("AbsoluteLayout")) {
			AndroidEditor.instance().setLayout(new AbsoluteLayout());
			layout = true;
		}
		else if (qName.equals("LinearLayout")) {
			AndroidEditor.instance().setLayout(new LinearLayout());
			layout = true;
		}
		else if (qName.equals("RelativeLayout")) {
			AndroidEditor.instance().setLayout(new RelativeLayout());
			layout = true;
			all_props.add("android:layout_alignRight");
			all_props.add("android:layout_alignLeft");
			all_props.add("android:layout_alignTop");
			all_props.add("android:layout_alignBottom");
			all_props.add("android:layout_toRight");
			all_props.add("android:layout_toLeft");
			all_props.add("android:layout_above");
			all_props.add("android:layout_below");
		}
		else if (!layout) {
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
			else if (qName.equals("CheckBox")) {
				String txt = atts.getValue("android:text");
				w = new CheckBox(txt);
			}
			if (w != null) {
				if (w instanceof TextView) {
					for (int i=0;i<TextView.propertyNames.length;i++) {
						w.setPropertyByAttName(TextView.propertyNames[i], atts.getValue(TextView.propertyNames[i]));
					}
				}
				int x = readLength(atts.getValue("android:layout_x"));
				int y = readLength(atts.getValue("android:layout_y"));
			
				for (String prop : all_props) {
					if (atts.getValue(prop) != null) {
						w.setPropertyByAttName(prop, atts.getValue(prop));	
					}
				}
				if (AndroidEditor.instance().getLayout() instanceof LinearLayout)
					w.setPosition(x+AndroidEditor.OFFSET_X, y+AndroidEditor.OFFSET_Y+AndroidEditor.instance().getScreenY());
				else
					w.setPosition(x+AndroidEditor.OFFSET_X, y+AndroidEditor.OFFSET_Y);
				w.apply();
				AndroidEditor.instance().addWidget(w);
			}
		}
	}
	
	protected static int readLength(String s) {
		if (s != null && s.endsWith("px")) {
			return Integer.parseInt(s.substring(0, s.length()-2));
		}
		return 0;
	}
	
	public static void loadFromString(String content) 
		throws SAXException, ParserConfigurationException, IOException
	{
		load(new InputSource(new StringReader(content)));
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
