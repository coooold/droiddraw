package org.droiddraw;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;
import javax.swing.UIManager;

import org.droiddraw.gui.DroidDrawPanel;
import org.droiddraw.gui.ImageResources;
import org.droiddraw.widget.Layout;

public class DroidDraw extends JApplet implements URLOpener {
	private static final long serialVersionUID = 1L;
	
	protected static final void switchToLookAndFeel(String clazz) {
		try {
			UIManager.setLookAndFeel(clazz);
		} catch (Exception ex) {
			AndroidEditor.instance().error(ex);
		}
	}
	
	public void openURL(String url) {
		try {
			getAppletContext().showDocument(new URL(url), "_blank");
		} 
		catch (MalformedURLException ex) {
			AndroidEditor.instance().error(ex);
		}
	}
	
	protected static final void setupRootLayout(Layout l) {
		l.setPosition(AndroidEditor.OFFSET_X,AndroidEditor.OFFSET_Y);
		l.setPropertyByAttName("android:layout_width", "fill_parent");
		l.setPropertyByAttName("android:layout_height", "fill_parent");
		l.apply();
	}
	
	MediaTracker md;
	int ix;
	
	protected void loadImage(String name) {
		Image img = getImage(getCodeBase(), "ui/"+name+".png");
		md.addImage(img, ix++);
		ImageResources.instance().addImage(img, name);
		
	}
	
	@Override
	public void init() {
		super.init();
		AndroidEditor.instance().setURLOpener(this);
		// This is so that I can test out the Google examples...
		// START
		/*
		if ("true".equals(this.getParameter("load_strings"))) {
			try {
				URL url = new URL(getCodeBase(), "strings.xml");
				AndroidEditor.instance().setStrings(StringHandler.load(url.openStream()));
			} catch (Exception ex) {
				AndroidEditor.instance().error(ex);
			}
		}*/
		// END
		
		String screen = this.getParameter("Screen");
		if (screen == null) {
			screen="hvgap";
		}
		md = new MediaTracker(this);
		ix = 0;
		
		loadImage("emu1");
		loadImage("emu2");
		loadImage("emu3");
		loadImage("emu4");
		loadImage("paint");
		loadImage("droiddraw_small");
		loadImage("paypal");
		
		loadImage("background_01p");
		loadImage("background_01l");
		
		loadImage("statusbar_background_p");
		loadImage("statusbar_background_l");
		
		loadImage("title_bar.9");
		loadImage("stat_sys_data_connected");
		loadImage("stat_sys_battery_charge_100");
		loadImage("stat_sys_signal_3");
		
		loadImage("light/checkbox_off_background");
		loadImage("light/checkbox_on_background");
		loadImage("light/clock_dial");
		loadImage("light/clock_hand_hour");
		loadImage("light/clock_hand_minute");
		loadImage("light/radiobutton_off_background");
		loadImage("light/radiobutton_on_background");
		loadImage("light/button_background_normal.9");
		loadImage("light/editbox_background_normal.9");
		loadImage("light/progress_circular_background");
		loadImage("light/progress_particle");
		loadImage("light/progress_circular_indeterminate");
		loadImage("light/arrow_up_float");
		loadImage("light/arrow_down_float");
		loadImage("light/spinnerbox_background_focus_yellow.9");
		loadImage("light/spinnerbox_arrow_middle.9");
		
		loadImage("def/btn_check_off");
		loadImage("def/btn_check_on");
		
		loadImage("def/btn_radio_off");
		loadImage("def/btn_radio_on");
		
		loadImage("def/textfield.9");
		loadImage("def/btn_default_normal.9");
		loadImage("def/progress_wheel_medium");
		
		loadImage("def/spinner_normal.9");
		loadImage("def/btn_dropdown_neither.9");
		
		
		for (int i=0;i<ix;i++) {
			try {
				md.waitForID(i);
			} catch (InterruptedException ex) {}
		}
		setLayout(new BorderLayout());
		setSize(1024, 650);
		add(new DroidDrawPanel(screen, true), BorderLayout.CENTER);
	}
}
