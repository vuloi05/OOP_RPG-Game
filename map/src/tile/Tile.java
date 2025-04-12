// Đây là file Tile.java
package map.src.tile;

import characters.entity.Player;

import java.awt.image.BufferedImage;

public class Tile {
    public BufferedImage image;
    public String type;
    public boolean isPassable;
    public Effect effect;

    public void interact(Player player) {
        // Xử lý hiệu ứng khi player tương tác với tile
        if (effect != null) {
            effect.apply(player);
        }
    }
}

class Effect {
    public void apply(Player player) {
        // Logic hiệu ứng
    }
}
