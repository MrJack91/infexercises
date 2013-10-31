/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import ch.zhaw.dbru.inf3.command.Command;
import ch.zhaw.dbru.inf3.command.CommandEnum;
import ch.zhaw.dbru.inf3.command.CommandSet;
import ch.zhaw.dbru.inf3.emulator.itf.EmulationController;
import ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler;
import ch.zhaw.dbru.inf3.emulator.logic.BinaryUtils;
import ch.zhaw.dbru.inf3.memory.Memory;

/**
 * @author Daniel Brun
 * 
 */
public class MiniPowerPC implements Runnable, EmulationController {

	private static final int THREAD_PAUSE = 2000;
	private Thread mpThread;
	private int currentMode = EmulationController.MODE_FAST;

	private Memory memory;
	private CommandSet commandSet;

	private BitSet commandCounter;
	private BitSet commandRegister;

	private BitSet[] registers;

	private Command baseCm;
	private boolean running;
	private boolean progEnd;
	private boolean reset;
	
	private boolean[] carryFlags;

	private List<EmulationHandler> handlers;

	/**
	 * 
	 */
	public MiniPowerPC(Memory aMemory) {
		memory = aMemory;

		handlers = new ArrayList<EmulationHandler>();

		commandSet = new CommandSet();

		running = false;
		carryFlags = new boolean[MPCConstants.REGISTER_COUNT];

		registers = new BitSet[MPCConstants.REGISTER_COUNT];

		for (int i = 0; i < registers.length; i++) {
			registers[i] = new BitSet(MPCConstants.BF_LENGTH);
			carryFlags[i] = false;
		}
		
		commandRegister = new BitSet(MPCConstants.BF_LENGTH);
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

			if (currentMode == MODE_SLOW) {
				updateHandlers();
				try {
					Thread.sleep(THREAD_PAUSE);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(currentMode == MODE_STEP){
				updateHandlers();
				running = false;
			}
		}

		if(reset){
			reset = false;
			reset();
		}
		
		if (currentMode == MODE_FAST) {
			updateHandlers();
		}

		mpThread = null;
	}

	/**
	 * Updates the content in all registered handlers.
	 */
	private void updateHandlers() {

		for (EmulationHandler handler : handlers) {
			handler.updateCommandCounter((BitSet) commandCounter.clone());
			handler.updateRegisters((BitSet[]) registers.clone());
			handler.updateCarryFlag(carryFlags[0]);
			handler.updateCommandRegister((BitSet) commandRegister.clone());
			handler.updateMemory(memory);
			handler.stepFinished();
			
			if(progEnd){
				handler.programmFinished();
			}
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
			result = new BitSet(lenOp);

			for (int i = 0; i < lenOp; i++) {
				result.set(i, commandRegister.get(startOp + lenOp - 1 - i));
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
		int regIndex = -1;
		CommandEnum ce = baseCm.getCommand();

		// http://de.wikipedia.org/wiki/Logische_Verschiebung
		// http://de.wikipedia.org/wiki/Arithmetische_Verschiebung#Arithmetische_Verschiebung
		switch (ce) {
		case CLR:
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);

			registers[regIndex].clear();
			carryFlags[regIndex] = false;
			break;
		case ADD:
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);

			carryFlags[0] = BinaryUtils.addBitSets(registers[0],
					registers[regIndex]);
			break;
		case ADDD:
			int length = baseCm.getLengthOfType(Command.OPERAND_ONE);
			BinaryUtils.expandBitSet(anOp1,length-1);
			carryFlags[0] = BinaryUtils.addBitSets(registers[0],anOp1);
			break;
		case INC:
			carryFlags[0] = BinaryUtils.addBitSets(registers[0],
					BinaryUtils.createBitSetFromIntStandard(1));
			break;
		case DEC:
			carryFlags[0] = BinaryUtils.addBitSets(registers[0],
					BinaryUtils.createBitSetFromIntStandard(-1));
			break;
		case LWDD:
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);
			registers[regIndex] = memory.getData(anOp2);
			break;
		case SWDD:
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);
			memory.setData(anOp2, registers[regIndex]);
			break;
		case SRA:
			carryFlags[0] = BinaryUtils.shiftRight(registers[0], true);
			break;
		case SLA:
			carryFlags[0] = BinaryUtils.shiftLeft(registers[0], true);
			break;
		case SRL:
			carryFlags[0] = BinaryUtils.shiftRight(registers[0], false);
			break;
		case SLL:
			carryFlags[0] = BinaryUtils.shiftLeft(registers[0], false);
			break;
		case AND:
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);
			registers[0].and(registers[regIndex]);
			break;
		case OR:
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);
			registers[0].or(registers[regIndex]);
			break;
		case NOT:
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);
			registers[0].andNot(registers[regIndex]);
			break;
		case BZ:
			incStep = 0;
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);

			if (BinaryUtils.compareBitSetToInt(registers[0], 0)) {
				commandCounter = registers[regIndex];
			}
			break;
		case BNZ:
			incStep = 0;
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);

			if (!BinaryUtils.compareBitSetToInt(registers[0], 0)) {
				commandCounter = registers[regIndex];
			}
			break;
		case BC:
			incStep = 0;
			regIndex = BinaryUtils.convertBitSetToInt(anOp1);

			if (carryFlags[0]) {
				commandCounter = registers[regIndex];
			}
			break;
		case B:
			incStep = 0;
			commandCounter = registers[regIndex];
			break;
		case BZD:
			incStep = 0;

			if (BinaryUtils.compareBitSetToInt(registers[0], 0)) {
				commandCounter = anOp1;
			}
			break;
		case BNZD:
			incStep = 0;

			if (!BinaryUtils.compareBitSetToInt(registers[0], 0)) {
				commandCounter = anOp1;
			}
			break;
		case BCD:
			incStep = 0;

			if (carryFlags[0]) {
				commandCounter = anOp1;
			}
			break;
		case BD:
			incStep = 0;
			commandCounter = anOp1;
			break;
		case END:
			progEnd = true;
			incStep = 0;
			running = false;
			break;
		default:
			// TODO: THROW Exception
			// TODO: Finish implementation
			break;
		}

		BinaryUtils.addBitSets(commandCounter,
				BinaryUtils.createBitSetFromIntStandard(incStep));
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
		progEnd = false;
		commandCounter = anAddr;
		updateHandlers();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.dbru.inf3.emulator.itf.EmulationController#step()
	 */
	@Override
	public void step() {

		if (running || mpThread != null) {
			// TODO: Throw Exception
		}

		mpThread = new Thread(this);
		mpThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.dbru.inf3.emulator.itf.EmulationController#registerHandler(ch
	 * .zhaw.dbru.inf3.emulator.itf.EmulationHandler)
	 */
	@Override
	public void registerHandler(EmulationHandler aHandler) {
		if (!handlers.contains(aHandler)) {
			handlers.add(aHandler);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.dbru.inf3.emulator.itf.EmulationController#loadProgramToMemory
	 * (java.util.BitSet[])
	 */
	@Override
	public BitSet loadProgramToMemory(BitSet[] someBinData) {
		BitSet addr = BinaryUtils.createBitSetFromInt(100,
				memory.getAddrWidth(),false);
		memory.setData(addr, someBinData);

		return addr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.zhaw.dbru.inf3.emulator.itf.EmulationController#getMemory()
	 */
	@Override
	public Memory getMemory() {
		return memory;
	}

	/* (non-Javadoc)
	 * @see ch.zhaw.dbru.inf3.emulator.itf.EmulationController#reset()
	 */
	@Override
	public void reset() {
		if (running && mpThread != null) {
			reset = true;
			running = false;
		}else{
			for (int i = 0; i < registers.length; i++) {
				registers[i].clear();
				carryFlags[i] = false;
			}
			
			commandRegister.clear();
			
			memory.clear();
			updateHandlers();
		}
	}

}
