package org.droiddraw;
import java.awt.BorderLayout;
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
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;


public class AndroidEditor {
	Layout layout;
	Widget selected;
	Viewer viewer;
	
	public static int OFFSET_X = 0;
	public static int OFFSET_Y = 48;
	
	public AndroidEditor() {
		layout = new LinearLayout();
	}

	public void setViewer(Viewer v) {
		this.viewer = v;
	}
	
	public void setLayout(Layout l) {
		Vector<Widget> widgets = layout.getWidgets();
		this.layout = l;
		for (Widget w : widgets) {
			l.addWidget(w);
		}
	}
	
	public Layout getLayout() {
		return layout;
	}
	
	public void addWidget(Widget w) {
		layout.addWidget(w);
	}

	public Widget getSelected() {
		return selected;
	}

	public void select(Widget w) {
		selected = w;
	}

	public void removeWidget(Widget w) {
		layout.removeWidget(w);
		if (selected == w) {
			selected = null;
		}
	}
	
	public void editSelected() {
		final Widget select = getSelected();
		if (select!=null) {
			JFrame f = new JFrame("Edit");
			f.getContentPane().setLayout(new BorderLayout());
			JPanel editor = select.getEditorPanel();
			f.getContentPane().add(editor, BorderLayout.CENTER);

			JButton apply = new JButton("Apply");
			apply.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					select.apply();
					viewer.repaint();
				}
			});

			f.getContentPane().add(apply, BorderLayout.SOUTH);
			f.pack();
			f.setVisible(true);
		}
	}

	public Widget findWidget(int x, int y) {
		Widget res = null;
		for (Widget w : layout.getWidgets()) {
			if (w.clickedOn(x, y)) {
				res = w;
				break;
			}
		}
		return res;
	}
	
	public void selectWidget(int x, int y) {
		Widget res = findWidget(x, y);
		if (res == selected) {
			selected = null;
		}
		else {
			selected = res;
		}
	}

	public void generate(PrintWriter pw) {
		pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		layout.printStartTag(pw);
		for (Widget w : layout.getWidgets()) {
			w.generate(pw);
		}
		layout.printEndTag(pw);
		pw.flush();
	}

	public static void main(String[] args) {
		JFrame jf = new JFrame("Android Editor");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final AndroidEditor ae = new AndroidEditor();
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("src/emu1.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		final Viewer viewer = new Viewer(ae, img);
		ae.setViewer(viewer);
		
		MenuBar mb = new MenuBar();
		Menu menu = new Menu("File");
		MenuItem it;
		it = new MenuItem("Save");
		it.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) { 
				JFileChooser jfc = new JFileChooser();
				FileFilter ff = new FileFilter() {
					public boolean accept(File arg) {
						return arg.getName().endsWith(".xml") || arg.isDirectory();
					}

					@Override
					public String getDescription() {
						return "Android Layout file (.xml)";
					} 
				};
				jfc.setFileFilter(ff);
				int res = jfc.showSaveDialog(viewer);
				if (res == JFileChooser.APPROVE_OPTION) {
					try {
						ae.generate(new PrintWriter(new FileWriter(jfc.getSelectedFile())));
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

		JButton butt;
		JToolBar tb = new JToolBar();

		butt = new JButton("Edit");
		butt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ae.editSelected();
			}
		});
		tb.addSeparator();
		tb.add(butt);

		tb.addSeparator();
		
		ButtonGroup bg = viewer.getListener().getInterfaceStateGroup();
		Enumeration<AbstractButton> buttons = bg.getElements();
		while (buttons.hasMoreElements()) {
			tb.add(buttons.nextElement());
			tb.addSeparator();
		}

		tb.add(viewer.getListener().getWidgetSelector());

		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(viewer, BorderLayout.CENTER);
		jf.getContentPane().add(tb, BorderLayout.SOUTH);
		jf.pack();
		jf.setVisible(true);
	}
}
