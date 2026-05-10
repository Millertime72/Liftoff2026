//TranslatorReader
//Reads through the chosen images textfile
package pkgETTranslator;
//imports
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TranslatorReader {
	//arraylist of read data
	private static ArrayList<String> rawData = new ArrayList<>();
	public static ArrayList<Word> collectedPotentialPhotos = new ArrayList<>();
	public static ArrayList<Word> collectedChosenPhotos = new ArrayList<>();
	public static String sentenceFile = "ChosenImages.txt";
	public static JFrame mainFrame;
	
	//main
	public static void main(String[] args) {		
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
	    	rawData = MiddleMan.textToImages(sentence, 3);
	    } catch (IOException err) {
	    	JOptionPane.showMessageDialog(null, err);
			System.exit(1);
	    }

	    for (String line : rawData) {
	        if (line == null || line.trim().isEmpty()) continue;

	        String[] splitLine = line.split("~"); // IMPORTANT: use ~

	        Word w = new Word(splitLine[0].trim());

	        for (int i = 1; i < splitLine.length; i++) {
	            String url = splitLine[i].trim();
	            if (!url.isEmpty()) {
	                w.append(url);
	            }
	        }

	        collectedPotentialPhotos.add(w);
	    }

	    System.out.println(collectedPotentialPhotos);
	}
	
	public static void switchToImgSelect() {
	    mainFrame.getContentPane().removeAll();
	    mainFrame.getContentPane().add(new ImageSelection());

	    mainFrame.revalidate();
	    mainFrame.repaint();
	}
}
