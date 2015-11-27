package ex2;

public class Tag {

    public final static int EOF = -1,
            NUM = 256,
            ID = 257,
            AND = 258,
            OR = 259,
            VAR = 260,
            INTEGER = 261,
            BOOLEAN = 262,
            ASSIGN = 263,
            EQ = 264,
            GE = 265,
            LE = 266,
            NE = 267,
            TRUE = 268,
            FALSE = 269,
            NOT = 270,
            PRINT = 271,
            IF = 272,
            THEN = 273,
            ELSE = 274,
            WHILE = 275,
            BEGIN = 276,
            END = 277;

    public static String print(int tag) {
        String retVal = "";
        switch (tag) {
            case -1:
                retVal = "EOF";
                break;
            case 256:
                retVal = "NUM";
                break;
            case 257:
                retVal = "ID";
                break;
            case 258:
                retVal = "AND";
                break;
            case 259:
                retVal = "OR";
                break;
            case 260:
                retVal = "VAR";
                break;
            case 261:
                retVal = "INTEGER";
                break;
            case 262:
                retVal = "BOOLEAN";
                break;
            case 263:
                retVal = "ASSIGN";
                break;
            case 264:
                retVal = "EQ";
                break;
            case 265:
                retVal = "GE";
                break;
            case 266:
                retVal = "LE";
                break;
            case 267:
                retVal = "NE";
                break;
            case 268:
                retVal = "TRUE";
                break;
            case 269:
                retVal = "FALSE";
                break;
            case 270:
                retVal = "NOT";
                break;
            case 271:
                retVal = "PRINT";
                break;
            case 272:
                /**
                 * esercizio 2.2
                 */
                retVal = "IF";
                break;
            case 273:
                retVal = "THEN";
                break;
            case 274:
                retVal = "ELSE";
                break;
            case 275:
                retVal = "WHILE";
                break;
            case 276:
                retVal = "BEGIN";
                break;
            case 277:
                retVal = "END";
                break;
        }
        return retVal;
    }
}
