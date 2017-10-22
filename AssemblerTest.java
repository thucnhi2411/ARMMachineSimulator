

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class AssemblerTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class AssemblerTest
{
    /**
     * Default constructor for test class AssemblerTest
     */
    public AssemblerTest()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    
    @Test
    public void TestHexToDec(){
        Assembler a = new Assembler("abc");
        int x = a.hexToDec("0x01");
        assertEquals(x,1);
    }
    
    @Test
    public void TestHexToDec2(){
        Assembler a = new Assembler("abc");
        int x = a.hexToDec("0xa");
        assertEquals(x,10);
    }
    
    @Test
    public void TestGetWordsize(){
        Assembler a = new Assembler("abc");
        int x = a.getWordsize();
        assertEquals(x,8);
    }
    
}
