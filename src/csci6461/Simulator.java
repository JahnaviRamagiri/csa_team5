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

	
	private byte opcode;
	
	private byte ix;
	private byte r;
	private byte i;
	private byte addr;
	
	private byte ry;
	
	private byte al;
	private byte lr;
	private byte count;
	
	private byte dev_id;
	private byte trap_code;
	
	private File f;
	 
	// an incremental variable to count the number of instructions read
	private int lines; 

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

		lines = 0; // keep track of lines of instructions read from file

	}

	public static Simulator getInstance() {
		return INSTANCE;
	}

	private static Memory memory = Memory.getInstance();

	public void init(String path) {
		lines = 0;
		loadFile(path);
		try {
			Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				String s1 = s.nextLine();
				String[] sa = s1.split(" ");
				// setting the memory
				int addr = Integer.parseInt(sa[0].trim(), 16);
				
				if (lines == 0) {
					setRegister(PC, addr);
				}

				Word content = Util.int2Word(Integer.parseInt(sa[1].trim(), 16));
				memory.write(content, addr);
				lines++;
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
		setRegister(MAR, 0);
		setRegister(MBR, 0);
		setRegister(MFR, 0);
		setRegister(IR, 0);
		setRegister(CC, 0);

	}

	public int singleStep() {

		// fetch instruction
		loadInstruction();
		// ir decode

		int ir = Util.bitSet2Int(IR);
		irDecode(ir);
		// operation
		return getInstance().operation();
		
	}
	
	public int runProgram() {
		while (singleStep() == 0);
		if (f.getName().equals("program1.txt")) {
			MainFrame.setPrinter("closest number: " + Util.bitSet2Int(memory.read(202)) + "\n");
		}
		if (f.getName().equals("program2.txt")) {
			int i_word = Util.bitSet2Int(memory.read(30));
			int i_sentence = Util.bitSet2Int(memory.read(31));
			if (i_word == 0 || i_sentence == 0) {
				MainFrame.setPrinter("Word not found.");
			}
			MainFrame.setPrinter("Word found at sentence " + i_sentence + ", word " + i_word + "\n");
		}
		return 1;
	}

	public void loadFile(String path) {
		f = new File(path);
	}

	/**
	 * load instruction at given address
	 * 
	 * @param input
	 */
	public void loadInstruction() {

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
			i = (byte) Integer.parseInt(ir_binary.substring(10, 11), 2);
			addr = (byte) Integer.parseInt(ir_binary.substring(11, 16), 2);
		} else if (opcode >= 20 &&opcode <= 25) {
			r = (byte) Integer.parseInt(ir_binary.substring(6, 8), 2);
			ry = (byte) Integer.parseInt(ir_binary.substring(8, 10), 2);
		} else if (opcode == 30) {
			trap_code = (byte) Integer.parseInt(ir_binary.substring(12, 16), 2);
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

	public int calculateEA(byte i, byte ix, byte address) {
		int ea = 0; // return value
		// no indirect
		if (i == 0) {
			if (ix == 0) {
				ea = address;
				return ea;
			} else if (ix <= 3 && ix >= 1) {
				ea = Util.bitSet2Int(getIXR(ix)) + address;
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
				int tmpAddr = Util.bitSet2Int(getIXR(ix)) + address;
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
		if (r == CC) {
			return "CC";
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
		if (r == "CC") {
			return CC;
		}
		return null;

	}

	public void setRegister(Register r, int content) {
		BitSet w = Util.int2BitSet(content);
		Util.bitSetDeepCopy(w, 16, r, r.getSize());
		MainFrame.updateRegUI(regName2Str(r), r, r.getSize());

	}

	public void setRegister(Register r, BitSet src) {
		int srcData = Util.bitSet2Int(src);
		setRegister(r, srcData);

	}
	
	public void setRegisterSigned(Register r, int content) {
		BitSet w = Util.int2BitSetSigned(content);
		Util.bitSetDeepCopy(w, 16, r, r.getSize());
		MainFrame.updateRegUI(regName2Str(r), r, r.getSize());
	}

	public void setCC(int i, boolean bit) {
		int bitIndex = 3 - i;
		//OVERFLOW = 0
		//UNDERFLOW = 1
		//DIVZERO = 2
		//EQUALORNOT = 3
		CC.set(bitIndex, bit);
		MainFrame.updateRegUI("CC", CC, 4);
	}
	
	public byte getCC(int i) {
		int bitIndex = 3 - i;
		//OVERFLOW = 0
		//UNDERFLOW = 1
		//DIVZERO = 2
		//EQUALORNOT = 3
		boolean r = CC.get(bitIndex);
		if (r) return 1;
		else return 0;
	}
	
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
	public Register getIXR(int ix) {
		switch (ix) {
		case 1:
			return X1;
		case 2:
			return X2;
		case 3:
			return X3;
		}
		return X1;
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
	public int operation() {
		int ea;

		switch (opcode) {
		case OpCodes.LDR:
			ea = calculateEA(i, ix, addr);
			setRegister(MAR, ea);
			int dataAddr = Util.bitSet2Int(MAR);
			int data = Util.bitSet2IntSigned(memory.read(dataAddr));
			setRegisterSigned(MBR, data);
			setRegister(getGPR(r), MBR);
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			System.out.println("LDR R" + r + ", @$" + dataAddr);
			break;
		case OpCodes.STR:
			System.out.println("STR");
			ea = calculateEA(i, ix, addr);
			setRegister(MAR, ea);
			setRegisterSigned(MBR, Util.bitSet2IntSigned(getGPR(r)));
			memory.write(Util.bitSet2IntSigned(MBR), Util.bitSet2Int(MAR));
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			System.out.println(Util.bitSet2IntSigned(MBR) + " @ $" + Util.bitSet2Int(MAR));
			break;

		case OpCodes.LDA:
			System.out.println("LDA");
			ea = calculateEA(i, ix, addr);
			setRegister(MAR, ea);
			setRegister(MBR, ea);
			setRegister(getGPR(r), MBR);
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;

		case OpCodes.LDX:
			System.out.println("LDX");
			
			ea = calculateEA((byte) 0, (byte) 0, addr);
			setRegister(MAR, ea);
			int dataAddr_1 = Util.bitSet2Int(MAR);
			int data_1 = Util.bitSet2Int(memory.read(dataAddr_1));
			setRegister(MBR, data_1);
			setRegister(getIXR(ix), MBR);
			System.out.println(Util.bitSet2Int(getIXR(ix)) + " ea:"+ ea + " dataAddr:" + dataAddr_1);
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
		case OpCodes.STX:
			System.out.println("STX");
			ea = calculateEA((byte) 0, ix, addr);
			setRegister(MAR, ea);
			setRegister(MBR, getIXR(ix));
			memory.write(Util.bitSet2Int(MBR), Util.bitSet2Int(MAR));
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;

		case OpCodes.JZ:
			System.out.println("JZ");
			ea = calculateEA(i, ix, addr);

			// cr is c(r) register content
			int cr = Util.bitSet2Int(getGPR(r));

			if (cr == 0) {
				setRegister(PC, ea); // PC <- EA
			} else {
				setRegister(PC, Util.bitSet2Int(PC) + 1); // PC <- PC + 1
			}
			break;

		case OpCodes.JNE:
			System.out.println("JNE");
			ea = calculateEA(i, ix, addr);

			// cr is c(r) register content
			cr = Util.bitSet2Int(getGPR(r));

			if (cr != 0) {
				setRegister(PC, ea); // PC <- -EA
			} else {
				setRegister(PC, Util.bitSet2Int(PC) + 1); // PC <- PC + 1
			}
			break;

		case OpCodes.JCC:
			System.out.println("JCC");
			byte cc = getCC(r); 
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
			System.out.println("JMA");
			ea = calculateEA(i, ix, addr);
			// Unconditional Jump To Address
			// R is ignored in this instruction
			setRegister(PC, ea); // PC <- EA
			break;

		case OpCodes.JSR:
			System.out.println("JSR");
			ea = calculateEA(i, ix, addr);

			// Jump and Save Return Address
			setRegister(R3, Util.bitSet2Int(PC) + 1);
			setRegister(PC, ea);
			break;

		case OpCodes.RFS:
			System.out.println("RFS");
			setRegister(R0, addr);
			setRegister(PC, R3);
			break;

		case OpCodes.SOB:
			System.out.println("SOB");
			ea = calculateEA(i, ix, addr);
			
			cr = Util.bitSet2IntSigned(getGPR(r));
			setRegisterSigned(getGPR(r), cr - 1);
			System.out.println(Util.bitSet2IntSigned(getGPR(r)));
			if (cr >= 0) {
				setRegister(PC, ea);
			} else {
				setRegister(PC, Util.bitSet2Int(PC) + 1); // PC <- PC + 1
			}
			break;

		case OpCodes.JGE:
			System.out.println("JGE");
			ea = calculateEA(i, ix, addr);
			cr = -1;

			cr = Util.bitSet2IntSigned(getGPR(r));
			System.out.println(cr);
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
				
				crx = Util.bitSet2Int(getGPR(r));
				cry = Util.bitSet2Int(getGPR(ry));
				
				int result = crx * cry;
				int upper = result >> 16;
				int lower = result - (upper << 16);
				
				if (upper > (2^16 - 1)) {
					setCC(Constants.CC_OVERFLOW, true); // setting OVERFLOW cc(0)
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
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.DVD:
			if ((r == 0 || r == 2) && (ry == 0 || ry == 2)) {
				int crx = 0;
				int cry = 0;
				
				crx = Util.bitSet2Int(getGPR(r));
				cry = Util.bitSet2Int(getGPR(ry));
				
				if (cry == 0) {
					setCC(Constants.CC_DIVZERO, true); // setting DIVZERO
					setRegister(PC, Util.bitSet2Int(PC) + 1);
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
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.TRR:
			int crx = Util.bitSet2Int(getGPR(r));
			int cry = Util.bitSet2Int(getGPR(ry));
			if (crx == cry) {
				setCC(Constants.CC_EQUALORNOT, true); // cc(4) <- 1
			} else {
				setCC(Constants.CC_EQUALORNOT, false);
			}
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.AND:
			System.out.println("AND");
			getGPR(r).and(getGPR(ry)); 
			setRegister(getGPR(r), getGPR(r));
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.ORR:
			System.out.println("ORR");
			getGPR(r).or(getGPR(ry));
			setRegister(getGPR(r), getGPR(r));
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.NOT:
			System.out.println("NOT");
			getGPR(r).flip(0, getGPR(r).length());
			setRegister(getGPR(r), getGPR(r));
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.AMR:
			System.out.println("AMR");
			ea = calculateEA(i, ix, addr);
			setRegister(MAR, ea);
			dataAddr = Util.bitSet2Int(MAR);
			data = Util.bitSet2Int(memory.read(dataAddr));
			setRegister(MBR, data);
			int result = Util.bitSet2IntSigned(getGPR(r)) + Util.bitSet2IntSigned(MBR);
			setRegisterSigned(getGPR(r), result);
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.SMR:
			System.out.println("SMR");
			ea = calculateEA(i, ix, addr);
			setRegister(MAR, ea);
			dataAddr = Util.bitSet2Int(MAR);
			data = Util.bitSet2Int(memory.read(dataAddr));
			setRegister(MBR, data);
			
			result = Util.bitSet2IntSigned(getGPR(r)) - Util.bitSet2IntSigned(MBR);
			System.out.println(ea + " " +i + " " + ix + " " + addr);
			System.out.println(data + " @ $" + dataAddr);

			System.out.println(Util.bitSet2IntSigned(getGPR(r)) + "-" +Util.bitSet2IntSigned(MBR)+"=" + result);
			setRegisterSigned(getGPR(r), result);
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			
			break;

		case OpCodes.AIR:
			System.out.println("AIR");
			result = Util.bitSet2IntSigned(getGPR(r)) + addr;
			setRegisterSigned(getGPR(r), result);
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			System.out.println(Util.bitSet2IntSigned(getGPR(r)) + " + " + addr + " = " + result);
			break;

		case OpCodes.SIR:
			System.out.println("SIR");
			result = Util.bitSet2IntSigned(getGPR(r)) - addr;
			setRegisterSigned(getGPR(r), result);
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.SRC:
			System.out.println("SRC");
			// arithmetic
			if (al == 0) {
				if (lr == 0) {
					result = Util.bitSet2IntSigned(getGPR(r)) << count;
					if (result > Constants.SIGNED_MAX || result < Constants.SIGNED_MIN) {
						setCC(Constants.CC_OVERFLOW, true);
					}
					setRegisterSigned(getGPR(r), result);
				}
					
				else if (lr == 1) {
					result = Util.bitSet2IntSigned(getGPR(r)) >> count;
					if (Util.bitSet2IntSigned(getGPR(r)) != (result << count)) {
						setCC(Constants.CC_UNDERFLOW, true);
					}
					setRegisterSigned(getGPR(r), result);
				}
					
			}
			// logical
			else if (al == 1) {
				if (lr == 0) {
					result = Util.bitSet2Int(getGPR(r)) << count;
					if (result > Constants.UNSIGNED_MAX) {
						setCC(Constants.CC_OVERFLOW, true);
					}
					setRegister(getGPR(r), result);
				}
				else if (lr == 1) {
					result = Util.bitSet2Int(getGPR(r)) >>> count;
					if (Util.bitSet2Int(getGPR(r)) != (result << count)) {
						setCC(Constants.CC_UNDERFLOW, true);
					}
					setRegister(getGPR(r), result);
				}
			}
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.RRC:
			System.out.println("RRC");
			cr = Util.bitSet2Int(getGPR(r));
			if (al == 1) {
				if (lr == 0)
					setRegister(getGPR(r), (cr << count) | (cr >> (16 - count)));
				else if (lr == 1)
					setRegister(getGPR(r), (cr >> count) | (cr << (16 - count)));
			}
			setRegister(PC, Util.bitSet2Int(PC) + 1);
		
			break;
			
		case OpCodes.IN:
			// Get devid
			// devid = 0 keyboard
			int inp = 0;
			if (dev_id == 0) {
				inp = MainFrame.getKeyboard();
				setRegister(getGPR(r), inp);	
			}
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.OUT:
			// devid = 1 printer
			if (dev_id == 1) {
				MainFrame.setPrinter(Util.bitSet2Int(getGPR(r)));
			}
			setRegister(PC, Util.bitSet2Int(PC) + 1);
			break;
			
		case OpCodes.CHK:
			break;
		
		case OpCodes.HLT:
			System.out.println("HLT");
			return 1;

		case OpCodes.TRAP:
			int sub_addr = Util.bitSet2Int(memory.read(0));
			memory.write(Util.bitSet2Int(PC) + 1, 2);
			sub_addr += trap_code;
			setRegister(PC, memory.read(sub_addr));
			runProgram();
			setRegister(PC, memory.read(2));
			break;
			
		}
		
		
		return 0;
		
	}

}
