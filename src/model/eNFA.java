package model;

import java.util.ArrayList;

public class eNFA {
    public String[] alphabet;
    public String[] states;
    public String[] transitions;
    public String initial_state;
    public String[] final_states;
    public ArrayList<Transition> transitionList = new ArrayList<>();

    /**
     * NDFA construct an Non-Determinstic finite automata from given params.
     *
     * @param alphabet  a finite set of input symbols.
     * @param states a finite set of states.
     * @param transitions a finite set of transitions.
     * @param initial_state an initial (or start) state.
     * @param final_states a set of states F distinguished as accepting (or final) states.
     */
    public eNFA(String alphabet, String states, String transitions, String initial_state, String final_states) throws Exception {
        this.states = states.split(",");
        this.final_states = final_states.split(",");

        if(!checkGoal()){
            throw new Exception("Invalid final states " + final_states);
        }

        alphabet += ",@";
        this.alphabet = alphabet.split(",");
        this.initial_state = initial_state;

        if(!checkStart()){
            throw new Exception("Invalid start state " + initial_state);
        }

        this.transitions = transitions.split(",");

        for(String transition : this.transitions){
            String [] transitionArray = transition.split("-");
            if(transitionArray.length != 3){
                throw new Exception("Invalid transition. Transitions should be of size 3");
            }
            for ( int i = 0 ; i < 3 ; i++){
                if(i == 1)
                    continue;
                if(!inArray(transitionArray[i], this.states)){
                    throw new Exception("Invalid transition. " + transitionArray[i] + " is not included in the states.");
                }
            }
            if(!inArray(transitionArray[1], this.alphabet)){
                throw new Exception("Invalid transition. "+transitionArray[1]+" is not included in the alphabet.");
            }
            transitionList.add(new Transition(transitionArray[0],transitionArray[2],transitionArray[1]));
        }
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
            if(!inArray(goal,states)){
                return false;
            }
        }
        return true;
    }

    /**
     * The function check if the given String is found in the given set of String.
     *
     * @param s String, the String to look for.
     * @param array String, set of Strings to be checked.
     * @return true if String is part of array and false if not.
     */
    private boolean inArray(String s , String [] array){
        for(int i = 0 ; i < array.length;i++){
            if(array[i].equals(s)){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if start is worth, function controll if the NFA start state is in array. Return true if he is or return false
     * if he's not.
     *
     * @return the validity of start states.
     */
    private boolean checkStart() {
        return inArray(initial_state, states);
    }
}
