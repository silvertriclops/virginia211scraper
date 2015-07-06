package me.silvertriclops.getcarescraper;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JProgressBar;

public class App extends JFrame {
	private JFileChooser chooser = new JFileChooser();
	private JTextField saveLocation;
	JPanel panel = new JPanel();
	
	public App() {
		setTitle("Virginia 211 Scraper");
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(panel_4);
		panel_4.setLayout(new BoxLayout(panel_4, BoxLayout.Y_AXIS));
		panel_4.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblSaveFolder = new JLabel("File to save: ");
		GridBagConstraints gbc_lblSaveFolder = new GridBagConstraints();
		gbc_lblSaveFolder.insets = new Insets(0, 0, 5, 5);
		gbc_lblSaveFolder.anchor = GridBagConstraints.EAST;
		gbc_lblSaveFolder.gridx = 0;
		gbc_lblSaveFolder.gridy = 0;
		panel.add(lblSaveFolder, gbc_lblSaveFolder);
		
		saveLocation = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel.add(saveLocation, gbc_textField);
		saveLocation.setColumns(10);
		
		JButton btnBrowse = new JButton("Browse...");
		GridBagConstraints gbc_btnBrowse = new GridBagConstraints();
		gbc_btnBrowse.insets = new Insets(0, 0, 5, 0);
		gbc_btnBrowse.gridx = 2;
		gbc_btnBrowse.gridy = 0;
		panel.add(btnBrowse, gbc_btnBrowse);
		
		JButton btnStart = new JButton("Start");
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.gridx = 2;
		gbc_btnStart.gridy = 1;
		gbc_btnStart.fill = GridBagConstraints.HORIZONTAL;
		panel.add(btnStart, gbc_btnStart);
		
		JPanel panel_1 = new JPanel();
		panel_4.add(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[] {0, 0};
		gbl_panel_2.rowHeights = new int[] {0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0};
		gbl_panel_2.rowWeights = new double[]{0.0, 0.0};
		panel_2.setLayout(gbl_panel_2);
		
		JLabel lblNeed = new JLabel("Need:");
		GridBagConstraints gbc_lblNeed = new GridBagConstraints();
		gbc_lblNeed.anchor = GridBagConstraints.EAST;
		gbc_lblNeed.insets = new Insets(0, 0, 5, 5);
		gbc_lblNeed.gridx = 0;
		gbc_lblNeed.gridy = 0;
		panel_2.add(lblNeed, gbc_lblNeed);
		
		textField = new JTextField();
		textField.setEditable(false);
		GridBagConstraints gbc_textField1 = new GridBagConstraints();
		gbc_textField1.insets = new Insets(0, 0, 5, 0);
		gbc_textField1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField1.gridx = 1;
		gbc_textField1.gridy = 0;
		panel_2.add(textField, gbc_textField1);
		textField.setColumns(10);
		
		JLabel lblProgram = new JLabel("Program:");
		GridBagConstraints gbc_lblProgram = new GridBagConstraints();
		gbc_lblProgram.anchor = GridBagConstraints.EAST;
		gbc_lblProgram.insets = new Insets(0, 0, 5, 5);
		gbc_lblProgram.gridx = 0;
		gbc_lblProgram.gridy = 1;
		panel_2.add(lblProgram, gbc_lblProgram);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		panel_2.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JPanel panel_6 = new JPanel();
		panel_1.add(panel_6);
		
		JPanel panel_3 = new JPanel();
		panel_4.add(panel_3);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setMaximum(150);
		progressBar.setValue(4);
		progressBar.setStringPainted(true);
		panel_3.add(progressBar);
		btnBrowse.addActionListener(chooseDir);

		chooser.setSelectedFile(new File("scrape.csv"));
		chooser.setCurrentDirectory(new File("user.home"));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
		chooser.setFileFilter(filter);
		
		//pack();
		setSize(new Dimension(462, 231));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	ActionListener chooseDir = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			chooser.setDialogTitle("Save to:");
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setAcceptAllFileFilterUsed(true);
			if (chooser.showSaveDialog(panel) == JFileChooser.APPROVE_OPTION) { 
				System.out.println("getCurrentDirectory(): " +  chooser.getCurrentDirectory());
				System.out.println("getSelectedFile() : " +  chooser.getSelectedFile());
				saveLocation.setText(chooser.getSelectedFile().toString());
			}
		}
	};
	private JTextField textField;
	private JTextField textField_1;

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new App();
	}

}
