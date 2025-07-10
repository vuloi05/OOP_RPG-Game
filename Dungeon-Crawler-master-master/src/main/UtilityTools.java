package main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UtilityTools {//Công cụ tiện ích tăng hiệu suất load game

    public BufferedImage scaleImage(BufferedImage initialImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, initialImage.getType());
        Graphics2D g2 = resizedImage.createGraphics();
        g2.drawImage(initialImage, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        return resizedImage;
    }
}
