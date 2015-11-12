/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ex1;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Alexandru Podgoreanu
 */
public class TreZeriNonConsTest {
    
    public TreZeriNonConsTest() {
    }
    /**
     * Test of scan method, of class TreZeriNonCons.
     */
    @Test
    public void testScan() {
        System.out.println(this.getClass().getName()+" scan");
        assertEquals(true, TreZeriNonCons.scan("010101"));
        assertEquals(false, TreZeriNonCons.scan("000100010001"));
        assertEquals(false, TreZeriNonCons.scan("0001100010001"));
        assertEquals(false, TreZeriNonCons.scan("000"));
        assertEquals(true, TreZeriNonCons.scan("00"));
        assertEquals(true, TreZeriNonCons.scan("0"));
        assertEquals(true, TreZeriNonCons.scan("10"));
        assertEquals(true, TreZeriNonCons.scan("1"));
        assertEquals(false, TreZeriNonCons.scan("2"));
    }
    
}
