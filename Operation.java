import java.util.*;
import java.lang.*;
/**
 * Operation class executes the instruction and the fetch-execute cycle.
 *
 * @author Thuc Nhi Le
 */
public class Operation
{
    // instance variables - replace the example below with your own
    ArrayList<Instruction> instrList;
    ArrayList<Register> regList;
    HashMap<Instruction, Integer> addressMap;
    HashMap<Integer, Instruction> reverseAddrMap;
    Memory mem;
    int prev = 0;
    int[] lastPos = new int[2];
    int returnTime = 0;
    int k = 0;
    int stage = 1;
    boolean n,z,c,v;
    /**
     * Constructor for objects of class Operation
     * 
     * @param   m       memory
     * @param   list1   instrList
     * @param   list2   regList
     * @param   map1    addressMap
     * @param   map2    reverseAddrMap
     */
    public Operation(Memory m, ArrayList<Instruction> list1, ArrayList<Register>list2,
    HashMap<Instruction, Integer>map1, HashMap<Integer, Instruction> map2)
    {
        initOp(list1,list2,map1,map2);
        mem = m;
    }

    /**
     * Initialize the lists and maps
     * 
     * @param   list1   instrList
     * @param   list2   regList
     * @param   map1    addressMap
     * @param   map2    reverseAddrMap
     */
    public void initOp(ArrayList<Instruction> list1, ArrayList<Register>list2,
    HashMap<Instruction, Integer>map1, HashMap<Integer, Instruction> map2){
        instrList = list1;
        regList = list2;
        addressMap = map1;
        reverseAddrMap = map2;
    }

    /**
     * Process the instruction
     * 
     * @param   flag    the status flag
     */
    public void process(Flag flag){
        if (flag.getStatus() == "HALT") return;
        Register pc = regList.get(9);
        Register ir = regList.get(10);
        if (stage == 1){
            // FETCH
            ir.setData(k);
            stage++;
            return;
        } else if ( stage == 2){
            // DECODE
            pc.setData(k);
            stage++;
            return;
        } else if ( stage == 3){
            // EXECUTE
            Instruction i = reverseAddrMap.get(k);
            switch (i.p1){
                case "MOV":
                    processMOV(i);
                    break;
                case "FMOV":
                    processMOV(i);
                    break;
                case "STP":
                    processSTP(i);
                    break;
                case "STUR":
                    processD(i);
                    int x = Integer.parseInt(i.p2.substring(1,i.p2.length()));
                    Register r1 = regList.get(x);
                    r1.strDouble = true;
                    break;
                case "STURH":
                    processD(i);
                    break;
                case "STURW":
                    processD(i);
                    break;     
                case "STURB":
                    processD(i);
                    break;                    
                case "LDUR":
                    processD(i);
                    break;
                case "LDURH":
                    processD(i);
                    break;
                case "LDURSW":
                    processD(i);
                    break;
                case "LDURB":
                    processD(i);
                    break;
                case "BL":
                    k = processBL(i, pc);
                    break;
                case "LDP":
                    processLDP(i);
                    break;
                case "HALT":
                    flag.setStatus("HALT");
                    return;
                case "ADD":
                    processADD(i);
                    break;
                case "ADDI":
                    processADD(i);
                    break;
                case "SUB":
                    processSUB(i);
                    break;
                case "AND":
                    processAND(i);
                    break;
                case "CBZ":
                    int x1 = Integer.parseInt(i.p2.substring(1,i.p2.length()));
                    Register r = regList.get(x1);
                    int number1 = Integer.parseInt(r.value, 2);
                    System.out.println(number1);
                    if (number1 == 0) k = processCBZ(i, pc);
                    break;
                case "RET":
                    k = processRET(i, pc);
                    break;
            }
            k += 8;
            stage++;
            return;
        } else if (stage == 4){
            // UPDATE
            if (n==true) flag.setN(true);
            if (z==true) flag.setZ(true);
            if (c==true) flag.setC(true);
            if (v==true) flag.setV(true);
            stage = 1;
            return;
        }


    }

    /**
     * Execute MOV, FMOV
     * 
     * @param   i   instruction to execute
     */
    public void processMOV(Instruction i){
        if (i.p2.equals("Stack")) return;
        if (i.p2.equals("fp") && i.p4.equals("sp")){
            Register sp = regList.get(7); //sp
            Register fp = regList.get(8); //fp
            fp.setData(sp.data);
        }else {
            int x = Integer.parseInt(i.p2.substring(1,i.p2.length()));
            Register r = regList.get(x);
            r.setVal(i.p4);
        }

    }

    /**
     * Execute stack push
     * 
     * @param   i   instruction to execute
     */
    public void processSTP(Instruction i){
        Register sp = regList.get(7);
        Register fp = regList.get(8);
        int a = sp.data;
        a = a + Integer.parseInt(i.p4);
        String s = "0x"+Integer.toString(a);
        sp.setData(a);
        fp.setData(a);
        prev = Integer.parseInt(i.p4);
    }

    /**
     * Execute D-type instructions
     * 
     * @param   i   instruction to execute
     */
    public void processD(Instruction i){
        Register fp = regList.get(8);
        int a = fp.data -(prev - Integer.parseInt(i.p4));
        String s = "0x"+Integer.toString(a);
        fp.setData(a);   
        prev= Integer.parseInt(i.p4);
    }

    /**
     * Execute stack pop
     * 
     * @param   i   instruction to execute
     */
    public void processLDP(Instruction i){
        Register sp = regList.get(7);
        Register fp = regList.get(8);
        int a = sp.data;
        a = a - Integer.parseInt(i.p4);
        sp.setData(a);
        fp.setData(a);
    }

