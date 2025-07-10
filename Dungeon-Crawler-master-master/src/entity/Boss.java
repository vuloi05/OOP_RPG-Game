package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

import main.GamePanel;

public class Boss extends Entity {
    GamePanel gp;
    private BufferedImage[] idleSprites; // Mảng chứa các sprite khi boss đứng yên
    private BufferedImage[] attackSprites; // Mảng chứa các sprite cho mỗi hoạt ảnh tấn công
    private BufferedImage[] healthBarSprites;
    private int currentFrame = 0;
    private int frameDelay = 0; // Đếm ngược cho đến khi thay đổi frame tiếp theo
    private int frameDelayLimit = 10; // Giới hạn thời gian chờ để thay đổi frame, có thể điều chỉnh để làm chậm animation
    private boolean attacking = false;
    private int width = 64;
    private int height = 64;
    Bossattack bossAttack;
    private Random rand = new Random();
    private static final int tamdanh = 400;
    private int maxHealth = 8;
    public int currentHealth = 8;
    public Boss(GamePanel gp,Bossattack bossAttack) {
        super(gp);
        this.gp = gp;
        this.bossAttack = bossAttack;
        loadSprites();
        setDefaultValues();

    }
    public void takeDamage(int damage) {
        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }

    }
    public void setDefaultValues() {
        worldX = gp.tileSize * 6;
        worldY = gp.tileSize * 38;
        currentHealth = 8;
    }
    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;
    }
    private void loadSprites() {
        try {
            BufferedImage idleSheet = ImageIO.read(getClass().getResourceAsStream("/boss/idleboss.png"));
            idleSprites = new BufferedImage[4];
            for (int i = 0; i < 4; i++) {
                idleSprites[i] = idleSheet.getSubimage(i * width, 0, width, height);
            }

            attackSprites = new BufferedImage[5];
            for (int i = 0; i < 5; i++) {
                attackSprites[i] = ImageIO.read(getClass().getResourceAsStream("/boss/bossattack" + (i + 1) + ".png"));
            }
            // Tải các sprite của health bar
            healthBarSprites = new BufferedImage[9];
            for (int i = 0; i < 9; i++) {
                healthBarSprites[i] = ImageIO.read(getClass().getResourceAsStream("/boss/bosshp" + (i + 1) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        frameDelay++;
        if (frameDelay >= frameDelayLimit) {
            currentFrame = (currentFrame + 1) % (attacking ? 5 : 4);
            frameDelay = 0; // Đặt lại bộ đếm thời gian để chờ đến frame tiếp theo
            attacking = attacking();  // Cập nhật trạng thái tấn công
            if (currentFrame == 0 && attacking) {
                Bossattack.AttackType attackType = rand.nextBoolean() ? Bossattack.AttackType.LUADEN : Bossattack.AttackType.GIATSET;
                bossAttack.startAttack(attackType);
            }
        }
    }


    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        BufferedImage image = attacking ? attackSprites[currentFrame] : idleSprites[currentFrame];
        g2.drawImage(image, screenX, screenY, width*4, height*4, null);
        int hpIndex = maxHealth - currentHealth; // Tính index dựa trên sức khỏe hiện tại
        g2.drawImage(healthBarSprites[hpIndex], screenX +30, screenY -35 , width*3 , height/2, null);
    }
    public boolean attacking() {
        int playerX = gp.player.getWorldX();
        int playerY = gp.player.getWorldY();
        double distance = Math.sqrt(Math.pow(worldX - playerX, 2) + Math.pow(worldY - playerY, 2));
        return distance <= tamdanh;
    }

}
