package pkgETTranslator;

import java.awt.Dimension;
import java.net.URL;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ImageSelection extends JPanel {
    private static int currWordIndex = 0;
    private static Word currWord;
    private static int currImgIndex = 0;
    private static int maxImgIndex = 0;

    private static final long serialVersionUID = 1L;

    public ImageSelection() {
        setLayout(null);
        this.setPreferredSize(new Dimension(800, 600));
        currWordIndex = 0;
        currImgIndex = 0;

        // safety check (prevents crash if empty)
        if (TranslatorReader.collectedPotentialPhotos.isEmpty()) {
            System.out.println("No words loaded.");
            return;
        }

        currWord = TranslatorReader.collectedPotentialPhotos.get(currWordIndex);

        maxImgIndex = currWord.getAllPhotos().size() - 1;
        if (maxImgIndex < 0) maxImgIndex = 0;

        JLabel lblDisplayImage = new JLabel();
        lblDisplayImage.setBounds(6, 44, 512, 512);
        add(lblDisplayImage);

        JLabel lblCurrImgNum = new JLabel();
        lblCurrImgNum.setBounds(44, 16, 474, 16);
        add(lblCurrImgNum);

        JLabel lblCurrSelecting = new JLabel("Currently Selecting: " + currWord.getWord());
        lblCurrSelecting.setBounds(530, 312, 253, 16);
        add(lblCurrSelecting);

        JButton btnSelect = new JButton("Select");
        btnSelect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int userConfirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to choose this image?",
                        "Please Confirm",
                        JOptionPane.YES_NO_OPTION
                );

                if (userConfirm == JOptionPane.YES_OPTION) {
                	if (currImgIndex >= currWord.getAllPhotos().size()) {
                	    currImgIndex = 0;
                	}
                    String selectedImage = currWord.getAllPhotos().get(currImgIndex);

                    currWord.setChosenPhoto(selectedImage);    
                    TranslatorReader.collectedChosenPhotos.add(currWord);

                    nextPhoto(lblDisplayImage, lblCurrSelecting, lblCurrImgNum);
                }
            }
        });

        btnSelect.setBounds(530, 530, 117, 29);
        add(btnSelect);

        JButton btnPrevious = new JButton("Previous");
        btnPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (currWord.getAllPhotos().isEmpty()) return;
                currImgIndex--;

                if (currImgIndex < 0) {
                    currImgIndex = maxImgIndex;
                }

                updateImage(lblDisplayImage, lblCurrImgNum);
            }
        });
        btnPrevious.setBounds(530, 44, 117, 29);
        add(btnPrevious);

        JButton btnNext = new JButton("Next");
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (currWord.getAllPhotos().isEmpty()) return;
                currImgIndex++;

                if (currImgIndex > maxImgIndex) {
                    currImgIndex = 0;
                }

                updateImage(lblDisplayImage, lblCurrImgNum);
            }
        });
        btnNext.setBounds(530, 85, 117, 29);
        add(btnNext);

        JButton btnSkipImage = new JButton("Skip Word");
        btnSkipImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int userConfirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to skip this word?",
                        "Please Confirm",
                        JOptionPane.YES_NO_OPTION
                );

                if (userConfirm == JOptionPane.YES_OPTION) {

                    Word skipped = new Word(currWord.getWord());
                    skipped.append("nothing");

                    TranslatorReader.collectedChosenPhotos.add(skipped);

                    nextPhoto(lblDisplayImage, lblCurrSelecting, lblCurrImgNum);
                }
            }
        });

        btnSkipImage.setBounds(530, 126, 117, 29);
        add(btnSkipImage);

        updateImage(lblDisplayImage, lblCurrImgNum);
    }

    private static void updateImage(JLabel lblDisplayImage, JLabel lblCurrImgNum) {
        try {
            if (currWord.getAllPhotos().isEmpty()) {
                lblDisplayImage.setIcon(null);
                return;
            }

            String imageURL = currWord.getAllPhotos().get(currImgIndex);

            if (imageURL == null || imageURL.isBlank() || imageURL.equals("nothing")) {
                lblDisplayImage.setIcon(null);
                return;
            }

            URL url = new URL(imageURL);
            BufferedImage img = ImageIO.read(url);

            if (img == null) {
                lblDisplayImage.setIcon(null);
                return;
            }

            Image scaled = img.getScaledInstance(512, 512, Image.SCALE_SMOOTH);
            lblDisplayImage.setIcon(new ImageIcon(scaled));

            lblCurrImgNum.setText("Current Image: " + (currImgIndex + 1) + "/" + (maxImgIndex +1 ));

        } catch (Exception e) {
            e.printStackTrace();
            lblDisplayImage.setIcon(null);
        }
    }

    public static void nextPhoto(JLabel lblDisplayImage, JLabel lblCurrSelecting, JLabel lblCurrImgNum) {
        currWordIndex++;

        if (currWordIndex >= TranslatorReader.collectedPotentialPhotos.size()) {
            TranslatorReader.mainFrame.getContentPane().removeAll();
            TranslatorReader. mainFrame.getContentPane().add(new SentenceDisplay());

            TranslatorReader. mainFrame.revalidate();
            TranslatorReader.mainFrame.repaint();
            return;
        }
        
        if (currWordIndex >= TranslatorReader.collectedPotentialPhotos.size()) {
            return;
        }
        currWord = TranslatorReader.collectedPotentialPhotos.get(currWordIndex);

        currImgIndex = 0;
        maxImgIndex = Math.max(0, currWord.getAllPhotos().size() - 1);

        lblCurrSelecting.setText("Currently Selecting: " + currWord.getWord());

        updateImage(lblDisplayImage, lblCurrImgNum);
    }
}