package pkgETTranslator;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SentenceDisplay extends JPanel {
    private static final long serialVersionUID = 1L;
    private static Image pendingImage;

    public SentenceDisplay() {
        setLayout(null);
        setPreferredSize(new Dimension(800, 600));

        JButton btnReturnToRecord = new JButton("Return to Record");
        btnReturnToRecord.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TranslatorReader.mainFrame.getContentPane().removeAll();
                TranslatorReader.mainFrame.getContentPane().add(new RecordSentence());

                TranslatorReader.mainFrame.revalidate();
                TranslatorReader.mainFrame.repaint();
            }
        });
        btnReturnToRecord.setBounds(6, 6, 210, 29);
        add(btnReturnToRecord);

        int startX = 16;
        int startY = 60;
        int gapX = 20;
        int gapY = 40;
        int imagesPerRow = 5;
        int baseSize = 128;
        int addedNums = 0;

        boolean pendingPast = false;
        boolean pendingFuture = false;
        boolean applyPast = false;
        boolean applyFuture = false;
        boolean continueWord = true;

        for (Word word : TranslatorReader.collectedChosenPhotos) {
            String w = word.getWord().toLowerCase();
            String imageURL = word.getChosenPhoto();
            
            if (imageURL == null || imageURL.isBlank() || imageURL.equals("nothing")) {
                continue;
            }

            imageURL = imageURL.trim();

            //check if modifier
            if (w.equals("past") || w.equals("future")) {
                if (w.equals("past")) pendingPast = true;
                if (w.equals("future")) pendingFuture = true;

                try {
                    URL url = new URL(imageURL);
                    BufferedImage img = ImageIO.read(url);
                    if (img != null) {
                        // Synchronously scale the modifier
                        pendingImage = new BufferedImage(baseSize / 2, baseSize / 2, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g = ((BufferedImage) pendingImage).createGraphics();
                        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g.drawImage(img, 0, 0, baseSize / 2, baseSize / 2, null);
                        g.dispose();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue; 
            }

            //draw regular words
            int row = addedNums / imagesPerRow;
            int col = addedNums % imagesPerRow;
            int x = startX + (baseSize + gapX) * col;
            int y = startY + (baseSize + gapY) * row;

            try {
                URL url = new URL(imageURL);
                BufferedImage img = ImageIO.read(url);
                if (img != null) {
                    //sync image
                    BufferedImage mainScaled = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = mainScaled.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(img, 0, 0, baseSize, baseSize, null);
                    g.dispose();

                    //create main image label
                    JLabel imageLabel = new JLabel(new ImageIcon(mainScaled));
                    imageLabel.setBounds(x, y, baseSize, baseSize);
                    add(imageLabel);

                    //apply pending modifiers
                    if ((pendingPast || pendingFuture) && pendingImage != null) {
                        JLabel timeLabel = new JLabel(new ImageIcon(pendingImage));
                        //position on top left of image
                        timeLabel.setBounds(x - 5, y - 5, baseSize / 2, baseSize / 2);
                        add(timeLabel);
                        //force op top
                        setComponentZOrder(timeLabel, 0);

                        //reset for next word
                        pendingPast = false;
                        pendingFuture = false;
                        pendingImage = null;
                    }

                    //word text label
                    JLabel textLabel = new JLabel(word.getWord());
                    textLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    textLabel.setBounds(x, y + baseSize + 5, baseSize, 20);
                    add(textLabel);

                    addedNums++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Refresh the UI to show the new components
        revalidate();
        repaint();
    }
}