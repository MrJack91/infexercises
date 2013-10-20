/**
 * 
 */
package ch.zhaw.dbru.inf3.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * @author Daniel Brun
 * 
 *         Table model to display a list of strings.
 */
public class ListTableModel extends AbstractTableModel {

	private List<String> secondCol;

	/**
	 * 
	 */
	public ListTableModel() {
		secondCol = new ArrayList<String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return secondCol.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int aRowIndex, int aColumnIndex) {
		switch (aColumnIndex) {
		case 0:
			return (aRowIndex + 1) + ":";
		case 1:
			return secondCol.get(aRowIndex);
		}
		return null;
	}

	public void setData(List<String> aList) {
		secondCol = aList;
	}
}
