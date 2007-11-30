package org.droiddraw;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.PrintWriter;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextView extends AbstractWidget {
	String text;
	int fontSize = 14;
	boolean isBold;
	boolean isItalic;
	String fontFace;
	
	JTextField textField;
	JTextField sizeField;
	JCheckBox bold;
	JCheckBox italic;
	JComboBox face;
	JPanel p;
	
	Font f;
	
	public TextView(String str) {
		super("TextView");
		text = str;
		buildFont();
		fontFace="plain";	
	}
	
	protected void buildFont() {
		f = new Font(fontFace,Font.PLAIN,fontSize);
		if (isBold) {
			f = f.deriveFont(f.getStyle() | Font.BOLD);
		}
		if (isItalic) {
			f = f.deriveFont(f.getStyle() | Font.ITALIC);
		}
	}
	
	public JPanel getEditorPanel() {
		if (p == null) {
			p = new JPanel();
			p.setLayout(new GridLayout(0,2));
			p.add(new JLabel("Text"));
			JPanel jp = new JPanel();
			textField = new JTextField(text, 10);
			jp.add(textField);
			p.add(jp);
			
			p.add(new JLabel("Font Size"));
			sizeField = new JTextField(fontSize+"", 10);
			jp = new JPanel();
			jp.add(sizeField);
			p.add(jp);
		
			face = new JComboBox(new String[] {"plain","sans","serif","monospace"});
			p.add(new JLabel("Font Face"));
			p.add(face);
			
			bold = new JCheckBox("Bold");
			italic = new JCheckBox("Italic");
			p.add(bold);
			p.add(italic);
		}
		return p;
	}

	public void apply() {
		text = textField.getText();
		isBold = bold.isSelected();
		isItalic = italic.isSelected();
		fontFace = (String)face.getSelectedItem();
		fontSize = Integer.parseInt(sizeField.getText());
		buildFont();
	}
	
	public void paint(Graphics g) {
		setSize(g.getFontMetrics(f).stringWidth(text)+5, fontSize+3);

		g.setColor(Color.black);
		g.setFont(f);
		g.drawString(text, getX()+2, getY()+fontSize);
	}
	

	public void generate(PrintWriter pw) {
		properties.put("android:layout_width", "wrap_content");
		properties.put("android:layout_height", "wrap_content");
		properties.put("android:layout_x", getX()+"px");
		properties.put("android:layout_y", getY()+"px");
		properties.put("android:typeface", fontFace);
		String style = "plain";
		if (isItalic && isBold) style = "bold_italic";
		else if (isBold) style = "bold";
		else if (isItalic) style = "italic";
		properties.put("android:textStyle", style);
		properties.put("android:text", text);
		super.generate(pw);
	}
}
