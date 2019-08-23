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
    TreeMap<String, HashMap<Integer, Integer>> mapOfTrochees;
    TreeMap<String, HashMap<Integer, Integer>> mapOfDactyles;
    TreeMap<String, Integer> mapOfKeys;
    TreeMap<String, Integer> mapOfTimeSigs;
    View view;

    public Model(View view){
        this.view = view;
        initList();
        midiLoader = new MidiLoader(toneList, rhythmList, mapOfTrochees, mapOfDactyles, mapOfKeys, mapOfTimeSigs);
    }

    public void initList(){
        toneList = new ArrayList<Integer>();
        for(int i = 0; i < 12; i++) toneList.add(0);
        rhythmList = new ArrayList<Integer>();
        for(int i = 0; i < 30; i++) rhythmList.add(0);
        mapOfTrochees = new TreeMap<String, HashMap<Integer, Integer>>();
        mapOfDactyles = new TreeMap<String, HashMap<Integer, Integer>>();
        mapOfKeys = new TreeMap<String, Integer>();
        mapOfTimeSigs = new TreeMap<String, Integer>();
    }


    public void setDirectory(File dir) throws IOException{
        int fileAmount = midiLoader.setDirectory(dir);
        view.updateLabels(dir.getName().substring(dir.getName().lastIndexOf("/") + 1),fileAmount);
    }

    public void onToneButton(){
        initList();
        midiLoader.setLists(toneList, rhythmList, mapOfTrochees, mapOfDactyles, mapOfKeys, mapOfTimeSigs);
        midiLoader.countAll();
        toneList = midiLoader.listOfTones();
        rhythmList = midiLoader.listOfRhythms();
        mapOfTrochees = midiLoader.mapOfTrochees();
        mapOfDactyles = midiLoader.mapOfDactyles();
        mapOfKeys = midiLoader.mapOfKeys();
        mapOfTimeSigs = midiLoader.mapOfTimeSigs();

        view.updateGrids(toneList, rhythmList, mapOfTrochees, mapOfDactyles, mapOfKeys, mapOfTimeSigs);
    }

    public ArrayList<Integer> getToneList(){
        return toneList;
    }

    public void filterTimeSig(ObservableList<String> items){
        midiLoader.filterTimeSig(items);
    }

    public void filterKeySig(ObservableList<String> items){
        midiLoader.filterKeySig(items);
    }

    public void filterMajorSig(ObservableList<String> items){
        midiLoader.filterMajorSig(items);
    }

}
