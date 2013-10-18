/**
 * 
 */
package ch.zhaw.mhdb.ad;

/**
 * List to store integer values. Null values are not permitted.
 * 
 */
public class List {

	private ListEntry firstEntry;

	/**
	 * Creates a new list.
	 */
	public List() {
		firstEntry = null;
	}

	/**
	 * Returns the first element of the list.
	 * 
	 * @return the element or null if the list is empty.
	 */
	public Integer getFirstElement() {

		if (firstEntry == null) {
			return null;
		}

		return firstEntry.getValue();
	}

	/**
	 * Returns the first element of the list.
	 * 
	 * @return the element or null if the list is empty.
	 */
	protected ListEntry getFirstEntry(){
		if (firstEntry == null) {
			return null;
		}

		return firstEntry;
	}
	
	/**
	 * Returns the last element of the list.
	 * 
	 * @return the element or null if the list is empty.
	 */
	public Integer getLastElement() {

		if (firstEntry == null) {
			return null;
		}

		ListEntry entry = firstEntry;
		while ((entry = entry.getNextEntry()) != null) {

			if (entry.getNextEntry() == null) {
				return entry.getValue();
			}
		}

		return null;
	}

	/**
	 * Adds the given integer to the beginning of the list.
	 * 
	 * @param anInteger
	 *            the integer to add.
	 */
	public void addOnFirstPosition(Integer anInteger) {
		firstEntry = new ListEntry(anInteger, firstEntry);
	}

	/**
	 * Adds the given integer to the end of the list.
	 * 
	 * @param anInteger
	 *            the integer to add.
	 */
	public void addOnLastPosition(Integer anInteger) {
		ListEntry entry = firstEntry;
		while ((entry = entry.getNextEntry()) != null) {

			if (entry.getNextEntry() == null) {
				entry.setNextEntry(new ListEntry(anInteger));
				break;
			}
		}
	}

	/**
	 * Checks if the list contains the given element.
	 * 
	 * @param anInteger
	 *            the integer to check.
	 * @return true if the list contains the element, false otherwise.
	 */
	public boolean containsElement(Integer anInteger) {

		ListEntry entry = firstEntry;
		while ((entry = entry.getNextEntry()) != null) {

			if (entry.getValue().equals(anInteger)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the size of the list.
	 * 
	 * @return the size of the list.
	 */
	public int getSize() {
		int count = 1;

		if (firstEntry == null) {
			return 0;
		}

		ListEntry entry = firstEntry;
		while ((entry = entry.getNextEntry()) != null) {
			count++;
		}

		return count;
	}

	@Override
	public int hashCode() {
		int hash = 59;
		
		ListEntry entry = firstEntry;
		while ((entry = entry.getNextEntry()) != null) {
			hash = 61 * hash + entry.hashCode();
		}

		return hash;
	}

	@Override
	public boolean equals(Object anObject) {
		
		if(anObject == this){
			return true;
		}
		
		if(!(anObject instanceof List)){
			return false;
		}
		
		List aList = (List) anObject;
		
		if(firstEntry != null && aList != null){
			if(!firstEntry.equals(aList.getFirstEntry())){
				return false;
			}
			ListEntry entry = firstEntry;
			ListEntry aEntry = aList.getFirstEntry();
			while ((entry = entry.getNextEntry()) != null) {
				aEntry = aEntry.getNextEntry();
				
				if(!entry.equals(aEntry)){
					return false;
				}
				
			}
		}
		
		return true;
	}
}
