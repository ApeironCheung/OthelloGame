package ca.yorku.eecs3311.othello.view;

import ca.yorku.eecs3311.othello.model.*;
import ca.yorku.eecs3311.util.Observable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class GameBoardPage extends Observable implements EventHandler<ActionEvent> {
	static final int BUTTON_WH = 50;
	GridPane gameBoard;
	Move move;				//store onclick button's row, col into Move

	public GameBoardPage() {
		this.gameBoard = startBoard();
		// move do not instance until make move
	}

	//for observer to get move
	public Move getMove() {
		return this.move;
	}

	//update button images of gameboard
	public void updateGrid(char[][] board) {
		for (int row = 0; row < Othello.DIMENSION; row++) {
			for (int col = 0; col < Othello.DIMENSION; col++) {
				Button button = (Button) this.gameBoard.getChildren().get(row * Othello.DIMENSION + col);
				updateButtonImage(button, board, row, col);
			}
		}
	}

	//create start board
	private GridPane startBoard() {
		int dim = Othello.DIMENSION;
		char[][] board = new char[dim][dim];
		for (int row = 0; row < dim; row++) {
			for (int col = 0; col < dim; col++) {
				board[row][col] = OthelloBoard.EMPTY;
			}
		}
		int mid = dim / 2;
		board[mid - 1][mid - 1] = board[mid][mid] = OthelloBoard.P1;
		board[mid][mid - 1] = board[mid - 1][mid] = OthelloBoard.P2;
		return createBoard(board);
	}

	//decode othello.getBoardString into char[][]
	private GridPane createBoard(char[][] board) {
		GridPane grid = new GridPane();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board.length; col++) {
				Button button = createButton(board, row, col);
				grid.add(button, col, row);
			}
		}
		return grid;
	}

	Button createButton(char[][] board, int row, int col) {
		Button button = new Button();
		button.setMinSize(BUTTON_WH, BUTTON_WH);
		updateButtonImage(button, board, row, col);
		button.setText(row + "" + col);
		// Add an event handler to the button
		button.setOnAction(event -> {
			this.move = new Move(row, col);// create new move when onClick
			this.notifyObservers();
		});

		return button;
	}

	//update the image of buttons
	private void updateButtonImage(Button button, char[][] board, int row, int col) {
		char token = board[row][col];
		String imagePath;// = "/Empty.jpg";
		imagePath = token == OthelloBoard.P1 ? "/Fed.jpg" : token == OthelloBoard.P2 ? "/Zeon.jpg" : "/Empty.jpg";// Default
																													// Image
		Image image = new Image(getClass().getResourceAsStream(imagePath));
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(BUTTON_WH);
		imageView.setFitHeight(BUTTON_WH);
		imageView.setPreserveRatio(true);
		button.setGraphic(imageView);
	}

	public void handle(ActionEvent arg0) {
	}
}
