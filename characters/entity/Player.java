// Đây là file Player.java
package characters.entity;

import characters.util.KeyHandler;
import main.GamePanel;

import java.awt.*;
import java.util.Objects;

public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyHandler;

    public final int screenX;
    public final int screenY;

    public Player(GamePanel gp, KeyHandler keyHandler) {
        this.gp = gp;
        this.keyHandler = keyHandler;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        // Tùy chỉnh kích thước vật rắn trên player
        solidArea = new Rectangle();
        solidArea.x = 58;
        solidArea.y = 100;
        solidArea.width = 16;
        solidArea.height = 30;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 25;
        worldY = gp.tileSize * 25;
        speed = 4;
        direction = "up"; // Hướng nhìn lên trên
        moving = false;
    }

    public void getPlayerImage() {

        Toolkit tk = Toolkit.getDefaultToolkit();

        // Ảnh động đứng yên
        idle = tk.getImage(Objects.requireNonNull(getClass().getResource("/characters/resources/Idle.gif")));

        // Ảnh động chạy
        upRun = tk.getImage(Objects.requireNonNull(getClass().getResource("/characters/resources/RunRight.gif")));
        downRun = tk.getImage(Objects.requireNonNull(getClass().getResource("/characters/resources/RunRight.gif")));
        leftRun = tk.getImage(Objects.requireNonNull(getClass().getResource("/characters/resources/RunLeft.gif")));
        rightRun = tk.getImage(Objects.requireNonNull(getClass().getResource("/characters/resources/RunRight.gif")));
    }

    public void update() {

        // Reset trạng thái di chuyển
        moving = false;

        if (keyHandler.upPressed || keyHandler.downPressed || keyHandler.leftPressed || keyHandler.rightPressed) {

            // Đặt hướng
            if (keyHandler.upPressed) direction = "up";
            else if (keyHandler.downPressed) direction = "down";
            else if (keyHandler.leftPressed) direction = "left";
            else direction = "right";

            // Reset flag va chạm trước khi kiểm tra
            collisionOn = false;

            // Kiểm tra va chạm tile
            gp.collisionChecker.checkTile(this);

            // Nếu không bị va chạm thì mới cho di chuyển
            if (!collisionOn) {
                switch (direction) {
                    case "up" -> worldY -= speed;
                    case "down" -> worldY += speed;
                    case "left" -> worldX -= speed;
                    case "right" -> worldX += speed;
                }
            }

            moving = true;
        }
    }

    public void draw(Graphics2D g2) {

        Image image = switch (direction) {
            case "up" -> moving ? upRun : idle;
            case "down" -> moving ? downRun : idle;
            case "left" -> moving ? leftRun : idle;
            case "right" -> moving ? rightRun : idle;
            default -> idle;
        };

        int scale = 3; // Thay đổi tỉ lệ nhân vật tại đây
        int drawWidth = gp.tileSize * scale;
        int drawHeight = gp.tileSize * scale;

        // Vẽ hình nhân vật
        g2.drawImage(image, screenX, screenY, drawWidth, drawHeight, null);

        // DEBUG: Vẽ vùng va chạm
        g2.setColor(new Color(255, 0, 0, 128)); // Màu đỏ trong suốt
        g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
    }
}