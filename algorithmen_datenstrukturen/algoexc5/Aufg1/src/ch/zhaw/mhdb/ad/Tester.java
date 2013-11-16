/**
 * 
 */
package ch.zhaw.mhdb.ad;

/**
 * @author Daniel Brun
 *
 * Tests the Queue and the Stack.
 */
public class Tester {

	/**
	 * 
	 */
	public Tester() {
		testList();
		testQueue();
		testStack();
	}

	private void testList() {
		System.out.println("\nTesting List:");
		List list = new List();
		
		System.out.println("Size: " + list.getSize());
		
		list.addOnFirstPosition(6);
		System.out.println("Size: " + list.getSize());
		list.addOnFirstPosition(5);
		list.addOnFirstPosition(4);
		
		System.out.println("Size: " + list.getSize());
		
		list.addOnLastPosition(7);
		list.addOnLastPosition(8);
		list.addOnLastPosition(9);
		
		System.out.println("Size: " + list.getSize());
		System.out.println(list.containsElement(10));
		System.out.println(list.containsElement(4));
		System.out.println(list.containsElement(6));
		System.out.println(list.containsElement(9));
	}



	private void testStack() {
		System.out.println("\nTesting Stack:");
		Stack stack = new Stack();
		
		System.out.println("Size: " + stack.getSize());
		
		stack.push(1);
		stack.push(2);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);
		stack.push(6);
		stack.push(7);
		stack.push(8);
		stack.push(9);
		stack.push(10);
		
		System.out.println("Size: " + stack.getSize());
		
		while(!stack.isEmpty()){
			System.out.println(stack.pop());
		}
		
	}

	private void testQueue() {
		System.out.println("\nTesting Queue:");
		Queue queue = new Queue();
		
		System.out.println("Size: " + queue.getSize());
		
		queue.enqueue(1);
		queue.enqueue(2);
		queue.enqueue(1);
		queue.enqueue(3);
		queue.enqueue(3);
		queue.enqueue(4);
		queue.enqueue(5);
		queue.enqueue(6);
		queue.enqueue(7);
		queue.enqueue(8);
		
		System.out.println("Size: " + queue.getSize());
		
		while(!queue.isEmpty()){
			System.out.println(queue.dequeue());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Tester();
	}

}
