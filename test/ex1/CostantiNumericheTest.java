/*
 * Universit� degli Studi di Torino
 * Dipartimento di Informatica
 */
package ex1;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexandru Podgoreanu
 */
public class CostantiNumericheTest {

    public CostantiNumericheTest() {
    }

    /**
     * Test of scan method, of class CostantiNumeriche.
     */
    @Test
    public void testScan() {
        System.out.println(this.getClass().getName() + " scan");
        assertEquals(true, CostantiNumeriche.scan("111"));
        assertEquals(true, CostantiNumeriche.scan("111.111"));
        assertEquals(true, CostantiNumeriche.scan("-111.111"));
        assertEquals(true, CostantiNumeriche.scan(".10"));
        assertEquals(true, CostantiNumeriche.scan("-.01"));
        assertEquals(false, CostantiNumeriche.scan("+.e-2"));
        assertEquals(true, CostantiNumeriche.scan("+.2e-2"));
        assertEquals(true, CostantiNumeriche.scan("2"));
    }
}
