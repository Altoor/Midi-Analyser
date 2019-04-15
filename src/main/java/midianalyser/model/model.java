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
    View view;

    public Model(View view){
        this.view = view;
        toneList = new ArrayList<Integer>();
        for(int i = 0; i < 12; i++) toneList.add(0);

        midiLoader = new MidiLoader(toneList);
    }


    public void setDirectory(File dir) throws IOException{
        midiLoader.setDirectory(dir);
    }

    public void onToneButton(){
        toneList = midiLoader.countTones();
        view.updateGrids(toneList);
    }

    public ArrayList<Integer> getToneList(){
        return toneList;
    }

}
