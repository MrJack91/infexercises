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
	
	/**
	 * Creates a new entry with the given value.
	 * 
	 * @param aValue the value.
	 */
	public ListEntry(Integer aValue) {
		value = aValue;
		nextEntry = null;
	}
	
	/**
	 * Creates a new entry with the given value and the given successor.
	 * 
	 * @param aValue the value.
	 * @param aNextEntry the next entry.
	 */
	public ListEntry(Integer aValue, ListEntry aNextEntry) {
		value = aValue;
		nextEntry = aNextEntry;
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
