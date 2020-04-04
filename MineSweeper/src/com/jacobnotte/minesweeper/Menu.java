package com.jacobnotte.minesweeper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

public class Menu{
	private final ObservableList<String> options = FXCollections.observableArrayList(
	        "Easy",
	        "Medium",
	        "Hard"
	    );
	private final ComboBox<String> cb = new ComboBox<String>();
	
	public Menu(Game game) {
		cb.setItems(options);
		cb.getSelectionModel().select(Game.difficulty - 1);
		
		cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue ov, Number val, Number newVal) {
				Game.difficulty = newVal.intValue() + 1;
				game.restart(game.stg, game.hud);
			}
			
		});
	}
	
	public ComboBox<String> getComboBox(){
		return cb;
	}
}
