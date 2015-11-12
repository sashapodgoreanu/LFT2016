/*
 * Università degli Studi di Torino - Dipartimento di Informatica
 */
package ex1;

/**
 *
 * @author Alexandru Podgoreanu Implementazione del <b>complemento</b> d'un
 * automa deterministico che riconosce tutte le stringhe sull'alfabeto {0,1} che
 * contengono una sequenza di almeno tre zeri consecutivi, che è l'espressione
 * regolare (1+ 01 + 001)(e + 0 + 00), e = stringa vuota
 *
 */
public class TreZeriNonCons {

    public static boolean scan(String s) {
        /**
         * Stato dell'automa in cui si trova Non può essere minore di 0
         */
        int state = 0;
        /**
         * Numero del carattere che è attualmente letto primo carattere ha
         * valore 0
         */
        int i = 0;
        //ciclo finchè sono in uno stato valido e non ho letto tutti i caratteri dell'input s
        //leggo un carattere in input
        while (state >= 0 && i < s.length()) {
            char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch == '0') {
                        state = 1;
                    } else if (ch == '1') {
                        state = 0;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:
                    if (ch == '0') {
                        state = 2;
                    } else if (ch == '1') {
                        state = 0;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (ch == '1') {
                        state = 0;
                    } else {
                        state = -1;
                    }
                    break;
            }
        }
        return state == 2 || state == 0 || state == 1;
    }
}
