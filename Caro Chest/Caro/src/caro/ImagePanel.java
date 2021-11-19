package caro;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author 
 */
public class ImagePanel extends JPanel {

    private int width;
    private int height;
    private Image myImage;

    public ImagePanel(String nameFile, int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        String s = getClass().getResource(nameFile).toString();
        this.myImage = new ImageIcon(getClass().getResource(nameFile)).getImage(); 
                        
        setLayout(null);
        this.setBounds(x, y, width, height);
        repaint();
    }

    public void setPicture(String nameFile) {
        this.myImage = new ImageIcon(getClass().getResource(nameFile)).getImage();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(myImage, 0, 0, this);

    }

}
