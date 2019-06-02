package midianalyser.model;


class MidiNote{

    private int note;
    private long startTick;
    private int songKey;
    private int length;

    MidiNote(int note, int startTick, int songKey){
        this.note = note;
        this.startTick = startTick;
        this.songKey = songKey;
    }

    void setEndTick(int endTick){
        length = endTick - startTick;
    }

}
