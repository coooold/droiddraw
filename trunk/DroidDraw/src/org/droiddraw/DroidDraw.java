package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class DroidDraw extends JApplet {
	@Override
	public void init() {
		super.init();
		final AndroidEditor ae = new AndroidEditor();
		final Viewer viewer = new Viewer(ae);
		JPanel jp = new JPanel();
		
		ae.setViewer(viewer);
		
		JButton butt;
		JToolBar tb = new JToolBar();
		
		final JTextArea text = new JTextArea(3,80);
		
		butt = new JButton("Generate");
		butt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StringWriter sw = new StringWriter();
				ae.generate(new PrintWriter(sw));
				text.setText(sw.getBuffer().toString());
			}
		});
		tb.add(butt);
		tb.addSeparator();
		
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

		jp.setLayout(new BorderLayout());
		jp.add(viewer, BorderLayout.CENTER);
		jp.add(tb, BorderLayout.SOUTH);
		setLayout(new BorderLayout());
		add(jp, BorderLayout.CENTER);
		add(new JScrollPane(text), BorderLayout.SOUTH);
	}
}
