package org.droiddraw.gui;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;

import org.droiddraw.AndroidEditor;
import org.droiddraw.widget.AbsoluteLayout;
import org.droiddraw.widget.AnalogClock;
import org.droiddraw.widget.AutoCompleteTextView;
import org.droiddraw.widget.Button;
import org.droiddraw.widget.CheckBox;
import org.droiddraw.widget.DatePicker;
import org.droiddraw.widget.DigitalClock;
import org.droiddraw.widget.EditView;
import org.droiddraw.widget.FrameLayout;
import org.droiddraw.widget.Gallery;
import org.droiddraw.widget.GridView;
import org.droiddraw.widget.ImageButton;
import org.droiddraw.widget.ImageView;
import org.droiddraw.widget.Layout;
import org.droiddraw.widget.LinearLayout;
import org.droiddraw.widget.ListView;
import org.droiddraw.widget.ProgressBar;
import org.droiddraw.widget.RadioButton;
import org.droiddraw.widget.RadioGroup;
import org.droiddraw.widget.RelativeLayout;
import org.droiddraw.widget.ScrollView;
import org.droiddraw.widget.Spinner;
import org.droiddraw.widget.TableLayout;
import org.droiddraw.widget.TableRow;
import org.droiddraw.widget.TextView;
import org.droiddraw.widget.Ticker;
import org.droiddraw.widget.TimePicker;
import org.droiddraw.widget.Widget;


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
	JComboBox widgetType = new JComboBox(new String[] {"AnalogClock","AutoCompleteTextView", "Button", "CheckBox", "DigitalClock","EditText", "ImageButton", "ImageView", "ListView", "ProgressBar", "RadioButton","RadioGroup", "Spinner", "TableRow", "TextView", "TimePicker", "AbsoluteLayout", "LinearLayout", "RelativeLayout", "TableLayout", "Ticker"});

	JToggleButton addButton;
	JToggleButton selectButton;
	ButtonGroup bg;
	DroidDrawPanel ddp;

	public ViewerListener(AndroidEditor app, DroidDrawPanel ddp, Viewer viewer) {
		this.ddp = ddp;
		this.app = app;
		this.viewer = viewer;
		this.addButton = new JToggleButton("Add");
		this.selectButton = new JToggleButton("Select");
		this.addButton.addActionListener(this);
		this.selectButton.addActionListener(this);
		this.select = true;
		bg = new ButtonGroup();
		bg.add(selectButton);
		bg.add(addButton);
		bg.setSelected(selectButton.getModel(), true);

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
		return createWidget(str);
	}

	public static Widget createWidget(String str) {
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
		else if (str.equals("ListView"))
			return new ListView();
		else if (str.equals("Ticker"))
			return new Ticker();
		else if (str.equals("Spinner"))
			return new Spinner();
		else if (str.equals("ImageView"))
			return new ImageView();
		else if (str.equals("ImageButton"))
			return new ImageButton();
		else if (str.equals("AutoCompleteTextView"))
			return new AutoCompleteTextView("AutoComplete");
		else if (str.equals("TableRow"))
			return new TableRow();
		else if (str.equals("TableLayout"))
			return new TableLayout();
		else if (str.equals("FrameLayout"))
			return new FrameLayout();
		else if (str.equals("ScrollView"))
			return new ScrollView();
		else if (str.equals("GridView"))
			return new GridView();
		else if (str.equals("Gallery"))
			return new Gallery();
		else if (str.equals("DatePicker"))
			return new DatePicker();
		else
			return null;
	}

	public void mouseEntered(MouseEvent arg0) { }
	public void mouseExited(MouseEvent arg0) { }
	public void mouseClicked(MouseEvent e) { 
		final int x = e.getX()-viewer.getOffX();
		final int y = e.getY()-viewer.getOffY();
		
		
		final MouseEvent ev = e;

		if (select) {
			final Vector<Widget> ws = app.findWidgets(x, y);
			Widget w = null;
			if (ws.contains(app.getSelected())) {
				w = app.getSelected();
			}
			else {
				switch (ws.size()) {
				case 0:
					break;
				case 1:
					w = ws.get(0);
					break;
				default:
					if (e.isControlDown() || e.getButton() == MouseEvent.BUTTON3) {
						JPopupMenu menu = new JPopupMenu();
						JMenuItem it = new JMenuItem("Select a widget:");
						it.setEnabled(false);
						menu.add(it);
						menu.addSeparator();

						for (int i=0;i<ws.size();i++) {
							it = new JMenuItem(ws.get(i).getTagName());
							final int id = i;
							it.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {
									doSelect(ws.get(id), ev.getClickCount(), x, y);
								}
							});
							menu.add(it);
						}	
						menu.show(viewer, x, y);			
					}
					else {
						w = ws.get(0);
					}
				}
			}
			doSelect(w, e.getClickCount(), x, y);
		}
		else {
			//Widget w = createWidget();
			//addWidget(w, x, y);
		}
	}

	protected void doSelect(Widget w, int clickCount, int x, int y) {
		if (clickCount > 1) {
			if (w != null) {
				if (w != app.getSelected()) {
					app.select(w);
				}
				ddp.editSelected();
			}
		}
		else if (mode == NORMAL ){
			if (w != null) {
				off_x = (w.getParent()!=null?w.getParent().getScreenX():0)+w.getX()-x;
				off_y = (w.getParent()!=null?w.getParent().getScreenY():0)+w.getY()-y;
			}
			app.select(w);
			viewer.requestFocus();
			viewer.repaint();
		}
	}

	public void addWidget(Widget ww, int xx, int yy) {
		final int x = xx;
		final int y = yy;
		final Widget w = ww;
		final Vector<Layout> ls = app.findLayouts(x, y);
		Layout l = null;

		switch (ls.size()) {
		case 0:
			return;
		case 1:
			l = ls.get(0);
			break;
		default:
			JPopupMenu menu = new JPopupMenu();
		JMenuItem it = new JMenuItem("Select a layout:");
		it.setEnabled(false);
		menu.add(it);
		menu.addSeparator();

		for (int i=0;i<ls.size();i++) {
			it = new JMenuItem(ls.get(i).getTagName());
			final int id = i;
			it.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					addWidget(w, ls.get(id), x, y);
				}
			});
			menu.add(it);
		}
		menu.show(viewer, x, y);			
		}
		if (l != null) {
			addWidget(w, l, x, y);
		}
	}

	protected void addWidget(Widget w, Layout l, int x, int y) 
	{
		if (shift && l instanceof AbsoluteLayout)
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

	public void mouseReleased(MouseEvent e) {
		e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseDragged(MouseEvent e) {		
		int x = e.getX()-viewer.getOffX();
		int y = e.getY()-viewer.getOffY();
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
		if (x > app.getScreenX())
			x = app.getScreenX();
		if (y > app.getScreenY())
			y = app.getScreenY();

		Widget selected = app.getSelected();
		Vector<Widget> ws = app.findWidgets(x, y);
		
		if (!ws.contains(selected) && ws.size() > 0) {
			app.select(ws.get(0));
			selected = app.getSelected();
		}
		
		if (selected != null) {
			Layout l = selected.getParent();
			Vector<Layout> ls = app.findLayouts(x, y);
			if (ls.size() > 0) {
				int ix = 0;
				do {
					l = ls.get(ix++);
				} while (l.equals(selected) && ix < ls.size());
			}
			else 
				l = (Layout)(selected.getParent());
			if (l != selected.getParent()) {
				if (!(selected instanceof Layout) || !((Layout)selected).containsWidget(l)){
					((Layout)selected.getParent()).removeWidget(selected);
					l.addWidget(selected);
				}
				else {
					l = selected.getParent();
				}
			}

			if (mode == NORMAL) {
				e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
				int nx = (x+off_x);
				int ny = (y+off_y);
				if (shift && l instanceof AbsoluteLayout) {
					nx = nx/grid_x*grid_x;
					ny = ny/grid_y*grid_y;
				}
				selected.setPosition(nx-l.getScreenX(),ny-l.getScreenY());
			}
			else if (mode == E) {
				Widget w = AndroidEditor.instance().getSelected();
				w.setWidth(x-(l.getScreenX()+w.getX()));
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
				w.setHeight(y-(l.getScreenY()+w.getY()));	
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
			boolean close_r = Math.abs(ex-(x+selected.getWidth())) < 5 && ey > y && ey < y+selected.getHeight();
			boolean close_b = Math.abs(ey-(y+selected.getHeight())) < 5 && ex > x && ex < x+selected.getWidth();


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

	public void mousePressed(MouseEvent e) {
		Widget w = app.getSelected();
		if (w != null) {
			int x = e.getX()-viewer.getOffX();
			int y = e.getY()-viewer.getOffY();
			
			off_x = (w.getParent()!=null?w.getParent().getScreenX():0)+w.getX()-x;
			off_y = (w.getParent()!=null?w.getParent().getScreenY():0)+w.getY()-y;
		}
	}
}
