// Đây là file CollisionChecker.java
package characters.util;

import characters.entity.Entity;
import main.GamePanel;
import map.src.tile.Tile;

public class CollisionChecker {

    GamePanel gp;

    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        Tile tile1, tile2;

        switch (entity.direction) {
            case "up" -> {
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tile1 = gp.currentMap.getTile(entityLeftCol, entityTopRow);
                tile2 = gp.currentMap.getTile(entityRightCol, entityTopRow);
                if (!tile1.isPassable || !tile2.isPassable) {
                    entity.collisionOn = true;
                }
            }
            case "down" -> {
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                tile1 = gp.currentMap.getTile(entityLeftCol, entityBottomRow);
                tile2 = gp.currentMap.getTile(entityRightCol, entityBottomRow);
                if (!tile1.isPassable || !tile2.isPassable) {
                    entity.collisionOn = true;
                }
            }
            case "left" -> {
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tileSize;
                tile1 = gp.currentMap.getTile(entityLeftCol, entityTopRow);
                tile2 = gp.currentMap.getTile(entityLeftCol, entityBottomRow);
                if (!tile1.isPassable || !tile2.isPassable) {
                    entity.collisionOn = true;
                }
            }
            case "right" -> {
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tileSize;
                tile1 = gp.currentMap.getTile(entityRightCol, entityTopRow);
                tile2 = gp.currentMap.getTile(entityRightCol, entityBottomRow);
                if (!tile1.isPassable || !tile2.isPassable) {
                    entity.collisionOn = true;
                }
            }
        }
    }
}