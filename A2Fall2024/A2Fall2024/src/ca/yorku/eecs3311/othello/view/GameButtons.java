package ca.yorku.eecs3311.othello.view;

import ca.yorku.eecs3311.util.Observable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class GameButtons extends Observable{
	
	GridPane gameButtons;
	static final int ButtonWidth = 130;
	static final int VGAP = 10;
	String action;						//for observer to know what to do
	
	public GameButtons() {
		this.gameButtons = buttonlist();
		this.action = "";
	}
	
	public GridPane getButtons() {
		return this.gameButtons;
	}
	//create a GridPane with buttons vertically
	public GridPane buttonlist() {
		GridPane pane = new GridPane();
		pane.add(createButton("saveGame")	, 0, 0);
		pane.add(createButton("loadGame")	, 0, 1);
		pane.add(createButton("restart")	, 0, 2);
		pane.add(createButton("undo")		, 0, 3);
		pane.add(createButton("redo")		, 0, 4);
		pane.setVgap(VGAP);
		return pane;
	}
	
	//set Text and events of buttons
	Button createButton(String text) {
		Button button = new Button();
		button.setText(text);
		button.setMinWidth(ButtonWidth);
		button.setOnAction(event->{
			this.action = button.getText();		//let observer know which action to take
			this.notifyObservers();
		});
		return button;
	}
	public String getAction() {
		return this.action;						//for observers to check which action this send
	}
}
