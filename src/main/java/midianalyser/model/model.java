package midianalyser.model;

import java.io.*;
import java.util.*;

public class Model{

    List<Runnable> observers;

    public Model(List<String> args) throws IOException, ClassNotFoundException {
        observers = new ArrayList<>();
    }

}
