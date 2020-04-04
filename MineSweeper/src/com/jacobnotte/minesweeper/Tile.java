package com.jacobnotte.minesweeper;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public abstract class Tile extends Button{
	
	public static double size = 18;
	
	public boolean isTileCovered;
	public boolean isTileClickable;
	public boolean isTileFlagged;
	
	protected ImageView uncoveredImage, imgCover;
	private Spawner spawner;
	private HUD hud;
	private final int posX;
	private final int posY;
		
	Tile(int x, int y, GridPane gp, HUD hud, Spawner spawner){
		this.isTileCovered = true;
		this.isTileClickable = true;
		this.isTileFlagged = false;
		this.posX = x;
		this.posY = y;
		this.spawner = spawner;
		this.hud = hud;
		
		setOnMousePressed(e2 -> {
			hud.setSmile(3);
		});
		setOnMouseReleased(e2 -> {
			hud.setSmile(0);
		});
		
		setMinWidth(size);
		setMaxWidth(size);
		setMinHeight(size);
		setMaxHeight(size);
		
		imgCover = new ImageView(new Image("file:res/cover.png"));
		imgCover.setFitHeight(size);
		imgCover.setFitWidth(size);
		this.setGraphic(imgCover);
		
		gp.add(this, posX, posY);
	}
	
	public int getX() {
		return this.posX;
	}
	public int getY() {
		return this.posY;
	}
	
	public void reveal() {
		//End game if bomb is revealed
		if(this instanceof BombTile) {
			hud.setSmile(2);
			ImageView redBomb = new ImageView(new Image("file:res/mine-red.png"));
			redBomb.setFitWidth(size);
			redBomb.setFitHeight(size);
			this.setUncoveredImage(redBomb);
			
			spawner.revealBombs(this.posX, this.posY);
			Game.isGameOver = true;
			spawner.markWrongFlags();
			hud.stopTimer();
		}
		
		if(this instanceof DefaultTile) {
			Game.defaultTilesLeft -= 1;
			
			//Win
			if(Game.defaultTilesLeft == 0) {
				win();
			}
		}
		
		this.setGraphic(this.uncoveredImage);
		this.setFocusTraversable(false);
		this.isTileClickable = false;
		this.isTileCovered = false;
	}
	
	protected void win() {
		hud.setSmile(1);
		Game.isGameOver = true;
		hud.stopTimer();
		ScoreHandler.setScore();
	}
	
	public void setUncoveredImage(ImageView img) {
		this.uncoveredImage = img;
	}
	
	public void setFlagCountAround(Tile tile, int n) {
		int x = tile.getX();
		int y = tile.getY();
		
		for(int xPos = x - 1; xPos <= x + 1; xPos++) {
			for(int yPos = y - 1; yPos <= y + 1; yPos++) {
				
				if(yPos != y || xPos != x) {
					if((xPos < spawner.getBoardX() && xPos >= 0) && (yPos < spawner.getBoardY() && yPos >= 0)) {
						
						if(spawner.tiles[xPos][yPos] instanceof DefaultTile) {
							DefaultTile tempTile = (DefaultTile)spawner.tiles[xPos][yPos];
							tempTile.flagCount += n;
							spawner.tiles[xPos][yPos] = tempTile;
						}
					}
				}
			}
		}
	}
}
