package com.jacobnotte.minesweeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ScoreHandler {
	public static ArrayList<Integer> highScoreArray = new ArrayList<Integer>();
	public static ArrayList<String> highScoreNames = new ArrayList<String>();
	public static int entrees;
	
	private static int score = 0;
	private static int difficulty;
	private static ArrayList<String> nom = new ArrayList<String>(Arrays.asList("na", "na", "na", "na", "na"));
	private static ArrayList<Integer> num = new ArrayList<Integer>(Arrays.asList(999, 999, 999, 999, 999));
	
	public ScoreHandler() {
		score = 0;
		difficulty = Game.difficulty;
		getHighScores();
		System.out.println(highScoreArray);
		System.out.println(highScoreNames);
	}
	
	public static void setScore() {
		score = Game.seconds;
		
		for(int pos = 0; pos < 5; pos++) {
			if(score < highScoreArray.get(pos)) {
				TextInputDialog td = new TextInputDialog("Enter name");
				td.setTitle("=HIGH SCORE=");
				td.setHeaderText("Congrats you set a high score! You are in position " + (pos + 1));
				td.setContentText("Please enter your name:");
				td.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);
				td.showAndWait();
				String name = td.getResult();
				
				if(name != null) {
					highScoreNames.add(pos, name);
					highScoreArray.add(pos, score);
				}
				pos = 5;
			}
		}
		//Write file
		System.out.println(highScoreArray);
		System.out.println(highScoreNames);
		writeFile(highScoreNames, highScoreArray);
	}
	
	//when user views high score menu
	public static void showScores(VBox vb) {
		entrees = 0;
		for(int i = 0; i < 5; i++) {
			if(highScoreArray.get(i) != 999) {
				entrees += 1;
				HBox hb = new HBox();
				
				String nm = highScoreNames.get(i);
				Label nameLBL = new Label(nm);
				String sc = highScoreArray.get(i).toString();
				Label scoreLBL = new Label(sc);
				
				hb.getChildren().addAll(nameLBL, scoreLBL);
				vb.getChildren().add(hb);
			}
		}
		if(entrees == 0) {
			entrees += 1;
			Label noEntry = new Label("No high score has been set!");
			vb.getChildren().add(noEntry);
		}
	}

	private static void getHighScores() {
		//Grab high scores
		try {
			readDataFile();
		}catch(FileNotFoundException e) {
			//If no high scores are found, create
			File res = new File(System.getProperty("user.dir") + "/res");
			if(!res.exists()) {
				res.mkdir();
			}
			File hs = null;
			if(difficulty == 1)
				hs = new File(res.getPath() + "/easy.dat");
			if(difficulty == 2)
				hs = new File(res.getPath() + "/medium.dat");
			if(difficulty == 3)
				hs = new File(res.getPath() + "/hard.dat");
			try {
				hs.createNewFile();
				//Writing to new created file to give our read function something to grab
				writeFile(nom, num);
				readDataFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private static void writeFile(ArrayList<String> names, ArrayList<Integer> scores) {
		try {
			File f = null;
			if(difficulty == 1) {
				f = new File("res/easy.dat");
			}else if(difficulty == 2) {
				f = new File("res/medium.dat");
			}else if(difficulty == 3) {
				f = new File("res/hard.dat");
			}
			
			PrintWriter output = new PrintWriter(new FileOutputStream(f, false));
			for(int i = 0; i < 5; i++) {
				output.print(scores.get(i));
				output.print(" " + names.get(i) + "\n");
			}
			output.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void readDataFile() throws FileNotFoundException{
		String filename = "";
		if(difficulty == 1) {
			filename = "res/easy.dat";
		}else if(difficulty == 2) {
			filename = "res/medium.dat";
		}else if(difficulty == 3) {
			filename = "res/hard.dat";
		}
		
		File file = new File(filename);
		if(!file.exists())
			throw new FileNotFoundException("404");
		Scanner input = new Scanner(file);
		int linecount = 0;
		while(input.hasNext()) {
			if(input.hasNextLine()) {
				linecount += 1;
			}
			
			int hs = 0;
			try {
				hs = input.nextInt();
			}catch(InputMismatchException e) {
				writeFile(nom, num);
				highScoreArray.clear();
				highScoreNames.clear();
				readDataFile();
				break;
			}
			String name = input.next();
			highScoreArray.add(hs);
			highScoreNames.add(name);
		}
		if(linecount < 5) {
			for(int i = 5 - linecount; i > 0; i --) {
				highScoreArray.add(0);
				highScoreNames.add("na");
			}
		}
		input.close();
	}
}
