package csci6461;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.BitSet;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
	
//	 Initialise memory
	private static Simulator simulator = Simulator.getInstance();

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
	private static JTextField textField_Keyboard;
	private static JTextField textField_Printer;
	
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
	public static void updateUI(String regStr, BitSet value, int length) {
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
		
	public static int getKeyboard() {
		String text = textField_Keyboard.getText();
		int result;
		try {
			result = Integer.parseInt(text, 0);
		} catch (NumberFormatException n) {
			result = Integer.parseInt(String.valueOf(text.charAt(0)));
		}
		return result;
	}
	
	public static void setPrinter(int output) {
		textField_Printer.setText("" + (char)output);
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
		setBounds(100, 100, 1471, 924);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("GPR 0");
		lblNewLabel.setBounds(25, 134, 63, 24);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("GPR 1");
		lblNewLabel_1.setBounds(25, 184, 45, 13);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("GPR 2");
		lblNewLabel_2.setBounds(25, 220, 45, 13);
		contentPane.add(lblNewLabel_2);
		
		textField_GPR0 = new JTextField();
		textField_GPR0.setEditable(false);
		textField_GPR0.setBounds(136, 137, 245, 19);
		textField_GPR0.setText("0000000000000000");
		contentPane.add(textField_GPR0);
		textField_GPR0.setColumns(10);
		
		textField_GPR1 = new JTextField();
		textField_GPR1.setEditable(false);
		textField_GPR1.setBounds(136, 181, 245, 19);
		textField_GPR1.setText("0000000000000000");
		contentPane.add(textField_GPR1);
		textField_GPR1.setColumns(10);
		
		textField_GPR2 = new JTextField();
		textField_GPR2.setEditable(false);
		textField_GPR2.setBounds(136, 217, 245, 19);
		textField_GPR2.setText("0000000000000000");
		contentPane.add(textField_GPR2);
		textField_GPR2.setColumns(10);
		
		JButton GPR0_load = new JButton("LD");
		GPR0_load.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("R0", textField_input.getText());

			}
		});
		GPR0_load.setBounds(391, 137, 85, 21);
		contentPane.add(GPR0_load);
		
		JButton GPR1_load = new JButton("LD");
		GPR1_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("R1", textField_input.getText());
			}
		});
		GPR1_load.setBounds(391, 181, 85, 21);
		contentPane.add(GPR1_load);
		
		JButton GPR2_load = new JButton("LD");
		GPR2_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("R2", textField_input.getText());
			}
		});
		GPR2_load.setBounds(391, 217, 85, 21);
		contentPane.add(GPR2_load);
		
		textField_input = new JTextField();
		textField_input.setBounds(91, 456, 358, 19);
		contentPane.add(textField_input);
		textField_input.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Input");
		lblNewLabel_3.setBounds(228, 483, 147, 13);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_2_1 = new JLabel("GPR 3");
		lblNewLabel_2_1.setBounds(25, 257, 45, 13);
		contentPane.add(lblNewLabel_2_1);
		
		textField_GPR3 = new JTextField();
		textField_GPR3.setEditable(false);
		textField_GPR3.setColumns(10);
		textField_GPR3.setBounds(136, 254, 245, 19);
		textField_GPR3.setText("0000000000000000");
		contentPane.add(textField_GPR3);
		
		JButton GPR3_load = new JButton("LD");
		GPR3_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("R3", textField_input.getText());
			}
		});
		GPR3_load.setBounds(391, 254, 85, 21);
		contentPane.add(GPR3_load);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("IXR 1");
		lblNewLabel_2_1_1.setBounds(25, 298, 45, 13);
		contentPane.add(lblNewLabel_2_1_1);
		
		textField_IXR1 = new JTextField();
		textField_IXR1.setEditable(false);
		textField_IXR1.setColumns(10);
		textField_IXR1.setBounds(136, 295, 245, 19);
		textField_IXR1.setText("0000000000000000");
		contentPane.add(textField_IXR1);
		
		JButton IXR1_load = new JButton("LD");
		IXR1_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("X1", textField_input.getText());
			}
		});
		IXR1_load.setBounds(391, 295, 85, 21);
		contentPane.add(IXR1_load);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("IXR 2");
		lblNewLabel_2_1_1_1.setBounds(25, 335, 45, 13);
		contentPane.add(lblNewLabel_2_1_1_1);
		
		textField_IXR2 = new JTextField();
		textField_IXR2.setEditable(false);
		textField_IXR2.setColumns(10);
		textField_IXR2.setBounds(136, 332, 245, 19);
		textField_IXR2.setText("0000000000000000");

		contentPane.add(textField_IXR2);
		
		JButton IXR2_load = new JButton("LD");
		IXR2_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("X2", textField_input.getText());
			}
		});
		IXR2_load.setBounds(391, 332, 85, 21);
		contentPane.add(IXR2_load);
		
		textField_IXR3 = new JTextField();
		textField_IXR3.setEditable(false);
		textField_IXR3.setColumns(10);
		textField_IXR3.setBounds(136, 374, 245, 19);
		textField_IXR3.setText("0000000000000000");

		contentPane.add(textField_IXR3);
		
		JLabel lblNewLabel_2_1_1_1_1 = new JLabel("IXR 3");
		lblNewLabel_2_1_1_1_1.setBounds(25, 377, 45, 13);
		contentPane.add(lblNewLabel_2_1_1_1_1);
		
		JButton IXR3_load = new JButton("LD");
		IXR3_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("X3", textField_input.getText());
			}
		});
		IXR3_load.setBounds(391, 374, 85, 21);
		contentPane.add(IXR3_load);
		
		JLabel lblPc = new JLabel("PC");
		lblPc.setBounds(670, 134, 63, 24);
		contentPane.add(lblPc);
		
		textField_PC = new JTextField();
		textField_PC.setEditable(false);
		textField_PC.setColumns(10);
		textField_PC.setBounds(781, 137, 313, 19);
		textField_PC.setText("000000000000");
		
		contentPane.add(textField_PC);
		
		JButton PC_load = new JButton("LD");
		PC_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("PC", textField_input.getText());
			}
		});
		PC_load.setBounds(1104, 137, 85, 21);
		contentPane.add(PC_load);
		
		JLabel lblNewLabel_4_1 = new JLabel("MAR");
		lblNewLabel_4_1.setBounds(670, 184, 63, 24);
		contentPane.add(lblNewLabel_4_1);
		
		textField_MAR = new JTextField();
		textField_MAR.setEditable(false);
		textField_MAR.setColumns(10);
		textField_MAR.setBounds(781, 187, 313, 19);
		textField_MAR.setText("000000000000");

		contentPane.add(textField_MAR);
		
		JButton MAR_load = new JButton("LD");
		MAR_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("MAR", textField_input.getText());
			}
		});
		MAR_load.setBounds(1104, 187, 85, 21);
		contentPane.add(MAR_load);
		
		JLabel lblNewLabel_4_1_1 = new JLabel("MBR");
		lblNewLabel_4_1_1.setBounds(670, 220, 63, 24);
		contentPane.add(lblNewLabel_4_1_1);
		
		textField_MBR = new JTextField();
		textField_MBR.setEditable(false);
		textField_MBR.setColumns(10);
		textField_MBR.setBounds(781, 223, 313, 19);
		textField_MBR.setText("0000000000000000");
		contentPane.add(textField_MBR);
		
		JButton MBR_load = new JButton("LD");
		MBR_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("MBR", textField_input.getText());
			}
		});
		MBR_load.setBounds(1104, 223, 85, 21);
		contentPane.add(MBR_load);
		
		JLabel lblNewLabel_4_1_1_1 = new JLabel("IR");
		lblNewLabel_4_1_1_1.setBounds(670, 262, 63, 24);
		contentPane.add(lblNewLabel_4_1_1_1);
		
		textField_IR = new JTextField();
		textField_IR.setEditable(false);
		textField_IR.setColumns(10);
		textField_IR.setBounds(781, 265, 313, 19);
		textField_IR.setText("0000000000000000");
		contentPane.add(textField_IR);
		
		JButton btnNewButton_3 = new JButton("Load");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.load();
			}
		});
		btnNewButton_3.setBounds(775, 531, 74, 37);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Store");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.store();
			}
		});
		btnNewButton_4.setBounds(775, 574, 74, 37);
		
		contentPane.add(btnNewButton_4);
		
		JButton SS_button = new JButton("SS");
		SS_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (simulator.singleStep() == 1) {
					SS_button.setEnabled(false);;
				}
			}
		});
		SS_button.setBounds(956, 543, 94, 59);
		contentPane.add(SS_button);
		
		JButton Run_button = new JButton("Run");
		Run_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				while (simulator.singleStep() == 0) {
				}
			}
		});
		Run_button.setBounds(1060, 543, 94, 59);
		contentPane.add(Run_button);
		
		JButton Init_Button = new JButton("INIT");
		Init_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.init();
			}
		});
		Init_Button.setBounds(856, 554, 74, 37);
		contentPane.add(Init_Button);
		
		JLabel lblNewLabel_4_1_1_2 = new JLabel("MFR");
		lblNewLabel_4_1_1_2.setBounds(670, 298, 63, 24);
		contentPane.add(lblNewLabel_4_1_1_2);
		
		textField_MFR = new JTextField();
		textField_MFR.setText("0000");
		textField_MFR.setEditable(false);
		textField_MFR.setColumns(10);
		textField_MFR.setBounds(781, 301, 313, 19);
		contentPane.add(textField_MFR);
		
		JLabel lblNewLabel_4_1_1_3 = new JLabel("Priviledged");
		lblNewLabel_4_1_1_3.setBounds(670, 340, 85, 24);
		contentPane.add(lblNewLabel_4_1_1_3);
		
		textField_Priviledged = new JTextField();
		textField_Priviledged.setText("0");
		textField_Priviledged.setEditable(false);
		textField_Priviledged.setColumns(10);
		textField_Priviledged.setBounds(781, 343, 313, 19);
		contentPane.add(textField_Priviledged);
		
		JLabel lblNewLabel_4_1_1_3_1 = new JLabel("CC");
		lblNewLabel_4_1_1_3_1.setBounds(670, 390, 85, 24);
		contentPane.add(lblNewLabel_4_1_1_3_1);
		
		textField_CC = new JTextField();
		textField_CC.setText("0000");
		textField_CC.setEditable(false);
		textField_CC.setColumns(10);
		textField_CC.setBounds(781, 393, 313, 19);
		contentPane.add(textField_CC);
		
		JButton CC_Button = new JButton("LD");
		CC_Button.setBounds(1104, 392, 85, 21);
		CC_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simulator.loadRegisterFromInput("CC", textField_input.getText());
			}
		});
		contentPane.add(CC_Button);
		
		textField_Keyboard = new JTextField();
		textField_Keyboard.setBounds(30, 531, 121, 24);
		contentPane.add(textField_Keyboard);
		textField_Keyboard.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("Keyboard Console");
		lblNewLabel_4.setBounds(40, 566, 94, 13);
		contentPane.add(lblNewLabel_4);
		
		textField_Printer = new JTextField();
		textField_Printer.setEditable(false);
		textField_Printer.setColumns(10);
		textField_Printer.setBounds(30, 624, 121, 118);
		contentPane.add(textField_Printer);
		
		JLabel lblNewLabel_4_2 = new JLabel("Printer Display");
		lblNewLabel_4_2.setBounds(49, 752, 85, 17);
		contentPane.add(lblNewLabel_4_2);
		
		simulator.init();
	}
}
