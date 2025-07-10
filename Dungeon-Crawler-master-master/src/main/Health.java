package main;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Health extends objectforgem{
    GamePanel gp;


    public Health(GamePanel gp) {

        this.gp = gp;

        try {
            image0 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_0.png"));
            image1 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_1.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_2.png"));
            image3 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_3.png"));
            image4 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_4.png"));
            image5 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_5.png"));
            image6 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_6.png"));
            image7 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_7.png"));
            image8 = ImageIO.read(getClass().getResourceAsStream("/hearts/Health_8.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void interact() {}

    @Override
    public void draw(Graphics2D g2) {}
}
