package org.droiddraw;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Main {
	
	protected static void loadImage(String name) 
		throws IOException
	{
		URL u = ClassLoader.getSystemClassLoader().getResource("ui/"+name+".png");
		if (u == null) {
			System.err.println("Couldn't open image : "+name);
			return;
		}
		InputStream is = u.openStream();
		BufferedImage img = ImageIO.read(is);
		ImageResources.instance().addImage(img, name);
	}
	
	public static void main(String[] args) 
		throws IOException
	{
		loadImage("emu1");
		loadImage("emu2");
		loadImage("emu3");
		loadImage("emu4");
		loadImage("checkbox_off_background");
		loadImage("checkbox_on_background");
		loadImage("clock_dial");
		loadImage("clock_hand_hour");
		loadImage("clock_hand_minute");
		loadImage("radiobutton_off_background");
		loadImage("radiobutton_on_background");
		loadImage("button_background_normal.9");
		loadImage("editbox_background_normal.9");
		loadImage("progress_circular_background");
		loadImage("progress_particle");
		loadImage("progress_circular_indeterminate");
		loadImage("arrow_up_float");
		loadImage("arrow_down_float");
		loadImage("spinnerbox_background_focus_yellow.9");
		loadImage("spinnerbox_arrow_middle.9");
		loadImage("paint");
		
		JFrame jf = new JFrame("DroidDraw");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final DroidDrawPanel ddp = new DroidDrawPanel("qvga");
		
		final FileFilter ff = new FileFilter() {
			@Override
			public boolean accept(File arg) {
				return arg.getName().endsWith(".xml") || arg.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Android Layout file (.xml)";
			} 
		};

		final JFileChooser jfc = new JFileChooser();
		jfc.setFileFilter(ff);

		
		MenuBar mb = new MenuBar();
		Menu menu = new Menu("File");
		MenuItem it;
		it = new MenuItem("Open");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) { 
				int res = jfc.showOpenDialog(ddp);
				if (res == JFileChooser.APPROVE_OPTION) {
					try {
						System.out.println("Loading..");
						AndroidEditor.instance().removeAllWidgets();
						DroidDrawHandler.load(jfc.getSelectedFile());
						ddp.repaint();
					} 
					catch (IOException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(ddp, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
					}
					catch (SAXException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(ddp, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
					}
					catch (ParserConfigurationException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(ddp, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		it.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
		menu.add(it);
		it = new MenuItem("Save");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) { 
				int res = jfc.showSaveDialog(ddp);
				if (res == JFileChooser.APPROVE_OPTION) {
					try {
						AndroidEditor.instance().generate(new PrintWriter(new FileWriter(jfc.getSelectedFile())));
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		it.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
		menu.add(it);
		menu.addSeparator();
		it = new MenuItem("Quit");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		it.setShortcut(new MenuShortcut(KeyEvent.VK_Q, false));
		menu.add(it);

		mb.add(menu);
		jf.setMenuBar(mb);
		
		jf.getContentPane().add(ddp);
		jf.pack();
		jf.setVisible(true);
	}
}
