package csci6461;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.Color;

public class MainFrame extends JFrame {
	
//	 Initialise memory
	private static Simulator simulator = Simulator.getInstance();
	private static Memory memory = Memory.getInstance();

	static int mar;
	static int mbr;
	static int pc = 10;
	static int ir;
	static String ir_binary;
	static byte OPCODE;
	static byte R;
	static byte IX;
	static byte I;
	static int ADDR;
	static int pcx;
	
	
	private JPanel contentPane;
	private static JTextField textField_GPR0;
	private static JTextField textField_GPR1;
	private static JTextField textField_GPR2;
	private static JTextField textField_input;
	private static JTextField textField_GPR3;
	private static JTextField textField_IXR1;
	private static JTextField textField_IXR2;
	private static JTextField textField_IXR3;
	private static JTextField textField_PC;
	private static JTextField textField_MAR;
	private static JTextField textField_MBR;
	private static JTextField textField_IR;
	 
	private static JTextField textField_MFR;
	private static JTextField textField_Priviledged;
	private static JTextField textField_CC;
	private static JTextArea textField_Keyboard;
	private static JTextArea textField_Printer;
	private static JTextArea textField_Tag;
	private static JTextArea textField_TagValue;
	private JTextField textField_FR0;
	private JTextField textField_FR1;
	
	/*
	 * Method to get the text field object for respective registers
	 */
	
	
	private static JTextField getRegisterGUI(String regStr) {
		if (regStr == "R0") {
			return textField_GPR0;
		}
		if (regStr == "R1") {
			return textField_GPR1;
		}
		if (regStr == "R2") {
			return textField_GPR2;
		}
		if (regStr == "R3") {
			return textField_GPR3;
		}
		if (regStr == "X1") {
			return textField_IXR1;
		}
		if (regStr == "X2") {
			return textField_IXR2;
		}
		if (regStr == "X3") {
			return textField_IXR3;
		}
		if (regStr == "PC") {
			return textField_PC;
		}
		if (regStr == "MAR") {
			return textField_MAR;
		}
		if (regStr == "MBR") {
			return textField_MBR;
		}
		if (regStr == "IR") {
			return textField_IR;
		}
		if (regStr == "CC") {
			return textField_CC;
		}
		return null;
	}
	
	
	/*
	 * 
	 */
	public static void updateRegUI(String regStr, BitSet value, int length) {
		JTextField reg = getRegisterGUI(regStr);
		// no update if no corresponding GUI element
		if (reg == null) {
			return;
		}
		String result = Integer.toBinaryString(Util.bitSet2Int(value));
		int zeros = length - result.length();
		for (int i = 0; i < zeros; i++) {
			result = "0" + result;
		}
		reg.setText(result);
	}
	
	public static void updateCacheUI(ArrayList<Integer> tag, ArrayList<Word> value) {
		String strTag = "";
		String strVal = "";
		for (int i = 0; i < tag.size(); i++) {
			strTag += tag.get(i).toString() + '\n';
			strVal += Util.bitSet2Int(value.get(i)) + "\n";
		}
		textField_Tag.setText(strTag);
		textField_TagValue.setText(strVal);
	}
		
	public static int getKeyboard() {
		String text = textField_Keyboard.getText();
		String token = text.split(",")[0]; // get first element
		int result;
		try {
			result = Integer.parseInt(token);
		} catch (NumberFormatException n) {
			result = Integer.parseInt(String.valueOf(token.charAt(0)));
		}
		if (text.indexOf(",") != -1)
			text = text.substring(text.indexOf(",") + 1);
		else text = "";
		textField_Keyboard.setText(text);
		return result;
	}
	
