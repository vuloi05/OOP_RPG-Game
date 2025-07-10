package letters;

import entity.Player;
import main.objectforgem;
import java.awt.*;

public class Letter extends objectforgem {
    Player player;

    public Letter(int x, int y, Player player) {
        super(x, y, "/object/letter/000.png", 16, 16);
        this.player = player;
    }

    @Override
    public void interact() {}

    @Override
    public void draw(Graphics2D g2) {

        int screenX = position.x - player.worldX + player.screenX;
        int screenY = position.y - player.worldY + player.screenY;

        g2.drawImage(image, screenX, screenY, (int)(image.getWidth() * 3 ), (int)(image.getHeight() * 3), null);

    }
}