package midianalyser;

import midianalyser.view.*;
import midianalyser.model.*;

import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.File;
import static javafx.scene.input.KeyCode.V;


public class Controller {
    private Model model;
    @FXML
    private Stage stage;

    /**
     *
     */
    public void init(Model model) {
        this.model = model;
    }

    /**
     *
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     *
     */
    @FXML
    private void onKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case V:
                try {
                    new View(model, this, new Stage());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                break;
        }
    }
}
