package org.droiddraw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ColorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JButton jb;
	JTextField jt;
	BufferedImage img;
	Color c;
	
	public ColorPanel(Color clr) {
		this.c = clr;
		img = new BufferedImage(20,20, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		jb = new JButton(new ImageIcon(img));
		jt = new JTextField(ColorProperty.makeColor(c), 6);
		
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color nc = chooseColor();
				if (nc != null) {
					Graphics g = img.getGraphics();
					g.setColor(nc);
					g.fillRect(0, 0, img.getWidth(), img.getHeight());
					jb.repaint();
					jt.setText(ColorProperty.makeColor(nc));
					c = nc;
				}
			}
		});
		add(jt);
		add(jb);
	}
	
	public Color getColor() {
		return c;
	}
	
	public String getString() {
		return jt.getText();
	}
	
	static JDialog jd;
	static JColorChooser jcc;
	static Color sc;
	
	public static Color chooseColor() {
		sc = null;
		if (jcc == null) {
			jcc = new JColorChooser();
			jd = JColorChooser.createDialog(null, "Choose a color", true, jcc, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					sc = jcc.getColor();
				}
			}, null);
		}
		jd.setVisible(true);
		return sc;
	}
	
}
