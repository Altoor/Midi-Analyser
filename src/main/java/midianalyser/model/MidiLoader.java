package midianalyser.model;

import java.io.File;

import java.util.*;

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
import javafx.collections.FXCollections;


//TODO checkout https://stackoverflow.com/questions/3850688/reading-midi-files-in-java

public class MidiLoader{
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    private File midiDirectory;
    private File[] midiFiles;
    private Sequence sequence;
    private ArrayList<Integer> listOfTones;
    private ArrayList<Integer> listOfRhythms;
    private TreeMap<String, HashMap<Integer, Integer>> mapOfTrochees;
    private TreeMap<String, HashMap<Integer, Integer>> mapOfDactyles;
    private TreeMap<String, Integer> mapOfKeys;
    private TreeMap<String, Integer> mapOfTimeSigs;

    private ObservableList<String> filterTimeSig = FXCollections.observableArrayList();
    private ObservableList<String> filterKeySig = FXCollections.observableArrayList();
    private ObservableList<String> filterMajorSig = FXCollections.observableArrayList();


    public MidiLoader(ArrayList<Integer> listOfTones, ArrayList<Integer> listOfRhythms, TreeMap<String, HashMap<Integer, Integer>> mapOfTrochees, TreeMap<String, HashMap<Integer, Integer>> mapOfDactyles, TreeMap<String, Integer> mapOfKeys, TreeMap<String, Integer> mapOfTimeSigs){
        this.listOfTones = listOfTones;
        this.listOfRhythms = listOfRhythms;
        this.mapOfTrochees = mapOfTrochees;
        this.mapOfDactyles = mapOfDactyles;
        this.mapOfKeys = mapOfKeys;
        this.mapOfTimeSigs = mapOfTimeSigs;

    }

    public void setLists(ArrayList<Integer> listOfTones, ArrayList<Integer> listOfRhythms, TreeMap<String, HashMap<Integer, Integer>> mapOfTrochees, TreeMap<String, HashMap<Integer, Integer>> mapOfDactyles, TreeMap<String, Integer> mapOfKeys, TreeMap<String, Integer> mapOfTimeSigs){
        this.listOfTones = listOfTones;
        this.listOfRhythms = listOfRhythms;
        this.mapOfTrochees = mapOfTrochees;
        this.mapOfDactyles = mapOfDactyles;
        this.mapOfKeys = mapOfKeys;
        this.mapOfTimeSigs = mapOfTimeSigs;
    }

    public void setDirectory(File midiDirectory) throws IOException{
        this.midiDirectory = midiDirectory;
        midiFiles = midiDirectory.listFiles(new MidiFileFilter());
        for(File file : midiFiles){
            System.out.println(file.getName());
        }
        clearAnalytics();
    }

