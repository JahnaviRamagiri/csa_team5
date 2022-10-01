/**
 * 
 */
package csci6461;

import java.util.Arrays;
import java.util.BitSet;

/**
 * Represents the memory of the CISC simulator, contains
 * 2048 words, expandable to 4096 words.
 * Implemented using singleton design pattern.
 * 
 * @author Sitong Liu
 *
 */
public class Memory {
	
	private static final Memory INSTANCE = new Memory();
	private static Word[] memory;
	
	//	private constructor to prevent initialization from outside the class
	private Memory() {
		memory = new Word[4096];
		Arrays.fill(memory, new Word());
	}
	public static Memory getInstance() {
		return INSTANCE;
	}
	
	public Word read(int address) {
		return memory[address];
		
	}
	
	public void write(Word inp, int address) {
		memory[address] = inp;
	}
}
