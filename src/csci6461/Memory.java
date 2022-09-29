/**
 * 
 */
package csci6461;

import java.util.Arrays;

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
	private static Word[] memory = new Word[2048];
	
	//	private constructor to prevent initialization from outside the class
	private Memory() {
		Arrays.fill(memory, new Word());
	}
	public static Memory getInstance() {
		return INSTANCE;
	}
	
	public Word read(int address) {
		return memory[address];
		
	}
	
	public void write(Word word, int address) {
		memory[address] = word;
	}
}
