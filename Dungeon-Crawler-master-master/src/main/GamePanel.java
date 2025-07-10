package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import enemy.Enemy;
import enemy.ghost;
import enemy.skeleton;
import entity.Boss;
import letters.Letter;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import entity.Bossattack;
import entity.Player;
import enemy.NightBorne;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {
	// SCREEN SETTINGS
	final int originalTileSize = 16; // 16x16 tile
	final int scale = 3;
	public final int tileSize = originalTileSize * scale; // 48x48 tile
	public final int maxScreenCol = 16;
	public final int maxScreenRow = 12;
	public final int screenWidth = tileSize * maxScreenCol; // 768
	public final int screenHeight = tileSize * maxScreenRow; // 576
	public Key key0, key1, key2, key3, key4, key5, key6, key7, key8;
	public LockedDoor lockedDoor0, lockedDoor1, lockedDoor2, lockedDoor3, lockedDoor4, lockedDoor5, lockedDoor6, lockedDoor7, lockedDoor8;
	public Boss boss;
	public Bossattack bossAttack;
	public BufferedImage escapeImage,tutorialImage,keyImage,monstersImage,spikesImage,teleImage,bossImage,endImage,suyImage,tipsImage;
	public ArrayList<Enemy> enemies;
	public Letter letter0,letter1,letter2,letter3,letter4,letter5,letter6,letter8,letter9,letter10;

	// WORLD SETTINGS
	public final int maxWorldCol = 50;
	public final int maxWorldRow = 50;

	// FPS
	int FPS = 60;

	//enemy
	public NightBorne nightBorne,nightBorne1,nightBorne2,nightBorne3,nightBorne4,nightBorne5,nightBorne6,nightBorne7,nightBorne8, nightBorne9,nightBorne10,nightBorne11,nightBorne12,nightBorne13,nightBorne14;
	public ghost ghost1,ghost2,ghost3,ghost4,ghost5,ghost6,ghost7,ghost8,ghost9,ghost10,ghost11,ghost12,ghost13,ghost14,ghost15,ghost16,ghost17,ghost18,ghost19,ghost20,
				ghost21,ghost22,ghost23,ghost24,ghost25,ghost26,ghost27,ghost28,ghost29,ghost30;
	public skeleton skeleton0, skeleton1, skeleton2,skeleton3, skeleton4, skeleton5, skeleton6, skeleton7, skeleton8,
					skeleton9, skeleton10, skeleton11, skeleton12, skeleton13, skeleton14, skeleton15,skeleton16,skeleton17,skeleton18,skeleton19,skeleton20,
					skeleton21,skeleton22,skeleton23,skeleton24,skeleton25,skeleton26,skeleton27,skeleton28,skeleton29;

	// SYSTEM
	public TileManager tileM = new TileManager(this);
	public KeyboardInput keyInput = new KeyboardInput(this);
	public CollisionChecker cChecker = new CollisionChecker(this);
	public UI ui = new UI(this);
	public EventHandler eHandler = new EventHandler(this);

	Thread gameThread;

	// ENTITY AND OBJECT
	public Player player = new Player(this, keyInput);
	private List<objectforgem> objects = new ArrayList<>(); // Danh sách các đối tượng trên màn hình
	private boolean keyRemoved = false;

	// GAME STATE
	public int gameState;
	public final int titleScreen = 0;
	public final int playState = 1;
	public final int pauseState = 2;
	public final int creditsState = 3;
	public final int gameCompletedState = 4;
	public final int gameOverState = 5;

	// SOUND
	public sound backgroundMusic,gameOver,gameComplete,attack;
	public sound paper0,paper1,paper2,paper3,paper4,paper5,paper6,paper7,paper8,paper9;


	public GamePanel() {


		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyInput);
		this.setFocusable(true);



		//LETTER
		letter0 = new Letter(tileSize * 4, tileSize * 3, player);
		letter1 = new Letter(tileSize * 18, tileSize * 8, player);
		letter2 = new Letter(tileSize * 4, tileSize * 14, player);
		letter3 = new Letter(tileSize * 22, tileSize * 29, player);
		letter4 = new Letter(tileSize * 22, tileSize * 24, player);
		letter5 = new Letter(tileSize * 46, tileSize * 13, player);
		letter6 = new Letter(tileSize * 4, tileSize * 18, player);
		letter8 = new Letter(tileSize * 21, tileSize * 37, player);
		letter9 = new Letter(tileSize * 34, tileSize * 45, player);
		letter10 = new Letter(tileSize * 41, tileSize * 26, player);

		this.bossAttack = new Bossattack(this);
		this.boss = new Boss(this, bossAttack);




		//GAME SETUP
		backgroundMusic = new sound("/sound/Dungeon of Mystery (8-Bit Music).wav");
		gameOver = new sound("/sound/gameover.wav");
		gameComplete = new sound("/sound/MU.wav");
		attack = new sound("/sound/attack.wav");

		paper0 = new sound("/sound/paper.wav");
		paper1 = new sound("/sound/paper.wav");
		paper2 = new sound("/sound/paper.wav");
		paper3 = new sound("/sound/paper.wav");
		paper4 = new sound("/sound/paper.wav");
		paper5 = new sound("/sound/paper.wav");
		paper6 = new sound("/sound/paper.wav");
		paper7 = new sound("/sound/paper.wav");
		paper8 = new sound("/sound/paper.wav");
		paper9 = new sound("/sound/paper.wav");



		backgroundMusic.setVolume(0.2f);
		gameState = titleScreen;


		//LETTERS
		try {
			escapeImage = ImageIO.read(getClass().getResourceAsStream("/letter/escape.png"));
			tutorialImage = ImageIO.read(getClass().getResourceAsStream("/letter/tutorial.png"));
			bossImage = ImageIO.read(getClass().getResourceAsStream("/letter/boss.png"));
			keyImage = ImageIO.read(getClass().getResourceAsStream("/letter/key.png"));
			teleImage = ImageIO.read(getClass().getResourceAsStream("/letter/teleport.png"));
			spikesImage = ImageIO.read(getClass().getResourceAsStream("/letter/spikes.png"));
			monstersImage = ImageIO.read(getClass().getResourceAsStream("/letter/monsters.png"));
			endImage = ImageIO.read(getClass().getResourceAsStream("/letter/endgame.png"));
			suyImage = ImageIO.read(getClass().getResourceAsStream("/letter/suy.png"));
			tipsImage = ImageIO.read(getClass().getResourceAsStream("/letter/tips.png"));

		} catch (IOException e) {
		e.printStackTrace();
		}
	}


	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1_000_000_000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {

			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);

			lastTime = currentTime;

			if (delta >= 1) {

				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1_000_000_000) {
				drawCount = 0;
				timer = 0;
			}

		}
	}

	public void update() {
		if (gameState == titleScreen){
			gameReset();
		}
		if (gameState == playState) {//Trạng thái game hoạt động
			player.update();
			boss.update();
			bossAttack.update();
			backgroundMusic.playBackgroundMusic();

			//nightBorne
			nightBorne.update();
			nightBorne1.update();
			nightBorne2.update();
			nightBorne3.update();
			nightBorne4.update();
			nightBorne5.update();
			nightBorne6.update();
			nightBorne7.update();
			nightBorne8.update();
			nightBorne9.update();
			nightBorne10.update();
			nightBorne11.update();
			nightBorne12.update();
			nightBorne13.update();
			nightBorne14.update();

			//ghost
			ghost1.update();
			ghost2.update();
			ghost3.update();
			ghost4.update();
			ghost5.update();
			ghost6.update();
			ghost7.update();
			ghost8.update();
			ghost9.update();
			ghost10.update();
			ghost11.update();
			ghost12.update();
			ghost13.update();
			ghost14.update();
			ghost15.update();
			ghost16.update();
			ghost17.update();
			ghost18.update();
			ghost19.update();
			ghost20.update();
			ghost21.update();
			ghost22.update();
			ghost23.update();
			ghost24.update();
			ghost25.update();
			ghost26.update();
			ghost27.update();
			ghost28.update();
			ghost29.update();
			ghost30.update();

			//skeleton
			skeleton0.update();
			skeleton1.update();
			skeleton2.update();
			skeleton3.update();
			skeleton4.update();
			skeleton5.update();
			skeleton6.update();
			skeleton7.update();
			skeleton8.update();
			skeleton9.update();
			skeleton10.update();
			skeleton11.update();
			skeleton12.update();
			skeleton13.update();
			skeleton14.update();
			skeleton15.update();
			skeleton16.update();
			skeleton17.update();
			skeleton18.update();
			skeleton19.update();
			skeleton20.update();
			skeleton21.update();
			skeleton22.update();
			skeleton23.update();
			skeleton24.update();
			skeleton25.update();
			skeleton26.update();
			skeleton27.update();
			skeleton28.update();
			skeleton29.update();





			// KEYS
			key0.interact();
			key1.interact();
			key2.interact();
			key3.interact();
			key4.interact();
			key5.interact();
			key5.interact();
			key6.interact();
			key7.interact();
			key8.interact();

			//DOORS
			lockedDoor0.interact();
			lockedDoor1.interact();
			lockedDoor2.interact();
			lockedDoor3.interact();
			lockedDoor4.interact();
			lockedDoor5.interact();
			lockedDoor6.interact();
			lockedDoor7.interact();
			lockedDoor8.interact();


			if (player.getKeyCount() == 0 && !keyRemoved) {
				objects.remove(key0);
				objects.remove(key1);
				objects.remove(key2);
				objects.remove(key3);
				objects.remove(key4);
				objects.remove(key5);
				objects.remove(key6);
				objects.remove(key7);
				objects.remove(key8);// Loại bỏ chìa khóa khỏi danh sách đối tượng
				keyRemoved = true;  // Đánh dấu chìa khóa đã bị loại bỏ
			}
		}

		if (player.HP <= 0) {
			gameState = gameOverState;
			backgroundMusic.stopMusic();
			gameOver.playSoundEffect();
			player.HP = 1;
		}
		if (boss.currentHealth <= 0) {
			gameState = gameCompletedState;
			backgroundMusic.stopMusic();
			gameComplete.playSoundEffect();
			boss.currentHealth = 1;
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;


		//TITLE SCREEN
		if (gameState == titleScreen) {
			ui.draw(g2);
		} else {


			// TILE
			tileM.draw(g2);

			//PLAYER
			player.draw(g2);
			boss.draw(g2);
			bossAttack.draw(g2);

			//enemy
			nightBorne.draw(g2);
			nightBorne1.draw(g2);
			nightBorne2.draw(g2);
			nightBorne3.draw(g2);
			nightBorne4.draw(g2);
			nightBorne5.draw(g2);
			nightBorne6.draw(g2);
			nightBorne7.draw(g2);
			nightBorne8.draw(g2);
			nightBorne9.draw(g2);
			nightBorne10.draw(g2);
			nightBorne11.draw(g2);
			nightBorne12.draw(g2);
			nightBorne13.draw(g2);
			nightBorne14.draw(g2);




			ghost1.draw(g2);
			ghost2.draw(g2);
			ghost3.draw(g2);
			ghost4.draw(g2);
			ghost5.draw(g2);
			ghost6.draw(g2);
			ghost7.draw(g2);
			ghost8.draw(g2);
			ghost9.draw(g2);
			ghost10.draw(g2);
			ghost11.draw(g2);
			ghost12.draw(g2);
			ghost13.draw(g2);
			ghost14.draw(g2);
			ghost15.draw(g2);
			ghost16.draw(g2);
			ghost17.draw(g2);
			ghost18.draw(g2);
			ghost19.draw(g2);
			ghost20.draw(g2);
			ghost21.draw(g2);
			ghost22.draw(g2);
			ghost23.draw(g2);
			ghost24.draw(g2);
			ghost25.draw(g2);
			ghost26.draw(g2);
			ghost27.draw(g2);
			ghost28.draw(g2);
			ghost29.draw(g2);
			ghost30.draw(g2);


			skeleton0.draw(g2);
			skeleton1.draw(g2);
			skeleton2.draw(g2);
			skeleton3.draw(g2);
			skeleton4.draw(g2);
			skeleton5.draw(g2);
			skeleton6.draw(g2);
			skeleton7.draw(g2);
			skeleton8.draw(g2);
			skeleton9.draw(g2);
			skeleton10.draw(g2);
			skeleton11.draw(g2);
			skeleton12.draw(g2);
			skeleton13.draw(g2);
			skeleton14.draw(g2);
			skeleton15.draw(g2);
			skeleton16.draw(g2);
			skeleton17.draw(g2);
			skeleton18.draw(g2);
			skeleton19.draw(g2);
			skeleton20.draw(g2);
			skeleton21.draw(g2);
			skeleton22.draw(g2);
			skeleton23.draw(g2);
			skeleton24.draw(g2);
			skeleton25.draw(g2);
			skeleton26.draw(g2);
			skeleton27.draw(g2);
			skeleton28.draw(g2);
			skeleton29.draw(g2);










			//UI
			ui.draw(g2);

			//OBJECT
			// KEYS
			key0.draw(g2);
			key1.draw(g2);
			key2.draw(g2);
			key3.draw(g2);
			key4.draw(g2);
			key5.draw(g2);
			key6.draw(g2);
			key7.draw(g2);
			key8.draw(g2);

			//DOORS
			lockedDoor0.draw(g2);
			lockedDoor1.draw(g2);
			lockedDoor2.draw(g2);
			lockedDoor3.draw(g2);
			lockedDoor4.draw(g2);
			lockedDoor5.draw(g2);
			lockedDoor6.draw(g2);
			lockedDoor7.draw(g2);
			lockedDoor8.draw(g2);

			//LETTER
			letter0.draw(g2);
			letter1.draw(g2);
			letter2.draw(g2);
			letter3.draw(g2);
			letter4.draw(g2);
			letter5.draw(g2);
			letter6.draw(g2);
			letter8.draw(g2);
			letter9.draw(g2);
			letter10.draw(g2);


			//UI
			ui.draw(g2);

		}


		if (gameState != titleScreen && eHandler.hit(4, 3, "any")) {
			paper0.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(tutorialImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(18, 8, "any")) {
			paper1.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(escapeImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(4, 14, "any")) {
			paper2.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(keyImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(22, 29, "any")) {
			paper3.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(bossImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(22, 24, "any")) {
			paper4.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(monstersImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(46, 13, "any")) {
			paper5.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(teleImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(4, 18, "any")) {
			paper6.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(spikesImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(21, 37, "any")) {
			paper7.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(endImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(34, 45, "any")) {
			paper8.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(suyImage, 176, 64, 416, 448, null);
		}
		if (gameState != titleScreen && eHandler.hit(41, 26, "any")) {
			paper9.playSoundEffect();
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.drawImage(tipsImage, 176, 64, 416, 448, null);
		}


		if (gameState == pauseState) {
			ui.drawPauseScreen();
		}

		if (gameState == gameCompletedState) {
			ui.drawCompletedScreen();
		}

		if (gameState == creditsState) {
			ui.drawCreditsScreen();
		}

		if (gameState == gameOverState) {
			ui.drawGameOverScreen();
		}

		g2.dispose();

	}

	public void gameReset() {
		player.setDefaultValues();
		setupEnemy();
		boss.setDefaultValues();
		setupKeyAndDoor();
	}

	public void setupEnemy(){
		enemies = new ArrayList<>();

		nightBorne = new NightBorne(this, tileSize * 42, tileSize * 16);
		nightBorne1 = new NightBorne(this, tileSize * 21, tileSize * 26);
		nightBorne6 = new NightBorne(this, (int) (tileSize * 26.5), tileSize * 26);
		nightBorne2 = new NightBorne(this, tileSize * 43, tileSize * 45);
		nightBorne3 = new NightBorne(this, tileSize * 24, tileSize * 38);
		nightBorne4 = new NightBorne(this, tileSize * 34, tileSize * 34);
		nightBorne5 = new NightBorne(this, tileSize * 34, tileSize * 26);
		nightBorne7 = new NightBorne(this, tileSize * 24, tileSize * 44);
		nightBorne8= new NightBorne(this, tileSize * 28, tileSize * 10);
		nightBorne9 = new NightBorne(this, tileSize * 39, tileSize * 32);
		nightBorne10 = new NightBorne(this, tileSize * 23, tileSize * 4);
		nightBorne11 =  new NightBorne(this, tileSize * 48, tileSize * 26);
		nightBorne12 = new NightBorne(this, tileSize * 15, tileSize * 29);
		nightBorne13 =  new NightBorne(this, tileSize * 8, tileSize * 29);
		nightBorne14 =  new NightBorne(this, tileSize * 27, tileSize * 16);






		ghost1 = new ghost(this, tileSize * 17, tileSize * 7);
		ghost15 = new ghost(this, tileSize * 14, tileSize * 6);
		ghost16 = new ghost(this, tileSize * 11, tileSize * 9);
		ghost17 = new ghost(this, tileSize * 16, tileSize * 3);

		ghost2 = new ghost(this, tileSize * 35, tileSize * 2);
		ghost18 = new ghost(this, tileSize * 32, tileSize * 3);
		ghost19 = new ghost(this, tileSize * 37, tileSize * 3);
		ghost20 = new ghost(this, tileSize * 32, tileSize * 6);
		ghost21 = new ghost(this, tileSize * 37, tileSize * 6);


		ghost3 = new ghost(this, tileSize * 38, tileSize * 19);

		ghost4 = new ghost(this, tileSize * 43, tileSize * 13);

		ghost5 = new ghost(this, tileSize * 46, tileSize * 20);
		ghost8 = new ghost(this, tileSize * 46, tileSize * 18);
		ghost22 = new ghost(this, tileSize * 46, tileSize * 18);

		ghost6 = new ghost(this, tileSize * 31, tileSize * 17);
		ghost7 = new ghost(this, tileSize * 44, tileSize * 37);


		ghost9 = new ghost(this, tileSize * 19, tileSize * 14);

		ghost10 = new ghost(this, (int) (tileSize * 19.5), tileSize * 33);
		ghost11 = new ghost(this, tileSize * 27, tileSize * 33);

		ghost12 = new ghost(this, tileSize * 42, tileSize * 29);
		ghost25 = new ghost(this, tileSize * 44, tileSize * 29);


		ghost13 = new ghost(this, tileSize * 5, tileSize * 12);
		ghost14 = new ghost(this, tileSize * 5, tileSize * 20);


		ghost23 = new ghost(this, tileSize * 19, tileSize * 21);
		ghost24 = new ghost(this, tileSize * 28, tileSize * 21);

		ghost26 = new ghost(this, tileSize * 45, tileSize * 43);
		ghost27 = new ghost(this, tileSize * 47, tileSize * 46);

		ghost28 = new ghost(this, tileSize * 39, tileSize * 43);

		ghost29 = new ghost(this, tileSize * 21, tileSize * 42);
		ghost30 = new ghost(this, tileSize * 25, tileSize * 42);








		skeleton0 = new skeleton(this, (int) (tileSize * 8.5), tileSize * 19);
		skeleton2 = new skeleton(this, (int) (tileSize * 8.5), tileSize * 11);
		skeleton1 = new skeleton(this, tileSize * 16, tileSize * 17);
		skeleton3 = new skeleton(this, tileSize * 19, tileSize * 28);
		skeleton4 = new skeleton(this, tileSize * 27, tileSize * 28);


		skeleton16 = new skeleton(this, (int) (tileSize * 22.5), (int) (tileSize * 8.5));
		skeleton17 = new skeleton(this, (int) (tileSize * 23.5), (int) (tileSize * 11.5));
		skeleton18 = new skeleton(this, (int) (tileSize * 22.5), (int) (tileSize * 15.5));
		skeleton19 = new skeleton(this, (int) (tileSize * 23.5), (int) (tileSize * 18.5));

		skeleton6 = new skeleton(this, (int) (tileSize * 42.5), (int) (tileSize * 2.5));
		skeleton7 = new skeleton(this, (int) (tileSize * 46.5), (int) (tileSize * 2.5));
		skeleton13 = new skeleton(this, (int) (tileSize * 44.5), (int) (tileSize * 3.5));
		skeleton14 = new skeleton(this, (int) (tileSize * 43.5), (int) (tileSize * 5.5));
		skeleton15 = new skeleton(this, (int) (tileSize * 45.5), (int) (tileSize * 5.5));

		skeleton8 = new skeleton(this, tileSize * 38, tileSize * 13);
		skeleton9 = new skeleton(this, tileSize * 42, tileSize * 19);

		skeleton10 = new skeleton(this, tileSize * 46, tileSize * 12);
		skeleton5 = new skeleton(this, tileSize * 46, tileSize * 14);
		skeleton23 = new skeleton(this, tileSize * 46, tileSize * 14);

		skeleton11 = new skeleton(this, tileSize * 14, tileSize * 21);
		skeleton12 = new skeleton(this, tileSize * 14, tileSize * 24);

		skeleton20 = new skeleton(this, tileSize * 42, tileSize * 36);
		skeleton21 = new skeleton(this, tileSize * 45, tileSize * 36);
		skeleton22 = new skeleton(this, tileSize * 42, tileSize * 40);

		skeleton24 = new skeleton(this, (int) (tileSize * 32), (int) (tileSize * 42));
		skeleton25 = new skeleton(this, (int) (tileSize * 32), (int) (tileSize * 46));
		skeleton26 = new skeleton(this, (int) (tileSize * 36), (int) (tileSize * 42));
		skeleton27 = new skeleton(this, (int) (tileSize * 36), (int) (tileSize * 46));

		skeleton28 = new skeleton(this, (int) (tileSize * 21), (int) (tileSize * 39));
		skeleton29 = new skeleton(this, (int) (tileSize * 25), (int) (tileSize * 39));




		enemies.add(nightBorne);
		enemies.add(nightBorne1);
		enemies.add(nightBorne2);
		enemies.add(nightBorne3);
		enemies.add(nightBorne4);
		enemies.add(nightBorne5);
		enemies.add(nightBorne6);
		enemies.add(nightBorne7);
		enemies.add(nightBorne8);
		enemies.add(nightBorne9);
		enemies.add(nightBorne10);
		enemies.add(nightBorne11);
		enemies.add(nightBorne12);
		enemies.add(nightBorne13);
		enemies.add(nightBorne14);



		enemies.add(ghost1);
		enemies.add(ghost2);
		enemies.add(ghost3);
		enemies.add(ghost4);
		enemies.add(ghost5);
		enemies.add(ghost6);
		enemies.add(ghost7);
		enemies.add(ghost8);
		enemies.add(ghost9);
		enemies.add(ghost10);
		enemies.add(ghost11);
		enemies.add(ghost12);
		enemies.add(ghost13);
		enemies.add(ghost14);
		enemies.add(ghost15);
		enemies.add(ghost16);
		enemies.add(ghost17);
		enemies.add(ghost18);
		enemies.add(ghost19);
		enemies.add(ghost20);
		enemies.add(ghost21);
		enemies.add(ghost22);
		enemies.add(ghost23);
		enemies.add(ghost24);
		enemies.add(ghost25);
		enemies.add(ghost26);
		enemies.add(ghost27);
		enemies.add(ghost28);
		enemies.add(ghost29);
		enemies.add(ghost30);


		enemies.add(skeleton0);
		enemies.add(skeleton1);
		enemies.add(skeleton2);
		enemies.add(skeleton3);
		enemies.add(skeleton4);
		enemies.add(skeleton5);
		enemies.add(skeleton6);
		enemies.add(skeleton7);
		enemies.add(skeleton8);
		enemies.add(skeleton9);
		enemies.add(skeleton10);
		enemies.add(skeleton11);
		enemies.add(skeleton12);
		enemies.add(skeleton13);
		enemies.add(skeleton14);
		enemies.add(skeleton15);
		enemies.add(skeleton16);
		enemies.add(skeleton17);
		enemies.add(skeleton18);
		enemies.add(skeleton19);
		enemies.add(skeleton20);
		enemies.add(skeleton21);
		enemies.add(skeleton22);
		enemies.add(skeleton23);
		enemies.add(skeleton24);
		enemies.add(skeleton25);
		enemies.add(skeleton26);
		enemies.add(skeleton27);
		enemies.add(skeleton28);
		enemies.add(skeleton29);


	}

	public void setupKeyAndDoor(){
		//KEYS
		key0 = new Key(tileSize * 3, tileSize * 3, player);
		key1 = new Key(tileSize * 7, tileSize * 13, player);
		key2 = new Key(tileSize * 47, tileSize*  2, player);
		key3 = new Key((int) (tileSize * 38.5), tileSize * 20, player);
		key4 = new Key((int) (tileSize * 42.5), tileSize * 13, player);
		key5 = new Key((int) (tileSize * 42.5), tileSize * 20, player);
		key6 = new Key(tileSize * 47, tileSize * 33, player);
		key7 = new Key((int) (tileSize * 23.5), tileSize * 27, player);
		key8 = new Key((int) (tileSize * 27.5), (int) (tileSize * 39.5), player);

		//DOORS
		lockedDoor0 = new LockedDoor(tileSize * 11, tileSize * 11, player, this);
		lockedDoor1 = new LockedDoor(tileSize * 23, tileSize * 7, player, this);
		lockedDoor2 = new LockedDoor(tileSize * 34, tileSize * 8, player, this);
		lockedDoor3 = new LockedDoor(tileSize * 34, tileSize * 24, player, this);
		lockedDoor4 = new LockedDoor(tileSize * 46, tileSize * 14, player, this);
		lockedDoor5 = new LockedDoor(tileSize * 46, tileSize * 18, player, this);
		lockedDoor6 = new LockedDoor(tileSize * 47, tileSize * 40, player, this);
		lockedDoor7 = new LockedDoor(tileSize * 23, tileSize * 34, player, this);
		lockedDoor8 = new LockedDoor(tileSize * 17, tileSize * 39, player, this);
	}
}
