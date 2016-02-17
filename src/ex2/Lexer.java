package ex2;

import java.io.IOException;
import java.util.Hashtable;

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
    private void readch() {
        try {
            peek = (char) System.in.read();
            System.out.println("Reading: " + peek);
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
    public Token lexical_scan() {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n') {
                line++;
            }
            readch();
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
                readch();
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
                readch();
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : " + peek);
                    return null;
                }
            case '=':
                readch();
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : " + peek);
                    return null;
                }
            case '<':
                readch();
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
                readch();
                // >=
                if (peek == '=') {
                    peek = ' ';//consume the character
                    return Word.ge;
                } else {// >
                    return Token.gt;
                }
            case ':':
                readch();
                // :=
                if (peek == '=') {
                    peek = ' ';//consume the character
                    return Word.assign;
                } else {// :
                    return Token.colon;
                }

            default:
                if (Character.isLetter(peek) || peek == '_') {
                    String s = "";
                    do {
                        s += peek;
                        readch();
                    } while (Character.isDigit(peek)
                            || Character.isLetter(peek) || peek == '_');
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
                } else if (Character.isDigit(peek)) {
                    String s = "";
                    /**
                     * Lessema 0 e costituita da un solo carattere
                     */
                    if (peek == '0') {
                        s += peek;
                        readch();
                        if (Character.isDigit(peek)) {
                            System.err.println("Erroneous number format: "
                                    + peek);
                        }
                    } else {
                        do {
                            s += peek;
                            readch();
                        } while (Character.isDigit(peek));
                    }

                    Word w = new Word(Tag.INTEGER, s);
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

        Token tok;
        do {
            tok = lex.lexical_scan();
            System.out.println("Scan: " + tok);
        } while (tok.tag != Tag.EOF);
    }

}
