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
	private GridPane rhythmTable;
	private GridPane rhythmTable1;
	private GridPane rhythmTable2;
	private GridPane trochaicTable;
	private GridPane dactylTable;


	public View(Controller controller, Stage primaryStage) throws IOException {
		controller.setStage(primaryStage);
		this.toneTable = controller.getToneTable();
		this.rhythmTable = controller.getRhythmTable();
		this.rhythmTable1 = controller.getRhythmTable1();
		this.rhythmTable2 = controller.getRhythmTable2();
		this.trochaicTable = controller.getTrochaicTable();
		this.dactylTable = controller.getDactylTable();
		primaryStage.show();
	}

	public void updateGrids(ArrayList<Integer> toneList, ArrayList<Integer> rhythmList, HashMap<String, Integer> mapOfTrochees, HashMap<String, Integer> mapOfDactyles){
		Text text = new Text();
		text.setTextAlignment(TextAlignment.CENTER);

		//toneTable
		toneTable.getChildren().clear();
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

		//rhytmTable
		rhythmTable.getChildren().clear();
		rhythmTable1.getChildren().clear();
		rhythmTable2.getChildren().clear();
		for(int i = 0; i < 10; i++){

			text = new Text("" +rhythmList.get(i));
			rhythmTable.setConstraints(text, 1, i);
			rhythmTable.getChildren().add(text);

			text = new Text("" +rhythmList.get(i+10));
			rhythmTable1.setConstraints(text, 1, i);
			rhythmTable1.getChildren().add(text);

			if(i+20 < rhythmList.size()){
				text = new Text("" +rhythmList.get(i+20));
				rhythmTable2.setConstraints(text, 1, i);
				rhythmTable2.getChildren().add(text);
			}

		}

		//rhytmTable
		trochaicTable.getChildren().clear();
		int q = 0;
		for (Map.Entry<String, Integer> entry : mapOfTrochees.entrySet()) {

			text = new Text(entry.getKey());
			trochaicTable.setConstraints(text, 0, q);
			trochaicTable.getChildren().add(text);

			text = new Text("" +entry.getValue());
			trochaicTable.setConstraints(text, 1, q);
			trochaicTable.getChildren().add(text);
			q++;
		}

		dactylTable.getChildren().clear();
		q = 0;
		for (Map.Entry<String, Integer> entry : mapOfDactyles.entrySet()) {
			System.out.println(entry.getKey());
			text = new Text(entry.getKey());
			dactylTable.setConstraints(text, 0, q);
			dactylTable.getChildren().add(text);

			text = new Text("" +entry.getValue());
			dactylTable.setConstraints(text, 1, q);
			dactylTable.getChildren().add(text);
			q++;
		}



	}
}
