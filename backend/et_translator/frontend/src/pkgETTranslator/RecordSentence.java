package pkgETTranslator;

import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JOptionPane; 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Timer;
import javax.swing.SwingUtilities;
import java.io.IOException;


public class RecordSentence extends JPanel {
	private static JFrame mainFrame;
	private int recordTime;
	private static final long serialVersionUID = 1L;
	private JTextField txtSpeechDuration;
	private JEditorPane displaySentence;
	private ImageIcon DefaultAlien = new ImageIcon("Images/DefaultAlien.png");
	private ImageIcon HappyAlien = new ImageIcon("Images/HappyAlien.png");
	private Timer happinessTimer;

	/**
	 * Create the panel.
	 */
	public RecordSentence() {
		setLayout(null);
		mainFrame = TranslatorFrame.frame;
		this.setPreferredSize(new Dimension(800, 600)); 
		
		String currentInput = "None";
		
		//background
        ImageIcon bgIcon = new ImageIcon(TranslatorReader.backgroundIMG);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 800, 600);
        
        //alien guy
        JButton alienGuy = new JButton(DefaultAlien);
        alienGuy.setContentAreaFilled(false);
        alienGuy.setBorderPainted(false);
        alienGuy.setFocusPainted(false);
        alienGuy.setBounds(477, 249, 320, 320);
        add(alienGuy);
        
        //set up happiness timer
        happinessTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Change back to the default face
                alienGuy.setIcon(DefaultAlien);
                // Ensure the timer stops after running once
                happinessTimer.stop(); 
            }
        });

        //detect alien guy press
        alienGuy.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                //only allow petting if the button is enabled
                if (alienGuy.isEnabled()) {
                    //stop if already happy
                    if (happinessTimer.isRunning()) {
                        happinessTimer.stop();
                    }

                    //change to happy
                    alienGuy.setIcon(HappyAlien);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //only run  if happy
                if (alienGuy.isEnabled() && alienGuy.getIcon().equals(HappyAlien)) {
                    //start happiness timer 
                    happinessTimer.start();
                }
            }
        });
		
        JButton btnContinue = new JButton("<html><span style='color:black;'>Continue</span></html>", TranslatorReader.defaultImageIcon);
		btnContinue.setVerticalTextPosition(SwingConstants.CENTER);
		btnContinue.setHorizontalTextPosition(SwingConstants.CENTER);
		btnContinue.setFocusPainted(false);
		btnContinue.setContentAreaFilled(false);
		btnContinue.setBorderPainted(false);
		btnContinue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// disable button while loading
		        btnContinue.setEnabled(false);

		        // loading text
		        btnContinue.setText("Loading...");

		        // optional repaint immediately
		        btnContinue.repaint();

		        SwingUtilities.invokeLater(new Runnable() {
		            public void run() {
						//read sentence file
						TranslatorReader.readSentenceFile(displaySentence.getText());
						
						//restore button if needed
		                btnContinue.setText("Continue");
		                btnContinue.setEnabled(true);
		                
						//continue to next panel
						TranslatorReader.switchToImgSelect();
		            }
		        });
			}
		});
		btnContinue.setBounds(6, 307, 117, 29);
		add(btnContinue);
		//disable continue on launch
		btnContinue.setEnabled(false);
		
		JButton btnRecord = new JButton("<html><span style='color:black;'>Record</span></html>", TranslatorReader.defaultImageIcon);
		btnRecord.setVerticalTextPosition(SwingConstants.CENTER);
		btnRecord.setHorizontalTextPosition(SwingConstants.CENTER);
		btnRecord.setFocusPainted(false);
		btnRecord.setContentAreaFilled(false);
		btnRecord.setBorderPainted(false);
		btnRecord.setBounds(6, 154, 117, 29);
		add(btnRecord);
		//start disabled
		btnRecord.setEnabled(false);
		
		displaySentence = new JEditorPane();
		displaySentence.setBounds(6, 195, 698, 93);
		add(displaySentence);
		displaySentence.setEnabled(false);
		
		txtSpeechDuration = new JTextField();
		txtSpeechDuration.setBounds(16, 6, 130, 26);
		add(txtSpeechDuration);
		txtSpeechDuration.setColumns(10);
		
		JButton btnInputSpeechDuration = new JButton("<html><span style='color:black;'>Input Speech Duration</span></html>", TranslatorReader.defaultImageIcon);
		btnInputSpeechDuration.setVerticalTextPosition(SwingConstants.CENTER);
		btnInputSpeechDuration.setHorizontalTextPosition(SwingConstants.CENTER);
		btnInputSpeechDuration.setFocusPainted(false);
		btnInputSpeechDuration.setContentAreaFilled(false);
		btnInputSpeechDuration.setBorderPainted(false);		btnInputSpeechDuration.addActionListener(new ActionListener() {
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
					displaySentence.setText(MiddleMan.speechToText(recordTime));
				} catch (IOException err) {
					JOptionPane.showMessageDialog(null, err);
					System.exit(1);
				}
				
				//enable continue & edit
				btnContinue.setEnabled(true);
				displaySentence.setEnabled(true);
			}
		});
		
	    //send bg to very back
        add(background);
        setComponentZOrder(background, getComponentCount() - 1);
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