	public static void setPrinter(int output) {
		textField_Printer.setText(textField_Printer.getText() + output);
	}
	public static void setPrinter(String output) {
		textField_Printer.setText(textField_Printer.getText() + output);
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
//		System.out.println(pc);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Simulator UI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1256, 764);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("GPR 0");
		lblNewLabel.setBounds(25, 32, 63, 24);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("GPR 1");
		lblNewLabel_1.setBounds(25, 82, 45, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("GPR 2");
		lblNewLabel_2.setBounds(25, 118, 45, 13);
		contentPane.add(lblNewLabel_2);
		
		textField_GPR0 = new JTextField();
		textField_GPR0.setEditable(false);
		textField_GPR0.setBounds(136, 35, 245, 19);
		textField_GPR0.setText("0000000000000000");
		contentPane.add(textField_GPR0);
		textField_GPR0.setColumns(10);
		
		textField_GPR1 = new JTextField();
		textField_GPR1.setEditable(false);
		textField_GPR1.setBounds(136, 79, 245, 19);
		textField_GPR1.setText("0000000000000000");
		contentPane.add(textField_GPR1);
		textField_GPR1.setColumns(10);
		
		textField_GPR2 = new JTextField();
		textField_GPR2.setEditable(false);
		textField_GPR2.setBounds(136, 115, 245, 19);
		textField_GPR2.setText("0000000000000000");
		contentPane.add(textField_GPR2);
		textField_GPR2.setColumns(10);
		
		JButton GPR0_load = new JButton("LD");
		GPR0_load.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("R0", textField_input.getText());

			}
		});
		GPR0_load.setBounds(391, 35, 85, 21);
		contentPane.add(GPR0_load);
		
		JButton GPR1_load = new JButton("LD");
		GPR1_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("R1", textField_input.getText());
			}
		});
		GPR1_load.setBounds(391, 79, 85, 21);
		contentPane.add(GPR1_load);
		
		JButton GPR2_load = new JButton("LD");
		GPR2_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("R2", textField_input.getText());
			}
		});
		GPR2_load.setBounds(391, 115, 85, 21);
		contentPane.add(GPR2_load);
		
		textField_input = new JTextField();
		textField_input.setBounds(90, 410, 358, 19);
		contentPane.add(textField_input);
		textField_input.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Input");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setBounds(90, 437, 358, 13);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_2_1 = new JLabel("GPR 3");
		lblNewLabel_2_1.setBounds(25, 155, 45, 13);
		contentPane.add(lblNewLabel_2_1);
		
		textField_GPR3 = new JTextField();
		textField_GPR3.setEditable(false);
		textField_GPR3.setColumns(10);
		textField_GPR3.setBounds(136, 152, 245, 19);
		textField_GPR3.setText("0000000000000000");
		contentPane.add(textField_GPR3);
		
		JButton GPR3_load = new JButton("LD");
		GPR3_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("R3", textField_input.getText());
			}
		});
		GPR3_load.setBounds(391, 152, 85, 21);
		contentPane.add(GPR3_load);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("IXR 1");
		lblNewLabel_2_1_1.setBounds(25, 196, 45, 13);
		contentPane.add(lblNewLabel_2_1_1);
		
		textField_IXR1 = new JTextField();
		textField_IXR1.setEditable(false);
		textField_IXR1.setColumns(10);
		textField_IXR1.setBounds(136, 193, 245, 19);
		textField_IXR1.setText("0000000000000000");
		contentPane.add(textField_IXR1);
		
		JButton IXR1_load = new JButton("LD");
		IXR1_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("X1", textField_input.getText());
			}
		});
		IXR1_load.setBounds(391, 193, 85, 21);
		contentPane.add(IXR1_load);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("IXR 2");
		lblNewLabel_2_1_1_1.setBounds(25, 233, 45, 13);
		contentPane.add(lblNewLabel_2_1_1_1);
		
		textField_IXR2 = new JTextField();
		textField_IXR2.setEditable(false);
		textField_IXR2.setColumns(10);
		textField_IXR2.setBounds(136, 230, 245, 19);
		textField_IXR2.setText("0000000000000000");

		contentPane.add(textField_IXR2);
		
		JButton IXR2_load = new JButton("LD");
		IXR2_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("X2", textField_input.getText());
			}
		});
		IXR2_load.setBounds(391, 230, 85, 21);
		contentPane.add(IXR2_load);
		
		textField_IXR3 = new JTextField();
		textField_IXR3.setEditable(false);
		textField_IXR3.setColumns(10);
		textField_IXR3.setBounds(136, 272, 245, 19);
		textField_IXR3.setText("0000000000000000");

		contentPane.add(textField_IXR3);
		
		JLabel lblNewLabel_2_1_1_1_1 = new JLabel("IXR 3");
		lblNewLabel_2_1_1_1_1.setBounds(25, 275, 45, 13);
		contentPane.add(lblNewLabel_2_1_1_1_1);
		
		JButton IXR3_load = new JButton("LD");
		IXR3_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("X3", textField_input.getText());
			}
		});
		IXR3_load.setBounds(391, 272, 85, 21);
		contentPane.add(IXR3_load);
		
		JLabel lblPc = new JLabel("PC");
		lblPc.setBounds(673, 32, 63, 24);
		contentPane.add(lblPc);
		
		textField_PC = new JTextField();
		textField_PC.setEditable(false);
		textField_PC.setColumns(10);
		textField_PC.setBounds(784, 35, 313, 19);
		textField_PC.setText("000000000000");
		
		contentPane.add(textField_PC);
		
		JButton PC_load = new JButton("LD");
		PC_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("PC", textField_input.getText());
			}
		});
		PC_load.setBounds(1107, 35, 85, 21);
		contentPane.add(PC_load);
		
		JLabel lblNewLabel_4_1 = new JLabel("MAR");
		lblNewLabel_4_1.setBounds(673, 82, 63, 24);
		contentPane.add(lblNewLabel_4_1);
		
		textField_MAR = new JTextField();
		textField_MAR.setEditable(false);
		textField_MAR.setColumns(10);
		textField_MAR.setBounds(784, 85, 313, 19);
		textField_MAR.setText("000000000000");

		contentPane.add(textField_MAR);
		
		JButton MAR_load = new JButton("LD");
		MAR_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("MAR", textField_input.getText());
			}
		});
		MAR_load.setBounds(1107, 85, 85, 21);
		contentPane.add(MAR_load);
		
		JLabel lblNewLabel_4_1_1 = new JLabel("MBR");
		lblNewLabel_4_1_1.setBounds(673, 118, 63, 24);
		contentPane.add(lblNewLabel_4_1_1);
		
		textField_MBR = new JTextField();
		textField_MBR.setEditable(false);
		textField_MBR.setColumns(10);
		textField_MBR.setBounds(784, 121, 313, 19);
		textField_MBR.setText("0000000000000000");
		contentPane.add(textField_MBR);
		
		JButton MBR_load = new JButton("LD");
		MBR_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("MBR", textField_input.getText());
			}
		});
		MBR_load.setBounds(1107, 121, 85, 21);
		contentPane.add(MBR_load);
		
		JLabel lblNewLabel_4_1_1_1 = new JLabel("IR");
		lblNewLabel_4_1_1_1.setBounds(673, 160, 63, 24);
		contentPane.add(lblNewLabel_4_1_1_1);
		
		textField_IR = new JTextField();
		textField_IR.setEditable(false);
		textField_IR.setColumns(10);
		textField_IR.setBounds(784, 163, 313, 19);
		textField_IR.setText("0000000000000000");
		contentPane.add(textField_IR);
		
		JButton btnNewButton_3 = new JButton("Load");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.load();
			}
		});
		btnNewButton_3.setBounds(673, 353, 74, 37);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Store");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.store();
			}
		});
		btnNewButton_4.setBounds(673, 396, 74, 37);
		
		contentPane.add(btnNewButton_4);
		
		JButton SS_button = new JButton("SS");
		SS_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.singleStep();
			}
		});
		SS_button.setBounds(854, 365, 94, 59);
		contentPane.add(SS_button);
		
		JButton Run_button = new JButton("Run");
		Run_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.runProgram();
			}
		});
		Run_button.setBounds(958, 365, 94, 59);
		contentPane.add(Run_button);
		
		JButton Init_Button = new JButton("INIT");
		Init_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.init("./src/csci6461/boot.txt");
			}
		});
		Init_Button.setBounds(754, 376, 74, 37);
		contentPane.add(Init_Button);
		
		JLabel lblNewLabel_4_1_1_2 = new JLabel("MFR");
		lblNewLabel_4_1_1_2.setBounds(673, 196, 63, 24);
		contentPane.add(lblNewLabel_4_1_1_2);
		
		textField_MFR = new JTextField();
		textField_MFR.setText("0000");
		textField_MFR.setEditable(false);
		textField_MFR.setColumns(10);
		textField_MFR.setBounds(784, 199, 313, 19);
		contentPane.add(textField_MFR);
		
		JLabel lblNewLabel_4_1_1_3 = new JLabel("Priviledged");
		lblNewLabel_4_1_1_3.setBounds(673, 238, 85, 24);
		contentPane.add(lblNewLabel_4_1_1_3);
		
		textField_Priviledged = new JTextField();
		textField_Priviledged.setText("0");
		textField_Priviledged.setEditable(false);
		textField_Priviledged.setColumns(10);
		textField_Priviledged.setBounds(784, 241, 313, 19);
		contentPane.add(textField_Priviledged);
		
		JLabel lblNewLabel_4_1_1_3_1 = new JLabel("CC");
		lblNewLabel_4_1_1_3_1.setBounds(673, 288, 85, 24);
		contentPane.add(lblNewLabel_4_1_1_3_1);
		
		textField_CC = new JTextField();
		textField_CC.setText("0000");
		textField_CC.setEditable(false);
		textField_CC.setColumns(10);
		textField_CC.setBounds(784, 291, 313, 19);
		contentPane.add(textField_CC);
		
		JButton CC_Button = new JButton("LD");
		CC_Button.setBounds(1107, 290, 85, 21);
		CC_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("CC", textField_input.getText());
			}
		});
		contentPane.add(CC_Button);
		
		textField_Keyboard = new JTextArea();
		textField_Keyboard.setLineWrap(true);
		textField_Keyboard.setBounds(673, 479, 223, 183);
		contentPane.add(textField_Keyboard);
		textField_Keyboard.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Keyboard Console");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setBounds(673, 672, 223, 13);
		contentPane.add(lblNewLabel_4);
		
		textField_Printer = new JTextArea();
		JScrollPane scrollPane = new JScrollPane (textField_Printer, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textField_Printer.setEditable(false);
		textField_Printer.setLineWrap(true);
		textField_Printer.setColumns(10);
		scrollPane.setBounds(939, 479, 223, 183);
		contentPane.add(scrollPane);

		
		JLabel lblNewLabel_4_2 = new JLabel("Printer Display");
		lblNewLabel_4_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_2.setBounds(939, 670, 223, 17);
		contentPane.add(lblNewLabel_4_2);
		
		textField_Tag = new JTextArea();
		JScrollPane scrollPaneTag = new JScrollPane (textField_Tag, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textField_Tag.setBackground(Color.WHITE);
		textField_Tag.setEditable(false);
		textField_Tag.setColumns(10);
		scrollPaneTag.setBounds(25, 483, 223, 183);
		contentPane.add(scrollPaneTag);
		
		textField_TagValue = new JTextArea();
		JScrollPane scrollPaneTagValue = new JScrollPane (textField_TagValue, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textField_TagValue.setBackground(Color.WHITE);
		textField_TagValue.setEditable(false);
		textField_TagValue.setColumns(10);
		scrollPaneTagValue.setBounds(279, 483, 223, 183);
		contentPane.add(scrollPaneTagValue);
		
		
		JLabel lblNewLabel_5 = new JLabel("CACHE");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setBounds(158, 676, 223, 23);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Tag");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setBounds(25, 460, 223, 13);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_6_1 = new JLabel("Value");
		lblNewLabel_6_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6_1.setBounds(279, 460, 223, 13);
		contentPane.add(lblNewLabel_6_1);
		
		JButton Program1_Button = new JButton("Program 1");
		Program1_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.init("./src/csci6461/program1.txt");
				if (textField_Keyboard.getText().isEmpty()) {
					setPrinter("using default value...\n");
					textField_Keyboard.setText("17827,8255,23642,1412,870,3177,2639,231,2899,18,1109,31,88,973,100,1281,1241,713,119,240,5000");
				}
					
				String msg = "Array of 20 integers:\n";
				setPrinter(msg);
				int k;
				// write array to memory and print in console output
				for (int i = 0; i < 20; i++) {
					k = getKeyboard();
					memory.write(Util.int2Word(k), 800 + i + 1);
					setPrinter(k);
					if (i < 19) setPrinter(","); 
					else setPrinter("\n");
				}
				// write search number to memory and print in console output
				k = getKeyboard();
				memory.write(Util.int2Word(k), 400);
				msg = "Search number: ";
				setPrinter(msg);
				setPrinter(k);
				setPrinter("\n");
			}
		});
		Program1_Button.setBounds(1075, 353, 157, 21);
		contentPane.add(Program1_Button);
		
		JButton Program2_Button = new JButton("Program 2");
		Program2_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.init("./src/csci6461/program2.txt");
				// store input paragraph
				if (textField_Keyboard.getText().isEmpty()) {
					setPrinter("Using default value...\n");
					String defaultInput = "Courage and stupidity were all he had."
							+ "The thunderous roar of the jet overhead confirmed her worst fears."
							+ "He had concluded that pigs must be able to fly in Hog Heaven."
							+ "Peanuts don't grow on trees, but cashews do."
							+ "He picked up trash in his spare time to dump in his neighbor's yard."
							+ "It's difficult to understand the lengths he'd go to remain short.";
					textField_Keyboard.setText(defaultInput);
				}
				String input = textField_Keyboard.getText();
				setPrinter(input + '\n');
				for (int i = 0; i < input.length(); i++) {
					int c = input.charAt(i);
					memory.write(Util.int2Word(c), 800 + i);
				}
				memory.write(Util.int2Word(input.length()+ 800), 36);
				
				// store search word
				textField_Keyboard.setText("");
				setPrinter("Search word: ");
				String searchWord = "roar";
				for (int i = 0; i < searchWord.length(); i++) {
					int c = searchWord.charAt(i);
					memory.write(Util.int2Word(c), 700 + i);
				}
				memory.write(Util.int2Word(searchWord.length() + 700), 35);
				setPrinter(searchWord);
				setPrinter("\n");
				
			}
		});
		Program2_Button.setBounds(1075, 384, 157, 21);
		contentPane.add(Program2_Button);
		
		JLabel lblNewLabel_2_1_1_1_1_1 = new JLabel("FR 0");
		lblNewLabel_2_1_1_1_1_1.setBounds(25, 315, 45, 13);
		contentPane.add(lblNewLabel_2_1_1_1_1_1);
		
		textField_FR0 = new JTextField();
		textField_FR0.setText("0000000000000000");
		textField_FR0.setEditable(false);
		textField_FR0.setColumns(10);
		textField_FR0.setBounds(136, 312, 245, 19);
		contentPane.add(textField_FR0);
		
		JButton FR0_load = new JButton("LD");
		FR0_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("X3", textField_input.getText());
			}
		});
		FR0_load.setBounds(391, 312, 85, 21);
		contentPane.add(FR0_load);
		
		JLabel lblNewLabel_2_1_1_1_1_2 = new JLabel("FR 1");
		lblNewLabel_2_1_1_1_1_2.setBounds(25, 353, 45, 13);
		contentPane.add(lblNewLabel_2_1_1_1_1_2);
		
		textField_FR1 = new JTextField();
		textField_FR1.setText("0000000000000000");
		textField_FR1.setEditable(false);
		textField_FR1.setColumns(10);
		textField_FR1.setBounds(136, 350, 245, 19);
		contentPane.add(textField_FR1);
		
		JButton FR1_load = new JButton("LD");
		FR1_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("X3", textField_input.getText());
			}
		});
		FR1_load.setBounds(391, 350, 85, 21);
		contentPane.add(FR1_load);
		
		JButton Program3_Button = new JButton("Program 3");
		Program3_Button.setBounds(1075, 409, 157, 21);
		Program3_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.init("./src/csci6461/program3.txt");

			}
		});
		contentPane.add(Program3_Button);
		
		simulator.init("./src/csci6461/boot.txt");
	}


	public static void clearPrinter() {
		textField_Printer.setText("");
	}
}
