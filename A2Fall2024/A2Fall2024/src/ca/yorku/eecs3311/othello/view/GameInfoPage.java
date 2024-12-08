package ca.yorku.eecs3311.othello.view;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
/*********************View Part**************************************************
 * View-----------------indexPage		: buttons choosing different opponents	*
 * 		L gamePage------GameInfoPage	: shows game infos						*
 * 					L	GameBoardPage	: buttons of making moves				*
 * 					L	GameButtons		: save/load/restart/undo/redo buttons	*
 * ******************************************************************************/
public class GameInfoPage {

	static final int VGAP = 10;
	GridPane gameinfo;
	Text P1name, P2name;
	Text P1count, P2count;
	Text report;
	Text reportMove;
	
	public GameInfoPage() {
		this.P1name = new Text("Federation");			this.P1name.setFill(Color.BLUE);
		this.P2name = new Text("Zeon");					this.P2name.setFill(Color.RED);
		this.P1count = new Text(": 2");					//default 2 pieces
		this.P2count = new Text(": 2");					//default 2 pieces
		this.report = new Text("Federation moves next");//default P1 moves next
		this.reportMove = new Text("Game Start!");		//default message when game start
		this.gameinfo = gameInfo();						//construct game info grid pane
	}
	
	// return a GridPane of info page 
	public GridPane getInfo() {
		return this.gameinfo;
	}
	
	// for model to update information
	public void updateInfo(int P1Token, int P2Token, String report, String reportMove) {
		this.P1count.setText(": " + P1Token);
		this.P2count.setText(": " + P2Token);
		this.report.setText(report);
		this.reportMove.setText(reportMove);		
	}
	//initialize GridPane gameInfo
	private GridPane gameInfo() {
		GridPane pane = new GridPane();
		GridPane playerPane = new GridPane();

		playerPane.add(this.P1name, 0, 0);
		playerPane.add(this.P2name, 0, 1);
		playerPane.add(this.P1count, 1, 0);
		playerPane.add(this.P2count, 1, 1);

		pane.add(playerPane, 0, 0);
		pane.add(this.report, 0, 1);
		pane.add(this.reportMove, 0, 2);
		pane.setVgap(VGAP);
		return pane;
	}
	
}
