/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex1;

/**
 * Esercizio 1.5. Progettare e implementare un DFA che riconosca il linguaggio
 * dei numeri binari (stringhe di 0 e 1) il cui valore e multiplo di 3. Per
 * esempio, ` 110 e 1001 sono stringhe del linguaggio (rappresentano
 * rispettivamente i numeri 6 e 9), mentre 10 e 111 no (rappresentano
 * rispettivamente i numeri 2 e 7). Suggerimento: usare tre stati per
 * rappresentare il resto della divisione per 3 del numero.
 *
 * @author Alexandru Podgoreanu
 *
 * Questo procedimento si applica per divisione a n
 * sia n = 3
 * 1)Aggiungere n stati. stato q0 iniziale e finale
 * 2)Aggiungere le transizioni
 * 
 *  per 0 in binario = '0'  - f:(q0,0) = q0 (0 mod 3 = 0)
 *  per 1 in binario = '1'  - f:(q0,1) = q1 (1 mod 3 = 1)
 *  per 2 in binario = '10' - f:(q0,1) = q1, f:(q1,0) = q2 (2 mod 3 = 2)
 *  per 3 in binario = '11' - f:(q0,1) = q1, f:(q1,1) = q0 (3 mod 3 = 0)
 *  per 4 in binario = '100'- f:(q0,1) = q1, f:(q1,0) = q2 f:(q2,0) = q1 (4 mod 3 = 1)
 *  ... ... ...
 *  procedere in questo modo finché tutti i n stati hanno transizione per 0 e 1
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
