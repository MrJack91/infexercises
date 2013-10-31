package ch.zhaw.dbru.inf3.emulator.logic;

import java.util.BitSet;

import org.apache.commons.lang3.StringUtils;

import ch.zhaw.dbru.inf3.emulator.MPCConstants;

/**
 * @author Daniel Brun
 * 
 *         Utility-Class for binary operations
 */
public class BinaryUtils {

	/**
	 * Converts a {@link BitSet} to a binary string.
	 * 
	 * @param aBitSet
	 *            the bit set to convert.
	 * @return the binary string.
	 */
	public static String convertBitSetToStringSW(BitSet aBitSet, int aWidth) {
		StringBuffer result = new StringBuffer();

		if (aBitSet != null) {
			for (int i = 0; i < aBitSet.length(); i++) {
				if (aBitSet.get(i)) {
					result.append("1");
				} else {
					result.append("0");
				}
			}

			// fill with 0 till the end (Case of Two-complement is solved by
			// the nature of the two-complement )
			for (int i = result.length(); i < aWidth; i++) {
				result.append("0");
			}
		}

		return result.toString();
	}

	/**
	 * Converts a {@link BitSet} to a binary string.
	 * 
	 * @param aBitSet
	 *            the bit set to convert.
	 * @return the binary string.
	 */
	public static String convertBitSetToString(BitSet aBitSet) {
		return convertBitSetToStringSW(aBitSet,MPCConstants.BF_LENGTH);
	}
	/**
	 * Converts a {@link BitSet} to an int.
	 * 
	 * @param aBitSet
	 *            the bit set to convert.
	 * @param aFullWidth
	 *            the full width.
	 * @return the int representation.
	 */
	public static int convertBitSetToInt(BitSet aBitSet) {
		return convertBitSetToIntSW(aBitSet, MPCConstants.BF_LENGTH);
	}
	
	/**
	 * Converts a {@link BitSet} to an int.
	 * 
	 * @param aBitSet
	 *            the bit set to convert.
	 * @param aWidth the width
	 * @return the int representation.
	 */
	public static int convertBitSetToIntSW(BitSet aBitSet, int aWidth) {
		return convertBitStringToInt(StringUtils
				.reverse(convertBitSetToStringSW(aBitSet, aWidth)));
	}
	

	/**
	 * Performs an addition based on the 2-Complement and stores the value in
	 * the first argument.
	 * 
	 * @param aSummandOne
	 *            the first summand and result.
	 * @param aSummandTwo
	 *            the second summand.
	 * @return the value of the carry flag (true if set, false otherwise).
	 */
	public static boolean addBitSets(BitSet aSummandOne, BitSet aSummandTwo) {
		boolean carryFlag = false;
		BitSet tmpResult = new BitSet(MPCConstants.BF_LENGTH);

		// Iterate over all positions
		for (int i = 0; i < MPCConstants.BF_LENGTH; i++) {

			// If 1 AND 1 => set carry flag
			if (aSummandOne.get(i) && aSummandTwo.get(i)) {

				// If carry flag already set, position is 1
				if (carryFlag) {
					tmpResult.set(i);
				}

				carryFlag = true;
				// If 1 AND 0 or 0 AND 1
			} else if ((aSummandOne.get(i) && !aSummandTwo.get(i))
					|| (!aSummandOne.get(i) && aSummandTwo.get(i))) {
				// If no carry flag is set, set 1
				if (!carryFlag) {
					tmpResult.set(i);
				}
			} else {
				// If carry flag is set, set 1
				if (carryFlag) {
					tmpResult.set(i);
					carryFlag = false;
				}
			}
		}

		// Transfer to output variable
		for (int i = 0; i < MPCConstants.BF_LENGTH; i++) {
			aSummandOne.set(i, tmpResult.get(i));
		}

		return carryFlag;
	}

	/**
	 * Creates a BitSet from an integer with the standard length.
	 * 
	 * @param anInt
	 *            the integer to convert.
	 * @return the bit set
	 */
	public static BitSet createBitSetFromIntStandard(int anInt) {
		return createBitSetFromInt(anInt, MPCConstants.BF_LENGTH,true);

	}

