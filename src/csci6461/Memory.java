/**
 * 
 */
package csci6461;

import java.util.Arrays;
import java.util.BitSet;
import java.util.ArrayList;


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
	private static Cache cache;
	
	//	private constructor to prevent initialization from outside the class
	private Memory() {
		memory = new Word[4096];
		Arrays.fill(memory, new Word());
		cache = new Cache();
	}
	public static Memory getInstance() {
		return INSTANCE;
	}
	
	public Word read(int address) {
		return cache.read(address);
		
	}
	
	private class Cache {
		public ArrayList<Integer> address;
		public ArrayList<Word> content;
		public int length;
		public Cache() {
			address = new ArrayList<>();
			content = new ArrayList<>();
			length = 0;
		}
		public void add(int addr, Word cont) {
			if (length < 16) {
				address.add(addr);
				content.add(cont);
			} else {
				address.remove(0);
				content.remove(0);
			}
		}
		public Word read(int addr) {
			for (int i = 0; i < 16; i++) {
				if (address.get(i) == addr) 
					return content.get(i);
			}
			this.add(addr, memory[addr]);
			return memory[addr];
		}
		
	}
	
	
	
	
	public void write(Word inp, int address) {
		memory[address] = inp;
	}
	
	public void write(int inp, int address) {
		memory[address] = Util.int2Word(inp);
	}
}
