package controller;

import model.DFA;
import model.Transition;

import java.util.ArrayList;

public class DFAMinimizationController {

    private int[][] tempTable;

    public DFA minimize(DFA a){
        DFA newAutomata = null;

        ArrayList<String> newStates = new ArrayList<>();
        ArrayList<Transition> transitions = new ArrayList<>();
        ArrayList<String> final_states = new ArrayList<>();
        String start_state;
        //hapi 1, hiqen te gjitha gjendjet e paaritshme
        ArrayList<String> states = this.removeIsolatedStates(a);
        System.out.print(states);

        //Hapi 2,3, plotesimi i tabeles ndihmese ku cdo qelize ka ciftet (p,q)
        // ciftet ku p eshte gjendje fundore dhe q eshte gjendje jo fundore ose e anasjellta shenohen
        initializeTable(states,a);

        pritnTable();

        //Hapi 4,5 nese (p,q) shkon ne nje gjendje te markuar markohet edhe ai, perseritet rekursivisht
        //derisa tabela nuk ndryshon
        extractTable(states,a);

        pritnTable();

        //hapi 6, Bashkimi i gjendjeve ekuivalente
        ArrayList<ArrayList<String>> mergedStates = this.mergeStates(states);

        //shtimi i gjendjeve qe nuk kane asnje gjendje ekuivalente
        for(String s:states)
            if(findGroup(mergedStates,s) == -1)
                newStates.add(s);

        //shtimi i gjendjeve te reja
        for(ArrayList<String> s:mergedStates)
            newStates.add(""+s);

        //krijimi i gjendjes fillestare
        int startIndex = findGroup(mergedStates,a.initial_state);
        if(startIndex != -1)
            start_state = "" + mergedStates.get(startIndex);
        else
            start_state = a.initial_state;

        //krijimi i gjendjeve fundore
        ArrayList<String> visited = new ArrayList<>();
        for(String finalState:a.final_states){
            if(!visited.contains(finalState)) {
                int finalIndex = findGroup(mergedStates, finalState);
                if (finalIndex != -1) {
                    final_states.add("" + mergedStates.get(finalIndex));
                    visited.addAll(mergedStates.get(finalIndex));
                } else {
                    final_states.add(finalState);
                    visited.add(finalState);
                }
            }
        }

        //krijimi i kalimeve per te gjitha gjendjet qe jane te pabashkuara
        for(String from:a.states){
            if(findGroup(mergedStates,from) == -1)
                for(String alpha:a.alphabet){
                    String reach = getReachableState(from,alpha,a);
                    int reachIndex = findGroup(mergedStates,reach);
                    if(reachIndex == -1)
                        transitions.add(new Transition(from,reach,alpha));
                    else {
                        transitions.add(new Transition(from,mergedStates.get(reachIndex)+"",alpha));
                    }
                }
        }

        //krijimi i kalimeve per te gjitha gjendjet qe jane te bashkuara
        for(ArrayList<String> state:mergedStates){
            for(String alpha:a.alphabet){
                ArrayList<String> reach = new ArrayList<>();
                for(String s:state){
                    String r = getReachableState(s,alpha,a);
                    reach.add(r);
                }
                int reachIndex = findGroup(mergedStates,reach.get(0));
                if(reachIndex == -1)
                    transitions.add(new Transition(""+state,reach.get(0),alpha));
                else
                    transitions.add(new Transition(""+state,mergedStates.get(reachIndex)+"",alpha));
            }
        }

        return new DFA(a.alphabet,newStates,start_state,final_states,transitions);
    }

    private ArrayList<ArrayList<String>> mergeStates(ArrayList<String> states) {
        ArrayList<ArrayList<String>> newStates = new ArrayList<>();
        ArrayList<String> state = new ArrayList<>();
        for(int i = 0; i < tempTable.length; i++) {
            for (int j = 0; j < i; j++) {
                if (tempTable[i][j] == 0) {
                    String p = states.get(i);
                    String q = states.get(j);
                    int pGroupIndex = findGroup(newStates,p);
                    int qGroupIndex = findGroup(newStates,q);
                    if(pGroupIndex == -1 && qGroupIndex == -1){
                        state = new ArrayList<>();
                        state.add(p);
                        state.add(q);
                        newStates.add(state);
                    }
                    else if(pGroupIndex == -1){
                        ArrayList<String> group = newStates.get(qGroupIndex);
                        group.add(p);
                        newStates.set(qGroupIndex,group);
                    }
                    else if(qGroupIndex == -1){
                        ArrayList<String> group = newStates.get(pGroupIndex);
                        group.add(q);
                        newStates.set(pGroupIndex,group);
                    }
                }
            }
        }
        return newStates;
    }

    private int findGroup(ArrayList<ArrayList<String>> groups, String state) {
        for(int i = 0; i < groups.size(); i++){
            ArrayList<String> group = groups.get(i);
            if(group.contains(state))
                return i;
        }
        return -1;
    }

    private ArrayList<String> removeIsolatedStates(DFA a){
        ArrayList<String> states = new ArrayList<>();
        states.add(a.initial_state);
        for(Transition t:a.transitionList){
            String s = t.to;
            if(!states.contains(s) && !t.from.equals(s))
                states.add(s);
        }
        return states;
    }

    private void initializeTable(ArrayList<String> states, DFA a){
        int n = states.size();
        tempTable = new int[n][n];
        for(int i = 0; i < n; i++){
            String p = states.get(i);
            for (int j = 0; j < n; j++){
                String q = states.get(j);
                if(i == j)
                    tempTable[i][j] = 0;
                else
                    if(a.final_states.contains(p)
                            && !a.final_states.contains(q)) {
                        tempTable[i][j] = 1;
                        tempTable[j][i] = 1;
                    }
                    else
                        if(tempTable[i][j] != 1)
                            tempTable[i][j] = 0;
            }
        }
    }

    private void pritnTable(){
        System.out.println();
        for (int i = 0 ; i < tempTable.length ; i++) {
            for (int j = 0; j < tempTable.length; j++){
                System.out.print(tempTable[i][j]+ " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void extractTable(ArrayList<String> states, DFA a){
        int n = states.size();
        for(int i = 0; i < n; i++){
            String p = states.get(i);
            for (int j = 0; j < n; j++){
                if(i != j && tempTable[i][j] == 0){
                    String q = states.get(j);
                    for (String alpha : a.alphabet) {
                        String pPrime = getReachableState(p,alpha,a);
                        String qPrime = getReachableState(q,alpha,a);
                        int pPrimeIndex = states.indexOf(pPrime);
                        int qPrimeIndex = states.indexOf(qPrime);
//                        System.out.println(p + "("+i+")" + " vete  " + pPrime +"("+pPrimeIndex + ")" + " // " + q + "(" + j + ")" + " vete " + qPrime + "(" + qPrimeIndex + ")");
                        if(tempTable[pPrimeIndex][qPrimeIndex] == 1) {
                            tempTable[i][j] = 1;
                            tempTable[j][i] = 1;
                            this.extractTable(states,a);
                            return;
                        }
                    }
                }
            }
        }
    }

    private String getReachableState(String from, String alpha, DFA a){
        for(Transition t : a.transitionList)
            if(t.from.equals(from) && t.alphabet.equals(alpha))
                return t.to;
        return null;
    }

}
