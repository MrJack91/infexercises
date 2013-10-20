/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator.itf;

import java.util.BitSet;

import ch.zhaw.dbru.inf3.emulator.logic.Memory;

/**
 * @author Daniel Brun
 *
 */
public interface EmulationHandler {

	public void updateCommandCounter(BitSet aBfz);
	public void updateRegisters(BitSet[] someRegisters);
	public void updateCommandRegister(BitSet aCr);
	public void updateCarryFlag(boolean aFlag);
	public void stepFinished();
	public void updateMemory(Memory aMemory);
}
