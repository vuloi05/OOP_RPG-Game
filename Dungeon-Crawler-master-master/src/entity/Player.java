package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import enemy.Enemy;
import main.KeyboardInput;
import main.LockedDoor;
import main.UtilityTools;
import main.*;


public class Player extends Entity {
    GamePanel gp;
    KeyboardInput keyInput;
    public final int screenX;
    public final int screenY;
    public boolean attacking = false;
    sound attack = new sound("/sound/attack.wav");

    public Player(GamePanel gp, KeyboardInput keyInput) {
        super(gp);

        this.gp = gp;
        this.keyInput = keyInput;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 30;
        solidArea.y = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = 20;
        solidArea.height = 40;

        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
    }

    public void setDefaultValues() { // cac gia tri mac dinh cua nhan vat
        worldX = gp.tileSize * 5;              //Vi tri cua nhan vat
        worldY = gp.tileSize * 5;
        speed = 4; // toc do di chuyen cua nhan vat
        direction = "down"; // huong nhan vat ban dau
        // Luong mau cua nhan vat
        maxHP = 16;
        HP = maxHP;
        keyCount = 0;
    }
    private int keyCount = 0;
    public void pickUpKey() {
        keyCount++;;
    }

    public void useKey() {
        if (keyCount > 0) {
            keyCount--;
        }
    }

    public boolean hasKey() {
        return keyCount > 0;
    }

    public int getKeyCount() { return keyCount; }

    public int getWorldX() {
        return worldX;
    }
    public int getWorldY() {
        return worldY;
    }

    public BufferedImage playerImageSetup(String imageName){
        UtilityTools util = new UtilityTools();
        BufferedImage resizedImage = null;

        try{
            resizedImage = ImageIO.read(getClass().getResourceAsStream("/player/"+imageName+".png"));
            resizedImage = util.scaleImage(resizedImage, (int) (1.6*gp.tileSize), (int) (1.6*gp.tileSize));
        }catch(IOException e) {
            e.printStackTrace();
        }
        return resizedImage;
    }
    //attackbyenemy
    public int HP = 100;
    private long lastHitTime;  // Thời điểm nhận sát thương gần nhất
    private long hitCooldown = 1000;  // Khoảng thời gian tối thiểu giữa các lần nhận sát thương (1000 ms = 1 giây)

