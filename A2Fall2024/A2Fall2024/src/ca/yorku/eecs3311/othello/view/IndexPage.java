package ca.yorku.eecs3311.othello.view;

import ca.yorku.eecs3311.othello.model.Othello;
import ca.yorku.eecs3311.util.Observable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class IndexPage extends Observable{

	Scene page;
	String action;
	
	public IndexPage() {
		this.page = new Scene(IndexVBox(), Othello.DIMENSION *  50 + 150, Othello.DIMENSION * 50);
		this.action = "";//command for sending to observer
	}
	
	public Scene getPage() {
		return this.page;
	}

	//combine panes together and set a picture background
	public VBox IndexVBox() {
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		
		//combine panes together
		vbox.getChildren().addAll(TitlePane(),ButtonPane());
		
		//set picture background
		Image bgImg = new Image("/Galaxy.jpg");
		BackgroundImage bgImgObj = new BackgroundImage(
				bgImg,
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, true)
				);
		Background bg = new Background(bgImgObj);
		
		vbox.setBackground(bg);
		
		return vbox;
	}
	
	public GridPane TitlePane() {
		GridPane pane = new GridPane();
		//set text title
		Text titleText = new Text("Othello");
		titleText.setFont(Font.font("Script MT Bold", FontWeight.BOLD, 100));
		titleText.setFill(Color.WHITE);
		
		//set subtitle
		GridPane subtitle = subtitlePane();		
		
		//set title above subtitle
		GridPane.setConstraints(titleText, 0, 0);
		GridPane.setConstraints(subtitle, 0, 1);

		pane.setAlignment(Pos.CENTER);
		pane.getChildren().addAll(titleText, subtitle);
		
		return pane;
	}
	public GridPane subtitlePane() {
		//set subtitle horizontally and in different colour
		GridPane pane = new GridPane();
		Text fed = new Text("Federation");		fed.setFont(Font.font("Impact", 24));
		fed.setFill(Color.BLUE);
		Text vs = new Text(" vs ");				vs.setFont(Font.font("Impact", 24));
		vs.setFill(Color.YELLOW);
		Text zeon = new Text("Zeon");			zeon.setFont(Font.font("Impact", 24));
		zeon.setFill(Color.RED);
		
		pane.setAlignment(Pos.CENTER);		
		pane.getChildren().addAll(fed, vs, zeon);
		pane.setVgap(10); pane.setHgap(10);

		GridPane.setConstraints(fed, 0, 0);
		GridPane.setConstraints(vs, 1, 0);
		GridPane.setConstraints(zeon, 2, 0);

		return pane;
	} 
	
	
	public GridPane ButtonPane() {
		//return a pane with buttons horizontally
		GridPane pane = new GridPane();		
		
		Button vsPlayer = createButton("Player vs Player");
		Button vsGreed = createButton("Player vs Greedy");
		Button vsRandom = createButton("Player vs Random");

		GridPane.setConstraints(vsPlayer, 0, 0);
		GridPane.setConstraints(vsGreed, 1, 0);
		GridPane.setConstraints(vsRandom, 2, 0);
		
		pane.setAlignment(Pos.CENTER);		
		pane.setVgap(10); pane.setHgap(10);
		pane.getChildren().addAll(vsPlayer, vsGreed, vsRandom);
		
		return pane;
	}
	
	public Button createButton(String text) {
		Button btn = new Button();
		btn.setText(text);
		btn.setOnAction(event->{
			this.action = btn.getText();//let observer know which action to take
			this.notifyObservers();
		});
		return btn;
	}
	public String getAction() {
		return this.action;
	}
}
