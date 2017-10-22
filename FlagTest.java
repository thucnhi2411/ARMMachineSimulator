

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class FlagTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class FlagTest
{
    /**
     * Default constructor for test class FlagTest
     */
    public FlagTest()
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
    public void TestSetN(){
        Flag f = new Flag();
        f.setN(true);
        assertEquals(f.n,true);
    }
    
    @Test
    public void TestSetZ(){
        Flag f = new Flag();
        f.setZ(true);
        assertEquals(f.z,true);
    }
    
    @Test
    public void TestSetC(){
        Flag f = new Flag();
        f.setC(true);
        assertEquals(f.c,true);
    }
    
    @Test
    public void TestSetV(){
        Flag f = new Flag();
        f.setV(true);
        assertEquals(f.v,true);
    }
}
