package org.droiddraw;
import java.io.PrintWriter;
import java.util.Hashtable;


public abstract class AbstractWidget implements Widget {
	int x, y;
	int width, height;
	Hashtable<String,String> properties;
	String tagName;

	public AbstractWidget(String tagName) {
		this.tagName = tagName;
		this.properties = new Hashtable<String,String>();
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean clickedOn(int x, int y) {
		return (x > this.x && x < this.x+width && y > this.y && y < this.y+height);
	}

	public void move(int dx, int dy) {
		setPosition(this.x+dx, this.y+dy);
	}

	public void generate(PrintWriter pw) {
		printStartTag(pw);
		printTagBody(pw);
		pw.println("</"+tagName+">");
	}

	protected void printStartTag(PrintWriter pw) {
		pw.println("<"+tagName); 
		for (String key : properties.keySet()) {
			pw.println("\t"+key+"=\""+properties.get(key)+"\"");
		}
		pw.println(">");
	}
	
	protected void printTagBody(PrintWriter pw) {}
}
