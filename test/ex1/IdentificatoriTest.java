/*
 * Universita degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex1;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexandru Podgoreanu
 */
public class IdentificatoriTest {
    
    public IdentificatoriTest() {
    }

    /**
     * Test of scan method, of class Identificatori.
     */
    @Test
    public void testScan() {
        System.out.println("scan");
        assertEquals(true, Identificatori.scan("_aaaa"));
        assertEquals(true, Identificatori.scan("_Aaaa"));
        assertEquals(true, Identificatori.scan("_Aa1"));
        assertEquals(true, Identificatori.scan("a_Aa1"));
        assertEquals(false, Identificatori.scan("1_Aa1"));
        assertEquals(true, Identificatori.scan("aaa"));
        assertEquals(false, Identificatori.scan("1aaa"));
        assertEquals(false, Identificatori.scan("1"));
    }
    
}
