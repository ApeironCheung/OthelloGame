package ca.yorku.eecs3311.othello.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.yorku.eecs3311.othello.model.*;

public class gameFileIO {
	protected Othello othello;
	Player player2;
	List<Move> moveHistory;
	
	gameFileIO(){
		this(new Othello(), new PlayerHuman(new Othello(), OthelloBoard.P2), new ArrayList<Move>());
	}
	
	gameFileIO(Othello othello, Player player2, List<Move> moveHistory){
		this.othello = othello;
		this.player2 = player2;
		this.moveHistory = new ArrayList<>();
	}	
	//save game by encode ArrayList<Move> and Player into String
		public static void saveGame(List<Move> moveHistory, Player player2){
			String opp = "";
			if(player2 instanceof PlayerHuman) {
				opp = "Human";
			}else if(player2 instanceof PlayerRandom) {
				opp = "Random";
			}else if(player2 instanceof PlayerGreedy) {
				opp = "Greedy";
			}
			String save = opp + "\n";							//the string to be saved
			for(int i = 0; i < moveHistory.size() ; i ++) {		//add moves to String save
				Move move = (Move) moveHistory.get(i);
				save += move.getRow() + " " + move.getCol() + "\n";
			}
			try (BufferedWriter writer = new BufferedWriter(new FileWriter("saveGame.othello"))) {
				writer.write(save);
			}catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		
		//Decode saved String, redo all moves to a new Othello, Othello, player and history to return
		public static gameFileIO loadGame() {
			String opp = null;
			List<Move> moves = new ArrayList<>();						//to store loaded moves
				try(BufferedReader reader = new BufferedReader(new FileReader("saveGame.othello"))) {
				opp = reader.readLine();								//read player2 type
				if(! opp.equals("Human") && !opp.equals("Random")&& !opp.equals("Greedy")) {
					throw new IllegalArgumentException("Invalid opponent type in save file.");
				}
				String line;
				while((line = reader.readLine())!= null) {
					String[] parts = line.split(" ");					//decode moves
					if(parts.length != 2) {
						throw new IllegalArgumentException("Invalid move format in save file.");
					}
					int row = Integer.parseInt(parts[0]);
					int col = Integer.parseInt(parts[1]);
					if(row < 0 || row >= Othello.DIMENSION || col < 0 || col >= Othello.DIMENSION) {
						throw new IllegalArgumentException("Invalid move format in save file.");
					}else {
						
						moves.add(new Move(row,col));					//save to arraylist
					}
				}
			}catch (IOException e) {
				System.out.println(e.getMessage());
				return null;
			}
			Othello othello = new Othello();							//initialize othello
			//Make othello moves according to the loaded record
			for(int i = 0; i < moves.size();i++) {
				Move move = moves.get(i);
				boolean success = othello.move(move.getRow(), move.getCol());
				if(!success) throw new IllegalStateException(
						"Invalid move in save file: move" + i + ": " 
								+ move.getRow() + ", " + move.getCol());
			}
			Player opponent;
			switch(opp) {												//initialize player2
				case "Human":
					opponent = new PlayerHuman(othello, OthelloBoard.P2); break;
				case "Random":
					opponent = new PlayerRandom(othello, OthelloBoard.P2); break;
				case "Greedy":
					opponent = new PlayerGreedy(othello, OthelloBoard.P2); break;
				default:
					throw new IllegalStateException("Unexpected opponent type: " + opp);
			}
			return new gameFileIO(othello, opponent, moves);			//return encapsulated data		
		}
		public Othello getOthello() {
			return this.othello;
		}
}
