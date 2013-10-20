/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator.logic;

import java.util.BitSet;

import ch.zhaw.dbru.inf3.command.CommandEnum;
import ch.zhaw.dbru.inf3.emulator.MPCConstants;

/**
 * @author Daniel Brun
 *
 * TODO: Only one operand per type, etc.
 */
public class Command {

	public static final short OPSCODE = 1;
	public static final short OPERAND_ONE = 2;
	public static final short OPERAND_TWO = 3;
	public static final short NOTUSED = 4;
	
	private CommandEnum command;
	
	private short[] allocation;
	
	private BitSet bitCommand;
	
	/**
	 * Creates a new instance for the given command enum.
	 * 
	 * @param aCommandEnum the command enum.
	 */
	public Command(CommandEnum aCommandEnum) {
		allocation = new short[MPCConstants.BF_LENGTH];
		command = aCommandEnum;
		
		bitCommand = new BitSet(MPCConstants.BF_LENGTH);
		
		
		for(int i = 0;i < allocation.length;i++){
			allocation[i] = NOTUSED;
		}
	}

	public void allocateUsage(int aStartPos, int anEndPos, short aType){
		
		if(anEndPos >= allocation.length){
			//TODO: Throw exception.
		}
		
		for(int i = aStartPos;i < anEndPos;i++){
			allocation[i] = aType;
		}
	}
	
	public void setBits(boolean... bits){
		for(int i = 0;i < bits.length;i++){
			bitCommand.set(i,bits[i]);
		}
	}

	/**
	 * @return the command
	 */
	public CommandEnum getCommand() {
		return command;
	}

	/**
	 * @return the bit stub of the command
	 */
	public BitSet getStub() {
		return (BitSet) bitCommand.clone();
	}

	/**
	 * @param aType
	 * @return
	 */
	public int getStartOfType(short aType) {
		for(int i = 0;i < allocation.length;i++){
			if(allocation[i] == aType){
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * @param aType
	 * @return
	 */
	public int getLengthOfType(short aType) {
		int length = 0;
		int lastType = -1;
		
		for(int i = 0;i < allocation.length;i++){
			if(allocation[i] != lastType && lastType == aType){
				return length;
			}
			
			if(allocation[i] == aType){
				length++;
			}
			
			lastType = allocation[i];
		}
		
		return length;
	}

	/**
	 * Checks if the given binary command matches this comman.d
	 * 
	 * @param aBinaryCommand the binary command to compare.
	 * @return true if the command matches.
	 */
	public boolean isMatchingCommand(BitSet aBinaryCommand) {
		
		boolean matching = false;
		
		for(int i = 0;i < allocation.length;i++){
			if(allocation[i] == OPSCODE){
				if(bitCommand.get(i) == aBinaryCommand.get(i)){
					matching = true;
				}else{
					return false;
				}
			}
		}
		
		return matching;
	}
}
