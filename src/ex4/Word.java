package ex4;

/**
 * La classe Word estende la classe Token. Un Token e una coppia costituita da
 * un nome e un valore di un attributo opzionale Il nome del token è un simbolo
 * astratto che rappresenta uno specifico tipo di unita lessicale. Pe esempio
 * una particolare parola chiave oppure una sequenza di caratteri che denota un
 * identificatore
 *
 * Una lessema e un'instanza di un token Per esempio: per il Token <ID>
 * corrispondono i lessemi: pi, score, D2 per il Token <NUMBER> corrispondono i
 * lessemi: 3.12, 345, 0.54
 *
 * @see Grammatica
 * @author UNITO
 */
public class Word extends Token {

    /**
     * lexeme e un'istanza di un Token
     */
    public String lexeme = "";

    /**
     *
     * @param tag Il nome del Token
     * @param s lessema
     */
    public Word(int tag, String s) {
        super(tag);
        lexeme = s;
    }

    public String toString() {
        return "<" + Tag.print(tag) + ", '" + lexeme + "'>";
    }
    public static final Word and = new Word(Tag.AND, "&&"),
            or = new Word(Tag.OR, "||"),
            eq = new Word(Tag.EQ, "=="),
            le = new Word(Tag.LE, "<="),
            ne = new Word(Tag.NE, "<>"),
            ge = new Word(Tag.GE, ">="),
            assign = new Word(Tag.ASSIGN, ":=");
}
