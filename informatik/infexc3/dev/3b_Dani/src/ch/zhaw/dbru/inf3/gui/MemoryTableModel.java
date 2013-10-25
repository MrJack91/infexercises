/**
 * 
 */
package ch.zhaw.dbru.inf3.gui;

import java.util.BitSet;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.StringUtils;

import ch.zhaw.dbru.inf3.emulator.logic.BinaryUtils;
import ch.zhaw.dbru.inf3.memory.Memory;

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

	private static final int MEM_COLS = 10;
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
		colCount = (memory.getWidth() * MEM_COLS) + 1;
		rowCount = memory.getMaxAddrMem() / memory.getWidth() / MEM_COLS;
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
					+ (aRowIndex * MEM_COLS * memory.getWidth())
					+ "-#"
					+ ((aRowIndex + 1) * MEM_COLS * memory.getWidth() - memory
							.getWidth()) + ":";
		default: {
			int addr = (aRowIndex * MEM_COLS * memory.getWidth())
					+ (aColumnIndex - 1);
			System.out.println(addr + " " + BinaryUtils.createBitSetFromInt(addr,memory.getAddrWidth()));
			String retStr = StringUtils.reverse(
					BinaryUtils.convertBitSetToString(memory.getDataWithWidth(
							BinaryUtils.createBitSetFromInt(addr,memory.getAddrWidth()), 1)).substring(0, 9));

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
		return (aColumnIndex > 0) && aRowIndex > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
	 * int, int)
	 */
	@Override
	public void setValueAt(Object aValue, int aRowIndex, int aColumnIndex) {
		if (isCellEditable(aRowIndex, aColumnIndex)) {
			if (aValue instanceof String) {
				String value = (String) aValue;
				
				if (value.trim().equals("")) {
					value = "00000000";
				}

				BitSet res = null;
				if (!value.matches("(0*1*)*")) {
					try {
						Integer intVal = Integer.parseInt(value);
						res = BinaryUtils.createBitSetFromIntStandard(intVal);
					} catch (NumberFormatException e) {
						throw new IllegalArgumentException("Es sind nur Binärzahlen oder Dezimalzahlen erlaubt!", e);
					}
					
				}else{
					res = BinaryUtils.createBitSetFromStringStandard(StringUtils.reverse(value));
				}
				
				int addr = (aRowIndex * MEM_COLS * memory.getWidth())
						+ (aColumnIndex - 1);
				
				memory.setData(BinaryUtils.createBitSetFromIntStandard(addr), res);
			}
		}
	}

	/**
	 * @param aMemory
	 *            the memory to set
	 */
	public void setMemory(Memory aMemory) {
		memory = aMemory;
	}
}
