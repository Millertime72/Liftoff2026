//TranslatorReader
//Reads through the chosen images textfile
package pkgETTranslator;
//imports
import java.io.IOException;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.ImageIcon;


public class TranslatorReader {
	//arraylist of read data
	private static ArrayList<String> rawData = new ArrayList<>();
	public static ArrayList<Word> collectedPotentialPhotos = new ArrayList<>();
	public static ArrayList<Word> collectedChosenPhotos = new ArrayList<>();
	public static final String sentenceFile = "ChosenImages.txt";
	public static JFrame mainFrame;
	public static final String backgroundIMG = "Images/GalaxyBackground.PNG";
	public static final ImageIcon defaultImageIcon = new ImageIcon("Images/DefaultButton.PNG");
	
	//main
	public static void main(String[] args) {		
		//set up font
		Font goofyFont = new Font("Comic Sans MS", Font.BOLD, 12);
		//set default font on used components
		UIManager.put("Label.font", goofyFont);
		UIManager.put("Button.font", goofyFont);
		UIManager.put("TextField.font", goofyFont);
		UIManager.put("EditorPane.font", goofyFont);
		UIManager.put("TextArea.font", goofyFont);
		UIManager.put("OptionPane.font", goofyFont);

		//launch displays
		TranslatorFrame translatorFrame = new TranslatorFrame();
		mainFrame = translatorFrame;
		translatorFrame.getContentPane().add(new RecordSentence());
		translatorFrame.setVisible(true);
	}
	
	public static void readSentenceFile(String sentence) {
	    collectedPotentialPhotos.clear();
	    collectedChosenPhotos.clear();
	    
	    try {
	    	rawData = MiddleMan.textToImages(sentence, 5);
	    } catch (IOException err) {
	    		JOptionPane.showMessageDialog(null, err);
			System.exit(1);
	    }

	    for (String line : rawData) {
	    	if (line == null || line.trim().isEmpty()) continue;

	        String[] parts = line.split("~");
	        
	        // Ensure we have at least Type and Word Name
	        if (parts.length < 2) {
	            System.err.println("Skipping malformed line: " + line);
	            continue;
	        }

	        try {
	            int wordType = Integer.parseInt(parts[0].trim());
	            
	            String wordName = parts[1].trim();
	            
	            Word w = new Word(wordName, wordType);
	            
	            // Loop through remaining parts for URLs
	            for (int i = 2; i < parts.length; i++) {
	                String url = parts[i].trim();
	                if (!url.isEmpty()) {
	                    w.append(url);
	                }
	            }

	            collectedPotentialPhotos.add(w);
	        } catch (NumberFormatException e) {
	            System.err.println("Invalid WordType format in line: " + line);
	        }
	    }
	}
	
	public static void switchToImgSelect() {
	    mainFrame.getContentPane().removeAll();
	    mainFrame.getContentPane().add(new ImageSelection());

	    mainFrame.revalidate();
	    mainFrame.repaint();
	}
}
