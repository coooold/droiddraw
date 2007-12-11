package org.droiddraw;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class NineWayImage {
	BufferedImage[] images;
	int dx, dy, wx, wy;
	
	public NineWayImage(Image img, int dx, int dy) {
		this.images = new BufferedImage[9];
		this.dx = dx;
		this.dy = dy;
		this.wx = img.getWidth(null)-2*dx;
		this.wy = img.getHeight(null)-2*dy;

		BufferedImage bi = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_4BYTE_ABGR);
		bi.getGraphics().drawImage(img, 0, 0, null);
		
		int w = bi.getWidth();
		int h = bi.getHeight();
		images[0] = bi.getSubimage(0, 0, dx, dy);
		images[1] = bi.getSubimage(dx, 0, wx, dy);
		images[2] = bi.getSubimage(w-dx, 0, dx, dy);
		
		images[3] = bi.getSubimage(0, dy, dx, wy);
		images[4] = bi.getSubimage(dx, dy,wx, wy);
		images[5] = bi.getSubimage(w-dx,dy,dx,wy);
		
		images[6] = bi.getSubimage(0,h-dy,dx,dy);
		images[7] = bi.getSubimage(dx,h-dy,wx,dy);
		images[8] = bi.getSubimage(w-dx,h-dy, dx, dy);
	}
	
	public void paint(Graphics g, int x, int y, int sx, int sy) {
		g.drawImage(images[0], x, y, null);
		g.drawImage(images[1], x+dx, y, sx-2*dx, dy, null);
		g.drawImage(images[2], x+sx-dx, y, null);
		
		g.drawImage(images[3], x, y+dy,dx,sy-2*dy, null);
		g.drawImage(images[4], x+dx, y+dy, sx-2*dx, sy-2*dy, null);
		g.drawImage(images[5], x+sx-dx, y+dy, dx, sy-2*dy, null);
		
		g.drawImage(images[6], x, y+sy-dy, null);
		g.drawImage(images[7], x+dx, y+sy-dy, sx-2*dx, dy, null);
		g.drawImage(images[8], x+sx-dx, y+sy-dy, null);
	}
}