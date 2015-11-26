package ex2;

/**
 * Questa classe definisce i token della grammatica del linguaggio. Un token e'
 * un simbolo del linguaggio, un carattere.
 * @see Grammatica
 * @author UNITO
 */
public class Token {
    /**
     * tag e' un simbolo della grammatica
     */
    public final int tag;

    public Token(int t) {
        tag = t;
    }

    public String toString() {
        return "<" + tag + ">";
    }
    /**
     * Definisce i token della grammatica
     */
    public static final Token comma = new Token(','),
            colon = new Token(':'),
            semicolon = new Token(';'),
            lpar = new Token('('),
            rpar = new Token(')'),
            plus = new Token('+'),
            minus = new Token('-'),
            mult = new Token('*'),
            div = new Token('/'),
            lt = new Token('<'),
            gt = new Token('>');
}
