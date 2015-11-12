/*
 * Università degli Studi di Torino - Dipartimento di Informatica
 */
package ex1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author Alexandru Podgoreanu
 */
public class TreZeriTest {

    public TreZeriTest() {
    }

    /**
     * Test of scan method, of class TreZeri.
     */
    @Test
    public void testScan() {
        System.out.println(this.getClass().getName()+" scan");
        assertEquals(false, TreZeri.scan("010101"));
        assertEquals(true, TreZeri.scan("000100010001"));
        assertEquals(true, TreZeri.scan("0001100010001"));
        assertEquals(true, TreZeri.scan("000"));
        assertEquals(false, TreZeri.scan("00"));
        assertEquals(false, TreZeri.scan("1"));
        assertEquals(false, TreZeri.scan("2"));
    }

}
