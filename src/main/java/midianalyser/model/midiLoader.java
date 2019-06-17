package midianalyser.model;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;


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
    private ArrayList<Integer> listOfRhythms;
    private ArrayList<Integer> listOfTrochees;
    private ArrayList<Integer> listOfDactyles;


    public MidiLoader(ArrayList<Integer> listOfTones, ArrayList<Integer> listOfRhythms){
        this.listOfTones = listOfTones;
        this.listOfRhythms = listOfRhythms;

    }

    public void setDirectory(File midiDirectory) throws IOException{
        this.midiDirectory = midiDirectory;
        midiFiles = midiDirectory.listFiles(new MidiFileFilter());
        for(File file : midiFiles){
            System.out.println(file.getName());
        }
        clearAnalytics();
    }

    public ArrayList<Integer> countAll(){
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
            long currQuarterTick =0; // round down to nearest tick representing a quarter
            int keySig = 0;

            for (Track track :  sequence.getTracks()) {
                trackNumber++;
                ArrayList<MidiNote> simulNotes = new ArrayList<MidiNote>();
                ArrayList<MidiNote> quarter = new ArrayList<MidiNote>();

                for (int i=0; i < track.size(); i++) {
                    MidiEvent event = track.get(i);
                    MidiMessage message = event.getMessage();

                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        System.out.println(sm.getCommand());
                        if (sm.getCommand() == NOTE_ON) {
                            int key = sm.getData1();
                            int octave = (key / 12)-1;
                            MidiNote note = new MidiNote(key % 12,event.getTick(),keySig);

                            if(event.getTick() >= currQuarterTick + PPQ){
                                currQuarterTick = event.getTick()-(event.getTick() % PPQ);

                                Collections.sort(quarter, new SortByStartTick());
                                rhythmCheck(quarter);
                                quarter.clear();
                            }
                            simulNotes.add(note);
                            quarter.add(note);

                            listOfTones.set(note.note(),listOfTones.get(note.note())+1);
                        } else if (sm.getCommand() == NOTE_OFF) {
                            int key = sm.getData1();
                            int octave = (key / 12)-1;
                            int note = key % 12;

                            for(int n = 0; n < simulNotes.size(); n++){
                                if(simulNotes.get(n).note() == note){
                                    int lengthInTicks = (int) (event.getTick() - simulNotes.get(n).startTick());
                                    System.out.println("lengthInTicks: " + lengthInTicks + ". PPQ: " + PPQ);
                                    simulNotes.get(n).setLength(0);
                                    for(int l = 1; l < 32; l ++){
                                        if(lengthInTicks >= (int) (PPQ/l)-((PPQ/l)/5) && lengthInTicks < (int) (PPQ/l)+((PPQ/l)/5)){
                                            simulNotes.get(n).setLength(l);
                                            break;
                                        }
                                    }
                                    System.out.println("length: " + simulNotes.get(n).length());
                                    simulNotes.remove(n);
                                }
                            }

                        }
                    }else if(message instanceof MetaMessage) {
                        MetaMessage mm = (MetaMessage) message;
                        int type = mm.getType();
                        //System.out.println(type);

                        if(type == MidiEventType.TRACKNAME.type()){

                        }else if(type == MidiEventType.END_OF_TRACK.type()){

                        }else if(type == MidiEventType.SET_TEMPO.type()){
                            int out = 0;
                            for(byte bt : mm.getData()){
                                out += bt;
                                out <<= 8;
                            }
                            tempo = out;

                        }else if(type == MidiEventType.TIME_SIGNATURE.type()){

                        }else if(type == MidiEventType.KEY_SIGNATURE.type()){
                            keySig = mm.getData()[0];
                            if(mm.getData()[1] == 1) keySig = 0-keySig;
                            //System.out.println(mm.getData()[0] + " + " +mm.getData()[1]);
                        }

                    }
                }
            }
        }
        return listOfTones;
    }

    public void rhythmCheck(ArrayList<MidiNote> quarter){
        switch(quarter.size()){
            case 1:
                listOfRhythms.set(0,listOfRhythms.get(0)+1);
                break;
            case 2:
                if(quarter.get(0).length() == 2 && quarter.get(1).length() == 2){
                    listOfRhythms.set(1,listOfRhythms.get(1)+1);
                }else if(quarter.get(0).length() == 2 && quarter.get(1).length() == 2){

                }

                break;
            case 3:
                listOfRhythms.set(4,listOfRhythms.get(4)+1);
                break;
            case 4:
                listOfRhythms.set(8,listOfRhythms.get(8)+1);
                break;
            default:
                listOfRhythms.set(9,listOfRhythms.get(9)+1);
        }

    }

    public void clearAnalytics(){

    }

    public ArrayList<Integer> listOfTones(){
        return listOfTones;
    }

    public ArrayList<Integer> listOfRhythms(){
        return listOfRhythms;
    }

    public ArrayList<Integer> listOfTrochees(){
        return listOfTrochees;
    }

    public ArrayList<Integer> listOfDactyles(){
        return listOfDactyles;
    }


}
