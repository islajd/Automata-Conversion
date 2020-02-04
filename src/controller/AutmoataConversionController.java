package controller;

import model.DFA;
import model.NFA;
import model.Transition;
import model.eNFA;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class AutmoataConversionController {
    public static DFA convertToDFA(NFA a){
        Queue<ArrayList<String>> q = new LinkedList();
        ArrayList<ArrayList<String>> states = new ArrayList<>();
        ArrayList<Transition> transitions = new ArrayList<>();

        ArrayList<String> initial_state = new ArrayList<>();
        initial_state.add(a.initial_state);
        //shtimi ne radhe i bashkesise me gjendjen fillestare
        states.add(initial_state);
        q.add(initial_state);

        //per sa kohe qe radha ka gjendje te reja
        while(!q.isEmpty()){
            ArrayList<String> tmp = q.remove();

            for(String al:a.alphabet){
                ArrayList<String> reach = getReachableStates(a,tmp, al);
                if(!isVisited(states,reach)){
                    q.add(reach);
                    states.add(reach);
                }
                transitions.add(new Transition(""+tmp,""+reach,al));
            }
        }

        ArrayList<ArrayList<String>> final_states = new ArrayList<>();
        for(ArrayList<String> state:states){
            for(String s:a.final_states) {
                if (state.contains(s)) {
                    final_states.add(state);
                    break;
                }
            }
        }
        return new DFA(a.alphabet,states,initial_state,final_states,transitions);
    }

    private static boolean isVisited(ArrayList<ArrayList<String>> states, ArrayList<String> reach) {
        for(ArrayList<String> state:states){
            if(state.equals(reach))
                return true;
        }
        return false;
    }

    private static ArrayList<String> getReachableStates(NFA a,ArrayList<String> from, String alphabet){
        ArrayList<String> states = new ArrayList<>();
        for(String f:from) {
            for (Transition t : a.transitionList) {
                if (t.from.equals(f) && t.alphabet.equals(alphabet) && !states.contains(t.to)) {
                    states.add(t.to);
                }
            }
        }
        return states;
    }

    public static NFA convertToNFA(eNFA a){
        ArrayList<Transition> transitions = new ArrayList<>();
        ArrayList<String> finalStates = new ArrayList<>();
        String[] newAlphabet = new String[a.alphabet.length - 1];
        for(String state:a.states){
            //epsilon closure i pare i nje gjendje cfardo
            ArrayList<String> eClosure = getEpsillonClosure(a,state,new ArrayList<>());
            if(containFinal(a,eClosure)){
                finalStates.add(state);
            }// shtim i gjendjeve finale, nese me epsilon kalojne ne gjendje fundore
            for(int i = 0; i < a.alphabet.length-1; i++) {
                ArrayList<String> visited = new ArrayList<>();
                String alpha = a.alphabet[i];
                newAlphabet[i] = alpha;
                for(String e:eClosure){
                    //gjendjet e arriteshme nga epsilon closure per cdo shkronje alfabeti
                    ArrayList<String> reachable = getReachableStates(a,e,alpha);
                    for(String r:reachable){
                        //kerkimi i epsilon mbylljes se dyte dhe shtimi ne gjendjet e arritshme
                        ArrayList<String> eClosure2 = getEpsillonClosure(a,r,new ArrayList<>());
                        visited.addAll(eClosure2);
                    }
                }
                for(String reach : visited) {
                    transitions.add(new Transition(state, reach, alpha));
                }
            }
        }
        a.final_states = convertToArray(finalStates);
        NFA B = null;
        try {
            B = new NFA(newAlphabet,a.states,a.initial_state,a.final_states,transitions);
        }
        catch (Exception e){

        }
        return B;
    }

    private static String[] convertToArray(ArrayList<String> s) {
        String[] newS = new String[s.size()];
        for(int i = 0 ; i < s.size(); i ++){
            newS[i] = s.get(i);
        }
        return newS;
    }

    private static boolean containFinal(eNFA a,ArrayList<String> eClosure) {
        for(String s:eClosure){
            if(inArray(s,a.final_states)){
                return true;
            }
        }
        return false;
    }

    private static ArrayList<String> getEpsillonClosure(eNFA a,String state,ArrayList<String> visited){
        visited.add(state);
        for(Transition t:a.transitionList){
            if(t.from.equals(state) && t.alphabet.equals("@") && !visited.contains(t.to)){
                visited = getEpsillonClosure(a,t.to,visited);
            }
        }
        return visited;
    }

    private static ArrayList<String> getReachableStates(eNFA a,String from, String alphabet){
        ArrayList<String> states = new ArrayList<>();
        for (Transition t : a.transitionList) {
            if (t.from.equals(from) && t.alphabet.equals(alphabet) && !states.contains(t.to)) {
                states.add(t.to);
            }
        }
        return states;
    }

    /**
     * The function check if the given String is found in the given set of String.
     *
     * @param s String, the String to look for.
     * @param array String, set of Strings to be checked.
     * @return true if String is part of array and false if not.
     */
    private static boolean inArray(String s , String [] array){
        for(int i = 0 ; i < array.length;i++){
            if(array[i].equals(s)){
                return true;
            }
        }
        return false;
    }
}
