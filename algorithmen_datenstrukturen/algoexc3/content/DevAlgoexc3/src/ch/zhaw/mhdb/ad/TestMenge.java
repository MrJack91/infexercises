/**
 * 
 */
package ch.zhaw.mhdb.ad;

/**
 * Tests the 'Menge' and the underlying 'List'.
 *
 */
public class TestMenge {

	public void execute(){
		testList();
		testMenge();
	}

	private void testMenge() {
		// TODO Auto-generated method stub
		
	}

	private void testList() {
		List listOne = new List();
		listOne.addOnFirstPosition(Integer.valueOf(5));
		listOne.addOnFirstPosition(Integer.valueOf(5));
		listOne.addOnFirstPosition(Integer.valueOf(8));
		listOne.addOnLastPosition(Integer.valueOf(10));
		
		if(listOne.containsElement(Integer.valueOf(10))){
			System.out.println(listOne.getSize());
			System.out.println(listOne.toString());
			
		}
		
		List listTwo = new List();
		listTwo.addOnFirstPosition(Integer.valueOf(5));
		listTwo.addOnFirstPosition(Integer.valueOf(5));
		listTwo.addOnFirstPosition(Integer.valueOf(8));
		listTwo.addOnLastPosition(Integer.valueOf(10));
		
		System.out.println(listOne.equals(listTwo));
		
		List listThree = new List();
		listTwo.addOnFirstPosition(Integer.valueOf(5));
		listTwo.addOnFirstPosition(Integer.valueOf(8));
		listTwo.addOnLastPosition(Integer.valueOf(10));
		
		System.out.println(listOne.equals(listThree));
		
		List listFour = new List();
		listTwo.addOnFirstPosition(Integer.valueOf(5));
		listTwo.addOnFirstPosition(Integer.valueOf(5));
		listTwo.addOnFirstPosition(Integer.valueOf(8));
		listTwo.addOnLastPosition(Integer.valueOf(10));
		listTwo.addOnLastPosition(Integer.valueOf(10));
		
		System.out.println(listOne.equals(listFour));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestMenge().execute();
	}

}
