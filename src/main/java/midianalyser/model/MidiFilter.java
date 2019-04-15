package midianalyser.model;
import java.io.File;
import java.io.FileFilter;

public class MidiFilter implements FileFilter{

    public boolean accept(File file) {
                if(file.getName().endsWith(".midi") || file.getName().endsWith(".mid")) return true;
                return false;
    }
}
