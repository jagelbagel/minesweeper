package com.jacobnotte.minesweeper;

import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class Spawner {
	public Tile[][] tiles;
	
	private HUD hud;
	private GridPane gp;
	private Random r;
	private int difficulty;
	private int startZoneMin, startZoneMax;
	private int safeZoneX, safeZoneY;
	private int boardX, boardY, totalBombs;
	
	public Spawner(GridPane gp, HUD hud) {
		this.gp = gp;
		this.hud = hud;
		this.difficulty = Game.difficulty;
		r = new Random();
		
		spawnTiles();
	}
	
	private void spawnTiles() {
		if(difficulty == 1) {
			boardX = 8;
			boardY = 8;
			totalBombs = 10;
			startZoneMin = 2; //Spaces on each side
			startZoneMax = 2; //Spaces on each side, could be larger depending on random bomb spawns
		}
		else if(difficulty == 2) {
			boardX = 16;
			boardY = 16;
			totalBombs = 40;
			startZoneMin = 2;
			startZoneMax = 3;
		}
		else if(difficulty == 3) {
			boardX = 16;
			boardY = 32;
			totalBombs = 99;
			startZoneMin = 3;
			startZoneMax = 4;
		}
		HUD.gridSize = boardX * Tile.size;
		Game.flagsRemaining = totalBombs;
		Game.bombsNeedingFlags = totalBombs;
		tiles = buildBoard(boardX, boardY);
	}
	
	//Used for initial tiles before first click
	private Tile[][] buildBoard(int xSize, int ySize){
		Tile[][] tileArr = new Tile[xSize][ySize];
		
		for(int x = 0; x < xSize; x++) {
			for(int y = 0; y < ySize; y++) {
				tileArr[x][y] = new DefaultTile(x, y, this, gp, hud);
			}
		}
		return tileArr;
	}
	
	public int getBoardX() {
		return this.boardX;
	}
	public int getBoardY() {
		return this.boardY;
	}
	
	//Called in DefaultTile
	public void boardSetup(int startX, int startY) {
		safeZoneX = startX;
		safeZoneY = startY;
		Tile[][] tileArr = tiles;
		
		//Spawn Bombs
		int safeZoneSize = r.nextInt((startZoneMax - startZoneMin) + 1) + startZoneMin;
		int bombLocs[][] = new int[boardX][boardY];
		for(int[] i1 : bombLocs) {
			for(int i : i1) {
				i = 0;
			}
		}
		
		for(int i = 0; i < totalBombs; i++) {
			int xPos = r.nextInt(boardX);
			int yPos = r.nextInt(boardY);
			//while not inside safezone or on another bomb
			while(((xPos < safeZoneX + safeZoneSize && xPos > safeZoneX - safeZoneSize) && (yPos < safeZoneY + safeZoneSize && yPos > safeZoneY - safeZoneSize)) || bombLocs[xPos][yPos] == 1) {
					xPos = r.nextInt(boardX);
					yPos = r.nextInt(boardY);
			}
			tileArr[xPos][yPos] = new BombTile(xPos, yPos, gp, hud, this);
			bombLocs[xPos][yPos] = 1;
		}
		
		//Set tile states
		for(int x = 0; x < boardX; x++) {
			for(int y = 0; y < boardY; y++) {
				
				if(tileArr[x][y] instanceof DefaultTile) {
					Game.defaultTilesLeft += 1;
					DefaultTile tempTile = (DefaultTile)tileArr[x][y];
					int bombCount = 0;
					int tempX = tileArr[x][y].getX();//Change to just x dont need this
					int tempY = tileArr[x][y].getY();
					
					//Check 3x3 around tile for bombs
					for(int x2 = tempX - 1; x2 <= tempX + 1; x2++) {
						for(int y2 = tempY -1; y2 <= tempY + 1; y2++) {
							
							if((x2 >= 0 && x2 < boardX) && (y2 >= 0 && y2 < boardY)) {
								if(tileArr[x2][y2] != tileArr[tempX][tempY]) {
									if(tileArr[x2][y2] instanceof BombTile)
										bombCount += 1;
								}	
							}
						}
					}
					tempTile.setBombCount(bombCount);
					tileArr[x][y] = tempTile;
				}
			}
		}
		tiles = tileArr;
	}
	
	public void uncoverTiles(DefaultTile t) {
		int tx = t.getX();
		int ty = t.getY();
		
		for(int x = tx - 1; x <= tx + 1; x++) {
			for(int y = ty - 1; y <= ty + 1; y++) {
				
				if((x >= 0 && x < boardX) && (y >= 0 && y < boardY)) {//inbounds
					Tile tempTile = tiles[x][y];
					
					if(tempTile.isTileCovered && tempTile.isTileClickable) {
						tempTile.reveal();
						
						if(tempTile instanceof DefaultTile) {
							DefaultTile temp = (DefaultTile)tempTile;
							if(temp.getBombCount() == 0)
								uncoverTiles((DefaultTile)tiles[x][y]);
						}
						tiles[x][y] = tempTile;
					}
				}
			}
		}
	}
	
	public void revealBombs(int x, int y) {
		int xPos = x;
		int yPos = y;
		for(Tile[] ta : tiles) {
			for(Tile t : ta) {
				//Make all tiles un-clickable
				t.setMouseTransparent(true);
				t.setFocusTraversable(false);
				//Show all bombs other than the one at x,y
				if(t instanceof BombTile) {
					if(t.getX() != xPos || t.getY() != yPos) {
						ImageView imgBomb = new ImageView(new Image("file:res/mine-grey.png"));
						imgBomb.setFitWidth(Tile.size);
						imgBomb.setFitHeight(Tile.size);
						t.setUncoveredImage(imgBomb);
						t.setGraphic(t.uncoveredImage);
					}
				}
			}
		}
	}
	
	public void markWrongFlags() {
		for(Tile[] ta : tiles) {
			for(Tile t : ta) {
				
				if(t.isTileFlagged) {
					if(t instanceof DefaultTile) {
						ImageView wrongFlag = new ImageView(new Image("file:res/mine-misflagged.png"));
						wrongFlag.setFitHeight(Tile.size);
						wrongFlag.setFitWidth(Tile.size);
						t.setGraphic(wrongFlag);
					}
				}
			}
		}
	}
}
