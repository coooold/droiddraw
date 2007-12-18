package org.droiddraw.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.droiddraw.AndroidEditor;
import org.droiddraw.util.DroidDrawHandler;
import org.droiddraw.widget.AbsoluteLayout;
import org.droiddraw.widget.AnalogClock;
import org.droiddraw.widget.AutoCompleteTextView;
import org.droiddraw.widget.Button;
import org.droiddraw.widget.CheckBox;
import org.droiddraw.widget.DigitalClock;
import org.droiddraw.widget.EditView;
import org.droiddraw.widget.FrameLayout;
import org.droiddraw.widget.ImageButton;
import org.droiddraw.widget.ImageView;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.LinearLayout;
import org.droiddraw.widget.ListView;
import org.droiddraw.widget.ProgressBar;
import org.droiddraw.widget.RadioButton;
import org.droiddraw.widget.RadioGroup;
import org.droiddraw.widget.RelativeLayout;
import org.droiddraw.widget.ScrollView;
import org.droiddraw.widget.Spinner;
import org.droiddraw.widget.TableLayout;
import org.droiddraw.widget.TableRow;
import org.droiddraw.widget.TextView;
import org.droiddraw.widget.Ticker;
import org.droiddraw.widget.TimePicker;
import org.xml.sax.SAXException;


