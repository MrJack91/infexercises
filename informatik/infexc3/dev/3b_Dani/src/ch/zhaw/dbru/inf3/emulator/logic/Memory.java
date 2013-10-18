/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator.logic;

import java.util.BitSet;

import ch.zhaw.dbru.inf3.emulator.MPCConstants;

/**
 * @author Daniel Brun
 * 
 */
public class Memory {

	private BitSet[] store;
	private BitSet initSet;

	/**
	 * Creates a new instance of 'Memory'.
	 */
	public Memory() {
		store = new BitSet[(int)Math.pow(2, MPCConstants.ADR_LENGTH)];

		initSet = new BitSet(MPCConstants.BF_LENGTH);
		
		for(int i = 0;i < store.length;i++){
			store[i] = (BitSet) initSet.clone();
		}
	}

	public BitSet getData(BitSet anAddr) {
		int convAddr = BinaryUtils.convertBitSetToInt(anAddr);

		if (convAddr > store.length) {
			// TODO: Throw exc
		}

		return store[convAddr];
	}

	public void setData(BitSet anAddr, BitSet someData) {
		int convAddr = BinaryUtils.convertBitSetToInt(anAddr);

		if (convAddr > store.length) {
			// TODO: Throw exc
		}
		
		store[convAddr] = someData;
	}
	
	public void setData(BitSet anAddr, BitSet[] someData) {
		int convAddr = BinaryUtils.convertBitSetToInt(anAddr);

		if (convAddr > store.length) {
			// TODO: Throw exc
		}
		
		for(int i = 0;i < someData.length;i++){
			store[convAddr + (i * MPCConstants.BF_W_LENGTH)] = someData[i];
		}
		
	}
}
