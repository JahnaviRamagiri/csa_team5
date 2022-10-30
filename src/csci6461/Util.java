package csci6461;

import java.util.BitSet;

public class Util {
	
		
	public static int bitSet2Int(BitSet bitSet) {
        int intValue = 0;
        for (int bit = 0; bit < bitSet.length(); bit++) {
            if (bitSet.get(bit)) {
                intValue |= (1 << bit);
            }
        }
        return intValue;
    }
	
	public static int bitSet2IntSigned(BitSet bitSet) {
        int intValue = 0;
        boolean sign = bitSet.get(15);
        bitSet.set(0, false);
        for (int bit = 0; bit < bitSet.length(); bit++) {
            if (bitSet.get(bit)) {
                intValue |= (1 << bit);
            }
        }
        if (sign) {
        	return -intValue;
        }
        return intValue;
    }
	
	public static BitSet int2BitSet(int value) {
		BitSet bits = new BitSet();
	    int index = 0;
	    while (value != 0) {
	      if (value % 2 != 0) {
	        bits.set(index);
	      }
	      ++index;
	      value = value >>> 1;
	    }
	    return bits;
	}
	
	public static BitSet int2BitSetSigned(int value) {
		BitSet bits = new BitSet();
		if (value < 0) {
			bits.set(15);
		} 
	    int index = 0;
	    while (value != 0) {
	      if (value % 2 != 0) {
	        bits.set(index);
	      }
	      ++index;
	      value = value >>> 1;
	    }
	    return bits;
	}
	
	
	public static Word int2Word(int value) {
		Word w = new Word();
	    int index = 0;
	    while (value != 0) {
	      if (value % 2 != 0) {
	        w.set(index);
	      }
	      ++index;
	      value = value >>> 1;
	    }
	    return w;
	}
	public static void bitSetDeepCopy(BitSet source, int sourceBits,
			BitSet destination, int destinationBits) {
		if (sourceBits <= destinationBits) {
			destination.clear();
			for (int i = 0; i < sourceBits; i++) {
				destination.set(i, source.get(i));
			}
		} else {
			destination.clear();
			for (int i = 0; i < destinationBits; i++) {
				destination.set(i, source.get(i));
			}
		}
	}
}