	/**
	 * Creates a BitSet from an integer.
	 * 
	 * @param anInt
	 *            the integer to convert.
	 * @param isCheckSignedLength If set to true, the length of the resulting bit set will be checked under consideration of the sign.
	 * @return the bit set
	 */
	public static BitSet createBitSetFromInt(int anInt, int aLength, boolean isCheckSignedLength) {
		BitSet result = new BitSet(aLength);

		boolean negative = false;

		if (anInt < 0) {
			anInt = anInt * -1;
			negative = true;
		}

		String intBin = StringUtils.reverse(Integer.toBinaryString(anInt));

		if ((isCheckSignedLength && intBin.length() > (aLength - 1))  || (!isCheckSignedLength && intBin.length() > aLength)) {
			throw new MiniPowerPCException(
					"Die Länge der konvertierten Zahl ist grösser als maximal erlaubt!");
		}

		// Iterate over generated binary string and transfer to bitset.
		for (int i = 0; i < intBin.length(); i++) {
			if (intBin.charAt(i) == '1') {
				result.set(i);
			}
		}

		// If the number is negative, perform the necessary steps for the
		// Two-Complement.
		if (negative) {
			for (int i = 0; i < aLength; i++) {
				result.set(i, !result.get(i));
			}
			addBitSets(result, createBitSetFromIntStandard(1));
		}

		return result;
	}

	/**
	 * Converts the given string into a bitset.
	 * 
	 * @param aString
	 *            the string to convert.
	 * @return the created bitset.
	 */
	public static BitSet createBitSetFromStringStandard(String aString) {
		BitSet res = new BitSet();
		if (aString != null) {
			for (int i = 0; i < aString.length(); i++) {
				res.set(i, aString.charAt(i) == '1');
			}
		}
		return res;
	}

	/**
	 * Performs a shift right operation.
	 * 
	 * @param aBitSet
	 *            the bitset to perform the operation.
	 * @param isArithmetic
	 *            true if the shift should be arithmetic.
	 * @return the value of the carry flag.
	 */
	public static boolean shiftRight(BitSet aBitSet, boolean isArithmetic) {
		boolean carryFlag = false;
		int iterCount = MPCConstants.BF_LENGTH - 1;

		carryFlag = aBitSet.get(0);

		if (isArithmetic) {
			iterCount = iterCount - 1;
		}

		for (int i = 0; i <= iterCount; i++) {
			aBitSet.set(i, aBitSet.get(i + 1));
		}

		if (!isArithmetic) {
			aBitSet.set(iterCount, false);
		}

		return carryFlag;
	}

	/**
	 * Performas shift left operation.
	 * 
	 * @param aBitSet
	 *            The bitset to perform the operation.
	 * @param isArithmetic
	 *            True if an arithmetic shift should be performed.
	 * @return The value of the carry flag.
	 */
	public static boolean shiftLeft(BitSet aBitSet, boolean isArithmetic) {
		boolean carryFlag = false;

		int length = MPCConstants.BF_LENGTH - 1;

		if (isArithmetic) {
			length--;
		}

		carryFlag = aBitSet.get(length);

		for (int i = length; i > 0; i--) {
			aBitSet.set(i, aBitSet.get(i - 1));
		}

		aBitSet.set(0, false);

		return carryFlag;
	}

	/**
	 * Compares the given bitset to an int.
	 * 
	 * @param aBitSet
	 *            The bitset to compare.
	 * @param anInt
	 *            The int to compare.
	 * @return true if the value is equal.
	 */
	public static boolean compareBitSetToInt(BitSet aBitSet, int anInt) {
		BitSet anIntSet = createBitSetFromInt(anInt, MPCConstants.BF_LENGTH,true);

		for (int i = 0; i < MPCConstants.BF_LENGTH; i++) {
			if (!(anIntSet.get(i) == aBitSet.get(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Expands the given bitset to the max. count of positions.
	 * 
	 * @param aBitSet
	 *            The bitset to expand.
	 */
	public static void expandBitSet(BitSet aBitSet, int aCurLastPos) {
		aBitSet.set(aCurLastPos, MPCConstants.BF_LENGTH,
				aBitSet.get(aCurLastPos));
	}

	/**
	 * Converts the given binary sting to an int value. Required bit direction:
	 * Left: MSB, Right: LSB
	 * 
	 * @param aBinaryValue
	 *            The string with the binary value.
	 * @return The converted int.
	 */
	public static int convertBitStringToInt(String aBinaryValue) {
		boolean negative = (aBinaryValue.charAt(0) == '1');
		String binaryString = aBinaryValue;
		int result = 0;
		
		if (negative) {
			boolean invertMode = false;
			StringBuffer binBuf = new StringBuffer();
			
			for (int i = aBinaryValue.length() - 1; i >= 0; i--) {
				if (!invertMode) {
					if (aBinaryValue.charAt(i) == '0') {
						binBuf.append('0');
					} else {
						binBuf.append('1');
						invertMode = true;
					}
				} else {
					if (aBinaryValue.charAt(i) == '0') {
						binBuf.append('1');
					} else {
						binBuf.append('0');
					}
				}
			}
			
			binaryString = "-" + binBuf.toString();
		}
		
		result = Integer.valueOf(binaryString,2);
		
		return result;
	}
	
}
