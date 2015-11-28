/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Alexandru Podgoreanu
 */
public class Parser {

    private Lexer lex;
    /**
     * il Token riconosciuto con successo
     */
    private Token look;
    private BufferedReader pbr;

    public Parser(Lexer l, BufferedReader br) {
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

    public void start() { // la procedura start puo‘ essere estesa (opzionale)
        expr();
        match(Tag.EOF);
    }

    private void expr() { // la procedura expr puo‘ essere estesa (opzionale)
        term();
        exprp();
    }

    private void exprp() {
        switch (look.tag) {
            case '+':
                match('+');
                term();
                exprp();
                break;
            case '-':
                match('-');
                term();
                exprp();
                break;
        }
    }

    private void term() {
        fact();
        termp();
    }

    private void termp() {
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;
            case '/':
                match('/');
                fact();
                termp();
                break;
        }
    }

    private void fact() {
        switch (look.tag) {
            case Tag.NUM:
                match(Tag.NUM);
                break;
            case '(':
                match('(');
                expr();
                match(')');
                break;
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String filePath = new File("").getAbsolutePath();
        String path = filePath + File.separator+ "program.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
