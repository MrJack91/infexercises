/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator.itf;

import java.util.BitSet;

import ch.zhaw.dbru.inf3.memory.Memory;

/**
 * @author Daniel Brun
 *
 */
public interface EmulationController {

	public static final int MODE_SLOW = 0;
	public static final int MODE_STEP = 1;
	public static final int MODE_FAST = 2;
	
	public void setMode(int aMode);
	
	public void startProgramm(BitSet anAddress);
	
	public void step();
	
	public void registerHandler(EmulationHandler aHandler);

	/**
	 * @param aBinData
	 * @return
	 */
	public BitSet loadProgramToMemory(BitSet[] someBinData);
	
	public Memory getMemory();
}
