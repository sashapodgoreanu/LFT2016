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
public class EpsilonNFATest {
    
    public EpsilonNFATest() {
    }

    /**
     * Test of scan method, of class EpsilonNFA.
     */
    @Test
    public void testScan() {
        System.out.println("scan");
        assertEquals(true, EpsilonNFA.scan("ab"));
        assertEquals(true, EpsilonNFA.scan("aab"));
        assertEquals(true, EpsilonNFA.scan("aaaaaaaab"));
        assertEquals(true, EpsilonNFA.scan("baaaaaaa"));
        assertEquals(true, EpsilonNFA.scan("ba"));
        assertEquals(false, EpsilonNFA.scan("bab"));
        assertEquals(false, EpsilonNFA.scan("aba"));
        assertEquals(false, EpsilonNFA.scan("x"));
    }
    
}
