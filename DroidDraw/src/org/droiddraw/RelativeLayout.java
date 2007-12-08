package org.droiddraw;

import java.util.Hashtable;
import java.util.Vector;

public class RelativeLayout extends AbstractLayout {
	Hashtable<Widget, Vector<Relation>> relations;
	public static final int PADDING = 4;
	
	public static final String[] propNames = 
		new String[] {
		"android:layout_toRight",
		"android:layout_toLeft",
		"android:layout_above",
		"android:layout_below",
		"android:layout_alignRight",
		"android:layout_alignLeft",
		"android:layout_alignTop",
		"android:layout_alignBottom"};
	
	public static final RelationType[] rts = {
		RelationType.TO_RIGHT,
		RelationType.TO_LEFT,
		RelationType.ABOVE,
		RelationType.BELOW,
		RelationType.RIGHT,
		RelationType.LEFT,
		RelationType.TOP,
		RelationType.BOTTOM
	};
	
	public RelativeLayout() {
		super("RelativeLayout");
		relations = new Hashtable<Widget, Vector<Relation>>();
	}
	
	protected static final String strip(String id) {
		int ix = id.indexOf("@");
		int ix2 = id.indexOf("+", ix);
		if (ix2 > ix) {
			return id.substring(ix2+1);
		}
		return id.substring(ix+1);
	}
	
	public Widget findById(String id) {
		for (Widget w : widgets) {
			if (strip(w.getId()).equals(strip(id))) {
				return w;
			}
		}
		return null;
	}

	public void applyRelation(RelationType r, Widget w, Widget parent) {
		int x = w.getX();
		int y = w.getY();
		if (r == RelationType.ABOVE) {
			y = parent.getY()-PADDING-w.getHeight();
		}
		if (r == RelationType.BELOW) {
			y = parent.getY()+parent.getHeight()+PADDING;
		}
		if (r == RelationType.BOTTOM) {
			y = parent.getY()+parent.getHeight()-w.getHeight();
		}
		if (r == RelationType.TOP) {
			y = parent.getY();
		}
		if (r == RelationType.LEFT) {
			x = parent.getX();
		}
		if (r == RelationType.RIGHT) {
			x = parent.getX()+parent.getWidth()-w.getWidth();
		}
		if (r == RelationType.TO_LEFT) {
			x = parent.getX()-PADDING-w.getWidth();
		}
		if (r == RelationType.TO_RIGHT) {
			x = parent.getX()+parent.getWidth()+PADDING;
		}
		w.setPosition(x,y);
	}
	
