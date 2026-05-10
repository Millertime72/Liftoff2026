package pkgETTranslator;

import java.awt.Dimension;
import java.net.URL;
import javax.swing.JFileChooser;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.image.BufferedImage;
import java.lang.foreign.Linker.Option;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class ImageSelection extends JPanel {
    private static int currWordIndex = 0;
    private static Word currWord;
    private static int currImgIndex = 0;
    private static int maxImgIndex = 0;
    private static JButton btnNext;
    private static JButton btnPrevious;
    private static JLabel lblDisplayImage;
    private static JLabel lblCurrSelecting;
    private static JLabel lblCurrImgNum;
    private static JLabel lblCurrWordNum;
    private JButton btnFromDesktop;

    private static final long serialVersionUID = 1L;

    public ImageSelection() {
        setLayout(null);
        this.setPreferredSize(new Dimension(800, 600));
        currWordIndex = 0;
        currImgIndex = 0;
        
        //background
        ImageIcon bgIcon = new ImageIcon(TranslatorReader.backgroundIMG);
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, 800, 600);

        //safety check (prevents crash if empty)
        if (TranslatorReader.collectedPotentialPhotos.isEmpty()) {
            System.out.println("No words loaded.");
            if (currWord.getAllPhotos().isEmpty()) {
                maxImgIndex = 0;
                currImgIndex = 0;
            }
            return;
        }
        
        currWord = TranslatorReader.collectedPotentialPhotos.get(currWordIndex);

        maxImgIndex = currWord.getAllPhotos().size() - 1;
        if (maxImgIndex < 0) maxImgIndex = 0;

        lblDisplayImage = new JLabel();
        lblDisplayImage.setBounds(6, 44, 512, 512);
        add(lblDisplayImage);

        lblCurrImgNum = new JLabel();
        lblCurrImgNum.setBounds(44, 16, 474, 16);
        add(lblCurrImgNum);

        lblCurrWordNum = new JLabel();
        lblCurrWordNum.setBounds(309, 16, 474, 16);
        add(lblCurrWordNum);

        lblCurrSelecting = new JLabel("Currently Selecting: " + currWord.getWord());
        lblCurrSelecting.setBounds(530, 312, 253, 16);
        add(lblCurrSelecting);

        ImageIcon shootingStar = new ImageIcon("Images/ShootingStar.PNG");
        JButton btnSelect = new JButton( "<html><span style='color:yellow;'>Select</span></html>", shootingStar);
        btnSelect.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSelect.setVerticalTextPosition(SwingConstants.CENTER);
        btnSelect.setBorderPainted(false);
        btnSelect.setFocusPainted(false);
        btnSelect.setContentAreaFilled(false);
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

                    Word copy = new Word(currWord.getWord(), currWord.getWordType());
                    copy.setChosenPhoto(selectedImage);

                    TranslatorReader.collectedChosenPhotos.add(copy);

                    nextPhoto();
                }
            }
        });

        btnSelect.setBounds(530, 530, 117, 29);
        add(btnSelect);

        ImageIcon leftRocket = new ImageIcon("Images/LeftRocket.PNG");
        btnPrevious = new JButton( "<html><span style='color:blue;'>Previous</span></html>", leftRocket);
        btnPrevious.setHorizontalTextPosition(SwingConstants.CENTER);
        btnPrevious.setVerticalTextPosition(SwingConstants.CENTER);
        btnPrevious.setBorderPainted(false);
        btnPrevious.setFocusPainted(false);
        btnPrevious.setContentAreaFilled(false);
        btnPrevious.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currWord.getAllPhotos().isEmpty()) return;
                currImgIndex--;

                if (currImgIndex < 0) {
                    currImgIndex = maxImgIndex;
                }

                updateImage();
            }
        });
        btnPrevious.setBounds(530, 44, 117, 29);
        add(btnPrevious);

        ImageIcon rightRocket = new ImageIcon("Images/RightRocket.PNG");
        btnNext = new JButton("<html><span style='color:red;'>Next</span></html>", rightRocket);
        btnNext.setHorizontalTextPosition(SwingConstants.CENTER);
        btnNext.setVerticalTextPosition(SwingConstants.CENTER);
        btnNext.setBorderPainted(false);
        btnNext.setFocusPainted(false);
        btnNext.setContentAreaFilled(false);

        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currWord.getAllPhotos().isEmpty()) return;
                currImgIndex++;

                if (currImgIndex > maxImgIndex) {
                    currImgIndex = 0;
                }

                updateImage();
            }
        });
        btnNext.setBounds(530, 85, 117, 29);
        add(btnNext);

        ImageIcon flamingMeteor = new ImageIcon("Images/FlamingMeteor.PNG");
        JButton btnSkipImage = new JButton("<html><span style='color:brown;'>Skip Word</span></html>", flamingMeteor);
        btnSkipImage.setHorizontalTextPosition(SwingConstants.CENTER);
        btnSkipImage.setVerticalTextPosition(SwingConstants.CENTER);
        btnSkipImage.setBorderPainted(false);
        btnSkipImage.setFocusPainted(false);
        btnSkipImage.setContentAreaFilled(false);

        btnSkipImage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int userConfirm = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to skip this word?",
                        "Please Confirm",
                        JOptionPane.YES_NO_OPTION
                );

                if (userConfirm == JOptionPane.YES_OPTION) {

                    Word skipped = new Word(currWord.getWord(), currWord.getWordType());
                    skipped.append("nothing");

                    TranslatorReader.collectedChosenPhotos.add(skipped);

                    nextPhoto();
                }
            }
        });

        btnSkipImage.setBounds(530, 126, 117, 29);
        add(btnSkipImage);
        
        ImageIcon dazzlingRing = new ImageIcon("Images/DazzlingRing.PNG");
        btnFromDesktop = new JButton ("<html><span style='color:black;'>Choose File</span></html>\"", dazzlingRing);
        btnFromDesktop.setVerticalTextPosition(SwingConstants.CENTER);
        btnFromDesktop.setHorizontalTextPosition(SwingConstants.CENTER);
        btnFromDesktop.setFocusPainted(false);
        btnFromDesktop.setContentAreaFilled(false);
        btnFromDesktop.setBorderPainted(false);
        btnFromDesktop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(null);

                if (result != JFileChooser.APPROVE_OPTION) return;

                try {
                    java.io.File file = fileChooser.getSelectedFile();
                    String path = file.getAbsolutePath();

                    BufferedImage img = ImageIO.read(file);

                    if (img == null) {
                        JOptionPane.showMessageDialog(null, "Invalid image file!");
                        return;
                    }

                    Image scaled = img.getScaledInstance(512, 512, Image.SCALE_SMOOTH);
                    ImageIcon preview = new ImageIcon(scaled);

                    JLabel previewLabel = new JLabel(preview);

                    int conf = JOptionPane.showConfirmDialog(
                            null,
                            previewLabel,
                            "Use this image?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

                    if (conf == JOptionPane.YES_OPTION) {

                        currWord.getAllPhotos().add(path);
                        Word copy = new Word(currWord.getWord(), currWord.getWordType());
                        copy.setChosenPhoto(path);

                        TranslatorReader.collectedChosenPhotos.add(copy);
                        maxImgIndex = currWord.getAllPhotos().size() - 1;
                        currImgIndex = maxImgIndex;

                        updateImage();
                        nextPhoto();
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not load image!");
                }
            }
        });
        btnFromDesktop.setBounds(530, 167, 117, 29);
        add(btnFromDesktop);

        //send bg to very back
        add(background);
        setComponentZOrder(background, getComponentCount() - 1);
       
        updateImage();
    }

    private static void updateImage() {
        try {
            if (currWord == null || currWord.getAllPhotos().isEmpty()) {
                lblDisplayImage.setIcon(null);
                return;
            }

            String imagePath = currWord.getAllPhotos().get(currImgIndex);

            if (imagePath == null || imagePath.isBlank() || imagePath.equals("nothing")) {
                lblDisplayImage.setIcon(null);
                return;
            }

            BufferedImage img = null;
            String path = imagePath.trim();

            try {
                // CASE 1: URL image
                if (path.startsWith("http://") || path.startsWith("https://")) {
                    img = ImageIO.read(new URL(path));
                }

                // CASE 2: file URI format (file:/...)
                else if (path.startsWith("file:/")) {
                    java.net.URI uri = new java.net.URI(path);
                    img = ImageIO.read(new java.io.File(uri));
                }

                // CASE 3: normal local file path
                else {
                    img = ImageIO.read(new java.io.File(path));
                }

            } catch (Exception ex) {
                System.out.println("Failed to load image: " + path);
                ex.printStackTrace();
                img = null;
            }

            if (img == null) {
                lblDisplayImage.setIcon(null);
                return;
            }

            Image scaledImg = img.getScaledInstance(512, 512, Image.SCALE_SMOOTH);
            lblDisplayImage.setIcon(new ImageIcon(scaledImg));

            lblCurrImgNum.setText("Current Image: " + (currImgIndex + 1) + "/" + (maxImgIndex + 1));
            lblCurrWordNum.setText("Current Word: " + (currWordIndex + 1) + "/" +
                    TranslatorReader.collectedPotentialPhotos.size());

            boolean hasMultiple = maxImgIndex > 0;
            btnNext.setEnabled(hasMultiple);
            btnPrevious.setEnabled(hasMultiple);

        } catch (Exception e) {
            e.printStackTrace();
            lblDisplayImage.setIcon(null);
        }
    }

    public static void nextPhoto() {
        currWordIndex++;

        if (currWordIndex >= TranslatorReader.collectedPotentialPhotos.size()) {
            TranslatorReader.mainFrame.getContentPane().removeAll();
            TranslatorReader.mainFrame.getContentPane().add(new SentenceDisplay());

            TranslatorReader.mainFrame.revalidate();
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

        updateImage();
    }
}
