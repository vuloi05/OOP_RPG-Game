package main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {//Thiết lập hiển thị thông tin Player trên màn hình
    GamePanel gp;
    Graphics2D g2;

    Font sansFont;
    BufferedImage keyImage;

    BufferedImage titleBackground, titleBoss, titleSword;
    BufferedImage zeroHP, twoHP, fourHP, sixHP, eightHP, tenHP, twelveHP, fourteenHP, sixteenHP;
    BufferedImage credits, gameOver, gameCompleted, gamePause, HUST, MU;

    public UI(GamePanel gp) {
        this.gp = gp;

        //Khai báo các font muốn dùng tại đây
        try {
            InputStream is = getClass().getResourceAsStream("/font/SVN-Determination Sans.otf");
            sansFont = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            keyImage = ImageIO.read(getClass().getResourceAsStream("/object/key.png"));
            titleBackground = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/background.png"));
            titleBoss = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/boss.png"));
            titleSword = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/sword.png"));
            credits = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/credits.png"));
            gameOver = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/gameover.png"));
            gameCompleted = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/gameEnd.png"));
            gamePause = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/paused.png"));
            HUST = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/HUST.png"));
            MU = ImageIO.read(getClass().getResourceAsStream("/titleMaterial/MUv1.png"));

        }catch(IOException e){
            e.printStackTrace();
        }


        //Create HUD Objects
        objectforgem health = new Health(gp);
        zeroHP = health.image0; twoHP = health.image1; fourHP = health.image2;
        sixHP = health.image3; eightHP = health.image4; tenHP = health.image5;
        twelveHP = health.image6; fourteenHP = health.image7; sixteenHP = health.image8;





    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        if(gp.gameState == gp.titleScreen){
            drawTitleScreen();
        }
        if(gp.gameState == gp.playState) {
            drawInterface(g2);
        }
        if(gp.gameState == gp.pauseState) {
            drawPauseScreen();
        }
        if(gp.gameState == gp.gameCompletedState) {
            drawCompletedScreen();
        }
        if(gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
    }

    public void drawTitleScreen() {
        g2.drawImage(titleBackground,0,0,null);
        g2.drawImage(titleBoss,275,215,240,288,null);
        g2.drawImage(titleSword,90,0,256,256,null);
    }

    public void drawPlayerHP(){
        int x = gp.tileSize / 2; //toa do hien HP tuong ung
        int y = gp.tileSize / 2;

        int width = 64 * 3; //kich co anh goc la 64x16;
        int height = 16 * 3;
        int index = gp.player.HP / 2; // Giả sử mỗi hình ảnh đại diện cho 2 HP
        if (index > 8) index = 8;

        BufferedImage[] healthImages = {
                zeroHP, zeroHP, twoHP, twoHP,
                fourHP, fourHP, sixHP, sixHP,
                eightHP, eightHP, tenHP, tenHP,
                twelveHP, twelveHP, fourteenHP, fourteenHP, sixteenHP, sixteenHP
        };
        int playerHP = gp.player.HP;
        if (playerHP >= 0 && playerHP < healthImages.length) {
            g2.drawImage(healthImages[playerHP], x, y, width, height, null);
        }
        if (playerHP < 0){
            g2.drawImage(zeroHP,x,y,width,height,null);
        }
        if (playerHP > 16){
            g2.drawImage(sixteenHP,x,y,width,height,null);
        }
    }

    public void drawPauseScreen() {
        g2.drawImage(gamePause,0,0,null);
    }

    public void drawCompletedScreen(){
        g2.drawImage(gameCompleted,0,0,null);
        g2.drawImage(HUST,200,200,150,225,null);
        g2.drawImage(MU,400,210,200,200,null);
    }

    public void drawGameOverScreen(){
        g2.drawImage(gameOver,0,0,null);
    }

    public void drawCreditsScreen() {
        g2.drawImage(credits,0,0,null);
    }

    public void drawKeyCount(Graphics2D g2){
        g2.setFont(sansFont);
        g2.setFont(g2.getFont().deriveFont(20F));
        g2.setColor(Color.white);
        g2.drawImage(keyImage, 12, 525, gp.tileSize, gp.tileSize, null);
        g2.drawString(gp.player.getKeyCount()+"/"+"20",60,558);
    }

    public void drawInterface(Graphics2D g2){//Cần hiển thị thông số nào trong khi chơi thì thêm vào đây
        drawKeyCount(g2);
        drawPlayerHP();
    }
}
