package com.jacobnotte.minesweeper;

import java.util.Timer;
import java.util.TimerTask;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class HUD {
	
	public static double gridSize;
	
	public Button btnSmile;
	public HBox mines, smileBox, time;
	
	private NumberImage m1, m2, m3;
	private NumberImage t1, t2, t3;
	private Timer timer;
	private TimerTask countSeconds;
	private int difficulty;
	private final int smileSize = 30;
	
	public HUD(Game game) {
		this.difficulty = Game.difficulty;
		
		this.btnSmile = new Button();
		m1 = new NumberImage();
		m2 = new NumberImage();
		m3 = new NumberImage();
		t1 = new NumberImage();
		t2 = new NumberImage();
		t3 = new NumberImage();
		
		setTime(0);
		
		if(difficulty == 1) {
			setBombCount(10);
		}else if(difficulty == 2) {
			setBombCount(40);
		}else if(difficulty == 3) {
			setBombCount(99);
		}
	}
	
	public GridPane createTileBox() {
		GridPane gp = new GridPane();
		//Styling code for tile gridpane
		
		return gp;
	}
	
	public HBox createTopBar() {
		double tSize = Tile.size;
		
		//Create header root container
		HBox hb = new HBox();
		hb.setMinWidth(gridSize);
		
		//create mine count box
		mines = new HBox();
		mines.setMinWidth(tSize * 3);
		mines.setMaxWidth(tSize * 3);
		mines.getChildren().addAll(m1, m2, m3);
		mines.setAlignment(Pos.CENTER_LEFT);
		
		//Create smile button
		smileBox = new HBox();
		smileBox.setMinWidth(tSize * 2);
		smileBox.setMaxWidth(tSize * 10);
		smileBox.getChildren().add(btnSmile);
		setSmile(0);
		HBox.setHgrow(smileBox, Priority.ALWAYS);
		smileBox.setAlignment(Pos.CENTER);
		
		//Create time box
		time = new HBox();
		time.setMinWidth(tSize * 3);
		time.setMaxWidth(tSize * 3);
		time.getChildren().addAll(t1, t2, t3);
		time.setAlignment(Pos.CENTER_RIGHT);
		
		hb.getChildren().addAll(mines, smileBox, time);
		return hb;
	}
	
	public VBox createBottomBar(ComboBox<String> cb, Stage stage, Scene scene) {
		double hsTrans;
		if(Game.difficulty == 1) {
			cb.setMaxWidth(75);
			hsTrans = 25;
		}else {
			cb.setMaxWidth(100);
			hsTrans = 40;
		}
		String diffString = difficultyToString();
		VBox bottomRoot = new VBox();
		HBox labels = new HBox();
		HBox controls = new HBox();
		
		Label diff = new Label("Difficulty: ");
		Label hsLabel = new Label("High Scores: ");
		Button hsButton = new Button(diffString + " scores");
		
		diff.setAlignment(Pos.CENTER_LEFT);
		hsLabel.setAlignment(Pos.CENTER_RIGHT);
		hsLabel.setTranslateX(hsTrans);
		hsButton.setAlignment(Pos.CENTER_RIGHT);
		
		labels.getChildren().addAll(diff, hsLabel);
		controls.getChildren().addAll(cb, hsButton);
		bottomRoot.getChildren().addAll(labels, controls);
		
		hsButton.setOnAction(e -> { // MAKE A CREATEHIGHSCORESCENE METHOD
			VBox root = new VBox();
			VBox vb = new VBox();
			VBox.setMargin(vb, new Insets(20));
			HBox head = new HBox();
			head.setPadding(new Insets(0, 10, 0, 10));
			
			Button back = new Button("Back");
			
			Label title = new Label(" = High Scores For " + diffString + " Difficulty =");
			title.setFont(new Font("Arial", 20));
			
			head.getChildren().addAll(back, title);
			
			back.setOnAction(e2 -> {
				stage.setScene(scene);
				stage.setMinWidth(stage.getWidth());
				stage.setMaxWidth(stage.getWidth());
				stage.setMinHeight(stage.getHeight());
				stage.setMaxHeight(stage.getHeight());
			});
			
			vb.getChildren().add(head);
			ScoreHandler.showScores(vb);
			
			vb.setMinHeight((75 * ScoreHandler.entrees) + 50);
			vb.setMinWidth(200);
			
			root.getChildren().add(vb);
			
			Scene highScoreScene = new Scene(root);
			stage.setScene(highScoreScene);
			stage.setMinWidth(stage.getWidth());
			stage.setMaxWidth(stage.getWidth());
			stage.setMinHeight(stage.getHeight());
			stage.setMaxHeight(stage.getHeight());
		});
		return bottomRoot;
	}
	
	public String difficultyToString() {
		String diffString = "";
		if(difficulty == 1)
			diffString = "Easy";
		if(difficulty == 2)
			diffString = "Medium";
		if(difficulty == 3)
			diffString = "Hard";
		
		return diffString;
	}
	
	public void setSmile(int state) {
		ImageView v;
		switch(state) {
		case 0:
			v = new ImageView(new Image("file:res/face-smile.png"));
			v.setFitWidth(smileSize);
			v.setFitHeight(smileSize);
			btnSmile.setGraphic(v);
			break;
		case 1:
			v = new ImageView(new Image("file:res/face-win.png"));
			v.setFitWidth(smileSize);
			v.setFitHeight(smileSize);
			btnSmile.setGraphic(v);
			break;
		case 2:
			v = new ImageView(new Image("file:res/face-dead.png"));
			v.setFitWidth(smileSize);
			v.setFitHeight(smileSize);
			btnSmile.setGraphic(v);
			break;
		case 3:
			v = new ImageView(new Image("file:res/face-O.png"));
			v.setFitWidth(smileSize);
			v.setFitHeight(smileSize);
			btnSmile.setGraphic(v);
			break;
		}
		btnSmile.setMinWidth(smileSize);
		btnSmile.setMaxWidth(smileSize);
		btnSmile.setMinHeight(smileSize);
		btnSmile.setMaxHeight(smileSize);
	}
	
	public void setTime(int time) {
		t1.setImageNumber(time / 100 % 10);
		t2.setImageNumber(time / 10 % 10);
		t3.setImageNumber(time % 10);
	}
	
	public void setBombCount(int bombsLeft) {
		m1.setImageNumber(bombsLeft / 100 % 10);
		m2.setImageNumber(bombsLeft / 10 % 10);
		m3.setImageNumber(bombsLeft % 10);
	}
	
	public void startTimer() {
		timer = new Timer();
		countSeconds = new TimerTask() {
			@Override
			public void run() {
				Game.seconds++;
				setTime(Game.seconds);
			}
		};
		timer.scheduleAtFixedRate(countSeconds, 1000, 1000);
	}
	
	public void stopTimer() {
		if(timer != null)
			timer.cancel();
	}
	
}

//NUMBERIMAGE CLASS
class NumberImage extends ImageView{
	private final double sizeX = 15;
	private final double sizeY = 25;
	
	public void setImageNumber(int num) {
		switch(num) {
		case 0:
			this.setImage(new Image("file:res/digits/0.png"));
			break;
		case 1:
			this.setImage(new Image("file:res/digits/1.png"));
			break;
		case 2:
			this.setImage(new Image("file:res/digits/2.png"));
			break;
		case 3:
			this.setImage(new Image("file:res/digits/3.png"));
			break;
		case 4:
			this.setImage(new Image("file:res/digits/4.png"));
			break;
		case 5:
			this.setImage(new Image("file:res/digits/5.png"));
			break;
		case 6:
			this.setImage(new Image("file:res/digits/6.png"));
			break;
		case 7:
			this.setImage(new Image("file:res/digits/7.png"));
			break;
		case 8:
			this.setImage(new Image("file:res/digits/8.png"));
			break;
		case 9:
			this.setImage(new Image("file:res/digits/9.png"));
			break;
		}
		this.setFitHeight(sizeY);
		this.setFitWidth(sizeX);
	}
}
