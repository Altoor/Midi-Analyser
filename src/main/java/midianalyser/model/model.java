package midianalyser.model;

import java.io.*;
import java.util.*;

import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener;
import javafx.collections.FXCollections;

public class Model{
    MidiLoader midiLoader;
    ObservableList<Integer> ToneList;

    public Model(){

        ToneList = FXCollections.observableList(new ArrayList<Integer>());
        ToneList.addListener(new ListChangeListener() {

            @Override
            public void onChanged(ListChangeListener.Change change) {
            }
        });

        midiLoader = new MidiLoader(ToneList);
    }

    public void setDirectory(File dir) throws IOException{
        midiLoader.setDirectory(dir);
    }

    public void onToneButton(){
        midiLoader.countTones();
    }

    public ObservableList<Integer> getToneList(){
        return ToneList;
    }

}
