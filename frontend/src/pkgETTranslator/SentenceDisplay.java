package pkgETTranslator;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SentenceDisplay extends JPanel {
	private static final long serialVersionUID = 1L;

	public SentenceDisplay() {
		setLayout(null);
		this.setPreferredSize(new Dimension(800, 600));

		// layout settings
		int nounWidth = 128;
		int nounHeight = 128;

		int startX = 16;
		int startY = 60;

		int gapX = 20;
		int gapY = 40;

		int imagesPerRow = 5;
		int addedNums = 0;

		//debug
		for (Word w : TranslatorReader.collectedChosenPhotos) {
			System.out.println("WORD: " + w.getWord());
		}

		//render all words + their images
		for (Word word : TranslatorReader.collectedChosenPhotos) {

		    for (String imageURL : word.getAllPhotos()) {

		        if (imageURL == null || imageURL.isBlank() || imageURL.equals("nothing")) {
		            continue;
		        }

		        imageURL = imageURL.trim();

		        if (!imageURL.startsWith("http")) {
		            continue;
		        }

		        int row = addedNums / imagesPerRow;
		        int col = addedNums % imagesPerRow;

		        int x = startX + (nounWidth + gapX) * col;
		        int y = startY + (nounHeight + gapY) * row;

		        try {
		            URL url = new URL(imageURL);
		            BufferedImage img = ImageIO.read(url);

		            if (img == null) continue;

		            Image scaled = img.getScaledInstance(
		                nounWidth,
		                nounHeight,
		                Image.SCALE_SMOOTH
		            );

		            //image
		            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
		            imageLabel.setBounds(x, y, nounWidth, nounHeight);
		            add(imageLabel);

		            //word label under image
		            JLabel textLabel = new JLabel(word.getWord());
		            textLabel.setBounds(x, y + nounHeight + 5, nounWidth, 20);
		            add(textLabel);

		            addedNums++;

		        } catch (Exception e) {
		            System.out.println("Failed to load: " + imageURL);
		            e.printStackTrace();
		        }
		    }
		}
		//dynamic panel height
		int totalRows = (int) Math.ceil((double) addedNums / imagesPerRow);
		int neededHeight = startY + (totalRows * (nounHeight + gapY)) + 100;

		this.setPreferredSize(new Dimension(800, neededHeight));
		
		JButton btnReturnToRecord = new JButton("Return to Record");
		btnReturnToRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	            TranslatorReader.mainFrame.getContentPane().removeAll();
	            TranslatorReader. mainFrame.getContentPane().add(new RecordSentence());

	            TranslatorReader. mainFrame.revalidate();
	            TranslatorReader.mainFrame.repaint();
	            return;
			}
		});
		btnReturnToRecord.setBounds(6, 0, 273, 15);
		add(btnReturnToRecord);
	}
}