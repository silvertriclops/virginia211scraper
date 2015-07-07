package getResults;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class StatusApp extends JFrame {
	private static final long serialVersionUID = 6661551473755757657L;
	JPanel panel = new JPanel();
	public JProgressBar progressBar = new JProgressBar();
	public JTextField txtStatus = new JTextField();
	private final Component verticalStrut = Box.createVerticalStrut(5);
	
	public StatusApp() {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		panel.setBorder(new EmptyBorder(10, 5, 10, 5));
		
		getContentPane().add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		txtStatus.setHorizontalAlignment(SwingConstants.CENTER);
		txtStatus.setEditable(false);
		panel.add(txtStatus);
		txtStatus.setColumns(10);
		
		panel.add(verticalStrut);
		
		progressBar.setStringPainted(true);
		panel.add(progressBar);
		
		pack();
		setSize(500, getHeight());
		setMinimumSize(getSize());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, InterruptedException  {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new StatusApp();
	}

}
