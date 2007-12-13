package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class DroidDrawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	Dimension d = new Dimension(1100,600);
	
	public Dimension getMinimumSize() {
		return d;
	}
	
	public Dimension getPreferredSize() {
		return d;
	}
		
	public void save(File f) {
		try {
			AndroidEditor.instance().generate(new PrintWriter(new FileWriter(f)));
		} catch (IOException ex) {
			ex.printStackTrace();
			error(ex.getMessage(), "Error saving...");
		}
	}
	
	public void open(File f) {
		try {
			this.open(new FileReader(f));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			error(ex.getMessage());
		}
	}
	
	public void open(FileReader r) {
		try {
			AndroidEditor.instance().removeAllWidgets();
			DroidDrawHandler.load(r);
			repaint();
		} 
		catch (IOException ex) {
			ex.printStackTrace();
			error(ex.getMessage());
		}
		catch (SAXException ex) {
			ex.printStackTrace();
			error(ex.getMessage());
		}
		catch (ParserConfigurationException ex) {
			ex.printStackTrace();
			error(ex.getMessage());
		}
	}
	
	public void error(String message) {
		this.error(message, "Error");
	}
	
	public void error(String message, String title) {
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}
	
	protected static final void switchToLookAndFeel(String clazz) {
		try {
			UIManager.setLookAndFeel(clazz);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected static final void setupRootLayout(Layout l) {
		l.setPosition(AndroidEditor.OFFSET_X,AndroidEditor.OFFSET_Y);
		l.setPropertyByAttName("android:layout_width", "fill_parent");
		l.setPropertyByAttName("android:layout_height", "fill_parent");
		l.apply();
	}
	
	public DroidDrawPanel(String screen) {	
		switchToLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		AndroidEditor ae = AndroidEditor.instance();
		
		AbsoluteLayout al = new AbsoluteLayout();
		setupRootLayout(al);
		ae.setLayout(al);
		
		Image img;
		
		if ("qvgap".equals(screen)) {
			ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_PORTRAIT);
			img = ImageResources.instance().getImage("emu2");
		}
		else if ("hvgal".equals(screen)) {
			ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_LANDSCAPE);
			img = ImageResources.instance().getImage("emu3");
		}
		else if ("hvgap".equals(screen)) {
			ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_PORTRAIT);
			img = ImageResources.instance().getImage("emu4");
		}
		else {
			img = ImageResources.instance().getImage("emu1");
		}
		final Viewer viewer = new Viewer(ae, img);
		JPanel jp = new JPanel();
		
		ae.setViewer(viewer);
		
		setLayout(new BorderLayout());
		
		JButton gen;
		JButton edit;
		
		final TextArea text = new TextArea(5,50);
		
		gen = new JButton("Generate");
		gen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StringWriter sw = new StringWriter();
				AndroidEditor.instance().generate(new PrintWriter(sw));
				text.setText(sw.getBuffer().toString());
			}
		});

		edit = new JButton("Edit");
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AndroidEditor.instance().editSelected();
			}
		});

		JButton delete = new JButton("Delete");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AndroidEditor.instance().removeWidget(AndroidEditor.instance().getSelected());
				viewer.repaint();
			}
		});
		
		
		ButtonGroup bg = viewer.getListener().getInterfaceStateGroup();

		JToolBar tb = new JToolBar();
		
		Enumeration<AbstractButton> buttons = bg.getElements();
		while (buttons.hasMoreElements()) {
			tb.add(buttons.nextElement());
			tb.addSeparator();
		}

		tb.add(viewer.getListener().getWidgetSelector());
		tb.addSeparator();
		tb.add(edit);
		tb.addSeparator();
		tb.add(delete);
		tb.setFloatable(false);
		
		JPanel p = new JPanel();
		SpringLayout sl = new SpringLayout();
		p.setLayout(sl);
		JLabel lbl = new JLabel("Root Layout:");
		sl.putConstraint(SpringLayout.WEST, lbl, 5, SpringLayout.WEST, p);
		p.add(lbl);
		
		final JComboBox layout = new JComboBox(new String[] {"AbsoluteLayout", "LinearLayout", "RelativeLayout", "TableLayout"});
		if (!System.getProperty("os.name").toLowerCase().contains("mac os x"))
			layout.setLightWeightPopupEnabled(false);
		final ActionListener layoutActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("comboBoxChanged")) {
					String select = (String)((JComboBox)e.getSource()).getSelectedItem();
					Layout l = null;
					if (select.equals("AbsoluteLayout")) {
						l = new AbsoluteLayout();
					}
					else if (select.equals("LinearLayout")) {
						l = new LinearLayout();
					}
					else if (select.equals("RelativeLayout")) {
						l = new RelativeLayout();
					}
					else if (select.equals("TableLayout")) {
						l = new TableLayout();
					}
					viewer.repaint();
					setupRootLayout(l);
					AndroidEditor.instance().setLayout(l);
				}
			}
		};
		
		layout.addActionListener(layoutActionListener);
		p.add(layout);
		// This is 1.6.x specific *sigh*
		//sl.putConstraint(SpringLayout.BASELINE, lbl, 0, SpringLayout.BASELINE, layout);
		sl.putConstraint(SpringLayout.NORTH, tb, 5, SpringLayout.SOUTH, layout);
		p.add(tb);
		
		JButton load = new JButton("Load");
		final Component c = this;
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AndroidEditor.instance().removeAllWidgets();
					DroidDrawHandler.loadFromString(text.getText());
					layout.removeActionListener(layoutActionListener);
					layout.setSelectedItem(AndroidEditor.instance().getLayout().toString());
					layout.addActionListener(layoutActionListener);
					viewer.repaint();
				} 
				catch (Exception ex) {
					JOptionPane.showMessageDialog(c, ex.getMessage(), ex.toString(), JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		
		sl.putConstraint(SpringLayout.WEST, gen, 5, SpringLayout.WEST, p);
		sl.putConstraint(SpringLayout.NORTH, gen, 0, SpringLayout.SOUTH, tb);
		sl.putConstraint(SpringLayout.NORTH, load, 0, SpringLayout.SOUTH, tb);
		sl.putConstraint(SpringLayout.EAST, gen, 0, SpringLayout.EAST, layout);
		sl.putConstraint(SpringLayout.WEST, gen, 0, SpringLayout.WEST, layout);
		sl.putConstraint(SpringLayout.EAST, layout, 0, SpringLayout.EAST, tb);
		sl.putConstraint(SpringLayout.SOUTH, p, 5, SpringLayout.SOUTH, gen);
		p.add(load);
		p.add(gen);
		p.setSize(200, 300);
		p.validate();
		//bp.add(p);
		
		//layout.setBorder(BorderFactory.createTitledBorder("Layout"));
		jp.setLayout(new BorderLayout());
		
		JComboBox screen_size = new JComboBox(new String[] {"QVGA Landscape", "QVGA Portrait", "HVGA Landscape", "HVGA Portrait"});
		JPanel top = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		top.setLayout(fl);
		top.add(new JLabel("Screen Size:"));
		top.add(screen_size);
		
		jp.add(top, BorderLayout.NORTH);
		jp.add(viewer, BorderLayout.CENTER);
		jp.setBorder(BorderFactory.createTitledBorder("Screen"));
		
		//setLayout(new BorderLayout());
		
		
		//add(jp, BorderLayout.WEST);
		
		JPanel out = new JPanel();
		out.setLayout(new BorderLayout());
		out.add(text, BorderLayout.CENTER);

		TitledBorder border = BorderFactory.createTitledBorder("Output");
		
		out.setBorder(border);
		//JPanel jp2 = new JPanel();
		//jp2.setLayout(f2);
		//jp2.setLayout(new GridLayout(0,1));
		//jp2.add(p);
		
		p.setBorder(BorderFactory.createTitledBorder("Tools"));
		
		//add(out, BorderLayout.CENTER);
		JSplitPane ctl = new JSplitPane(JSplitPane.VERTICAL_SPLIT, p, out);
		final JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp, ctl);
		add(jsp, BorderLayout.CENTER);
		screen_size.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox jcb = (JComboBox)e.getSource();
				int ix = jcb.getSelectedIndex();
				AndroidEditor ae = AndroidEditor.instance();
				switch (ix) {
				case 0:
					ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_LANDSCAPE);
					viewer.resetScreen(ImageResources.instance().getImage("emu1"));
					//setSize(1000,450);
					break;
				case 1:
					ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_PORTRAIT);
					viewer.resetScreen(ImageResources.instance().getImage("emu2"));
					//setSize(1000,550);
					
					break;
				case 2:
					ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_LANDSCAPE);
					viewer.resetScreen(ImageResources.instance().getImage("emu3"));
					//setSize(1100,550);
					
					break;
				case 3:
					ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_PORTRAIT);
					viewer.resetScreen(ImageResources.instance().getImage("emu4"));
					//setSize(1000,750);
					
					break;
				}
				jsp.validate();
				viewer.repaint();
			}
		});
		validate();
	}
	
}
