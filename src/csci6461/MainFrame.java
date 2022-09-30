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
import java.util.BitSet;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {
	
//	 Initialise memory
	static int[] MEM = new int[4096];
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
	
	
	private JPanel contentPane;
	private JTextField textField_GPR0;
	private JTextField textField_GPR1;
	private JTextField textField_GPR2;
	private JTextField textField_input;
	private JTextField textField_GPR3;
	private JTextField textField_IXR1;
	private JTextField textField_IXR2;
	private JTextField textField_IXR3;
	private JTextField textField_PC;
	private JTextField textField_MAR;
	private JTextField textField_MBR;
	private JTextField textField_IR;
	
	static void load_instruction(String input) {
		
//		String st1=input.getText();
		
		
		pc = Integer.parseInt(input,2);
		mar = pc;
		mbr = MEM[mar];
		ir = mbr;
		
		
				
		
	    System.out.println("I just got executed!");
	  }
	
	static void ir_decode(int ir) {
		ir_binary = Integer.toBinaryString(ir);
//		ir[16] == 
//			ir[0:5] = opcode
//			ir[6:7] = Register
//			ir[8:9] = Index
//			ir[10] = Instruction
//			ir[11:15] = Address
		
		// Convert to bytes
		OPCODE = (byte)Integer.parseInt(ir_binary.substring(0,6), 2);
		R = (byte)Integer.parseInt(ir_binary.substring(6,8),2);
		IX = (byte)Integer.parseInt(ir_binary.substring(8,10),2);
		I = (byte)Integer.parseInt(ir_binary.substring(10),2);
		ADDR = Integer.parseInt(ir_binary.substring(11,16),2);
		
		
		
		
		
	}
	
//	public static void updateUI(String register, BitSet value) {
//		try {
//			JTextField[] curr_reg = Registers.get(register);
//			curr_reg.setText(Integer.toBinaryString(pc))
//			
////			for (int i = 0; i < curr_reg.length; i++) {
////				if (value.get(i)) {
////					curr_reg[i].setSelected(true);
////				} else {
////					curr_reg[i].setSelected(false);
////				}
////			}
//		} catch (Exception e) {
//
//		}
//	}

	
//	public static void updateUI() {
//		
//	}
		

	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.out.println(pc);
		
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
		setBounds(100, 100, 1377, 698);
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
		contentPane.add(textField_GPR0);
		textField_GPR0.setColumns(10);
		
		textField_GPR1 = new JTextField();
		textField_GPR1.setEditable(false);
		textField_GPR1.setBounds(136, 181, 245, 19);
		contentPane.add(textField_GPR1);
		textField_GPR1.setColumns(10);
		
		textField_GPR2 = new JTextField();
		textField_GPR2.setEditable(false);
		textField_GPR2.setBounds(136, 217, 245, 19);
		contentPane.add(textField_GPR2);
		textField_GPR2.setColumns(10);
		
		JButton GPR0_load = new JButton("Load");
		GPR0_load.addActionListener(new ActionListener() {
			/*
			 * take input from 'input'
			 * load to register GPR0
			 * 
			 * 
			 * 
			*/
//			load_input
			public void actionPerformed(ActionEvent e) {
				String st1 = textField_input.getText(); // opcode||R||IX|I|Address
				
				textField_GPR0.setText(st1);

			}
		});
		GPR0_load.setBounds(391, 137, 85, 21);
		contentPane.add(GPR0_load);
		
		JButton GPR1_load = new JButton("Load");
		GPR1_load.setBounds(391, 181, 85, 21);
		contentPane.add(GPR1_load);
		
		JButton GPR2_load = new JButton("Load");
		GPR2_load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		contentPane.add(textField_GPR3);
		
		JButton GPR3_load = new JButton("Load");
		GPR3_load.setBounds(391, 254, 85, 21);
		contentPane.add(GPR3_load);
		
		JLabel lblNewLabel_2_1_1 = new JLabel("IXR 1");
		lblNewLabel_2_1_1.setBounds(25, 298, 45, 13);
		contentPane.add(lblNewLabel_2_1_1);
		
		textField_IXR1 = new JTextField();
		textField_IXR1.setEditable(false);
		textField_IXR1.setColumns(10);
		textField_IXR1.setBounds(136, 295, 245, 19);
		contentPane.add(textField_IXR1);
		
		JButton IXR1_load = new JButton("Load");
		IXR1_load.setBounds(391, 295, 85, 21);
		contentPane.add(IXR1_load);
		
		JLabel lblNewLabel_2_1_1_1 = new JLabel("IXR 2");
		lblNewLabel_2_1_1_1.setBounds(25, 335, 45, 13);
		contentPane.add(lblNewLabel_2_1_1_1);
		
		textField_IXR2 = new JTextField();
		textField_IXR2.setEditable(false);
		textField_IXR2.setColumns(10);
		textField_IXR2.setBounds(136, 332, 245, 19);
		contentPane.add(textField_IXR2);
		
		JButton IXR2_load = new JButton("Load");
		IXR2_load.setBounds(391, 332, 85, 21);
		contentPane.add(IXR2_load);
		
		textField_IXR3 = new JTextField();
		textField_IXR3.setEditable(false);
		textField_IXR3.setColumns(10);
		textField_IXR3.setBounds(136, 374, 245, 19);
		contentPane.add(textField_IXR3);
		
		JLabel lblNewLabel_2_1_1_1_1 = new JLabel("IXR 3");
		lblNewLabel_2_1_1_1_1.setBounds(25, 377, 45, 13);
		contentPane.add(lblNewLabel_2_1_1_1_1);
		
		JButton IXR3_load = new JButton("Load");
		IXR3_load.setBounds(391, 374, 85, 21);
		contentPane.add(IXR3_load);
		
		JLabel lblPc = new JLabel("PC");
		lblPc.setBounds(670, 134, 63, 24);
		contentPane.add(lblPc);
		
		textField_PC = new JTextField();
		textField_PC.setEditable(false);
		textField_PC.setColumns(10);
		textField_PC.setBounds(781, 137, 313, 19);
		contentPane.add(textField_PC);
		
		JButton PC_load = new JButton("Load");
		PC_load.setBounds(1104, 137, 85, 21);
		contentPane.add(PC_load);
		
		JLabel lblNewLabel_4_1 = new JLabel("MAR");
		lblNewLabel_4_1.setBounds(670, 184, 63, 24);
		contentPane.add(lblNewLabel_4_1);
		
		textField_MAR = new JTextField();
		textField_MAR.setEditable(false);
		textField_MAR.setColumns(10);
		textField_MAR.setBounds(781, 187, 313, 19);
		contentPane.add(textField_MAR);
		
		JButton MAR_load = new JButton("Load");
		MAR_load.setBounds(1104, 187, 85, 21);
		contentPane.add(MAR_load);
		
		JLabel lblNewLabel_4_1_1 = new JLabel("MBR");
		lblNewLabel_4_1_1.setBounds(670, 220, 63, 24);
		contentPane.add(lblNewLabel_4_1_1);
		
		textField_MBR = new JTextField();
		textField_MBR.setEditable(false);
		textField_MBR.setColumns(10);
		textField_MBR.setBounds(781, 223, 313, 19);
		contentPane.add(textField_MBR);
		
		JButton MBR_load = new JButton("Load");
		MBR_load.setBounds(1104, 223, 85, 21);
		contentPane.add(MBR_load);
		
		JLabel lblNewLabel_4_1_1_1 = new JLabel("IR");
		lblNewLabel_4_1_1_1.setBounds(670, 262, 63, 24);
		contentPane.add(lblNewLabel_4_1_1_1);
		
		textField_IR = new JTextField();
		textField_IR.setEditable(false);
		textField_IR.setColumns(10);
		textField_IR.setBounds(781, 265, 313, 19);
		contentPane.add(textField_IR);
		
		JButton btnNewButton_3 = new JButton("Load");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String st1 = textField_input.getText(); // opcode||R||IX|I|Address
				
				
				load_instruction(st1);
				textField_PC.setText(Integer.toBinaryString(pc));
				textField_MAR.setText(Integer.toBinaryString(mar));
				textField_MBR.setText(Integer.toBinaryString(mbr));
				System.out.println(pc);
//				textField_PC
			}
		});
		btnNewButton_3.setBounds(768, 408, 186, 21);
		contentPane.add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Store");
		btnNewButton_4.setBounds(964, 408, 199, 21);
		contentPane.add(btnNewButton_4);
		
		JButton SS_button = new JButton("SS");
		SS_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// input ir
				// ir decode
				ir_decode(ir);
				// operation
				Simulator.getInstance().operation(OPCODE, R, I, IX, ADDR);
//				getRegister()
				
			}
		});
		SS_button.setBounds(831, 455, 85, 21);
		contentPane.add(SS_button);
		
		JButton Run_button = new JButton("Run");
		Run_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		Run_button.setBounds(926, 455, 85, 21);
		contentPane.add(Run_button);
		
		JButton Init_Button = new JButton("INIT");
		Init_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		Init_Button.setBounds(1032, 455, 85, 21);
		contentPane.add(Init_Button);
	}
}
