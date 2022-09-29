package csci6461;

import java.util.BitSet;

public class Simulator {
	
	private static final Simulator INSTANCE = new Simulator();
	
	private Register R0;
	private Register R1;
	private Register R2;
	private Register R3;
	
	private Register X1;
	private Register X2;
	private Register X3;
	
	private Register PC;
	private Register IR;
	private Register CC;
	private Register MAR;
	private Register MBR;
	private Register MFR;
	
	private Register OPCODE;
	private Register IX;
	private Register R;
	private Register I;
	private Register ADDR;
	
	private Simulator() {
		// initialize registers
		R0 = new Register(16);
		R1 = new Register(16);
		R2 = new Register(16);
		R3 = new Register(16);
		
		X1 = new Register(16);
		X2 = new Register(16);
		X3 = new Register(16);
		
		PC = new Register(12);
		IR = new Register(16);
		CC = new Register(4);
		
		MAR = new Register(12);
		MBR = new Register(16);
//		MSR = new Register(18);
		MFR = new Register(4);
		
		OPCODE = new Register(6);
		IX = new Register(2);
		R = new Register(2);
		I = new Register(1);
		ADDR = new Register(5);
		
		
	}
	
	public static Simulator getInstance() {
		return INSTANCE;
	}
	
	private static Memory memory = Memory.getInstance();
	
	public static int calculateEA(byte i, byte ix, int address) {
		int ea = 0;	// return value
		// no indirect
		if (i == 0) {
			if (ix == 0) {
				ea = address;
				return ea;
			} else if (ix <= 3 && ix >= 1) {
				ea = Util.bitSet2Int(memory.read((int)ix)) + address;
				return ea;
			}
		}
		// with indirect
		if (i == 1) {
			if (ix == 0) {
				ea = Util.bitSet2Int(memory.read(address));
				return ea;
			} else if (ix <= 3 && ix >= 1) {
				// variable for c(IX) + c(Address Field)
				int tmpAddr = Util.bitSet2Int(memory.read((int)ix)) + address;
				// fetch content at given address
				ea = Util.bitSet2Int(memory.read(tmpAddr));
				return ea;
			}
		}

		return ea;
	}
	
	public void setRegister(Register r, int content) {
		Word w = (Word) Util.int2BitSet(content);
		Util.bitsetDeepCopy(w, 16, r, r.getSize());
		// TODO: update GUI
	}
	public void setRegister(Register r, BitSet src) {
		int srcData = Util.bitSet2Int(src);
		setRegister(r, srcData);
		// TODO Auto-generated method stub
		
	}
	public void operation(byte opcode, byte r, byte i, byte ix, int address) {
		int ea;
		switch (opcode) {
		case OpCodes.LDR:
			ea = calculateEA(i, ix, address);
			setRegister(MAR, ea);
			int dataAddr = Util.bitSet2Int(MAR);
			int data = Util.bitSet2Int(memory.read(dataAddr));
			setRegister(MBR, data);
			switch (r) {
			case 0:
				setRegister(R0, MBR);
			
			}
		case OpCodes.STR:
			
		case OpCodes.LDA:
			
		case OpCodes.LDX:
			ea = calculateEA((byte)0, ix, address);
		case OpCodes.STX:
		}
		
	}

	
}