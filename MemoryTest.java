

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class MemoryTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class MemoryTest
{
    /**
     * Default constructor for test class MemoryTest
     */
    public MemoryTest()
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
    public void testGetByte(){
        Memory m = new Memory(2,2);
        m.getMem()[0] = 1;
        int x = m.getByte(0);
        assertEquals(x,1);
    }
    
    @Test
    public void testGetByteArr(){
        Memory m = new Memory(4,2);
        m.getMem()[0] = 1;
        int x = m.getByteA(0)[0];
        assertEquals(x,1);
    }
    
    @Test
    public void testSetByte(){
        Memory m = new Memory(2,2);
        m.setByte(0,(byte)1);
        byte x = m.getByte(0);
        assertEquals(x,1);
    }
    
    @Test
    public void testBuildData(){
        Memory m = new Memory(2,2);
        byte[] b = new byte[2];
        b[0] = (byte) 2;
        b[1] = (byte) 1;
        String x = m.buildData(b);
        assertEquals(x,"21");
    }
    
    @Test
    public void testGetMemsize(){
        Memory m = new Memory(2,2);
        int x = m.getMemSize();
        assertEquals(x,2);
    }
}
