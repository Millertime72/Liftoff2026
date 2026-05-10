package pkgETTranslator;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
public class SentenceDisplay extends JPanel {
   private static final long serialVersionUID = 1L;
  
   private Image pendingImage = null;
   private String pendingString = "";
   private boolean hasPendingModifier = false;
   public SentenceDisplay() {
       setLayout(null);
       setPreferredSize(new Dimension(800, 600));
      
       //background
       ImageIcon bgIcon = new ImageIcon(TranslatorReader.backgroundIMG);
       JLabel background = new JLabel(bgIcon);
       background.setBounds(0, 0, 800, 600);
       //return button
       JButton btnReturnToRecord = new JButton("<html><span style='color:black;'>Return to Record</span></html>", TranslatorReader.defaultImageIcon);
       btnReturnToRecord.setVerticalTextPosition(SwingConstants.CENTER);
       btnReturnToRecord.setHorizontalTextPosition(SwingConstants.CENTER);
       btnReturnToRecord.setFocusPainted(false);
       btnReturnToRecord.setContentAreaFilled(false);
       btnReturnToRecord.setBorderPainted(false);
       btnReturnToRecord.setBounds(6, 6, 210, 29);
       btnReturnToRecord.addActionListener(e -> {
           TranslatorReader.mainFrame.getContentPane().removeAll();
           TranslatorReader.mainFrame.getContentPane().add(new RecordSentence());
           TranslatorReader.mainFrame.revalidate();
           TranslatorReader.mainFrame.repaint();
       });
       add(btnReturnToRecord);
       //frog
       ImageIcon originalUFrogO = new ImageIcon("Images/UFrogO.png");
       Image scaledUFrogO = originalUFrogO.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
       JLabel lblUFrogO = new JLabel(new ImageIcon(scaledUFrogO));
       lblUFrogO.setBounds(223, 11, 48, 48);
       add(lblUFrogO);
       //grid
       int startX = 30;
       int startY = 80;
       int gapX = 25;
       int gapY = 50;
       int imagesPerRow = 5;
       int baseSize = 128;
       int addedNums = 0;
       //main loop
       for (Word word : TranslatorReader.collectedChosenPhotos) {
    	   System.out.println(word.getWord() + word.getWordType());
    	   
           String w = word.getWord().toLowerCase();
           String imageURL = word.getChosenPhoto();
           if (imageURL == null || imageURL.isBlank() || imageURL.equals("nothing")) continue;
           //check if modifier
           if (word.getWordType() == 1 || w.equals("past") || w.equals("future")) {
               hasPendingModifier = true;
               pendingString = w;
               BufferedImage img = loadImage(imageURL);
               if (img != null) {
                   //draw into buffer
                   pendingImage = new BufferedImage(baseSize / 2, baseSize / 2, BufferedImage.TYPE_INT_ARGB);
                   Graphics2D g = ((BufferedImage) pendingImage).createGraphics();
                   g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                   g.drawImage(img, 0, 0, baseSize / 2, baseSize / 2, null);
                   g.dispose();
               }
               //skip drawing as main
               continue;
           }
           //draw regulars
           int row = addedNums / imagesPerRow;
           int col = addedNums % imagesPerRow;
           int x = startX + (baseSize + gapX) * col;
           int y = startY + (baseSize + gapY) * row;
           BufferedImage mainImg = loadImage(imageURL);
           if (mainImg != null) {
               //scale main img
               BufferedImage mainScaled = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_ARGB);
               Graphics2D g2 = mainScaled.createGraphics();
               g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
               g2.drawImage(mainImg, 0, 0, baseSize, baseSize, null);
               g2.dispose();
               JLabel imageLabel = new JLabel(new ImageIcon(mainScaled));
               imageLabel.setBounds(x, y, baseSize, baseSize);
               add(imageLabel);
               //word
               JLabel textLabel = new JLabel(word.getWord());
               textLabel.setHorizontalAlignment(SwingConstants.CENTER);
               textLabel.setBounds(x, y + baseSize + 5, baseSize, 25);
              
               //apply modifier
               if (hasPendingModifier && pendingImage != null) {
                   JLabel modifierLabel = new JLabel(new ImageIcon(pendingImage));
                   // Position badge on the top-left corner
                   modifierLabel.setBounds(x - 10, y - 10, baseSize / 2, baseSize / 2);
                   add(modifierLabel);
                   setComponentZOrder(modifierLabel, 0); // Force to front
                   //update text
                   if (pendingString.equals("future")) textLabel.setText("will " + word.getWord());
                   else if (pendingString.equals("past")) textLabel.setText("did " + word.getWord());
                   else textLabel.setText(pendingString + " " + word.getWord());
                   //reset modifier
                   hasPendingModifier = false;
                   pendingImage = null;
               }
              
               add(textLabel);
              
               //send bg to very back
               add(background);
               setComponentZOrder(background, getComponentCount() - 1);
              
               addedNums++;
           }
       }
       revalidate();
       repaint();
   }
   private BufferedImage loadImage(String path) {
       try {
           if (path == null || path.isBlank()) return null;
           path = path.trim();
           //web urls
           if (path.startsWith("http")) {
               return ImageIO.read(new URL(path));
           }
           //local files
           java.io.File file = new java.io.File(path);
           if (file.exists()) {
               return ImageIO.read(file);
           } else {
               //classpath/resource load
               System.out.println("Local file not found, check path: " + path);
               return null;
           }
       } catch (Exception e) {
           System.err.println("Error loading image: " + path);
           return null;
       }
   }
}


