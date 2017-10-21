import java.util.*;
import java.lang.*;
/**
 * Write a description of class Intruction here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Instruction
{
    HashMap<String, Byte> registerMap;
    HashMap<String, Byte> functionMap;
    ArrayList<Register> listReg = new ArrayList<Register>();
    String p1 = null;
    String p2 = null;
    String p3 = null;
    String p4 = null;
    String func = null;
    byte[] byteArr = new byte[8];
    Byte b = 0;

    /**
     * Constructor for objects of class Intruction
     */
    public Instruction(Byte b)
    {
        this.b = b;
        initRegisterMap();
        initFunctionMap();
        decodeOpcode(b);
    }

    public void decodeOpcode(Byte b){
        // for HALT, NOP, RET, STP, LDP
        if (b < 4 && b>0){
            byteArr[1] = b;
            for (int i = 0; i<byteArr.length; i++){
                if (i!=1){
                    byteArr[i] = 0;
                } 
            }
            // for others
        } else {
            String s = b.toString();
            byteArr[0] = (byte) Integer.parseInt(s.substring(0,1));
            byteArr[1] = (byte) Integer.parseInt(s.substring(1,2));
        }
    }

    public void decodeRegister(String[] arr){
        // for MOV, FMOV --> arr[2]:x0, arr[3]:doubleval
        for (int e = 0; e < arr.length; e++) {
            arr[e] = arr[e].replaceAll("[^\\w]", "");
        }
        if (b<4 && b>0){
            p1 = arr[1];
        } else if (b>=19 && b<22){
            decodeStackOp(arr);
        }
        else if (b>=16 && b<19){
            decodeMOV(arr);
        } else if (b>=32 && b<=64 ){ 
            // for type R, D, I -> arr[2]: x0, arr[3]: x29, arr[4]: #28
            decodeRDI(arr);
        } else if (b==80){
            // for CBZ  
            decodeCBZ(arr);
        } else if (b==81){
            // for BL one 
            decodeBL(arr);
        }

    }

    public byte[] getByteArr(){
        return byteArr;
    }

    private void decodeStackOp(String[] arr){
        p1 = arr[1];
        mapping(arr[2],2,3);
        p2 = arr[2];
        mapping(arr[3],4,5);
        p3 = arr[3];
        mapping(arr[4],6,7);
        p4 = arr[4];
    }
    
    private void decodeMOV(String[] arr){
        p1 = arr[1];
        mapping(arr[2],2,3); //arr[2]
        p2 = arr[2];
        byteArr[4] = 0;
        byteArr[5] = 0;
        mapping(arr[3], 6,7);
        p4 = arr[3];
    }

    private void decodeRDI(String[] arr){
        p1 = arr[1];
        mapping(arr[2],2,3);
        p2 = arr[2];
        mapping(arr[3],4,5);
        p3 = arr[3];
        mapping(arr[4],6,7);
        p4 = arr[4];
    }

    private void decodeCBZ(String[] arr){
        p1 = arr[1];
        mapping(arr[2],2,3);
        p2 = arr[2];
        byteArr[4] = 0;
        byteArr[5] = 0;
        mapping(arr[3],6,7);
        p4 = arr[3];
    }

    private void decodeBL(String[] arr){
        p1 = arr[1];
        byteArr[2] = 0;
        byteArr[3] = 0;
        byteArr[4] = 0;
        byteArr[5] = 0;
        mapping(arr[2],6,7);
        p4 = arr[2];
    }

    private void mapping(String s, int first, int second){
        Byte rb = 0;
        if (isNum(s)){
            rb = (byte) Integer.parseInt(s);
        } else if (registerMap.containsKey(s)) {
            rb = registerMap.get(s);
            if (!s.equals("Stack") && !s.equals("sp") && !s.equals("fp")){
                int id = Integer.parseInt(s.substring(1,s.length()));
                Register r = new Register(8,id,null);
                if (!listReg.contains(r)) listReg.add(r);
            } else if (s.equals("sp")){
                Register r = new Register(8,7,null);
                if (!listReg.contains(r)) listReg.add(r);
            } else if (s.equals("fp")){
                Register r = new Register(8,8,null);
                if (!listReg.contains(r)) listReg.add(r);
            }

        } else if (functionMap.containsKey(s)){
            rb = functionMap.get(s);
        }
        if (rb < 9){
            byteArr[first] = 0;
            byteArr[second] = rb;
        } else {
            s = rb.toString();
            byteArr[first] = (byte) Integer.parseInt(s.substring(0,1));
            byteArr[second] = (byte) Integer.parseInt(s.substring(1,2));
        }

    }

    public void initRegisterMap() {
        // Fill Operation Code Table
        registerMap = new HashMap<String, Byte>();
        registerMap.put("x0", (byte)0x00);
        registerMap.put("x1", (byte)0x01);
        registerMap.put("x2", (byte)0x02);
        registerMap.put("x3", (byte)0x03);
        registerMap.put("x4", (byte)0x04);
        registerMap.put("x5", (byte)0x05);
        registerMap.put("x6", (byte)0x06);
        registerMap.put("x7", (byte)0x07);
        registerMap.put("fp", (byte)0x29);
        registerMap.put("sp", (byte)0x40);
        registerMap.put("Stack", (byte)0x60);
    }

    public void initFunctionMap() {
        // Fill Operation Code Table
        functionMap = new HashMap<String, Byte>();
        functionMap.put("main", (byte)0x50);
        functionMap.put("calculate", (byte)0x51);
        functionMap.put("calByte", (byte)0x02);
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
    
    public void setFunction(String s){
        func = s;
    }

    public ArrayList<Register> getListReg(){
        return listReg;
    }
    
}
