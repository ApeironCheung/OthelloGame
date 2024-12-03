package ca.yorku.eecs3311.othello.viewcontroller;

import ca.yorku.eecs3311.othello.model.*;
import ca.yorku.eecs3311.othello.view.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class OthelloApplication extends Application {
	// REMEMBER: To run this in the lab put 
	// --module-path "/usr/share/openjfx/lib" --add-modules javafx.controls,javafx.fxml
	// in the run configuration under VM arguments.
	// You can import the JavaFX.prototype launch configuration and use it as well.
	
	@Override
	public void start(Stage stage) throws Exception {
		// Create and hook up the Model, View and the controller
		
		// MODEL
		Othello othello = new Othello();

		// CONTROLLER
		OthelloControllerGUI ocg = new OthelloControllerGUI(othello);

		// CONTROLLER->MODEL hookup

		// VIEW
		View view = new View(stage);
		// VIEW->CONTROLLER hookup
		view.indexPage.attach(ocg);
		view.gamePage.board.attach(ocg);
		view.gamePage.buttons.attach(ocg);
		// MODEL->VIEW hookup
		ocg.attach(view);
		ocg.attach(view.gamePage);
		
		// SCENE
		stage.setTitle("Othello");
		stage.setScene(view.getScene());
				
		// LAUNCH THE GUI
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
