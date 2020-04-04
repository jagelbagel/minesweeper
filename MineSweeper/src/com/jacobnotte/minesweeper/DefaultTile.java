package com.jacobnotte.minesweeper;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class DefaultTile extends Tile{
	
	protected int bombCount;
	protected int flagCount;

	DefaultTile(int x, int y, Spawner spawner, GridPane gp, HUD hud) {
		super(x, y, gp, hud, spawner);
		
		setOnMouseClicked(e -> {
			if(!Game.isGameOver){
				if(e.getButton() == MouseButton.PRIMARY) {
					if(this.isTileClickable) {
						
						if(Game.firstTurn) {
							//First turn click
							spawner.boardSetup(x, y);
							hud.startTimer();
							Game.firstTurn = false;
						}
						
						this.reveal();
						if(bombCount == 0)
							spawner.uncoverTiles(this);
						
					}
				}else if(e.getButton() == MouseButton.SECONDARY) {
					if(this.isTileClickable && Game.flagsRemaining > 0 && !Game.firstTurn) {
						//Flag set
						ImageView imgFlag = new ImageView(new Image("file:res/flag.png"));
						imgFlag.setFitHeight(size);
						imgFlag.setFitWidth(size);
						this.setGraphic(imgFlag);
						this.isTileClickable = false;
						this.isTileFlagged = true;
						setFlagCountAround(this, 1);
						
						Game.flagsRemaining -= 1;
						hud.setBombCount(Game.flagsRemaining);
						
					}else if(!Game.firstTurn && this.isTileFlagged) {
						//Flag removed
						this.setGraphic(imgCover);
						this.isTileClickable = true;
						this.isTileFlagged = false;
						setFlagCountAround(this, -1);
						
						Game.flagsRemaining += 1;
						hud.setBombCount(Game.flagsRemaining);
						
					}
				}else if(e.getButton() == MouseButton.MIDDLE) {
					if(!Game.firstTurn) {
						if(this.bombCount == this.flagCount) {
							spawner.uncoverTiles(this);
						}
					}
				}
			}
		});
	}
	
	public int getBombCount() {
		return this.bombCount;
	}
	
	public int getFlagCount() {
		return this.flagCount;
	}
	
	public void setBombCount(int bombs) {
		this.bombCount = bombs;
		
		switch(bombs) {
		case 0:
			ImageView imgZero = new ImageView(new Image("file:res/0.png"));
			imgZero.setFitWidth(size);
			imgZero.setFitHeight(size);
			setUncoveredImage(imgZero);
			break;
		case 1:
			ImageView imgOne = new ImageView(new Image("file:res/1.png"));
			imgOne.setFitWidth(size);
			imgOne.setFitHeight(size);
			setUncoveredImage(imgOne);
			break;
		case 2:
			ImageView imgTwo = new ImageView(new Image("file:res/2.png"));
			imgTwo.setFitWidth(size);
			imgTwo.setFitHeight(size);
			setUncoveredImage(imgTwo);
			break;
		case 3:
			ImageView imgThree = new ImageView(new Image("file:res/3.png"));
			imgThree.setFitWidth(size);
			imgThree.setFitHeight(size);
			setUncoveredImage(imgThree);
			break;
		case 4:
			ImageView imgFour = new ImageView(new Image("file:res/4.png"));
			imgFour.setFitWidth(size);
			imgFour.setFitHeight(size);
			setUncoveredImage(imgFour);
			break;
		case 5:
			ImageView imgFive = new ImageView(new Image("file:res/5.png"));
			imgFive.setFitWidth(size);
			imgFive.setFitHeight(size);
			setUncoveredImage(imgFive);
			break;
		case 6:
			ImageView imgSix = new ImageView(new Image("file:res/6.png"));
			imgSix.setFitWidth(size);
			imgSix.setFitHeight(size);
			setUncoveredImage(imgSix);
			break;
		case 7:
			ImageView imgSeven = new ImageView(new Image("file:res/7.png"));
			imgSeven.setFitWidth(size);
			imgSeven.setFitHeight(size);
			setUncoveredImage(imgSeven);
			break;
		case 8:
			ImageView imgEight = new ImageView(new Image("file:res/8.png"));
			imgEight.setFitWidth(size);
			imgEight.setFitHeight(size);
			setUncoveredImage(imgEight);
			break;
		}
	}
}
