
/**
 * Flag class represents the flag and the status in the machine
 * 
 * @author Thuc Nhi Le
 */
public class Flag
{
    boolean n, z, c, v;
    private String status;

    /**
     * The constructor of the flag class 
     */
    public Flag()
    {
        n = false; //negative
        z = false; //zero
        c = false; //carry out
        v = false; //overflow
        status = "AOK";
    }
    
    /**
     * Change Negative flag
     * 
     * @param   n   boolean value
     */
    public void setN(boolean n)
    {
        this.n = n;
    }
    
    /**
     * Change Zero flag
     * 
     * @param   z   boolean value
     */
    public void setZ(boolean z)
    {
        this.z = z;
    }
    
    /**
     * Change Carry on flag
     * 
     * @param   c   boolean value
     */
    public void setC(boolean c)
    {
        this.c = c;
    }
    
    /**
     * Change Overflow flag
     * 
     * @param   v   boolean value
     */
    public void setV(boolean v)
    {
        this.v = v;
    }

    /**
     * Change status flag
     * 
     * @param   status   status of the program
     */
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    /**
     * Get the status of the program
     * 
     * @return  status of the program
     */
    public String getStatus()
    {
        return this.status;
    }
}
