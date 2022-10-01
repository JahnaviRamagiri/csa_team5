package csci6461;

import java.util.BitSet;

public class Word extends BitSet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Word() {
		super(16);
	}
	
	public static Word int2Word(int value) {
		Word w = new Word();
		BitSet bitset = Util.int2BitSet(value);
		Util.bitSetDeepCopy(bitset, bitset.length(), w, 16);
		return w;
	}
}