    /**
     * Execute ADD, ADDI
     * 
     * @param   i   instruction to execute
     */
    public void processADD(Instruction i){
        int x1 = Integer.parseInt(i.p3.substring(1,i.p3.length()));
        Register r1 = regList.get(x1);
        String s1 = r1.value;
        int x2 = Integer.parseInt(i.p4);
        String s2 = Integer.toBinaryString(x2);
        String sum = addBinary(s1,s2);
        int x3 = Integer.parseInt(i.p2.substring(1,i.p2.length()));
        Register r3 = regList.get(x3);
        r3.setVal(sum);
        if (r3.value.length() > s1.length()){
            c = true;
            v = true;
        }
    }

    /**
     * Execute SUB
     * 
     * @param   i   instruction to execute
     */
    public void processSUB(Instruction i){
        int x1 = Integer.parseInt(i.p3.substring(1,i.p3.length()));
        Register r1 = regList.get(x1);
        int x2 = Integer.parseInt(i.p4.substring(1,i.p4.length()));
        Register r2 = regList.get(x2);
        String s1 = "0"+r1.value;
        System.out.println(s1);
        String s2 = twosCompliment(r2.value);
        System.out.println(s2);
        String sub = addBinary(s1,s2);
        int val = Integer.parseInt(sub, 2);
        int x3 = Integer.parseInt(i.p2.substring(1,i.p2.length()));
        Register r3 = regList.get(x3);
        r3.setVal(sub.substring(1,sub.length()));
        System.out.println(sub);
        System.out.println(val);
        if (val==0) z = true;
        if (val<0) n=true;
        c = true;

    }

    /**
     * Execute AND
     * 
     * @param   i   instruction to execute
     */
    public void processAND(Instruction i){
        int x1 = Integer.parseInt(i.p3.substring(1,i.p3.length()));
        Register r1 = regList.get(x1);
        int x2 = Integer.parseInt(i.p4.substring(1,i.p4.length()));
        Register r2 = regList.get(x2);
        int number1 = Integer.parseInt(r1.value, 2);
        int number2 = Integer.parseInt(r2.value, 2);
        int result = number1 & number2; 
        int x3 = Integer.parseInt(i.p2.substring(1,i.p2.length()));
        Register r3 = regList.get(x3);
        r3.setVal(Integer.toString(result));
    }

    /**
     * Execute BL
     * 
     * @param   i   instruction to execute
     * @param   pc  program counter
     */
    public int processBL(Instruction i, Register pc){
        for (int k = 0; k<instrList.size(); k++){
            Instruction i2 = instrList.get(k);
            if (i.p4.equals(i2.func)){
                pc.setData(addressMap.get(i2)-8);
                lastPos[0] = addressMap.get(i);
                returnTime++;
                break;
            }
        }
        return pc.data;
    }

    /**
     * Execute CBZ
     * 
     * @param   i   instruction to execute
     * @param   pc  program counter
     */
    public int processCBZ(Instruction i, Register pc){
        for (int k = 0; k<instrList.size(); k++){
            Instruction i2 = instrList.get(k);
            if (i.p4.equals(i2.func)){
                pc.setData(addressMap.get(i2)-8);
                lastPos[1] = addressMap.get(i);
                returnTime++;
                break;
            }
        }
        return pc.data;
    }

    /**
     * Execute RET
     * 
     * @param   i   instruction to execute
     * @param   pc  program counter
     */
    public int processRET(Instruction i, Register pc){
        if (i.func.equals("calByte")){
            pc.setData(lastPos[1]);
        } else {
            pc.setData(lastPos[0]);
        }
        return pc.data;
    }

    /**
     * Add Binary
     * 
     * @param   num1    first bin string
     * @param   num2    second bin string
     * @return  the sum in binary
     */
    protected String addBinary(String num1, String num2) {
        int p1 = num1.length() - 1;
        int p2 = num2.length() - 1;
        StringBuilder buf = new StringBuilder();
        int carry = 0;
        while (p1 >= 0 || p2 >= 0) {
            int sum = carry;
            if (p1 >= 0) {
                sum += num1.charAt(p1) - '0';
                p1--;
            }
            if (p2 >= 0) {
                sum += num2.charAt(p2) - '0';
                p2--;
            }
            carry = sum >> 1;
            sum = sum & 1;
            buf.append(sum == 0 ? '0' : '1');
        }
        if (carry > 0) {
            buf.append('1');
        }
        buf.reverse();
        return buf.toString();
    }
    
    /**
     * Find the 2's compliment of a binary string
     * 
     * @param   bin     binary string
     * @return  the 2's compliment of the binary string
     */
    public String twosCompliment(String bin) {
        String twos = "", ones = "";

        for (int i = 0; i < bin.length(); i++) {
            ones += flip(bin.charAt(i));
        }
        int number0 = Integer.parseInt(ones, 2);
        StringBuilder builder = new StringBuilder(ones);
        boolean b = false;
        for (int i = ones.length() - 1; i > 0; i--) {
            if (ones.charAt(i) == '1') {
                builder.setCharAt(i, '0');
            } else {
                builder.setCharAt(i, '1');
                b = true;
                break;
            }
        }
        if (!b)
            builder.append("1", 0, bin.length());

        twos = builder.toString();

        return twos;
    }

    /**
     * Flip the number
     * 
     * @param   c   the char(1/0)
     * @return  the flipped char (1 if 0, 0 if 1);
     */
    private char flip(char c) {
        return (c == '0') ? '1' : '0';
    }
}
