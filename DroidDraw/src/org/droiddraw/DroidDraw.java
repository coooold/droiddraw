package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
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
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

public class DroidDraw extends JApplet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() {
		super.init();
		final AndroidEditor ae = new AndroidEditor();
		Image img = getImage(getCodeBase(), "emu1.png");
		final Viewer viewer = new Viewer(ae, img);
		JPanel jp = new JPanel();
		
		ae.setViewer(viewer);
		
		JButton gen;
		JButton edit;
		
		final TextArea text = new TextArea(3,80);
		
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
		fl.setAlignment(FlowLayout.RIGHT);
		
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
		//bp.add(tb);
		tb.setFloatable(false);
		
		JPanel p = new JPanel();
		p.setLayout(fl);
		
		p.add(new JLabel("Layout:"));
		JComboBox layout = new JComboBox(new String[] {"AbsoluteLayout", "LinearLayout"});
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
				}
			}
		});
		p.add(layout);
		bp.add(p);
		bp.add(tb);
		
		p = new JPanel();
		p.add(gen);
		bp.add(p);
		
		//layout.setBorder(BorderFactory.createTitledBorder("Layout"));
		jp.setLayout(new BorderLayout());
		
		jp.add(viewer, BorderLayout.CENTER);
		jp.add(bp, BorderLayout.SOUTH);
		jp.setBorder(BorderFactory.createTitledBorder("Screen"));
		
		setLayout(new BorderLayout());
		
		
		add(jp, BorderLayout.WEST);
		
		jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(text, BorderLayout.CENTER);

		TitledBorder border = BorderFactory.createTitledBorder("Output");
		
		jp.setBorder(border);
		
		add(jp, BorderLayout.CENTER);
	}
}
