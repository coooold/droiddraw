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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
import javax.xml.parsers.ParserConfigurationException;

import org.droiddraw.gui.DroidDrawPanel;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.gui.LayoutPainter;
import org.droiddraw.gui.Preferences;
import org.droiddraw.gui.PreferencesPanel;
import org.droiddraw.gui.ScrollViewPainter;
import org.droiddraw.gui.WidgetDeleteRecord;
import org.droiddraw.gui.WidgetRegistry;
import org.droiddraw.util.DroidDrawHandler;
import org.droiddraw.util.FileFilterExtension;
import org.droiddraw.util.LayoutUploader;
import org.droiddraw.widget.AbstractLayout;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.ScrollView;
import org.droiddraw.widget.TabWidget;
import org.droiddraw.widget.Widget;
import org.droiddraw.widget.WidgetTransferable;
import org.simplericity.macify.eawt.Application;
import org.simplericity.macify.eawt.ApplicationEvent;
import org.simplericity.macify.eawt.ApplicationListener;
import org.simplericity.macify.eawt.DefaultApplication;
import org.xml.sax.SAXException;

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
	static FileFilterExtension xmlFilter = null;
	static FileFilter dirFilter = null;
	static FileFilter imgFilter = null;

	protected static void doMacOSXIntegration() {
		Application a = new DefaultApplication();
		a.addApplicationListener( new Main() );
	}

	protected static void open( String file ) {
		open( new File( file ) );
	}

	protected static void open( File f ) {
		ddp.open( f );
		saveFile = f;
	}

	protected static void quit() {
		quit( true );
	}

	protected static void quit( boolean cancelable ) {
		if ( AndroidEditor.instance().isChanged() ) {
			int opt = JOptionPane.showConfirmDialog( ddp, "Do you wish to save changes to your layout?", "Unsaved Changes", cancelable ? JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_NO_OPTION );
			switch ( opt ) {
			case JOptionPane.CANCEL_OPTION:
				return;
			case JOptionPane.YES_OPTION:
				if ( doSave() ) break;
				else return;
			case JOptionPane.NO_OPTION:
				break;
			}
		}
		System.exit( 0 );
	}

	protected static void preferences() {
		JFrame jf = new JFrame();
		jf.getContentPane().add( new PreferencesPanel( AndroidEditor.instance().getPreferences(), jf ) );
		jf.pack();
		jf.setVisible( true );
	}

	protected static void about() {
		final JDialog jd = new JDialog( jf, "About DroidDraw" );
		jd.getContentPane().setLayout( new BorderLayout() );
		jd.getContentPane().add( new JLabel( new ImageIcon( ImageResources.instance().getImage( "droiddraw_small" ) ) ), BorderLayout.CENTER );
		jd.pack();
		jd.setResizable( false );
		jd.setLocationRelativeTo( null );
		jd.setVisible( true );
		jd.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked( MouseEvent ev ) {
				jd.setVisible( false );
				jd.dispose();
			}
		} );
	}


	public static void doClear(boolean confirm) {
		int res = JOptionPane.YES_OPTION;
		if (confirm) {
			res = JOptionPane.showConfirmDialog( jf, "This will delete your entire GUI.  Proceed?", "Clear Screen?", JOptionPane.YES_NO_OPTION );
		}
		if ( res == JOptionPane.YES_OPTION ) {
			ddp.clear();
			ddp.repaint();
		}
	}
	
	public static File doOpen() {
		return doOpen( null );
	}

	public static File doOpen( File file ) {
		return doOpen( file, xmlFilter );
	}

	public static File doOpenImage( File file ) {
		return doOpen( file, imgFilter );
	}

	private static File doOpen( File file, FileFilter filter ) {
		if ( !osx ) {
			jfc.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
			jfc.setFileFilter( filter );
			if ( file != null ) {
				if ( file.isDirectory() )
					jfc.setCurrentDirectory( file );
				else
					jfc.setCurrentDirectory( file.getParentFile() );
			}
			int res = jfc.showOpenDialog( ddp );
			if ( res == JFileChooser.APPROVE_OPTION ) {
				return jfc.getSelectedFile();
			}
		}
		else {
			fd.setMode( FileDialog.LOAD );
			if ( file != null ) {
				try {
					if ( file.isDirectory() )
						fd.setDirectory( file.getCanonicalPath() );
					else
						fd.setDirectory( file.getParentFile().getCanonicalPath() );
				}
				catch ( IOException ex ) {
					AndroidEditor.instance().error( ex );
				}
			}

			fd.setVisible( true );
			if ( fd.getDirectory() != null && fd.getFile() != null ) {
				return new File( fd.getDirectory() + "/" + fd.getFile() );
			}
		}
		return null;
	}

	public static File doOpenDir() {
		//if (!osx) {
			jfc.setFileFilter( dirFilter );
			jfc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
			int res = jfc.showOpenDialog( ddp );
			if ( res == JFileChooser.APPROVE_OPTION ) {
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
		if ( !osx ) {
			int res = jfc.showSaveDialog( ddp );
			if ( res == JFileChooser.APPROVE_OPTION ) {
				f = jfc.getSelectedFile();
				FileFilter ff = jfc.getFileFilter();
				if (FileFilterExtension.class.isInstance(ff)) {
					String extension = ((FileFilterExtension)ff).getExtension();
					if (extension.length() > 0 && !f.getName().endsWith(extension)) {
						f = new File(f.getAbsolutePath() + "." + extension);
					}
				}
			}
		}
		else {
			fd.setMode( FileDialog.SAVE );
			fd.setVisible( true );
			if ( fd.getFile() != null ) {
				f = new File( fd.getDirectory() + "/" + fd.getFile() );
			}
		}
		if ( f != null && f.exists() ) {
			int res = JOptionPane.showConfirmDialog( ddp, f.getName() + " exists. Overwrite?", "Overwrite", JOptionPane.OK_CANCEL_OPTION );
			if ( res == JOptionPane.CANCEL_OPTION )
				return null;
		}
		return f;
	}

	protected static boolean doSave() {
		File f = doSaveBasic();
		if ( f != null ) {
			jf.setTitle( "DroidDraw: " + f.getName() );
			ddp.save( f );
			saveFile = f;
			return true;	/* 2010/10/16: Added this line of code as a result of commenting out the code block at line 263. */

/*	2010/10/16: Disabled this code block, as per email conversation with Brendan Burns */
//			File src = new File( f.getParentFile(), "Foo.java" );
//			try {
//				FileWriter fw = new FileWriter( src );
//				PrintWriter pw = new PrintWriter( fw );
//				AndroidEditor.instance().generateSource( pw, "foo.bar" );
//				pw.flush();
//				fw.flush();
//				pw.close();
//				fw.close();
//				return true;
//			}
//			catch ( IOException ex ) {
//				ex.printStackTrace();
//				return false;
//			}
		}
		else return false;
	}

	protected static void loadImage( String name )
	throws IOException {
		URL u = ClassLoader.getSystemClassLoader().getResource( "ui/" + name + ".png" );
		if ( u == null ) {
			AndroidEditor.instance().error( "Couldn't open image : " + name );
			return;
		}
		InputStream is = u.openStream();
		BufferedImage img = ImageIO.read( is );
		ImageResources.instance().addImage( img, name );
	}

	public static final int BUFFER = 4096;
	
	protected static void makeAPK( File dir, boolean install )
	throws IOException {
		URL u = ClassLoader.getSystemClassLoader().getResource( "data/activity.zip" );
		if ( u == null ) {
			AndroidEditor.instance().error( "Couldn't open activity.zip" );
			return;
		}
		InputStream is = u.openStream();

		ZipInputStream zis = new ZipInputStream( new BufferedInputStream( is ) );
		ZipEntry entry;
		while ( ( entry = zis.getNextEntry() ) != null ) {
			int count;
			byte data[] = new byte[BUFFER];
			// write the files to the disk
			if ( entry.isDirectory() ) {
				File f = new File( dir, entry.getName() );
				f.mkdir();
			}
			else {
				FileOutputStream fos = new FileOutputStream( new File( dir, entry.getName() ) );
				BufferedOutputStream dest = new BufferedOutputStream( fos, BUFFER );
				while ( ( count = zis.read( data, 0, BUFFER ) ) != -1 ) {
					dest.write( data, 0, count );
				}
				dest.flush();
				dest.close();
			}
		}
		zis.close();


		String[] cmd = install ? new String[]{"ant", "install"} : new String[]{"ant"};
		File wd = new File( dir, "activity" );
		run(cmd, wd);
		
		File res = new File( wd, "res" );
		res = new File( res, "layout" );
		res = new File( res, "main.xml" );
		PrintWriter pw = new PrintWriter( new FileWriter( res ) );
		AndroidEditor.instance().generate( pw );
		pw.flush();
		pw.close();

	}

	private static boolean run(String[] cmd, File workingDirectory) throws IOException {	
		Process p = Runtime.getRuntime().exec( cmd, null, workingDirectory);
		try {
			int ret = p.waitFor();
			if ( ret != 0 ) {
				InputStream input = p.getErrorStream();
				byte[] buff = new byte[4096];
				for (int i = input.read(buff); i != -1; i = input.read(buff)) {
					System.err.write(buff, 0, i);
				}
				input = p.getInputStream();
				for (int i = input.read(buff); i != -1; i = input.read(buff)) {
					System.out.write(buff, 0, i);
				}
				return false;
			}
		}
		catch ( InterruptedException ex ) {
		}
		return true;
	}
	
	public static void copy( File from, File to )
	throws IOException {
		FileInputStream fis = new FileInputStream( from );
		FileOutputStream fos = new FileOutputStream( to );

		byte[] buffer = new byte[4096];
		int rd = fis.read( buffer );
		while ( rd != -1 ) {
			fos.write( buffer, 0, rd );
			rd = fis.read( buffer );
		}
		fos.flush();
		fos.close();
	}

	public static void main( String[] args )
	throws IOException {

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


		AndroidEditor.instance().setURLOpener( new Main() );

		osx = ( System.getProperty( "os.name" ).toLowerCase().contains( "mac os x" ) );
		if ( osx ) {
			doMacOSXIntegration();
		}

		final Preferences prefs = AndroidEditor.instance().getPreferences();

		boolean checkUpdate = false;
		if ( prefs.getUpdateCheck() == Preferences.Update.YES ) {
			checkUpdate = true;
		}
		else if ( prefs.getUpdateCheck() == Preferences.Update.ASK ) {
			int res = JOptionPane.showConfirmDialog( null, "Check for updates to DroidDraw?", "Update?", JOptionPane.YES_NO_OPTION );
			checkUpdate = res == JOptionPane.YES_OPTION;
		}
		if ( checkUpdate ) {
			if ( !AndroidEditor.instance().isLatestVersion() ) {
				int res = JOptionPane.showConfirmDialog( ddp, "There is a new DroidDraw version available. Do you wish to download it?", "DroidDraw Update", JOptionPane.YES_NO_OPTION );
				if ( res == JOptionPane.YES_OPTION ) {
					AndroidEditor.instance().getURLOpener().openURL( "http://code.google.com/p/droiddraw/downloads/list" );
				}
			}
		}


		loadImage( "emu1" );
		loadImage( "emu2" );
		loadImage( "emu3" );
		loadImage( "emu4" );
		loadImage( "paint" );
		loadImage( "droiddraw_small" );
		loadImage( "paypal" );

		loadImage( "background_01p" );
		loadImage( "background_01l" );

		loadImage( "statusbar_background_p" );
		loadImage( "statusbar_background_l" );

		loadImage( "title_bar.9" );
		loadImage( "stat_sys_data_connected" );
		loadImage( "stat_sys_battery_charge_100" );
		loadImage( "stat_sys_signal_3" );

		loadImage( "scrollbar.9" );
		loadImage( "scrollfield.9" );
		
		loadImage("mapview");

		loadImage("rate_star_big_on");
		loadImage("rate_star_med_on");
		loadImage("rate_star_small_on");

		loadImage( "light/checkbox_off_background" );
		loadImage( "light/checkbox_on_background" );
		loadImage( "light/clock_dial" );
		loadImage( "light/clock_hand_hour" );
		loadImage( "light/clock_hand_minute" );
		loadImage( "light/radiobutton_off_background" );
		loadImage( "light/radiobutton_on_background" );
		loadImage( "light/button_background_normal.9" );
		loadImage( "light/editbox_background_normal.9" );
		loadImage( "light/progress_circular_background" );
		loadImage( "light/progress_particle" );
		loadImage( "light/progress_circular_indeterminate" );
		loadImage( "light/arrow_up_float" );
		loadImage( "light/arrow_down_float" );
		loadImage( "light/spinnerbox_background_focus_yellow.9" );
		loadImage( "light/spinnerbox_arrow_middle.9" );

		loadImage( "def/btn_check_off" );
		loadImage( "def/btn_check_on" );

		loadImage( "def/btn_radio_off" );
		loadImage( "def/btn_radio_on" );

		loadImage( "def/textfield.9" );
		loadImage( "def/btn_default_normal.9" );
		loadImage( "def/progress_wheel_medium" );

		loadImage( "def/spinner_normal.9" );
		loadImage( "def/btn_dropdown_neither.9" );
		loadImage( TabWidget.IMAGE_NAME );
		loadImage( "mdpi/textfield_default.9");
		
		loadImage("def/btn_toggle_off.9");
		loadImage("def/btn_toggle_on.9");
		
		jf = new JFrame( "DroidDraw" );
		jf.addWindowListener( new WindowAdapter() {
			@Override
			public void windowClosing( WindowEvent e ) {
				quit( false );
			}
		} );

		WidgetRegistry.registerPainter( ScrollView.class, new ScrollViewPainter() );
		WidgetRegistry.registerPainter( AbstractLayout.class, new LayoutPainter() );

		String screen = "hvgap";
		AndroidEditor.ScreenMode scr = prefs.getScreenMode();
		if ( scr.equals( AndroidEditor.ScreenMode.HVGA_LANDSCAPE ) )
			screen = "hvgal";
		else if ( scr.equals( AndroidEditor.ScreenMode.QVGA_LANDSCAPE ) )
			screen = "qvgal";
		else if ( scr.equals( AndroidEditor.ScreenMode.QVGA_PORTRAIT ) )
			screen = "qvgap";
		else if ( scr.equals( AndroidEditor.ScreenMode.WVGA_LANDSCAPE ) ) {
			screen = "wvgal";
		}
		else if ( scr.equals( AndroidEditor.ScreenMode.WVGA_PORTRAIT ) ) {
			screen = "wvgap";
		}

		ddp = new DroidDrawPanel( screen, false );
		AndroidEditor.instance().setScreenMode( prefs.getScreenMode() );
		fd = new FileDialog( jf );
		jfc = new JFileChooser();

		xmlFilter = new FileFilterExtension("xml", "Android Layout file (.xml)");

		dirFilter = new FileFilter() {
			@Override
			public boolean accept( File arg ) {
				return arg.isDirectory();
			}

			@Override
			public String getDescription() {
				return "Directory";
			}
		};

		imgFilter = new FileFilter() {
			@Override
			public boolean accept( File f ) {
				if ( f.isDirectory() ) {
					return true;
				}
				return f.getName().endsWith( ".png" ) ||
				f.getName().endsWith( ".jpg" ) ||
				f.getName().endsWith( ".jpeg" ) ||
				f.getName().endsWith( ".gif" );
			}

			@Override
			public String getDescription() {
				return "Image file (*.png, *.jpg, *.jpeg, *.gif)";
			}
		};

		jfc.setFileFilter( xmlFilter );

		int ctl_key = InputEvent.CTRL_MASK;
		if ( osx )
			ctl_key = InputEvent.META_MASK;

		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu( "File" );
		JMenuItem it;
		it = new JMenuItem("New");
		it.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doClear(false);
			}
		});
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ctl_key));
		menu.add(it);
		it = new JMenuItem( "Open" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				open( doOpen() );
			}
		} );
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, ctl_key ) );
		menu.add( it );
		menu.addSeparator();
		it = new JMenuItem( "Save" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent ev ) {
				if ( saveFile == null ) {
					doSave();
				}
				else {
					ddp.save( saveFile );
				}
			}
		} );
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, ctl_key ) );
		menu.add( it );

		it = new JMenuItem( "Save As..." );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				doSave();
			}
		} );
		menu.add( it );

		it = new JMenuItem( "Export as .apk" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				String tmpFile = System.getProperty( "java.io.tmpdir" );
				File f = new File( tmpFile );

				if ( f != null ) {
					try {
						makeAPK( f, false );

						File save = doOpenDir();

						File apk = new File( f, "activity" );
						apk = new File( apk, "bin" );
						apk = new File( apk, "DroidDrawActivity.apk" );

						save = new File( save, "DroidDrawActivity.apk" );

						copy( apk, save );

						AndroidEditor.instance().message( "Saved", "Layout saved as " + save.getCanonicalPath() );
					}
					catch ( IOException ex ) {
						AndroidEditor.instance().error( ex );
					}
				}


			}
		} );
		menu.add( it );

		it = new JMenuItem( "Export to device" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				String tmpFile = System.getProperty( "java.io.tmpdir" );
				File f = new File( tmpFile );
				if ( f.exists() ) {
					try {
						makeAPK( f, true );

						AndroidEditor.instance().message( "Installed", "Layout successfully installed." );
					}
					catch ( IOException ex ) {
						AndroidEditor.instance().error( ex );
					}
				}
				else {
					AndroidEditor.instance().error( "Error generating .apk" );
				}
			}
		} );
		menu.add( it );

		if ( !osx ) {

			it = new JMenuItem( "Preferences" );
			it.addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent e ) {
					preferences();
				}
			} );
			menu.add( it );

			menu.addSeparator();

			it = new JMenuItem( "Quit" );
			it.addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent arg0 ) {
					quit();
				}
			} );
			it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Q, ctl_key ) );
			menu.add( it );
		}

		mb.add( menu );

		menu = new JMenu( "Edit" );

		it = new JMenuItem( "Undo" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				AndroidEditor.instance().undo();
			}
		} );
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Z, ctl_key ) );
		menu.add( it );

		it = new JMenuItem( "Redo" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				AndroidEditor.instance().redo();
			}
		} );
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Y, ctl_key ) );
		menu.add( it );


		it = new JMenuItem( "Cut" );
		addCutAction(it);
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_X, ctl_key ) );
		menu.add( it );
		it = new JMenuItem( "Copy" );
		addCopyAction(it);
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_C, ctl_key ) );
		menu.add( it );
		it = new JMenuItem( "Paste" );
		addPasteAction(it);
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_V, ctl_key ) );
		menu.add( it );

		menu.addSeparator();
		it = new JMenuItem( "Select All" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				ddp.selectAll();
			}
		} );
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_A, ctl_key ) );
		//it.setShortcut(new MenuShortcut(KeyEvent.VK_A, false));
		menu.add( it );

		menu.addSeparator();

		it = new JMenuItem( "Clear Screen" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				doClear(true);
			}
		} );
		menu.add( it );

		it = new JMenuItem( "Set Ids from Labels" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				AndroidEditor.instance().setIdsFromLabels();
			}
		} );
		menu.add( it );
		mb.add( menu );

		menu = new JMenu( "Project" );
		it = new JMenuItem( "Load string resources" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				File f = doOpen();
				if ( f != null ) {
					AndroidEditor.instance().setStrings( f );
				}
			}
		} );
		menu.add( it );

		it = new JMenuItem( "Load color resources" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				File f = doOpen();
				if ( f != null ) {
					AndroidEditor.instance().setColors( f );
				}
			}
		} );
		menu.add( it );

		it = new JMenuItem( "Load array resources" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				File f = doOpen();
				if ( f != null ) {
					AndroidEditor.instance().setArrays( f );
				}
			}
		} );
		menu.add( it );

		it = new JMenuItem( "Set drawables directory" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				File f = doOpenDir();
				if ( f != null ) {
					AndroidEditor.instance().setDrawableDirectory( f );
				}
			}
		} );
		menu.add( it );


		menu.addSeparator();

		it = new JMenuItem( "Load resource directory" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				File f = doOpenDir();
				if ( f != null ) {
					File drawable = new File( f, "drawable" );
					if ( drawable.exists() && drawable.isDirectory() ) {
						AndroidEditor.instance().setDrawableDirectory( f );
					}

					f = new File( f, "values" );
					if ( f.exists() && f.isDirectory() ) {
						File strings = new File( f, "strings.xml" );
						if ( strings.exists() ) {
							AndroidEditor.instance().setStrings( strings );
						}
						File colors = new File( f, "colors.xml" );
						if ( colors.exists() ) {
							AndroidEditor.instance().setColors( colors );
						}
						File arrays = new File( f, "arrays.xml" );
						if ( arrays.exists() ) {
							AndroidEditor.instance().setArrays( arrays );
						}
					}
				}
			}
		} );
		menu.add( it );
		menu.addSeparator();

		it = new JMenuItem( "Send GUI to device" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				try {
					ByteArrayOutputStream ba = new ByteArrayOutputStream();
					PrintWriter pw = new PrintWriter( ba );
					AndroidEditor.instance().generate( pw );
					pw.flush();
					ba.flush();
					if ( LayoutUploader.upload( "127.0.0.1", 6100, new ByteArrayInputStream( ba.toByteArray() ) ) )
						JOptionPane.showMessageDialog( jf, "Upload succeeded" );
					else
						JOptionPane.showMessageDialog( jf, "Upload failed.  Is AnDroidDraw running?" );
				}
				catch ( IOException ex ) {
					AndroidEditor.instance().error( ex );
				}
			}
		} );
		it.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ctl_key));
		menu.add( it );

		mb.add( menu );

		menu = new JMenu( "Help" );
		it = new JMenuItem( "Tutorial" );
		it.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_H, ctl_key ) );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				try {
					BrowserLauncher l = new BrowserLauncher();
					l.openURLinBrowser( "http://www.droiddraw.org/tutorial.html" );
				}
				catch ( UnsupportedOperatingSystemException ex ) {
					AndroidEditor.instance().error( ex );
				}
				catch ( BrowserLaunchingInitializingException ex ) {
					AndroidEditor.instance().error( ex );
				}
			}
		} );
		menu.add( it );

		if ( !osx ) {
			it = new JMenuItem( "About" );
			it.addActionListener( new ActionListener() {
				public void actionPerformed( ActionEvent arg0 ) {
					about();
				}
			} );
			menu.add( it );
		}

		it = new JMenuItem( "Donate" );
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				try {
					BrowserLauncher l = new BrowserLauncher();
					l.openURLinBrowser( "https://www.paypal.com/us/cgi-bin/webscr?cmd=_xclick&business=brendan.d.burns@gmail.com&item_name=Support%20DroidDraw&currency_code=USD" );
				}
				catch ( UnsupportedOperatingSystemException ex ) {
					AndroidEditor.instance().error( ex );
				}
				catch ( BrowserLaunchingInitializingException ex ) {
					AndroidEditor.instance().error( ex );
				}
			}
		} );
		menu.add( it );

		mb.add( menu );
		jf.setJMenuBar( mb );

		jf.getContentPane().add( ddp );
		jf.pack();
		jf.setVisible( true );
		
		if (args.length > 0) {
			String file = args[0];
			if (file.endsWith(".xml")) {
				open(new File(file));
			}
		}
	}

	public static void addPasteAction(JMenuItem it) {
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
				if (ddp != null && ddp.hasFocus()) {
					try {
						String txt = ( String ) c.getData( DataFlavor.stringFlavor );
						if ( txt != null ) {
							ddp.insertText( txt );
						}
					}
					catch ( UnsupportedFlavorException ex ) {
					}
					catch ( IOException ex ) {
					}
				} else {
					DataFlavor flavor = new DataFlavor(Widget.class, "DroidDraw Widget");
					if (c.isDataFlavorAvailable(flavor)) {
						boolean ok = false;
						try {
							String s = (String)c.getData(flavor);
							Widget w = DroidDrawHandler.parseFromString(s);
							String id = w.getId() + "_copy";
							w.setPropertyByAttName("android:id", id);
							if (w != null) {
								AndroidEditor.instance().addWidget(w, 50, 50);
							}
							ok = true;
						} catch (IOException ex) {
							ex.printStackTrace();
						} catch (UnsupportedFlavorException ex) {
							ex.printStackTrace();
						} catch (SAXException ex) {
							ex.printStackTrace();
						} catch (ParserConfigurationException ex) {
							ex.printStackTrace();
						}
						if (!ok) {
							AndroidEditor.instance().error("Paste failed.");
						}
					} 
				}
			}
		} );
	}

	public static void addCopyAction(JMenuItem it) {
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				if (ddp.hasFocus()) {
					if (ddp.getSelectedText() != null && ddp.getSelectedText().length() != 0) {
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents( new StringSelection( ddp.getSelectedText() ), null );
					}
				} else {
					Widget w = AndroidEditor.instance().getSelected();
					if (w != null) {
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new WidgetTransferable(w), null);
					}
				}
			}});
	}

	public static void addCutAction(JMenuItem it) {
		it.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent arg0 ) {
				if (ddp.hasFocus()) {
					String txt = ddp.getSelectedText();
					ddp.deleteSelectedText();
					Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
					c.setContents( new StringSelection( txt ), null );
				} else {
					Widget w = AndroidEditor.instance().getSelected();
					if (w != null) {
						Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new WidgetTransferable(w), null);
						AndroidEditor.instance().removeWidget(w);
						Layout l = w.getParent();
						AndroidEditor.instance().queueUndoRecord(new WidgetDeleteRecord(l, w));
						AndroidEditor.instance().viewer.repaint();
					}
				}
			}
		} );
	}

	public void handleAbout( ApplicationEvent ev ) {
		about();
		ev.setHandled( true );
	}

	public void handleOpenApplication( ApplicationEvent arg0 ) {
	}

	public void handleOpenFile( ApplicationEvent ev ) {
		String f = ev.getFilename();
		if ( f.endsWith( ".xml" ) ) {
			open( ev.getFilename() );
			ev.setHandled( true );
		}
	}

	public void handlePreferences( ApplicationEvent arg0 ) {
		preferences();
	}

	public void handlePrintFile( ApplicationEvent arg0 ) {
	}

	public void handleQuit( ApplicationEvent arg0 ) {
		quit();
	}

	public void handleReopenApplication( ApplicationEvent arg0 ) {
	}

	public void openURL( String url ) {
		try {
			BrowserLauncher l = new BrowserLauncher();
			l.openURLinBrowser( url );
		}
		catch ( UnsupportedOperatingSystemException ex ) {
			AndroidEditor.instance().error( ex );
		}
		catch ( BrowserLaunchingInitializingException ex ) {
			AndroidEditor.instance().error( ex );
		}
	}
}
