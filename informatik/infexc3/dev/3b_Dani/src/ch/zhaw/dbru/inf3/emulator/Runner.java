/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.dbru.inf3.command.CommandSet;
import ch.zhaw.dbru.inf3.compiler.AssemblerCompiler;
import ch.zhaw.dbru.inf3.emulator.logic.BinaryUtils;
import ch.zhaw.dbru.inf3.gui.GuiEmulator;
import ch.zhaw.dbru.inf3.memory.Memory;

/**
 * @author Daniel Brun
 * 
 *         Constructs and runs the 'Mini-Power-PC'.
 */
public class Runner {

	/**
	 * Creates a new instance.
	 */
	public Runner() {
		//TODO: Clean up
		Memory memory = new Memory(10, 2);
		CommandSet cms = new CommandSet();

		AssemblerCompiler ac = new AssemblerCompiler(cms);

		List<String> prog = new ArrayList<String>();
		prog.add("CLR R0");
		prog.add("CLR R2");
		prog.add("ADD R3");
		prog.add("END");

		memory.setData(BinaryUtils.createBitSetFromIntStandard(0),
				ac.compile(prog));

		MiniPowerPC mpc = new MiniPowerPC(memory);

		GuiEmulator guiEmu = new GuiEmulator(mpc, ac);

		// mpc.startProgramm(BinaryUtils.createBitSetFromIntStandard(100));
		// mpc.step();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Runner();
	}

}
