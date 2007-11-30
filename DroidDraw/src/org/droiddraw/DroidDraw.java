package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.Image;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
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
		JToggleButton select;
		JToggleButton add;
		
		
		final JTextArea text = new JTextArea(3,80);
		
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
		tb.add(gen);

		jp.setLayout(new BorderLayout());
		jp.add(viewer, BorderLayout.CENTER);
		jp.add(tb, BorderLayout.SOUTH);
		jp.setBorder(BorderFactory.createTitledBorder("Screen"));
		
		setLayout(new BorderLayout());
		
		
		add(jp, BorderLayout.WEST);
		
		jp = new JPanel();
		jp.setLayout(new BorderLayout());
		jp.add(new JScrollPane(text), BorderLayout.CENTER);

		TitledBorder border = BorderFactory.createTitledBorder("Output");
		
		jp.setBorder(border);
		
		add(jp, BorderLayout.CENTER);
	}
}
