package csci6461;

public class Simulator {
	private Simulator() {
		// initialize registers
		Register R0 = new Register(16);
		Register R1 = new Register(16);
		Register R2 = new Register(16);
		Register R3 = new Register(16);
		
		Register X1 = new Register(16);
		Register X2 = new Register(16);
		Register X3 = new Register(16);
		
		Register PC = new Register(12);
		Register IR = new Register(16);
		Register CC = new Register(4);
		
		Register MAR = new Register(12);
		Register MBR = new Register(16);
//		Register MSR = new Register(18);
		Register MFR = new Register(4);
		
		Register OPCODE = new Register(6);
		Register IX = new Register(2);
		Register R = new Register(2);
		Register I = new Register(1);
		Register ADDR = new Register(5);
		
		
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
}
