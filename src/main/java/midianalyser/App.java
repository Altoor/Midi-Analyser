package midianalyser;

import midianalyser.model.*;
import midianalyser.view.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
	public void start(Stage stage) throws Exception {
		Model model = new Model(getParameters().getRaw());
        Controller controller = new Controller();
		View view = new View(model, controller, stage);

	}
    
    public static void main(String[] args) {
        launch(args);
    }
}
