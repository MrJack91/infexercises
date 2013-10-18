/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator.logic;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 * @author Daniel Brun
 * 
 */
public class CommandSet {

	private HashMap<CommandEnum, Command> commands;

	/**
	 * Creates a new instance of this class.
	 */
	public CommandSet() {
		commands = new HashMap<CommandEnum, Command>();

		Command cmCLR = new Command(CommandEnum.CLR);
		cmCLR.allocateUsage(0, 4, Command.OPSCODE);
		cmCLR.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmCLR.allocateUsage(6, 9, Command.OPSCODE);
		cmCLR.setBits(false, false, false, false, false, false, true, false,
				true);
		commands.put(cmCLR.getCommand(), cmCLR);

		Command cmADD = new Command(CommandEnum.ADD);
		cmADD.allocateUsage(0, 4, Command.OPSCODE);
		cmADD.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmADD.allocateUsage(6, 9, Command.OPSCODE);
		cmADD.setBits(false, false, false, false, false, false, true, true,
				true);
		commands.put(cmADD.getCommand(), cmADD);

		Command cmADDD = new Command(CommandEnum.ADDD);
		cmADDD.allocateUsage(0, 1, Command.OPSCODE);
		cmADDD.allocateUsage(1, 16, Command.OPERAND_ONE);
		cmADDD.setBits(true);
		commands.put(cmADDD.getCommand(), cmADDD);

		Command cmINC = new Command(CommandEnum.INC);
		cmINC.allocateUsage(0, 9, Command.OPSCODE);
		cmINC.setBits(false, false, false, false, false, false, false, true);
		commands.put(cmINC.getCommand(), cmINC);

		Command cmDEC = new Command(CommandEnum.DEC);
		cmDEC.allocateUsage(0, 9, Command.OPSCODE);
		cmDEC.setBits(false, false, false, false, false, true);
		commands.put(cmDEC.getCommand(), cmDEC);
		
		
		
		
		Command cmEND = new Command(CommandEnum.END);
		cmEND.allocateUsage(0, 16, Command.OPSCODE);
		commands.put(cmEND.getCommand(), cmEND);
	}

	/**
	 * Gets the requested command.
	 * 
	 * @param aCommand
	 *            the command.
	 * @return the command or null if no command could be found.
	 */
	public Command getComand(CommandEnum aCommand) {
		return commands.get(aCommand);
	}

	/**
	 * Gets the comand to the given binary command.
	 * 
	 * @param aBinaryCommand
	 *            the binary command.
	 * @return the command or null if no matching command could be found.
	 */
	public Command getCommand(BitSet aBinaryCommand) {

		for (Entry<CommandEnum, Command> entry : commands.entrySet()) {
			if (entry.getValue().isMatchingCommand(aBinaryCommand)) {
				return entry.getValue();
			}
		}

		return null;
	}
}
