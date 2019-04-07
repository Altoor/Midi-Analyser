package midianalyser.view;

import midianalyser.model.*;
import midianalyser.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class View {
	public View(Model model, Controller controller, Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("View.fxml"));
		Stage stage = new Stage();
		stage.setScene(new Scene(loader.load()));
		stage.getScene().getStylesheets().add("midianalyser/view/stylesheet.css");
		controller.init(model);
		controller.setStage(stage);
		stage.show();
	}
}
