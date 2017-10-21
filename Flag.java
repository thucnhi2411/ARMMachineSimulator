
/**
 * Write a description of class Flags here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Flag
{
    private boolean n, z, c, v;
    private String status;

    public Flag()
    {
        n = false; //negative
        z = false; //zero
        c = false; //carry out
        v = false; //overflow
        status = "AOK";
    }
    
    public void setN(boolean n)
    {
        this.n = n;
    }
    
    public void setZ(boolean z)
    {
        this.z = z;
    }
    
    public void setC(boolean c)
    {
        this.c = c;
    }
    
    public void setV(boolean v)
    {
        this.v = v;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public boolean getN()
    {
        return this.n;
    }
    
    public boolean getZ()
    {
        return this.z;
    }
    
    public boolean getC()
    {
        return this.c;
    }    

    public boolean getV()
    {
        return this.v;
    }
    
    public String getStatus()
    {
        return this.status;
    }
}
