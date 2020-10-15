package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.scene.*;

public class Main extends Application {
	
	/**
	 * runs at the start of the program
	 */
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false);
		FXMLLoader loader = new
		FXMLLoader(getClass().getResource("view/View.fxml"));
		Parent root = loader.load();
		Scene myScene = new Scene(root,467,335);
		primaryStage.setScene(myScene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}