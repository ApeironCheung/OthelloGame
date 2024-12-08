package ca.yorku.eecs3311.othello.controller;

/***************Design Pattern: Bridge Pattern************************** 
 * GameButtons and GameFlow separately develop
 * Abstract interface GameStateManagement		for handling game buttons
 * Abstract interface GameFlowProcessor			for handling game flow	
 * Abstract class GameStateProcessor implements GameStateManagement
 * Concrete Class OthelloControllerGUI implements GameFlowProcessor
 * Concrete class OthelloGameStateProcessor extends GameState Processor,	 
 * 					also include OthelloControllerGUI in constructor
 * **********************************************************************/
public abstract interface GameStateManagement {
	public static final String saveGame	= "SAVE_GAME";
	public static final String loadGame	= "LOAD_GAME";
	public static final String restart	= "RESTART";
	public static final String undo		= "UNDO";
	public static final String redo		= "REDO";
	public void getState();
	public GameFlowProcessor getGameFlowProcessor();
}
