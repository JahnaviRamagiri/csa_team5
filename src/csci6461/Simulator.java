package csci6461;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;

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

//	private Register OPCODE;
//	private Register IX;
//	private Register R;
//	private Register I;
//	private Register ADDR;
	
	private byte opcode;
	
	private byte ix;
	private byte r;
	private byte i;
	private byte addr;
	
	//private byte rx;
	private byte ry;
	
	private byte al;
	private byte lr;
	private byte count;
	
	private byte dev_id;

	private File f;
	private ArrayList<Integer> instructionAddr = new ArrayList<>();
	
	// an incremental variable to count the number of instructions executed
	private int pcx; 

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
		MFR = new Register(4);

//		OPCODE = new Register(6);
//		IX = new Register(2);
//		R = new Register(2);
//		I = new Register(1);
//		ADDR = new Register(5);

		pcx = 0;

	}

	public static Simulator getInstance() {
		return INSTANCE;
	}

	private static Memory memory = Memory.getInstance();

	public void init() {
		instructionAddr.clear();
		pcx = 0;
		loadFile("./src/csci6461/input.txt");
		try {
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				String s1 = s.nextLine();
				String[] sa = s1.split(" ");
				// setting the memory
				int addr = Integer.parseInt(sa[0].trim(), 16);
				instructionAddr.add(addr); // store addresses of instructions
				Word content = Util.int2Word(Integer.parseInt(sa[1].trim(), 16));
				memory.write(content, addr);

			}
			s.close();
		} catch (Exception ex) {
			System.out.println("Exception occured in input file" + ex);
		}
		setRegister(R0, 0);
		setRegister(R1, 0);
		setRegister(R2, 0);
		setRegister(R3, 0);
		setRegister(X1, 0);
		setRegister(X2, 0);
		setRegister(X3, 0);
		setRegister(PC, 0);
		setRegister(MAR, 0);
		setRegister(MBR, 0);
		setRegister(IR, 0);
	}

	public int singleStep() {
		if (pcx >= instructionAddr.size()) {
			System.out.println("All instructions executed");
			return 1;
		}
		// fetch instruction
		loadInstruction(instructionAddr.get(pcx));
		// ir decode

		int ir = Util.bitSet2Int(IR);
		irDecode(ir);
		// operation
		getInstance().operation();
		pcx++;
		return 0;
	}

	public void loadFile(String path) {
		f = new File(path);
	}

	/**
	 * load instruction at given address
	 * 
	 * @param input
	 */
	public void loadInstruction(int address) {
		setRegister(PC, address);

		setRegister(MAR, PC);

		setRegister(MBR, memory.read(Util.bitSet2Int(MAR)));

		setRegister(IR, MBR);

	}

	public void irDecode(int ir) {
		// constructing ir as string
		String ir_binary = Integer.toBinaryString(ir);
		int zeros = 16 - ir_binary.length();
		for (int i = 0; i < zeros; i++) {
			ir_binary = "0" + ir_binary;
		}
		
//		ir[16] == 
//			ir[0:5] = opcode
//			ir[6:7] = Register
//			ir[8:9] = Index
//			ir[10] = Instruction
//			ir[11:15] = Address
		opcode = (byte) Integer.parseInt(ir_binary.substring(0, 6), 2);
		
		if ((opcode >= 1 && opcode <= 7) 
				||  opcode == 41 || opcode == 42 
				|| (opcode >= 10 && opcode <= 17)) {
			r = (byte) Integer.parseInt(ir_binary.substring(6, 8), 2);
			ix = (byte) Integer.parseInt(ir_binary.substring(8, 10), 2);
			i = (byte) Integer.parseInt(ir_binary.substring(10), 2);
			addr = (byte) Integer.parseInt(ir_binary.substring(11, 16), 2);
		} else if (opcode >= 20 &&opcode <= 25) {
			r = (byte) Integer.parseInt(ir_binary.substring(6, 8), 2);
			ry = (byte) Integer.parseInt(ir_binary.substring(8, 10), 2);
		} else if (opcode == 31 || opcode == 32) {
			r = (byte) Integer.parseInt(ir_binary.substring(6, 8), 2);
			al = (byte) Integer.parseInt(ir_binary.substring(8, 9), 2);
			lr = (byte) Integer.parseInt(ir_binary.substring(9, 10), 2);
			count = (byte) Integer.parseInt(ir_binary.substring(12, 16), 2);
		} else if (opcode >= 61 && opcode <= 63) {
			r = (byte) Integer.parseInt(ir_binary.substring(6, 8), 2);
			dev_id = (byte) Integer.parseInt(ir_binary.substring(11, 16), 2);
		}

	}

	public static int calculateEA(byte i, byte ix, byte address) {
		int ea = 0; // return value
		// no indirect
		if (i == 0) {
			if (ix == 0) {
				ea = address;
				return ea;
			} else if (ix <= 3 && ix >= 1) {
				ea = Util.bitSet2Int(memory.read((int) ix)) + address;
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
				int tmpAddr = Util.bitSet2Int(memory.read((int) ix)) + address;
				// fetch content at given address
				ea = Util.bitSet2Int(memory.read(tmpAddr));
				return ea;
			}
		}

		return ea;
	}

	private String regName2Str(Register r) {
		if (r == R0) {
			return "R0";
		}
		if (r == R1) {
			return "R1";
		}
		if (r == R2) {
			return "R2";
		}
		if (r == R3) {
			return "R3";
		}
		if (r == X1) {
			return "X1";
		}
		if (r == X2) {
			return "X2";
		}
		if (r == X3) {
			return "X3";
		}
		if (r == PC) {
			return "PC";
		}
		if (r == MAR) {
			return "MAR";
		}
		if (r == MBR) {
			return "MBR";
		}
		if (r == IR) {
			return "IR";
		}
		return null;

	}

	private Register regStr2Name(String r) {
		if (r == "R0") {
			return R0;
		}
		if (r == "R1") {
			return R1;
		}
		if (r == "R2") {
			return R2;
		}
		if (r == "R3") {
			return R3;
		}
		if (r == "X1") {
			return X1;
		}
		if (r == "X2") {
			return X2;
		}
		if (r == "X3") {
			return X3;
		}
		if (r == "PC") {
			return PC;
		}
		if (r == "MAR") {
			return MAR;
		}
		if (r == "MBR") {
			return MBR;
		}
		if (r == "IR") {
			return IR;
		}
		return null;

	}

	public void setRegister(Register r, int content) {
		BitSet w = Util.int2BitSet(content);
		Util.bitSetDeepCopy(w, 16, r, r.getSize());
		MainFrame.updateUI(regName2Str(r), r, r.getSize());

	}

	public void setRegister(Register r, BitSet src) {
		int srcData = Util.bitSet2Int(src);
		setRegister(r, srcData);

	}

