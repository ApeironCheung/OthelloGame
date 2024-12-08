package ca.yorku.eecs3311.othello.controller;

import ca.yorku.eecs3311.othello.model.Othello;
import ca.yorku.eecs3311.othello.model.OthelloBoard;
/********************************************************
 * include frequently used methods, 					*
 * easy for clients to include in constructor and use	*
 * ******************************************************/
public class OthelloBoardDecoder {

	/* decode this.othello.getBoardString, for client to get  
	 * @ensure \reuslt != null  */
	public char[][] getBoard(Othello othello){
		int dim = Othello.DIMENSION;
		String[]lines = othello.getBoardString().split("\n");
		char[][] board = new char[dim][dim];
		for(int row=0; row<dim; row++) {
			String line = lines[2 * row + 2];//skip first 2 lines and every alternate line after
			for(int col=0; col<dim; col++) {
				board[row][col] = line.charAt(2 * col + 2);//skip first 2 chars and every alternate char after
			}
		}		
		return board;
	}
	//return a board as game start
	public char[][] resetBoard(){
		int dim = Othello.DIMENSION;
		char P1 = OthelloBoard.P1;
		char P2 = OthelloBoard.P2;
		char EMPTY = OthelloBoard.EMPTY;
		char[][] board = new char[dim][dim];
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				board[row][col] = EMPTY;
			}
		}
		int mid = dim / 2;
		board[mid - 1][mid - 1] = board[mid][mid] = P1;
		board[mid][mid - 1] = board[mid - 1][mid] = P2;
		return board;
	}
}
