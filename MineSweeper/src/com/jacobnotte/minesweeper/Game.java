package com.jacobnotte.minesweeper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Game extends Application{
	public static boolean firstTurn = true;
	public static int defaultTilesLeft = 0;
	public static int flagsRemaining;
	public static int bombsNeedingFlags;
	public static boolean isGameOver = false;
	public static int seconds;
	public static int difficulty = 2;
	
	public HUD hud;
	public Stage stg;
	
	private Menu menu;
	//Everything done in constructor do not remove!!
	private ScoreHandler sh;
	private Spawner spawner;
	
	private BorderPane root;
	private GridPane gp;
	private HBox topBar;
	private VBox bottomBar;
	private ComboBox<String> cb;
	private Scene scene;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage startStage) throws Exception {
		stg = startStage;
		startStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		        Platform.exit();
		        System.exit(0);
		    }
		});
		startNewGame(startStage);
	}
	
	private void cleanup(HUD hud) {
		HUD.gridSize = 0;
		firstTurn = true;
		defaultTilesLeft = 0;
		isGameOver = false;
		seconds = 0;
		hud.stopTimer();
		ScoreHandler.highScoreArray.clear();
		ScoreHandler.highScoreNames.clear();
	}
	
	public void restart(Stage stage, HUD hud) {
		cleanup(hud);
		startNewGame(stage);
	}
	
	public void startNewGame(Stage stage){
		//Initialization
		hud = new HUD(this);
		menu = new Menu(this);
		sh = new ScoreHandler();
		
		root = new BorderPane();
		scene = new Scene(root);
		
		gp = hud.createTileBox();
		spawner = new Spawner(gp, hud);
		
		topBar = hud.createTopBar();
		hud.btnSmile.setOnMouseClicked(e -> {
			restart(stage, hud);
		});
		cb = menu.getComboBox();
		bottomBar = hud.createBottomBar(cb, stage, scene);
		
		root.setTop(topBar);
		root.setCenter(gp);
		root.setBottom(bottomBar);
		
		//Styling
		root.setStyle("-fx-background-color: #bfbfbf;-fx-border-color: #fafafa #787878 #787878 #fafafa; -fx-border-width: 3; -fx-border-radius: 0.001;");
        topBar.setStyle("-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width: 3; -fx-border-radius: 0.001;");
        //hud.time.setStyle("-fx-border-color:  #787878 #fafafa #fafafa #787878; -fx-border-width: 2; -fx-border-radius: 0.001;");
		gp.setStyle("-fx-border-color:  #787878 #fafafa #fafafa #787878; -fx-border-width: 3; -fx-border-radius: 0.001;");
		
		stage.getIcons().add(new Image("file:res/mine-grey.png"));
		stage.setTitle("Jagel's MineSweeper");
		stage.setScene(scene);
		stage.show();
		stage.setMinWidth(stage.getWidth());
		stage.setMaxWidth(stage.getWidth());
		stage.setMinHeight(stage.getHeight());
		stage.setMaxHeight(stage.getHeight());
	}

}
