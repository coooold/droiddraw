package org.droiddraw;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

public class Viewer extends JPanel {
	private static final long serialVersionUID = 1L;

	Dimension d;
	AndroidEditor app;
	ViewerListener vl;
	Image img;

	public Viewer(AndroidEditor app, Image img) {
		this.app = app;
		this.d = new Dimension(app.getScreenX(),app.getScreenY());
		vl = new ViewerListener(app, this);
		addMouseListener(vl);
		addMouseMotionListener(vl);
		addKeyListener(vl);
		this.img = img;
	}

	public void resetScreen(Image img) {
		this.d = new Dimension(app.getScreenX(),app.getScreenY());
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
		return (int)(getWidth()-d.getWidth())/2;
	}

	public int getOffY() {
		return 0;
	}

	public void paint(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g2d = (Graphics2D)g;
		int dx = getOffX();
		int dy = getOffY();
		g2d.transform(AffineTransform.getTranslateInstance(dx, dy));


		if (img != null)
			g.drawImage(img, 0, 0, this);

		app.getLayout().paint(g);

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
}