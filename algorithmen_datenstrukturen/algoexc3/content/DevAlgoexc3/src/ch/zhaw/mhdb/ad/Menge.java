/**
 * 
 */
package ch.zhaw.mhdb.ad;

/**
 * 
 *
 */
public class Menge {

	private List list;

	/**
	 * Creates a new 'Menge'.
	 */
	public Menge() {
		list = new List();
	}

	/**
	 * Counts the elements of the 'Menge'. Duplicates will only be counted once.
	 * 
	 * @return the count.
	 */
	public int getSize() {
		int counter = 0;
		
		List tmpList = new List();
		
		ListEntry entry = getFirstEntry();
		if (entry != null) {
			do {
				if (!tmpList.containsElement(entry.getValue())) {
					tmpList.addOnFirstPosition(entry.getValue());
					counter++;
				}
			} while ((entry = entry.getNextEntry()) != null);
		}
		
		return counter;
	}

	/**
	 * Cuts this 'Menge' with the given 'Menge'.
	 * 
	 * @param aMenge
	 *            The 'Menge' to cut.
	 */
	public void cut(Menge aMenge) {

		List cutList = new List();
		
		ListEntry entry = getFirstEntry();
		if (entry != null) {
			do {
				if (aMenge.containsElement(entry.getValue())) {
					cutList.addOnLastPosition(entry.getValue());
				}
			} while ((entry = entry.getNextEntry()) != null);
		}
		list = cutList;
	}

	/**
	 * Aggregates / Joins / Units the given 'Menge' with this instance of
	 * 'Menge'.
	 * 
	 * @param aMenge
	 *            The 'Menge' to aggregate.
	 * 
	 */
	public void aggregate(Menge aMenge) {
		if (aMenge == null) {
			throw new IllegalArgumentException(
					"The parameter aMenge must not be null");
		}

		ListEntry entry = aMenge.getFirstEntry();
		if (entry != null) {
			do {
				addElement(entry.getValue());
			} while ((entry = entry.getNextEntry()) != null);
		}
	}

	/**
	 * Gets the first entry of the 'Menge'.
	 * 
	 * @return the first entry of the list or null if the list is empty.
	 */
	private ListEntry getFirstEntry() {
		return list.getFirstEntry();
	}

	/**
	 * Adds a new element to the 'Menge'.
	 * 
	 * @param anInt
	 *            the element to add.
	 * @return True if the element was added successfully.
	 */
	public void addElement(Integer anInt) {
		list.addOnLastPosition(anInt);
	}

	/**
	 * Checks if an element is part of the 'Menge'.
	 * 
	 * @param anInt
	 *            The element.
	 * @return True if the element is part of the 'Menge'
	 */
	public boolean containsElement(Integer anInt) {
		return list.containsElement(anInt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer("{");

		ListEntry entry = getFirstEntry();
		if (entry != null) {
			do {
				buf.append(entry.getValue());

				if (entry.getNextEntry() != null) {
					buf.append(", ");
				}
			} while ((entry = entry.getNextEntry()) != null);
		}

		buf.append("}");

		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object anObject) {
		if (anObject == this) {
			return true;
		}

		if (!(anObject instanceof Menge)) {
			return false;
		}

		Menge aMenge = (Menge) anObject;

		ListEntry entry = getFirstEntry();
		if (entry != null) {
			do {
				if(!aMenge.containsElement(entry.getValue())){
					return false;
				}
			} while ((entry = entry.getNextEntry()) != null);
		}
		
		entry = aMenge.getFirstEntry();
		if (entry != null) {
			do {
				if(!containsElement(entry.getValue())){
					return false;
				}
			} while ((entry = entry.getNextEntry()) != null);
		}

		return true;
	}

	@Override
	public int hashCode() {
		int hashCode = 67;

		hashCode = 67 * hashCode + list.hashCode();

		return hashCode();
	}
}
