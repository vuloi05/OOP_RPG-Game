package entity;

import main.GamePanel;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Entity {

	public GamePanel gp;
	public int worldX, worldY;
	public int speed;

	public int maxHP;
	public BufferedImage up1, up2, up3, up4,up5,up6;
	public BufferedImage down1, down2, down3, down4,down5,down6;
	public BufferedImage left1, left2, left3, left4,left5,left6;
	public BufferedImage right1, right2, right3, right4,right5,right6;
	public BufferedImage attackUp0,attackUp1,attackUp2,attackUp3,attackUp4,attackUp5,attackUp6;
	public BufferedImage attackDown0,attackDown1,attackDown2,attackDown3,attackDown4,attackDown5,attackDown6;
	public BufferedImage attackRight0,attackRight1,attackRight2,attackRight3,attackRight4,attackRight5,attackRight6;
	public BufferedImage attackLeft0,attackLeft1,attackLeft2,attackLeft3,attackLeft4,attackLeft5,attackLeft6;
	public String direction;
	public int spriteDem = 0;
	public int spriteNum = 1;
	public Rectangle solidArea;
	public int solidAreaDefaultX, solidAreaDefaultY; 
	public boolean collisionOn = false;



	public Entity(GamePanel gp) {
		this.gp = gp;
	}
}
