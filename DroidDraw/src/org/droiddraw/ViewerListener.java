package org.droiddraw;
import java.awt.Component;
import java.awt.Cursor;
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
	boolean add;
	boolean select;
	boolean shift;
	
	int mode;

	private static final int NORMAL = 0;
	private static final int E = 1;
	private static final int SE = 2;
	private static final int S = 3;

	Viewer viewer;
	AndroidEditor app;
	JComboBox widgetType = new JComboBox(new String[] {"AnalogClock", "Button", "CheckBox", "DigitalClock","EditText", "ProgressBar", "RadioButton","RadioGroup", "TextView", "TimePicker", "AbsoluteLayout", "LinearLayout", "RelativeLayout"});

	JToggleButton addButton;
	JToggleButton selectButton;
	ButtonGroup bg;

	public ViewerListener(AndroidEditor app, Viewer viewer) {
		this.app = app;
		this.viewer = viewer;
		this.addButton = new JToggleButton("Add");
		this.selectButton = new JToggleButton("Select");
		this.addButton.addActionListener(this);
		this.selectButton.addActionListener(this);
		bg = new ButtonGroup();
		bg.add(selectButton);
		bg.add(addButton);
		bg.setSelected(addButton.getModel(), true);

		if (!System.getProperty("os.name").toLowerCase().contains("mac os x"))
			widgetType.setLightWeightPopupEnabled(false);
	}

	public ButtonGroup getInterfaceStateGroup() {
		return bg;
	}

	public Component getWidgetSelector() {
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
		else if (str.equals("RadioButton"))
			return new RadioButton("RadioButton");
		else if (str.equals("RadioGroup"))
			return new RadioGroup();
		else if (str.equals("TimePicker"))
			return new TimePicker();
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
			else if (mode == NORMAL ){
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

	public void mouseReleased(MouseEvent e) {
		e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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
			Layout l = (Layout)(selected.getParent());
			if (mode == NORMAL) {
				e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				int nx = (x+off_x);
				int ny = (y+off_y);
				if (!shift && l instanceof AbsoluteLayout) {
					nx = nx/grid_x*grid_x;
					ny = ny/grid_y*grid_y;
				}
				selected.setPosition(nx-l.getScreenX(),ny-l.getScreenY());
			}
			else if (mode == E) {
				Widget w = AndroidEditor.instance().getSelected();
				w.setSize(x-(l.getScreenX()+w.getX()), w.getHeight());
			}
			else if (mode == SE) {
				Widget wd = AndroidEditor.instance().getSelected();
				int w = x-(l.getScreenX()+wd.getX());
				int h = y-(l.getScreenY()+wd.getY());
				if (shift) {
					if (w > h) h = w;
					else w = h;
				}
				wd.setSize(w,h);
			}
			else if (mode == S) {
				Widget w = AndroidEditor.instance().getSelected();
				w.setSize(w.getWidth(), y-(l.getScreenY()+w.getY()));	
			}
			l.positionWidget(selected);
			l.apply();
			viewer.repaint();
		}
	}

	public void mouseMoved(MouseEvent ev) { 
		int ex = ev.getX();
		int ey = ev.getY();
		
		Widget selected = AndroidEditor.instance().getSelected();
		mode = 0;
		Cursor c = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

		if (selected != null) {
			int x = selected.getParent().getScreenX()+selected.getX()+viewer.getOffX();
			int y = selected.getParent().getScreenY()+selected.getY()+viewer.getOffY();
			boolean close_r = Math.abs(ex-(x+selected.getWidth())) < 5;
			boolean close_b = Math.abs(ey-(y+selected.getHeight())) < 5 && ex > x && ey < x+selected.getWidth();


			if (close_r) {
				if (close_b) {
					c = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);
					mode = SE;
				}
				else {
					c = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);
					mode = E;
				}
			}
			else if (close_b) {
				c = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);
				mode = S;
			}
		}
		ev.getComponent().setCursor(c);
	}

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
			shift = true;
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
			shift = false;
			break;
		}
	}

	public void keyTyped(KeyEvent ev) {
	}
}
