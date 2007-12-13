package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PropertiesPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	Vector<Property> properties;
	Hashtable<Property, JComponent> components;
	Viewer viewer;
	Widget w;
	JPanel items;
	Dimension d;

	public PropertiesPanel() {
		this(new Vector<Property>(), null);
	}

	public PropertiesPanel(Vector<Property> properties, Widget w) {
		this.components = new Hashtable<Property, JComponent>();
		this.w =w;

		setProperties(properties, w);
		this.d = new Dimension(200,400);
	}

	public void setProperties(Vector<Property> properties, Widget w) {
		this.properties = properties;
		this.removeAll();
		items = new JPanel();
		items.setLayout(new GridLayout(0,2));
		//items.setBorder(BorderFactory.createTitledBorder("Properties"));
		components.clear();
		this.w = w;
		if (properties.size() > 0) {
			items.add(new JLabel("Properties for: "));
			items.add(new JLabel(w.getTagName()));
		}
		java.awt.FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.RIGHT);
		for (Property prop: properties) {
			if (prop.getEditable()) {
				if (prop instanceof BooleanProperty) {
					items.add(new JPanel());
					JCheckBox jcb = new JCheckBox(prop.getEnglishName());
					jcb.setSelected(((BooleanProperty)prop).getBooleanValue());
					components.put(prop, jcb);
					items.add(jcb);
				}	
				else if (prop instanceof StringProperty) {
					items.add(new JLabel(prop.getEnglishName()));
					JComponent jc;
					if (prop instanceof SelectProperty) {
						JComboBox jcb = new JComboBox(((SelectProperty)prop).getOptions());
						jcb.setSelectedIndex(((SelectProperty)prop).getSelectedIndex());
						jc = jcb;
					}
					else {
						jc = new JTextField(prop.getValue()!=null?prop.getValue().toString():"", 10);
					}
					components.put(prop, jc);
					JPanel p = new JPanel();
					p.setLayout(fl);
					p.add(jc);
					items.add(p);
				}
			}
		}
		this.setLayout(new BorderLayout());
		JPanel p = new JPanel();
		p.add(items);
		this.add(p, BorderLayout.CENTER);
		if (properties.size() > 0) {
			JButton apply = new JButton("Apply");
			apply.addActionListener(this);
			p = new JPanel();
			p.add(apply);
			this.add(p, BorderLayout.SOUTH);
		}
	}

	public void setViewer(Viewer v) {
		this.viewer = v;
	}

	public void actionPerformed(ActionEvent arg0) {
		for (Property prop : properties) {
			if (prop.getEditable()) {
				if (prop instanceof BooleanProperty) {
					JCheckBox jcb = (JCheckBox)components.get(prop);
					((BooleanProperty)prop).setBooleanValue(jcb.isSelected());
				}
				else if (prop instanceof StringProperty) {
					if (prop instanceof SelectProperty) {
						JComboBox jcb = (JComboBox)components.get(prop);
						((SelectProperty)prop).setSelectedIndex(jcb.getSelectedIndex());
					}
					else {
						JTextField jtf = (JTextField)components.get(prop);
						((StringProperty)prop).setStringValue(jtf.getText());
					}
				}
			}
		}
		w.apply();
		if (w instanceof Layout) {
			((Layout)w).repositionAllWidgets();
			w.apply();
		}
		w.getParent().positionWidget(w);
		
		if (viewer != null)
			viewer.repaint();
	}
}
