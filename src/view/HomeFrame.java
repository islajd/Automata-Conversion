package view;

import controller.AutmoataConversionController;
import controller.DFAMinimizationController;
import model.DFA;
import model.NFA;
import model.Transition;
import model.eNFA;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeFrame extends JFrame {
    private final static int WIDTH = 800;
    private final static int HEIGHT = 550;

    private JPanel panel_initialAutomata;
    private JPanel panel_convertedAutomata;
    private JPanel panel_convert;
    private JButton button_convert;
    private JButton button_clean;
    private JTextField textField_alphabet = new JTextField();
    private JTextField textField_initialState = new JTextField();
    private JTextField textField_acceptState = new JTextField();
    private JTextField textField_states = new JTextField();
    private JTextField textField_transitions = new JTextField();
    private JComboBox comboBox_automata;
    private JComboBox comboBox_converted_automata;

    private JLabel converted_alphabet = new JLabel("Alphabet: ");
    private JLabel converted_states = new JLabel("States: ");
    private JLabel converted_initialState = new JLabel("Start State: ");
    private JLabel converted_acceptStates = new JLabel("Final States: ");
    private JLabel label_converted_transitions = new JLabel("Transitions: ");

    private JPanel panel_convertAction;
    private JTextArea jtxt_converted_transitions;
    private JTextArea jtxt_converted_acceptStates;
    private JTextArea jtxt_converted_states;


    public HomeFrame(){
        setConfig();
        setLayout(null);

        buildAutomataAction();
        buildInitialAutomataPanel();
        buildConvertedAutomataPanel();
        buildConvertButtonPanel();

        buildAction();

        add(panel_initialAutomata);
        add(panel_convert);
        add(panel_convertedAutomata);
    }

    private void buildInitialAutomataPanel(){
        panel_initialAutomata = new JPanel();
        panel_initialAutomata.setBorder(new TitledBorder(new EtchedBorder(),"Initial Automata"));

        panel_initialAutomata.setLocation(0,0);
        panel_initialAutomata.setSize(new Dimension((WIDTH/2) - 40,HEIGHT-60));
        panel_initialAutomata.setLayout(new GridLayout(6,2));

        JLabel automata = new JLabel("Convert From");
        JLabel alphabet = new JLabel("Alphabet");
        JLabel states = new JLabel("States");
        JLabel initialState = new JLabel("Start State");
        JLabel acceptStates = new JLabel("Final States");
        JLabel transitions = new JLabel("Transitions");

        panel_initialAutomata.add(automata);
        panel_initialAutomata.add(panel_convertAction);
        panel_initialAutomata.add(panel_convertAction);
        panel_initialAutomata.add(alphabet);
        panel_initialAutomata.add(textField_alphabet);
        panel_initialAutomata.add(states);
        panel_initialAutomata.add(textField_states);
        panel_initialAutomata.add(initialState);
        panel_initialAutomata.add(textField_initialState);
        panel_initialAutomata.add(acceptStates);
        panel_initialAutomata.add(textField_acceptState);
        panel_initialAutomata.add(transitions);
        panel_initialAutomata.add(textField_transitions);
    }

    private void buildConvertedAutomataPanel(){
        panel_convertedAutomata = new JPanel();
        panel_convertedAutomata.setBorder(new TitledBorder(new EtchedBorder(),"Converted Automata"));

        panel_convertedAutomata.setLocation((WIDTH/2) + 40,0);
        panel_convertedAutomata.setSize(new Dimension((WIDTH/2)-40, HEIGHT-60));
        panel_convertedAutomata.setLayout(new GridLayout(5,1));

        panel_convertedAutomata.add(converted_alphabet);
        panel_convertedAutomata.add(converted_initialState);

        jtxt_converted_states = new JTextArea(3,28);
        jtxt_converted_states.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(jtxt_converted_states,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel panel = new JPanel();
        panel.add(converted_states, BorderLayout.NORTH);
        panel.add(scrollPane,BorderLayout.CENTER);

        jtxt_converted_acceptStates = new JTextArea(3,28);
        jtxt_converted_acceptStates.setEditable(false);
        JScrollPane scrollPane1 = new JScrollPane(jtxt_converted_acceptStates,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JPanel panel1 = new JPanel();
        panel1.add(converted_acceptStates, BorderLayout.NORTH);
        panel1.add(scrollPane1,BorderLayout.CENTER);

        jtxt_converted_transitions = new JTextArea(3,28);
        jtxt_converted_transitions.setEditable(false);
        JScrollPane scroll = new JScrollPane(jtxt_converted_transitions);
        JPanel panel_converted_transitions = new JPanel();
        panel_converted_transitions.add(label_converted_transitions,BorderLayout.NORTH);
        panel_converted_transitions.add(scroll,BorderLayout.CENTER);

        panel_convertedAutomata.add(panel);
        panel_convertedAutomata.add(panel1);
        panel_convertedAutomata.add(panel_converted_transitions);
    }

    private void buildConvertButtonPanel(){
        panel_convert = new JPanel();

        panel_convert.setLocation((WIDTH/2)-40, (2 * HEIGHT/5));
        panel_convert.setSize(new Dimension(80,HEIGHT));

        button_convert = new JButton("Convert");
        button_clean = new JButton("Clean");
        panel_convert.add(button_convert, BorderLayout.NORTH);
        panel_convert.add(button_clean, BorderLayout.SOUTH);
    }

    private void buildAction(){
        button_convert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String alphabet = textField_alphabet.getText();
                String states = textField_states.getText();
                String start = textField_initialState.getText();
                String finalStates = textField_acceptState.getText();
                String transition = textField_transitions.getText();
                String convertFrom = (String) comboBox_automata.getSelectedItem();
                String convertTo = (String) comboBox_converted_automata.getSelectedItem();

                try {
                    if(convertFrom.equals("DFA") && convertTo.equals("Minimize")){
                        DFA automata = new DFA(alphabet, states, transition, start, finalStates);
                        DFA a = new DFAMinimizationController().minimize(automata);
                        setDFAinFrame(a);
                    }
                    else if(convertFrom.equals("NFA") && convertTo.equals("DFA")){
                        NFA automata = new NFA(alphabet, states, transition, start, finalStates);
                        DFA a = AutmoataConversionController.convertToDFA(automata);
                        if(JOptionPane.showConfirmDialog(panel_convertedAutomata,"Do you want the minimal DFA?","Minimimal Automata",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                            a = new DFAMinimizationController().minimize(a);
                        setDFAinFrame(a);
                    }
                    else if(convertFrom.equals("e-NFA") && convertTo.equals("NFA")){
                        eNFA automata = new eNFA(alphabet, states, transition, start, finalStates);
                        NFA a = AutmoataConversionController.convertToNFA(automata);
                        setNFAinFrame(a);
                    }
                    else if(convertFrom.equals("e-NFA") && convertTo.equals("DFA")){
                        eNFA automata = new eNFA(alphabet, states, transition, start, finalStates);
                        NFA na = AutmoataConversionController.convertToNFA(automata);
                        DFA a = AutmoataConversionController.convertToDFA(na);
                        if(JOptionPane.showConfirmDialog(panel_convertedAutomata,"Do you want the minimal DFA?","Minimimal Automata",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                            a = new DFAMinimizationController().minimize(a);
                        setDFAinFrame(a);
                    }
                }
                catch (Exception exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage());
                }
            }
        });
        button_clean.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField_alphabet.setText("");
                textField_states.setText("");
                textField_initialState.setText("");
                textField_acceptState.setText("");
                textField_transitions.setText("");
                converted_alphabet.setText("Alphabet: ");
                converted_initialState.setText("Start State: ");
                jtxt_converted_states.setText("");
                jtxt_converted_acceptStates.setText("");
                jtxt_converted_transitions.setText("");
            }
        });
    }

    private void buildAutomataAction(){
        panel_convertAction = new JPanel();
        comboBox_automata = new JComboBox<>();
        comboBox_automata.addItem("DFA");
        comboBox_automata.addItem("NFA");
        comboBox_automata.addItem("e-NFA");
        comboBox_converted_automata = new JComboBox<>();
        comboBox_converted_automata.addItem("DFA");
        comboBox_automata.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) comboBox_automata.getSelectedItem();
                if(s.equals("DFA")){
                    comboBox_converted_automata.removeAllItems();
                    comboBox_converted_automata.addItem("Minimize");
                }
                else if(s.equals("e-NFA")) {
                    comboBox_converted_automata.removeAllItems();
                    comboBox_converted_automata.addItem("DFA");
                    comboBox_converted_automata.addItem("NFA");
                }else if(s.equals("NFA")){
                    comboBox_converted_automata.removeAllItems();
                    comboBox_converted_automata.addItem("DFA");
                }
            }
        });
        panel_convertAction.add(comboBox_automata,BorderLayout.WEST);
        panel_convertAction.add(new JLabel("To"));
        panel_convertAction.add(comboBox_converted_automata,BorderLayout.EAST);
    }

    private void setDFAinFrame(DFA a){
        //vendosja e alfabetit
        String alphabet = "{ ";
        for(int i = 0; i < a.alphabet.length; i++){
            if(i != a.alphabet.length - 1) {
                alphabet = alphabet + a.alphabet[i] + ",";
            }
            else {
                alphabet = alphabet + a.alphabet[i];
            }
        }
        alphabet = alphabet + " }";
        converted_alphabet.setText(converted_alphabet.getText()+" "+ alphabet);

        //vendosja e gjendjeve
        String states = "{ ";
        for(int i = 0 ; i < a.states.size(); i++){
            if(i != a.states.size() - 1) {
                states = states + a.states.get(i) + " ,";
            }
            else {
                states = states + a.states.get(i);
            }
        }
        states = states + " }";
        jtxt_converted_states.setText(jtxt_converted_states.getText()+" "+ states);

        //vendosja e gjendjes fillestare
        converted_initialState.setText(converted_initialState.getText() + " " + a.initial_state);

        //vendosja e gjendjeve fundore
        String accepted = "{ ";
        for(int i = 0; i < a.final_states.size(); i ++){
            if(i != a.final_states.size() - 1) {
                accepted = accepted + a.final_states.get(i) + " ,";
            }
            else {
                accepted = accepted + a.final_states.get(i);
            }
        }
        accepted = accepted + " }";
        jtxt_converted_acceptStates.setText(jtxt_converted_acceptStates.getText()+" "+ accepted);

        //vendosja e kalimeve te automates
        String transitions = "{ \n";
        for(int i = 0; i < a.transitionList.size(); i++){
            Transition t = a.transitionList.get(i);
            transitions = transitions + t.from + "-" + t.alphabet + "-" + t.to + "; \n";
        }
        transitions = transitions + " }";
        jtxt_converted_transitions.setText(jtxt_converted_transitions.getText()+" "+ transitions);
    }

    private void setNFAinFrame(NFA a){
        //vendosja e alfabetit
        String alphabet = "{ ";
        for(int i = 0; i < a.alphabet.length; i++){
            if(i != a.alphabet.length - 1) {
                alphabet = alphabet + a.alphabet[i] + ",";
            }
            else {
                alphabet = alphabet + a.alphabet[i];
            }
        }
        alphabet = alphabet + " }";
        converted_alphabet.setText(converted_alphabet.getText()+" "+ alphabet);

        //vendosja e gjendjeve
        String states = "{ ";
        for(int i = 0 ; i < a.states.length; i++){
            if(i != a.states.length - 1) {
                states = states + a.states[i] + " ,";
            }
            else {
                states = states + a.states[i];
            }
        }
        states = states + " }";
        jtxt_converted_states.setText(jtxt_converted_states.getText()+" "+ states);

        //vendosja e gjendjes fillestare
        converted_initialState.setText(converted_initialState.getText() + " " + a.initial_state);

        //vendosja e gjendjeve fundore
        String accepted = "{ ";
        for(int i = 0; i < a.final_states.length; i ++){
            if(i != a.final_states.length - 1) {
                accepted = accepted + a.final_states[i] + " ,";
            }
            else {
                accepted = accepted + a.final_states[i];
            }
        }
        accepted = accepted + " }";
        jtxt_converted_acceptStates.setText(jtxt_converted_acceptStates.getText()+" "+ accepted);

        //vendosja e kalimeve te automates
        String transitions = "{ \n";
        for(int i = 0; i < a.transitionList.size(); i++){
            Transition t = a.transitionList.get(i);
            transitions = transitions + t.from + "-" + t.alphabet + "-" + t.to + "; \n";
        }
        transitions = transitions + "}";
        jtxt_converted_transitions.setText(jtxt_converted_transitions.getText()+" "+ transitions);
    }

    private void setConfig(){
        setSize(WIDTH,HEIGHT);
        setTitle("Automata Converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}