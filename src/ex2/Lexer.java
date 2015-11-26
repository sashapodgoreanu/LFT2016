package ex2;

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
    private void readch() {
        try {
            peek = (char) System.in.read();
        } catch (IOException exc) {
            peek = (char) -1; // ERROR
        }
    }

    /**
     * Questa funzione legge una sequenza di caratteri e restituisce un Token,
     * @return un Token della grammatica.
     * @see Grammatica
     */
    public Token lexical_scan() {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n') {
                line++;
            }
            readch();
        }

        switch (peek) {
            case ',':
                peek = ' ';
                return Token.comma;

			// ... gestire gli altri casi ... //
            case '&':
                readch();
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek);
                    return null;
                }

			// ... gestire gli altri casi ... //
            default:
                if (Character.isLetter(peek)) {
                    String s = "";
                    do {
                        s += peek;
                        readch();
                    } while (Character.isDigit(peek)
                            || Character.isLetter(peek));
                    if ((Word) words.get(s) != null) {
                        return (Word) words.get(s);
                    } else {
                        Word w = new Word(Tag.ID, s);
                        words.put(s, w);
                        return w;
                    }
                } else {

			// ... gestire il caso dei numeri ... //
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

        Token tok;
        do {
            tok = lex.lexical_scan();
            System.out.println("Scan: " + tok);
        } while (tok.tag != Tag.EOF);
    }

}
