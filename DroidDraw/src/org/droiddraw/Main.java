package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

import org.droiddraw.gui.DroidDrawPanel;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.util.LayoutUploader;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;

import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;


public class Main implements ApplicationListener, URLOpener {
	static File saveFile = null;
	static JFrame jf;
	static DroidDrawPanel ddp;
	static JFileChooser jfc = null;
	static FileDialog fd = null;
	static public boolean osx;
	static FileFilter xmlFilter = null;
	static FileFilter dirFilter = null;
	
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
	
	protected static File doOpen() {
		if (!osx) {
			jfc.setFileFilter(xmlFilter);
			int res = jfc.showOpenDialog(ddp);
			if (res == JFileChooser.APPROVE_OPTION) {
				return jfc.getSelectedFile();
			}
		}
		else {
			fd.setMode(FileDialog.LOAD);
			fd.setVisible(true);
			if (fd.getDirectory() != null && fd.getFile() != null) {
				return new File(fd.getDirectory()+"/"+fd.getFile());
			}
		}
		return null;
	}
	
	protected static File doOpenDir() {
		//if (!osx) {
			jfc.setFileFilter(dirFilter);
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int res = jfc.showOpenDialog(ddp);
			if (res == JFileChooser.APPROVE_OPTION) {
				return jfc.getSelectedFile();
			}
		//}
		//else {
		//	fd.setMode(FileDialog.LOAD);
		//	fd.setFilenameFilter(new FilenameFilter() {
		//		public boolean accept(File arg0, String arg1) {
		//			return arg0.isDirectory();
		//		}
		//	});
		//	fd.setVisible(true);
		//	return new File(fd.getDirectory()+"/"+fd.getFile());
		//}
		return null;
	}
	
	public static File doSaveBasic() {
		File f = null;
		if (!osx) {
			int res = jfc.showSaveDialog(ddp);
			if (res == JFileChooser.APPROVE_OPTION) {
				f = jfc.getSelectedFile();
			}
		}
		else {
			fd.setMode(FileDialog.SAVE);
			fd.setVisible(true);
			if (fd.getFile() != null) {
				f = new File(fd.getDirectory()+"/"+fd.getFile());
			}
		}
		if (f != null && f.exists()) {
			int res = JOptionPane.showConfirmDialog(ddp, f.getName()+" exists. Overwrite?", "Overwrite", JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.CANCEL_OPTION)
				return null;
		}
		return f;
	}
	
