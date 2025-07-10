package enemy;

import main.GamePanel;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics2D;

public class NightBorne extends Enemy {

    private BufferedImage[] idleSprites, runSprites, attackSprites, hurtSprites, deathSprites;
    private int currentFrame = 0;
    private int frameDelay = 0; // Biến để đếm độ trễ
    private final int frameDelayLimit = 15;


    public NightBorne(GamePanel gp, int x, int y) {
        super(gp, x, y, 2);
        setDefaultValues();
        loadSprites();
    }

    public void setDefaultValues() {}

    @Override
    protected void loadSprites() {
        try {
            idleSprites = new BufferedImage[8];
            for (int i = 0; i < idleSprites.length; i++) {
                idleSprites[i] = ImageIO.read(getClass().getResourceAsStream("/enemy/necro/idle/necro_" + i + ".png"));
            }

            runSprites = new BufferedImage[8];
            for (int i = 0; i < runSprites.length; i++) {
                runSprites[i] = ImageIO.read(getClass().getResourceAsStream("/enemy/necro/run/necro_" + (8 + i) + ".png"));
            }

            attackSprites = new BufferedImage[2];
            for (int i = 0; i < attackSprites.length; i++) {
                attackSprites[i] = ImageIO.read(getClass().getResourceAsStream("/enemy/necro/attack/necro_" + (16 + i) + ".png"));
            }

            hurtSprites = new BufferedImage[5];
            for (int i = 0; i < hurtSprites.length; i++) {
                hurtSprites[i] = ImageIO.read(getClass().getResourceAsStream("/enemy/necro/hurt/necro_" + (59 + i) + ".png"));
            }

            deathSprites = new BufferedImage[10];
            for (int i = 0; i < deathSprites.length; i++) {
                deathSprites[i] = ImageIO.read(getClass().getResourceAsStream("/enemy/necro/death/necro_" + (64 + i) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        BufferedImage[] currentSpriteArray = getCurrentSpriteArray();
        if (currentFrame < 0 || currentFrame >= currentSpriteArray.length) {
            currentFrame = 0;
        }
        BufferedImage frame = currentSpriteArray[currentFrame];
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        int screenX = worldX - gp.player.getWorldX() + gp.screenWidth / 3 - frameWidth / 3;
        int screenY = worldY - gp.player.getWorldY() + gp.screenHeight / 3 - frameHeight / 3;

        g2.drawImage(frame, screenX, screenY, frameWidth*2, frameHeight*2, null);

        if (state != State.DEATH || !deathAnimationPlayed) {
            updateFrame();
        }
    }
    private void updateFrame() {
        frameDelay++;
        if (frameDelay >= frameDelayLimit) {
            currentFrame = (currentFrame + 1) % getCurrentSpriteArray().length;
            frameDelay = 0;
            if (state == State.DEATH && currentFrame == getCurrentSpriteArray().length - 1) {
                deathAnimationPlayed = true;
            }
        }
    }

    private BufferedImage[] getCurrentSpriteArray() {
        switch (state) {
            case IDLE:
                return idleSprites;
            case RUN:
                return runSprites;
            case ATTACK:
                return attackSprites;
            case HURT:
                return hurtSprites;
            case DEATH:
                return deathSprites;
            default:
                return idleSprites;
        }
    }

}

