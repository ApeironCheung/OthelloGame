package ca.yorku.eecs3311.othello.viewcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ca.yorku.eecs3311.othello.model.Move;
import ca.yorku.eecs3311.othello.model.Othello;
import ca.yorku.eecs3311.othello.model.Player;
import ca.yorku.eecs3311.othello.model.PlayerHuman;

public class gameActionOfButtons {
	gameFileIO io;
	Othello othello, lastOthello, currOthello;
	List<Move> moveHistory;
	Stack<Othello> undoStack;
	Stack<Move> redoStack;
	
	gameActionOfButtons(Othello othello){
		this.othello = othello;
		this.moveHistory = new ArrayList<>();
		this.io = new gameFileIO();
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();
	}
	void actionStartGame(Othello othello) {
		this.currOthello = othello;
		this.lastOthello = othello.copy();
	}
	void recordMove(Move move) {
		this.moveHistory.add(move);
	}
	void actionMakeMove() {
		this.lastOthello = this.othello.copy();
		this.undoStack.push(this.othello.copy());
	}
	protected void actionSaveGame(Player player2) {
		gameFileIO.saveGame(this.moveHistory, player2);
	}
	
	//load game file, resume othello, player2 and move history
	protected gameFileIO actionLoadGame() {
		gameFileIO in =  gameFileIO.loadGame();
		this.currOthello = in.othello;
		this.moveHistory = in.moveHistory;
		return in;
	}
	
	//use last othello as curr othello, roll back move history, push redoStack
	protected Othello actionUndo(Player player2) {
		if(! this.moveHistory.isEmpty()) {
			if(!(player2 instanceof PlayerHuman)) {
				//remove AI move
				this.moveHistory.remove(this.moveHistory.size() -1);
			}//put human move to redoStack
			this.undoStack.pop();
			this.redoStack.push(this.moveHistory.remove(this.moveHistory.size() -1));System.out.println("redoStack.size: "+this.redoStack.size());
		}
		this.othello = this.lastOthello; //use lastOthello as curr othello
		return this.undoStack.pop();
	}
	//
	Move actionRedo() {
		Move result = new Move(-1,-1);
		if(! this.redoStack.isEmpty()) {
			result = this.redoStack.pop();System.out.println("redoStack.size: "+this.redoStack.size());
		}
		return result;
	}
}
