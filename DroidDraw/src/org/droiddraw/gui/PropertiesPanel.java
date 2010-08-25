package org.droiddraw.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.droiddraw.AndroidEditor;
import org.droiddraw.Main;
import org.droiddraw.property.BooleanProperty;
import org.droiddraw.property.ColorProperty;
import org.droiddraw.property.ImageProperty;
import org.droiddraw.property.IntProperty;
import org.droiddraw.property.Property;
import org.droiddraw.property.SelectProperty;
import org.droiddraw.property.StringProperty;
import org.droiddraw.util.FileCopier;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.Widget;

public class PropertiesPanel
extends JPanel
implements ActionListener, PropertyChangeListener, KeyListener
{
	private static final long serialVersionUID = 1L;

	Vector<Property> properties;
	Hashtable<Property, JComponent> components;
	Hashtable<ColorProperty, Color> colorTable;
	Viewer viewer;
	Widget w;
	JPanel items;
	Dimension d;
	boolean applet;

	public PropertiesPanel(boolean applet) {
		this(new Vector<Property>(), null, applet);
	}

	public PropertiesPanel(Vector<Property> properties, Widget w, boolean applet) {
		this.components = new Hashtable<Property, JComponent>();
		this.colorTable = new Hashtable<ColorProperty, Color>();
		this.w =w;
		this.applet = applet;

		setProperties(properties, w);
		this.d = new Dimension(200,400);
	}

	public void setApplet(boolean applet) {
		this.applet = applet;
	}

	public void setProperties(Vector<Property> properties, Widget w) {
		this.properties = properties;
		this.removeAll();
		this.w = w;
		if (w == null)
			return;
		w.setPropertyChangeListener(this);
		items = new JPanel();
		items.setLayout(new GridLayout(0,2));
		//items.setBorder(BorderFactory.createTitledBorder("Properties"));
		components.clear();

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
				else if (prop instanceof IntProperty) {
					items.add(new JLabel(prop.getEnglishName()));
					JTextField jf = new JTextField(prop.getValue()!=null?prop.getValue().toString():"", 5);
					JPanel jp = new JPanel();
					jp.setLayout(fl);
					jp.add(jf);
					components.put(prop, jf);
					items.add(jp);
				}
				else if (prop instanceof ImageProperty) {
					items.add(new JLabel(prop.getEnglishName()));
					final JTextField jf = new JTextField(prop.getValue()!=null?prop.getValue().toString():"", 10);
					JPanel jp = new JPanel();
					jp.setLayout(fl);
					jp.add(jf);
					if (!applet) {
						JButton jb = new JButton("Browse");
						jb.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (AndroidEditor.instance().getDrawableDirectory() == null) {
									JOptionPane.showMessageDialog(PropertiesPanel.this, "You must select a drawables directory.\n If you select an image which is not in this directory,\n it will be copied into it.",  "Select Drawable Dir.", JOptionPane.INFORMATION_MESSAGE);
									File dir = Main.doOpenDir();
									if (dir != null) {
										AndroidEditor.instance().setDrawableDirectory(dir);
									}
								}
								File dir = AndroidEditor.instance().getDrawableDirectory();
								if (dir != null) {
									File img = Main.doOpenImage(dir);
									if (img != null) {
										if (!img.getParentFile().equals(dir)) {
											try {
												FileCopier.copy(img, dir, true);
											}
											catch (IOException ex) {
												AndroidEditor.instance().error(ex);
											}
										}


										String name = img.getName();
										int ix = name.indexOf(".");
										if (ix != -1) {
											name = name.substring(0, ix);
										}
										jf.setText("@drawable/"+name);
										jf.requestFocus();
									}
								}
							}
						});
						jp.add(jb);
					}
					components.put(prop, jf);
					items.add(jp);

				}
				else if (prop instanceof StringProperty) {
					items.add(new JLabel(prop.getEnglishName()));

					JComponent jc;
					if (prop instanceof SelectProperty) {
						JComboBox jcb = new JComboBox(((SelectProperty)prop).getOptions());
						jcb.setSelectedIndex(((SelectProperty)prop).getSelectedIndex());
						jc = jcb;
					}
					else if (prop instanceof ColorProperty) {
						jc = new ColorPanel(((ColorProperty)prop).getColorValue());
					}
					else {
						if (prop.getAtttributeName().equals("android:layout_width") ||
								prop.getAtttributeName().equals("android:layout_height"))
						{
							Vector<String> v = new Vector<String>();
							v.add("");
							v.add("fill_parent");
							v.add("wrap_content");
							//JAutoComboBox jat = new JAutoComboBox(v);
							JAutoTextField jat = new JAutoTextField(v);
							jat.setStrict(false);
							jat.setColumns(10);
							if (prop.getValue() != null) {
								jat.setText(prop.getValue().toString());
							}
							jc = jat;
						}
						else {
							jc = new JTextField(prop.getValue()!=null?prop.getValue().toString():"", 10);
						}
					}
					prop.addPropertyChangeListener(new PropertyChangeListener() {
						public void propertyChange(PropertyChangeEvent evt) {
							Property p = (Property)evt.getSource();
							JComponent jc = components.get(p);
							if (p instanceof BooleanProperty) {
								((JCheckBox)jc).setSelected(((Boolean)evt.getNewValue()).booleanValue());
							}
							else if (p instanceof ColorProperty) {
								// TODO
							}
							else if (p instanceof SelectProperty) {
								// TODO
							}
							else if (p instanceof StringProperty) {
								if (jc != null){
									((JTextField)jc).setText(evt.getNewValue().toString());
								}
							}

						}
					});
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
		this.add(new JScrollPane(p), BorderLayout.CENTER);
		if (properties.size() > 0) {
			JButton apply = new JButton("Apply");
			apply.addActionListener(this);
			p = new JPanel();
			p.add(apply);
			this.add(p, BorderLayout.SOUTH);
		}
		this.repaint();
		this.invalidate();
	}

	public void setViewer(Viewer v) {
		this.viewer = v;
	}

	public void actionPerformed(ActionEvent arg0) {
		apply();
	}

	protected void apply() {
		for (Property prop : properties) {
			if (prop.getEditable()) {
				if (prop instanceof BooleanProperty) {
					JCheckBox jcb = (JCheckBox)components.get(prop);
					((BooleanProperty)prop).setBooleanValue(jcb.isSelected());
				}
				else if (prop instanceof IntProperty) {
					JTextField jf = (JTextField)components.get(prop);
					try {
						((IntProperty)prop).setIntValue(Integer.parseInt(jf.getText()));
					} catch (NumberFormatException ex) {
						AndroidEditor.instance().error(ex);
					}
				}
				else if (prop instanceof StringProperty) {
					if (prop instanceof SelectProperty) {
						JComboBox jcb = (JComboBox)components.get(prop);
						if (jcb == null) {
							AndroidEditor.instance().error("Couldn't find select for: "+prop.getAtttributeName());
						}
						else
							((SelectProperty)prop).setSelectedIndex(jcb.getSelectedIndex());
					}
					else if (prop instanceof ColorProperty) {
						ColorPanel cp = (ColorPanel)components.get(prop);
						//((ColorProperty)prop).setColorValue(cp.getColor());
						((ColorProperty)prop).setStringValue(cp.getString());
					}
					else {
						JTextField jtf = (JTextField)components.get(prop);
						if (jtf == null)
							AndroidEditor.instance().error("Couldn't find text for: "+prop.getAtttributeName());
						else if(prop.getAtttributeName().equals("android:layout_width") ||
								prop.getAtttributeName().equals("android:layout_height")){
							if(!jtf.getText().endsWith("px") && !jtf.getText().equals("wrap_content") && !jtf.getText().equals("fill_parent")){
								AndroidEditor.instance().error("Incorrect Syntax for: " + prop.getEnglishName() + "\n\"px\" is required after a width or height entry");
							}
							else
								((StringProperty)prop).setStringValue(jtf.getText());
						}
						else
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
		if (w.getParent() != null)
			w.getParent().positionWidget(w);

		if (viewer != null)
			viewer.repaint();
	}

	@SuppressWarnings("unchecked")
	public void propertyChange(PropertyChangeEvent evt) {
		if (w.equals(evt.getSource())) {
			setProperties((Vector<Property>)evt.getNewValue(), w);
		}
	}

	public void keyPressed(KeyEvent e) {}

	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			apply();
		}
	}
}
