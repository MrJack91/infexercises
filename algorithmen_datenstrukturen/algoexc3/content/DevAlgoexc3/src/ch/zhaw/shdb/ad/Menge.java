/**
 * 
 */
package ch.zhaw.shdb.ad;

/**
 * 
 *
 */
public class Menge {

	private List list;
	
	/**
	 * 
	 */
	public Menge() {
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		// Reihenfolge & Wiederholungen egal.
		return super.equals(arg0);
	}
	
	/**
	 * Counts the elements of the 'Menge'.
	 * Duplicates will only be counted once.
	 * 
	 * @return the count.
	 */
	public int getSize(){
		return -1;
	}
	
	/**
	 * Cuts this 'Menge' with the given 'Menge'.
	 * 
	 * @param aMenge The 'Menge' to cut.
	 * @return True if the 'Menge' was cutted succesfully.
	 */
	public boolean cut(Menge aMenge){
		
		return false;
	}
	
	/**
	 * Aggregates / Joins / Units the given 'Menge' with this instance of 'Menge'.
	 * 
	 * @param aMenge The 'Menge' to aggregate.
	 * @return True if the aggregation was successfully.
	 */
	public boolean aggregate(Menge aMenge){
		
		return false;
	}
	
	/**
	 * Adds a new element to the 'Menge'.
	 * 
	 * @param anInt the element to add.
	 * @return True if the element was added successfully.
	 */
	public boolean addElement(Integer anInt){
		
		return false;
	}
	
	/**
	 * Checks if an element is part of the 'Menge'.
	 * 
	 * @param anInt the element.
	 * @return True if the element is part of the 'Menge'
	 */
	public boolean isElementOf(Integer anInt){
	
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return null;
	}

}
