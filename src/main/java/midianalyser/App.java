package midianalyser;

import midianalyser.model.*;
import midianalyser.view.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.scene.Scene;


public class App extends Application {
    @Override
	public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/View.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.getScene().getStylesheets().add("midianalyser/view/stylesheet.css");
        Controller controller = loader.getController();
		Model model = new Model();
		View view = new View(model, controller, stage);

	}

    public static void main(String[] args) {
        launch(args);
    }
}
