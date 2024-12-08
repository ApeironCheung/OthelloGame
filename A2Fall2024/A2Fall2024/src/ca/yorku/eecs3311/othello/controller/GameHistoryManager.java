package ca.yorku.eecs3311.othello.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ca.yorku.eecs3311.othello.model.Move;
import ca.yorku.eecs3311.othello.model.Othello;
import ca.yorku.eecs3311.othello.model.Player;
import ca.yorku.eecs3311.othello.model.PlayerHuman;
/*************************************
 * Manage Game History to perform:
 * Save Game, Load Game, undo, redo 
 * ***********************************/
public class GameHistoryManager {
	gameFileIO io;
	Othello othello;
	List<Move> moveHistory; //record each validated move
	Stack<Othello> undoStack; //push Othellos of each turn onto stack, pop when undo
	Stack<Move> redoStack;  //push each undo onto stack, pop when redo
	
	GameHistoryManager(Othello othello){
		this.othello = othello;
		this.moveHistory = new ArrayList<>();
		this.io = new gameFileIO();
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
	}
	/* @ requires othello.move(move) == true; 
	 * record human and AI moves*/
	void recordMove(Move move) {
		this.moveHistory.add(move);
		System.out.println("record move");
		this.printMoveHistory();
	}
	
	/* @requires this.othello.equals(client.othello); 
	 * push human moves*/
	void actionMakeMove() {

		this.undoStack.push(this.othello.copy());
	}
	/* @requires player!=null; */
	protected void actionSaveGame(Player player2) {
		gameFileIO.saveGame(this.moveHistory, player2);
	}
	/* @ensures in.othello !=null && in.player2 !=null & in.moveHistory!=null ;
	load game file, resume othello, player2 and move history*/
	protected gameFileIO actionLoadGame() {
		gameFileIO in =  gameFileIO.loadGame();
		this.moveHistory = in.moveHistory;
		return in;
	}
	
	/* @requires player2!=null;
	roll back move history, push redoStack*/
	protected Othello actionUndo(Player player2) {

		if(! this.moveHistory.isEmpty()) {
			if(!(player2 instanceof PlayerHuman)) {
				//remove AI move
				this.moveHistory.remove(this.moveHistory.size() -1);
			}//put human move to redoStack
			this.undoStack.pop();
			this.redoStack.push(this.moveHistory.remove(this.moveHistory.size() -1));System.out.println("redoStack.size: "+this.redoStack.size());
		}
		System.out.println("undo");
		this.printMoveHistory();
		return this.undoStack.pop();
	}
	/*@ensures result !=null; 
	 * return a valid move if redoStack is not Empty*/
	Move actionRedo() {
		Move result = new Move(-1,-1);
		if(! this.redoStack.isEmpty()) {
			result = this.redoStack.pop();System.out.println("redoStack.size: "+this.redoStack.size());
		}
		System.out.println("redo:");
		this.printMoveHistory();
		return result;
	}
	private void printMoveHistory() {
		for(int i = 0; i < this.moveHistory.size(); i++) {
			System.out.println(this.moveHistory.get(i));
		}
	}
}
