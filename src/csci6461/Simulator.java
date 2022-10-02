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

	private Register OPCODE;
	private Register IX;
	private Register R;
	private Register I;
	private Register ADDR;

	private File f;
	private ArrayList<Integer> instructionAddr = new ArrayList<>();
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
//		CC = new Register(4);

		MAR = new Register(12);
		MBR = new Register(16);
//		MFR = new Register(4);

		OPCODE = new Register(6);
		IX = new Register(2);
		R = new Register(2);
		I = new Register(1);
		ADDR = new Register(5);

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
		String ir_binary = Integer.toBinaryString(ir);
//		ir[16] == 
//			ir[0:5] = opcode
//			ir[6:7] = Register
//			ir[8:9] = Index
//			ir[10] = Instruction
//			ir[11:15] = Address

		int zeros = 16 - ir_binary.length();
		for (int i = 0; i < zeros; i++) {
			ir_binary = "0" + ir_binary;
		}

		setRegister(OPCODE, Integer.parseInt(ir_binary.substring(0, 6), 2));
		setRegister(R, Integer.parseInt(ir_binary.substring(6, 8), 2));
		setRegister(IX, Integer.parseInt(ir_binary.substring(8, 10), 2));
		setRegister(I, Integer.parseInt(ir_binary.substring(10), 2));
		setRegister(ADDR, Integer.parseInt(ir_binary.substring(11, 16), 2));

	}

	public static int calculateEA(byte i, byte ix, int address) {
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
		int value = Integer.parseInt(input,2); // opcode||R||IX|I|Address
		setRegister(regStr2Name(regStr), value);
	}
	
	/**
	 * 
	 */
	public void operation() {
		int ea;
		byte opcode = (byte) Util.bitSet2Int(OPCODE);
		byte r = (byte) Util.bitSet2Int(R);
		byte i = (byte) Util.bitSet2Int(I);
		byte ix = (byte) Util.bitSet2Int(IX);
		int address = Util.bitSet2Int(ADDR);
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
			ea = calculateEA(i, ix, address);
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

			ea = calculateEA(i, ix, address);
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
			ea = calculateEA((byte) 0, ix, address);
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
			ea = calculateEA((byte) 0, ix, address);
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
		}

	}

}
