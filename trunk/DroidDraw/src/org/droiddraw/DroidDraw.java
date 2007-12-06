package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

public class DroidDraw extends JApplet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		super.init();
		final AndroidEditor ae = AndroidEditor.instance();
		String screen = this.getParameter("Screen");
		Image img;
		MediaTracker md = new MediaTracker(this);
		final Image[] images = new Image[4];
		images[0] = getImage(getCodeBase(), "emu1.png");
		for (int i=1;i<5;i++) {
			images[i-1] = getImage(getCodeBase(), "emu"+i+".png");
			md.addImage(images[i-1], i);
		}
		for (int i=1;i<5;i++) {
			try {
				md.waitForID(i);
			} catch (InterruptedException ex) {}
		}

		if ("qvgap".equals(screen)) {
			ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_PORTRAIT);
			img = images[1];
		}
		else if ("hvgal".equals(screen)) {
			ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_LANDSCAPE);
			img = images[2];
		}
		else if ("hvgap".equals(screen)) {
			ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_PORTRAIT);
			img = images[3];
		}
		else {
			img = images[0];
		}
		final Viewer viewer = new Viewer(ae, img);
		JPanel jp = new JPanel();
		
		ae.setViewer(viewer);
		
		JButton gen;
		JButton edit;
		
		final TextArea text = new TextArea(5,50);
		
		gen = new JButton("Generate");
		gen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StringWriter sw = new StringWriter();
				ae.generate(new PrintWriter(sw));
				text.setText(sw.getBuffer().toString());
			}
		});

		edit = new JButton("Edit");
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ae.editSelected();
			}
		});

		JButton delete = new JButton("Delete");
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ae.removeWidget(ae.getSelected());
			}
		});
		
		
		ButtonGroup bg = viewer.getListener().getInterfaceStateGroup();


		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		
		FlowLayout f2 = new FlowLayout();
		f2.setAlignment(FlowLayout.LEFT);
		
		JPanel bp = new JPanel();
		bp.setLayout(new GridLayout(0,1));
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
		p.setLayout(fl);
		
		p.add(new JLabel("Layout:"));
		final JComboBox layout = new JComboBox(new String[] {"AbsoluteLayout", "LinearLayout", "RelativeLayout"});
		layout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("comboBoxChanged")) {
					String select = (String)((JComboBox)e.getSource()).getSelectedItem();
					if (select.equals("AbsoluteLayout")) {
						ae.setLayout(new AbsoluteLayout());
						viewer.repaint();
					}
					else if (select.equals("LinearLayout")) {
						ae.setLayout(new LinearLayout());
						viewer.repaint();
					}
					else if (select.equals("RelativeLayout")) {
						ae.setLayout(new RelativeLayout());
						viewer.repaint();
					}
				}
			}
		});
		p.add(layout);
		bp.add(p);
		JPanel tbp = new JPanel();
		tbp.setLayout(fl);
		tbp.add(tb);
		bp.add(tbp);
		
		p = new JPanel();
		SpringLayout l = new SpringLayout();

		p.setLayout(l);
		JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					ae.removeAllWidgets();
					DroidDrawHandler.loadFromString(text.getText());
					layout.setSelectedItem(ae.getLayout().toString());
					viewer.repaint();
				} 
				catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		
		l.putConstraint(SpringLayout.WEST, gen, 5, SpringLayout.WEST, p);
		l.putConstraint(SpringLayout.NORTH, load, 0, SpringLayout.NORTH, gen);
		l.putConstraint(SpringLayout.WEST, gen, 40, SpringLayout.EAST, load);
		p.add(load);
		p.add(gen);
		
		bp.add(p);
		
		//layout.setBorder(BorderFactory.createTitledBorder("Layout"));
		jp.setLayout(new BorderLayout());
		
		JComboBox screen_size = new JComboBox(new String[] {"QVGA Landscape", "QVGA Portrait", "HVGA Landscape", "HVGA Portrait"});
		JPanel top = new JPanel();
		top.setLayout(fl);
		top.add(new JLabel("Screen Size:"));
		top.add(screen_size);
		
		jp.add(top, BorderLayout.NORTH);
		jp.add(viewer, BorderLayout.CENTER);
		jp.add(bp, BorderLayout.SOUTH);
		jp.setBorder(BorderFactory.createTitledBorder("Screen"));
		
		//setLayout(new BorderLayout());
		
		
		//add(jp, BorderLayout.WEST);
		
		JPanel out = new JPanel();
		out.setLayout(new BorderLayout());
		out.add(text, BorderLayout.CENTER);

		TitledBorder border = BorderFactory.createTitledBorder("Output");
		
		out.setBorder(border);
		JPanel jp2 = new JPanel();
		//jp2.setLayout(f2);
		jp2.setLayout(new GridLayout(0,1));
		jp2.add(bp);
		
		jp2.setBorder(BorderFactory.createTitledBorder("Tools"));
		
		//add(out, BorderLayout.CENTER);
		JSplitPane ctl = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jp2, out);
		final JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, jp, ctl);
		add(jsp);
		screen_size.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox jcb = (JComboBox)e.getSource();
				int ix = jcb.getSelectedIndex();
				switch (ix) {
				case 0:
					ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_LANDSCAPE);
					viewer.resetScreen(images[0]);
					setSize(1000,450);
					break;
				case 1:
					ae.setScreenMode(AndroidEditor.ScreenMode.QVGA_PORTRAIT);
					viewer.resetScreen(images[1]);
					setSize(1000,550);
					
					break;
				case 2:
					ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_LANDSCAPE);
					viewer.resetScreen(images[2]);
					setSize(1100,550);
					
					break;
				case 3:
					ae.setScreenMode(AndroidEditor.ScreenMode.HVGA_PORTRAIT);
					viewer.resetScreen(images[3]);
					setSize(1000,750);
					
					break;
				}
				jsp.validate();
				viewer.repaint();
			}
		});
	}
}
