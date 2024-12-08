package ca.yorku.eecs3311.othello.controller;

import ca.yorku.eecs3311.othello.model.Move;
import ca.yorku.eecs3311.othello.model.Player;

public interface GameFlowProcessor {
	public static final String startGame = "START_GAME";
	public static final String startTurn = "START_TURN";
	public static final String waitingHuman = "WAITING_HUMAN";
	public static final String waitingAI = "WAITING_AI";
	public static final String validatingMove = "VALIDATING_MOVE";
	public static final String gameOver = "GAME_OVER";
	public void StartGame(Player opponent) ;
	public void StartTurn() ;
	public void ValidatingMove(Move move);
	public void GameOver();
}
