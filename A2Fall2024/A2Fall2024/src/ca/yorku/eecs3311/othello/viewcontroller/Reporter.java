package ca.yorku.eecs3311.othello.viewcontroller;

import ca.yorku.eecs3311.util.Observable;
import ca.yorku.eecs3311.othello.controller.OthelloBoardDecoder;
import ca.yorku.eecs3311.othello.model.*;
public class Reporter extends Observable{
	char[][] board;						//for view to update game board
	String report;						//report move or report final
	int P1count, P2count;				//players' token count
	char whosTurn;						//who is the current player
	public boolean isGameOver;
	boolean isMoveSuccess;	
	Othello othello;					//most update othelloGUI
	OthelloBoardDecoder decoder;
	
	public Reporter() {
		this.decoder = new OthelloBoardDecoder();
	}
	
	public void makeReport(Othello othello) {
		this.board = this.decoder.getBoard(othello);
		this.isMoveSuccess = othello.isMoveSuscess();
		this.P1count = othello.getCount(OthelloBoard.P1);
		this.P2count = othello.getCount(OthelloBoard.P2);
		this.whosTurn = othello.getWhosTurn();
		this.isGameOver = othello.isGameOver();
		this.othello = othello;
		if(othello.isGameOver()) {
			this.reportFinal(othello);
		}else {
			this.reportMove(othello.getWhosTurn(), othello.getMove());
		}
		this.notifyObservers();
	}
	
	public void reportMove(char whosTurn, Move move) {
		char lastMoved = OthelloBoard.otherPlayer(whosTurn);
		char P1 = OthelloBoard.P1;
		String lastMovedPlayer = lastMoved == P1? "Federation" : "Zeon";		
		this.report = lastMovedPlayer + " makes move:\n " + move.getRow() + ", "+ move.getCol() + "\n";
	}
	public void reportFinal(Othello othello) {
	String P1 = "Federation";
	String P2 = "Zeon";
	String winner = othello.getWinner() == OthelloBoard.P1? P1 : P2;
	this.report = 	P1 + ":" + othello.getCount(OthelloBoard.P1) 
			+ " " + P2 + ":" + othello.getCount(OthelloBoard.P2) 
			+ "\n  " + winner + " won\n";		
}
	public char getWhosTurn() {
		return this.whosTurn;
	}
	public int getCount(char player) {
		return player == OthelloBoard.P1? this.P1count : this.P2count;
	}
	public boolean getIsGameOver() {
		return this.isGameOver;
	}
	public Othello getOthello(){
		Othello copy = this.othello.copy();
		return copy;
	}
	public char[][] getBoard(){
		return this.board;
	}
	public String getReport() {
		return this.report;
	}
}
