package entity;

import main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import main.sound;

public class Bossattack {
    enum AttackType {
        LUADEN, GIATSET
    }

    private enum State {
        IDLE, WARNING, ATTACKING
    }

    private State state = State.IDLE;
    private BufferedImage[][] sprites;
    private BufferedImage warningSprite;
    private AttackType currentAttackType = AttackType.LUADEN;
    private int[] maxFrames = {10, 7};
    private int currentFrame = 0;
    private int frameDelay = 0;
    private int frameDelayLimit = 5;
    private int width = 32;
    private int height = 32;
    private int worldX, worldY;
    private GamePanel gp;
    private int attackTimer = 0;
    private int attackCooldown = 10;
    private int warningTimer = 0;
    private int warningDuration = 120; // 2 giây
    private int attackStartTimer = 0;
    private static final int tamdanh = 400; // Tầm tấn công
    public sound bossAttack = new sound ("/sound/explosion.wav");
    public sound bossHit = new sound ("/sound/receivedamage.wav");

    // Lưu vị trí của người chơi
    private Queue<int[]> playerPositions = new LinkedList<>();
    private static final int dotre = 30; // Lưu 0.5 giây (120 khung hình) // chu ky tan cong

    // Offset để điều chỉnh vị trí vẽ
    private int offsetX = -50;
    private int offsetY = -75;



    public Bossattack(GamePanel gp) {
        this.gp = gp;
        setDefaultValues();
        loadSprites();
    }

    private void setDefaultValues() {
        worldX = gp.tileSize * 6;
        worldY = gp.tileSize * 38;
    }

    private void loadSprites() {
        sprites = new BufferedImage[2][];
        try {
            sprites[0] = new BufferedImage[10];
            for (int i = 0; i < 10; i++) {
                sprites[0][i] = ImageIO.read(getClass().getResourceAsStream("/boss/luaden0" + i + ".png"));
            }
            sprites[1] = new BufferedImage[7];
            for (int i = 0; i < 7; i++) {
                sprites[1][i] = ImageIO.read(getClass().getResourceAsStream("/boss/giatset" + i + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startAttack(AttackType type) {
        state = State.ATTACKING;
        currentAttackType = type;
        currentFrame = 0;
        frameDelay = 0;
        // Lấy vị trí của người chơi trước đó 1 giây
        int[] position = getPlayerPositionFromHistory();
        if (position != null) {
            worldX = position[0];
            worldY = position[1];
        }
    }
    public void update() {
        savePlayerPosition();
        attackStartTimer++;
        if (isPlayerInRange()) {
            switch (state) {
                case WARNING:
                    warningTimer++;
                    if (warningTimer >= warningDuration) {
                        startAttack(currentAttackType);
                    }
                    break;
                case ATTACKING:
                    bossAttack.playSoundEffect();
                    frameDelay++;
                    if (frameDelay >= frameDelayLimit) {
                        currentFrame = (currentFrame + 1) % maxFrames[currentAttackType.ordinal()];
                        frameDelay = 0;
                        if (currentFrame == 0) {
                            state = State.IDLE;
                        } else {
                            reducePlayerHealth(); // Check and reduce player health
                        }
                    }
                    break;
                case IDLE:
                    attackTimer++;
                    if (attackTimer >= attackCooldown) {
                        startRandomWarning();
                        attackTimer = 0;
                    }
                    break;
            }
        }
    }
    private boolean isPlayerInRange() {
        int playerX = gp.player.getWorldX();
        int playerY = gp.player.getWorldY();
        double distance = Math.sqrt(Math.pow(worldX - playerX, 2) + Math.pow(worldY - playerY, 2));
        return distance <= tamdanh;
    }
    private void startRandomWarning() {
        // Không thay đổi tọa độ của tấn công
    }
    public void draw(Graphics2D g2) {
        int screenX = worldX - gp.player.worldX + gp.player.screenX + offsetX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY + offsetY;
        if (state == State.WARNING) {
            g2.drawImage(warningSprite, screenX, screenY, width * 5, height * 5, null);
        } else if (state == State.ATTACKING) {
            BufferedImage image = getCurrentSprite();
            g2.drawImage(image, screenX, screenY, width * 5, height * 5, null);
        }}
    private BufferedImage getCurrentSprite() {
        return sprites[currentAttackType.ordinal()][currentFrame];
    }

    private void savePlayerPosition() {
        int[] position = {gp.player.getWorldX(), gp.player.getWorldY()};
        playerPositions.add(position);
        if (playerPositions.size() > dotre) {
            playerPositions.poll(); // Loại bỏ vị trí cũ nhất nếu vượt quá giới hạn
        }
    }
    private int[] getPlayerPositionFromHistory() {
        if (playerPositions.size() < dotre) {
            return null; // Không đủ dữ liệu để lấy vị trí
        }
        int[] peek = playerPositions.peek();
        return peek;
    }
    private void reducePlayerHealth() {
        int playerX = gp.player.getWorldX();
        int playerY = gp.player.getWorldY();
        if (playerX >= worldX && playerX <= worldX + width && playerY >= worldY && playerY <= worldY + height) {
            bossHit.playSoundEffect();
            gp.player.HP -= 2;
        }
    }
}
