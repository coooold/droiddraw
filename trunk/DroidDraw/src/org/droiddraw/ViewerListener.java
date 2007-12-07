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
	JComboBox widgetType = new JComboBox(new String[] {"AnalogClock", "Button", "CheckBox", "DigitalClock","EditText", "ProgressBar",  "TextView", "AbsoluteLayout", "LinearLayout","RelativeLayout"});
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
		else if (str.equals("AnalogClock"))
			return new AnalogClock();
		else if (str.equals("DigitalClock"))
			return new DigitalClock();
		else if (str.equals("ProgressBar"))
			return new ProgressBar();
 		else if (str.equals("LinearLayout"))
			return new LinearLayout();
 		else if (str.equals("AbsoluteLayout"))
 			return new AbsoluteLayout();
 		else if (str.equals("RelativeLayout"))
 			return new RelativeLayout();
		else
			return null;
	}
	
	public void mouseClicked(MouseEvent ev) { 
	}
	
	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }
	public void mousePressed(MouseEvent e) { 
		int x = e.getX();
		int y = e.getY();
		
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
				Widget w = app.findWidget(x,y);
				if (w != null) {
					off_x = (w.getParent()!=null?w.getParent().getScreenX():0)+w.getX()-x;
					off_y = (w.getParent()!=null?w.getParent().getScreenY():0)+w.getY()-y;
				}
				app.selectWidget(x, y);
				viewer.requestFocus();
				viewer.repaint();
			}
		}
		else {
			Widget w = createWidget();
			Layout l = app.findLayout(x, y);
			if (l instanceof AbsoluteLayout)
				w.setPosition((x/grid_x)*grid_x-l.getScreenX(), (y/grid_y)*grid_y-l.getScreenY());
			else
				w.setPosition(x-l.getScreenX(), y-l.getScreenY());
			l.addWidget(w);
			l.apply();
			app.select(w);
			bg.setSelected(addButton.getModel(), false);
			bg.setSelected(selectButton.getModel(), true);
			select = true;
			viewer.requestFocus();
			viewer.repaint();
		}
	}
	
	public void mouseReleased(MouseEvent ev) {
	}

	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
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
			Layout l = (Layout)(selected.getParent());
			selected.setPosition(nx-l.getScreenX(),ny-l.getScreenY());
			l.positionWidget(selected);
			l.apply();
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
		switch (ev.getKeyCode()) {
		case KeyEvent.VK_SHIFT:
			grid = false;
			break;
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
			app.removeWidget(app.getSelected());
			viewer.repaint();
			break;
		}
		Widget w = app.getSelected();
		if (w != null && w.getParent() instanceof AbsoluteLayout) {
			switch (ev.getKeyCode()) {
			case KeyEvent.VK_UP:
				w.move(0, -1);
				viewer.repaint();
				break;
			case KeyEvent.VK_DOWN:
				w.move(0, 1);
				viewer.repaint();
				break;
			case KeyEvent.VK_LEFT:
				w.move(-1, 0);
				viewer.repaint();
				break;
			case KeyEvent.VK_RIGHT:
				w.move(1, 0);
				viewer.repaint();
				break;
			}
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
