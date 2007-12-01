package org.droiddraw;

import java.awt.BorderLayout;
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
	
	public PropertiesPanel(Vector<Property> properties, Widget w) {
		this.properties = properties;
		this.components = new Hashtable<Property, JComponent>();
		this.w =w;
		
		JPanel items = new JPanel();
		items.setLayout(new GridLayout(0,2));
		for (Property prop: properties) {
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
					jc = new JComboBox(((SelectProperty)prop).getOptions());
				}
				else {
					jc = new JTextField(prop.getValue().toString(), 10);
				}
				components.put(prop, jc);
				items.add(jc);
			}
		}
		this.setLayout(new BorderLayout());
		this.add(items, BorderLayout.CENTER);
		JButton apply = new JButton("Apply");
		apply.addActionListener(this);
		this.add(apply, BorderLayout.SOUTH);
	}
	
	public void setViewer(Viewer v) {
		this.viewer = v;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		for (Property prop : properties) {
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
		if (viewer != null)
			viewer.repaint();
		w.apply();
	}
}
