package model;

import java.util.ArrayList;
import java.util.Arrays;

public class DFA {
    public String[] alphabet;
    public ArrayList<String> states;
    public String initial_state;
    public ArrayList<String> final_states;
    public ArrayList<Transition> transitionList;

    public DFA(String[] alphabet, ArrayList<String> states, String initial_state, ArrayList<String> final_states, ArrayList<Transition> transitionList) {
        this.alphabet = alphabet;
        this.states = states;
        this.initial_state = initial_state;
        this.final_states = final_states;
        this.transitionList = transitionList;
    }

    public DFA(String alphabet, String states, String transitions, String initial_state, String final_states) throws Exception {
        this.states = new ArrayList<>();
        this.final_states = new ArrayList<>();
        this.transitionList = new ArrayList<>();

        this.states.addAll(Arrays.asList(states.split(",")));

        this.final_states.addAll(Arrays.asList(final_states.split(",")));

        if(!checkGoal()){
            throw new Exception("Invalid final states " + final_states);
        }

        this.alphabet = alphabet.split(",");

        this.initial_state = initial_state;

        if(!checkStart()){
            throw new Exception("Invalid start state " + initial_state);
        }

        String[] tempTransitions = transitions.split(",");

        for(String transition : tempTransitions){
            String [] transitionArray = transition.split("-");
            if(transitionArray.length != 3){
                throw new Exception("Invalid transition. Transitions should be of size 3");
            }
            for ( int i = 0 ; i < 3 ; i++){
                if(i == 1)
                    continue;
                if(!states.contains(transitionArray[i])){
                    throw new Exception("Invalid transition. " + transitionArray[i] + " is not included in the states.");
                }
            }
            if(!inArray(transitionArray[1], this.alphabet)){
                throw new Exception("Invalid transition. "+transitionArray[1]+" is not included in the alphabet.");
            }
            transitionList.add(new Transition(transitionArray[0],transitionArray[2],transitionArray[1]));
        }
    }

    public DFA(String[] alphabet, ArrayList<ArrayList<String>> states, ArrayList<String> initial_state, ArrayList<ArrayList<String>> final_states, ArrayList<Transition> transitionList) {
        this.alphabet = alphabet;
        this.states = new ArrayList<>();
        constructStates(states);
        contructInitialState(initial_state);
        this.final_states = new ArrayList<String>();
        constructFinalStates(final_states);
        this.transitionList = transitionList;
    }

    private void constructStates(ArrayList<ArrayList<String>> states){
        for(ArrayList<String> state: states)
            this.states.add("" + state);
    }

    private void constructFinalStates(ArrayList<ArrayList<String>> states){
        for(ArrayList<String> state: states)
            this.final_states.add("" + state);
    }
    private void contructInitialState(ArrayList<String> state){
        this.initial_state = "" + state;
    }
    private boolean inArray(String s , String [] array){
        for(int i = 0 ; i < array.length;i++){
            if(array[i].equals(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * check if goals are worth, function controll if the NFA set of goals are in array. Return true if they are or return false
     * if they not.
     *
     * @return the validity of final states.
     */
    private boolean checkGoal() {
        for(String goal : final_states){
            if(goal.equals("")){
                continue;
            }
            if(!states.contains(goal)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check if start is worth, function controll if the NFA start state is in array. Return true if he is or return false
     * if he's not.
     *
     * @return the validity of start states.
     */
    private boolean checkStart() {
        return states.contains(initial_state);
    }

}