	protected static void doSave() {
		File f = doSaveBasic();
		if (f != null) {
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
			AndroidEditor.instance().error("Couldn't open image : "+name);
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
		/*if (args.length > 0) {
			try {
				AndroidEditor.instance().setStrings(StringHandler.load(new FileInputStream("src/strings.xml")));
			} catch (Exception ex) {
				AndroidEditor.instance().error(ex);
			}
		}*/
		// END
		

		AndroidEditor.instance().setURLOpener(new Main());
		
		osx = (System.getProperty("os.name").toLowerCase().contains("mac os x"));
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
		loadImage("paypal");
		
		jf = new JFrame("DroidDraw");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ddp = new DroidDrawPanel("hvgap", false);
		fd = new FileDialog(jf);
		jfc = new JFileChooser();
		
		xmlFilter = new FileFilter() {
			@Override
			public boolean accept(File arg) {
				return arg.getName().endsWith(".xml") || arg.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Android Layout file (.xml)";
			} 
		};
		
		dirFilter = new FileFilter() {
			@Override
			public boolean accept(File arg) {
				return arg.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Directory";
			} 
		};
		jfc.setFileFilter(xmlFilter);
		
		int ctl_key = InputEvent.CTRL_MASK;
		if (osx)
			ctl_key = InputEvent.META_MASK;
		
		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem it;
		it = new JMenuItem("Open");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) { 
				open(doOpen());
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ctl_key));
		menu.add(it);
		menu.addSeparator();
		it = new JMenuItem("Save");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) { 
				if (saveFile == null) {
					doSave();
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
				doSave();
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
		
		menu = new JMenu("Edit");

		it = new JMenuItem("Cut");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String txt = ddp.getSelectedText();
				ddp.deleteSelectedText();
				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
				c.setContents(new StringSelection(txt), null);
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ctl_key));
		menu.add(it);
		it = new JMenuItem("Copy");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ddp.getSelectedText()), null);
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ctl_key));
		menu.add(it);
		it = new JMenuItem("Paste");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
				try {
					String txt = (String)c.getData(DataFlavor.stringFlavor);
					if (txt != null) {
						ddp.insertText(txt);
					}
				} 
				catch (UnsupportedFlavorException ex) {}
				catch (IOException ex) {}
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ctl_key));
		menu.add(it);
		
		menu.addSeparator();
		it = new JMenuItem("Select All");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ddp.selectAll();
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ctl_key));
		//it.setShortcut(new MenuShortcut(KeyEvent.VK_A, false));
		menu.add(it);
	
		menu.addSeparator();
		
		it = new JMenuItem("Clear Screen");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int res = JOptionPane.showConfirmDialog(jf, "This will delete your entire GUI.  Proceed?", "Clear Screen?", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.YES_OPTION) {
					AndroidEditor.instance().getLayout().removeAllWidgets();
					AndroidEditor.instance().select(AndroidEditor.instance().getLayout());
					ddp.repaint();
				}
			}
		});
		menu.add(it);
		
		it = new JMenuItem("Set Ids from Labels");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AndroidEditor.instance().setIdsFromLabels();
			}
		});
		menu.add(it);
		mb.add(menu);
		
		menu = new JMenu("Project");
		it = new JMenuItem("Load string resources");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f = doOpen();
				if (f != null) {
					AndroidEditor.instance().setStrings(f);
				}
			}
		});
		menu.add(it);
		
		it = new JMenuItem("Load color resources");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f = doOpen();
				if (f != null) {
					AndroidEditor.instance().setColors(f);
				}
			}
		});
		menu.add(it);
		
		it = new JMenuItem("Load array resources");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f = doOpen();
				if (f != null) {
					AndroidEditor.instance().setArrays(f);
				}
			}
		});
		menu.add(it);
		
		it = new JMenuItem("Set drawables directory");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f = doOpenDir();
				if (f != null) {
					AndroidEditor.instance().setDrawableDirectory(f);
				}
			}
		});
		menu.add(it);
		
		
		
		menu.addSeparator();
		
		it = new JMenuItem("Load resource directory");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File f = doOpenDir();
				if (f != null) {
					File drawable = new File(f, "drawable");
					if (drawable.exists() && drawable.isDirectory()) {
						AndroidEditor.instance().setDrawableDirectory(f);
					}
					
					f = new File(f, "values");
					if (f.exists() && f.isDirectory()) {
						File strings = new File(f, "strings.xml");
						if (strings.exists()) {
							AndroidEditor.instance().setStrings(strings);
						}
						File colors = new File(f, "colors.xml");
						if (colors.exists()) {
							AndroidEditor.instance().setColors(colors);
						}
						File arrays = new File(f, "arrays.xml");
						if (arrays.exists()) {
							AndroidEditor.instance().setArrays(arrays);
						}
					}
				}
			}
		});
		menu.add(it);
		menu.addSeparator();
		
		it = new JMenuItem("Send layout to device");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ByteArrayOutputStream ba = new ByteArrayOutputStream();
					PrintWriter pw = new PrintWriter(ba);
					AndroidEditor.instance().generate(pw);
					pw.flush();
					ba.flush();
					if (LayoutUploader.upload("127.0.0.1", 6100, new ByteArrayInputStream(ba.toByteArray())))
						JOptionPane.showMessageDialog(jf, "Upload suceeded");
					else
						JOptionPane.showMessageDialog(jf, "Upload failed.  Is AnDroidDraw running?");
				}
				catch (IOException ex) {
					AndroidEditor.instance().error(ex);
				}
			}
		});
		menu.add(it);
		
		mb.add(menu);
		
		menu = new JMenu("Help");
		it = new JMenuItem("Tutorial");
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ctl_key));
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BrowserLauncher l = new BrowserLauncher();
					l.openURLinBrowser("http://www.droiddraw.org/tutorial.html");
				}
				catch (UnsupportedOperatingSystemException ex) {AndroidEditor.instance().error(ex);}
				catch (BrowserLaunchingInitializingException ex) {AndroidEditor.instance().error(ex);}
			}
		});
		menu.add(it);
		
		if (!osx) {
			it = new JMenuItem("About");
			it.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					about();
				}
			});
			menu.add(it);
		}
		
		it = new JMenuItem("Donate");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					BrowserLauncher l = new BrowserLauncher();
					l.openURLinBrowser("https://www.paypal.com/us/cgi-bin/webscr?cmd=_xclick&business=brendan.d.burns@gmail.com&item_name=Support%20DroidDraw&currency_code=USD");
				}
				catch (UnsupportedOperatingSystemException ex) {AndroidEditor.instance().error(ex);}
				catch (BrowserLaunchingInitializingException ex) {AndroidEditor.instance().error(ex);}
			}
		});
		menu.add(it);
		
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

	public void openURL(String url) {
		try {
			BrowserLauncher l = new BrowserLauncher();
			l.openURLinBrowser("https://www.paypal.com/us/cgi-bin/webscr?cmd=_xclick&business=brendan.d.burns@gmail.com&item_name=Support%20DroidDraw&currency_code=USD");
		}
		catch (UnsupportedOperatingSystemException ex) {AndroidEditor.instance().error(ex);}
		catch (BrowserLaunchingInitializingException ex) {AndroidEditor.instance().error(ex);}
	}
}
