/**
 * 
 */
package ch.zhaw.dbru.inf3.command;

/**
 * @author Daniel Brun
 * 
 * Enum for the different assembler commands.
 */
public enum CommandEnum {

	CLR,

	ADD,

	ADDD,

	INC,

	DEC,
	
	LWDD,
	
	SWDD,

	END;

	/**
	 * Resolves the enum of the given name.
	 * 
	 * @param aName
	 *            the name to resolve.
	 * @return the resolved enum or null if no enum could be found.
	 */
	public static CommandEnum resolve(String aName) {
		CommandEnum cmdEum = null;

		if (aName != null) {
			String upName = aName.toUpperCase();

			for (CommandEnum tmpEnum : values()) {
				if (tmpEnum.toString().equals(upName)) {
					return tmpEnum;
				}
			}
		}

		return cmdEum;
	}
}
