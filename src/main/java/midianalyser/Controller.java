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
import javafx.collections.*;

import javafx.stage.DirectoryChooser;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.File;
import static javafx.scene.input.KeyCode.V;

import org.controlsfx.control.*;

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
    private GridPane rhythmTable1;

    @FXML
    private GridPane rhythmTable2;

    @FXML
    private GridPane trochaicTable;

    @FXML
    private GridPane dactylTable;

    @FXML
    private GridPane keyTable;

    @FXML
    private GridPane timeSigTable;

    @FXML
    private GridPane majorTable;

    @FXML
    private CheckComboBox<String> timeFilter;

    @FXML
    private CheckComboBox<String> keyFilter;

    @FXML
    private CheckComboBox<String> majorFilter;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    /**
     *
     */
    public void init(Model model) {
        // create the data to show in the CheckComboBox
        ObservableList<String> filterTimeSig = FXCollections.observableArrayList();
        for (int i = 1; i <= 6; i++) {
            filterTimeSig.add(i + "/" + 4);
        }
        for (int i = 3; i <= 12; i += 3) {
            filterTimeSig.add(i + "/" + 8);
        }
        timeFilter.getItems().addAll(filterTimeSig);

        ObservableList<String> filterKeySig = FXCollections.observableArrayList();
        filterKeySig.add("c");
        filterKeySig.add("c#");
        filterKeySig.add("d");
        filterKeySig.add("d#");
        filterKeySig.add("e");
        filterKeySig.add("f");
        filterKeySig.add("f#");
        filterKeySig.add("g");
        filterKeySig.add("g#");
        filterKeySig.add("a");
        filterKeySig.add("a#");
        filterKeySig.add("b");

        keyFilter.getItems().addAll(filterKeySig);

        ObservableList<String> filterMajorSig = FXCollections.observableArrayList();
        filterMajorSig.add("major");
        filterMajorSig.add("minor");

        majorFilter.getItems().addAll(filterMajorSig);

        this.model = model;



        timeFilter.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {

                ObservableList<String> selectedItems = timeFilter.getCheckModel().getCheckedItems();
                model.filterTimeSig(selectedItems);
         }
        });

        keyFilter.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {

                ObservableList<String> selectedItems = keyFilter.getCheckModel().getCheckedItems();
                model.filterKeySig(selectedItems);
            }
        });

        majorFilter.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {

                ObservableList<String> selectedItems = majorFilter.getCheckModel().getCheckedItems();

                model.filterMajorSig(selectedItems);
            }
        });
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
    public GridPane getRhythmTable1(){
        return rhythmTable1;
    }
    public GridPane getRhythmTable2(){
        return rhythmTable2;
    }

    public GridPane getTrochaicTable(){
        return trochaicTable;
    }

    public GridPane getDactylTable(){
        return dactylTable;
    }

    public GridPane getKeyTable(){
        return keyTable;
    }

    public GridPane getTimeSigTable(){
        return timeSigTable;
    }
}
