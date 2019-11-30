package midianalyser.model;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class MidiLoaderTest{
    
    private MidiLoader ml;

    @Before
    public void Setup_Midi_Loader(){
        ArrayList<Integer> listOfTones = new ArrayList<Integer>();
        for(int i = 0; i < 12; i++) listOfTones.add(0);
        ArrayList<Integer> listOfRhythms = new ArrayList<Integer>();
        for(int i = 0; i < 30; i++) listOfRhythms.add(0);
        TreeMap<String, HashMap<Integer, Integer>> mapOfTrochees = new TreeMap<String, HashMap<Integer, Integer>>();
        TreeMap<String, HashMap<Integer, Integer>> mapOfDactyles = new TreeMap<String, HashMap<Integer, Integer>>();
        TreeMap<String, Integer> mapOfKeys = new TreeMap<String, Integer>();
        TreeMap<String, Integer> mapOfTimeSigs = new TreeMap<String, Integer>();

        ml = new MidiLoader(listOfTones, listOfRhythms, mapOfTrochees, mapOfDactyles, mapOfKeys, mapOfTimeSigs);
    }

    @Test
    public void Test_All_Registered_Rhytms(){
        try {
            System.out.println(getClass().getResource("/RhytmTest").getPath());
            ml.setDirectory(new File(getClass().getResource("/RhytmTest").getPath()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        for(int i = 1; i < 25; i++){
            ArrayList<MidiNote> quarter = ml.count(i);
            Assert.assertEquals((Integer)  i,(Integer) ml.rhythmCheck(quarter));
            System.out.println("got to"+i);
        }
    }

    @Test
    public void Test_All_Throcaic_possibilities(){
        //TODO: Implement
    }


} 