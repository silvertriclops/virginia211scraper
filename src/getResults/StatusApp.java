package getResults;

import java.awt.Dimension;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StatusApp extends JFrame {
	private static final long serialVersionUID = 6661551473755757657L;
	JPanel panel = new JPanel();
	public JProgressBar progressBar = new JProgressBar();
	
	public StatusApp() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(10, 5, 10, 5));
		
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		progressBar.setStringPainted(true);
		panel.add(progressBar);
		
		pack();
		setSize(500, getHeight());
		setMinimumSize(getSize());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/*public static void progmax(int m) {
		progressBar.setMaximum(m);
	}
	
	public static void progstat(int m) {
		progressBar.setValue(m);
	}*/
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, InterruptedException  {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new StatusApp();
	}

}
