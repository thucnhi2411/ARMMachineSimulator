

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class RegisterTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class RegisterTest
{
    /**
     * Default constructor for test class RegisterTest
     */
    public RegisterTest()
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
    public void TestSetData(){
        Register r = new Register(8,1,"a");
        r.setData(2);
        int a = r.data;
        assertEquals(a,2);
    }
    
    @Test
    public void TestSetVal(){
        Register r = new Register(8,1,"a");
        r.setVal("1");
        String a = r.value;
        assertEquals(a,"1");
    }
    
    @Test
    public void TestDecToBin(){
        Register r = new Register(8,1,"a");
        String x = r.DecToBin("3");
        assertEquals(x,"11");
    }
    
    @Test
    public void TestbinToHex(){
        Register r = new Register(8,1,"a");
        String x = r.binToHex("10");
        assertEquals(x,"2");
    }
}
