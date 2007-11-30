package org.droiddraw;
import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;


public class AndroidEditor {
	protected Vector<Widget> widgets;
	Widget selected;
	Viewer viewer;
	
	public AndroidEditor() {
		widgets = new Vector<Widget>();
	}

	public void setViewer(Viewer v) {
		this.viewer = v;
	}
	
	public void addWidget(Widget w) {
		widgets.add(w);
	}

	public Widget getSelected() {
		return selected;
	}

	public void select(Widget w) {
		selected = w;
	}

	public void removeWidget(Widget w) {
		widgets.remove(w);
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

	public Widget selectWidget(int x, int y) {
		Widget res = null;
		for (Widget w : widgets) {
			if (w.clickedOn(x, y)) {
				res = w;
				if (w == selected) {
					selected = null;
				}
				else {
					selected = w;
				}
				break;
			}
		}
		return res;
	}

	public Vector<Widget> getWidgets() {
		return widgets;
	}

	public void generate(PrintWriter pw) {
		pw.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		pw.println("<AbsoluteLayout xmlns:android=\"http://schemas.android.com/apk/res/android\" android:orientation=\"vertical\" android:layout_width=\"fill_parent\" android:layout_height=\"fill_parent\">");
		for (Widget w : widgets) {
			w.generate(pw);
		}
		pw.println("</AbsoluteLayout>");
		pw.flush();
	}

	public static void main(String[] args) {
		JFrame jf = new JFrame("Android Editor");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final AndroidEditor ae = new AndroidEditor();
		final Viewer viewer = new Viewer(ae);
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
		ButtonGroup bg = new ButtonGroup();
		JToggleButton jtb = new JToggleButton("Select");
		jtb.addActionListener(viewer.getListener());
		bg.add(jtb);
		tb.add(jtb);
		tb.addSeparator();

		jtb = new JToggleButton("Add");
		jtb.addActionListener(viewer.getListener());
		bg.add(jtb);
		tb.add(jtb);

		bg.setSelected(jtb.getModel(), true);

		tb.add(viewer.getListener().getWidgetSelector());

		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(viewer, BorderLayout.CENTER);
		jf.getContentPane().add(tb, BorderLayout.SOUTH);
		jf.pack();
		jf.setVisible(true);
	}
}