	@Override
	public void positionWidget(Widget w) {
		Vector<Relation> v = relations.get(w);
		if (v == null) {
			v = new Vector<Relation>();
			relations.put(w, v);
		}
		v.clear();
		boolean positioned = false;
		
		for (int i=0;i<propNames.length;i++) {
			if (w.getPropertyByAttName(propNames[i]) != null) {
				StringProperty p = (StringProperty)w.getPropertyByAttName(propNames[i]);
				String id = p.getStringValue();
				Widget parent = findById(id);
				if (parent != null) {
					v.add(new Relation(w, parent, rts[i]));
					applyRelation(rts[i], w, parent);
					w.removeProperty(p);
					positioned = true;
				}
			}
		}
		if (positioned)
			return;
		
		if (widgets.size() == 1) {
			w.setPosition(0,0);
			return;
		}

		Widget closestTop = null;
		Widget closestLeft = null;
		int closeVert = Integer.MAX_VALUE;
		int closeHorz = Integer.MAX_VALUE;
		int modeVert=0;
		int modeHorz=0;
		
		for (Widget wd : widgets) {
			if (wd == w)
				break;
			if (wd != w) {
				int dist[] = new int[4];
				dist[0] = Math.abs(w.getY()-wd.getY());
				dist[1] = Math.abs(w.getY()+w.getHeight()-(wd.getY()+wd.getHeight()));
				dist[2] = Math.abs(w.getY()+w.getHeight()-wd.getY());
				dist[3] = Math.abs(w.getY()-(wd.getY()+wd.getHeight()));
				for (int i=0;i<dist.length;i++) {
					if (dist[i] < closeVert) {
						closeVert = dist[i];
						closestTop = wd;
						modeVert = i;
					}
				}

				dist[0] = Math.abs(w.getX()-wd.getX());
				dist[1] = Math.abs(w.getX()+w.getWidth()-(wd.getX()+wd.getWidth()));
				dist[2] = Math.abs(w.getX()+w.getWidth()-wd.getX());
				dist[3] = Math.abs(w.getX()-(wd.getX()+wd.getWidth()));
				for (int i=0;i<dist.length;i++) {
					if (dist[i] < closeHorz) {
						closeHorz = dist[i];
						closestLeft = wd;
						modeHorz = i;
					}
				}
			}
		}
		if (closestTop == null || closestLeft == null) {
			w.setPosition(0,0);
			return;
		}
		int x, y;
		x = w.getX();
		y = w.getY();
		switch (modeVert) {
		case 0:
			y = closestTop.getY();
			v.add(new Relation(w, closestTop, RelationType.TOP));
			break;
		case 1:
			y = closestTop.getY()+closestTop.getHeight()-w.getHeight();
			v.add(new Relation(w, closestTop, RelationType.BOTTOM));
			break;
		case 2:
			y = closestTop.getY()-w.getHeight()-PADDING;
			v.add(new Relation(w, closestTop, RelationType.ABOVE));
			break;
		case 3:
			y = closestTop.getY()+closestTop.getHeight()+PADDING;
			v.add(new Relation(w, closestTop, RelationType.BELOW));
			break;
		}
		switch (modeHorz) {
		case 0:
			x = closestLeft.getX();
			v.add(new Relation(w, closestLeft, RelationType.LEFT));
			break;
		case 1:
			x = closestLeft.getX()+closestLeft.getWidth()-w.getWidth();
			v.add(new Relation(w, closestLeft, RelationType.RIGHT));
			break;
		case 2:
			x = closestLeft.getX()-w.getWidth()-PADDING;
			v.add(new Relation(w, closestLeft, RelationType.TO_LEFT));
			break;
		case 3:
			x = closestLeft.getX()+closestLeft.getWidth()+PADDING;
			v.add(new Relation(w, closestLeft, RelationType.TO_RIGHT));
			break;
		}
		/*
		if (closestLeft != null) {
			if (closestLeft.getX()+closestLeft.getWidth() < w.getX()) {
				x = closestLeft.getX()+closestLeft.getWidth()+PADDING;
				v.add(new Relation(w, closestLeft, RelationType.TO_RIGHT));
			}
			else if (w.getX() < closestLeft.getX()) {
				x = closestLeft.getX()-w.getWidth()-PADDING;
				v.add(new Relation(w, closestLeft, RelationType.TO_LEFT));
			}
			else {
				if (Math.abs(w.getX()-closestLeft.getX()) < Math.abs(w.getX()+w.getWidth()-closestLeft.getX()-closestLeft.getWidth())) {
					x = closestLeft.getX();
					v.add(new Relation(w, closestLeft, RelationType.LEFT));
				}
				else {
					x = closestLeft.getX()+closestLeft.getWidth()-w.getWidth();
					v.add(new Relation(w, closestLeft, RelationType.RIGHT));
				}
			}
			*/
		w.setPosition(x, y);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void repositionAllWidgets() {
		Vector<Widget> ws = (Vector<Widget>)widgets.clone();
		widgets.clear();
		for (Widget w : ws) {
			addWidget(w);
		}
	}

	public void addEditableProperties(Widget w) {}

	public void removeEditableProperties(Widget w) {}

	public void addOutputProperties(Widget w, Vector<Property> properties) {
		Vector<Relation> rels = relations.get(w);
		if (rels != null) {
			for (Relation r : rels) {
				if (r.getRelation().equals(RelationType.LEFT)) {
					properties.add(new StringProperty("relation", "android:layout_alignLeft", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.TO_LEFT)) {
					properties.add(new StringProperty("relation", "android:layout_toLeft", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.RIGHT)) {
					properties.add(new StringProperty("relation", "android:layout_alignRight", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.TO_RIGHT)) {
					properties.add(new StringProperty("relation", "android:layout_toRight", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.ABOVE)) {
					properties.add(new StringProperty("relation", "android:layout_above", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.BELOW)) {
					properties.add(new StringProperty("relation", "android:layout_below", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.TOP)) {
					properties.add(new StringProperty("relation", "android:layout_alignTop", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.BOTTOM)) {
					properties.add(new StringProperty("relation", "android:layout_alignBottom", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.CENTER_VERTICAL)) {
					properties.add(new StringProperty("relation", "android:layout_centerVertical", r.getRelatedTo().getId()));
				}
				else if (r.getRelation().equals(RelationType.CENTER_HORIZONTAL)) {
					properties.add(new StringProperty("relation", "android:layout_centerHorizontal", r.getRelatedTo().getId()));
				}
				
				
			}
		}
	}
	
	public static enum RelationType {TOP, ABOVE, BOTTOM, BELOW, LEFT, TO_LEFT, RIGHT, TO_RIGHT, CENTER_VERTICAL, CENTER_HORIZONTAL};
	
	public static class Relation {
		RelationType relation;
		Widget parent;
		Widget widget;
		
		public Relation(Widget widget, Widget parent, RelationType relation) {
			this.widget = widget;
			this.parent = parent;
			this.relation = relation;
		}
		
		public RelationType getRelation() {
			return relation;
		}
		
		public Widget getRelatedTo() {
			return parent;
		}
		
		public Widget getWidget() {
			return widget;
		}
	}
}
