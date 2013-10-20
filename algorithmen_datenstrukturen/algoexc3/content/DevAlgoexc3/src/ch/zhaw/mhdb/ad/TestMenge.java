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
		System.out.println("\nTesting Menge...\n-----------------------\n");
		Menge mengeOne = new Menge();
		mengeOne.addElement(Integer.valueOf(5));
		mengeOne.addElement(Integer.valueOf(5));
		mengeOne.addElement(Integer.valueOf(8));
		mengeOne.addElement(Integer.valueOf(10));
		
		System.out.println(mengeOne.toString());
		System.out.println(mengeOne.getSize());
		System.out.println(mengeOne.containsElement(Integer.valueOf(8)));
		System.out.println(mengeOne.containsElement(Integer.valueOf(2)));

		Menge mengeTwo = new Menge();
		mengeTwo.addElement(Integer.valueOf(8));
		mengeTwo.addElement(Integer.valueOf(10));
		
		mengeOne.cut(mengeTwo);
		System.out.println(mengeOne.toString());
		
		Menge mengeThree = new Menge();
		mengeThree.addElement(Integer.valueOf(5));
		mengeThree.addElement(Integer.valueOf(5));
		mengeThree.addElement(Integer.valueOf(6));
		mengeThree.addElement(Integer.valueOf(10));
		
		mengeOne.aggregate(mengeThree);
		System.out.println(mengeOne.toString());
		
	}

	private void testList() {
		System.out.println("\nTesting List...\n-----------------------\n");
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
