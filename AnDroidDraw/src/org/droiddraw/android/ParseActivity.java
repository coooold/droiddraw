package org.droiddraw.android;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ParseActivity extends Activity {
	public static final String DATA = "data";
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		this.setTitle("AnDroidDraw: Preview");
		String data = getIntent().getStringExtra(DATA);
		
		ViewInflater inflater = new ViewInflater(this);
		
		XmlPullParser parse;		
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			parse = factory.newPullParser();
			
			parse.setInput(new StringReader(data));
			
			View v = inflater.inflate(parse);
			setContentView(v);
		}
		catch (XmlPullParserException ex) { error(ex); }
		catch (IOException ex) { error(ex); }		
	}
	
	protected void error(Exception ex) {
		ex.printStackTrace();
	}
}
