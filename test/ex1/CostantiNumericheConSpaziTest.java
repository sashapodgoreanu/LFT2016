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
public class CostantiNumericheConSpaziTest {
    
    public CostantiNumericheConSpaziTest() {
    }

    /**
     * Test of scan method, of class CostantiNumericheConSpazi.
     */
    @Test
    public void testScan() {
        System.out.println(this.getClass().getName() + " scan");
        assertEquals(true, CostantiNumericheConSpazi.scan("111 123 123.5 .567 +7.5 -.7 67e10 1e-2 -.7e2"));
        //assertEquals(false, CostantiNumericheConSpazi.scan("111 123   123.5.567   +7.5-.7   67e10   1e-2   -.7e2"));
    }

}
