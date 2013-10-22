package ch.zhaw.dbru.inf3.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import ch.zhaw.dbru.inf3.compiler.AssemblerCompiler;
import ch.zhaw.dbru.inf3.compiler.exception.AssemblerCompilationException;
import ch.zhaw.dbru.inf3.emulator.MPCConstants;
import ch.zhaw.dbru.inf3.emulator.itf.EmulationController;
import ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler;
import ch.zhaw.dbru.inf3.emulator.logic.BinaryUtils;
import ch.zhaw.dbru.inf3.memory.Memory;

public class GuiEmulator extends JFrame implements EmulationHandler,
		ActionListener {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -2254232311377521648L;

	private static final String SLID_BFZ = "bfz";
	private static final String SLID_BFR = "bfr";
	private static final String SLID_R0 = "r0";
	private static final String SLID_R1 = "r1";
	private static final String SLID_R2 = "r2";
	private static final String SLID_R3 = "r3";
	private static final String SLID_CF = "cf";

	private AssemblerCompiler compiler;
	private EmulationController controller;

	private JPanel progOverviewPanel;

	private JScrollPane progPane;
	private JTable progTable;
	private ListTableModel progTableModel;

	private JScrollPane progCompPane;
	private JTable progCompTable;
	private ListTableModel progCompTableModel;

	private JPanel memoryPanel;
	private JScrollPane memoryPane;
	private JTable memoryTable;
	private MemoryTableModel memoryTableModel;

	private JPanel commandPanel;
	private JButton loadScriptBtn;
	private JButton clearBtn;
	private JButton nextBtn;

	private JPanel settingsPanel;
	private JPanel statePanel;

	private JFileChooser fileChooser;

	private HashMap<String, JLabel> stateValueLabelBin;
	private HashMap<String, JLabel> stateValueLabelDez;
	private HashMap<String, JLabel> stateLabel;

	/**
	 * Creates a new GUI-Emulator
	 */
	public GuiEmulator(EmulationController aController,
			AssemblerCompiler aCompiler) {
		super("Mini-Power-PC");

		controller = aController;

		controller.registerHandler(this);

		compiler = aCompiler;
		fileChooser = new JFileChooser();

		initComponents();

		// Set Frame settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(600, 380));

		// Set visible
		pack();
		setVisible(true);
	}

	private void initComponents() {

		//TODO: SplitPane
		
		// Create overview panel for code and compiled code
		progOverviewPanel = new JPanel();
		progOverviewPanel.setLayout(new GridLayout(1, 2));

		progTableModel = new ListTableModel();
		progTable = new JTable(progTableModel);
		progPane = new JScrollPane(progTable);
		progOverviewPanel.add(progPane);

		progCompTableModel = new ListTableModel();
		progCompTable = new JTable(progCompTableModel);
		progCompPane = new JScrollPane(progCompTable);
		progOverviewPanel.add(progCompPane);

		// Create settings panel
		settingsPanel = new JPanel();

		initStateComponents();

		// Create memory panel
		memoryPanel = new JPanel();
		memoryTableModel = new MemoryTableModel(controller.getMemory());
		memoryTable = new JTable(memoryTableModel);
		memoryPane = new JScrollPane(memoryTable);
		memoryPanel.add(memoryPane);

		// Create command panel
		commandPanel = new JPanel();
		loadScriptBtn = new JButton("Programm laden");
		clearBtn = new JButton("Zurücksetzen");
		nextBtn = new JButton("Step");

		nextBtn.setEnabled(false);
		
		loadScriptBtn.addActionListener(this);
		clearBtn.addActionListener(this);
		nextBtn.addActionListener(this);

		commandPanel.setLayout(new FlowLayout());
		commandPanel.add(loadScriptBtn);
		commandPanel.add(clearBtn);
		commandPanel.add(nextBtn);

		// Set to main panel
		getContentPane().setLayout(new BorderLayout());

		getContentPane().add(settingsPanel, BorderLayout.NORTH);
		getContentPane().add(progOverviewPanel, BorderLayout.CENTER);
		getContentPane().add(statePanel, BorderLayout.WEST);
		getContentPane().add(memoryPanel, BorderLayout.EAST);
		getContentPane().add(commandPanel, BorderLayout.SOUTH);
	}

	private void initStateComponents() {
		stateValueLabelBin = new HashMap<String, JLabel>();
		stateValueLabelDez = new HashMap<String, JLabel>();
		stateLabel = new HashMap<String, JLabel>();

		// Create state panel
		statePanel = new JPanel();
		statePanel.setLayout(new GridLayout(7, 3, 8, 0));

		createLabelRecord(SLID_BFR, "Befehlsregister:");
		createLabelRecord(SLID_BFZ, "Befehlszähler:");
		createLabelRecord(SLID_CF, "Carry Flag:");
		createLabelRecord(SLID_R0, "Akkumulator:");
		createLabelRecord(SLID_R1, "Register 1:");
		createLabelRecord(SLID_R2, "Register 2:");
		createLabelRecord(SLID_R3, "Register 3:");
	}

	/**
	 * Creates a new record.
	 * 
	 * @param anId
	 *            the id
	 * @param aLabel
	 *            the label
	 */
	private void createLabelRecord(String anId, String aLabel) {

		stateValueLabelBin.put(anId, new JLabel("", JLabel.CENTER));
		stateValueLabelDez.put(anId, new JLabel("", JLabel.CENTER));
		stateLabel.put(anId, new JLabel(aLabel, JLabel.RIGHT));

		statePanel.add(stateLabel.get(anId));
		statePanel.add(stateValueLabelBin.get(anId));
		statePanel.add(stateValueLabelDez.get(anId));

	}

	/**
	 * Sets the record values
	 * 
	 * @param anId
	 *            the id.
	 * @param aBinaryValue
	 *            the binary value.
	 */
	private void setRecordValue(String anId, String aBinaryValue) {
		stateValueLabelBin.get(anId).setText(aBinaryValue);
		stateValueLabelDez.get(anId).setText(
				Integer.parseInt(aBinaryValue, 2) + "");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler#updateBefehslzaehler(
	 * java.util.BitSet)
	 */
	@Override
	public void updateCommandCounter(BitSet aBfz) {
		setRecordValue(SLID_BFZ,
				StringUtils.reverse(BinaryUtils.convertBitSetToString(aBfz, MPCConstants.BF_LENGTH)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler#updateRegisters(java.
	 * util.BitSet[])
	 */
	@Override
	public void updateRegisters(BitSet[] someRegisters) {
		for (int i = 0; i < someRegisters.length; i++) {
			setRecordValue("r" + i, StringUtils.reverse(BinaryUtils
					.convertBitSetToString(someRegisters[i], MPCConstants.BF_LENGTH)));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler#updateCommandRegister
	 * (java.util.BitSet)
	 */
	@Override
	public void updateCommandRegister(BitSet aCr) {
		setRecordValue(SLID_BFR,
				StringUtils.reverse(BinaryUtils.convertBitSetToString(aCr, MPCConstants.BF_LENGTH)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler#updateCarryFlag(boolean)
	 */
	@Override
	public void updateCarryFlag(boolean aFlag) {
		setRecordValue(SLID_CF, aFlag ? "1" : "0");
	}

	/* (non-Javadoc)
	 * @see ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler#updateMemory(ch.zhaw.dbru.inf3.emulator.logic.Memory)
	 */
	@Override
	public void updateMemory(Memory aMemory) {
		memoryTableModel.setMemory(aMemory);
		memoryTable.updateUI();
	}
	
	/* (non-Javadoc)
	 * @see ch.zhaw.dbru.inf3.emulator.itf.EmulationHandler#stepFinished()
	 */
	@Override
	public void stepFinished() {
		nextBtn.setEnabled(false);
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent anEvent) {
		Object source = anEvent.getSource();

		if (source.equals(loadScriptBtn)) {
			processActionLoadScriptFile();
		}else if (source.equals(nextBtn)){
			nextBtn.setEnabled(false);
			controller.step();
		}
	}

	/**
	 * Process Action for 'LoadScript'.
	 */
	private void processActionLoadScriptFile() {
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File inFile = fileChooser.getSelectedFile();
			if (inFile.exists()) {
				try {
					// Get file content.
					List<String> fileContent = FileUtils.readLines(inFile);

					// Set data to table and update ui.
					progTableModel.setData(fileContent);
					progTable.updateUI();

					// Compile data
					try {
						BitSet[] binData = compiler.compile(fileContent);
						List<String> binStrData = new ArrayList<String>(
								binData.length);

						// Set compiled data to ui and update it.
						for (BitSet bs : binData) {
							binStrData.add(BinaryUtils
									.convertBitSetToString(bs, controller.getMemory().getFullWidth()));
						}
						progCompTableModel.setData(binStrData);
						progCompTable.updateUI();

						// load compiled programm into memory.
						BitSet anAddr = controller.loadProgramToMemory(binData);
						controller.startProgramm(anAddr);
						nextBtn.setEnabled(true);
						memoryTable.updateUI();
					} catch (AssemblerCompilationException e) {
						JOptionPane.showMessageDialog(this,
								"Beim Kompilieren ist ein Fehler aufgetreten:\nZeile:"
										+ e.getCodeLine() + ": " + e.getCode()
										+ "\n\n" + e.getMessage(),
								"Fehler beim Kompilieren...",
								JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException e) {
					JOptionPane
							.showMessageDialog(
									this,
									"Beim Einlesen der Datei ist ein Fehler aufgetreten!",
									"Fehler...", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			} else {
				JOptionPane.showMessageDialog(this,
						"Die ausgewählte Datei konnte nicht gefunden werden!",
						"Fehler...", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
}
