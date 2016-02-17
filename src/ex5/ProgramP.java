/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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
    private SymbolTable st;
    private int curent_adress;

    public ProgramP(Lexer l, BufferedReader br, CodeGenerator c, SymbolTable st) {
        lex = l;
        pbr = br;
        code = c;
        this.st = st;
        curent_adress = 0;
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
        throw new Error("near line " + lex.line + ": ");
        ///
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF) {
                move();
            }
        } else {
            error(this.getClass().getEnclosingMethod() + "syntax error");
        }
    }

    public void prog() { // la procedura start puo‘ essere estesa (opzionale)
        Integer[] avTags = new Integer[]{Tag.INTEGER, Tag.BOOLEAN, Tag.ID,
            Tag.PRINT, Tag.BEGIN, Tag.WHILE, Tag.IF, Tag.EOF};
        if (Arrays.asList(avTags).contains(look.tag)) {
            declist();
            stat();
            match(Tag.EOF);
            try {
                code.toJasmin();
            } catch (IOException ex) {
                Logger.getLogger(ProgramP.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax Error ");
        }
    }

    public void declist() {
        if (look.tag == Tag.INTEGER || look.tag == Tag.BOOLEAN) {
            dec();
            match(';');
            declist();
        } else {
            //*** EPSILON ***//
        }
    }

    public void dec() {
        Integer[] avTags = new Integer[]{Tag.INTEGER, Tag.BOOLEAN};
        if (Arrays.asList(avTags).contains(look.tag)) {
            Type tipo = type();
            if (look.tag == Tag.ID) {
                Word ww = (Word) look;
                st.insert(ww.lexeme, tipo, curent_adress++);
                match(Tag.ID);
                idlist(tipo);
            }
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax Error ");
        }
    }

    public Type type() {
        Type tipo = null;
        switch (look.tag) {
            case Tag.INTEGER:
                match(Tag.INTEGER);
                tipo = Type.INTEGER;
                break;
            case Tag.BOOLEAN:
                match(Tag.BOOLEAN);
                tipo = Type.BOOLEAN;
                break;
            default:
                error(this.getClass().getEnclosingMethod() + "Syntax Error ");
                break;
        }
        return tipo;
    }

    public void idlist(Type it) {
        if (look.tag == ',') {
            match(',');
            Word ww = (Word) look;
            match(Tag.ID);
            st.insert(ww.lexeme, it, curent_adress++);
            idlist(it);
        } else if (look.tag == ';') {
            //*** EPSILON ***//
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax Error ");
        }

    }

    public void stat() {
        Type exp_tipo, tipo;
        switch (look.tag) {
            case (Tag.ID):
                Word ww = (Word) look;
                match(Tag.ID);
                match(Tag.ASSIGN);
                int address = st.lookupAddress(ww.lexeme);
                tipo = st.lookupType(ww.lexeme);
                exp_tipo = exp();
                if (tipo == exp_tipo) {
                    code.emit(OpCode.istore, address);
                } else {
                    error(this.getClass().getEnclosingMethod() + "Expected: " + tipo + ", found: " + exp_tipo);
                }

                break;
            case (Tag.PRINT):
                match(Tag.PRINT);
                match('(');
                exp_tipo = exp();
                match(')');
                if (exp_tipo == Type.INTEGER) {
                    code.emit(OpCode.invokestatic, 1);
                } else {
                    code.emit(OpCode.invokestatic, 0);
                }
                break;
            case (Tag.BEGIN):
                match(Tag.BEGIN);
                statlist();
                match(Tag.END);
                break;
            case (Tag.IF):
                match(Tag.IF);
                tipo = exp();
                if (tipo != Type.BOOLEAN) {
                    error(this.getClass().getEnclosingMethod() + "Boolean expected");
                }
                int lif = code.newLabel();
                code.emit(OpCode.ldc, 0);
                code.emit(OpCode.if_icmpeq, lif);
                match(Tag.THEN);
                stat();
                if (look.tag == Tag.ELSE) {
                    match(Tag.ELSE);
                    int lelse = code.newLabel();
                    code.emit(OpCode.GOto, lelse);
                    code.emitLabel(lif);
                    stat();
                    code.emitLabel(lelse);
                } else {
                    code.emitLabel(lif);
                }

                break;
            case (Tag.WHILE):
                match(Tag.WHILE);
                int lwhile = code.newLabel();
                code.emitLabel(lwhile);
                tipo = exp();
                if (tipo != Type.BOOLEAN) {
                    error(this.getClass().getEnclosingMethod() + "Boolean expected");
                }
                code.emit(OpCode.ldc, 0);
                int ldo = code.newLabel();
                code.emit(OpCode.if_icmpeq, ldo);
                match(Tag.DO);
                stat();
                code.emit(OpCode.GOto, lwhile);
                code.emitLabel(ldo);
                break;
            default:
                Integer[] avTags = new Integer[]{
                    (int) ';',
                    Tag.ELSE, Tag.EOF, Tag.END};
                if (Arrays.asList(avTags).contains(look.tag)) {
                    //*** EPSILON ***/
                } else {
                    error(this.getClass().getEnclosingMethod() + "Syntax error");
                }
                break;
        }

    }

    public void statlist() {
        Integer[] avTags = new Integer[]{Tag.ID, Tag.PRINT, Tag.BEGIN, Tag.WHILE, Tag.IF};
        if (Arrays.asList(avTags).contains(look.tag)) {
            stat();
            statlist_p();
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax Error");
        }
    }

    public void statlist_p() {
        if (look.tag == ';') {
            match(';');
            stat();
            statlist_p();
        } else if (look.tag == Tag.END) {
            //*** EPSILON ***// 
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax Error");
        }
    }

    public Type exp() {
        Integer[] avTags = new Integer[]{(int) '(', Tag.ID, Tag.NUM, Tag.TRUE, Tag.FALSE};
        if (Arrays.asList(avTags).contains(look.tag)) {
            return orE();
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax error");
        }
        return null;
    }

    public Type orE() {
        Type tipo = null, andE_tipo, orE_p_tipo;
        Integer[] avTags = new Integer[]{(int) '(', Tag.ID, Tag.NUM, Tag.TRUE, Tag.FALSE};
        if (Arrays.asList(avTags).contains(look.tag)) {
            andE_tipo = andE();
            orE_p_tipo = orE_p();
            if (andE_tipo == Type.INTEGER && (orE_p_tipo == null || orE_p_tipo == Type.INTEGER)) {
                tipo = Type.INTEGER;
            } else if (andE_tipo == Type.BOOLEAN && (orE_p_tipo == null || orE_p_tipo == Type.BOOLEAN)) {
                tipo = Type.BOOLEAN;
            } else {
                error(this.getClass().getEnclosingMethod() + "Type Error");
            }
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax Error");
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
                error(this.getClass().getEnclosingMethod() + "Expected Boolean");
            }
        } else {
            Integer[] avTags = new Integer[]{(int) ')', (int) ';', Tag.DO, Tag.THEN, Tag.ELSE, Tag.END, Tag.EOF};
            if (!Arrays.asList(avTags).contains(look.tag)) {
                System.out.println(look.tag);
                System.out.println((int) ';');
                error(this.getClass().getEnclosingMethod() + "Syntax error");
            }
        }
        return tipo;
    }

    public Type andE() {

        Type tipo = null, relE_tipo, andE_p_tipo;
        Integer[] avTags = new Integer[]{(int) '(', Tag.ID, Tag.NUM, Tag.TRUE, Tag.FALSE};
        if (Arrays.asList(avTags).contains(look.tag)) {
            relE_tipo = relE();
            andE_p_tipo = andE_p();
            //*** Type cheking ***//
            if (andE_p_tipo == null) {
                tipo = relE_tipo;
            } else if (relE_tipo == Type.BOOLEAN && andE_p_tipo == Type.BOOLEAN) {
                tipo = Type.BOOLEAN;
            } else {
                error(this.getClass().getEnclosingMethod() + "Type error");
            }
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax error");
        }
        return tipo;
    }

    public Type andE_p() {
        Type tipo = null, relE_tipo, andE_p_tipo;
        Integer[] avTags = new Integer[]{(int) ';', (int) ')', Tag.OR, Tag.DO, Tag.THEN, Tag.ELSE, Tag.EOF, Tag.END};
        if (Arrays.asList(avTags).contains(look.tag)) {
            if (look.tag == Tag.AND) {
                match(Tag.AND);
                relE_tipo = relE();
                code.emit(OpCode.iand);
                andE_p_tipo = andE_p();
                if (relE_tipo == Type.BOOLEAN && (andE_p_tipo == null || andE_p_tipo == Type.BOOLEAN)) {
                    tipo = Type.BOOLEAN;
                } else {
                    error(this.getClass().getEnclosingMethod() + "Expected Boolean");
                }
            } else if (Arrays.asList(avTags).contains(look.tag)) {
                //*** EPSILON ***//
            } else {
                error(this.getClass().getEnclosingMethod() + "Syntax Error");
            }
        }
        return tipo;
    }

    public Type relE() {
        Type tipo = null, addE_tipo, relE_p_tipo;
        Integer[] avTags = new Integer[]{(int) '(', Tag.ID, Tag.NUM, Tag.TRUE, Tag.FALSE};
        if (Arrays.asList(avTags).contains(look.tag)) {
            addE_tipo = addE();
            relE_p_tipo = relE_p();
            //*** Type cheking ***//
            if (relE_p_tipo == null) {
                tipo = addE_tipo;
            } else if (addE_tipo == Type.INTEGER) {
                tipo = Type.BOOLEAN;
            } else {
                error(this.getClass().getEnclosingMethod() + "Integer expected");
            }
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax Error");
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
            default:
                Integer[] avTags = new Integer[]{(int) ';', (int) ')', Tag.OR, Tag.DO, Tag.THEN, Tag.ELSE, Tag.EOF, Tag.END, Tag.AND};
                if (Arrays.asList(avTags).contains(look.tag)) {
                    //*** EPSILON ***//
                } else {
                    error(this.getClass().getEnclosingMethod() + "Syntax Error");
                }
        }
        return tipo;
    }

    private Type typecheck(Type a) {
        //*** type cheking ***//
        if (a == Type.INTEGER) {
            return Type.INTEGER;
        } else {
            error(this.getClass().getEnclosingMethod() + "Integer expected");
        }
        return null;
    }

    public Type addE() {
        Type tipo = null, multE_tipo, addE_p_tipo;
        Integer[] avTags = new Integer[]{(int) '(', Tag.ID, Tag.NUM, Tag.TRUE, Tag.FALSE};
        if (Arrays.asList(avTags).contains(look.tag)) {
            multE_tipo = multE();
            addE_p_tipo = addE_p();
            //*** Type cheking ***//
            if (addE_p_tipo == null) {
                tipo = multE_tipo;
            } else if (multE_tipo == Type.INTEGER) {
                tipo = Type.INTEGER;
            } else {
                error(this.getClass().getEnclosingMethod() + "Integer expected");
            }
        } else {
            error(this.getClass().getEnclosingMethod() + "Syntax Error");
        }
        return tipo;
    }

    public Type addE_p() {
        Type tipo = null, multE_tipo = null, addE_p_tipo;
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
                    error(this.getClass().getEnclosingMethod() + "Integer expected");
                }
                break;
            case '-':
                match('-');
                multE_tipo = multE();
                code.emit(OpCode.isub);
                addE_p_tipo = addE_p();
                //*** Type cheking ***//
                if (multE_tipo == Type.INTEGER
                        && (addE_p_tipo == Type.INTEGER || addE_p_tipo == null)) {
                    tipo = Type.INTEGER;
                } else {
                    error(this.getClass().getEnclosingMethod() + "Integer expected");
                }
                break;
            default:
                Integer[] avTags = new Integer[]{Tag.EQ, Tag.NE, Tag.LE, Tag.GE,
                    (int) '<', (int) '>', (int) ')', (int) ';', Tag.AND, Tag.OR, Tag.DO, Tag.THEN, Tag.ELSE, Tag.EOF, Tag.END};
                if (Arrays.asList(avTags).contains(look.tag)) {
                    //*** Episoln ***//
                } else {
                    error(this.getClass().getEnclosingMethod() + "Syntax Error");
                }

                break;
        }
        return tipo;
    }

    //TO DO

    public Type multE() {
        Type tipo = null, fact_tipo, multE_p_tipo;
        fact_tipo = fact();
        multE_p_tipo = multE_p();
        if (multE_p_tipo == null) {
            tipo = fact_tipo;
        } else if (fact_tipo == Type.INTEGER) {
            tipo = Type.INTEGER;
        } else {
            error(this.getClass().getEnclosingMethod() + "Integer expected");
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
                    error(this.getClass().getEnclosingMethod() + "Integer expected");
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
                    error(this.getClass().getEnclosingMethod() + "Integer expected");
                }
                break;
        }
        //}

        return tipo;
    }

    public Type fact() {
        Type tipo = null;
        Word ww;
        switch (look.tag) {
            case Tag.NUM:
                ww = (Word) look;
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
                tipo = exp();
                match(')');
                break;
            case Tag.ID:
                //find in symboltable il  tipo del'ID
                ww = (Word) look;
                tipo = st.lookupType(ww.lexeme);
                if (tipo == null) {
                    error(this.getClass().getEnclosingMethod() + "Variable not defined");
                } else {
                    code.emit(OpCode.iload, st.lookupAddress(ww.lexeme));
                    match(Tag.ID);
                }
                break;
        }
        return tipo;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        CodeGenerator c = new CodeGenerator();
        SymbolTable st = new SymbolTable();
        String filePath = new File("").getAbsolutePath();
        String path = filePath + File.separator + "program.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            ProgramP parser = new ProgramP(lex, br, c, st);
            parser.prog();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
