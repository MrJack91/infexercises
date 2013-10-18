package ch.zhaw.dbru.inf3.emulator.out;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import ch.zhaw.dbru.inf3.emulator.itf.EmulationController;
import ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler;

public class GuiEmulator extends JFrame implements EmulationHandler {

	private static final String SLID_BFZ = "bfz";
	private static final String SLID_BFR = "bfr";
	private static final String SLID_R0 = "r0";
	private static final String SLID_R1 = "r1";
	private static final String SLID_R2 = "r2";
	private static final String SLID_R3 = "r3";
	private static final String SLID_CF = "cf";
	
	private EmulationController controller;

	private JPanel progOverviewPanel;
	
	private JScrollPane progPane;
	private JTable progTable;
	
	private JScrollPane progCompPane;
	private JTable progCompTable;
	
	private JPanel memoryPanel;
	private JScrollPane memoryPane;
	private JTable memoryTable;

	private JPanel commandPanel;
	private JPanel settingsPanel;
	private JPanel statePanel;

	private HashMap<String, JLabel> stateValueLabelBin;
	private HashMap<String, JLabel> stateValueLabelDez;
	private HashMap<String, JLabel> stateLabel;

	/**
	 * Creates a new GUI-Emulator
	 */
	public GuiEmulator(EmulationController aController) {
		super("Mini-Power-PC");

		initComponents();

		// Set Frame settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(600, 380));

		// Set visible
		pack();
		setVisible(true);
	}

	private void initComponents() {

		// Create overview panel for code and compiled code
		progOverviewPanel = new JPanel();
		progOverviewPanel.setLayout(new GridLayout(1, 2));

		progTable = new JTable();
		progPane = new JScrollPane(progTable);
		progOverviewPanel.add(progPane);

		progCompTable = new JTable();
		progCompPane = new JScrollPane(progCompTable);
		progOverviewPanel.add(progCompPane);

		// Create settings panel
		settingsPanel = new JPanel();

		initStateComponents();

		//Create memory panel
		memoryPanel = new JPanel();
		
		// Create command panel
		commandPanel = new JPanel();

		// Set to main panel
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(settingsPanel, BorderLayout.NORTH);
		getContentPane().add(progOverviewPanel, BorderLayout.CENTER);
		getContentPane().add(statePanel, BorderLayout.EAST);
		getContentPane().add(memoryPanel, BorderLayout.WEST);
		getContentPane().add(commandPanel, BorderLayout.SOUTH);
	}

	private void initStateComponents() {
		stateValueLabelBin = new HashMap<String, JLabel>();
		stateValueLabelDez = new HashMap<String, JLabel>();
		stateLabel = new HashMap<String, JLabel>();
		
		// Create state panel
		statePanel = new JPanel();
		statePanel.setLayout(new GridLayout(7,3));
		
		createLabelRecord(SLID_BFR,"Befehlsregister:");
		createLabelRecord(SLID_BFZ,"Befehlszähler:");
		createLabelRecord(SLID_CF,"Carry Flag:");
		createLabelRecord(SLID_R0,"Akkumulator:");
		createLabelRecord(SLID_R1,"Register 1:");
		createLabelRecord(SLID_R2,"Register 2:");
		createLabelRecord(SLID_R3,"Register 3:");
	}

	/**
	 * Creates a new record.
	 * 
	 * @param anId the id
	 * @param aLabel the label
	 */
	private void createLabelRecord(String anId, String aLabel){
		stateValueLabelBin.put(anId, new JLabel("")); 
		stateValueLabelDez.put(anId, new JLabel("")); 
		stateLabel.put(anId, new JLabel(aLabel));
		
		statePanel.add(stateValueLabelBin.get(anId));
		statePanel.add(stateValueLabelDez.get(anId));
		statePanel.add(stateLabel.get(anId));
	}
	
	/**
	 * Sets the record values
	 * 
	 * @param anId the id.
	 * @param aBinaryValue the binary value.
	 */
	private void setRecordValue(String anId, String aBinaryValue){
		stateValueLabelBin.get(anId).setText(aBinaryValue);
		stateValueLabelDez.get(anId).setText(Integer.parseInt(aBinaryValue, 2) + "");
	}
	
	@Override
	public void updateBefehslzaehler(byte[] aBefehlszaehler) {
		// TODO Auto-generated method stub

	}

}
