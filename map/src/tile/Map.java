// Đây là file TileManager.java
package map.src.tile;

import main.GamePanel;
import characters.entity.Player;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Map {
    private final GamePanel gp;
    public int[][] mapData;
    public List<Tile> tiles = new ArrayList<>();
    public String mapId;
    public List<Monster> monsters = new ArrayList<>();
    public List<NPC> npcs = new ArrayList<>();
    private Tile defaultTile;

    public Map(GamePanel gp, String mapId) {
        this.gp = gp;
        this.mapId = mapId;
        initializeTiles();
        loadMap("/map/resources/maps/" + mapId + ".txt");
        loadEntities();
    }

    private void initializeTiles() {
        try {
            // Khởi tạo các loại tile
            Tile grass = createTile("/map/resources/test/grass.png", "grass", true);
            Tile wall = createTile("/map/resources/test/wall.png", "wall", false);
//            Tile water = createTile("/map/resources/tiles/water.png", "water", false);
//            Tile earth = createTile("/map/resources/tiles/earth.png", "earth", true);
//            Tile tree = createTile("/map/resources/tiles/tree.png", "tree", false);
//            Tile floor = createTile("/map/resources/tiles/floor.png", "floor", true);

            tiles.add(grass);    // index 0
            tiles.add(wall);     // index 1
//            tiles.add(water);    // index 2
//            tiles.add(earth);    // index 3
//            tiles.add(tree);     // index 4
//            tiles.add(floor);    // index 5

            defaultTile = grass;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Tile createTile(String imagePath, String type, boolean passable) throws IOException {
        Tile tile = new Tile();
        tile.image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        tile.type = type;
        tile.isPassable = passable;
        return tile;
    }

    private void loadEntities() {
        // Tạm thời thêm quái vật và NPC mẫu
        if (mapId.equals("M0")) {
            // monsters.add(new Monster(gp, 100, 100));
            // npcs.add(new NPC(gp, 200, 200));
        }
    }

    public void loadMap(String filePath) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(getClass().getResourceAsStream(filePath))))) {

            mapData = new int[gp.maxWorldCol][gp.maxWorldRow];
            for (int row = 0; row < gp.maxWorldRow; row++) {
                String line = br.readLine();
                if(line == null) break;
                String[] numbers = line.split(" ");
                for (int col = 0; col < Math.min(numbers.length, gp.maxWorldCol); col++) {
                    mapData[col][row] = Integer.parseInt(numbers[col]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading map: " + filePath);
        }
    }

    public Tile getTile(int col, int row) {
        if (col >= 0 && col < gp.maxWorldCol &&
                row >= 0 && row < gp.maxWorldRow) {
            int tileIndex = mapData[col][row];
            if(tileIndex >= 0 && tileIndex < tiles.size()) {
                return tiles.get(tileIndex);
            }
        }
        return defaultTile;
    }

    public boolean canMove(int col, int row) {
        Tile tile = getTile(col, row);
        return tile.isPassable && tile.effect == null;
    }

    public void switchMap(String newMapId, Player player) {
        this.mapId = newMapId;
        loadMap("/map/resources/maps/" + newMapId + ".txt");
        loadEntities();

        // Reset player position khi chuyển map
        player.worldX = gp.tileSize * 10;
        player.worldY = gp.tileSize * 10;
    }

    public void draw(Graphics2D g2, Player player) {
        // Tính toán vùng nhìn thấy
        int left = player.worldX - player.screenX;
        int right = left + gp.screenWidth;
        int top = player.worldY - player.screenY;
        int bottom = top + gp.screenHeight;

        for (int col = 0; col < gp.maxWorldCol; col++) {
            for (int row = 0; row < gp.maxWorldRow; row++) {
                int worldX = col * gp.tileSize;
                int worldY = row * gp.tileSize;

                if (worldX + gp.tileSize > left &&
                        worldX - gp.tileSize < right &&
                        worldY + gp.tileSize > top &&
                        worldY - gp.tileSize < bottom) {

                    int screenX = worldX - player.worldX + player.screenX;
                    int screenY = worldY - player.worldY + player.screenY;

                    Tile tile = getTile(col, row);
                    if(tile.image != null) {
                        g2.drawImage(
                                tile.image,
                                screenX,
                                screenY,
                                gp.tileSize,
                                gp.tileSize,
                                null
                        );
                    }
                }
            }
        }
    }

    // Lớp tạm cho Monster và NPC
    static class Monster { /*...*/ }
    static class NPC { /*...*/ }
}