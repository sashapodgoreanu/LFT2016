/*
 * Università degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex1;

/**
 * ESERCIZIO 1.4 Implementare un automa deterministico che riconosca il
 * linguaggio degli identificatori in un linguaggio in stile Java. Un
 * identificatore è una sequenza non vuota di lettere, numeri, ed il simbolo di
 * sottolineatura _ che non comincia con un numero e che non può essere composto
 * solo da un _
 *
 * @author Alexandru Podgoreanu
 */
public class Identificatori {

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
                //stato 0
                case 0:
                    if (ch == '_') {
                        state = 1;
                    } else if (Character.isLetter(ch)) {
                        state = 2;
                    } else {
                        state = -1;     //altrimenti errore (stato -1)
                    }
                    break;

                //stato 1
                case 1:
                    if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;

                //stato 2
                case 2:
                    if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
                        state = 2;
                    } else {
                        state = -1;
                    }
                    break;

            }
        }
        return state == 2;
    }
}