    public void countAll(){
        for(File file : midiFiles){
            try{
                sequence = MidiSystem.getSequence(file);
            }catch(IOException | InvalidMidiDataException e){
                 System.out.println(e.getMessage());
                continue;
            }

            int trackNumber = 0;
            ArrayList<MidiEvent> metaMessages = new ArrayList();

            for (Track track :  sequence.getTracks()) {
                int tempo = 500000;
                int PPQ = sequence.getResolution();
                long currQuarterTick =0; // round down to nearest tick representing a quarter
                long PPQChangeTick =0; // The tick of the last played note.
                int keySig = 0;
                boolean majorKey = true;
                int timeSigNumerator = 4;
                int timeSigDenominator = 4;

                trackNumber++;
                for(MidiEvent me : metaMessages){
                    track.add(me);
                }

                ArrayList<MidiNote> simulNotes = new ArrayList<MidiNote>();
                ArrayList<MidiNote> quarter = new ArrayList<MidiNote>();
                ArrayList<MidiEvent> events = sortTrack(track);

                for (int i=0; i < events.size(); i++) {
                    MidiEvent event = events.get(i);
                    MidiMessage message = event.getMessage();
                    //System.out.print(" @" + event.getTick());

                    if (message instanceof ShortMessage) {
                        ShortMessage sm = (ShortMessage) message;
                        System.out.println(sm.getCommand());
                        if (sm.getCommand() == NOTE_ON && sm.getData2() != 0) {
                            int key = sm.getData1();
                            MidiNote note = new MidiNote(key,event.getTick(),keySig);

                            if(filterTimeSig.isEmpty() || filterTimeSig.contains(timeSigNumerator+"/"+timeSigDenominator)){
                                if(filterKeySig.isEmpty() || filterKeySig.contains(keyToString(keySigCheck(0,keySig,majorKey)))){
                                    if(filterMajorSig.isEmpty() || (filterMajorSig.contains("major") &&  majorKey) || (filterMajorSig.contains("minor") &&  !majorKey)){

                                        if(event.getTick() >= currQuarterTick + PPQ ){
                                            currQuarterTick = event.getTick()-((event.getTick()-PPQChangeTick) % PPQ);
                                            checkQuarter(quarter, keySig, majorKey);
                                            quarter.clear();
                                        }

                                    }
                                }
                            }



                            simulNotes.add(note);
                            quarter.add(note);

                        } else if (sm.getCommand() == NOTE_OFF || sm.getCommand() == NOTE_ON && sm.getData2() == 0) {
                            int key = sm.getData1();

                            for(int n = 0; n < simulNotes.size(); n++){
                                if(simulNotes.get(n).note() == key){
                                    int lengthInTicks = (int) (event.getTick() - simulNotes.get(n).startTick());
                                    //System.out.println("note: " + key);
                                    //System.out.println("lengthInTicks: " + lengthInTicks + ". PPQ: " + PPQ);
                                    simulNotes.get(n).setLength(0);
                                    for(double l = 1; l < 32; l +=0.5){
                                        if(lengthInTicks >= (int) (PPQ/l)-((PPQ/l)/4) && lengthInTicks <= (int) (PPQ/l)+((PPQ/l)/4)){
                                            simulNotes.get(n).setLength(l);
                                            break;
                                        }
                                    }
                                    //System.out.println("length" + simulNotes.get(n).length());
                                    simulNotes.remove(n);
                                }
                            }

                        }
                    }else if(message instanceof MetaMessage) {

                        MetaMessage mm = (MetaMessage) message;
                        int type = mm.getType();

                        System.out.println(type);

                        if(type == MidiEventType.TRACKNAME.type()){

                        }else if(type == MidiEventType.END_OF_TRACK.type()){
                            checkQuarter(quarter, keySig, majorKey);

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
                            PPQChangeTick = event.getTick();
                            currQuarterTick = event.getTick()-((event.getTick()-PPQChangeTick) % PPQ);
                            checkQuarter(quarter, keySig, majorKey);

                            if(! metaMessages.contains(event)){
                                if(filterTimeSig.isEmpty() || filterTimeSig.contains(timeSigNumerator+"/"+timeSigDenominator)){
                                    if(filterKeySig.isEmpty() || filterKeySig.contains(keySigCheck(0,keySig,majorKey))){
                                        if(filterMajorSig.isEmpty() || (filterMajorSig.contains("major") &&  majorKey) || (filterMajorSig.contains("minor") &&  !majorKey)){

                                            addTimeSig(timeSigNumerator, timeSigDenominator);

                                        }
                                    }
                                }

                            }

                        }else if(type == MidiEventType.KEY_SIGNATURE.type()){
                            keySig = mm.getData()[0];
                            if(mm.getData()[1] == 1) majorKey = false;
                            System.out.println("keySig :" +keySig);

                            if(! metaMessages.contains(event)){
                                if(filterTimeSig.isEmpty() || filterTimeSig.contains(timeSigNumerator+"/"+timeSigDenominator)){
                                    if(filterKeySig.isEmpty() || filterKeySig.contains(keySigCheck(0,keySig,majorKey))){
                                        if(filterMajorSig.isEmpty() || (filterMajorSig.contains("major") &&  majorKey) || (filterMajorSig.contains("minor") &&  !majorKey)){

                                            addKeySig(keySig, majorKey);
                                        }

                                    }
                                }

                            }

                        }
                        metaMessages.add(event);

                    }
                }
            }
        }
    }

    public ArrayList<MidiEvent> sortTrack(Track track){
        ArrayList<MidiEvent> eventlist = new ArrayList();
        for (int i=0; i < track.size(); i++) {
            eventlist.add(track.get(i));
        }

        Collections.sort(eventlist, new SortEventByTick());
        return eventlist;
    }

    public void checkQuarter(ArrayList<MidiNote> quarter, int keySig, boolean majorKey){
        Collections.sort(quarter, new SortByStartTick());
        System.out.println("notes in quarter: " +quarter.size());
        rhythmCheck(quarter);
        if(quarter.size() ==2){
            TrochaicCheck(quarter, keySig, majorKey);
        }else if(quarter.size() ==3){
            DactylCheck(quarter, keySig, majorKey);
        }
        for(MidiNote note : quarter){
            int noteFromKey = keySigCheck(note.note(), keySig, majorKey);
            listOfTones.set(noteFromKey,listOfTones.get(noteFromKey)+1);
        }


        quarter.clear();


    }

