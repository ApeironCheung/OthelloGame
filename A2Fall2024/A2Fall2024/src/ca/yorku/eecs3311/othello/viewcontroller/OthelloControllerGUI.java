package ca.yorku.eecs3311.othello.viewcontroller;

import ca.yorku.eecs3311.othello.model.*;
import ca.yorku.eecs3311.othello.view.*;
import ca.yorku.eecs3311.util.Observable;
import ca.yorku.eecs3311.util.Observer;

public class OthelloControllerGUI extends Observable implements Observer{
	protected Othello othello;
	protected Player player2;
	protected String action, report;
	protected gameActionOfButtons gab;
	protected boolean isUndo = false;
	protected boolean waitingPlayer;
	protected int turn;

	//use setPlayer to change Player when game start, adapting 
	public OthelloControllerGUI (Othello othello) {
		this.othello = othello;
		this.player2 = new PlayerHuman(this.othello, OthelloBoard.P2);
		this.gab = new gameActionOfButtons(this.othello);
	}

	//Event driven instead of while loop
	//StartGame->StartTurn->MakingMove->TryMove->StartTurn ---- ->GameOver
	public void stateStartGame(Player player2) {
		this.setPlayer2(player2);
		this.stateStartTurn();
		this.gab.actionStartGame(othello);
		this.action = "StartGame";
		this.notifyObservers();
	}
	public void stateStartTurn() {
		if(othello.isGameOver()) {
			this.stateGameOver();
		}else {
			this.report();
			this.stateMakingMove();
		}
	}
	public void stateMakingMove() {
		if(othello.getWhosTurn()== OthelloBoard.P2 && 
				(this.player2 instanceof PlayerGreedy || this.player2 instanceof PlayerRandom)) {
			this.stateTryMove(this.player2.getMove());
		}else {
			this.waitingPlayer = true;
			this.gab.actionMakeMove();
		}	
	}
	public void stateTryMove(Move move) {
		if(this.othello.move(move.getRow(), move.getCol())) {
			this.gab.recordMove(move);
			if(this.othello.isGameOver()) {
				this.stateGameOver();
			}else {
				this.reportMove(othello.getWhosTurn(), move);
				this.stateStartTurn();
			}
		}else {
			this.stateMakingMove();
		}
	}
	public void stateGameOver() {
		this.reportFinal();
	}
	protected void reportFinal() {
		this.action = "reportFinal";
		String P1 = "Federation";
		String P2 = "Zeon";
		String winner = this.othello.getWinner() == OthelloBoard.P1? P1 : P2;
		this.report = 	P1 + ":" + othello.getCount(OthelloBoard.P1) 
				+ " " + P2 + ":" + othello.getCount(OthelloBoard.P2) 
				+ "\n  " + winner + " won\n";		
		this.notifyObservers();
	}
	public void report() {
		this.action = "report";
		this.notifyObservers();
	}	
	public void reportMove(char whosTurn, Move move) {
		this.report = whosTurn + "makes move" + move + "\n";
		this.action = "reportMove";
		this.notifyObservers();
	}
	public String getReport() {
		return this.report;
	}
	@Override
	public void update(Observable o) {
		if (o instanceof IndexPage) {
			IndexPage ip = (IndexPage) o;
			if(ip.getAction() == "Player vs Player") {
				this.stateStartGame(new PlayerHuman(this.othello, OthelloBoard.P2)); 
			}else if(ip.getAction() == "Player vs Greedy") {
				this.stateStartGame(new PlayerGreedy(this.othello, OthelloBoard.P2));
			}else if (ip.getAction() == "Player vs Random") {
				this.stateStartGame(new PlayerRandom(this.othello, OthelloBoard.P2));
			}
		}else if(o instanceof GameButtons) {
			GameButtons gb = (GameButtons) o;
			if(gb.getAction() == "saveGame") {
				this.gab.actionSaveGame(this.player2);
			}else if(gb.getAction() == "loadGame") {
				gameFileIO io = this.gab.actionLoadGame();
				this.setPlayer2(io.player2);
				this.othello = io.othello;
				this.stateStartTurn();
			}else if(gb.getAction() == "restart") {
				this.othello = new Othello();
				this.action = "restart";
				this.notifyObservers();//notify view to change to index page
			}else if (gb.getAction() == "undo"){//update this.othello and othello inside player2
				this.isUndo = true;
				this.othello = this.gab.actionUndo(this.player2);//update this.othello
				this.updatePlayer2Othello();					//update player2's othello
				this.stateStartTurn();							//start turn, render view
			}else if(gb.getAction() == "redo") {
				if(this.isUndo) {
					Move redo = this.gab.actionRedo();
					this.stateTryMove(redo);	
				}
			}
			
		}else if (o instanceof GameBoardPage && this.waitingPlayer) {
			this.isUndo = false;
			this.waitingPlayer = false;
			GameBoardPage gbp = (GameBoardPage) o;
			this.stateTryMove(gbp.getMove());//move by event
		}
	}
	//set player2 when start of game or resume after load game
	private void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	//set the player2's othello to this.othello
	private void updatePlayer2Othello() {
		Player player;
		if(this.player2 instanceof PlayerGreedy) {
			player = new PlayerGreedy(this.othello, OthelloBoard.P2);
		}else if(this.player2 instanceof PlayerRandom){
			player = new PlayerRandom(this.othello, OthelloBoard.P2);
		}else {
			player = new PlayerHuman(this.othello, OthelloBoard.P2);
		}
		this.setPlayer2(player);

	}
	
	/* decode this.othello.getBoardString, for GameBoard GridPane to get  
	 * @returns 2Darray of game board*/
	public char[][] getBoard(){
		int dim = Othello.DIMENSION;
		String[]lines = this.othello.getBoardString().split("\n");
		char[][] board = new char[dim][dim];
		for(int row=0; row<dim; row++) {
			String line = lines[2 * row + 2];//skip first 2 lines and every alternate line after
			for(int col=0; col<dim; col++) {
				board[row][col] = line.charAt(2 * col + 2);//skip first 2 chars and every alternate char after
			}
		}		
		return board;
	}
	
	//for game info grid to get player's count
	public int getCount(char player) {
		return this.othello.getCount(player);
	}
	//for game info grid to getWhosTurn
	public char getWhosTurn() {
		return this.othello.getWhosTurn();
	}
	//for observers to get Action
	public String getAction() {
		return this.action;
	}

}	
