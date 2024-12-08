package ca.yorku.eecs3311.othello.model;
import ca.yorku.eecs3311.othello.controller.GameFlowProcessor;
import ca.yorku.eecs3311.othello.controller.OthelloBoardDecoder;
import ca.yorku.eecs3311.util.*;

import java.util.Random;

/**
 * Capture an Othello game. This includes an OthelloBoard as well as knowledge
 * of how many moves have been made, whosTurn is next (OthelloBoard.P1 or
 * OthelloBoard.P2). It knows how to make a move using the board and can tell
 * you statistics about the game, such as how many tokens P1 has and how many
 * tokens P2 has. It knows who the winner of the game is, and when the game is
 * over.
 * 
 * See the following for a short, simple introduction.
 * https://www.youtube.com/watch?v=Ol3Id7xYsY4
 * 
 * @author student
 *
 */
public class Othello implements Element{
	public static final int DIMENSION=8; // This is an 8x8 game

	protected OthelloBoard board=new OthelloBoard(Othello.DIMENSION);
	private char whosTurn = OthelloBoard.P1;
	private int numMoves = 0;
	protected boolean isMoveSuccess;		// For Visitor to get
	protected Move move = new Move(-1,-1);					// For Game Flow Controller to get
	private OthelloBoardDecoder decoder = new OthelloBoardDecoder();
	
	/**
	 * return P1,P2 or EMPTY depending on who moves next.
	 * 
	 * @return P1, P2 or EMPTY
	 */
	public char getWhosTurn() {
		return this.whosTurn;
	}
	
	/**
	 * 
	 * @param row 
	 * @param col
	 * @return the token at position row, col.
	 */
	public char getToken(int row, int col) {
		return this.board.get(row, col);
	}

	/**
	 * Attempt to make a move for P1 or P2 (depending on whos turn it is) at
	 * position row, col. A side effect of this method is modification of whos turn
	 * and the move count.
	 * 
	 * @param row
	 * @param col
	 * @return whether the move was successfully made.
	 */
	public boolean move(int row, int col) {
		if(this.board.move(row, col, this.whosTurn)) {
			this.whosTurn = OthelloBoard.otherPlayer(this.whosTurn);
			char allowedMove = board.hasMove();
			if(allowedMove!=OthelloBoard.BOTH)this.whosTurn=allowedMove;
			this.numMoves++;
			this.move = new Move(row, col);
			return true;
		} else {
			return false;
		}
	}


	/**
	 * 
	 * @param player P1 or P2
	 * @return the number of tokens for player on the board
	 */
	public int getCount(char player) {
		return board.getCount(player);
	}


	/**
	 * Returns the winner of the game.
	 * 
	 * @return P1, P2 or EMPTY for no winner, or the game is not finished.
	 */
	public char getWinner() {
		if(!this.isGameOver())return OthelloBoard.EMPTY;
		if(this.getCount(OthelloBoard.P1)> this.getCount(OthelloBoard.P2))return OthelloBoard.P1;
		if(this.getCount(OthelloBoard.P1)< this.getCount(OthelloBoard.P2))return OthelloBoard.P2;
		return OthelloBoard.EMPTY;
	}


	/**
	 * 
	 * @return whether the game is over (no player can move next)
	 */
	public boolean isGameOver() {
		return this.whosTurn==OthelloBoard.EMPTY;
	}

	/**
	 * 
	 * @return a copy of this. The copy can be manipulated without impacting this.
	 */
	public Othello copy() {
		Othello o= new Othello();
		o.board=this.board.copy();
		o.numMoves = this.numMoves;
		o.whosTurn = this.whosTurn;
		return o;
	}


	/**
	 * 
	 * @return a string representation of the board.
	 */
	public String getBoardString() {
		return board.toString()+"\n";
	}


	/**
	 * run this to test the current class. We play a completely random game. DO NOT
	 * MODIFY THIS!! See the assignment page for sample outputs from this.
	 * 
	 * @param args
	 */
	public static void main(String [] args) {
		Random rand = new Random();


		Othello o = new Othello();
		System.out.println(o.getBoardString());
		while(!o.isGameOver()) {
			int row = rand.nextInt(8);
			int col = rand.nextInt(8);

			if(o.move(row, col)) {
				System.out.println("makes move ("+row+","+col+")");
				System.out.println(o.getBoardString()+ o.getWhosTurn()+" moves next");
			}
		}

	}
	/**Newly Added methods:
	 * accept(Visitor visitor)		: accept Visitor
	 * makeMove(int row, int col)	: record move for Getters to get
	 * Move getMove()				: for Observer to get move
	 * resumeGame(char[][] board)	: for load game or undo
	 * void update(Observable o)	: Validate move for Controller 
	 * 								  Load Game/Undo for Game State manager
	 * isMoveSuccess				: let visitor know is move success
	 * */

	@Override
	public void accept(Visitor visitor) {
		visitor.visitOthello(this);
	}
	public void resumeGame(char[][] board) {
		this.board = new OthelloBoard(board);
	}
	public boolean makeMove(int row, int col) {
		if(this.move(row, col)) {
			this.move = new Move(row, col);
			return true;
		}else {
			return false;
		}
	}
	//return the last player's move
	public Move getMove() {
		return this.move;
	}
	
	public void loadGame(Othello othello) {
		this.numMoves = othello.getNumMoves();
		this.setBoard(this.decoder.getBoard(othello));
		this.whosTurn = othello.getWhosTurn();
		System.out.println("Othello load game:\n" + this.getBoardString());
	}
	public void restartGame() {
		this.numMoves = 0;
		this.setBoard(this.decoder.resetBoard());
		this.whosTurn = OthelloBoard.P1;
	}
	public int getNumMoves() {
		return this.numMoves;
	}
	
	private void setBoard(char[][] boardStr) {
		OthelloBoard board = (OthelloBoard) this.board;
		board.setBoard(boardStr);
	}

	public boolean isMoveSuscess() {
		return this.isMoveSuccess;
	}
}


