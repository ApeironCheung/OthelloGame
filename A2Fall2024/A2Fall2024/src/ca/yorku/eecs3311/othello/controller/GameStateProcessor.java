package ca.yorku.eecs3311.othello.controller;

import ca.yorku.eecs3311.util.Observable;
import ca.yorku.eecs3311.util.Observer;

public abstract class GameStateProcessor extends Observable implements GameStateManagement, Observer{
	protected GameFlowProcessor gameflow;
	protected boolean isUndo;
	protected char[][] board;
	protected OthelloBoardDecoder decoder;
	
	public GameStateProcessor() {
		this.isUndo = false;
		this.decoder = new OthelloBoardDecoder();
	}
	public char[][] getBoard() {
		return this.board;
	}
}
