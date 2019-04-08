package midianalyser.view;

import midianalyser.model.*;
import midianalyser.Controller;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import javafx.collections.ObservableList;


import java.io.IOException;

public class View {
	private GridPane toneTable;

	public View(Model model, Controller controller, Stage primaryStage) throws IOException {
		controller.setStage(primaryStage);
		this.toneTable = controller.getToneTable();
		primaryStage.show();
		setUpGrids(model.getToneList());
	}

	public void setUpGrids(ObservableList<Integer> toneList){

		Text text1 = new Text("" +toneList.get(0));
		toneTable.add(text1, 1, 1);
	}
}
