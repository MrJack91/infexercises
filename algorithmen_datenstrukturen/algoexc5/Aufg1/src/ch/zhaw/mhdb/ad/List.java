/**
 * 
 */
package ch.zhaw.mhdb.ad;

/**
 * List to store integer values. Null values are not permitted.
 * 
 */
public class List {

	private ListEntry guardEntry;

	/**
	 * Creates a new list.
	 */
	public List() {
		guardEntry = new ListEntry(null);
		guardEntry.setNextEntry(guardEntry);
		guardEntry.setLastEntry(guardEntry);
	}

	/**
	 * Returns the first element of the list.
	 * 
	 * @return the element or null if the list is empty.
	 */
	public Integer getFirstElement() {

		if (guardEntry.getNextEntry() == null) {
			return null;
		}

		return guardEntry.getNextEntry().getValue();
	}

	/**
	 * Returns the first element of the list.
	 * 
	 * @return the element or null if the list is empty.
	 */
	protected ListEntry getFirstEntry() {
		if (guardEntry.getNextEntry() == null) {
			return null;
		}

		return guardEntry.getNextEntry();
	}

	/**
	 * Returns the last element of the list.
	 * 
	 * @return the element or null if the list is empty.
	 */
	public Integer getLastElement() {

		if (guardEntry.getLastEntry() == null) {
			return null;
		}

		return guardEntry.getLastEntry().getValue();
	}

	/**
	 * Returns the last entry of the list.
	 * 
	 * @return the entry or null if the list is empty.
	 */
	public ListEntry getLastEntry() {

		return guardEntry.getLastEntry();
	}

	/**
	 * Adds the given integer to the beginning of the list.
	 * 
	 * @param anInteger
	 *            the integer to add.
	 */
	public void addOnFirstPosition(Integer anInteger) {
		ListEntry entry = new ListEntry(anInteger, guardEntry.getNextEntry(),
				guardEntry);

		if (guardEntry.getLastEntry() == null
				&& guardEntry.getNextEntry() == null) {
			guardEntry.setLastEntry(entry);
		} else {
			guardEntry.getNextEntry().setLastEntry(entry);
		}

		guardEntry.setNextEntry(entry);
	}

	/**
	 * Adds the given integer to the end of the list.
	 * 
	 * @param anInteger
	 *            the integer to add.
	 */
	public void addOnLastPosition(Integer anInteger) {
		ListEntry entry = new ListEntry(anInteger, guardEntry,
				guardEntry.getLastEntry());

		if (guardEntry.getLastEntry() == null
				&& guardEntry.getNextEntry() == null) {
			guardEntry.setNextEntry(entry);
		} else {
			guardEntry.getLastEntry().setNextEntry(entry);
		}

		guardEntry.setLastEntry(entry);

	}

	/**
	 * Checks if the list contains the given element.
	 * 
	 * @param anInteger
	 *            the integer to check.
	 * @return true if the list contains the element, false otherwise.
	 */
	public boolean containsElement(Integer anInteger) {

		ListEntry entry = guardEntry.getNextEntry();
		do {
			if (entry.getValue().equals(anInteger)) {
				return true;
			}
		} while (!(entry = entry.getNextEntry()).equals(
				guardEntry));
		
		return false;
	}

	/**
	 * Gets the size of the list.
	 * 
	 * @return the size of the list.
	 */
	public int getSize() {
		int count = -1;

		ListEntry entry = guardEntry;
		do {
			count++;
		} while (!(entry = entry.getNextEntry()).equals(
				guardEntry));

		return count;
	}

	@Override
	public int hashCode() {
		int hash = 59;

		ListEntry entry = guardEntry.getNextEntry();

		do {
			hash = 61 * hash + entry.hashCode();
		} while (!(entry = entry.getNextEntry()).equals(
				guardEntry));

		return hash;
	}

	@Override
	public boolean equals(Object anObject) {

		if (anObject == this) {
			return true;
		}

		if (!(anObject instanceof List)) {
			return false;
		}

		List aList = (List) anObject;

		if (getFirstEntry() != null && aList != null) {
			if (!getFirstEntry().equals(aList.getFirstEntry())) {
				return false;
			}
			ListEntry entry = getFirstEntry();
			ListEntry aEntry = aList.getFirstEntry();
			while (!(entry = entry.getNextEntry()).equals(
					guardEntry)) {
				aEntry = aEntry.getNextEntry();

				if (!entry.equals(aEntry)) {
					return false;
				}

			}
		}

		return true;
	}
}
