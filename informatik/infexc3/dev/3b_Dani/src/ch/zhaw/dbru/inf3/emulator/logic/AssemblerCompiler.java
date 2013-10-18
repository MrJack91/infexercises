/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator.logic;

import java.util.BitSet;
import java.util.List;

/**
 * @author Daniel Brun
 * 
 */
public class AssemblerCompiler {

	private static final String COMMENT_SIGN = ";";
	private CommandSet commandSet;

	/**
	 * 
	 */
	public AssemblerCompiler(CommandSet aCommandSet) {
		commandSet = aCommandSet;
		initialize();
	}

	private void initialize() {

	}

	public BitSet[] compile(List<String> someData) {
		BitSet[] compiledProg = new BitSet[someData.size()];

		for (int i = 0; i < someData.size(); i++) {
			compiledProg[i] = compileLine(someData.get(i));
		}

		return compiledProg;
	}

	private BitSet compileLine(String aProgLine) {
		String tmpPL = aProgLine;

		if (aProgLine.indexOf(COMMENT_SIGN) >= 0) {
			tmpPL = aProgLine.substring(1, aProgLine.indexOf(COMMENT_SIGN));
		}

		String[] splitted = tmpPL.split(" ");

		CommandEnum bf = CommandEnum.valueOf(CommandEnum.class,
				splitted[0].toUpperCase());

		if (bf == null) {
			// TODO: throw exception
		}

		Command command = commandSet.getComand(bf);

		BitSet bitCom = command.getStub();

		if (splitted.length > 1) {
			// Process arguments
			String[] argSplit = splitted[1].split(",");

			if (argSplit.length > 0) {

				int startOp1 = command.getStartOfType(Command.OPERAND_ONE);
				int lenOp1 = command.getLengthOfType(Command.OPERAND_ONE);

				if (startOp1 >= 0) {
					if (argSplit.length < 1) {
						// TODO: Throw exception
					}

					String op1 = argSplit[0];

					if (op1.startsWith("R") || op1.startsWith("#")) {
						op1 = op1.substring(1, op1.length());
					}

					int op1Int = Integer.valueOf(op1);

					BitSet op1BS = BinaryUtils.createBitSetFromInt(op1Int,
							lenOp1);

					for (int i = 0; i < op1BS.length(); i++) {
						bitCom.set(startOp1 + i, op1BS.get(i));
					}

				}

				int startOp2 = command.getStartOfType(Command.OPERAND_TWO);
				int lenOp2 = command.getLengthOfType(Command.OPERAND_TWO);

				if (startOp2 >= 0) {
					if (argSplit.length < 2) {
						// TODO: Throw exception
					}

					String op2 = argSplit[0];

					if (op2.startsWith("R") || op2.startsWith("#")) {
						op2 = op2.substring(1, op2.length());
					}

					int op2Int = Integer.valueOf(op2);

					BitSet op2BS = BinaryUtils.createBitSetFromInt(op2Int,
							lenOp2);

					for (int i = 0; i < op2BS.length(); i++) {
						bitCom.set(startOp1 + i, op2BS.get(i));
					}

				}

			}
		}

		return bitCom;
	}
}
