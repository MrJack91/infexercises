/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator.itf;

import java.util.BitSet;

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
}
