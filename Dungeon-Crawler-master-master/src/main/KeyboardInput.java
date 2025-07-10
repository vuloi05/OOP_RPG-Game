package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInput implements KeyListener {

    public boolean diTren, diXuong, diTrai, diPhai, attack, interact;

    GamePanel gp;

    public KeyboardInput(GamePanel gp) {
        this.gp = gp;
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        //TITLE SCREEN
        if (gp.gameState == gp.titleScreen){
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            }
        }

        //PLAY STATE
        if (gp.gameState == gp.playState) {
            if (e.getKeyCode() == KeyEvent.VK_W) {
                diTren = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                diXuong = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                diTrai = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                diPhai = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                attack = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                interact = true;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_M) {
            if (gp.gameState == gp.playState) {gp.gameState = gp.pauseState;}
            else if (gp.gameState == gp.pauseState) {gp.gameState = gp.playState;}
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            if (gp.gameState == gp.playState) {gp.gameState = gp.creditsState;}
            else if (gp.gameState == gp.creditsState) {gp.gameState = gp.playState;}
        }

        if (gp.gameState == gp.gameOverState){
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                gp.gameState = gp.titleScreen;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W) {
            diTren = false;
       }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            diXuong = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            diTrai = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            diPhai = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            attack = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            interact = false;
        }
    }
}



	
	


