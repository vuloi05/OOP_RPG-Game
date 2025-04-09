package com.dungeondelicacy.rpg;

import com.dungeondelicacy.rpg.characters.Enemy;
import com.dungeondelicacy.rpg.characters.Player;
import com.dungeondelicacy.rpg.map.Map;
import com.dungeondelicacy.rpg.map.Tile;
import com.dungeondelicacy.rpg.systems.GameManager;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameWindow {
    private long window; // ID của cửa sổ GLFW
    private final int width = 800; // Chiều rộng cửa sổ
    private final int height = 600; // Chiều cao cửa sổ
    private GameManager gameManager; // Quản lý game
    private final int mapSize = 16; // Kích thước bản đồ (16x16)
    private final int tileSize = 35; // Kích thước mỗi ô (pixel), điều chỉnh để vừa màn hình

    public GameWindow(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        // Thiết lập callback lỗi
        GLFWErrorCallback.createPrint(System.err).set();

        // Khởi tạo GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Cấu hình GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Ẩn cửa sổ cho đến khi tạo xong
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // Cho phép thay đổi kích thước cửa sổ

        // Tạo cửa sổ
        window = glfwCreateWindow(width, height, "Mỹ Vị Hầm Ngục", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Thiết lập callback cho phím
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true); // Đóng cửa sổ khi nhấn ESC
            }
            handleInput(key, action);
        });

        // Căn giữa cửa sổ
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        // Thiết lập context OpenGL
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Bật V-Sync
        glfwShowWindow(window); // Hiển thị cửa sổ

        // Khởi tạo OpenGL
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Màu nền đen
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Xóa buffer

            render(); // Vẽ giao diện

            glfwSwapBuffers(window); // Hoán đổi buffer
            glfwPollEvents(); // Xử lý sự kiện
        }
    }

    private void render() {
        // Vẽ bản đồ
        drawMap();

        // Vẽ quái vật
        drawEnemies();

        // Vẽ người chơi
        drawPlayer();
    }

    private void drawMap() {
        Map map = gameManager.getCurrentMap();
        if (map == null) return;

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                float x = j * tileSize;
                float y = i * tileSize;

                // Chuyển tọa độ sang không gian OpenGL (-1 đến 1)
                float x1 = (x / (float) width) * 2 - 1;
                float y1 = 1 - (y / (float) height) * 2;
                float x2 = ((x + tileSize) / (float) width) * 2 - 1;
                float y2 = 1 - ((y + tileSize) / (float) height) * 2;

                // Chọn màu dựa trên loại ô
                Tile tile = map.getTile(i, j);
                if (tile.getType().equals("Ground")) {
                    glColor3f(0.5f, 0.5f, 0.5f); // Màu xám cho đất
                } else if (tile.getType().equals("Wall")) {
                    glColor3f(0.2f, 0.2f, 0.2f); // Màu xám đậm cho tường
                } else if (tile.getType().equals("Transition")) {
                    glColor3f(0.0f, 0.0f, 1.0f); // Màu xanh dương cho ô chuyển bản đồ
                }

                // Vẽ ô
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
            float radius = tileSize / 4.0f;

            // Chuyển tọa độ sang không gian OpenGL
            float centerX = (x / (float) width) * 2 - 1;
            float centerY = 1 - (y / (float) height) * 2;

            // Vẽ hình tròn cho quái vật (màu xanh lá)
            glColor3f(0.0f, 1.0f, 0.0f); // Màu xanh lá
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
        float radius = tileSize / 4.0f;

        // Chuyển tọa độ sang không gian OpenGL
        float centerX = (x / (float) width) * 2 - 1;
        float centerY = 1 - (y / (float) height) * 2;

        // Vẽ hình tròn cho người chơi
        glColor3f(1.0f, 0.0f, 0.0f); // Màu đỏ
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

    private void handleInput(int key, int action) {
        if (action != GLFW_PRESS) return;

        Player player = gameManager.getPlayer();
        Map map = gameManager.getCurrentMap();
        int newX = player.getX();
        int newY = player.getY();

        // Xử lý di chuyển bằng phím W/A/S/D
        if (key == GLFW_KEY_W) {
            newY--;
        } else if (key == GLFW_KEY_S) {
            newY++;
        } else if (key == GLFW_KEY_A) {
            newX--;
        } else if (key == GLFW_KEY_D) {
            newX++;
        }

        // Di chuyển người chơi
        gameManager.movePlayer(newX, newY);

        // Kiểm tra ô chuyển bản đồ
        Tile tile = map.getTile(player.getX(), player.getY());
        if (tile.getType().equals("Transition")) {
            String currentMapId = map.getMapId();
            // Ô (14,14) ở Tầng 1, Tầng 2, Tầng 3: Chuyển tiến
            if (currentMapId.equals("Tầng 1 - Khu chợ") && player.getX() == 14 && player.getY() == 14) {
                gameManager.switchMap("Tầng 2 - Rừng rậm");
            } else if (currentMapId.equals("Tầng 2 - Rừng rậm") && player.getX() == 14 && player.getY() == 14) {
                gameManager.switchMap("Tầng 3 - Hang động tối");
            } else if (currentMapId.equals("Tầng 3 - Hang động tối") && player.getX() == 14 && player.getY() == 14) {
                gameManager.switchMap("Tầng cuối - Hang rồng");
            }
            // Ô (1,1) ở Tầng 2, Tầng 3, Tầng cuối: Quay lại
            else if (player.getX() == 1 && player.getY() == 1) {
                gameManager.goBackToPreviousMap();
            }
            // Ô (1,1) ở Tầng cuối: Kết thúc game
            if (currentMapId.equals("Tầng cuối - Hang rồng") && player.getX() == 1 && player.getY() == 1) {
                gameManager.endGame();
                glfwSetWindowShouldClose(window, true);
            }
        }
    }

    private void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }
}