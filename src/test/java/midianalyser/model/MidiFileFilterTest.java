package midianalyser.model;

import java.io.File;

import org.junit.*;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class MidiFileFilterTest {

    @DataPoint
    public static File testFile1 = new File("test.mid");

    @DataPoint
    public static File testFile2 = new File("test.mid");

    @Theory
    public void Test_True_if_mid_and_midi_file(File testFile) {
        MidiFileFilter ff = new MidiFileFilter();
        Assert.assertTrue(ff.accept(testFile));
    }

    @Test
    public void Test_False_if_not_mid_and_midi_file() {
        File testFile = new File("test.mio");
        MidiFileFilter ff = new MidiFileFilter();
        Assert.assertFalse(ff.accept(testFile));
    }

}