/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex1;

/**
 *
 * @author Alexandru Podgoreanu
 * 
 */
public class MultipliDiTre {

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
                    if (ch == '0') {
                        state = 0;
                    } else if (ch == '1') {
                        state = 1;
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
                    if (ch == '0') {
                        state = 1;
                    } else if (ch == '1') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;
            }
        }
        return state == 0;
    }
}