    public void rhythmCheck(ArrayList<MidiNote> quarter){

        switch(quarter.size()){
            case 1:
                listOfRhythms.set(0,listOfRhythms.get(0)+1);
                break;
            case 2:
                if(quarter.get(0).length() == 2.0 && quarter.get(1).length() == 2.0){
                    listOfRhythms.set(1,listOfRhythms.get(1)+1);
                }else if(quarter.get(0).length() == 1.5 && quarter.get(1).length() >= 3.0){
                    listOfRhythms.set(5,listOfRhythms.get(5)+1);
                }else if(quarter.get(0).length() >= 3.0 && quarter.get(1).length() == 1.5){
                    listOfRhythms.set(6,listOfRhythms.get(6)+1);
                }else if(quarter.get(0).length() == 1.5 && quarter.get(1).length() == 2.5){
                    listOfRhythms.set(8,listOfRhythms.get(8)+1);
                }
                break;
            case 3:
                if(quarter.get(0).length() == 2.5 && quarter.get(1).length() == 2.5 && quarter.get(2).length() == 2.5){
                    listOfRhythms.set(9,listOfRhythms.get(9)+1);
                }else if(quarter.get(0).length() == 2.0 && quarter.get(1).length() >= 5.0){
                    listOfRhythms.set(10,listOfRhythms.get(10)+1);
                }else if(quarter.get(0).length() >= 5.0 && quarter.get(1).length() == 2.0){
                    listOfRhythms.set(11,listOfRhythms.get(11)+1);
                }else if(quarter.get(1).length() == 2.0 && quarter.get(2).length() >= 5.0){
                    listOfRhythms.set(12,listOfRhythms.get(12)+1);
                }else if(quarter.get(1).length() >= 5.0 && quarter.get(2).length() == 2.0){
                    listOfRhythms.set(13,listOfRhythms.get(13)+1);
                }else if(quarter.get(0).length() == 2.0){
                    listOfRhythms.set(2,listOfRhythms.get(2)+1);
                }else if(quarter.get(1).length() == 2.0 ){
                    listOfRhythms.set(3,listOfRhythms.get(3)+1);
                }else if(quarter.get(2).length() == 2.0){
                    listOfRhythms.set(4,listOfRhythms.get(4)+1);
                }

                break;
            case 4:
                if(quarter.get(0).length() == 3.5 && quarter.get(1).length() == 3.5 && quarter.get(2).length() == 3.5 && quarter.get(3).length() == 3.5){
                    listOfRhythms.set(7,listOfRhythms.get(7)+1);
                }else if( quarter.get(2).length() == 2.5 && quarter.get(3).length() == 2.5){
                    listOfRhythms.set(14,listOfRhythms.get(14)+1);
                }else if( quarter.get(2).length() == 2.0){
                    listOfRhythms.set(15,listOfRhythms.get(15)+1);
                }else if( quarter.get(3).length() == 2.0){
                    listOfRhythms.set(16,listOfRhythms.get(16)+1);
                }else if( quarter.get(0).length() == 2.5 && quarter.get(3).length() == 2.5){
                    listOfRhythms.set(19,listOfRhythms.get(19)+1);
                }else if( quarter.get(0).length() == 2.5 && quarter.get(1).length() == 2.5){
                    listOfRhythms.set(21,listOfRhythms.get(21)+1);
                }else if( quarter.get(0).length() == 2.0 ){
                    listOfRhythms.set(22,listOfRhythms.get(22)+1);
                }else if( quarter.get(1).length() == 2.0){
                    listOfRhythms.set(23,listOfRhythms.get(23)+1);
                }

                break;

            case 5:
                if(quarter.get(4).length() == 2.5){
                    listOfRhythms.set(17,listOfRhythms.get(17)+1);
                }else if(quarter.get(0).length() == 2.5){
                    listOfRhythms.set(20,listOfRhythms.get(20)+1);
                }
                break;

            case 6:
                listOfRhythms.set(18,listOfRhythms.get(18)+1);
                break;
            default:
        }

    }

