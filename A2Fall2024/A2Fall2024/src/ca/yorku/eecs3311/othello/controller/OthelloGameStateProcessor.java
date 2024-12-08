package ca.yorku.eecs3311.othello.controller;

import ca.yorku.eecs3311.othello.model.*;
import ca.yorku.eecs3311.othello.view.GameButtons;
import ca.yorku.eecs3311.othello.view.IndexPage;
import ca.yorku.eecs3311.util.Observable;

/***************Design Pattern: Bridge Pattern************************** 
 * GameButtons and GameFlow separately develop
 * Abstract interface GameStateManagement		for handling game buttons
 * Abstract interface GameFlowProcessor			for handling game flow	
 * Abstract class GameStateProcessor implements GameStateManagement
 * Concrete Class OthelloControllerGUI implements GameFlowProcessor
 * Concrete class OthelloGameStateProcessor extends GameState Processor,	 
 * 					also include OthelloControllerGUI in constructor
 * **********************************************************************/
/*************** Design Pattern: Strategy Pattern *******************
 *  Please see public void update() start from line 41										*
 *  Include abstract object Player in constructor of this.gameflow	*
 *  leave it not instance until get message from index Page			*
 *  Instance concrete object for strategy after receive message 	*
 ********************************************************************/	

public class OthelloGameStateProcessor extends GameStateProcessor {

	public OthelloGameStateProcessor(Othello othello) {
		super();
		this.gameflow = new OthelloControllerGUI(othello);
	}
	
	@Override
	public GameFlowProcessor getGameFlowProcessor() {
		return this.gameflow;
	}

	@Override
	public void update(Observable o) {
		OthelloControllerGUI controller  = (OthelloControllerGUI)this.gameflow;

		if (o instanceof IndexPage) {		
			IndexPage ip = (IndexPage) o;
			Player opp = null;
			if(ip.getAction() == "Player vs Player") {
				new PlayerHuman(controller.othello, OthelloBoard.P2);
			}else if(ip.getAction() == "Player vs Greedy") {
				opp = new PlayerGreedy(controller.othello, OthelloBoard.P2);
			}else if (ip.getAction() == "Player vs Random") {
				opp = new PlayerRandom(controller.othello, OthelloBoard.P2);
			}
			controller.StartGame(opp);
			}else if(o instanceof GameButtons) {
			GameButtons gb = (GameButtons) o;
			OthelloControllerGUI ctrl = (OthelloControllerGUI)this.gameflow;
			if(gb.getAction() == GameStateManagement.saveGame) {
				ctrl.history.actionSaveGame(ctrl.player2);
			}else if(gb.getAction() == GameStateManagement.loadGame) {
				gameFileIO io = ctrl.history.actionLoadGame();
				ctrl.setPlayer2(io.player2);
				ctrl.othello.loadGame(io.othello);
				ctrl.StartTurn();
			}else if(gb.getAction() == GameStateManagement.restart) {
				ctrl.state = GameStateManagement.restart;
				ctrl.othello.restartGame();
				ctrl.notifyObservers();//notify view to change to index page
			}else if (gb.getAction() == GameStateManagement.undo){//update this.othello and othello inside player2
				System.out.println("undo pressed");
				ctrl.state = GameStateManagement.undo;
				ctrl.isUndo = true;
				ctrl.othello.loadGame(ctrl.history.actionUndo(ctrl.player2));
				ctrl.othello.getBoardString();
				ctrl.StartTurn();							//start turn, render view
			}else if(gb.getAction() == GameStateManagement.redo) {
				System.out.println("Processor get redo");
				if(ctrl.isUndo) {
					Move redo = ctrl.history.actionRedo();
					ctrl.history.actionMakeMove();
					ctrl.ValidatingMove(redo);
				}
			}
		}
	}
	
	/*******************************************************************************
	* for observers to get this.gameflow
	* Allows further development of separating othello from constructor of gameflow 
	* and then this can notice othello instead calling gameflow.othello 
	********************************************************************************/
	public OthelloControllerGUI getCtrl() {
		return (OthelloControllerGUI)this.gameflow;
	}
	@Override
	public void getState() {}
}
