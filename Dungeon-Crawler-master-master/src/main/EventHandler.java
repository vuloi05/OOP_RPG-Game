package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EventHandler {
    GamePanel gp;
    BufferedImage escape,keys,monsters,spikes,teleports,boss;
    int eventRectDefaultX, eventRectDefaultY;
    Rectangle eventRect;
    private long lastHitTime;
    // GAME SOUND
    sound hit = new sound ("/sound/hit.wav");
    sound teleport1 = new sound ("/sound/teleport1.wav");
    sound healing = new sound ("/sound/healing.wav");

    public EventHandler(GamePanel gp) {
        this.gp = gp;

        eventRect = new Rectangle();

        eventRect.x = 20;
        eventRect.y = 20;
        eventRect.height = 8;
        eventRect.width = 8;
        eventRectDefaultX = eventRect.x;
        eventRectDefaultY = eventRect.y;

        lastHitTime = System.currentTimeMillis();

        try {
            escape = ImageIO.read(getClass().getResourceAsStream("/letter/escape.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkEvent(){

        if (hit(13, 5, "any") || hit(28,44,"any") ||
                hit(11, 7, "any") || hit(16, 5, "any") ||
                hit(13, 8, "any") ||hit(18, 6, "any") ||
                hit(34, 4, "any") || hit(30, 5, "any") ||
                hit(36, 4, "any") || hit(29, 9, "any") ||
                hit(34, 6, "any") || hit(29, 11, "any") ||
                hit(36, 6, "any") || hit(25, 16, "any") ||
                hit(43, 7, "any") || hit(28, 16, "any") ||
                hit(45, 3, "any") || hit(19, 44, "any") ||
                hit(47, 7, "any") || hit(19, 46, "any") ||
                hit(28, 46, "any") || hit(42, 32, "any") ||
                hit(44, 32, "any") || hit(46, 32, "any") ||
                hit(47, 35, "any") || hit(48, 38, "any") ||
                hit(16, 8, "any") || hit(25, 18, "any") ||
                hit(28, 18, "any")) {
            hit.playSoundEffect();
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastHitTime;

            // Add a cooldown of, for example, 1000 milliseconds (1 second)
            if (elapsedTime >= 1000) {
                spikeHit();
                lastHitTime = currentTime; // Update lastHitTime
            }
        }
        if (hit(22,36,"any")){
            teleport1.playSoundEffect();
            teleportTile1();
        }
        if (hit(11,34,"any")){
            teleport1.playSoundEffect();
            teleportTile2();
        }
        if (hit(18,9,"any")){
            teleport1.playSoundEffect();
            teleportTile3();
        }
        if (hit(19,25,"any") || hit(28,25,"any") ){
            teleport1.playSoundEffect();
            teleportTile4();
        }
        if (hit(46,20,"any")){
            teleport1.playSoundEffect();
            teleportTile5();
        }


        if (hit(23, 3, "any") || hit(42,25,"any") ||
                hit(35, 2, "any") || hit(43, 25, "any") ||
                hit(41, 2, "any") ||hit(46, 25, "any") ||
                hit(48, 2, "any") || hit(19, 38, "any") ||
                hit(39, 25, "any") || hit(28, 38, "any") ||
                hit(20, 25, "any") || hit(27, 25, "any") ||
                hit(38, 12, "any") || hit(43, 12, "any") ||
                hit(45, 42, "any") || hit(6,11,"any")||
        hit(6,11,"any")){

            healingTile();

        }
    }
    public boolean hit(int eventCol, int eventRow, String reqDirection) {

        boolean hit = false;

        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
        eventRect.x = eventCol * gp.tileSize + eventRect.x;
        eventRect.y = eventRow * gp.tileSize + eventRect.y;

        if (gp.player.solidArea.intersects(eventRect)) {
            if (gp.player.direction.contentEquals(reqDirection) || reqDirection.equals("any")) {
                hit = true;
            }
        }

        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
        eventRect.x = eventRectDefaultX;
        eventRect.y = eventRectDefaultY;

        return hit;
    }

    public void spikeHit(){
        gp.player.HP -= 1;

    }

    public void healingTile() {
        if (gp.keyInput.interact) {
            healing.playSoundEffect();
            if (gp.player.HP < 16) {
                gp.player.HP += 1;
            } else {
                gp.player.HP = 16;
            }
        }
        gp.keyInput.interact = false;
    }

    public void teleportTile1(){
        gp.player.worldX = gp.tileSize * 15;
        gp.player.worldY = (int) (gp.tileSize * 39.5);
    }
    public void teleportTile2(){
        gp.player.worldX = gp.tileSize * 24;
        gp.player.worldY = gp.tileSize * 40;
    }
    public void teleportTile3(){
        gp.player.worldX = gp.tileSize * 5;
        gp.player.worldY = gp.tileSize * 15;
    }
    public void teleportTile4(){
        gp.player.worldX = gp.tileSize * 48;
        gp.player.worldY = gp.tileSize * 3;
    }
    public void teleportTile5(){
        gp.player.worldX = (int) (gp.tileSize * 34.5);
        gp.player.worldY = (int) (gp.tileSize * 16.5);
    }
}
