package midianalyser.view;

import midianalyser.model.*;
import midianalyser.Controller;

import java.util.*;

import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

import javafx.collections.ObservableList;


import java.io.IOException;

public class View {
	private GridPane toneTable;

	public View(Controller controller, Stage primaryStage) throws IOException {
		controller.setStage(primaryStage);
		this.toneTable = controller.getToneTable();
		primaryStage.show();
	}

	public void updateGrids(ArrayList<Integer> toneList){
		Text text = new Text();
		text.setTextAlignment(TextAlignment.CENTER);
		for(int i = 0; i < 12; i++){
			//the tone Step
			text = new Text(""+i);
			toneTable.setConstraints(text, i, 0);
			toneTable.getChildren().add(text);

			//the # of tones
			text = new Text("" +toneList.get(i));
			toneTable.setConstraints(text, i, 1);

			toneTable.getChildren().add(text);
		}


	}
}
