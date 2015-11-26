package ex2;

/**
 * Questa classe definisce tutte le lexeme della grammatica del linguaggio
 * @see Grammatica
 * @author UNITO
 */
public class Word extends Token {

    /**
     * lexeme e un simbolo della grammatica che ha 2 o piu caratteri
     */
    public String lexeme = "";

    public Word(int tag, String s) {
        super(tag);
        lexeme = s;
    }

    public String toString() {
        return "<" + tag + ", " + lexeme + ">";
    }
    public static final Word and = new Word(Tag.AND, "&&"),
            or = new Word(Tag.OR, "||"),
            eq = new Word(Tag.EQ, "=="),
            le = new Word(Tag.LE, "<="),
            ne = new Word(Tag.NE, "<>"),
            ge = new Word(Tag.GE, ">="),
            assign = new Word(Tag.ASSIGN, ":=");
}