    public void TrochaicCheck(ArrayList<MidiNote> quarter, int keySig, boolean majorKey){
        int diff =halfToneToTone(quarter.get(0).note(), quarter.get(1).note());
        String sharpNotater = "";
        if (diff< 0) sharpNotater = ".";

        String key = 1+ "" + Math.abs(diff) + sharpNotater;

        if(mapOfTrochees.get(key) == null){
            HashMap<Integer, Integer> newHM = new HashMap<Integer, Integer>();
            newHM.put(halfToneToTone(0,keySigCheck(quarter.get(0).note(),keySig, majorKey)), 1);
            mapOfTrochees.put(key,newHM);

        }else{
            int count = 0;
            if(mapOfTrochees.get(key).get(halfToneToTone(0,keySigCheck(quarter.get(0).note(),keySig, majorKey))) != null){
                count = mapOfTrochees.get(key).get(halfToneToTone(0,keySigCheck(quarter.get(0).note(),keySig, majorKey)));
            }

            mapOfTrochees.get(key).put(halfToneToTone(0,keySigCheck(quarter.get(0).note(),keySig, majorKey)),count+1);
        }

    }

    public void DactylCheck(ArrayList<MidiNote> quarter, int keySig, boolean majorKey){
        int diff1 =halfToneToTone(quarter.get(0).note(), quarter.get(1).note());
        int diff2 =halfToneToTone(quarter.get(0).note(), quarter.get(2).note());
        String sharpNotater1 = "";
        String sharpNotater2= "";
        if (diff1< 0) sharpNotater1 = ".";
        if (diff2< 0) sharpNotater2 = ".";

        String key = 1+ "" + Math.abs(diff1) + sharpNotater1+ Math.abs(diff2) + sharpNotater2;

        if(mapOfDactyles.get(key) == null){
            HashMap<Integer, Integer> newHM = new HashMap<Integer, Integer>();
            newHM.put(halfToneToTone(0,keySigCheck(quarter.get(0).note(),keySig, majorKey)), 1);

            mapOfDactyles.put(key,newHM);
        }else{

            int count = 0;
            if(mapOfDactyles.get(key).get(halfToneToTone(0,keySigCheck(quarter.get(0).note(),keySig, majorKey))) != null){
                count = mapOfDactyles.get(key).get(halfToneToTone(0,keySigCheck(quarter.get(0).note(),keySig, majorKey)));
            }

            mapOfDactyles.get(key).put(halfToneToTone(0,keySigCheck(quarter.get(0).note(),keySig, majorKey)),count+1);
        }

    }

    public String keyToString(int keySig){
        String key = "";
        switch(keySig){
            case 0: key = "c"; break;
            case 1: key = "c#"; break;
            case 2: key = "d"; break;
            case 3: key = "d#"; break;
            case 4: key = "e"; break;
            case 5: key = "f"; break;
            case 6: key = "f#"; break;
            case 7: key = "g"; break;
            case 8: key = "g#"; break;
            case 9: key = "a"; break;
            case 10: key = "a#"; break;
            case 11: key = "b"; break;
            case 12: key = "c"; break;
            default: key = "c"; break;
        }
        return key;
    }

    public void addKeySig(int keySig, boolean majorKey){
        String key = "" +keySigCheck(0,keySig,majorKey);
        if(majorKey){
            key += " major";
        }else{
            key += " minor";
        }

        if(mapOfKeys.get(key) == null){
            mapOfKeys.put(key,1);
        }else{
            mapOfKeys.put(key,mapOfKeys.get(key)+1);
        }
    }

    public void addTimeSig(int timeSigNumerator, int timeSigDenominator){
        String key = timeSigNumerator + "/" + timeSigDenominator;
        if(mapOfTimeSigs.get(key) == null){
            mapOfTimeSigs.put(key,1);
        }else{
            mapOfTimeSigs.put(key,mapOfTimeSigs.get(key)+1);
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

    public TreeMap<String,HashMap<Integer, Integer>> mapOfTrochees(){
        return mapOfTrochees;
    }

    public TreeMap<String,HashMap<Integer, Integer>> mapOfDactyles(){
        return mapOfDactyles;
    }

    public TreeMap<String, Integer> mapOfKeys(){
        return mapOfKeys;
    }

    public TreeMap<String, Integer> mapOfTimeSigs(){
        return mapOfTimeSigs;
    }

    public void filterTimeSig(ObservableList<String> items){
        this.filterTimeSig = items;
    }

    public void filterKeySig(ObservableList<String> items){
        this.filterKeySig = items;
    }

    public void filterMajorSig(ObservableList<String> items){
        this.filterMajorSig = items;
    }


}

class SortEventByTick implements Comparator<MidiEvent>{

        public int compare(MidiEvent a, MidiEvent b){
            int out = 0;
            if(a.getTick() < b.getTick()){
                out = -1;
            }else if(a.getTick() > b.getTick()){
                out = 1;
            }
            return out;
        }
}