    public void receiveDamage(int damage) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastHitTime > hitCooldown) {
            HP -= damage;
            if (HP < 0) HP = 0;
            lastHitTime = currentTime;
        }
    }

    public void getPlayerImage() {
            up1 = playerImageSetup("run_up_40x40_1");
            up2 = playerImageSetup("run_up_40x40_2");
            up3 = playerImageSetup("run_up_40x40_3");
            up4 = playerImageSetup("run_up_40x40_4");
            up5 = playerImageSetup("run_up_40x40_5");
            up6 = playerImageSetup("run_up_40x40_6");

            down1 = playerImageSetup("run_down_40x40_1");
            down2 = playerImageSetup("run_down_40x40_2");
            down3 = playerImageSetup("run_down_40x40_3");
            down4 = playerImageSetup("run_down_40x40_4");
            down5 = playerImageSetup("run_down_40x40_5");
            down6 = playerImageSetup("run_down_40x40_6");

            left1 = playerImageSetup("run_left_40x40_1");
            left2 = playerImageSetup("run_left_40x40_2");
            left3 = playerImageSetup("run_left_40x40_3");
            left4 = playerImageSetup("run_left_40x40_4");
            left5 = playerImageSetup("run_left_40x40_5");
            left6 = playerImageSetup("run_left_40x40_6");

            right1 = playerImageSetup("run_right_40x40_1");
            right2 = playerImageSetup("run_right_40x40_2");
            right3 = playerImageSetup("run_right_40x40_3");
            right4 = playerImageSetup("run_right_40x40_4");
            right5 = playerImageSetup("run_right_40x40_5");
            right6 = playerImageSetup("run_right_40x40_6");
    }


    public void getPlayerAttackImage(){
        attackUp0=playerImageSetup("attack/attackup_0");
        attackUp1=playerImageSetup("attack/attackup_1");
        attackUp2=playerImageSetup("attack/attackup_2");
        attackUp3=playerImageSetup("attack/attackup_3");
        attackUp4=playerImageSetup("attack/attackup_4");
        attackUp5=playerImageSetup("attack/attackup_5");
        attackUp6=playerImageSetup("attack/attackup_6");

        attackDown0=playerImageSetup("attack/attackdown_0");
        attackDown1=playerImageSetup("attack/attackdown_1");
        attackDown2=playerImageSetup("attack/attackdown_2");
        attackDown3=playerImageSetup("attack/attackdown_3");
        attackDown4=playerImageSetup("attack/attackdown_4");
        attackDown5=playerImageSetup("attack/attackdown_5");
        attackDown6=playerImageSetup("attack/attackdown_6");

        attackLeft0=playerImageSetup("attack/attackleft_0");
        attackLeft1=playerImageSetup("attack/attackleft_1");
        attackLeft2=playerImageSetup("attack/attackleft_2");
        attackLeft3=playerImageSetup("attack/attackleft_3");
        attackLeft4=playerImageSetup("attack/attackleft_4");
        attackLeft5=playerImageSetup("attack/attackleft_5");
        attackLeft6=playerImageSetup("attack/attackleft_6");

        attackRight0=playerImageSetup("attack/attackright_0");
        attackRight1=playerImageSetup("attack/attackright_1");
        attackRight2=playerImageSetup("attack/attackright_2");
        attackRight3=playerImageSetup("attack/attackright_3");
        attackRight4=playerImageSetup("attack/attackright_4");
        attackRight5=playerImageSetup("attack/attackright_5");
        attackRight6=playerImageSetup("attack/attackright_6");


    }



    public void update(){
        gp.eHandler.checkEvent();
        gp.keyInput.interact = false;
        if (attacking == true){
            attack.playSoundEffect();
            attacking();
        }

        if (!attacking && keyInput.attack) {
            attacking = true;
            keyInput.attack = false; // Đặt lại giá trị của phím tấn công sau khi sử dụng
        }

        if ((keyInput.diPhai || keyInput.diTrai || keyInput.diTren
                || keyInput.diXuong) && !attacking) {


            collisionOn = false;
            gp.cChecker.checkTile(this);
            int potentialX = worldX;
            int potentialY = worldY;

            // Giả sử đầu vào từ bàn phím để điều chỉnh tọa độ tiềm năng
            if (keyInput.diTren) {
                direction = "up";
                if (collisionOn == false) {
                    potentialY = worldY - 4;
                }
            }
            if (keyInput.diXuong) {
                direction = "down";
                if (collisionOn == false) {
                    potentialY = worldY + 4;
                }
            }
            if (keyInput.diTrai) {
                direction = "left";
                if (collisionOn == false) {
                    potentialX = worldX - 4;

                }
            }
            if (keyInput.diPhai) {
                direction = "right";
                if (collisionOn == false) {
                    potentialX = worldX + 4;
                }
            }
            if (keyInput.attack) {
                attacking = true;
                keyInput.attack = false; // Đặt lại giá trị của phím Enter sau khi sử dụng
            }

                // Kiểm tra va chạm với cửa
                boolean collision = false;
                LockedDoor door0 = gp.lockedDoor0;
                LockedDoor door1 = gp.lockedDoor1;
                LockedDoor door2 = gp.lockedDoor2;
                LockedDoor door3 = gp.lockedDoor3;
                LockedDoor door4 = gp.lockedDoor4;
                LockedDoor door5 = gp.lockedDoor5;
                LockedDoor door6 = gp.lockedDoor6;
                LockedDoor door7 = gp.lockedDoor7;
                LockedDoor door8 = gp.lockedDoor8;




                if (door0.isLocked && door0.checkCollision(potentialX, potentialY)) {
                    collision = true;
                }
                if (door1.isLocked && door1.checkCollision(potentialX, potentialY)) {
                    collision = true;
                }
                if (door2.isLocked && door2.checkCollision(potentialX, potentialY)) {
                    collision = true;
                }
                if (door3.isLocked && door3.checkCollision(potentialX, potentialY)) {
                    collision = true;
                }
                if (door4.isLocked && door4.checkCollision(potentialX, potentialY)) {
                    collision = true;
                }
                if (door5.isLocked && door5.checkCollision(potentialX, potentialY)) {
                    collision = true;
                }
                if (door6.isLocked && door6.checkCollision(potentialX, potentialY)){
                    collision = true;
                }
                if (door7.isLocked && door7.checkCollision(potentialX, potentialY)) {
                    collision = true;
                }
                if (door8.isLocked && door8.checkCollision(potentialX, potentialY)) {
                   collision = true;
                }





                // Cập nhật vị trí người chơi nếu không có va chạm
                if (!collision) {
                    worldX = potentialX;
                    worldY = potentialY;
                }


            }

            // toggle between sprites for animation
            if ((keyInput.diPhai || keyInput.diTrai || keyInput.diTren
                    || keyInput.diXuong) && !attacking) {

                spriteDem++;

                if (spriteDem > 8) {
                    if (spriteNum == 1) {
                        spriteNum = 2;
                    } else if (spriteNum == 2) {
                        spriteNum = 3;
                    } else if (spriteNum == 3) {
                        spriteNum = 4;
                    } else if (spriteNum == 4) {
                        spriteNum = 1;
                    } else if (spriteNum == 5) {
                        spriteNum = 6;
                    }else if (spriteNum == 6) {
                        spriteNum = 1;
                    }
                    spriteDem = 0;
                }
            }


        }
    private boolean kiemtratancong = false;
    public  void attacking(){
        spriteDem++;
        if (spriteDem<=5){
            spriteNum=1;
        }
        if(spriteDem>5&&spriteDem<=10){
            spriteNum=2;
        }
        if(spriteDem>5&&spriteDem<=8){
            spriteNum=3;
        }
        if(spriteDem>8&&spriteDem<=10){
            spriteNum=4;
        }
        if(spriteDem>10&&spriteDem<=12){
            spriteNum=5;
        }if(spriteDem>12&&spriteDem<=15){
            spriteNum=6;
        }



        if (spriteDem>15){
            spriteNum=1;
            spriteDem=0;
            attacking=false;
            kiemtratancong = false;
        }
        for (int i = 0; i < gp.enemies.size(); i++) {
            Enemy enemy = gp.enemies.get(i);
            if (isEnemyInRange(enemy) ) {
                enemy.receiveDamage();
                if (enemy.isDead()) {
                    gp.enemies.remove(i);
                    i--;
                }
            }
        }

        if (isBossInRange() && !kiemtratancong) {
            gp.boss.takeDamage(1);
            kiemtratancong = true;
        }
    }
    private boolean isBossInRange() {
        double bossX = gp.tileSize * 8;
        double bossY = gp.tileSize * 42;
        double distance = Math.hypot(worldX - bossX, worldY - bossY);
        return distance <= 100;
    }
    private boolean isEnemyInRange(Enemy enemy) {
        return Math.hypot(worldX - enemy.worldX, worldY - enemy.worldY) <= 40;
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        switch (direction) {
            case "up":
                    if(attacking==false){
                        if (spriteNum == 1) {
                            image = up1;
                        }
                        if (spriteNum == 2) {
                            image = up2;
                        }
                        if (spriteNum == 3) {
                            image = up3;
                        }
                        if (spriteNum == 4) {
                            image = up4;
                        }
                        if (spriteNum == 5) {
                            image = up5;
                        }
                        if (spriteNum == 6) {
                            image = up6;
                        }

                    }
                    if(attacking==true){
                        if(spriteNum == 1){
                            image = attackUp0;
                        }
                        if(spriteNum == 2){
                            image = attackUp1;
                        }
                        if(spriteNum == 3){
                            image = attackUp2;
                        }
                        if(spriteNum == 4){
                            image = attackUp3;
                        }
                        if(spriteNum == 5){
                            image = attackUp4;
                        }
                        if(spriteNum == 6){
                            image = attackUp5;
                        }
                        if(spriteNum == 7){
                            image=attackUp6;
                        }

                    }
                    break;
            case "down":
                if(attacking==false){
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    if (spriteNum == 3) {
                        image = down3;
                    }
                    if (spriteNum == 4) {
                        image = down4;
                    }
                    if (spriteNum == 5) {
                        image = down5;
                    }
                    if (spriteNum == 6) {
                        image = down6;
                    }

                }
                if(attacking==true){
                    if(spriteNum == 1){
                        image = attackDown0;
                    }
                    if(spriteNum == 2){
                        image = attackDown1;
                    }
                    if(spriteNum == 3){
                        image = attackDown2;
                    }
                    if(spriteNum == 4){
                        image = attackDown3;
                    }
                    if(spriteNum == 5){
                        image = attackDown4;
                    }
                    if(spriteNum == 6){
                        image = attackDown5;
                    }
                    if(spriteNum == 7){
                        image=attackDown6;
                    }

                }
                break;

            case "left":
                if(attacking==false){
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    if (spriteNum == 3) {
                        image = left3;
                    }
                    if (spriteNum == 4) {
                        image = left4;
                    }
                    if (spriteNum == 5) {
                        image = left5;
                    }
                    if (spriteNum == 6) {
                        image = left6;
                    }

                }
                if(attacking==true){
                    if(spriteNum == 1){
                        image=attackLeft0;
                    }
                    if(spriteNum == 2){
                        image = attackLeft1;
                    }
                    if(spriteNum == 3){
                        image = attackLeft2;
                    }
                    if(spriteNum == 4){
                        image = attackLeft3;
                    }
                    if(spriteNum == 5){
                        image = attackLeft4;
                    }
                    if(spriteNum == 6){
                        image = attackLeft5;
                    }
                    if(spriteNum == 7){
                        image=attackLeft6;
                    }

                }
                break;

            case "right":
                if(attacking==false){
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    if (spriteNum == 3) {
                        image = right3;
                    }
                    if (spriteNum == 4) {
                        image = right4;
                    }
                    if (spriteNum == 5) {
                        image = right5;
                    }
                    if (spriteNum == 6) {
                        image = right6;
                    }

                }
                if(attacking==true){
                    if(spriteNum == 1){
                        image=attackRight0;
                    }
                    if(spriteNum == 2){
                        image = attackRight1;
                    }
                    if(spriteNum == 3){
                        image = attackRight2;
                    }
                    if(spriteNum == 4){
                        image = attackRight3;
                    }
                    if(spriteNum == 5){
                        image = attackRight4;
                    }
                    if(spriteNum == 6){
                        image = attackRight5;
                    }
                    if(spriteNum == 7){
                        image=attackRight6;
                    }

                }
                break;
            }
            g2.drawImage(image, screenX, screenY, null);
        }
}