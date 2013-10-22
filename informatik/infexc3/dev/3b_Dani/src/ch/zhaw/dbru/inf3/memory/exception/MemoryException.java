/**
 * 
 */
package ch.zhaw.dbru.inf3.memory.exception;

/**
 * @author Daniel Brun
 *
 * A Memory-Exception
 */
public class MemoryException extends RuntimeException {

	/**
	 * Generated Serial Version UID.
	 */
	private static final long serialVersionUID = 4612009484020301540L;
	
	/**
	 * Creates an new instance of this class.
	 * 
	 * @param aMessage the message.
	 */
	public MemoryException(String aMessage) {
		super(aMessage);
	}
	
}
