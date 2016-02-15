package ex3;

/**
 * Un Token e una coppia costituita da un nome e un valore di un attributo opzionale
 * Il nome del token è un simbolo astratto che rappresenta uno specifico tipo di unita lessicale. 
 * Pe esempio una particolare parola chiave oppure una sequenza di caratteri che denota un identificatore
 * 
 * Una lessema e un'instanza di un token
 * Per esempio:
 * per il Token <ID> corrispondono i lessemi: pi, score, D2
 * per il Token <NUMBER> corrispondono i lessemi: 3.12, 345, 0.54
 *
 * @see Grammatica
 * @author UNITO
 */
public class Token {

    /**
     * tag e' il nome del token
     */
    public final int tag;

    public Token(int t) {
        tag = t;
    }

    public String toString() {
        return "<" + tag +", '"+ (char) tag+"'>";
    }
    /**
     * Definisce le lessemi della grammatica
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
