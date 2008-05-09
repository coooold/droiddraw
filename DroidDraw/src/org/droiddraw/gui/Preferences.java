package org.droiddraw.gui;

import java.util.prefs.BackingStoreException;

import org.droiddraw.AndroidEditor;
import org.droiddraw.AndroidEditor.ScreenMode;

public class Preferences {
	public static enum Layout {ABSOLUTE, LINEAR, RELATIVE};
	
	
	private static String SNAP = "snap";
	private static String SCREEN = "screen";
	private static String LAYOUT = "layout";
	
	protected boolean snap;
	protected ScreenMode screen;
	protected Layout layout;
	
	
	public Preferences() {
		this.snap = false;
		this.screen = ScreenMode.HVGA_PORTRAIT;
		this.layout = Layout.ABSOLUTE;
	}
	
	public void load() {
		java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
		this.snap = prefs.getBoolean(SNAP, false);
		
		screen = ScreenMode.values()[prefs.getInt(SCREEN, 4)];
		layout = Layout.values()[prefs.getInt(LAYOUT, 0)];
		
	}
	
	public void save() {
		java.util.prefs.Preferences prefs = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
		
		prefs.putBoolean(SNAP, snap);
		prefs.putInt(SCREEN, screen.ordinal());
		prefs.putInt(LAYOUT, layout.ordinal());
		
		try {
			prefs.sync();
		}
		catch (BackingStoreException ex) {
			AndroidEditor.instance().error(ex);
		}
	}
	
	public void setSnap(boolean value) {
		this.snap = value;
	}
	
	public boolean getSnap() {
		return snap;
	}
	
	public void setScreenMode(ScreenMode screen) {
		this.screen = screen;
	}
	
	public ScreenMode getScreenMode() {
		return screen;
	}
	
	public void setDefaultLayout(Layout l) {
		this.layout = l;
	}
	
	public Layout getDefaultLayout() {
		return layout;
	}
}
