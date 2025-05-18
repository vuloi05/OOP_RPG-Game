import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import characters.Player;
import characters.Enemy;
import map.Map;
import map.Tile;
import systems.GameManager;
import java.util.ArrayList;
import java.util.List;
import items.Item.Item;
import items.Item.Equipment;
import items.Item.ConsumableItem;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class GameWindowSwing extends JPanel {
    private final int width = 800;
    private final int height = 600;
    private final int mapSize = 16;
    private final int tileSize = 35;
    private GameManager gameManager;

    public GameWindowSwing(GameManager gameManager) {
        this.gameManager = gameManager;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e.getKeyCode());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMap(g);
        drawEnemies(g);
        drawPlayer(g);
        drawInventory(g);
    }

    private void drawMap(Graphics g) {
        Map map = gameManager.getCurrentMap();
        if (map == null) return;

        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                Tile tile = map.getTile(i, j);
                switch (tile.getType()) {
                    case "Ground" -> g.setColor(Color.LIGHT_GRAY);
                    case "Wall" -> g.setColor(Color.DARK_GRAY);
                    case "Transition" -> g.setColor(Color.BLUE);
                    default -> g.setColor(Color.BLACK);
                }
                g.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                g.setColor(Color.BLACK);
                g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
            }
        }
    }

    private void drawEnemies(Graphics g) {
        Map map = gameManager.getCurrentMap();
        if (map == null) return;

        g.setColor(Color.GREEN);
        for (Enemy enemy : map.getEnemies()) {
            int x = enemy.getX() * tileSize + tileSize / 4;
            int y = enemy.getY() * tileSize + tileSize / 4;
            int radius = tileSize / 2;
            g.fillOval(x, y, radius, radius);
        }
    }

    private void drawPlayer(Graphics g) {
        Player player = gameManager.getPlayer();
        if (player == null) return;

        int x = player.getX() * tileSize + tileSize / 4;
        int y = player.getY() * tileSize + tileSize / 4;
        int radius = tileSize / 2;

        g.setColor(Color.RED);
        g.fillOval(x, y, radius, radius);
    }

    private void drawInventory(Graphics g) {
        Player player = gameManager.getPlayer();
        if (player == null) return;

        List<Item> inventory = player.getInventory();

        int slotSize = 50;
        int padding = 10;
        int cols = 4;
        int rows = 2;

        int startX = width - (slotSize * cols) - padding;
        int startY = height - (slotSize * rows) - padding;

        g.setColor(Color.WHITE);
        g.drawString("INVENTORY", startX, startY - 10);

        for (int i = 0; i < inventory.size(); i++) {
            int row = i / cols;
            int col = i % cols;

            int x = startX + col * slotSize;
            int y = startY + row * slotSize;

            Item item = inventory.get(i);

            // Vẽ khung
            g.setColor(Color.GRAY);
            g.fillRect(x, y, slotSize, slotSize);
            g.setColor(Color.BLACK);
            g.drawRect(x, y, slotSize, slotSize);

            // Vẽ tên vật phẩm
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            g.setColor(Color.WHITE);
            g.drawString(item.getName(), x + 3, y + 12);

            // Load và hiển thị ảnh từ imagePath
            try {
                Image img = ImageIO.read(getClass().getResourceAsStream(item.getImagePath()));
                img = img.getScaledInstance(slotSize - 10, slotSize - 20, Image.SCALE_SMOOTH);
                g.drawImage(img, x + 5, y + 15, null);
            } catch (IOException | IllegalArgumentException e) {
                g.setColor(Color.RED);
                g.drawString("No Img", x + 5, y + 35);
            }
        }
    }



    private void handleInput(int key) {
        Player player = gameManager.getPlayer();
        Map map = gameManager.getCurrentMap();
        int newX = player.getX();
        int newY = player.getY();

        switch (key) {
            case KeyEvent.VK_W -> newY--;
            case KeyEvent.VK_S -> newY++;
            case KeyEvent.VK_A -> newX--;
            case KeyEvent.VK_D -> newX++;
        }

        gameManager.movePlayer(newX, newY);

        // Check chuyển map
        Tile tile = map.getTile(player.getX(), player.getY());
        if (tile.getType().equals("Transition")) {
            String currentMapId = map.getMapId();
            int px = player.getX();
            int py = player.getY();

            if (currentMapId.equals("Tầng 1 - Khu chợ") && px == 14 && py == 14) {
                gameManager.switchMap("Tầng 2 - Rừng rậm");
            } else if (currentMapId.equals("Tầng 2 - Rừng rậm") && px == 14 && py == 14) {
                gameManager.switchMap("Tầng 3 - Hang động tối");
            } else if (currentMapId.equals("Tầng 3 - Hang động tối") && px == 14 && py == 14) {
                gameManager.switchMap("Tầng cuối - Hang rồng");
            } else if (px == 1 && py == 1) {
                if (currentMapId.equals("Tầng cuối - Hang rồng")) {
                    gameManager.endGame();
                    System.exit(0);
                } else {
                    gameManager.goBackToPreviousMap();
                }
            }
        }
    }

    public static void start(GameManager gameManager) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mỹ Vị Hầm Ngục - Swing Edition");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new GameWindowSwing(gameManager));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
