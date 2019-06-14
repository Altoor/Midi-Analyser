package midianalyser.model;

import java.util.Comparator;


public class MidiNote{

    private int note;
    private long startTick;
    private int songKey;
    private int length; //length denotes the fraction of a quarter node that this note is. 1 is therefore a quarter note, 2 is an eigth.

    public MidiNote(int note, long startTick, int songKey){
        this.note = note;
        this.startTick = startTick;
        this.songKey = songKey;
    }

    public void setLength(int length){
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

    public long length(){
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
