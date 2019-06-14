package midianalyser;

import midianalyser.view.*;
import midianalyser.model.*;

import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javafx.stage.DirectoryChooser;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.File;
import static javafx.scene.input.KeyCode.V;


public class Controller {
    private Model model;
    @FXML
    private Stage stage;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private GridPane toneTable;

    @FXML
    private GridPane rhythmTable;

    @FXML
    private GridPane melodyTable;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    /**
     *
     */
    public void init(Model model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
    }


    /**
     *
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    private void onSelectFolder(){
        DirectoryChooser folderChooser = new DirectoryChooser();
        folderChooser.setTitle("select folder");
        File selectedFolder = folderChooser.showDialog(stage);
        if (selectedFolder != null) {
            try {
                model.setDirectory(selectedFolder);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }

    @FXML
    private void onToneButton(){
        model.onToneButton();
    }

    public GridPane getToneTable(){
        return toneTable;
    }

    public GridPane getRhythmTable(){
        return rhythmTable;
    }

    public GridPane getMelodyTable(){
        return melodyTable;
    }
}
