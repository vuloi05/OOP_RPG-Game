package tile;

import java.awt.Graphics2D;
import java.io.*;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTools;

public class TileManager {

	GamePanel gp;
	public Tile[] tile;
	public int[][] mapTileNum;

	public TileManager(GamePanel gp) {
		this.gp = gp;

		tile = new Tile[150];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];

		getTileImage();
		loadMap("/map/worldmap.txt");
	}

	public void getTileImage() {
		for(int index = 0; index < 122; index++) {
			tileSetup(index, index == 0 || (8 <= index && index <= 36) || (40 <= index && index <= 42) || (49 <= index && index <= 55) || index == 57 || (59 <= index && index <= 66));
		}
	}
	public void tileSetup(int tileOrder, boolean collision){
		UtilityTools util = new UtilityTools();
		try{
			tile[tileOrder] = new Tile();
			if(tileOrder <10) {tile[tileOrder].image = ImageIO.read(getClass().getResourceAsStream("/tiles/00"+tileOrder+".png"));}
				else if (tileOrder <= 99){tile[tileOrder].image = ImageIO.read(getClass().getResourceAsStream("/tiles/0"+tileOrder+".png"));}
					else if (tileOrder <= 109){tile[tileOrder].image = ImageIO.read(getClass().getResourceAsStream("/tiles/10"+(tileOrder-100)+".png"));}
						else {tile[tileOrder].image = ImageIO.read(getClass().getResourceAsStream("/tiles/1"+(tileOrder-100)+".png"));}
			tile[tileOrder].image = util.scaleImage(tile[tileOrder].image, gp.tileSize, gp.tileSize);
			tile[tileOrder].collision = collision;

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void loadMap(String filePath) {

		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			int col = 0;
			int row = 0;

			while (col < gp.maxWorldCol && row < gp.maxWorldRow) {

				String line = br.readLine();

				while (col < gp.maxWorldCol) {
					String[] numbers = line.split(" ");

					int num = Integer.parseInt(numbers[col]);

					mapTileNum[col][row] = num;
					col++;
				}
				if (col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();

		} catch (Exception e) {

		}

	}

	public void draw(Graphics2D g2) {

		int worldCol = 0;
		int worldRow = 0;

		while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {

			int tileNum = mapTileNum[worldCol][worldRow];

			int worldX = worldCol * gp.tileSize;
			int worldY = worldRow * gp.tileSize;
			int screenX = worldX - gp.player.worldX + gp.player.screenX;
			int screenY = worldY - gp.player.worldY + gp.player.screenY;

			g2.drawImage(tile[tileNum].image, screenX, screenY, null);
			worldCol++;

			if (worldCol == gp.maxWorldCol) {
				worldCol = 0;
				worldRow++;
			}

		}

	}

}
