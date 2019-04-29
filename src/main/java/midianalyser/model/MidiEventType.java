package midianalyser.model;

public enum MidiEventType {

    TRACKNAME       (0x3),
    END_OF_TRACK    (0x2F),
    SET_TEMPO       (0x51),
    TIME_SIGNATURE  (0x58),
    KEY_SIGNATURE   (0x59);


    private final int type;
    MidiEventType(int type){
        this.type = type;
    }

    public final int type(){ return type; }
}
