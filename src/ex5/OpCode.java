/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex5;

/**
 *
 * @author Alexandru Podgoreanu
 */
public enum OpCode {

    ldc, imul, ineg, idiv, iadd,
    isub, istore, ior, iand, iload,
    if_icmpeq, if_icmple, if_icmplt, if_icmpne, if_icmpge,
    if_icmpgt, ifne, GOto, invokestatic, label
}
