package org.droiddraw;

import java.io.PrintWriter;
import java.util.Vector;

public abstract class AbstractLayout implements Layout {
	protected Vector<Widget> widgets;
	protected String tagName;
	
	public AbstractLayout(String tagName) {
		this.widgets = new Vector<Widget>();
		this.tagName = tagName;
	}
	
	public void addWidget(Widget w) {
		widgets.add(w);
		positionWidget(w);
	}
	
	public Vector<Widget> getWidgets() {
		return widgets;
	}

	public void removeWidget(Widget w) {
		widgets.remove(w);
		repositionAllWidgets(widgets);
	}

	protected void printStartTag(java.util.Hashtable<String,String> atts, PrintWriter pw) 
	{
		pw.println("<"+tagName);
		for (String key : atts.keySet()) {
			pw.println(key+"=\""+atts.get(key)+"\"");
		}
		pw.println(">");
	}

	public void printEndTag(PrintWriter pw) {
		pw.println("</"+tagName+">");
	}
	
	public abstract void positionWidget(Widget w);
	protected abstract void repositionAllWidgets(Vector<Widget> widgets);
}
