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
	private GridPane keyTable;
	private GridPane timeSigTable;


	public View(Controller controller, Stage primaryStage) throws IOException {
		controller.setStage(primaryStage);
		this.toneTable = controller.getToneTable();
		this.rhythmTable = controller.getRhythmTable();
		this.rhythmTable1 = controller.getRhythmTable1();
		this.rhythmTable2 = controller.getRhythmTable2();
		this.trochaicTable = controller.getTrochaicTable();
		this.dactylTable = controller.getDactylTable();
		this.keyTable = controller.getKeyTable();
		this.timeSigTable = controller.getTimeSigTable();
		primaryStage.show();
	}

	public void updateGrids(ArrayList<Integer> toneList, ArrayList<Integer> rhythmList, TreeMap<String, HashMap<Integer, Integer>> mapOfTrochees, TreeMap<String, HashMap<Integer, Integer>> mapOfDactyles, TreeMap<String, Integer> mapOfKeys, TreeMap<String, Integer> mapOfTimeSigs){
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
		int total = 0;
		int superTotal = 0;
		ArrayList<Integer> stepsFromRootTotal = new ArrayList<Integer>();
		for(int i = 0; i < 7; i++){
			stepsFromRootTotal.add(0);
		}
		for (Map.Entry<String, HashMap<Integer, Integer>> entry : mapOfTrochees.entrySet()) {
			for(int i = 1; i < 8; i++){
				int entryInt = 0;
				if(entry.getValue().get(i) != null) entryInt = entry.getValue().get(i);
				superTotal += entryInt;
			}
		}
		for (Map.Entry<String, HashMap<Integer, Integer>> entry : mapOfTrochees.entrySet()) {
			text = new Text(entry.getKey());
			trochaicTable.setConstraints(text, 0, q);
			trochaicTable.getChildren().add(text);
			total = 0;

			for(int i = 1; i < 8; i++){
				int entryText = 0;
				if(entry.getValue().get(i) != null) entryText = entry.getValue().get(i);
				text = new Text("" + entryText);
				trochaicTable.setConstraints(text, i, q);
				trochaicTable.getChildren().add(text);
				total += entryText;
				stepsFromRootTotal.set(i-1, stepsFromRootTotal.get(i-1) +entryText);
			}

			text = new Text("" + total);
			trochaicTable.setConstraints(text, 9, q);
			trochaicTable.getChildren().add(text);

			text = new Text("" + (Math.round(((double) total/(double) superTotal*100)*100.0)/100.0)+"%");
			trochaicTable.setConstraints(text, 10, q);
			trochaicTable.getChildren().add(text);

			q++;
		}

		text = new Text("Totals");
		trochaicTable.setConstraints(text, 0, q);
		trochaicTable.getChildren().add(text);

		for(int i = 1; i < 8; i++){
			int entryInt = 0;
			if(stepsFromRootTotal.get(i-1) != null) entryInt = stepsFromRootTotal.get(i-1);
			text = new Text("" + entryInt);
			trochaicTable.setConstraints(text, i, q);
			trochaicTable.getChildren().add(text);
			total += entryInt;
		}

		text = new Text("" + total);
		trochaicTable.setConstraints(text, 9, q);
		trochaicTable.getChildren().add(text);

		q++;

		text = new Text("Totals %");
		trochaicTable.setConstraints(text, 0, q);
		trochaicTable.getChildren().add(text);

		for(int i = 1; i < 8; i++){
			int entryInt = 0;
			if(stepsFromRootTotal.get(i-1) != null) entryInt = stepsFromRootTotal.get(i-1);
			text = new Text("" + (Math.round(((double) entryInt/(double) superTotal*100)*100.0)/100.0)+"%");
			trochaicTable.setConstraints(text, i, q);
			trochaicTable.getChildren().add(text);
		}

		text = new Text("100%");
		trochaicTable.setConstraints(text, 9, q);
		trochaicTable.getChildren().add(text);





		dactylTable.getChildren().clear();
		q = 0;
		total = 0;
		superTotal = 0;
		stepsFromRootTotal = new ArrayList<Integer>();
		for(int i = 0; i < 7; i++){
			stepsFromRootTotal.add(0);
		}
		for (Map.Entry<String, HashMap<Integer, Integer>> entry : mapOfDactyles.entrySet()) {
			for(int i = 1; i < 8; i++){
				int entryInt= 0;
				if(entry.getValue().get(i) != null) entryInt = entry.getValue().get(i);
				superTotal += entryInt;
			}
		}

		for (Map.Entry<String, HashMap<Integer, Integer>> entry : mapOfDactyles.entrySet()) {
			text = new Text(entry.getKey());
			dactylTable.setConstraints(text, 0, q);
			dactylTable.getChildren().add(text);
			total = 0;
			for(int i = 1; i < 8; i++){
				int entryText = 0;
				if(entry.getValue().get(i) != null) entryText = entry.getValue().get(i);
				text = new Text("" + entryText);
				dactylTable.setConstraints(text, i, q);
				dactylTable.getChildren().add(text);
				total += entryText;
				stepsFromRootTotal.set(i-1, stepsFromRootTotal.get(i-1) +entryText);
			}
			text = new Text("" + total);
			dactylTable.setConstraints(text, 9, q);
			dactylTable.getChildren().add(text);

			text = new Text("" + (Math.round(((double) total/(double) superTotal*100)*100.0)/100.0)+"%");
			dactylTable.setConstraints(text, 10, q);
			dactylTable.getChildren().add(text);

			q++;
		}

		text = new Text("Totals");
		dactylTable.setConstraints(text, 0, q);
		dactylTable.getChildren().add(text);

		for(int i = 1; i < 8; i++){
			int entryInt = 0;
			if(stepsFromRootTotal.get(i-1) != null) entryInt = stepsFromRootTotal.get(i-1);
			text = new Text("" + entryInt);
			dactylTable.setConstraints(text, i, q);
			dactylTable.getChildren().add(text);
			total += entryInt;
		}

		text = new Text("" + total);
		dactylTable.setConstraints(text, 9, q);
		dactylTable.getChildren().add(text);

		q++;

		text = new Text("Totals %");
		dactylTable.setConstraints(text, 0, q);
		dactylTable.getChildren().add(text);

		for(int i = 1; i < 8; i++){
			int entryInt = 0;
			if(stepsFromRootTotal.get(i-1) != null) entryInt = stepsFromRootTotal.get(i-1);
			text = new Text("" + (Math.round(((double) entryInt/(double) superTotal*100)*100.0)/100.0)+"%");
			dactylTable.setConstraints(text, i, q);
			dactylTable.getChildren().add(text);
		}

		text = new Text("100%");
		dactylTable.setConstraints(text, 9, q);
		dactylTable.getChildren().add(text);




		keyTable.getChildren().clear();
		q = 0;
		superTotal = 0;
		for (Map.Entry<String, Integer> entry : mapOfKeys.entrySet()) {
			text = new Text(entry.getKey());
			keyTable.setConstraints(text, 0, q);
			keyTable.getChildren().add(text);

			text = new Text("" + entry.getValue());
			keyTable.setConstraints(text, 1, q);
			keyTable.getChildren().add(text);
			q++;
		}

		timeSigTable.getChildren().clear();
		q = 0;
		for (Map.Entry<String, Integer> entry : mapOfTimeSigs.entrySet()) {
			text = new Text(entry.getKey());
			timeSigTable.setConstraints(text, 0, q);
			timeSigTable.getChildren().add(text);

			text = new Text("" + entry.getValue());
			timeSigTable.setConstraints(text, 1, q);
			timeSigTable.getChildren().add(text);
			q++;
		}



	}
}
