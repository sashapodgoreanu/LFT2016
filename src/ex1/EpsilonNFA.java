/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex1;

import java.util.ArrayList;

/**
 *
 * Esercizio 1.8. Costruire il DFA equivalente al €-NFA in Figura 3, e
 * implementare il DFA seguendo la construzione vista in Figura 2.
 *
 *
 *
 * @author Alexandru Podgoreanu
 */
public class EpsilonNFA {

    /**
     * Il e-NFA può verificare se una stringa w appartiene al linguaggio
     * percorrendo su più camini contemporeamente.
     * 
     */
    public static boolean scan(String s) {
        /**
         * states contiene i stati attuali dell'automa 
         */
        ArrayList<Integer> states = new ArrayList<>();
        /**
         * statesPasato contiene i stati da percorrere dell'automa
         */
        ArrayList<Integer> statesPasato = new ArrayList<>();
        
        //aggiungiamo qui le epsilon transizioni
        states.add(1);
        states.add(2);
        /**
         * Numero del carattere che è attualmente letto. primo carattere ha
         * valore 0
         */
        int i = 0;
        //states è vuoto se contiene lo stato de errore
        //ciclo finchè states non è nello stato di errore e ho letto tutti i caratteri dell'input s
        //leggo un carattere in input
        while (!states.isEmpty() && i < s.length()) {
            char ch = s.charAt(i++);
            statesPasato.clear();
            statesPasato.addAll(states);
            for (int state : statesPasato) {
                switch (state) {
                    case 1:
                        if (ch == 'a') {
                            removeState(states, 1);
                            states.add(1);
                            states.add(3);
                        } else {
                            removeState(states, 1);
                        }
                        break;
                    case 2:
                        if (ch == 'b') {
                            removeState(states, 2);
                            states.add(4);
                        } else {
                            removeState(states, 2);
                        }
                        break;
                    case 3:
                        if (ch == 'b') {
                            removeState(states, 3);
                            states.add(5);
                        } else {
                            removeState(states, 3);
                        }
                        break;
                    case 4:
                        if (ch == 'a') {
                            removeState(states, 4);
                            states.add(4);
                            states.add(5);
                        } else {
                            removeState(states, 4);
                        }
                        break;
                    case 5:
                        removeState(states, 5);
                        break;

                }
            }
        }
        return states.contains(5);
    }

    private static void removeState(ArrayList<Integer> states, int state) {
        if (states.indexOf(state) > -1) {
            states.remove(states.indexOf(state));
        }
    }
}
