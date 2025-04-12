// Đây là file Entity.java
package characters.entity;

import java.awt.*;

public class Entity {

    public int worldX, worldY;
    public int speed;
    public Image idle;
    public Image upRun, downRun, leftRun, rightRun;
    public String direction;
    public boolean moving = false;
    public Rectangle solidArea;
    public boolean collisionOn = false;
}