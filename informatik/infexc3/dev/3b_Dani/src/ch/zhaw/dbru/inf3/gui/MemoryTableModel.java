/**
 * 
 */
package ch.zhaw.dbru.inf3.gui;

import javax.swing.table.AbstractTableModel;

import ch.zhaw.dbru.inf3.emulator.logic.BinaryUtils;
import ch.zhaw.dbru.inf3.emulator.logic.Memory;

/**
 * @author Daniel Brun
 * 
 *         Table model to display a list of strings.
 */
/**
 * @author Daniel Brun
 * 
 */
public class MemoryTableModel extends AbstractTableModel {

	/**
	 * Generated Serial Version UID.
	 */
	private static final long serialVersionUID = 3352370284875596307L;

	private Memory memory;
	private int colCount;
	private int rowCount;

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param aMemory
	 *            the base memory.
	 */
	public MemoryTableModel(Memory aMemory) {
		memory = aMemory;
		// TODO: memory.getWidthInByte() * 10 + 1
		colCount = 10 + 1;
		rowCount = memory.getTotalBytes() / memory.getWidthInByte() / 10;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return rowCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return colCount;
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
			return "#"
					+ (aRowIndex * 10 * memory.getWidthInByte())
					+ "-#"
					+ ((aRowIndex + 1) * 10 * memory.getWidthInByte() - memory
							.getWidthInByte()) + ":";
		default: {
			// TODO: Evtl change to 10 * memory.get... (2)
			int addr = (aRowIndex * 10) + (aColumnIndex - 1) * 2;

			String retStr = BinaryUtils.convertBitSetToString(memory
					.getData(BinaryUtils.createBitSetFromInt(addr,
							memory.getAddrWidth())));

			if (retStr.matches("0+")) {
				retStr = "";
			}
			return retStr;
		}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
	 */
	@Override
	public boolean isCellEditable(int aRowIndex, int aColumnIndex) {
		return (aColumnIndex > 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int aRowIndex, int aColumnIndex) {
		// TODO Implement
		super.setValueAt(aValue, aRowIndex, aColumnIndex);
	}

	/**
	 * @param aMemory
	 *            the memory to set
	 */
	public void setMemory(Memory aMemory) {
		memory = aMemory;
	}
}
