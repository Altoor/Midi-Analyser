package midianalyser.model;

import java.io.File;

import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import java.io.IOException;


import javafx.collections.ObservableList;


//TODO checkout https://stackoverflow.com/questions/3850688/reading-midi-files-in-java

public class MidiLoader{
    private File midiDirectory;
    private ObservableList<Integer> listOfTones;

    public MidiLoader(ObservableList<Integer> listOfTones){
        this.listOfTones = listOfTones;

        listOfTones.add(1);
    }

    public void setDirectory(File midiDirectory) throws IOException{
        this.midiDirectory = midiDirectory;
        clearAnalytics();
    }

    public void countTones(){
    }

    public void clearAnalytics(){

    }


}
