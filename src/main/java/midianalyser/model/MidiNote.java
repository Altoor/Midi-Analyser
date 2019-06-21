package midianalyser.model;

import java.util.Comparator;


public class MidiNote{

    private int note;
    private long startTick;
    private int songKey;
    private double length; //length denotes the length in ticks

    public MidiNote(int note, long startTick, int songKey){
        this.note = note;
        this.startTick = startTick;
        this.songKey = songKey;
    }

    public void setLength(double length){
        this.length = length;
    }

    public int note(){
        return note;
    }

    public long startTick(){
        return startTick;
    }

    public int songKey(){
        return songKey;
    }

    public double length(){
        return length;
    }

}

class SortByStartTick implements Comparator<MidiNote>
{

    public int compare(MidiNote a, MidiNote b)
    {
        return (int) (a.startTick() - b.startTick());
    }
}