//	public void setGPR(int r, int content) {
//		switch (r) {
//		case 0:
//			setRegister(R0, content);
//			break;
//		case 1:
//			setRegister(R1, content);
//			break;
//		case 2:
//			setRegister(R2, content);
//			break;
//		case 3:
//			setRegister(R3, content);
//			break;	
//		}
//	}
	public Register getGPR(int r) {
		switch (r) {
		case 0:
			return R0;
		case 1:
			return R1;
		case 2:
			return R2;
		case 3:
			return R3;
		}
		return R0;
	}
	
	public void load() {
		int dataAddr = Util.bitSet2Int(MAR);
		int data = Util.bitSet2Int(memory.read(dataAddr));
		setRegister(MBR, data);
	}

	public void store() {
		int dataAddr = Util.bitSet2Int(MAR);
		Word data = Util.int2Word(Util.bitSet2Int(MBR));
		memory.write(data, dataAddr);
	}

	public void loadRegisterFromInput(String regStr, String input) {
		int value = Integer.parseInt(input, 2); // opcode||R||IX|I|Address
		setRegister(regStr2Name(regStr), value);
	}

	/**
	 * 
	 */
	public void operation() {
		int ea;

		switch (opcode) {
		case OpCodes.LDR:
			ea = calculateEA(i, ix, addr);
			setRegister(MAR, ea);
			int dataAddr = Util.bitSet2Int(MAR);
			int data = Util.bitSet2Int(memory.read(dataAddr));
			setRegister(MBR, data);
			switch (r) {
			case 0:
				setRegister(R0, MBR);
				break;
			case 0b1:
				setRegister(R1, MBR);
				break;
			case 0b10:
				setRegister(R2, MBR);
				break;
			case 0b11:
				setRegister(R3, MBR);
				break;

			}
			break;
		case OpCodes.STR:
			ea = calculateEA(i, ix, addr);
			setRegister(MAR, ea);
			switch (r) {
			case 0:
				setRegister(MBR, R0);
				break;
			case 0b1:
				setRegister(MBR, R1);
				break;
			case 0b10:
				setRegister(MBR, R2);
				break;
			case 0b11:
				setRegister(MBR, R3);
				break;
			}
			Util.bitSetDeepCopy(MBR, MBR.getSize(), memory.read(ea), 16);
//		     memory.write(MBR,ea); // register MBR to word
			break;

		case OpCodes.LDA:

			ea = calculateEA(i, ix, addr);
			setRegister(MAR, ea);

			setRegister(MBR, ea);
			switch (r) {
			case 0:
				setRegister(R0, MBR);
				break;
			case 0b1:
				setRegister(R1, MBR);
				break;
			case 0b10:
				setRegister(R2, MBR);
				break;
			case 0b11:
				setRegister(R3, MBR);
				break;
			}
			break;

		case OpCodes.LDX:
			ea = calculateEA((byte) 0, ix, addr);
			setRegister(MAR, ea);
			int dataAddr_1 = Util.bitSet2Int(MAR);
			int data_1 = Util.bitSet2Int(memory.read(dataAddr_1));
			setRegister(MBR, data_1);
			switch (r) {
			case 0:
				setRegister(R0, MBR);
				break;
			case 0b1:
				setRegister(R1, MBR);
				break;
			case 0b10:
				setRegister(R2, MBR);
				break;
			case 0b11:
				setRegister(R3, MBR);
				break;
			}
			break;
		case OpCodes.STX:
			ea = calculateEA((byte) 0, ix, addr);
			setRegister(MAR, ea);

			switch (r) {
			case 0:
				setRegister(MBR, R0);
				break;
			case 0b1:
				setRegister(MBR, R1);
				break;
			case 0b10:
				setRegister(MBR, R2);
				break;
			case 0b11:
				setRegister(MBR, R3);
				break;
			}
			Util.bitSetDeepCopy(MBR, MBR.getSize(), memory.read(ea), 16);
			// memory.write(MBR); register MBR to word
			break;

		case OpCodes.JZ:
			ea = calculateEA(i, ix, addr);

			// cr is c(r) register content
			int cr = -1;
			switch (r) {
			case 0:
				cr = Util.bitSet2Int(R0);
				break;
			case 0b1:
				cr = Util.bitSet2Int(R1);
				break;
			case 0b10:
				cr = Util.bitSet2Int(R2);
				break;
			case 0b11:
				cr = Util.bitSet2Int(R3);
				break;
			}

			if (cr == 0) {
				setRegister(PC, ea); // PC <- EA
			} else {
				setRegister(PC, Util.bitSet2Int(PC) + 1); // PC <- PC + 1
			}
			break;

		case OpCodes.JNE:
			ea = calculateEA(i, ix, addr);

			// cr is c(r) register content
			cr = -1;
			switch (r) {
			case 0:
				cr = Util.bitSet2Int(R0);
				break;
			case 0b1:
				cr = Util.bitSet2Int(R1);
				break;
			case 0b10:
				cr = Util.bitSet2Int(R2);
				break;
			case 0b11:
				cr = Util.bitSet2Int(R3);
				break;
			}

			if (cr != 0) {
				setRegister(PC, ea); // PC <- -EA
			} else {
				setRegister(PC, Util.bitSet2Int(PC) + 1); // PC <- PC + 1
			}
			break;

		case OpCodes.JCC:
			byte cc = (byte) ((CC.get(3 - r))? 1 : 0); // bitset reverse indexed so 3-r
			ea = calculateEA(r, ix, addr);
			if (cc == 1) {
				setRegister(PC, ea);
			} else {
				setRegister(PC, Util.bitSet2Int(PC) + 1); // PC <- PC + 1
			}
			break;

		// Jump If Condition Code
		// cc replaces r for this instruction
		// cc takes values 0, 1, 2, 3 as above and specifies
		// the bit in the Condition Code Register to check

		case OpCodes.JMA:
			ea = calculateEA(i, ix, addr);

			// Unconditional Jump To Address
			// R is ignored in this instruction

			setRegister(PC, ea); // PC <- EA
			break;

		case OpCodes.JSR:
			ea = calculateEA(i, ix, addr);

			// Jump and Save Return Address
			setRegister(R3, Util.bitSet2Int(PC) + 1);
			setRegister(PC, ea);
			break;

		case OpCodes.RFS:
			setRegister(R0, addr);
			setRegister(PC, R3);
			break;

		case OpCodes.SOB:
			ea = calculateEA(i, ix, addr);
			cr = -1;
			switch (r) {
			case 0:
				cr = Util.bitSet2Int(R0);
				setRegister(R0, cr - 1);
				break;
			case 0b1:
				cr = Util.bitSet2Int(R1);
				setRegister(R1, cr - 1);
				break;
			case 0b10:
				cr = Util.bitSet2Int(R2);
				setRegister(R2, cr - 1);
				break;
			case 0b11:
				cr = Util.bitSet2Int(R3);
				setRegister(R3, cr - 1);
				break;
			}

			if (cr >= 0) {
				setRegister(PC, ea);
			} else {
				setRegister(PC, Util.bitSet2Int(PC) + 1); // PC <- PC + 1
			}
			break;

		case OpCodes.JGE:

			ea = calculateEA(i, ix, addr);
			cr = -1;

			switch (r) {
			case 0:
				cr = Util.bitSet2Int(R0);
				break;
			case 0b1:
				cr = Util.bitSet2Int(R1);
				break;
			case 0b10:
				cr = Util.bitSet2Int(R2);
				break;
			case 0b11:
				cr = Util.bitSet2Int(R3);
				break;
			}
			if (cr >= 0) {
				setRegister(PC, ea);
			} else {
				setRegister(PC, Util.bitSet2Int(PC) + 1); // PC <- PC + 1
			}
			break;
			
		case OpCodes.MLT:
			if ((r == 0 || r == 2) && (ry == 0 || ry == 2)) {
				int crx = 0;
				int cry = 0;
				
				if (r==0) crx = Util.bitSet2Int(R0);
				else crx = Util.bitSet2Int(R2);
				
				if (ry==0) cry = Util.bitSet2Int(R0);
				else cry = Util.bitSet2Int(R2);
				
				int result = crx * cry;
				int upper = result >> 16;
				int lower = result - (upper << 16);
				
				if (upper > 131071) {
					CC.set(3); // setting OVERFLOW cc(0), which has bitIndex 3
					upper = upper - ((upper >> 16) << 16);
				} 
				
				if (r==0) {
					setRegister(R0, upper);
					setRegister(R1, lower);
				}
				else {
					setRegister(R2, upper);
					setRegister(R3, lower);
				}
			} else {
				// some kind of machine fault?
			}
			break;
			
		case OpCodes.DVD:
			if ((r == 0 || r == 2) && (ry == 0 || ry == 2)) {
				int crx = 0;
				int cry = 0;
				
				if (r==0) crx = Util.bitSet2Int(R0);
				else crx = Util.bitSet2Int(R2);
				
				if (ry==0) cry = Util.bitSet2Int(R0);
				else cry = Util.bitSet2Int(R2);
				
				if (cry == 0) {
					CC.set(1); // setting DIVZERO
					break;
				} 
				
				int result = crx / cry;
				int upper = result;
				int lower = crx - (cry * upper);
				
				if (r==0) {
					setRegister(R0, upper);
					setRegister(R1, lower);
				}
				else {
					setRegister(R2, upper);
					setRegister(R3, lower);
				}
			} else {
				// some kind of machine fault?
			}
			break;
			
		case OpCodes.TRR:
			int crx = Util.bitSet2Int(getGPR(r));
			int cry = Util.bitSet2Int(getGPR(ry));
			if (crx == cry) {
				CC.set(0); // cc(4) <- 1
			} else {
				CC.set(0, false);
			}
			break;
			
		case OpCodes.AND:
			getGPR(r).and(getGPR(ry)); 
			break;
			
		case OpCodes.ORR:
			getGPR(r).or(getGPR(ry));
			break;
			
		case OpCodes.NOT:
			getGPR(r).flip(0, getGPR(r).length());
			break;
			
			
		}
		
		
		
	}

}
