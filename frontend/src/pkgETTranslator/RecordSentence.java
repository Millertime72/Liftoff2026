package pkgETTranslator;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane; 

public class RecordSentence extends JPanel {
	private static JFrame mainFrame;
	private int recordTime;
	private static final long serialVersionUID = 1L;
	private JTextField txtSpeechDuration;
	private String sentence;
	
	/**
	 * Create the panel.
	 */
	public RecordSentence() {
		setLayout(null);
		mainFrame = TranslatorFrame.frame;
		this.setPreferredSize(new Dimension(800, 600)); 
		
		String currentInput = "None";
		
		JButton btnContinue = new JButton("Continue");
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//read sentence file
				TranslatorReader.readSentenceFile(sentence); 
				
				//continue to next panel
				TranslatorReader.switchToImgSelect();
			}
		});
		btnContinue.setBounds(6, 307, 117, 29);
		add(btnContinue);
		//disable continue on launch
		btnContinue.setEnabled(false);
		
		JButton btnRecord = new JButton("Record");
		btnRecord.setBounds(6, 154, 117, 29);
		add(btnRecord);
		//start disabled
		btnRecord.setEnabled(false);
		
		JEditorPane displaySentence = new JEditorPane();
		displaySentence.setBounds(6, 195, 698, 93);
		add(displaySentence);
		displaySentence.setEnabled(false);
		
		txtSpeechDuration = new JTextField();
		txtSpeechDuration.setBounds(16, 6, 130, 26);
		add(txtSpeechDuration);
		txtSpeechDuration.setColumns(10);
		
		JButton btnInputSpeechDuration = new JButton("Input Speech Duration");
		btnInputSpeechDuration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//retrieve input
				String inputString = txtSpeechDuration.getText();
				if (isInteger(inputString.trim())) {
					//enable record
					btnRecord.setEnabled(true);
					//save time
					recordTime = Integer.parseInt(inputString);
				} else {
					//show error message
					JOptionPane.showMessageDialog(null, "Please input an integer value!");
				}
			}
			
		});
		btnInputSpeechDuration.setBounds(6, 32, 207, 29);
		add(btnInputSpeechDuration);
		
		//record button functionality
		btnRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					sentence = MiddleMan.speechToText(recordTime);
					displaySentence.setText(sentence);
				} catch (IOException err) {
					JOptionPane.showMessageDialog(null, err);
					System.exit(1);
				}
				
				//enable continue & edit
				btnContinue.setEnabled(true);
				displaySentence.setEnabled(true);
			}
		});
	}
	
	//check if input is int
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    try {
	        Integer.parseInt(str);
	        return true;
	    } catch (NumberFormatException e) {
	        return false;
	    }
	}

}
