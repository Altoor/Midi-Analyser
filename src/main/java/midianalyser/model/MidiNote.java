package midianalyser.model;


class MidiNote{

    private int note;
    private long startTick;
    private int songKey;
    private long length;

    MidiNote(int note, long startTick, int songKey){
        this.note = note;
        this.startTick = startTick;
        this.songKey = songKey;
    }

    void setEndTick(long endTick){
        length = endTick - startTick;
    }

    int note(){
        return note;
    }

    long startTick(){
        return startTick;
    }

    int songKey(){
        return songKey;
    }

    long length(){
        return length;
    }

}
