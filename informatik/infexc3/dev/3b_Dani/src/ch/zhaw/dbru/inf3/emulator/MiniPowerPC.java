/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import ch.zhaw.dbru.inf3.emulator.itf.EmulationController;
import ch.zhaw.dbru.inf3.emulator.logic.AssemblerCompiler;
import ch.zhaw.dbru.inf3.emulator.logic.BinaryUtils;
import ch.zhaw.dbru.inf3.emulator.logic.Command;
import ch.zhaw.dbru.inf3.emulator.logic.CommandEnum;
import ch.zhaw.dbru.inf3.emulator.logic.CommandSet;
import ch.zhaw.dbru.inf3.emulator.logic.Memory;

/**
 * @author Daniel Brun
 * 
 */
public class MiniPowerPC implements Runnable, EmulationController {

	private Thread mpThread;
	private int currentMode = EmulationController.MODE_FAST;

	private Memory memory;
	private CommandSet commandSet;

	private BitSet commandCounter;
	private BitSet commandRegister;

	private BitSet[] registers;
	
	private Command baseCm;
	private boolean running;
	
	private boolean[] carryFlags;

	/**
	 * 
	 */
	public MiniPowerPC(Memory aMemory) {
		memory = aMemory;

		commandSet = new CommandSet();
		mpThread = new Thread(this);
		running = false;
		carryFlags = new boolean[MPCConstants.REGISTER_COUNT];
		
		registers = new BitSet[MPCConstants.REGISTER_COUNT];
		
		for(int i = 0;i < registers.length;i++){
			registers[i] = new BitSet(MPCConstants.BF_LENGTH);
			carryFlags[i] = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		running = true;

		while (running) {
			loadCCCommandIntoCR();

			baseCm = decodeCommandInCR();

			if (baseCm == null) {
				// TODO: throw new Exception
			}

			BitSet operandOne = loadOperand(Command.OPERAND_ONE);
			BitSet operandTwo = loadOperand(Command.OPERAND_TWO);

			execCommand(operandOne, operandTwo);
		}

	}

	/**
	 * Loads the command which is referenced by the 'CommandCounter' into the
	 * 'CommandRegister'.
	 */
	private void loadCCCommandIntoCR() {
		// Load value which is referenced by bfz into bfr
		commandRegister = memory.getData(commandCounter);
	}

	/**
	 * Decodes the command in the 'CommandRegister'.
	 * 
	 * @return the base command or null if no command could be found.
	 */
	private Command decodeCommandInCR() {
		return commandSet.getCommand(commandRegister);
	}

	/**
	 * Loads and extracts the operand of the given type.
	 * 
	 * @param aCommand
	 *            the command to work on.
	 * @param aType
	 *            the operand type to load.
	 * @return the extracted binary operand.
	 */
	private BitSet loadOperand(short aType) {
		BitSet result = null;

		int startOp = baseCm.getStartOfType(aType);
		int lenOp = baseCm.getLengthOfType(aType);

		if (startOp > -1) {
			int posC = 0;
			result = new BitSet(lenOp);

			for (int i = startOp; i < startOp + lenOp; i++) {
				result.set(posC, commandRegister.get(i));
				posC++;
			}
		}

		return result;
	}

	/**
	 * Executes the command with the given operands
	 * 
	 * @param anOp1
	 *            the first operand.
	 * @param anOp2
	 *            the second operand.
	 */
	private void execCommand(BitSet anOp1, BitSet anOp2) {
		short incStep = MPCConstants.BF_W_LENGTH;
		
		CommandEnum ce = baseCm.getCommand();

		switch (ce) {
			case CLR:
				int index = BinaryUtils.convertBitSetToInt(anOp1);
				
				registers[index].clear();
				carryFlags[index] = false;
				break;
			case ADD:
				carryFlags[0] = BinaryUtils.addBitSets(registers[0],anOp1,carryFlags[0]);
				break;
			case END:
				running = false;
				break;
		default:
			//TODO: THROW Exception
			break;
		}

		BinaryUtils.addBitSets(commandCounter, BinaryUtils.createBitSetFromIntStandard(incStep), false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.dbru.inf3.emulator.itf.EmulationController#setMode(int)
	 */
	@Override
	public void setMode(int aMode) {
		currentMode = aMode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.dbru.inf3.emulator.itf.EmulationController#startProgramm(byte[])
	 */
	@Override
	public void startProgramm(BitSet anAddr) {
		commandCounter = anAddr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.dbru.inf3.emulator.itf.EmulationController#step()
	 */
	@Override
	public void step() {
		mpThread.start();
	}

	public static void main(String[] args) {
		CommandSet cms = new CommandSet();
		AssemblerCompiler ac = new AssemblerCompiler(cms);
		Memory memory = new Memory();
		
		List<String> prog = new ArrayList<String>();
		prog.add("CLR R0");
		prog.add("CLR R2");
		prog.add("ADD R3");
		prog.add("END");
		
		memory.setData(BinaryUtils.createBitSetFromIntStandard(100), ac.compile(prog));
		
		MiniPowerPC mpc = new MiniPowerPC(memory);
		
		mpc.startProgramm(BinaryUtils.createBitSetFromIntStandard(100));
		mpc.step();
	
	}
}
