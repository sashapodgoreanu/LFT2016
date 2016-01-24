/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alexandru Podgoreanu
 */
public class ProgramP {

    private Lexer lex;
    /**
     * il Token riconosciuto con successo
     */
    private Token look;
    private BufferedReader pbr;
    private CodeGenerator code;

    public ProgramP(Lexer l, BufferedReader br, CodeGenerator c) {
        lex = l;
        pbr = br;
        code = c;
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

    public void prog() { // la procedura start puo‘ essere estesa (opzionale)
        Type orE_tipo;
        match(Tag.PRINT);
        match('(');
        orE_tipo = orE();
        match(')');
        match(Tag.EOF);
        if (orE_tipo == Type.INTEGER) {
            code.emit(OpCode.invokestatic, 1);
        } else {
            code.emit(OpCode.invokestatic, 0);
        }
        try {
            code.toJasmin();
        } catch (IOException ex) {
            Logger.getLogger(ProgramP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Type orE() {
        Type tipo = null, andE_tipo, orE_p_tipo;
        andE_tipo = andE();
        orE_p_tipo = orE_p();
        if (andE_tipo == Type.INTEGER && (orE_p_tipo == null || orE_p_tipo == Type.INTEGER)) {
            tipo = Type.INTEGER;
        } else if (andE_tipo == Type.BOOLEAN && (orE_p_tipo == null || orE_p_tipo == Type.BOOLEAN)) {
            tipo = Type.BOOLEAN;
        } else {
            error("Type Error");
        }
        return tipo;
    }

    public Type orE_p() {
        Type tipo = null, andE_tipo, orE_p_tipo;
        if (look.tag == Tag.OR) {
            match(Tag.OR);
            andE_tipo = andE();
            code.emit(OpCode.ior);
            orE_p_tipo = orE_p();
            if (andE_tipo == Type.BOOLEAN && (orE_p_tipo == null || orE_p_tipo == Type.BOOLEAN)) {
                tipo = Type.BOOLEAN;
            } else {
                error("Expected Boolean");
            }
        }
        return tipo;
    }

    public Type andE() {
        Type tipo = null, relE_tipo, andE_p_tipo;
        relE_tipo = relE();
        andE_p_tipo = andE_p();
        //*** Type cheking ***//
        if (andE_p_tipo == null) {
            tipo = relE_tipo;
        } else if (relE_tipo == Type.BOOLEAN && andE_p_tipo == Type.BOOLEAN) {
            tipo = Type.BOOLEAN;
        } else {
            error("Type error");
        }
        return tipo;
    }

    public Type andE_p() {
        Type tipo = null, relE_tipo, andE_p_tipo;
        if (look.tag == Tag.AND) {
            match(Tag.AND);
            relE_tipo = relE();
            code.emit(OpCode.iand);
            andE_p_tipo = andE_p();
            if (relE_tipo == Type.BOOLEAN && (andE_p_tipo == null || andE_p_tipo == Type.BOOLEAN)) {
                tipo = Type.BOOLEAN;
            } else {
                error("Expected Boolean");
            }
        }
        return tipo;
    }

    public Type relE() {
        Type tipo = null, addE_tipo, relE_p_tipo;
        addE_tipo = addE();
        relE_p_tipo = relE_p();
        //*** Type cheking ***//
        if (relE_p_tipo == null) {
            tipo = addE_tipo;
        } else if (addE_tipo == Type.INTEGER) {
            tipo = Type.BOOLEAN;
        } else {
            error("Integer expected");
        }
        return tipo;
    }

    public Type relE_p() {
        Type addE_tipo = null, tipo = null;
        int ltrue, lnext;
        switch (look.tag) {
            case Tag.EQ:
                match(Tag.EQ);
                addE_tipo = addE();
                ltrue = code.newLabel();
                lnext = code.newLabel();
                code.emit(OpCode.if_icmpeq, ltrue);
                code.emit(OpCode.ldc, 0);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(ltrue);
                code.emit(OpCode.ldc, 1);
                code.emitLabel(lnext);
                tipo = typecheck(addE_tipo);
                break;
            case Tag.NE:
                match(Tag.NE);
                addE_tipo = addE();
                ltrue = code.newLabel();
                lnext = code.newLabel();
                code.emit(OpCode.if_icmpne, ltrue);
                code.emit(OpCode.ldc, 0);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(ltrue);
                code.emit(OpCode.ldc, 1);
                code.emitLabel(lnext);
                tipo = typecheck(addE_tipo);
                break;
            case Tag.GE:
                match(Tag.GE);
                addE_tipo = addE();
                ltrue = code.newLabel();
                lnext = code.newLabel();
                code.emit(OpCode.if_icmpge, ltrue);
                code.emit(OpCode.ldc, 0);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(ltrue);
                code.emit(OpCode.ldc, 1);
                code.emitLabel(lnext);
                tipo = typecheck(addE_tipo);
                break;
            case Tag.LE:
                match(Tag.LE);
                addE_tipo = addE();
                ltrue = code.newLabel();
                lnext = code.newLabel();
                code.emit(OpCode.if_icmple, ltrue);
                code.emit(OpCode.ldc, 0);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(ltrue);
                code.emit(OpCode.ldc, 1);
                code.emitLabel(lnext);
                tipo = typecheck(addE_tipo);
                break;
            case '<':
                match('<');
                addE_tipo = addE();
                ltrue = code.newLabel();
                lnext = code.newLabel();
                code.emit(OpCode.if_icmplt, ltrue);
                code.emit(OpCode.ldc, 0);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(ltrue);
                code.emit(OpCode.ldc, 1);
                code.emitLabel(lnext);
                tipo = typecheck(addE_tipo);
                break;
            case '>':
                match('>');
                addE_tipo = addE();
                ltrue = code.newLabel();
                lnext = code.newLabel();
                code.emit(OpCode.if_icmpgt, ltrue);
                code.emit(OpCode.ldc, 0);
                code.emit(OpCode.GOto, lnext);
                code.emitLabel(ltrue);
                code.emit(OpCode.ldc, 1);
                code.emitLabel(lnext);
                tipo = typecheck(addE_tipo);
                break;
        }
        return tipo;
    }
    
    private Type typecheck(Type a){
    //*** type cheking ***//
        if (a == Type.INTEGER) {
            return Type.INTEGER;
        } else {
            error("Integer expected");
        }
        return null;
    }

    public Type addE() {
        Type tipo = null, multE_tipo, addE_p_tipo;
        multE_tipo = multE();
        addE_p_tipo = addE_p();
        //*** Type cheking ***//
        if (addE_p_tipo == null) {
            tipo = multE_tipo;
        } else if (multE_tipo == Type.INTEGER) {
            tipo = Type.INTEGER;
        } else {
            error("Integer expected");
        }
        return tipo;
    }

    public Type addE_p() {
        Type tipo = null, multE_tipo = null, addE_p_tipo;
        //*** Caso Epsilon ***//
        /*if (look.tag == '+' || look.tag == '-' || look.tag == Tag.EQ
         || look.tag == Tag.NE || look.tag == Tag.GE || look.tag == '<'
         || look.tag == '>' || look.tag == Tag.AND || look.tag == '('
         || look.tag == Tag.NUM || look.tag == Tag.TRUE || look.tag == Tag.FALSE
         || look.tag == ')') {
         tipo = null;
         } else {*/
        switch (look.tag) {
            case '+':
                match('+');
                multE_tipo = multE();
                code.emit(OpCode.iadd);
                addE_p_tipo = addE_p();
                //*** Type cheking ***//
                if (multE_tipo == Type.INTEGER
                        && (addE_p_tipo == Type.INTEGER || addE_p_tipo == null)) {
                    tipo = Type.INTEGER;
                } else {
                    error("Integer expected");
                }
                break;
            case '-':
                match('/');
                multE_tipo = multE();
                code.emit(OpCode.isub);
                addE_p_tipo = addE_p();
                //*** Type cheking ***//
                if (multE_tipo == Type.INTEGER
                        && (addE_p_tipo == Type.INTEGER || addE_p_tipo == null)) {
                    tipo = Type.INTEGER;
                } else {
                    error("Integer expected");
                }
                break;
        }
        //}

        return tipo;
    }

    public Type multE() {
        Type tipo = null, fact_tipo, multE_p_tipo;
        fact_tipo = fact();
        multE_p_tipo = multE_p();
        if (multE_p_tipo == null) {
            tipo = fact_tipo;
        } else if (fact_tipo == Type.INTEGER) {
            tipo = Type.INTEGER;
        } else {
            error("Integer expected");
        }
        return tipo;
    }

    public Type multE_p() {
        Type tipo = null, fact_tipo = null, multE_p_tipo;
        //*** Caso Epsilon ***//
        /*if (look.tag == '+' || look.tag == '-' || look.tag == Tag.EQ
         || look.tag == Tag.NE || look.tag == Tag.GE || look.tag == '<'
         || look.tag == '>' || look.tag == Tag.AND || look.tag == '('
         || look.tag == Tag.NUM || look.tag == Tag.TRUE || look.tag == Tag.FALSE
         || look.tag == ')') {
         tipo = null;
         } else {*/
        switch (look.tag) {
            case '*':
                match('*');
                fact_tipo = fact();
                code.emit(OpCode.imul);
                multE_p_tipo = multE_p();
                //*** Type cheking ***//
                if (fact_tipo == Type.INTEGER
                        && (multE_p_tipo == Type.INTEGER || multE_p_tipo == null)) {
                    tipo = Type.INTEGER;
                } else {
                    error("Integer expected");
                }
                break;
            case '/':
                match('/');
                fact_tipo = fact();
                code.emit(OpCode.idiv);
                multE_p_tipo = multE_p();
                //*** Type cheking ***//
                if (fact_tipo == Type.INTEGER
                        && (multE_p_tipo == Type.INTEGER || multE_p_tipo == null)) {
                    tipo = Type.INTEGER;
                } else {
                    error("Integer expected");
                }
                break;
        }
        //}

        return tipo;
    }

    public Type fact() {
        Type tipo = null;

        
        switch (look.tag) {
            case Tag.NUM:
                Word ww = (Word) look;
                code.emit(OpCode.ldc, Integer.parseInt(ww.lexeme));
                tipo = Type.INTEGER;
                match(Tag.NUM);
                break;
            case Tag.TRUE:
                code.emit(OpCode.ldc, 1);
                tipo = Type.BOOLEAN;
                match(Tag.TRUE);
                break;
            case Tag.FALSE:
                code.emit(OpCode.ldc, 0);
                tipo = Type.BOOLEAN;
                match(Tag.FALSE);
                break;
            case '(':
                match('(');
                tipo = orE();
                match(')');
                break;
        }
        return tipo;
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
            case ')':
                break;
            case Tag.EOF:
                break;
            default:
                error("Expected '+' or '-'");

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
            case '+':
                break;
            case '-':
                break;
            case ')':
                break;
            case Tag.EOF:
                break;
            default:
                error("Expected '*' or '/'");
        }
    }
    /*
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
     */

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        CodeGenerator c = new CodeGenerator();
        String filePath = new File("").getAbsolutePath();
        String path = filePath + File.separator + "program.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ProgramP parser = new ProgramP(lex, br, c);
            parser.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
