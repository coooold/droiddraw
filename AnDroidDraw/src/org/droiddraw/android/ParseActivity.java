package org.droiddraw.android;

import java.io.IOException;
import java.io.StringReader;
import java.util.Hashtable;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AnalogClock;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DigitalClock;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ParseActivity extends Activity {
	Stack<ViewGroup> layoutStack;
	Hashtable<String, Integer> ids;
	int idg;
	
	public static final String DATA = "data";
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setTitle("AnDroidDraw: Preview");
		
		layoutStack = new Stack<ViewGroup>();
		ids = new Hashtable<String,Integer>();
		
		String data = getIntent().getStringExtra(DATA);
		
		XmlPullParser parse;
		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			parse = factory.newPullParser();
			
			//URL u = new URL("http://www.droiddraw.org/test.xml");
			//InputStream is = u.openStream();
			//parse.setInput(new InputStreamReader(is));
			
			parse.setInput(new StringReader(data));
			
			View v = inflate(parse);
			setContentView(v);
		}
		catch (XmlPullParserException ex) { error(ex); }
		catch (IOException ex) { error(ex); }
		//catch (MalformedURLException ex) { error(ex); }
		
	}
	
	protected void error(Exception ex) {
		ex.printStackTrace();
	}
	
	protected View inflate(XmlPullParser parse) 
	throws XmlPullParserException, IOException
	{
		layoutStack.clear();
		ids.clear();
		
		Stack<StringBuffer> data = new Stack<StringBuffer>();
		int evt = parse.getEventType();
		View root = null;
		while (evt != XmlPullParser.END_DOCUMENT) {
			switch (evt) {
			case XmlPullParser.START_DOCUMENT:
				data.clear();
				break;
			case XmlPullParser.START_TAG:
				data.push(new StringBuffer());
				View v = createView(parse);
				if (v == null)
					continue;
				if (root == null) {
					root = v;
				}
				else {
					layoutStack.peek().addView(v);
				}
				if (v instanceof ViewGroup) {
					layoutStack.push((ViewGroup)v);
				}
				break;
			case XmlPullParser.TEXT:
				data.peek().append(parse.getText());
				break;
			case XmlPullParser.END_TAG:
				data.pop();
				if (isLayout(parse.getName()))
					layoutStack.pop();
				
			}
			evt = parse.next();
		}
		return root;
	}
	
	protected boolean isLayout(String name) {
		return name.endsWith("Layout") || name.equals("RadioGroup") || name.equals("TableRow") || name.equals("ScrollView");
	}
	
	protected View createView(XmlPullParser parse) {
		String name = parse.getName();
		View result = null;
		AttributeSet atts = Xml.asAttributeSet(parse);
		if (name.equals("LinearLayout")) {
			result = new LinearLayout(this);
		}
		else if (name.equals("RadioGroup")) {
			result = new RadioGroup(this);
		}
		else if (name.equals("TableRow")) {
			result = new TableRow(this);
		}
		else if (name.equals("TableLayout")) {
			result = new TableLayout(this);
		}
		else if (name.equals("AbsoluteLayout")) {
			result = new AbsoluteLayout(this);
		}
		else if (name.equals("RelativeLayout")) {
			result = new RelativeLayout(this);
		}
		else if (name.equals("ScrollView")) {
			result = new ScrollView(this);
		}
		else if (name.equals("FrameLayout")) {
			result = new FrameLayout(this);
		}
		else if (name.equals("TextView")) {
			result = new TextView(this);
		}
		else if (name.equals("AutoCompleteTextView")) {
			result = new AutoCompleteTextView(this);
		}
		else if (name.equals("AnalogClock")) {
			result = new AnalogClock(this);
		}
		else if (name.equals("Button")) {
			result = new Button(this);
		}
		else if (name.equals("CheckBox")) {
			result = new CheckBox(this);
		}
		//else if (name.equals("DatePicker")) {
		//	result = new DatePicker(this);
		//}
		else if (name.equals("DigitalClock")) {
			result = new DigitalClock(this);
		}
		else if (name.equals("EditText")) {
			result = new EditText(this);
		}
		else if (name.equals("ProgressBar")) {
			result = new ProgressBar(this);
		}
		else if (name.equals("RadioButton")) {
			result = new RadioButton(this);
		}
		else {
			Toast.makeText(this, "Unhandled tag:"+name,
					Toast.LENGTH_SHORT).show();	
		}
		
		if (result == null)
			return null;
		
		String id = findAttribute(atts, "id");

		if (id != null) {
			int idNumber = lookupId(id);
			if (idNumber > -1) {
				result.setId(idNumber);
			}
		}
		
		if (result instanceof TextView) {
			TextView tv = (TextView)result;
			String text = findAttribute(atts, "android:text");
			if (text != null) {
				text = text.replace("\\n", "\n");
				tv.setText(text);
			}
		}
		
		if (result instanceof CompoundButton) {
			CompoundButton cb = (CompoundButton)result;
			String checked = findAttribute(atts, "android:checked");
			cb.setChecked("true".equals(checked));
		}
		
		if (result instanceof ProgressBar) {
			ProgressBar pb = (ProgressBar)result;
			String indet = findAttribute(atts, "android:indeterminate");
			if (indet != null) {
				pb.setIndeterminate("true".equals(indet));
			}
			/*String orientation = findAttribute(atts, "android:orientation");
			if (orientation != null) {
				if ("horizontal".equals(orientation)) {
					pb.setOrientation(ProgressBar.HORIZONTAL);
				}
				else if ("vertical".equals(orientation)) {
					pb.setOrientation(ProgressBar.VERTICAL);
				}
			}*/
			String max = findAttribute(atts, "android:max");
			if (max != null) {
				pb.setMax(Integer.parseInt(max));
			}
		}
		
		if (result instanceof LinearLayout) {
			LinearLayout ll = (LinearLayout)result;
			String orient = findAttribute(atts, "android:orientation");
			if (orient != null) {
				if (orient.equals("horizontal"))
					ll.setOrientation(LinearLayout.HORIZONTAL);
				else if (orient.equals("vertical"))
					ll.setOrientation(LinearLayout.VERTICAL);
			}
		}
		
		if (result instanceof RadioGroup) {
			RadioGroup rg = (RadioGroup)result;
			String cid = findAttribute(atts, "android:checkedButton");
			if (cid != null) {
				rg.check(Integer.parseInt(cid));
			}
		}
		
		if (layoutStack.size() > 0) {
			result.setLayoutParams(loadLayoutParams(atts, layoutStack.peek()));
		}
		return result;
	}
	
	protected int lookupId(String id) {
		int ix = id.indexOf("/");
		if (ix != -1) {
			String idName = id.substring(ix+1);
			Integer n = ids.get(idName);
			if (n == null && id.startsWith("@+")) {
				n = new Integer(idg++);
				ids.put(idName, n);
			}
			if (n != null)
				return n.intValue();
		}
		return -1;
	}
	
	protected String findAttribute(AttributeSet atts, String id) {
		for (int i=0;i<atts.getAttributeCount();i++) {
			if (atts.getAttributeName(i).equals(id)) {
				return atts.getAttributeValue(i);
			}
		}
		int ix = id.indexOf(":");
		if (ix != -1) {
			return atts.getAttributeValue("http://schemas.android.com/apk/res/android", id.substring(ix+1));
		}
		else {
			return null;
		}
	}
	
	protected ViewGroup.LayoutParams loadLayoutParams(AttributeSet atts, ViewGroup vg) {
		ViewGroup.LayoutParams lps = null;
		
		String width = findAttribute(atts, "android:layout_width");
		String height = findAttribute(atts, "android:layout_height");
		int w, h;
		w = readSize(width);
		h = readSize(height);
		
		if (vg instanceof RadioGroup) {
			lps = new RadioGroup.LayoutParams(w, h);
		}
		else if (vg instanceof TableRow) {
			lps = new TableRow.LayoutParams();
		}
		else if (vg instanceof TableLayout) {
			lps = new TableLayout.LayoutParams();
		}
		else if (vg instanceof LinearLayout) {
			lps = new LinearLayout.LayoutParams(w,h);
		}
		else if (vg instanceof AbsoluteLayout) {
			String x = findAttribute(atts, "android:layout_x");
			String y = findAttribute(atts, "android:layout_y");
			
			lps = new AbsoluteLayout.LayoutParams(w, h, readSize(x), readSize(y));
		}
		else if (vg instanceof RelativeLayout) {
			lps = new RelativeLayout.LayoutParams(w,h);
		}
		else if (vg instanceof ScrollView) {
			lps = new ScrollView.LayoutParams(w,h);
		}
		else if (vg instanceof FrameLayout) {
			lps = new FrameLayout.LayoutParams(w,h);
		}
		
		if (lps instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams l = (LinearLayout.LayoutParams)lps;
			String gravity = findAttribute(atts, "android:layout_gravity");
			if (gravity != null) {
				l.gravity = Integer.parseInt(gravity);
			}
			
			String weight = findAttribute(atts, "android:layout_weight");
			if (weight != null) {
				l.weight = Float.parseFloat(weight);
			}
			lps = l;
		}
		
		if (lps instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams)lps;
			for (int i=0;i<relative_strings.length;i++) {
				String id  = findAttribute(atts, relative_strings[i]);
				if (id != null) {
					int idN = lookupId(id);
					l.addRule(relative_verbs[i], idN);
				}
			}
			// Margin handling
			// Contributed by Vishal Choudhary - Thanks!
			String bottom = findAttribute(atts, "android:layout_marginBottom");
	        String left = findAttribute(atts, "android:layout_marginLeft");
	        String right = findAttribute(atts, "android:layout_marginRight");
	        String top = findAttribute(atts, "android:layout_marginTop");
	        int bottomInt=0, leftInt=0, rightInt=0, topInt=0;
	        if (bottom != null)
	            bottomInt = readSize(bottom);
	        if (left != null)
	            leftInt = readSize(left);
	        if (right != null)
	            rightInt = readSize(right);
	        if (top != null)
	            topInt = readSize(top);
		    
		    l.setMargins(leftInt, topInt, rightInt, bottomInt);
		}
		
		return lps;
	}
	
	protected int readSize(String val) {
		if ("wrap_content".equals(val)) {
			return ViewGroup.LayoutParams.WRAP_CONTENT;
		}
		else if ("fill_parent".equals(val)) {
			return ViewGroup.LayoutParams.FILL_PARENT;
		}
		else if (val != null) {
			int ix = val.indexOf("px");
			if (ix != -1)
				return Integer.parseInt(val.substring(0, ix));
			else
				return 0;
		}
		else {
			return ViewGroup.LayoutParams.WRAP_CONTENT;
		}
	}
	
	String[] relative_strings = new String[]
	                            	       {"android:layout_above", 
	                            			"android:layout_alignBaseline", 
	                            			"android:layout_alignBottom", 
	                            			"android:layout_alignLeft",
	                            			"android:layout_alignParentBottom",
	                            			"android:layout_alignParentLeft",
	                            			"android:layout_alignParentRight",
	                            			"android:layout_alignParentTop",
	                            			"android:layout_alignRight", 
	                            			"android:layout_alignTop", 
	                            			"android:layout_below",
	                            			"android:layout_centerHorizontal",
	                            			"android:layout_centerInParent",
	                            			"android:layout_centerVertical",
	                            			"android:layout_toLeft",
	                            			"android:layout_toRight"};
	                            	
	                            	int[] relative_verbs = new int[]
	                            	       {RelativeLayout.ABOVE,
	                            			RelativeLayout.ALIGN_BASELINE,
	                            			RelativeLayout.ALIGN_BOTTOM,
	                            			RelativeLayout.ALIGN_LEFT,
	                            			RelativeLayout.ALIGN_PARENT_BOTTOM,
	                            			RelativeLayout.ALIGN_PARENT_LEFT,
	                            			RelativeLayout.ALIGN_PARENT_RIGHT,
	                            			RelativeLayout.ALIGN_PARENT_TOP,
	                            			RelativeLayout.ALIGN_RIGHT,
	                            			RelativeLayout.ALIGN_TOP,
	                            			RelativeLayout.BELOW,
	                            			RelativeLayout.CENTER_HORIZONTAL,
	                            			RelativeLayout.CENTER_IN_PARENT,
	                            			RelativeLayout.CENTER_VERTICAL,
	                            			RelativeLayout.LEFT_OF,
	                            			RelativeLayout.RIGHT_OF,
	                            	      };
}
