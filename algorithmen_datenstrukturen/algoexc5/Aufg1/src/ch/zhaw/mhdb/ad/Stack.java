package ch.zhaw.mhdb.ad;

/**
 * @author Daniel Brun
 * 
 *         Stack implemented with a double chained list.
 */
public class Stack {

	private List list;

	/**
	 * Creates a new stack.
	 */
	public Stack() {
		list = new List();
	}

	/**
	 * Pushs a new value into the stack.
	 * 
	 * @param aValue
	 *            The value to push.
	 */
	public void push(Integer aValue) {
		list.addOnFirstPosition(aValue);
	}

	/**
	 * Pops the first element of the stack.
	 * 
	 * @return the first element of the stack.
	 */
	public Integer pop() {
		ListEntry entry = list.getFirstEntry();

		if (entry != null) {
			entry.getNextEntry().setLastEntry(entry.getLastEntry());
			entry.getLastEntry().setNextEntry(entry.getNextEntry());
			return entry.getValue();
		}

		return null;
	}

	/**
	 * The size of the stack.
	 * 
	 * @return the size.
	 */
	public int getSize() {
		return list.getSize();
	}

	/**
	 * Checks if the stack is empty.
	 * 
	 * @return True if the stack is empty.
	 */
	public boolean isEmpty() {
		return (getSize() == 0);
	}
}