public class DroidDrawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	Dimension d = new Dimension(1100,600);
	JTabbedPane jtb = new JTabbedPane();
	TextArea text;
	JTextArea jtext;
	
	public String getSelectedText() {
		if (text != null) {
			return text.getSelectedText();
		}
		else {
			return jtext.getSelectedText();
		}
	}
	
	public void deleteSelectedText() {
		if (text != null) {
			String txt = text.getText();
			int start = text.getSelectionStart();
			int end = text.getSelectionEnd();
			System.out.println(start+":"+end);
			if (end < txt.length())
				text.setText(txt.substring(0, start)+txt.substring(end+1));
			else 
				text.setText(txt.substring(0, start));
		}
		else {
			String txt = jtext.getText();
			int start = jtext.getSelectionStart();
			int end = jtext.getSelectionEnd();
			if (end < txt.length())
				jtext.setText(txt.substring(0, start)+txt.substring(end+1));
			else 
				jtext.setText(txt.substring(0, start));
		}
	}
	
	public void insertText(String txt) {
		int start = (text != null)?text.getSelectionStart():jtext.getSelectionStart();
		deleteSelectedText();
		if (text != null) {
			text.insert(txt, start);
		}
		else {
			jtext.insert(txt, start);
		}
	}
	
	public void selectAll() {
		if (text != null) {
			text.selectAll();
		}
		else {
			jtext.selectAll();
		}
	}
	
	public Dimension getMinimumSize() {
		return d;
	}
	
	public Dimension getPreferredSize() {
		return d;
	}
	
	public void editSelected() {
		jtb.setSelectedIndex(2);
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
			StringBuffer buff = new StringBuffer();
			char[] data = new char[4098];
			int read = r.read(data);
			while (read != -1) {
				buff.append(data, 0, read);
				read = r.read(data);
			}
			AndroidEditor.instance().removeAllWidgets();
			DroidDrawHandler.loadFromString(buff.toString());
			if (text != null)
				text.setText(buff.toString());
			else
				jtext.setText(buff.toString());
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
	
	public DroidDrawPanel(String screen, boolean applet) {	
		switchToLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		AndroidEditor ae = AndroidEditor.instance();
		
		if (applet) {
			text = new TextArea(5,50);
		}
		else {
			jtext = new JTextArea(5,50);
		}
		
		AbsoluteLayout al = new AbsoluteLayout();
		setupRootLayout(al);
		ae.setLayout(al);
		
		Image img = null;
		
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
		else if ("qvgal".equals(screen)) {
			img = ImageResources.instance().getImage("emu1");
		}
		final Viewer viewer = new Viewer(ae, this, img);
		JPanel jp = new JPanel();
		
		ae.setViewer(viewer);
		
		setLayout(new BorderLayout());
		
		JButton gen;
		JButton edit;
		
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
				editSelected();
			}
		});

		JButton delete = new JButton("Delete");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AndroidEditor.instance().removeWidget(AndroidEditor.instance().getSelected());
				viewer.repaint();
			}
		});
		
		
		//ButtonGroup bg = viewer.getListener().getInterfaceStateGroup();

		JToolBar tb = new JToolBar();
		
		//Enumeration<AbstractButton> buttons = bg.getElements();
		//while (buttons.hasMoreElements()) {
		//	tb.add(buttons.nextElement());
		//	tb.addSeparator();
		//}

		//tb.add(viewer.getListener().getWidgetSelector());
		tb.addSeparator();
		tb.add(edit);
		tb.addSeparator();
		tb.add(delete);
		tb.addSeparator();
		tb.setFloatable(false);
		
		JPanel p = new JPanel();
		SpringLayout sl = new SpringLayout();
		p.setLayout(sl);
		JLabel lbl = new JLabel("Root Layout:");
		//tb.add(lbl);
		//sl.putConstraint(SpringLayout.WEST, lbl, 5, SpringLayout.WEST, p);
		
		final JComboBox layout = new JComboBox(new String[] {"AbsoluteLayout", "LinearLayout", "RelativeLayout", "ScrollView", "TableLayout"});
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
					else if (select.equals("ScrollView")) {
						l = new ScrollView();
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
		tb.add(layout);
		// This is 1.6.x specific *sigh*
		//sl.putConstraint(SpringLayout.BASELINE, lbl, 0, SpringLayout.BASELINE, layout);
		//sl.putConstraint(SpringLayout.NORTH, tb, 5, SpringLayout.SOUTH, layout);
		p.add(tb);
		
		JButton load = new JButton("Load");
		final Component c = this;
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					AndroidEditor.instance().removeAllWidgets();
					if (text != null)
						DroidDrawHandler.loadFromString(text.getText());
					else
						DroidDrawHandler.loadFromString(jtext.getText());
								
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
		
		//sl.putConstraint(SpringLayout.WEST, gen, 5, SpringLayout.WEST, p);
		//sl.putConstraint(SpringLayout.NORTH, gen, 0, SpringLayout.SOUTH, tb);
		//sl.putConstraint(SpringLayout.NORTH, load, 0, SpringLayout.SOUTH, tb);
		//sl.putConstraint(SpringLayout.EAST, gen, 0, SpringLayout.EAST, layout);
		//sl.putConstraint(SpringLayout.WEST, gen, 0, SpringLayout.WEST, layout);
		//sl.putConstraint(SpringLayout.EAST, layout, 0, SpringLayout.EAST, tb);
		//sl.putConstraint(SpringLayout.SOUTH, p, 5, SpringLayout.SOUTH, gen);
		//p.add(load);
		//p.add(gen);
		
		p.setSize(200, 300);
		p.validate();
		//bp.add(p);
		
		//layout.setBorder(BorderFactory.createTitledBorder("Layout"));
		jp.setLayout(new BorderLayout());
		
		JComboBox screen_size = new JComboBox(new String[] {"QVGA Landscape", "QVGA Portrait", "HVGA Landscape", "HVGA Portrait"});
		screen_size.setSelectedIndex(3);
		JPanel top = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		top.setLayout(new GridLayout(2,2));

		top.add(lbl);
		top.add(layout);
		top.add(new JLabel("Screen Size:"));
		top.add(screen_size);
		p = new JPanel();
		p.setLayout(fl);
		p.add(top);
		jp.add(p, BorderLayout.NORTH);
		jp.add(viewer, BorderLayout.CENTER);
		jp.setBorder(BorderFactory.createTitledBorder("Screen"));
		
		//setLayout(new BorderLayout());
		
		
		//add(jp, BorderLayout.WEST);
		
		JPanel out = new JPanel();
		out.setLayout(new BorderLayout());
		out.add(text!=null?text:new JScrollPane(jtext), BorderLayout.CENTER);
		JPanel gp = new JPanel();
		gp.add(gen);
		gp.add(load);
		out.add(gp, BorderLayout.SOUTH);
		
		TitledBorder border = BorderFactory.createTitledBorder("Output");
		
		out.setBorder(border);
		//JPanel jp2 = new JPanel();
		//jp2.setLayout(f2);
		//jp2.setLayout(new GridLayout(0,1));
		//jp2.add(p);
		
		//p.setBorder(BorderFactory.createTitledBorder("Tools"));
		
		JPanel wp = new JPanel();
		JPanel mp = new JPanel();
		wp.setLayout(new GridLayout(0,1));
		AnalogClock ac = new AnalogClock();
		ac.setSize(50, 50);
		mp.add(new WidgetPanel(ac));
		mp.add(new WidgetPanel(new AutoCompleteTextView("AutoComplete")));
		mp.add(new WidgetPanel(new Button("Button")));
		mp.add(new WidgetPanel(new CheckBox("CheckBox")));
		mp.add(new WidgetPanel(new DigitalClock()));
		wp.add(mp);
		mp = new JPanel();
		mp.add(new WidgetPanel(new EditView("EditText")));
		mp.add(new WidgetPanel(new ImageButton()));
		mp.add(new WidgetPanel(new ImageView()));
		mp.add(new WidgetPanel(new ListView()));
		mp.add(new WidgetPanel(new ProgressBar()));
		mp.add(new WidgetPanel(new RadioButton("RadioButton")));
		mp.add(new WidgetPanel(new RadioGroup()));
		wp.add(mp);
		mp = new JPanel();
		mp.add(new WidgetPanel(new Spinner()));
		mp.add(new WidgetPanel(new TextView("TextView")));
		mp.add(new WidgetPanel(new Ticker()));
		mp.add(new WidgetPanel(new TimePicker()));
		wp.add(mp);
		//wp.setSize(wp.getWidth(), 150);
		JPanel ppp = new JPanel();
		ppp.add(wp);
		JScrollPane jswp = new JScrollPane(ppp);
		//jswp.setPreferredSize(new Dimension(wp.getWidth(), 80));
		
		JPanel lp = new JPanel();
		lp.setLayout(new GridLayout(0,1));
		mp = new JPanel();
		mp.setLayout(new FlowLayout());
		mp.add(new WidgetPanel(new AbsoluteLayout()));
		mp.add(new WidgetPanel(new FrameLayout()));
		mp.add(new WidgetPanel(new LinearLayout()));
		mp.add(new WidgetPanel(new ScrollView()));
		lp.add(mp);
		mp = new JPanel();

		mp.add(new WidgetPanel(new RelativeLayout()));
		TableRow tr = new TableRow();
		tr.setSizeInternal(70, tr.getHeight());
		mp.add(new WidgetPanel(tr));
		mp.add(new WidgetPanel(new TableLayout()));
		lp.add(mp);
		mp = new JPanel();
		mp.add(lp);
		JScrollPane jslp = new JScrollPane(mp);
		
		jtb.addTab("Widgets", jswp);
		jtb.addTab("Layouts", jslp);
		jtb.addTab("Properties", new JScrollPane(AndroidEditor.instance().getPropertiesPanel()));
		
		//add(out, BorderLayout.CENTER);
		JSplitPane ctl = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jtb, out);
		final JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp, ctl);
		
		//p = new JPanel();
		//p.setLayout(fl);
		//p.add(tb);
		//add(p, BorderLayout.NORTH);
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
