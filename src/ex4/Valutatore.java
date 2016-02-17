/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Alexandru Podgoreanu
 */
public class Valutatore {

    private Lexer lex;
    private Token look;
    private BufferedReader pbr;

    public Valutatore(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    /**
     * Passa al prossimo Token
     */
    void move() {
        look = lex.lexical_scan(pbr);
        System.err.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex.line + ": " + s);
        ///
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) {
                move();
            }
        } else {
            error("syntax error");
        }
    }

    public void start() {
        int expr_val;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            expr_val = expr();
            match(Tag.EOF);
            System.out.println(expr_val);
        } else {
            error("Syntax Error");
        }
    }
// la procedura start puo‘ essere estesa
// come in Esercizio 3.1 (opzionale)

    private int expr() {
        int term_val, exprp_val = 0;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            term_val = term();
            exprp_val = exprp(term_val);
        } else {
            error("Syntax Error");
        }
        return exprp_val;
    }
// la procedura expr puo‘ essere estesa
// come in Esercizio 3.1 (opzionale)

    private int exprp(int exprp_i) {
        int term_val, exprp_val = 0;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            default: //CASO EPSILON
                if (look.tag == ')' || look.tag == Tag.EOF) {
                    exprp_val = exprp_i;
                } else {
                    error("Syntax error");
                }

        }
        return exprp_val;
    }

    private int term() {
        int term_val = 0, fact_val;
        if (look.tag == '(' || look.tag == Tag.NUM) {
            fact_val = fact();
            term_val = termp(fact_val);
        } else {
            error("Syntax Error");
        }
        return term_val;
    }

    private int termp(int termp_i) {
        int fact_val, termp_val = 1;
        switch (look.tag) {
            case '*':
                match('*');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            case '/':
                match('/');
                fact_val = fact();
                termp_val = termp(termp_i * fact_val);
                break;
            default:
                if (look.tag == '+' || look.tag == '-' || look.tag == ')' || look.tag == Tag.EOF) {
                    //CASO EPSILON
                    termp_val = termp_i;
                } else {
                    error("Syntax Error");
                }
            //*** END Caso EPSILON ***//
        }
        return termp_val;
    }

    private int fact() {
        int fact_val = 0;
        switch (look.tag) {
            case Tag.NUM:
                Word ww = (Word) look;
                System.out.println("look.tag = " + look.tag);
                fact_val = Integer.parseInt(ww.lexeme);
                match(Tag.NUM);
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String filePath = new File("").getAbsolutePath();
        String path = filePath + File.separator + "program.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
