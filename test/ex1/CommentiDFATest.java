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
public class CommentiDFATest {
    
    public CommentiDFATest() {
    }

    /**
     * Test of scan method, of class CommentiDFA.
     */
    @Test
    public void testScan() {
        System.out.println("scan");
        assertEquals(true, CommentiDFA.scan("aa"));
        assertEquals(true, CommentiDFA.scan("a/a"));
        assertEquals(true, CommentiDFA.scan("/**/"));
        assertEquals(true, CommentiDFA.scan("/* */"));
        assertEquals(true, CommentiDFA.scan("/* a */"));
        assertEquals(true, CommentiDFA.scan("/******/"));
        assertEquals(true, CommentiDFA.scan("/***a/*/"));
        assertEquals(true, CommentiDFA.scan("a/a/**/a/a")); 
        assertEquals(true, CommentiDFA.scan("a/a a*a /* ** a/a ** */ a/a a*a ")); 
        assertEquals(false, CommentiDFA.scan("/*")); 
        assertEquals(false, CommentiDFA.scan("*/")); 
        assertEquals(false, CommentiDFA.scan("a*/")); 
        assertEquals(false, CommentiDFA.scan("/*****/*/")); 

    }
    
}
