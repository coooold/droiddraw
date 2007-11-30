package org.droiddraw;
import java.awt.Graphics;
import java.io.PrintWriter;

import javax.swing.JPanel;

public interface Widget {
	public boolean clickedOn(int x, int y);
	public int getX();
	public int getY();
	public int getWidth();
	public int getHeight();
	public void setPosition(int x, int y);
	public void move(int dx, int dy);
	public void paint(Graphics g);
	public void generate(PrintWriter pw);
	public JPanel getEditorPanel();
	public void apply();
}