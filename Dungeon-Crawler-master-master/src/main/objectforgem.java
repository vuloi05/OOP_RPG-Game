package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class objectforgem {
    protected Point position; // Vị trí của đối tượng trên bản đồ
    public BufferedImage image,image0,image1,image2,image3,image4,image5,image6,image7,image8; // Hình ảnh của đối tượng

    public objectforgem(int x, int y, String imagePath, int width, int height) {
        this.position = new Point(x, y);
        this.image = loadImage(imagePath, width, height);
    }

    public objectforgem() {}

    protected BufferedImage loadImage(String path, int width, int height) {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResourceAsStream(path));
            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();
            return scaledImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public abstract void interact();
    public abstract void draw(Graphics2D g2);
}

