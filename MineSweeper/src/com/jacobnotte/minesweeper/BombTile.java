package com.jacobnotte.minesweeper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class BombTile extends Tile{

	BombTile(int x, int y, GridPane gp, HUD hud, Spawner spawner) {
		super(x, y, gp, hud, spawner);
		
		setOnMouseClicked(e -> {
			if(!Game.isGameOver) {
				
				if(e.getButton() == MouseButton.PRIMARY) {
					Game.isGameOver = true;
					if(this.isTileClickable) {
						this.reveal();
					}
					
				}else if(e.getButton() == MouseButton.SECONDARY) {
					if(this.isTileClickable && Game.flagsRemaining > 0) {
						ImageView imgFlag = new ImageView(new Image("file:res/flag.png"));
						imgFlag.setFitHeight(size);
						imgFlag.setFitWidth(size);
						this.setGraphic(imgFlag);
						this.isTileClickable = false;
						this.isTileFlagged = true;
						setFlagCountAround(this, 1);
						
						Game.flagsRemaining -= 1;
						Game.bombsNeedingFlags -=1;
						hud.setBombCount(Game.flagsRemaining);
						
						//Win
						if(Game.bombsNeedingFlags == 0) {
							win();
						}
					}else if(this.isTileFlagged) {
						this.isTileClickable = true;
						this.isTileFlagged = false;
						this.setGraphic(imgCover);
						setFlagCountAround(this, -1);
						
						Game.flagsRemaining += 1;
						Game.bombsNeedingFlags += 1;
						hud.setBombCount(Game.flagsRemaining);
					}
					
				}
			}
		});
	}
	
}
