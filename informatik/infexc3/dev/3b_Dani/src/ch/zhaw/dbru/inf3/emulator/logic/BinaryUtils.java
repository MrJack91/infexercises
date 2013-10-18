package ch.zhaw.dbru.inf3.emulator.logic;

import java.util.BitSet;

import org.apache.commons.lang3.StringUtils;

import ch.zhaw.dbru.inf3.emulator.MPCConstants;

public class BinaryUtils {

	/**
	 * Converts a {@link BitSet} to a binary string.
	 * 
	 * @param aBitSet
	 *            the bit set to convert.
	 * @return the binary string.
	 */
	public static String convertBitSetToString(BitSet aBitSet) {
		StringBuffer result = new StringBuffer();

		if (aBitSet != null) {
			for (int i = 0; i < aBitSet.length(); i++) {
				if (aBitSet.get(i)) {
					result.append("1");
				} else {
					result.append("0");
				}
			}

			for (int i = result.length(); i < MPCConstants.BF_LENGTH; i++) {
				result.append("0");
			}
		}

		return result.toString();
	}

	/**
	 * Converts a {@link BitSet} to an int.
	 * 
	 * @param aBitSet
	 *            the bit set to convert.
	 * @return the int representation.
	 */
	public static int convertBitSetToInt(BitSet aBitSet) {
		return Integer.valueOf(
				StringUtils.reverse(convertBitSetToString(aBitSet)), 2);
	}

	/**
	 * Performs an addition based on the 2-Complement and stores the value in
	 * the first argument.
	 * 
	 * @param aSummandOne
	 *            the first summand and result.
	 * @param aSummandTwo
	 *            the second summand.
	 * @param isCarryFlagSet
	 *            true if the carry flag is set.
	 * @return the value of the carry flag (true if set, false otherwise).
	 */
	public static boolean addBitSets(BitSet aSummandOne, BitSet aSummandTwo,
			boolean isCarryFlagSet) {
		boolean carryFlag = false;
		BitSet tmpResult = new BitSet(MPCConstants.BF_LENGTH);

		if (aSummandOne.length() != aSummandTwo.length()) {
			// TODO: Throw exception
		}

		for (int i = 0; i < MPCConstants.BF_LENGTH; i++) {
			if (aSummandOne.get(i) && aSummandTwo.get(i)) {

				if (carryFlag) {
					tmpResult.set(i);
				}

				carryFlag = true;
			} else if ((aSummandOne.get(i) && !aSummandTwo.get(i))
					|| (!aSummandOne.get(i) && aSummandTwo.get(i))) {
				if (!carryFlag) {
					tmpResult.set(i);
				}
			}else{
				if(carryFlag){
					tmpResult.set(i);
					carryFlag = false;
				}
			}
		}

		for(int i = 0;i < MPCConstants.BF_LENGTH;i++){
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
		return createBitSetFromInt(anInt, MPCConstants.BF_LENGTH);

	}

	/**
	 * Creates a BitSet from an integer.
	 * 
	 * @param anInt
	 *            the integer to convert.
	 * @return the bit set
	 */
	public static BitSet createBitSetFromInt(int anInt, int aLength) {
		BitSet result = new BitSet(aLength);


		boolean negative = false;

		if (anInt < 0) {
			anInt = anInt * -1;
			negative = true;
		}

		String intBin = StringUtils.reverse(Integer.toBinaryString(anInt));

		if (intBin.length() > aLength) {
			// TODO: throw exception
		}

		for (int i = 0; i < intBin.length();i++) {
			if (intBin.charAt(i) == '1') {
				result.set(i);
			}
		}

		if (negative) {
			for (int i = 0; i < aLength; i++) {
				result.set(i, !result.get(i));
			}
			addBitSets(result, createBitSetFromIntStandard(1), false);
		}

		return result;
	}
}
