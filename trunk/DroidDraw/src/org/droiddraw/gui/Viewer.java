package org.droiddraw.gui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import java.text.DateFormat;
import java.util.Date;

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
	Image back;
	
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
		String theme = AndroidEditor.instance().getTheme();
		if (theme == null || theme.equals("default")) {
			Image back = null;
			Image stat = null;
			int wx = AndroidEditor.instance().getScreenX();
			
			if (AndroidEditor.instance().getScreenMode().equals(AndroidEditor.ScreenMode.HVGA_PORTRAIT)) {
				back = ImageResources.instance().getImage("background_01p");
				stat = ImageResources.instance().getImage("statusbar_background_p");
			}
			else if (AndroidEditor.instance().getScreenMode().equals(AndroidEditor.ScreenMode.HVGA_LANDSCAPE)) {
				back = ImageResources.instance().getImage("background_01l");
				stat = ImageResources.instance().getImage("statusbar_background_l");	
			}
			if (back != null)
				g.drawImage(back, 0, 0, this);
			if (stat != null)
				g.drawImage(stat, 0, 0, this);
			
			Image dat = ImageResources.instance().getImage("stat_sys_data_connected");
			g.drawImage(dat, wx-160, 0, this);
			
			Image sig = ImageResources.instance().getImage("stat_sys_signal_3");
			g.drawImage(sig, wx-130, 2, this);
			
			Image bat = ImageResources.instance().getImage("stat_sys_battery_charge_100");
			g.drawImage(bat, wx-100, 4, this);
			
			Font f = g.getFont();
			Font f2 = f.deriveFont(Font.BOLD);
			f2 = f2.deriveFont(14f);
			g.setFont(f2);
			g.setColor(Color.black);
			g.drawString(DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()), wx-65, 17);
			
			Image title = ImageResources.instance().getImage("title_bar.9");
			NineWayImage nwt = new NineWayImage(title, 0, 0);
			nwt.paint(g, 0, stat.getHeight(null), wx, stat.getHeight(null));
			
			g.setColor(Color.lightGray);
			g.drawString("DroidDraw", 5, stat.getHeight(null)+17);
			g.setFont(f);
			
		}
		else {
			if (img != null)
				g.drawImage(img, 0, 0, this);
		}
		
		
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