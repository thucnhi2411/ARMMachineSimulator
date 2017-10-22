import java.nio.*;
import java.lang.*;

/**
 * Register class represents both general and special registers
 * 
 * @author Thuc Nhi Le
 */
public class Register
{
    // instance variables - replace the example below with your own
    Integer data;
    private int wordSize;
    int id;
    String value = null;
    String hexVal = null;
    boolean strDouble = false;

    /**
     * Constructor of the register class
     * 
     * @param   wordSize    size of each register
     * @param   id          id for general register
     * @param   val         value of each register
     */
    public Register(int wordSize, int id, String val)
    {
        this.wordSize = wordSize;
        this.id = id;
    }

    /**
     * Set the decimal value for special register (sp, fp, pc, ir)
     * 
     * @param   x   the value
     */
    public void setData(Integer x){
        data = x;
        value = "0x"+Integer.toHexString(x);
    }

    /**
     * Set value for register
     * 
     * @param   val     the value
     */
    public void setVal(String val){
        if (isNum(val)) {
            value = DecToBin(val);
            hexVal = "0x"+binToHex(value);
        } else if (val.startsWith("x")) {
            return;
        } 
        if (id == 16 || id ==17){
            value = val;
        }
        
    }

    /**
     * Check whether the string can be converted into Integer
     * 
     * @param   strNum  the string to check
     * @return          whether the string can be converted into Integer
     */
    protected boolean isNum(String strNum) {
        boolean ret = true;
        try {
            Integer.parseInt(strNum);
        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

    /**
     * Convert from decimal to binary
     * 
     * @param   s   String that can be converted into decimal
     * @return      the binary string
     */
    protected String DecToBin(String s){
        return Integer.toBinaryString(Integer.parseInt(s));
    }

    /**
     * Convert from binary to hex
     * 
     * @param   s   binary string
     * @return      hex string
     */
    protected String binToHex(String s){
        int decimal = Integer.parseInt(s,2);
        String hexStr = Integer.toString(decimal,16);
        return hexStr;
    }
}
