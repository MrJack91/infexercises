/**
 * 
 */
package ch.zhaw.dbru.inf3.command;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map.Entry;

import ch.zhaw.dbru.inf3.emulator.logic.Command;

/**
 * @author Daniel Brun
 * 
 *         Assembler command set. Used for the assembler compiler and the
 *         'Mini-Power-PC'.
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

		Command cmLWDD = new Command(CommandEnum.LWDD);
		cmLWDD.allocateUsage(0, 3, Command.OPSCODE);
		cmLWDD.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmLWDD.allocateUsage(6, 16, Command.OPERAND_TWO);
		cmLWDD.setBits(false, true);
		commands.put(cmLWDD.getCommand(), cmLWDD);

		Command cmSWDD = new Command(CommandEnum.SWDD);
		cmSWDD.allocateUsage(0, 3, Command.OPSCODE);
		cmSWDD.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmSWDD.allocateUsage(6, 16, Command.OPERAND_TWO);
		cmSWDD.setBits(false, true, true);
		commands.put(cmSWDD.getCommand(), cmSWDD);

		Command cmSRA = new Command(CommandEnum.SRA);
		cmSRA.allocateUsage(0, 9, Command.OPSCODE);
		cmSRA.setBits(false, false, false, false, false, true, false, true);
		commands.put(cmSRA.getCommand(), cmSRA);

		Command cmSLA = new Command(CommandEnum.SLA);
		cmSLA.allocateUsage(0, 9, Command.OPSCODE);
		cmSLA.setBits(false, false, false, false, true);
		commands.put(cmSLA.getCommand(), cmSLA);

		Command cmSRL = new Command(CommandEnum.SRL);
		cmSRL.allocateUsage(0, 9, Command.OPSCODE);
		cmSRL.setBits(false, false, false, false, true,false,false,true);
		commands.put(cmSRL.getCommand(), cmSRL);

		Command cmSLL = new Command(CommandEnum.SLL);
		cmSLL.allocateUsage(0, 9, Command.OPSCODE);
		cmSLL.setBits(false, false, false, false, true,true);
		commands.put(cmSLL.getCommand(), cmSLL);
		
		Command cmAnd = new Command(CommandEnum.AND);
		cmAnd.allocateUsage(0, 4, Command.OPSCODE);
		cmAnd.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmAnd.allocateUsage(6, 9, Command.OPSCODE);
		cmAnd.setBits(false, false, false, false, false,false,true);
		commands.put(cmAnd.getCommand(), cmAnd);
		
		Command cmOr = new Command(CommandEnum.OR);
		cmOr.allocateUsage(0, 4, Command.OPSCODE);
		cmOr.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmOr.allocateUsage(6, 9, Command.OPSCODE);
		cmOr.setBits(false, false, false, false, false,false,true,true);
		commands.put(cmOr.getCommand(), cmOr);
		
		Command cmNot = new Command(CommandEnum.NOT);
		cmNot.allocateUsage(0, 9, Command.OPSCODE);
		cmNot.setBits(false, false, false, false, false,false,false,false,true);
		commands.put(cmNot.getCommand(), cmNot);
		
		Command cmBZ = new Command(CommandEnum.BZ);
		cmBZ.allocateUsage(0, 4, Command.OPSCODE);
		cmBZ.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmBZ.allocateUsage(6, 8, Command.OPSCODE);
		cmBZ.setBits(false, false, false, true, false,false,true);
		commands.put(cmBZ.getCommand(), cmBZ);
		
		Command cmBNZ = new Command(CommandEnum.BNZ);
		cmBNZ.allocateUsage(0, 4, Command.OPSCODE);
		cmBNZ.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmBNZ.allocateUsage(6, 8, Command.OPSCODE);
		cmBNZ.setBits(false, false, false, true, false,false,false,true);
		commands.put(cmBNZ.getCommand(), cmBNZ);
		
		Command cmBC = new Command(CommandEnum.BC);
		cmBC.allocateUsage(0, 4, Command.OPSCODE);
		cmBC.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmBC.allocateUsage(6, 8, Command.OPSCODE);
		cmBC.setBits(false, false, false, true, false,false,true,true);
		commands.put(cmBC.getCommand(), cmBC);
		
		Command cmB = new Command(CommandEnum.B);
		cmB.allocateUsage(0, 4, Command.OPSCODE);
		cmB.allocateUsage(4, 6, Command.OPERAND_ONE);
		cmB.allocateUsage(6, 8, Command.OPSCODE);
		cmB.setBits(false, false, false, true);
		commands.put(cmB.getCommand(), cmB);
		
		Command cmBZD = new Command(CommandEnum.BZD);
		cmBZD.allocateUsage(0, 5, Command.OPSCODE);
		cmBZD.setBits(false, false, true, true);
		commands.put(cmBZD.getCommand(), cmBZD);
		
		Command cmBNZD = new Command(CommandEnum.BNZD);
		cmBNZD.allocateUsage(0, 5, Command.OPSCODE);
		cmBNZD.setBits(false, false, true, false,true);
		commands.put(cmBNZD.getCommand(), cmBNZD);
		
		Command cmBCD = new Command(CommandEnum.BCD);
		cmBCD.allocateUsage(0, 5, Command.OPSCODE);
		cmBCD.setBits(false, false, true, true,true);
		commands.put(cmBCD.getCommand(), cmBCD);
		
		Command cmBD = new Command(CommandEnum.BD);
		cmBD.allocateUsage(0, 5, Command.OPSCODE);
		cmBD.setBits(false, false, true, true,true);
		commands.put(cmBD.getCommand(), cmBD);

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
	 * Gets the command which corresponds to the given binary command.
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
