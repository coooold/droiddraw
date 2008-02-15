package org.droiddraw.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.swing.JPanel;

import org.droiddraw.AndroidEditor;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

public class Viewer extends JPanel implements DropTargetListener {
	private static final long serialVersionUID = 1L;

	Dimension d;
	AndroidEditor app;
	ViewerListener vl;
	Image img;
	DropTarget dt;

	
	
	public Viewer(AndroidEditor app, DroidDrawPanel ddp, Image img) {
		this.app = app;
		//this.d = new Dimension(app.getScreenX(),app.getScreenY());
		vl = new ViewerListener(app, ddp, this);
		addMouseListener(vl);
		addMouseMotionListener(vl);
		addKeyListener(vl);
		this.img = img;
		this.d = new Dimension(480,480);
		
		dt = new DropTarget(this, DnDConstants.ACTION_MOVE, this, true );
	}
	
	public void resetScreen(Image img) {
		//this.d = new Dimension(app.getScreenX(),app.getScreenY());
		this.img = img;
	}

	ViewerListener getListener() {
		return vl;
	}

	public Dimension getPreferredSize() {
		return d;
	}

	public Dimension getMinimumSize() {
		return d;
	}

	public int getOffX() {
		return (int)(getWidth()-app.getScreenX())/2;
	}

	public int getOffY() {
		return 0;
	}

	public void paint(Graphics g, Layout l) 
	{
		l.clearRendering();
		l.resizeForRendering();
		WidgetPainter wp = WidgetRegistry.getPainter(l.getClass());
		if (wp != null) {
			wp.paint(l, g);
		}
		else {
			l.paint(g);
		}
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		Graphics2D g2d = (Graphics2D)g;
		int dx = getOffX();
		int dy = getOffY();
		g2d.transform(AffineTransform.getTranslateInstance(dx, dy));

		g.setColor(Color.white);
		g.fillRect(0,0, app.getScreenX(), app.getScreenY());
		if (img != null)
			g.drawImage(img, 0, 0, this);
		
		paint(g, app.getLayout());
		
		Widget w = app.getSelected();

		if (w != null) {
			int off_x = 0;
			int off_y = 0;
			if (w.getParent() != null) {
				off_x = w.getParent().getScreenX();
				off_y = w.getParent().getScreenY();
			}
		
			g.setColor(Color.black);
			g.drawRect(w.getX()+off_x, w.getY()+off_y, w.getWidth(), w.getHeight());
		}
	}

	public void dragEnter(DropTargetDragEvent arg0) {
	}

	public void dragExit(DropTargetEvent arg0) {
	}

	public void dragOver(DropTargetDragEvent arg0) {
	}

	public void drop(DropTargetDropEvent e) {
		e.acceptDrop(DnDConstants.ACTION_COPY);
		Transferable t = e.getTransferable();
		try {
			Object data = t.getTransferData(t.getTransferDataFlavors()[0]);
			Point l = e.getLocation();
			vl.addWidget(ViewerListener.createWidget((String)data), l.x-getOffX(), l.y-getOffY());
			e.dropComplete(true);
		} catch (IOException ex) {
			AndroidEditor.instance().error(ex);
		} catch (UnsupportedFlavorException ex) {
			AndroidEditor.instance().error(ex);
		}
	}

	public void dropActionChanged(DropTargetDragEvent arg0) {
	}
}