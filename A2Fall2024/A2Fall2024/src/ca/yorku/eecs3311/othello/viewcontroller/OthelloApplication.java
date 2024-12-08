package ca.yorku.eecs3311.othello.viewcontroller;
import ca.yorku.eecs3311.othello.controller.*;
import ca.yorku.eecs3311.othello.model.*;
import ca.yorku.eecs3311.othello.view.View;
import javafx.application.Application;
import javafx.stage.Stage;

public class OthelloApplication extends Application {
	// REMEMBER: To run this in the lab put 
	// --module-path "/usr/share/openjfx/lib" --add-modules javafx.controls,javafx.fxml
	// in the run configuration under VM arguments.
	// You can import the JavaFX.prototype launch configuration and use it as well.
	/****************** MVC of This Application *****************************
	 * Model: Othello, OthelloBoard (edited)								*
	 * View: Scenes, GridPanes and Buttons in View,IndexPage, GamePage etc	*
	 * Controller:	GameState and button handling: GameStateProcessor		*
	 * 				GameFlow handling: OthelloControllerGUI					*
	 * 				GameReporting:	OthelloVisitor, Reporter				*
	 * Helpers:	gameFileIO, GameHistoryManager, OthelloBoardDecoder			*
	 * **********************************************************************/
	/***************Design Pattern: Bridge Pattern*******************************
	 * GameButtons and GameFlow separately develop								*
	 * Abstract interface GameStateManagement		for handling game buttons	*
	 * Abstract interface GameFlowProcessor			for handling game flow		*
	 * Abstract class GameStateProcessor implements GameStateManagement			*
	 * Concrete Class OthelloControllerGUI implements GameFlowProcessor			*
	 * Concrete class OthelloGameStateProcessor extends GameState Processor,	* 
	 * 					also include OthelloControllerGUI in constructor		*
	 * **************************************************************************/
	/*************** Design Pattern: Strategy Pattern ***************************
	 *  Concrete classes PlayerHuman, PlayerRandom, PlayerGreedy				* 
	 *  extends Abstract class Player											*
	 *  Include abstract Player in constructor of OthelloGameProcessor.gameflow	*
	 *  leave it not instance until get message from index Page					*
	 *  Instance concrete object for strategy after receive message 			*
	 ****************************************************************************/
	/****************Design Pattern: Visitor Pattern*************************
	 * Interface Element, Visitor, Concrete Class Visitor         		  	*
	 * OthelloVisitor implements Visitor include Othello in visit method	*
	 * Othello implements Element, accepts Visitor OthelloVisitor  			*
	 * **********************************************************************/
	/****************Design Pattern: Observer Pattern************************************
	 * Classes extends Observable attach classes implements Observer					*
	 * IndexPage attach OthelloGameStateManagement: 				for game start & P2	*
	 * OthelloGameStateProcessor.OthelloControllerGUI attach View:	for switch Scenes	*
	 * GameBoardPage attach OthelloControllerGUI:					for move event		* 
	 * OthelloControllerGUI attach OthelloVisitor:					for call visit		*
	 * OthelloVisitor.Reporter attach GamePage:						for game updates	*
	 * GameButtons attach OthelloGameStateProcessor:	for restart/save/load/undo/redo	*
	 * **********************************************************************************/

	
	@Override
	public void start(Stage stage) throws Exception {
		// Create and hook up the Model, View and the controller		
		// MODEL
		Othello othello=new Othello();		
		// CONTROLLER	
		OthelloGameStateProcessor processor = new OthelloGameStateProcessor(othello);
		OthelloControllerGUI ocg = processor.getCtrl();
		OthelloVisitor visitor = new OthelloVisitor();
		// CONTROLLER->MODEL hookup
		othello.accept(visitor);					//visitor can visit othello
		ocg.attach(visitor);						//controller can call visitor make visit
		// VIEW
		View view = new View(stage);
		// VIEW->CONTROLLER hookup
		view.indexPage.attach(processor);			//index message P2 strategy to processor
		view.gamePage.board.attach(ocg);			//game board message moves to controller 
		view.gamePage.buttons.attach(processor);	//buttons notice processor for events
		// MODEL->VIEW hookup
		ocg.attach(view);							//controller notices view for restart/ start
		visitor.reporter.attach(view.gamePage);		//gamePage subscribe reporter for reports

		// SCENE
        stage.setTitle("Othello");
		stage.setScene(view.getScene());
				
		// LAUNCH THE GUI
		stage.show();
	}
	
	public static void main(String[] args) {
//		OthelloApplication view = new OthelloApplication();
		launch(args);
	}
}
