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
	private static int[] cache_addr;
	private static Word[] cache_cont;
	
	//	private constructor to prevent initialization from outside the class
	private Memory() {
		memory = new Word[4096];
		Arrays.fill(memory, new Word());
		cache_addr = new int[16];
		cache_cont = new Word[16];
	}
	public static Memory getInstance() {
		return INSTANCE;
	}
	
	public Word read(int address) {
		return memory[address];
		
	}
	
	private static int find(int addr) {
		for (int i = 0; i < 16; i++) {
			if (cache_addr[i] == addr) 
				return i;
		}
		return -1;
	}
	
	public Word readFromCache(int address) {
		int index = find(address);
		if (index != -1) {
			return cache_cont[index];
		} 
		cache_cont[0] = memory[address];
		cache_addr[0] = address;
		return cache_cont[0];
		
	}
	
	public void write(Word inp, int address) {
		memory[address] = inp;
	}
}
