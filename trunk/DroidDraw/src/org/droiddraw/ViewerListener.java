package org.droiddraw;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JToggleButton;


public class ViewerListener implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
	int off_x;
	int off_y;
	int grid_x = 10;
	int grid_y = 10;
	boolean grid;
	boolean add;
	boolean select;
	
	Viewer viewer;
	AndroidEditor app;
	JComboBox widgetType = new JComboBox(new String[] {"Button", "CheckBox", "EditText", "TextView"});
	JToggleButton addButton;
	JToggleButton selectButton;
	ButtonGroup bg;
	
	public ViewerListener(AndroidEditor app, Viewer viewer) {
		this.app = app;
		this.viewer = viewer;
		this.grid = true;
		this.addButton = new JToggleButton("Add");
		this.selectButton = new JToggleButton("Select");
		this.addButton.addActionListener(this);
		this.selectButton.addActionListener(this);
		bg = new ButtonGroup();
		bg.add(selectButton);
		bg.add(addButton);
		bg.setSelected(addButton.getModel(), true);
	}
	
	public ButtonGroup getInterfaceStateGroup() {
		return bg;
	}
	
	public JComboBox getWidgetSelector() {
		return widgetType;
	}
	
	public Widget createWidget() {
		String str = (String)widgetType.getSelectedItem();
		if (str.equals("Button"))
			return new Button("Button");
		else if (str.equals("CheckBox"))
			return new CheckBox("CheckBox");
		else if (str.equals("EditText"))
			return new EditView("EditText");
		else if (str.equals("TextView"))
			return new TextView("TextView");
		else
			return null;
	}
	
	public void mouseClicked(MouseEvent ev) { 
	}
	
	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent e) { 
		int x = e.getX()-viewer.getOffX();
		int y = e.getY()-viewer.getOffY();
		
		if (select) {
			if (e.getClickCount() > 1) {
				Widget w = app.findWidget(x, y);
				if (w != null) {
					if (w != app.getSelected()) {
						app.select(w);
					}
					app.editSelected();
				}
			}
			else {
				app.selectWidget(x, y);
				Widget w = app.getSelected();
				if (w != null) {
					off_x = w.getX()-x;
					off_y = w.getY()-y;
				}
			}
		}
		else {
			Widget w = createWidget();
			if (app.getLayout() instanceof AbsoluteLayout)
				w.setPosition((x/grid_x)*grid_x, (y/grid_y)*grid_y);
			else
				w.setPosition(x, y);
			app.addWidget(w);
			app.select(w);
			bg.setSelected(addButton.getModel(), false);
			bg.setSelected(selectButton.getModel(), true);
			select = true;
		}
		viewer.requestFocus();
		viewer.repaint();
	}
	
	public void mouseReleased(MouseEvent ev) {
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX()-viewer.getOffX();
		int y = e.getY()-viewer.getOffY();
		
		Widget selected = app.getSelected();
		if (selected == null) {
			app.selectWidget(x, y);
			selected = app.getSelected();
		}
		if (selected != null) {
			int nx = (x+off_x);
			int ny = (y+off_y);
			if (grid) {
				nx = nx/grid_x*grid_x;
				ny = ny/grid_y*grid_y;
			}
			selected.setPosition(nx,ny);
			app.getLayout().positionWidget(selected);
			viewer.repaint();
		}
	}

	public void mouseMoved(MouseEvent e) { }

	public void actionPerformed(ActionEvent ev) {
		if (ev.getActionCommand().equals("Add")) {
			add = true;
			select = false;
		}
		else if (ev.getActionCommand().equals("Select")) {
			add = false;
			select = true;
		}
	}

	public void keyPressed(KeyEvent ev) { 
		Widget w;
		switch (ev.getKeyCode()) {
		case KeyEvent.VK_SHIFT:
			grid = false;
			break;
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
			app.removeWidget(app.getSelected());
			viewer.repaint();
			break;
		case KeyEvent.VK_UP:
			w = app.getSelected();
			if (w != null) {
				w.move(0, -1);
			}
			viewer.repaint();
			break;
		case KeyEvent.VK_DOWN:
			w = app.getSelected();
			if (w != null) {
				w.move(0, 1);
			}
			viewer.repaint();
			break;
		case KeyEvent.VK_LEFT:
			w = app.getSelected();
			if (w != null) {
				w.move(-1, 0);
			}
			viewer.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			w = app.getSelected();
			if (w != null) {
					w.move(1, 0);
			}
			viewer.repaint();
			break;
		}
	}

	public void keyReleased(KeyEvent ev) {
		switch (ev.getKeyCode()) {
		case KeyEvent.VK_SHIFT:
			grid = true;
			break;
		}
	}

	public void keyTyped(KeyEvent ev) {
	}
}
