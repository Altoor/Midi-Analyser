package midianalyser.model;

import java.io.File;

import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Track;
import javax.sound.midi.InvalidMidiDataException;

import java.io.IOException;


import javafx.collections.ObservableList;


//TODO checkout https://stackoverflow.com/questions/3850688/reading-midi-files-in-java

public class MidiLoader{
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    private File midiDirectory;
    private File[] midiFiles;
    private Sequence sequence;
    private ArrayList<Integer> listOfTones;

    public MidiLoader(ArrayList<Integer> listOfTones){
        this.listOfTones = listOfTones;

    }

    public void setDirectory(File midiDirectory) throws IOException{
        this.midiDirectory = midiDirectory;
        midiFiles = midiDirectory.listFiles(new MidiFileFilter());
        for(File file : midiFiles){
            System.out.println(file.getName());
        }
        clearAnalytics();
    }

    public ArrayList<Integer> countTones(){
        for(File file : midiFiles){
            try{
                sequence = MidiSystem.getSequence(file);
            }catch(IOException | InvalidMidiDataException e){
                 System.out.println(e.getMessage());
                continue;
            }

            int trackNumber = 0;
            int tempo = 500000;
            int PPQ = sequence.getResolution();
            for (Track track :  sequence.getTracks()) {
                trackNumber++;
                ArrayList<MidiNote> simulNotes = new ArrayList<MidiNote>();


                for (int i=0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();

                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        if (sm.getCommand() == NOTE_ON) {
                            int key = sm.getData1();
                            int octave = (key / 12)-1;
                            int note = key % 12;


                            simulNotes.add(note);


                            listOfTones.set(note,listOfTones.get(note)+1);
                        } else if (sm.getCommand() == NOTE_OFF) {
                            int key = sm.getData1();
                            int octave = (key / 12)-1;
                            int note = key % 12;
                        }
                    }else if(message instanceof MetaMessage) {
                        MetaMessage mm = (MetaMessage) message;
                        int type = mm.getType();
                        System.out.println(type);

                        if(type == MidiEventType.TRACKNAME.type()){

                        }else if(type == MidiEventType.END_OF_TRACK.type()){

                        }else if(type == MidiEventType.SET_TEMPO.type()){
                            System.out.print("SET_TEMPO: ");
                            int out = 0;
                            for(byte bt : mm.getData()){
                                out += bt;
                                out <<= 8;
                            }
                            tempo = out;

                        }else if(type == MidiEventType.TIME_SIGNATURE.type()){

                        }else if(type == MidiEventType.KEY_SIGNATURE.type()){

                        }

                    }
                }
            }
        }
        return listOfTones;



    }

    public void clearAnalytics(){

    }


}
