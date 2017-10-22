

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class InstructionTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class InstructionTest
{
    /**
     * Default constructor for test class InstructionTest
     */
    public InstructionTest()
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
    public void TestDecodeOpcode(){
        Instruction i = new Instruction((byte)0x01);
        byte a = i.byteArr[1];
        assertEquals(a,(byte)1);
    }
    
    @Test
    public void TestDecodeOpcode2(){
        Instruction i = new Instruction((byte)0x02);
        byte a = i.byteArr[0];
        assertEquals(a,(byte)0);
    }
    
    @Test
    public void TestSetName(){
        Instruction i = new Instruction((byte)0x01);
        i.setName("abcd");
        assertEquals(i.name,"b");
    }
    
    @Test
    public void TestIsNum(){
        Instruction i = new Instruction((byte)0x01);
        boolean a = i.isNum("123");
        assertEquals(a,true);
    }
    
    @Test
    public void TestIsNum2(){
        Instruction i = new Instruction((byte)0x01);
        boolean a = i.isNum("123a");
        assertEquals(a,false);
    }
    
    @Test
    public void TestSetFunction(){
        Instruction i = new Instruction((byte)0x01);
        i.setFunction("abcd");
        assertEquals(i.func,"abcd");
    }
}
