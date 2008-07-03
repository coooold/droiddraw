package org.droiddraw.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.droiddraw.AndroidEditor;
import org.droiddraw.widget.Widget;

public class WidgetPanel extends JPanel implements DragGestureListener, DragSourceListener {
	private static final long serialVersionUID = 1L;
	Widget w;
	Dimension d;
	DragSource ds;
	BufferedImage img;
	int x, y;
	
	public WidgetPanel(Widget w) {
		this.w = w;
		d = new Dimension(w.getWidth(), w.getHeight());
		this.w.setPosition(0, 0);
		this.setToolTipText(w.getTagName());
	
		this.ds = DragSource.getDefaultDragSource();
		this.ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY, this);
	
		this.img = new BufferedImage(w.getWidth(), w.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		w.paint(g);	
	}
	
	@Override
  public Dimension getMinimumSize() { return d; }
	
	@Override
  public Dimension getPreferredSize() { return d; }
	
	@Override
  public void paint(Graphics g) {
		w.paint(g);
	}

	public void dragGestureRecognized(DragGestureEvent e) {
		try {
			Transferable t = new StringSelection(w.getTagName());
			ds.startDrag(e, DragSource.DefaultCopyDrop, img, new Point(x,y), t, this);
			//ds.startDrag(e, DragSource.DefaultCopyDrop, t, this);
		} catch (InvalidDnDOperationException ex) {
			AndroidEditor.instance().error(ex);
		}
	}
	
	public void dragDropEnd(DragSourceDropEvent e) {}
	public void dragEnter(DragSourceDragEvent e) {}
	public void dragExit(DragSourceEvent e) {}
	public void dragOver(DragSourceDragEvent e) {}
	public void dropActionChanged(DragSourceDragEvent arg0) {}
}
