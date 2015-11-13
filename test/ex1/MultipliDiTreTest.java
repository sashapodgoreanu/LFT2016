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
public class MultipliDiTreTest {
    
    public MultipliDiTreTest() {
    }

    /**
     * Test of scan method, of class MultipliDiTre.
     */
    @Test
    public void testScan() {
        System.out.println("scan");

        assertEquals(true, MultipliDiTre.scan("000"));//0
        assertEquals(false, MultipliDiTre.scan("001"));//1
        assertEquals(false, MultipliDiTre.scan("010"));//2
        assertEquals(true, MultipliDiTre.scan("011"));//3
        assertEquals(false, MultipliDiTre.scan("100"));//4
        assertEquals(false, MultipliDiTre.scan("101"));//5
        assertEquals(true, MultipliDiTre.scan("110"));//6
        assertEquals(false, MultipliDiTre.scan("111"));//7
        assertEquals(false, MultipliDiTre.scan("1000"));//8
        assertEquals(true, MultipliDiTre.scan("1001"));//9
        assertEquals(false, MultipliDiTre.scan("1010"));//10
    }
    
}
