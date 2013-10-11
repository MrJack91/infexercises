/**
 * 
 */
package ch.zhaw.mhdb.ad;

/**
 * List to store integer values.
 * Null values are not permitted.
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
	public ListEntry getFirstElement(){
		
		return null;
	}
	
	/**
	 * Returns the last element of the list.
	 * 
	 * @return the element or null if the list is empty.
	 */
	public ListEntry getLastElement(){
		
		return null;
	}

	
	/**
	 * Adds the given integer to the beginning of the list.
	 * 
	 * @param anInteger the integer to add.
	 */
	public void addOnFirstPosition(Integer anInteger){
		
	}
	
	/**
	 * Adds the given integer to the end of the list.
	 * 
	 * @param anInteger the integer to add.
	 */
	public void addOnLastPosition(Integer anInteger){
		
	}
	
	/**
	 * Checks if the list contains the given element.
	 * 
	 * @param anInteger the integer to check.
	 * @return true if the list contains the element, false otherwise.
	 */
	public boolean containsElement(Integer anInteger){
		
		return false;
	}
	
	/**
	 * Gets the size of the list.
	 * 
	 * @return the size of the list.
	 */
	public int getSize(){
		return 0;
	}
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
}
