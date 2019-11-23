package midianalyser.model;
import java.io.File;
import java.io.FileFilter;

/**
 * @author Sebastian Bové Wagner <Sebastian@pda.dk>
 * @version 1.0
 */

public class MidiFileFilter implements FileFilter{

    public boolean accept(File file) {
                if(file.getName().endsWith(".midi") || file.getName().endsWith(".mid")) return true;
                return false;
    }
}
