package ca.yorku.eecs3311.othello.view;

import ca.yorku.eecs3311.othello.controller.GameFlowProcessor;
import ca.yorku.eecs3311.othello.controller.GameStateManagement;
import ca.yorku.eecs3311.othello.controller.OthelloControllerGUI;
import ca.yorku.eecs3311.util.Observable;
import ca.yorku.eecs3311.util.Observer;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*********************View Part**************************************************
 * View-----------------indexPage		: buttons choosing different opponents	*
 * 		L gamePage------GameInfoPage	: shows game infos						*
 * 					L	GameBoardPage	: buttons of making moves				*
 * 					L	GameButtons		: save/load/restart/undo/redo buttons	*
 * ******************************************************************************/
public class View implements Observer{

	Scene currScene;
	public GamePage gamePage;
	public IndexPage indexPage;
	Stage stage;
	
	public View(Stage stage) {
		this.stage = stage;
		this.gamePage = new GamePage();
		this.indexPage = new IndexPage();
		this.currScene = this.indexPage.getPage();
	}
	//to change the scene via game and index
	public void setScene(String page){
		Scene scene =	page.equals("game") ? this.gamePage.getScene(): 
										this.indexPage.getPage();
		this.currScene = scene;
		stage.setScene(this.currScene);
		stage.show();
	}
	//for OthelloApplication to get
	public Scene getScene() {
		return this.currScene;
	}	
	@Override
	public void update(Observable o) {
		if((o instanceof OthelloControllerGUI) ) {
			OthelloControllerGUI ctrl = (OthelloControllerGUI) o;
			if(ctrl.getState() == GameFlowProcessor.startGame) {
				this.setScene("game");						//switch to game scene			
			}else if(ctrl.getState() == GameStateManagement.restart) {
				this.setScene("index");						//switch the index scene
			}
		}
	}
}
