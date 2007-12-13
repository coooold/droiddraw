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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class Main {
	static final File[] saveFile = new File[1];
	              
	protected static void doSave(DroidDrawPanel ddp, JFileChooser jfc, JFrame jf) {
		int res = jfc.showSaveDialog(ddp);
		if (res == JFileChooser.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			if (f.exists()) {
				res = JOptionPane.showConfirmDialog(ddp, f.getName()+" exists. Overwrite?", "Overwrite", JOptionPane.OK_CANCEL_OPTION);
				if (res == JOptionPane.CANCEL_OPTION)
					return;
			}
			jf.setTitle("DroidDraw: "+f.getName());
			ddp.save(f);
			saveFile[0] = f;
		}
	}
	
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
		
		// This is so that I can test out the Google examples...
		// START
		if (args.length > 0) {
			try {
				AndroidEditor.instance().setStrings(StringHandler.load(new FileInputStream("src/strings.xml")));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		// END
		
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
		
		final JFrame jf = new JFrame("DroidDraw");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final DroidDrawPanel ddp = new DroidDrawPanel("qvga", true);
		
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
					ddp.open(jfc.getSelectedFile());
					saveFile[0] = jfc.getSelectedFile();
				}
			}
		});
		it.setShortcut(new MenuShortcut(KeyEvent.VK_O, false));
		menu.add(it);
		it = new MenuItem("Save");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) { 
				if (saveFile[0] == null) {
					doSave(ddp, jfc, jf);
				}
				else {
					ddp.save(saveFile[0]);
				}
			}
		});
		it.setShortcut(new MenuShortcut(KeyEvent.VK_S, false));
		menu.add(it);
		
		it = new MenuItem("Save As...");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doSave(ddp, jfc, jf);
			}
		});
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
		
	/*
	 	menu = new Menu("Edit");
		it = new MenuItem("Cut");
		it.setShortcut(new MenuShortcut(KeyEvent.VK_X, false));
		menu.add(it);
		it = new MenuItem("Copy");
		it.setShortcut(new MenuShortcut(KeyEvent.VK_C, false));
		menu.add(it);
		it = new MenuItem("Paste");
		it.setShortcut(new MenuShortcut(KeyEvent.VK_V, false));
		menu.add(it);
		
		menu.addSeparator();
		it = new MenuItem("Select All");
		it.setShortcut(new MenuShortcut(KeyEvent.VK_A, false));
		menu.add(it);
	*/	
		mb.add(menu);
		
		jf.setMenuBar(mb);
		
		jf.getContentPane().add(ddp);
		jf.pack();
		jf.setVisible(true);
	}
}
