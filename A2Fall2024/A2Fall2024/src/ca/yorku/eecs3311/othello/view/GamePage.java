package ca.yorku.eecs3311.othello.view;

import ca.yorku.eecs3311.othello.model.Othello;
import ca.yorku.eecs3311.othello.model.OthelloBoard;
import ca.yorku.eecs3311.othello.viewcontroller.OthelloControllerGUI;
import ca.yorku.eecs3311.util.Observable;
import ca.yorku.eecs3311.util.Observer;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

public class GamePage implements Observer{
	public GameInfoPage info;
	public GameButtons buttons;
	public GameBoardPage board;
	Scene scene;
	
	static final int GAP = 10;
	
	public GamePage(){
		this.info = new GameInfoPage();
		this.buttons = new GameButtons();
		this.board = new GameBoardPage();
		this.scene = new Scene(GamePagePane(), Othello.DIMENSION * 50 + 150, Othello.DIMENSION * 50);
	}
	
	public Scene getScene() {
		return this.scene;
	}
	
	
	//Construct GridPane = info + buttons + board
	GridPane GamePagePane() {
		GridPane pane = new GridPane();
		pane.add(this.board.gameBoard, 0, 0);
		pane.add(SideBar(), 1, 0);
		pane.setHgap(GAP);
		return pane;
	}
	//Sidebar of scene, contrains info and buttons
	GridPane SideBar() {
		GridPane pane = new GridPane();
		pane.add(this.buttons.getButtons(), 0, 0);
		pane.add(this.info.getInfo(), 0, 1);
		pane.setVgap(GAP);
		return pane;
	}
	
	@Override
	public void update(Observable o) {
		//depends on action from CommandSender, do different actions by if else
		if (o instanceof OthelloControllerGUI) {
			OthelloControllerGUI ocg = (OthelloControllerGUI) o;
			String command = ocg.getAction();
			if(command == "report") {
				//update player's count, board, who moves next
				this.info.P1count.setText("" + ocg.getCount(OthelloBoard.P1));
				this.info.P2count.setText("" + ocg.getCount(OthelloBoard.P2));
				String currPlayer = ocg.getWhosTurn() == OthelloBoard.P1? "Federation"	:
									ocg.getWhosTurn() == OthelloBoard.P2? "Zeon"		:"";
				this.info.report.setText(currPlayer + " moves next");
				this.board.updateGrid(ocg.getBoard());
			}else if(command == "reportMove") {
				//update game info
				this.info.reportMove.setText(ocg.getReport());
			}else if(command == "reportFinal") {
				//show winner and final result
				this.info.reportMove.setText(ocg.getReport());
				this.board.updateGrid(ocg.getBoard());
			}
		}
	}

}
