/**
 * 
 */
package ch.zhaw.dbru.inf3.compiler.exception;

/**
 * @author Daniel Brun
 *
 */
public class AssemblerCompilationException extends RuntimeException {

	/**
	 * Generated Serial Version UID.
	 */
	private static final long serialVersionUID = 3643354644979228526L;
	
	private int codeLine;
	private String code;
	
	
	/**
	 * Creates an new instance of this class.
	 * 
	 * @param aMessage the message.
	 */
	public AssemblerCompilationException(String aMessage) {
		super(aMessage);
	}
	/**
	 * @param aCode the code to set
	 */
	public void setCode(String aCode) {
		code = aCode;
	}
	/**
	 * @param aCodeLine the codeLine to set
	 */
	public void setCodeLine(int aCodeLine) {
		codeLine = aCodeLine;
	}
	/**
	 * @return the codeLine
	 */
	public int getCodeLine() {
		return codeLine;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	
	
}
