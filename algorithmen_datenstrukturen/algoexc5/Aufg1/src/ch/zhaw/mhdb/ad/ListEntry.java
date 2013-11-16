/**
 * 
 */
package ch.zhaw.mhdb.ad;

/**
 * A List Entry with a reference to the next entry and the integer value field.
 *
 */
public class ListEntry {

	private Integer value;
	private ListEntry nextEntry;
	private ListEntry lastEntry;
	
	/**
	 * Creates a new entry with the given value.
	 * 
	 * @param aValue the value.
	 */
	public ListEntry(Integer aValue) {
		value = aValue;
		nextEntry = null;
		lastEntry = null;
	}
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param value The value.
	 * @param nextEntry The next entry.
	 * @param lastEntry The last entry.
	 */
	public ListEntry(Integer value, ListEntry nextEntry, ListEntry lastEntry) {
		super();
		this.value = value;
		this.nextEntry = nextEntry;
		this.lastEntry = lastEntry;
	}

	/**
	 * @return the nextEntry
	 */
	public ListEntry getNextEntry() {
		return nextEntry;
	}

	/**
	 * @param nextEntry the nextEntry to set
	 */
	public void setNextEntry(ListEntry nextEntry) {
		this.nextEntry = nextEntry;
	}

	/**
	 * @return the lastEntry
	 */
	public ListEntry getLastEntry() {
		return lastEntry;
	}

	/**
	 * @param lastEntry the lastEntry to set
	 */
	public void setLastEntry(ListEntry lastEntry) {
		this.lastEntry = lastEntry;
	}

	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		int hash = 37;
		
		hash = 53 * hash + (value == null ? 0 : value.hashCode());
		
		return hash;
	}

	@Override
	public boolean equals(Object anObject) {
		
		if(anObject == this){
			return true;
		}
		
		if(!(anObject instanceof ListEntry)){
			return false;
		}
		
		ListEntry entry = (ListEntry) anObject;
		
		return value.equals(entry.getValue());
	}
}
