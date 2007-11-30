package org.droiddraw;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComboBox;


public class ViewerListener implements MouseListener, MouseMotionListener, ActionListener, KeyListener {
	int off_x;
	int off_y;
	int grid_x = 10;
	int grid_y = 10;
	boolean add;
	boolean select;
	boolean grid;
	
	Viewer viewer;
	AndroidEditor app;
	JComboBox widgetType = new JComboBox(new String[] {"Button", "CheckBox", "EditView", "TextView"});
	
	public ViewerListener(AndroidEditor app, Viewer viewer) {
		this.app = app;
		this.viewer = viewer;
		this.add = true;
		this.grid = true;
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
		else if (str.equals("EditView"))
			return new EditView("EditView");
		else if (str.equals("TextView"))
			return new TextView("TextView");
		else
			return null;
	}
	
	public void mouseClicked(MouseEvent ev) { 
	}
	
	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent ev) { 
		if (select) {
			if (ev.getClickCount() > 1) {
				app.editSelected();
			}
			else {
				Widget w = app.selectWidget(ev.getX(), ev.getY());
				if (w != null) {
					off_x = w.getX()-ev.getX();
					off_y = w.getY()-ev.getY();
				}
			}
		}
		else {
			Widget w = createWidget();
			w.setPosition((ev.getX()/grid_x)*grid_x, (ev.getY()/grid_y)*grid_y);
			app.addWidget(w);
		}
		viewer.requestFocus();
		viewer.repaint();
	}
	
	public void mouseReleased(MouseEvent ev) {
	}

	public void mouseDragged(MouseEvent ev) {
		Widget selected = app.getSelected();
		if (selected == null) {
			app.selectWidget(ev.getX(), ev.getY());
			selected = app.getSelected();
		}
		if (selected != null) {
			int nx = (ev.getX()+off_x);
			int ny = (ev.getY()+off_y);
			if (grid) {
				nx = nx/grid_x*grid_x;
				ny = ny/grid_y*grid_y;
			}
			selected.setPosition(nx,ny);
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
