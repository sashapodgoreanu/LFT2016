package ex5;

import java.io.*;
import java.util.*;

public class Lexer {

    /**
     * Il i-esimo carattere letto da input
     */
    public static int line = 1;
    /**
     *
     */
    private char peek = ' ';

    Hashtable<String, Word> words = new Hashtable<String, Word>();

    public Lexer() {
        reserve(new Word(Tag.VAR, "var"));
        reserve(new Word(Tag.BOOLEAN, "boolean"));
        reserve(new Word(Tag.INTEGER, "integer"));
        reserve(new Word(Tag.FALSE, "false"));
        reserve(new Word(Tag.TRUE, "true"));
        reserve(new Word(Tag.PRINT, "print"));
        reserve(new Word(Tag.IF, "if"));
        reserve(new Word(Tag.THEN, "then"));
        reserve(new Word(Tag.ELSE, "else"));
        reserve(new Word(Tag.WHILE, "while"));
        reserve(new Word(Tag.DO, "do"));
        reserve(new Word(Tag.BEGIN, "begin"));
        reserve(new Word(Tag.END, "end"));
    }

    /**
     * Questa funzione aggiunge una lessema alla grammatica
     *
     * @param w simbolo della grammatica
     */
    private void reserve(Word w) {
        words.put(w.lexeme, w);
    }

    /**
     * Legge un carattere dal'input
     */
    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    /**
     * Questa funzione legge una sequenza di caratteri e restituisce un Token,
     *
     * @return un Token della grammatica.
     * @see Grammatica
     */
    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n') {
                line++;
            }
            readch(br);
        }
        /**
         * Casi per i Token che hanno solo 1 carattere
         */
        switch (peek) {
            case ',':
                peek = ' ';//consume the character
                return Token.comma;
            case ';':
                peek = ' ';
                return Token.semicolon;
            case '(':
                peek = ' ';
                return Token.lpar;
            case ')':
                peek = ' ';
                return Token.rpar;
            case '+':
                peek = ' ';
                return Token.plus;
            case '-':
                peek = ' ';
                return Token.minus;
            case '*':
                peek = ' ';
                return Token.mult;
            case '/':
                peek = ' ';
                return Token.div;
            /**
             * Casi per i Token che hanno 1 o piu caratteri
             */
            //&&
            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek);
                    return null;
                }
            // ||
            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : " + peek);
                    return null;
                }
            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = " + peek);
                    return null;
                }
            case '<':
                readch(br);
                // <=
                if (peek == '=') {
                    peek = ' ';//consume the character
                    return Word.le;

                } else if (peek == '>') {// <>
                    peek = ' ';//consume the character
                    return Word.ne;
                } else {// <
                    return Token.lt;
                }

            case '>':
                readch(br);
                // >=
                if (peek == '=') {
                    peek = ' ';//consume the character
                    return Word.ge;
                } else {// >
                    return Token.gt;
                }
            case ':':
                readch(br);
                // :=
                if (peek == '=') {
                    peek = ' ';//consume the character
                    return Word.assign;
                } else {// :
                    return Token.colon;
                }

            default:
                /**
                 * ID
                 */
                if (Character.isLetter(peek) || peek == '_') {
                    /**
                     * Identificatore da costruire
                     */
                    String s = "";
                    int state = 0;
                    //ciclo finchè sono in uno stato valido e non ho letto tutti i caratteri dell'input s
                    //leggo un carattere in input
                    do {
                        char ch = peek;
                        s += peek;
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
                        readch(br);
                    } while (state >= 0 && Character.isLetter(peek) || Character.isDigit(peek) || peek == '_');
                    if (state == 2) {
                        /**
                         * Verifica se s e una lessema riservata
                         */
                        if ((Word) words.get(s) != null) {
                            return (Word) words.get(s);
                        } else {
                            Word w = new Word(Tag.ID, s);
                            words.put(s, w);
                            return w;
                        }
                    } else {
                        System.err.println("Erroneous ID: "
                                + peek);
                        return null;
                    }
                } //if (Character.isLetter(peek)) {
                //    String s = "";
                //    do {
                
                //        s += peek;
                //        readch();
                //    } while (Character.isDigit(peek)
                //            || Character.isLetter(peek));
                /**
                 * Verifica se s e una lessema riservata
                 */
                //    if ((Word) words.get(s) != null) {
                //        return (Word) words.get(s);
                //    } else {
                //        Word w = new Word(Tag.ID, s);
                //        words.put(s, w);
                //        return w;
                //    }
                //}
                /**
                 * INTEGER
                 */
                else if (Character.isDigit(peek)) {
                    String s = "";
                    /**
                     * Lessema 0 e costituita da un solo carattere
                     */
                    if (peek == '0') {
                        s += peek;
                        readch(br);
                        if (Character.isDigit(peek)) {
                            System.err.println("Erroneous number format: "
                                    + peek);
                        }
                    } else {
                        do {
                            s += peek;
                            readch(br);
                        } while (Character.isDigit(peek));
                    }

                    Word w = new Word(Tag.NUM, s);
                    words.put(s, w);
                    return w;

                } else {
                    if (peek == '$') {
                        return new Token(Tag.EOF);
                    } else {
                        System.err.println("Erroneous character: "
                                + peek);
                        return null;
                    }
                }
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String filePath = new File("").getAbsolutePath();
        String path = filePath + "\\program.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok != null && tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
