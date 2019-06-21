package midianalyser.model;

import midianalyser.view.*;
import midianalyser.*;

import java.io.*;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;

public class Model{
    MidiLoader midiLoader;
    ArrayList<Integer> toneList;
    ArrayList<Integer> rhythmList;
    HashMap<String, Integer> mapOfTrochees;
    HashMap<String, Integer> mapOfDactyles;
    View view;

    public Model(View view){
        this.view = view;
        toneList = new ArrayList<Integer>();
        for(int i = 0; i < 12; i++) toneList.add(0);
        rhythmList = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++) rhythmList.add(0);
        mapOfTrochees = new HashMap<String, Integer>();
        mapOfDactyles = new HashMap<String, Integer>();
        midiLoader = new MidiLoader(toneList, rhythmList, mapOfTrochees, mapOfDactyles);
    }


    public void setDirectory(File dir) throws IOException{
        midiLoader.setDirectory(dir);
    }

    public void onToneButton(){
        midiLoader.countAll();
        toneList = midiLoader.listOfTones();
        rhythmList = midiLoader.listOfRhythms();
        mapOfTrochees = midiLoader.mapOfTrochees();
        mapOfDactyles = midiLoader.mapOfDactyles();
        view.updateGrids(toneList, rhythmList, mapOfTrochees, mapOfDactyles);
    }

    public ArrayList<Integer> getToneList(){
        return toneList;
    }

}
