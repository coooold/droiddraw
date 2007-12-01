package org.droiddraw;
import java.awt.Color;
import java.awt.Graphics;
import java.io.PrintWriter;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class EditView extends TextView {
	JCheckBox password;
	JComboBox capitalize;
	JCheckBox numeric;
	JCheckBox phone;
	JCheckBox autoText;
	JTextField digits;
	
	public EditView(String txt) {
		super(txt);
		// This is a hack and bad oo, I know...
		this.tagName="EditText";
		

		setSize(text.length()*8+12, fontSize+6);
		
		password = new JCheckBox("Password");
		capitalize = new JComboBox(new String[] {"Sentences", "Words"});
		numeric = new JCheckBox("Numeric");
		phone = new JCheckBox("Phone Number");
		autoText = new JCheckBox("Correct Spelling");
		digits = new JTextField("");
	}
	
	
	@Override
	public JPanel getEditorPanel() {
		if (p == null) {
			super.getEditorPanel();

			p.add(new JLabel("Valid Digits"));
			JPanel jp = new JPanel();
			jp.add(digits);
			p.add(jp);

			p.add(new JLabel("Capitalize"));
			p.add(capitalize);
			
			p.add(password);
			p.add(numeric);
			p.add(phone);
			p.add(autoText);
		}
		return p;
	}

	public void generate(PrintWriter pw) {
		if (autoText.isSelected())
			properties.put("android:autoText", "true");
		properties.put("android:capitalize", ((String)capitalize.getSelectedItem()).toLowerCase());
		if (password.isSelected())
			properties.put("android:password", "true");
		if (numeric.isSelected())
			properties.put("android:numeric", "true");
		if (phone.isSelected())
			properties.put("android:phoneNumber", "true");
		if (digits.getText().length() > 0) 
			properties.put("android:digits", digits.getText());
		
		super.generate(pw);
	}

	public void paint(Graphics g) {
		setSize(g.getFontMetrics(f).stringWidth(text)+12, fontSize+6);

		g.setColor(Color.white);
		g.fillRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		
		g.setColor(Color.darkGray);
		g.drawRoundRect(getX(), getY(), getWidth(), getHeight(), 8, 8);
		g.setFont(f);
		g.drawString(text, getX()+8, getY()+fontSize+2);
		g.drawLine(getX()+7, getY()+2, getX()+7, getY()+fontSize+2);
	}
}
