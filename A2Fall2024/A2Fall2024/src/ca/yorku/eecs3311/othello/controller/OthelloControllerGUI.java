package ca.yorku.eecs3311.othello.controller;

import ca.yorku.eecs3311.othello.model.*;
import ca.yorku.eecs3311.othello.view.*;
import ca.yorku.eecs3311.util.Observable;
import ca.yorku.eecs3311.util.Observer;

/**********************OthelloControllerGUI**************************
 * Focus on game flow management:									*
 * instead of while loop, use methods in event driven way.			*
 * StartGame->StartTurn->ValidateMove->StartTurn ---- ->GameOver	*
 * Get command from GameStateProcessor								*
 * Notice Visitor,  Observe GameBoardPage							*
 * *****************************************************************/
/***************Design Pattern: Bridge Pattern************************** 
 * GameButtons and GameFlow separately develop
 * Abstract interface GameStateManagement		for handling game buttons
 * Abstract interface GameFlowProcessor			for handling game flow	
 * Abstract class GameStateProcessor implements GameStateManagement
 * Concrete Class OthelloControllerGUI implements GameFlowProcessor
 * Concrete class OthelloGameStateProcessor extends GameState Processor,	 
 * 					also include OthelloControllerGUI in constructor
 * **********************************************************************/

public class OthelloControllerGUI extends Observable implements Observer, GameFlowProcessor{
	protected Othello othello;
	protected Player player2;
	protected boolean isUndo = false;
	protected boolean waitingPlayer;
	protected int turn;
	protected OthelloBoardDecoder decoder;
	protected GameHistoryManager history;
	public String state;

	//use setPlayer to change Player when game start, adapting 
	public OthelloControllerGUI (Othello othello) {
		this.othello = othello;
		this.player2 = new PlayerHuman(this.othello, OthelloBoard.P2);
		this.history = new GameHistoryManager(othello);
		this.decoder = new OthelloBoardDecoder();
	}

	//Event driven instead of while loop
	//StartGame->StartTurn->ValidateMove->StartTurn ---- ->GameOver
	@Override
	public void StartGame(Player opponent) {
		this.setPlayer2(opponent);
		this.StartTurn();
		this.state = GameFlowProcessor.startGame;
		this.notifyObservers();
	}

	//Start of each turn, check isGameOver, whosTurn, call visitor, record for undo
	@Override
	public void StartTurn() {
		this.state = GameFlowProcessor.startTurn;
		this.notifyObservers();				//call visitor
		if(othello.isGameOver()) {
			this.GameOver();
		}else {
			if(othello.getWhosTurn()== OthelloBoard.P2 && 
					(this.player2 instanceof PlayerGreedy || this.player2 instanceof PlayerRandom)) {
				this.state = GameFlowProcessor.waitingAI;
				this.ValidatingMove(this.player2.getMove());
			}else {
				this.state = GameFlowProcessor.waitingHuman;
				this.waitingPlayer = true;		//set boolean prevent invalid notice
				this.history.actionMakeMove();	//record Othello before move for undo
			}	
		}	
	}

	//let Othello validate move, check 
	@Override
	public void ValidatingMove(Move move) {
		this.state = GameFlowProcessor.validatingMove;
		if(this.othello.move(move.getRow(), move.getCol())) {
			this.history.recordMove(move);
		}
		this.StartTurn();
	}

	@Override
	public void GameOver() {
		this.state = GameFlowProcessor.gameOver;
	}

	@Override
	public void update(Observable o) {
		if (o instanceof GameBoardPage && this.waitingPlayer) {
			this.isUndo = false;
			this.waitingPlayer = false;
			GameBoardPage gbp = (GameBoardPage) o;
			this.ValidatingMove(gbp.getMove());	//move by event
		} 		  
	}
	//set player2 when start of game or resume after load game
	protected void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	public char[][] getBoard(){
		return this.decoder.getBoard(this.othello);
	}
	
	//for game info grid to get player's count
	public int getCount(char player) {
		return this.othello.getCount(player);
	}
	public boolean isUndo() {
		return this.isUndo;
	}
	public String getState() {
		return this.state;
	}
}	
