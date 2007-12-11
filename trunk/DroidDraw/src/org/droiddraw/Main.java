package org.droiddraw;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Main {
	
	protected static void loadImage(String name) 
		throws IOException
	{
		BufferedImage img = ImageIO.read(new File("src"+File.separator+"ui"+File.separator+name+".png"));
		ImageResources.instance().addImage(img, name);
	}
	
	public static void main(String[] args) 
		throws IOException
	{
		JFrame jf = new JFrame("DroidDraw");
		
		loadImage("emu1");
		loadImage("emu2");
		loadImage("emu3");
		loadImage("emu4");
		loadImage("checkbox_off_background");
		loadImage("checkbox_on_background");
		loadImage("clock_dial");
		loadImage("clock_hand_hour");
		loadImage("clock_hand_minute");
		loadImage("radiobutton_off_background");
		loadImage("radiobutton_on_background");
		loadImage("button_background_normal.9");
		loadImage("editbox_background_normal.9");
		loadImage("progress_circular_background");
		loadImage("progress_particle");
		loadImage("progress_circular_indeterminate");
		loadImage("arrow_up_float");
		loadImage("arrow_down_float");
		
		jf.getContentPane().add(new DroidDrawPanel("qvga"));
		jf.pack();
		jf.setVisible(true);
	}
}
