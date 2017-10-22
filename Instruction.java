import java.util.*;
import java.lang.*;
/**
 * Instructor class represents an instructor and decode the instructor
 *
 * @author Thuc Nhi Le
 */
public class Instruction
{
    HashMap<String, Byte> registerMap; //map register to byte
    HashMap<String, Byte> functionMap; //map function to byte
    ArrayList<Register> listReg = new ArrayList<Register>(); //list of register
    String p1 = null; //1st position
    String p2 = null; //2nd position
    String p3 = null; //3rd position
    String p4 = null; //4th position
    String func = null; //the function that the instructor belongs to
    String name = null; //the string represent the instructor
    byte[] byteArr = new byte[8];
    Byte b = 0; // represent opcode

    /**
     * Constructor for objects of class Intruction
     * 
     * @param   b   the byte representing opcode of the instruction
     */
    public Instruction(Byte b)
    {
        this.b = b;
        initRegisterMap();
        initFunctionMap();
        decodeOpcode(b);
    }

    /**
     * Decode the opcode into byte type
     * 
     * @param   b   the byte representing the opcode
     */
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

    /**
     * Decode the register and the immediate
     * 
     * @param   arr     the array of string that is parsed in Assembler
     */
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

    /**
     * Set name value
     * 
     * @param   s   the string that is parsed from the Assembler
     */
    public void setName(String s){
        s = s.substring(1,s.length()-2);
        name = s;
    }
    
    /**
     * Return the byte array of wordsize
     * 
     * @return  the byte array
     */
    public byte[] getByteArr(){
        return byteArr;
    }

    /**
     * Decode the stack
     * 
     * @param   the array of string that is parsed in Assembler
     */
    private void decodeStackOp(String[] arr){
        p1 = arr[1];
        mapping(arr[2],2,3);
        p2 = arr[2];
        mapping(arr[3],4,5);
        p3 = arr[3];
        mapping(arr[4],6,7);
        p4 = arr[4];
    }
    
    /**
     * Decode the MOV and FMOV instruction
     * 
     * @param   the array of string that is parsed in Assembler
     */
    private void decodeMOV(String[] arr){
        p1 = arr[1];
        mapping(arr[2],2,3); //arr[2]
        p2 = arr[2];
        byteArr[4] = 0;
        byteArr[5] = 0;
        mapping(arr[3], 6,7);
        p4 = arr[3];
    }
    
    /**
     * Decode the R, D, I type instructions
     * 
     * @param   the array of string that is parsed in Assembler
     */
    private void decodeRDI(String[] arr){
        p1 = arr[1];
        mapping(arr[2],2,3);
        p2 = arr[2];
        mapping(arr[3],4,5);
        p3 = arr[3];
        mapping(arr[4],6,7);
        p4 = arr[4];
    }

    /**
     * Decode the CBZ instruction
     * 
     * @param   the array of string that is parsed in Assembler
     */
    private void decodeCBZ(String[] arr){
        p1 = arr[1];
        mapping(arr[2],2,3);
        p2 = arr[2];
        byteArr[4] = 0;
        byteArr[5] = 0;
        mapping(arr[3],6,7);
        p4 = arr[3];
    }

    /**
     * Decode the BL instruction
     * 
     * @param   the array of string that is parsed in Assembler
     */
    private void decodeBL(String[] arr){
        p1 = arr[1];
        byteArr[2] = 0;
        byteArr[3] = 0;
        byteArr[4] = 0;
        byteArr[5] = 0;
        mapping(arr[2],6,7);
        p4 = arr[2];
    }

    /**
     * Map the register to the byte represent it in the memory
     * 
     * @param   s   the register
     * @param   first   the first position
     * @param   second  the second position
     */
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

    /**
     * Create the map to map the register to the byte representation
     */
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

    /**
     * Create the map to map the function to the byte representation
     */
    public void initFunctionMap() {
        // Fill Operation Code Table
        functionMap = new HashMap<String, Byte>();
        functionMap.put("main", (byte)0x50);
        functionMap.put("calculate", (byte)0x51);
        functionMap.put("calByte", (byte)0x02);
    }

    /**
     * Check if the string is a number for the parsing process
     * 
     * @param   strNum  the string to check
     * @return          whether the string can be parsed into integer 
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
     * Set the func value
     * 
     * @param   s   the string to set
     */
    public void setFunction(String s){
        func = s;
    }

    /**
     * Return the register list
     * 
     * @return  the register list
     */
    public ArrayList<Register> getListReg(){
        return listReg;
    }
    
}
