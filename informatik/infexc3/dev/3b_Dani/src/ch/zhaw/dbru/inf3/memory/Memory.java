/**
 * 
 */
package ch.zhaw.dbru.inf3.memory;

import java.util.BitSet;

import ch.zhaw.dbru.inf3.emulator.logic.BinaryUtils;
import ch.zhaw.dbru.inf3.memory.exception.MemoryException;

/**
 * @author Daniel Brun
 * 
 */
public class Memory {

	private int addrWidth;
	private int maxAddrMem;
	private int width;

	private byte[] store;

	/**
	 * 
	 */
	/**
	 * Creates a new instance of 'Memory'.
	 * 
	 * @param anAddressWidth
	 *            the width of the 'address' (Max. addressable memory).
	 * @param aWidth
	 *            the memory cell width of the returned data.
	 */
	public Memory(int anAddressWidth, int aWidth) {
		addrWidth = anAddressWidth;
		width = aWidth;

		maxAddrMem = (int) Math.pow(2, addrWidth);

		store = new byte[maxAddrMem];
	}

	/**
	 * Gets the data on the given address.
	 * 
	 * @param anAddr
	 *            the address to access.
	 * @return the data
	 */
	public BitSet getData(BitSet anAddr) {
		return getDataWithWidth(anAddr, width);
	}
	
	/**
	 * Gets the data on the given address.
	 * 
	 * @param anAddr
	 *            the address to access.
	 * @param the width to get.
	 * @return the data with the given width.
	 */
	public BitSet getDataWithWidth(BitSet anAddr, int aWidth) {
		int convAddr = BinaryUtils.convertBitSetToInt(anAddr, addrWidth);

		if (convAddr >= store.length) {
			throw new MemoryException("Die addressierte Adresse '"
					+ BinaryUtils.convertBitSetToString(anAddr,addrWidth) + "' ("
					+ convAddr
					+ ") befindet sich ausserhalb des Speicherbereiches!");
		}

		byte[] retByte = new byte[aWidth];

		for (int i = 0; i < aWidth; i++) {
			retByte[i] = store[convAddr + i];
		}

		return BitSet.valueOf(retByte);
	}

	/**
	 * Sets the data to the given address.
	 * 
	 * @param anAddr
	 *            the address to access.
	 * @param someData
	 *            the data to store.
	 */
	public void setData(BitSet anAddr, BitSet someData) {
		setData(anAddr, someData, width);
	}
	
	/**
	 * Sets the data to the given address.
	 * 
	 * @param anAddr
	 *            the address to access.
	 * @param aWidth the width to use.
	 * @param someData
	 *            the data to store.
	 */
	public void setData(BitSet anAddr, BitSet someData, int aWidth) {
		int convAddr = BinaryUtils.convertBitSetToInt(anAddr,addrWidth);

		if (convAddr > store.length) {
			throw new MemoryException("Die addressierte Adresse '"
					+ BinaryUtils.convertBitSetToString(anAddr,addrWidth) + "' ("
					+ convAddr
					+ ") befindet sich ausserhalb des Speicherbereiches!");
		}

		byte[] tmp = someData.toByteArray();

		for (int i = 0; i < aWidth; i++) {
			if(i < tmp.length){
				store[convAddr + i] = tmp[i];
			}else{
				store[convAddr + i] = 00000000;
			}
		}
	}

	/**
	 * Sets the data array to the given address.
	 * 
	 * @param anAddr
	 *            the address to access.
	 * @param someData
	 *            the data to store.
	 */
	public void setData(BitSet anAddr, BitSet[] someData) {
		int convAddr = BinaryUtils.convertBitSetToInt(anAddr,addrWidth);

		if (convAddr > store.length) {
			throw new MemoryException("Die addressierte Adresse '"
					+ BinaryUtils.convertBitSetToString(anAddr,addrWidth) + "' ("
					+ convAddr
					+ ") befindet sich ausserhalb des Speicherbereiches!");
		}

		for (int i = 0; i < someData.length; i++) {
			setData(BinaryUtils.createBitSetFromIntStandard(convAddr + (i * width)),someData[i]);
		}
	}

	/**
	 * @return the addrWidth
	 */
	public int getAddrWidth() {
		return addrWidth;
	}

	/**
	 * @return the maxAddrMem
	 */
	public int getMaxAddrMem() {
		return maxAddrMem;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	public int getFullWidth(){
		return width * 8;
	}
}
