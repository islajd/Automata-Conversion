package model;

import java.util.ArrayList;

public class Transition {
    public String from;
    public String to;
    public ArrayList<String> fromAL;
    public ArrayList<String> toAL;
    public String alphabet;
    public Transition(String from, String to, String alphabet){
        this.from = from;
        this.to = to;
        this.alphabet = alphabet;
    }
    public Transition(ArrayList<String> from, ArrayList<String> to, String alphabet){
        this.fromAL = from;
        this.toAL = to;
        this.alphabet = alphabet;
    }
}
