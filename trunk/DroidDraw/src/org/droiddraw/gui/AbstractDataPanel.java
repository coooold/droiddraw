package org.droiddraw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

public abstract class AbstractDataPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private class DataTableModel extends AbstractTableModel {
		private static final long serialVersionUID = 1L;
		private Class<?>[] classes;
		
		public DataTableModel(Class<?>[] classes) {
			this.classes = classes;
		}

		public int getColumnCount() {
			return classes.length;
		}
		
		public boolean isCellEditable(int row, int col) {
			return true;
		}

		
		@Override
		public Class<?> getColumnClass(int col) {
			return classes[col];
		}

		@Override
		public String getColumnName(int col) {
			if (col == 0) {
				return "Name";
			}
			else {
				return "Value";
			}
		}

		public int getRowCount() {
			return parentRowCount();
		}

		public Object getValueAt(int row, int col) {
			return parentValueAt(row, col);
		}

		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			parentSetValueAt(value, rowIndex, columnIndex);
		}
		
		
	};
		
	Dimension d;
	
	JTable dataTable;
	JButton save;
	JButton create;
	JButton delete;
	
	public AbstractDataPanel(Class<?>[] classes) {
		d = new Dimension(500, 300);
		dataTable = new JTable(new DataTableModel(classes));
		dataTable.setShowHorizontalLines(true);
		//dataTable.setShowGrid(true);
		
		setLayout(new BorderLayout());
		add(dataTable, BorderLayout.CENTER);
		
		JPanel jp = new JPanel();
		jp.setLayout(new FlowLayout());
		JButton save;
		JButton create;
		JButton delete;
		
		save = new JButton("Save");
		create = new JButton("New");
		delete = new JButton("Delete");
		
		jp.add(save);
		jp.add(create);
		jp.add(delete);
		add(jp, BorderLayout.SOUTH);
	}
	
	public Dimension getPreferredSize() {
		return d;
	}
	
	public Dimension getMinimumSize() {
		return d;
	}
	
	protected abstract int parentRowCount();
	protected abstract Object parentValueAt(int row, int col);
	protected abstract void parentSetValueAt(Object value, int rowIndex, int columnIndex);
}
