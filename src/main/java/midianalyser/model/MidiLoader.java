package midianalyser.model;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.lang.Math;

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
    private HashMap<String, Integer> mapOfTrochees;
    private HashMap<String, Integer> mapOfDactyles;


    public MidiLoader(ArrayList<Integer> listOfTones, ArrayList<Integer> listOfRhythms, HashMap<String, Integer> mapOfTrochees, HashMap<String, Integer> mapOfDactyles){
        this.listOfTones = listOfTones;
        this.listOfRhythms = listOfRhythms;
        this.mapOfTrochees = mapOfTrochees;
        this.mapOfDactyles = mapOfDactyles;

    }

    public void setLists(ArrayList<Integer> listOfTones, ArrayList<Integer> listOfRhythms, HashMap<String, Integer> mapOfTrochees, HashMap<String, Integer> mapOfDactyles){
        this.listOfTones = listOfTones;
        this.listOfRhythms = listOfRhythms;
        this.mapOfTrochees = mapOfTrochees;
        this.mapOfDactyles = mapOfDactyles;
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
            boolean majorKey = true;
            int timeSigNumerator = 4;
            int timeSigDenominator = 4;


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
                        if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                            int key = sm.getData1();
                            MidiNote note = new MidiNote(key,event.getTick(),keySig);

                            if(event.getTick() >= currQuarterTick + PPQ){
                                currQuarterTick = event.getTick()-(event.getTick() % PPQ);

                                Collections.sort(quarter, new SortByStartTick());
                                rhythmCheck(quarter);
                                if(quarter.size() ==2){
                                    TrochaicCheck(quarter);
                                }else if(quarter.size() ==3){
                                    DactylCheck(quarter);
                                }
                                quarter.clear();
                            }
                            simulNotes.add(note);
                            quarter.add(note);

                            int noteFromKey = keySigCheck(note.note(), keySig, majorKey);

                            listOfTones.set(noteFromKey,listOfTones.get(noteFromKey)+1);
                        } else if (sm.getCommand() == NOTE_OFF || sm.getCommand() == NOTE_ON && sm.getData2() == 0) {
                            int key = sm.getData1();

                            for(int n = 0; n < simulNotes.size(); n++){
                                if(simulNotes.get(n).note() == key){
                                    int lengthInTicks = (int) (event.getTick() - simulNotes.get(n).startTick());
                                    System.out.println("note: " + key);
                                    System.out.println("lengthInTicks: " + lengthInTicks + ". PPQ: " + PPQ);
                                    simulNotes.get(n).setLength(0);
                                    for(double l = 1; l < 32; l +=0.5){
                                        if(lengthInTicks >= (int) (PPQ/l)-((PPQ/l)/5) && lengthInTicks < (int) (PPQ/l)+((PPQ/l)/5)){
                                            simulNotes.get(n).setLength(l);
                                            break;
                                        }
                                    }
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
                            timeSigNumerator = mm.getData()[0];
                            timeSigDenominator = (int) Math.pow(2,mm.getData()[1]);
                            if(timeSigDenominator == 8) PPQ = (int) Math.round(PPQ* 1.5);
                            System.out.println("timeSig :" +timeSigNumerator + "/" + timeSigDenominator);

                        }else if(type == MidiEventType.KEY_SIGNATURE.type()){
                            keySig = mm.getData()[0];
                            if(mm.getData()[1] == 1) majorKey = false;
                            System.out.println("keySig :" +keySig);
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
                if(quarter.get(0).length() == 2.0 && quarter.get(1).length() == 2.0){
                    listOfRhythms.set(1,listOfRhythms.get(1)+1);
                }else if(quarter.get(0).length() == 1.5 && quarter.get(1).length() == 3.5){
                    listOfRhythms.set(2,listOfRhythms.get(2)+1);
                }else if(quarter.get(0).length() == 3.5 && quarter.get(1).length() == 1.5){
                    listOfRhythms.set(3,listOfRhythms.get(3)+1);
                }
                break;
            case 3:
                if(quarter.get(0).length() == 3.0 && quarter.get(1).length() == 3.0 && quarter.get(2).length() == 3.0){
                    listOfRhythms.set(4,listOfRhythms.get(4)+1);
                }else if(quarter.get(0).length() == 2.0 && quarter.get(1).length() == 3.5 && quarter.get(2).length() == 3.5){
                    listOfRhythms.set(5,listOfRhythms.get(5)+1);
                }else if(quarter.get(0).length() == 3.5 && quarter.get(1).length() == 3.5 && quarter.get(2).length() == 2.0){
                    listOfRhythms.set(6,listOfRhythms.get(6)+1);
                }else if(quarter.get(0).length() == 3.5 && quarter.get(1).length() == 2.0 && quarter.get(2).length() == 3.5){
                    listOfRhythms.set(6,listOfRhythms.get(6)+1);
                }

                break;
            case 4:
                listOfRhythms.set(8,listOfRhythms.get(8)+1);
                break;
            default:
                listOfRhythms.set(9,listOfRhythms.get(9)+1);
        }

    }

    public void TrochaicCheck(ArrayList<MidiNote> quarter){
        int diff =halfToneToTone(quarter.get(0).note(), quarter.get(1).note());
        String sharpNotater = "";
        if (diff< 0) sharpNotater = ".";

        String key = 1+ "" + Math.abs(diff) + sharpNotater;

        if(mapOfTrochees.get(key) == null){
            mapOfTrochees.put(key,1);
        }else{
            mapOfTrochees.put(key,mapOfTrochees.get(key)+1);
        }

    }

    public void DactylCheck(ArrayList<MidiNote> quarter){
        int diff1 =halfToneToTone(quarter.get(0).note(), quarter.get(1).note());
        int diff2 =halfToneToTone(quarter.get(0).note(), quarter.get(2).note());
        String sharpNotater1 = "";
        String sharpNotater2= "";
        if (diff1< 0) sharpNotater1 = ".";
        if (diff2< 0) sharpNotater2 = ".";

        String key = 1+ "" + Math.abs(diff1) + sharpNotater1+ Math.abs(diff2) + sharpNotater2;

        if(mapOfDactyles.get(key) == null){
            mapOfDactyles.put(key,1);
        }else{
            mapOfDactyles.put(key,mapOfDactyles.get(key)+1);
        }

    }

    public int keySigCheck(int node, int keySig, boolean majorKey){
            int keyNote = 0;
        if(!majorKey){
            keyNote = 9;
        }
        keyNote = (keyNote+(7*keySig)) %12;

        node %= 12;
        if(node < keyNote) node += 12;


        return (node-keyNote)%12;

    }

    public int halfToneToTone(int firstNote, int secondNote){
        int halfToneDifference = secondNote-firstNote;
        int toneDifference = 1;
        switch(Math.abs(halfToneDifference)){
            case 0: toneDifference = 1; break;
            case 1: toneDifference = 2; break;
            case 2: toneDifference = 2; break;
            case 3: toneDifference = 3; break;
            case 4: toneDifference = 3; break;
            case 5: toneDifference = 4; break;
            case 6: toneDifference = 5; break;
            case 7: toneDifference = 5; break;
            case 8: toneDifference = 6; break;
            case 9: toneDifference = 6; break;
            case 10: toneDifference = 7; break;
            case 11: toneDifference = 7; break;
            case 12: toneDifference = 8; break;
            default: toneDifference = 1; break;
        }

        if(halfToneDifference < 0){
            return -toneDifference;
        }else{
            return toneDifference;
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

    public HashMap<String,Integer> mapOfTrochees(){
        return mapOfTrochees;
    }

    public HashMap<String,Integer> mapOfDactyles(){
        return mapOfDactyles;
    }


}
