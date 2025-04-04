package com.dungeondelicacy.rpg.map;

public class Map {
    private String mapId; // ten map
    private int[][] mapData; // ma tran ban do
    private Tile[][] tiles; // ma tran cac o

    public Map(String mapId, int[][] mapData){
        this.mapId = mapId;
        this.mapData = mapData;
        this.tiles = new Tile[mapData.length][mapData[0].length];
        initializeTiles();
    }

    private void initializeTiles(){
        for( int i=0; i < mapData.length; i++){
            for( int j=0; j < mapData[0].length; j++){
                if(mapData[i][j] == 0) {
                    tiles[i][j] = new Tile("Ground", true); //đất, có thể đi qua
                }else if (mapData [i][j] == 1){
                    tiles[i][j] = new Tile("Wall", false); // tường, không thể đi qua
                }else if (mapData[i][j] == 2){
                    tiles[i][j] = new Tile("Transition", true); // chỗ chuyển map
                }
            }
        }
    }

    public String getMapId() { return mapId; }
    public Tile getTile(int x, int y){ return  tiles[x][y];}
    public boolean canMove(int x, int y){
        if(x<0 || x>=tiles.length || y<0 || y>=tiles[0].length){
            return false;
        }
        return tiles[x][y].isPassable();
    }
}
