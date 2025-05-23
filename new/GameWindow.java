package com.dungeondelicacy.rpg;

import com.dungeondelicacy.rpg.characters.Character;
import com.dungeondelicacy.rpg.characters.Enemy;
import com.dungeondelicacy.rpg.characters.Player;
import com.dungeondelicacy.rpg.map.Map;
import com.dungeondelicacy.rpg.map.Tile;
import com.dungeondelicacy.rpg.systems.GameManager;
import com.dungeondelicacy.rpg.systems.Item;
import com.dungeondelicacy.rpg.systems.Quest;
import com.dungeondelicacy.rpg.systems.NPC;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameWindow {
    private long window;
    private final int width = 800;
    private final int height = 600;
    private GameManager gameManager;
    private final int mapSize = 16;
    private final int tileSize = 35;
    private boolean isInBattle;
    private Enemy battleEnemy;
    private boolean isDefending;
    private boolean showInventory;
    private boolean showQuests;
    private boolean showNPCDialog;
    private String battleMessage; // Thông báo kết quả chiến đấu

    public GameWindow(GameManager gameManager) {
        this.gameManager = gameManager;
        this.isInBattle = false;
        this.battleEnemy = null;
        this.isDefending = false;
        this.showInventory = false;
        this.showQuests = false;
        this.showNPCDialog = false;
        this.battleMessage = "";
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        window = glfwCreateWindow(width, height, "Mỹ Vị Hầm Ngục", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }
            handleKeyInput(key, action);
        });
        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            handleMouseInput(button, action);
        });
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        }
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            update();
            render();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void update() {
        if (!isInBattle) {
            Player player = gameManager.getPlayer();
            Map map = gameManager.getCurrentMap();
            for (Enemy enemy : map.getEnemies()) {
                if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                    isInBattle = true;
                    battleEnemy = enemy;
                    gameManager.getBattleSystem().startBattle(player, enemy.getType());
                    break;
                }
            }
            showNPCDialog = false;
            for (NPC npc : map.getNpcs()) {
                if (npc.getX() == player.getX() && npc.getY() == player.getY()) {
                    showNPCDialog = true;
                    break;
                }
            }
        }
    }

    private void render() {
        drawMap();
        drawEnemies();
        drawNPCs();
        drawPlayer();
        if (isInBattle && battleEnemy != null) {
            drawBattleDialog();
            drawHealthBars();
            drawBattleMessage();
        }
        if (showInventory) {
            drawInventory();
        }
        if (showQuests) {
            drawQuests();
        }
        if (showNPCDialog) {
            drawNPCDialog();
        }
    }

    private void drawMap() {
        Map map = gameManager.getCurrentMap();
        if (map == null) return;
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                float x = j * tileSize;
                float y = i * tileSize;
                float x1 = (x / (float) width) * 2 - 1;
                float y1 = 1 - (y / (float) height) * 2;
                float x2 = ((x + tileSize) / (float) width) * 2 - 1;
                float y2 = 1 - ((y + tileSize) / (float) height) * 2;
                Tile tile = map.getTile(i, j);
                if (tile.getType().equals("Ground")) {
                    glColor3f(0.6f, 0.4f, 0.2f); // Màu nâu đất
                } else if (tile.getType().equals("Wall")) {
                    glColor3f(0.3f, 0.3f, 0.3f); // Màu xám đậm
                } else if (tile.getType().equals("Transition")) {
                    glColor3f(0.0f, 0.5f, 1.0f); // Màu xanh dương nhạt
                }
                glBegin(GL_QUADS);
                glVertex2f(x1, y1);
                glVertex2f(x2, y1);
                glVertex2f(x2, y2);
                glVertex2f(x1, y2);
                glEnd();
            }
        }
    }

    private void drawEnemies() {
        Map map = gameManager.getCurrentMap();
        if (map == null) return;
        for (Enemy enemy : map.getEnemies()) {
            float x = enemy.getX() * tileSize + tileSize / 4;
            float y = enemy.getY() * tileSize + tileSize / 4;
            float radius = tileSize / 3.0f; // Tăng kích thước
            float centerX = (x / (float) width) * 2 - 1;
            float centerY = 1 - (y / (float) height) * 2;
            glColor3f(0.2f, 0.8f, 0.2f); // Màu xanh lá đậm
            glBegin(GL_TRIANGLE_FAN);
            glVertex2f(centerX, centerY);
            int numSegments = 20;
            for (int i = 0; i <= numSegments; i++) {
                float angle = (float) (2.0 * Math.PI * i / numSegments);
                float dx = (radius / (float) width) * 2 * (float) Math.cos(angle);
                float dy = (radius / (float) height) * 2 * (float) Math.sin(angle);
                glVertex2f(centerX + dx, centerY + dy);
            }
            glEnd();
        }
    }

    private void drawNPCs() {
        Map map = gameManager.getCurrentMap();
        if (map == null) return;
        for (NPC npc : map.getNpcs()) {
            float x = npc.getX() * tileSize + tileSize / 4;
            float y = npc.getY() * tileSize + tileSize / 4;
            float radius = tileSize / 3.0f;
            float centerX = (x / (float) width) * 2 - 1;
            float centerY = 1 - (y / (float) height) * 2;
            glColor3f(0.5f, 0.5f, 1.0f); // Màu xanh dương nhạt
            glBegin(GL_TRIANGLE_FAN);
            glVertex2f(centerX, centerY);
            int numSegments = 20;
            for (int i = 0; i <= numSegments; i++) {
                float angle = (float) (2.0 * Math.PI * i / numSegments);
                float dx = (radius / (float) width) * 2 * (float) Math.cos(angle);
                float dy = (radius / (float) height) * 2 * (float) Math.sin(angle);
                glVertex2f(centerX + dx, centerY + dy);
            }
            glEnd();
        }
    }

    private void drawPlayer() {
        Player player = gameManager.getPlayer();
        if (player == null) return;
        float x = player.getX() * tileSize + tileSize / 4;
        float y = player.getY() * tileSize + tileSize / 4;
        float radius = tileSize / 3.0f; // Tăng kích thước
        float centerX = (x / (float) width) * 2 - 1;
        float centerY = 1 - (y / (float) height) * 2;
        glColor3f(1.0f, 0.3f, 0.3f); // Màu đỏ đậm
        glBegin(GL_TRIANGLE_FAN);
        glVertex2f(centerX, centerY);
        int numSegments = 20;
        for (int i = 0; i <= numSegments; i++) {
            float angle = (float) (2.0 * Math.PI * i / numSegments);
            float dx = (radius / (float) width) * 2 * (float) Math.cos(angle);
            float dy = (radius / (float) height) * 2 * (float) Math.sin(angle);
            glVertex2f(centerX + dx, centerY + dy);
        }
        glEnd();
    }

    private void drawBattleDialog() {
        float dialogWidth = 400.0f / width * 2;
        float dialogHeight = 200.0f / height * 2;
        float x1 = -dialogWidth / 2;
        float y1 = dialogHeight / 2;
        float x2 = dialogWidth / 2;
        float y2 = -dialogHeight / 2;
        glColor3f(0.4f, 0.4f, 0.4f); // Màu xám nhạt
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
    }

    private void drawHealthBars() {
        Player player = gameManager.getPlayer();
        if (player == null || battleEnemy == null) return;
        drawHealthBar(player, 10, 10, "Player");
        drawManaBar(player, 10, 30);
        drawHealthBar(battleEnemy, width - 110, 10, battleEnemy.getType());
    }

    private void drawHealthBar(Character character, float x, float y, String label) {
        float x1 = (x / (float) width) * 2 - 1;
        float y1 = 1 - (y / (float) height) * 2;
        float barWidth = 100.0f / width * 2;
        float barHeight = 10.0f / height * 2;
        float hpRatio = (float) character.getHp() / 100.0f;
        glColor3f(0.5f, 0.5f, 0.5f);
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x1 + barWidth, y1);
        glVertex2f(x1 + barWidth, y1 - barHeight);
        glVertex2f(x1, y1 - barHeight);
        glEnd();
        glColor3f(1.0f, 0.0f, 0.0f);
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x1 + barWidth * hpRatio, y1);
        glVertex2f(x1 + barWidth * hpRatio, y1 - barHeight);
        glVertex2f(x1, y1 - barHeight);
        glEnd();
    }

    private void drawManaBar(Character character, float x, float y) {
        float x1 = (x / (float) width) * 2 - 1;
        float y1 = 1 - (y / (float) height) * 2;
        float barWidth = 100.0f / width * 2;
        float barHeight = 10.0f / height * 2;
        float mpRatio = (float) character.getMp() / 50.0f;
        glColor3f(0.5f, 0.5f, 0.5f);
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x1 + barWidth, y1);
        glVertex2f(x1 + barWidth, y1 - barHeight);
        glVertex2f(x1, y1 - barHeight);
        glEnd();
        glColor3f(0.0f, 0.0f, 1.0f);
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x1 + barWidth * mpRatio, y1);
        glVertex2f(x1 + barWidth * mpRatio, y1 - barHeight);
        glVertex2f(x1, y1 - barHeight);
        glEnd();
    }

    private void drawBattleMessage() {
        // Hiển thị thông báo kết quả chiến đấu (giả lập text bằng hình chữ nhật)
        float x1 = -0.3f;
        float y1 = 0.0f;
        float x2 = 0.3f;
        float y2 = -0.05f;
        glColor3f(1.0f, 1.0f, 0.0f); // Màu vàng
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
    }

    private void drawInventory() {
        float dialogWidth = 300.0f / width * 2;
        float dialogHeight = 200.0f / height * 2;
        float x1 = -1.0f + 0.1f;
        float y1 = -1.0f + 0.1f;
        float x2 = x1 + dialogWidth;
        float y2 = y1 + dialogHeight;
        glColor3f(0.4f, 0.4f, 0.4f);
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
        // Hiển thị danh sách vật phẩm
        List<Item> items = gameManager.getPlayer().getInventory().getItems();
        float itemY = y2 - 0.05f;
        for (int i = 0; i < Math.min(3, items.size()); i++) { // Hiển thị tối đa 3 vật phẩm
            float itemHeight = 0.03f;
            glColor3f(0.8f, 0.8f, 0.8f);
            glBegin(GL_QUADS);
            glVertex2f(x1 + 0.02f, itemY);
            glVertex2f(x2 - 0.02f, itemY);
            glVertex2f(x2 - 0.02f, itemY - itemHeight);
            glVertex2f(x1 + 0.02f, itemY - itemHeight);
            glEnd();
            itemY -= itemHeight + 0.01f;
        }
    }

    private void drawQuests() {
        float dialogWidth = 300.0f / width * 2;
        float dialogHeight = 200.0f / height * 2;
        float x1 = 1.0f - dialogWidth - 0.1f;
        float y1 = 1.0f - 0.1f;
        float x2 = 1.0f - 0.1f;
        float y2 = y1 - dialogHeight;
        glColor3f(0.4f, 0.4f, 0.4f);
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
        // Hiển thị danh sách nhiệm vụ
        List<Quest> quests = gameManager.getPlayer().getActiveQuests();
        float questY = y1 - 0.05f;
        for (int i = 0; i < Math.min(3, quests.size()); i++) {
            float questHeight = 0.03f;
            glColor3f(0.8f, 0.8f, 0.8f);
            glBegin(GL_QUADS);
            glVertex2f(x1 + 0.02f, questY);
            glVertex2f(x2 - 0.02f, questY);
            glVertex2f(x2 - 0.02f, questY - questHeight);
            glVertex2f(x1 + 0.02f, questY - questHeight);
            glEnd();
            questY -= questHeight + 0.01f;
        }
    }

    private void drawNPCDialog() {
        float dialogWidth = 400.0f / width * 2;
        float dialogHeight = 200.0f / height * 2;
        float x1 = -dialogWidth / 2;
        float y1 = dialogHeight / 2;
        float x2 = dialogWidth / 2;
        float y2 = -dialogHeight / 2;
        glColor3f(0.4f, 0.4f, 0.4f);
        glBegin(GL_QUADS);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(x1, y1);
        glVertex2f(x2, y1);
        glVertex2f(x2, y2);
        glVertex2f(x1, y2);
        glEnd();
        // Hiển thị câu thoại (giả lập)
        float textY = y1 - 0.05f;
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_QUADS);
        glVertex2f(x1 + 0.02f, textY);
        glVertex2f(x2 - 0.02f, textY);
        glVertex2f(x2 - 0.02f, textY - 0.03f);
        glVertex2f(x1 + 0.02f, textY - 0.03f);
        glEnd();
        // Hiển thị nút tương tác (Mua đồ, Nhận nhiệm vụ)
        float buttonWidth = 0.2f;
        float buttonHeight = 0.05f;
        // Nút "Mua đồ"
        glColor3f(0.6f, 0.6f, 0.6f);
        glBegin(GL_QUADS);
        glVertex2f(x1 + 0.05f, y2 + 0.1f);
        glVertex2f(x1 + 0.05f + buttonWidth, y2 + 0.1f);
        glVertex2f(x1 + 0.05f + buttonWidth, y2 + 0.1f - buttonHeight);
        glVertex2f(x1 + 0.05f, y2 + 0.1f - buttonHeight);
        glEnd();
        // Nút "Nhận nhiệm vụ"
        glColor3f(0.6f, 0.6f, 0.6f);
        glBegin(GL_QUADS);
        glVertex2f(x2 - 0.05f - buttonWidth, y2 + 0.1f);
        glVertex2f(x2 - 0.05f, y2 + 0.1f);
        glVertex2f(x2 - 0.05f, y2 + 0.1f - buttonHeight);
        glVertex2f(x2 - 0.05f - buttonWidth, y2 + 0.1f - buttonHeight);
        glEnd();
    }

    private void handleKeyInput(int key, int action) {
        if (action != GLFW_PRESS) return;
        Player player = gameManager.getPlayer();
        Map map = gameManager.getCurrentMap();
        int newX = player.getX();
        int newY = player.getY();
        if (!isInBattle && !showNPCDialog) {
            if (key == GLFW_KEY_W) {
                newY--;
            } else if (key == GLFW_KEY_S) {
                newY++;
            } else if (key == GLFW_KEY_A) {
                newX--;
            } else if (key == GLFW_KEY_D) {
                newX++;
            }
            gameManager.movePlayer(newX, newY);
            Tile tile = map.getTile(player.getX(), player.getY());
            if (tile.getType().equals("Transition")) {
                String currentMapId = map.getMapId();
                if (currentMapId.equals("Tầng 1 - Khu chợ") && player.getX() == 14 && player.getY() == 14) {
                    gameManager.switchMap("Tầng 2 - Rừng rậm");
                } else if (currentMapId.equals("Tầng 2 - Rừng rậm") && player.getX() == 14 && player.getY() == 14) {
                    gameManager.switchMap("Tầng 3 - Hang động tối");
                } else if (currentMapId.equals("Tầng 3 - Hang động tối") && player.getX() == 14 && player.getY() == 14) {
                    gameManager.switchMap("Tầng cuối - Hang rồng");
                } else if (player.getX() == 1 && player.getY() == 1) {
                    gameManager.goBackToPreviousMap();
                }
                if (currentMapId.equals("Tầng cuối - Hang rồng") && player.getX() == 1 && player.getY() == 1) {
                    gameManager.endGame();
                    glfwSetWindowShouldClose(window, true);
                }
            }
        } else if (isInBattle) {
            if (key == GLFW_KEY_1) {
                gameManager.getBattleSystem().useSkill(player, "Healing");
            }
        } else if (showNPCDialog) {
            if (key == GLFW_KEY_1) {
                // Mua đồ
                gameManager.interactWithNPC(player, "Buy");
            } else if (key == GLFW_KEY_2) {
                // Nhận nhiệm vụ
                gameManager.interactWithNPC(player, "Quest");
            }
        }
        if (key == GLFW_KEY_I) {
            showInventory = !showInventory;
            showQuests = false;
            showNPCDialog = false;
        } else if (key == GLFW_KEY_Q) {
            showQuests = !showQuests;
            showInventory = false;
            showNPCDialog = false;
        }
        if (key == GLFW_KEY_F5) {
            gameManager.saveGame();
        } else if (key == GLFW_KEY_F6) {
            gameManager.loadGame();
        }
    }

    private void handleMouseInput(int button, int action) {
        if (action != GLFW_PRESS || !isInBattle || battleEnemy == null) return;
        Player player = gameManager.getPlayer();
        if (button == GLFW_MOUSE_BUTTON_LEFT) {
            int damage = gameManager.getBattleSystem().attack(player, battleEnemy, isDefending);
            battleMessage = player.getName() + " deals " + damage + " damage to " + battleEnemy.getName();
        } else if (button == GLFW_MOUSE_BUTTON_RIGHT) {
            isDefending = true;
        }
    }

    private void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}