import java.nio.*;
import java.lang.*;

/**
 * Abstract class Register - write a description of the class here
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

    /**
     * An example of a method - replace this comment with your own
     * 
     * @param  y    a sample parameter for a method
     * @return        the sum of x and y 
     */
    public Register(int wordSize, int id, String val)
    {
        this.wordSize = wordSize;
        this.id = id;

    }

    public void setData(Integer x){
        data = x;
        value = "0x"+Integer.toHexString(x);
    }

    public void setVal(String val){
        if (isNum(val)) {
            value = DecToBin(val);
            hexVal = "0x"+binToHex(value);
        } else if (val.startsWith("x")) {
            return;
        } 
        if (id == 16){
            value = val;
        }
        
    }

    private boolean isNum(String strNum) {
        boolean ret = true;
        try {
            Integer.parseInt(strNum);
        }catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

    private String DecToBin(String s){
        return Integer.toBinaryString(Integer.parseInt(s));
    }

    private String binToHex(String s){
        int decimal = Integer.parseInt(s,2);
        String hexStr = Integer.toString(decimal,16);
        return hexStr;
    }
}
