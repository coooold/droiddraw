package org.droiddraw.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.droiddraw.AndroidEditor;
import org.droiddraw.property.ColorProperty;

public class ColorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JButton jb;
	JTextField jt;
	JAutoComboBox jac;
	BufferedImage img;
	Color c;
	
	public ColorPanel(Color clr) {
		this.c = clr;
		img = new BufferedImage(20,20, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics g = img.getGraphics();
		g.setColor(c);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		jb = new JButton(new ImageIcon(img));
		//jt = new JTextField(ColorProperty.makeColor(c), 6);
		
		Vector<String> colors = new Vector<String>();
		colors.add("");
		for (String key : AndroidEditor.instance().getColors().keySet()) {
			colors.add("@color/"+key);
		}
		jt = new JTextField();
		jac = new JAutoComboBox(colors);
		jac.setEditable(true);
		jac.setStrict(false);
		jac.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(jac.getSelectedItem().toString());
				Color c = ColorProperty.parseColor(jac.getSelectedItem().toString());
				if (c != null) {
					setColor(c);
				}
			}
		});
		
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color nc = chooseColor();
				if (nc != null) {
					setColor(nc);
					//jt.setText(ColorProperty.makeColor(nc));
					String colorString = ColorProperty.makeColor(nc);
					jac.addItem(colorString);
					jac.setSelectedItem(colorString);
				}
			}
		});
		add(jac);
		add(jb);
	}
	
	public void setColor(Color nc) {
		System.out.println(nc);
		Graphics g = img.getGraphics();
		g.setColor(nc);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		jb.repaint();
		c = nc;
	}
	
	public Color getColor() {
		return c;
	}
	
	public String getString() {
		//return jt.getText();
		return jac.getSelectedItem().toString();
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
