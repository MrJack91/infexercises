/**
 * 
 */
package ch.zhaw.mhdb.ad;


/**
 * @author Daniel Brun
 *
 * Queue based on a double chained list.
 */
public class Queue {

	private List list;
	
	/**
	 * Creates a new Queue.
	 */
	public Queue() {
		list = new List();
	}

	/**
	 * Enqueues the given value.
	 * 
	 * @param aValue The value to enqueue.
	 */
	public void enqueue(Integer aValue){
		list.addOnLastPosition(aValue);
	}
	
	/**
	 * Dequeues the first element.
	 * 
	 * @return the first element.
	 */
	public Integer dequeue(){
		ListEntry entry = list.getFirstEntry();
		
		entry.getNextEntry().setLastEntry(entry.getLastEntry());
		entry.getLastEntry().setNextEntry(entry.getNextEntry());
		
		return entry.getValue();
	}
	
	/**
	 * Checks if the queue is mepty.
	 * 
	 * @return True if empty.
	 */
	public boolean isEmpty(){
		return (list.getSize() == 0);
	}
	
	/**
	 * Evaluates the size of the queue.
	 * 
	 * @return The size.
	 */
	public int getSize(){
		return list.getSize();
	}
}
