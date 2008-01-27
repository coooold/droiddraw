package org.droiddraw.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

import org.droiddraw.AndroidEditor;

public class StringsPanel extends AbstractDataPanel {
	private static final long serialVersionUID = 1L;
	
	public StringsPanel() {
		super(new Class<?>[] {String.class, String.class});
		dataTable.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JTextField()));
		dataTable.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(new JTextField()));
	}
	
	protected int parentRowCount() {
		Hashtable<String,String> strings = AndroidEditor.instance().getStrings();
		return strings.size();
	}

	protected Object parentValueAt(int row, int col) {
		Hashtable<String,String> strings = AndroidEditor.instance().getStrings();
		ArrayList<String> sorted = Collections.list(strings.keys());
		Collections.sort(sorted);
		if (col == 0) {
			return sorted.get(row);
		}
		else if (col == 1) {
			return strings.get(sorted.get(row));
		}
		return null;
	}

	@Override
	protected void parentSetValueAt(Object value, int rowIndex, int columnIndex) {
		String key = (String)parentValueAt(rowIndex, 0);
		Hashtable<String,String> strings = AndroidEditor.instance().getStrings();
		if (columnIndex == 1) {
			strings.put(key, (String)value);
		}
		else {
			String val = strings.get(key);
			strings.remove(key);
			strings.put((String)value, val);
		}
	}
}
