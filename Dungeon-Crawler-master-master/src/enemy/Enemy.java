package enemy;

import main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.sound;

public abstract class Enemy {
    enum State {
        IDLE, RUN, ATTACK, HURT, DEATH
    }

    protected BufferedImage[] sprites;
    protected State state;
    public int worldX, worldY;
    protected int speed;
    protected GamePanel gp;
    protected int health = 0;
    protected boolean deathAnimationPlayed = false;
    protected int DEATH_SPRITE_INDEX;
    public sound monsterHit = new sound ("/sound/receivedamage.wav");

    public Enemy(GamePanel gp, int worldX, int worldY, int speed) {
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
        this.speed = speed;
        this.state = State.IDLE;
        loadSprites();
    }

    protected abstract void loadSprites();
    // Cac trang thai cua Enemy
    public void update() {
        switch (state) {
            case IDLE:
                if (isPlayerInRange()) {
                    state = State.RUN;
                }
                break;
            case RUN:
                moveToPlayer();
                    if (isPlayerInAttackRange()) {
                    state = State.ATTACK;
                } else if (!isPlayerInRange()) {
                    state = State.IDLE;
                }
                break;
            case ATTACK:
                attackPlayer();
                if (!isPlayerInAttackRange()) {
                    state = State.RUN;
                }
                break;
            case HURT:

                break;
            case DEATH:

                break;
        }
    }
    public void receiveDamage() {
        health++;

        if (health < 4) {
            state = State.HURT;
        } else {
            state = State.DEATH;
            deathAnimationPlayed = false; // Reset để chắc chắn hoạt ảnh chết được chạy
        }

    }
    public boolean isDead() {
        boolean dead = state == State.DEATH && deathAnimationPlayed;
        return dead;
    }
    protected boolean isPlayerInRange() {
        int playerX = gp.player.getWorldX();
        int playerY = gp.player.getWorldY();
        return Math.hypot(worldX - playerX, worldY - playerY) <= 200; //tầm nhìn
    }

    protected boolean isPlayerInAttackRange() {
        int playerX = gp.player.getWorldX();
        int playerY = gp.player.getWorldY();
        return Math.hypot(worldX - playerX, worldY - playerY) <= 25; //tầm đánh
    }

    protected void moveToPlayer() {
        int playerX = gp.player.getWorldX();
        int playerY = gp.player.getWorldY();
        if (worldX < playerX) {
            worldX += speed;
        } else if (worldX > playerX) {
            worldX -= speed;
        }

        if (worldY < playerY) {
            worldY += speed;
        } else if (worldY > playerY) {
            worldY -= speed;
        }
    }

    protected void attackPlayer() {
        monsterHit.playSoundEffect();
        gp.player.receiveDamage(2);
    }

    public void draw(Graphics2D g2) {
        if (state == State.DEATH) {
            if (!deathAnimationPlayed) {
                g2.drawImage(sprites[DEATH_SPRITE_INDEX], worldX, worldY, null);
                deathAnimationPlayed = true;
            }
        } else {
            g2.drawImage(sprites[state.ordinal()], worldX, worldY, null);
            if (state == State.HURT) {
            }
        }}
}