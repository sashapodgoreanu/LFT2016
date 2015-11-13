/*
 * Universit� degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex1;

/**
 * ESERCIZIO 1.3
 *
 * @author Alexandru Podgoreanu
 *
 *
 * Implementazione di un automa deterministico che riconosce il linguaggio delle
 * costanti numeriche in virgola mobile precedute e/o seguite da una sequenza
 * (eventualmente vuota) di spazi. Esempi di tali costanti sono: 123 123.5 .567
 * +7.5 -.7 67e10 1e-2 -.7e2
 * vedi esercizio 1.3.svg
 *
 */
public class CostantiNumericheConSpazi {

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
                    if (Character.isDigit(ch)) {
                        state = 3;
                    } else if (ch == '+' || ch == '-') {
                        state = 1;
                    } else if (ch == '.') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;

                case 1:
                    if (Character.isDigit(ch)) {
                        state = 3;
                    } else if (ch == '.') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
                case 2:
                    if (Character.isDigit(ch)) {
                        state = 4;
                    } else {
                        state = -1;
                    }
                    break;
                //finale
                case 3:
                    if (Character.isDigit(ch)) {
                        state = 3;
                    } else if (ch == 'e') {
                        state = 5;
                    } else if (ch == '.') {
                        state = 2;
                    } else if (ch == ' ') {
                        state = 8;
                    } else {
                        state = -1;
                    }
                    break;
//finale
                case 4:
                    if (Character.isDigit(ch)) {
                        state = 4;
                    } else if (ch == 'e') {
                        state = 5;
                    } else if (ch == ' ') {
                        state = 8;
                    } else {
                        state = -1;
                    }
                    break;
                case 5:
                    if (Character.isDigit(ch)) {
                        state = 6;
                    } else if (ch == '+' || ch == '-') {
                        state = 7;
                    } else {
                        state = -1;
                    }
                    break;
                //finale
                case 6:
                    if (Character.isDigit(ch)) {
                        state = 6;
                    } else if (ch == ' ') {
                        state = 8;
                    } else {
                        state = -1;
                    }
                    break;
                case 7:
                    if (Character.isDigit(ch)) {
                        state = 6;
                    } else {
                        state = -1;
                    }
                    break;
                case 8:
                    if (Character.isDigit(ch)) {
                        state = 3;
                    } else if (ch == '+' || ch == '-') {
                        state = 1;
                    } else if (ch == '.') {
                        state = 2;
                    } else if (ch == ' ') {
                        state = 8;
                    } else {
                        state = -1;
                    }
                    break;
            }
        }
        return state == 3 || state == 4 || state == 6;
    }

}
