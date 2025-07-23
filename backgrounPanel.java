package ise503_final;

import javax.swing.*;
import java.awt.*;

public class backgrounPanel extends JPanel {
    private Image backgroundImage;

    public backgrounPanel(String imagePath) {
        backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
