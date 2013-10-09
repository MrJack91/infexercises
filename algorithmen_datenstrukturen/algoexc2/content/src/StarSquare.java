/**
 * 
 */
package da.aufgaben.serie2;

public class StarSquare {

	/**
	 * Crates a square of stars in a char array.
	 * 
	 * @param anArray
	 *            the array to fill with stars.
	 * @return the array with the star square.
	 */
	public char[][] fillWithStars(char[][] anArray) {
		if (anArray.length == 0 || anArray.length % 2 == 0
				|| anArray[0].length % 2 == 0) {
			throw new IllegalArgumentException(
					"Das Eingabe-Array muss eine ungerade Dimension n x n haben.");
		}

		int dimension = anArray.length;
		int middleLine = ((dimension - 1) / 2);
		int offset = 0;

		for (int col = 0; col <= middleLine; col++) {
			anArray[middleLine][col] = '*';
			anArray[middleLine + offset][col] = '*';
			anArray[middleLine - offset][col] = '*';

			anArray[middleLine][dimension - col - 1] = '*';
			anArray[middleLine + offset][dimension - col - 1] = '*';
			anArray[middleLine - offset][dimension - col - 1] = '*';

			offset++;

		}

		return anArray;
	}

	/**
	 * Creates an array with the given dimensions and fills it with spaces.
	 * 
	 * @param aDimension
	 *            the dimension of the array.
	 * @return the array filled with spaces.
	 */
	public char[][] createArrayWithSpaces(int aDimension) {
		char[][] empptyArray = new char[aDimension][aDimension];

		for (int x = 0; x < empptyArray.length; x++) {
			for (int y = 0; y < empptyArray[x].length; y++) {
				empptyArray[x][y] = ' ';
			}
		}
		return empptyArray;
	}

	/**
	 * Prints the given array. Each column is separated by a '|'.
	 * 
	 * @param anArray
	 *            the array to print.
	 */
	public void printArrayInTable(char[][] anArray) {
		for (int x = 0; x < anArray.length; x++) {
			for (int y = 0; y < anArray[x].length; y++) {
				System.out.print("|" + anArray[x][y]);
			}
			System.out.print("|\n");
		}
	}

	/**
	 * Runs the class.
	 * 
	 * @param args some arguments
	 */
	public static void main(String[] args) {
		StarSquare starSquare = new StarSquare();
		char[][] myStarSquareArray = starSquare.createArrayWithSpaces(21);
		myStarSquareArray = starSquare.fillWithStars(myStarSquareArray);
		starSquare.printArrayInTable(myStarSquareArray);
	}

}
