/**
 * 
 */
package ch.zhaw.dbru.inf3.compiler;

import java.util.BitSet;
import java.util.List;

import ch.zhaw.dbru.inf3.command.CommandEnum;
import ch.zhaw.dbru.inf3.command.CommandSet;
import ch.zhaw.dbru.inf3.compiler.exception.AssemblerCompilationException;
import ch.zhaw.dbru.inf3.emulator.logic.BinaryUtils;
import ch.zhaw.dbru.inf3.emulator.logic.Command;

/**
 * @author Daniel Brun
 * 
 *         Assembler Compiler
 */
public class AssemblerCompiler {

	/**
	 * Marks the beginning of code comments.
	 */
	private static final String COMMENT_SIGN = ";";
	private CommandSet commandSet;

	/**
	 * Creates a new Assembler based on the given command set.
	 */
	public AssemblerCompiler(CommandSet aCommandSet) {
		commandSet = aCommandSet;
	}

	/**
	 * Compiles the given data based on the command set.
	 * 
	 * @param someData
	 *            the data to compile.
	 * @return the compiled data as {@Link BitSet} Array.
	 */
	public BitSet[] compile(List<String> someData)
			throws AssemblerCompilationException {
		BitSet[] compiledProg = new BitSet[someData.size()];

		for (int i = 0; i < someData.size(); i++) {
			try {
				compiledProg[i] = compileLine(someData.get(i));
			} catch (AssemblerCompilationException e) {
				e.setCodeLine(i + 1);
				e.setCode(someData.get(i));
				throw e;
			}
		}

		return compiledProg;
	}

	/**
	 * Compiles the given line.
	 * 
	 * @param aProgLine
	 *            the line to compile
	 * @return the compiled line as {@link BitSet}.
	 */
	private BitSet compileLine(String aProgLine) {
		String tmpPL = aProgLine;

		// Cut off comments.
		if (aProgLine.indexOf(COMMENT_SIGN) >= 0) {
			tmpPL = aProgLine.substring(1, aProgLine.indexOf(COMMENT_SIGN));
		}

		// Split command.
		String[] splitted = tmpPL.split(" ");

		// Evaluate command.
		CommandEnum bf = CommandEnum.resolve(splitted[0].toUpperCase());

		if (bf == null) {
			throw new AssemblerCompilationException("Der Befehl '"
					+ splitted[0] + "' konnte nicht gefunden werden!");
		}

		// Get command and stub
		Command command = commandSet.getComand(bf);
		BitSet bitCom = command.getStub();

		// Process arguments
		if (splitted.length > 1) {
			String[] argSplit = splitted[1].split(",");

			// TODO: Refactoring
			if (argSplit.length > 0) {
				int startOp1 = command.getStartOfType(Command.OPERAND_ONE);
				int lenOp1 = command.getLengthOfType(Command.OPERAND_ONE);

				if (startOp1 >= 0) {
					if (argSplit.length < 1) {
						throw new AssemblerCompilationException(
								"Dieser Befehl erfordert einen Operanden!");
					}

					String op1 = argSplit[0];

					if (op1.startsWith("R") || op1.startsWith("#")) {
						op1 = op1.substring(1, op1.length());
					}

					int op1Int = Integer.valueOf(op1);

					BitSet op1BS = BinaryUtils.createBitSetFromInt(op1Int,
							lenOp1);

					if (op1BS.length() > lenOp1) {
						throw new AssemblerCompilationException(
								"Der erste Operand ist zu lange!");
					}

					for (int i = 0; i < op1BS.length(); i++) {
						bitCom.set(startOp1 + i, op1BS.get(i));
					}

				}

				if (splitted.length > 2) {
					argSplit = splitted[2].split(",");

					int startOp2 = command.getStartOfType(Command.OPERAND_TWO);
					int lenOp2 = command.getLengthOfType(Command.OPERAND_TWO);

					if (startOp2 >= 0) {
						if (argSplit.length < 1) {
							throw new AssemblerCompilationException(
									"Dieser Befehl erfordert einen zweiten Operanden!");
						}

						String op2 = argSplit[0];

						if (op2.startsWith("R") || op2.startsWith("#")) {
							op2 = op2.substring(1, op2.length());
						}

						int op2Int = Integer.valueOf(op2);

						BitSet op2BS = BinaryUtils.createBitSetFromInt(op2Int,
								lenOp2);

						if (op2BS.length() > lenOp2) {
							throw new AssemblerCompilationException(
									"Der zweite Operand ist zu lange!");
						}

						for (int i = 0; i < op2BS.length(); i++) {
							bitCom.set(startOp2 + i, op2BS.get(i));
						}

					}
				}

			}
		}

		return bitCom;
	}
}
