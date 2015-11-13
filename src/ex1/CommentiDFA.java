/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex1;

/**
 * Esercizio 1.6. Progettare e implementare un DFA con l’alfabeto {/, *, a} che
 * riconosca il linguaggio di stringhe che contengono almeno 4 caratteri che
 **/
 // iniziano con /*, che finiscono con */
/*
 * @author Alexandru Podgoreanu
 */
public class CommentiDFA {

    public static boolean scan(String s) {
        /**
         * Stato dell'automa in cui si trova Non può essere minore di 0
         */
        int state = 0;
        /**
         * Numero del carattere che è attualmente letto. primo carattere ha
         * valore 0
         */
        int i = 0;
        //ciclo finchè sono in uno stato valido e non ho letto tutti i caratteri dell'input s
        //leggo un carattere in input
        while (state >= 0 && i < s.length()) {
            char ch = s.charAt(i++);
            switch (state) {
                case 0:
                    if (ch == '/') {
                        state = 1;
                    } else if (isCharNonCom (ch) ) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 1:
                    if (ch == '*') {
                        state = 3;
                    } else if (isCharNonCom (ch)) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (isCharNonCom (ch)) {
                        state = 2;
                    } else if (ch == '/') {
                        state = 1;
                    } else if (ch == '*') {
                        state = 7;
                    } else {
                        state = -1;
                    }
                    break;
                case 3:
                    if (isCharNonCom (ch)) {
                        state = 4;
                    } else if (ch == '*') {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
                case 4:
                    if (isCharNonCom (ch) || ch == '/') {
                        state = 4;
                    } else if (ch == '*') {
                        state = 5;
                    } else {
                        state = -1;
                    }
                    break;
                case 5:
                    if (isCharNonCom (ch)) {
                        state = 4;
                    } else if (ch == '*') {
                        state = 5;
                    } else if (ch == '/') {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 6:
                    if (isCharNonCom (ch)) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 7:
                    if (isCharNonCom (ch)) {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
            }
        }
        return state == 2 || state == 6;
    }
    private static boolean isCharNonCom(char ch){
        return !(ch == '/' || ch == '*');
    }
}
