package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;


public class Main implements ApplicationListener {
	static File saveFile = null;
	static JFrame jf;
	static DroidDrawPanel ddp;
	
	
	protected static void doMacOSXIntegration() {
		Application a = new DefaultApplication();
		a.addApplicationListener(new Main());
	}
	
	protected static void open(String file) {
		open(new File(file));
	}
	
	protected static void open(File f) {
		ddp.open(f);
		saveFile = f;
	}
	
	protected static void quit() {
		System.exit(0);
	}
	
	protected static void about() {
		final JDialog jd = new JDialog(jf, "About DroidDraw");
		jd.getContentPane().setLayout(new BorderLayout());
		jd.getContentPane().add(new JLabel(new ImageIcon(ImageResources.instance().getImage("droiddraw_small"))), BorderLayout.CENTER);
		jd.pack();
		jd.setResizable(false);
		jd.setLocationRelativeTo(null);
		jd.setVisible(true);
		jd.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent ev) {
				jd.setVisible(false);
				jd.dispose();
			}
		});
	}
	
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
			saveFile = f;
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
		
		boolean osx = (System.getProperty("os.name").toLowerCase().contains("mac os x"));
		if (osx) {
			doMacOSXIntegration();
		}
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
		loadImage("droiddraw_small");
		
		jf = new JFrame("DroidDraw");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		final DroidDrawPanel ddp = new DroidDrawPanel("hvgap", true);
		
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

		
		int ctl_key = InputEvent.CTRL_MASK;
		if (osx)
			ctl_key = InputEvent.META_MASK;
		
		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem it;
		it = new JMenuItem("Open");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) { 
				int res = jfc.showOpenDialog(ddp);
				if (res == JFileChooser.APPROVE_OPTION) {
					open(jfc.getSelectedFile());
				}
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ctl_key));
		menu.add(it);
		menu.addSeparator();
		it = new JMenuItem("Save");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) { 
				if (saveFile == null) {
					doSave(ddp, jfc, jf);
				}
				else {
					ddp.save(saveFile);
				}
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ctl_key));
		menu.add(it);
		
		it = new JMenuItem("Save As...");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doSave(ddp, jfc, jf);
			}
		});
		menu.add(it);
		
		if (!osx) {
			menu.addSeparator();
			it = new JMenuItem("Quit");
			it.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					quit();
				}
			});
			it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ctl_key));
			menu.add(it);
		}
		
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
		
		jf.setJMenuBar(mb);
		
		jf.getContentPane().add(ddp);
		jf.pack();
		jf.setVisible(true);
	}

	public void handleAbout(ApplicationEvent ev) {
		about();
		ev.setHandled(true);
	}

	public void handleOpenApplication(ApplicationEvent arg0) {}

	public void handleOpenFile(ApplicationEvent ev) {
		String f = ev.getFilename();
		if (f.endsWith(".xml")) {
			open(ev.getFilename());
			ev.setHandled(true);
		}
	}

	public void handlePreferences(ApplicationEvent arg0) {
	}

	public void handlePrintFile(ApplicationEvent arg0) {
	}

	public void handleQuit(ApplicationEvent arg0) {
		quit();
	}

	public void handleReopenApplication(ApplicationEvent arg0) {
	}
}
