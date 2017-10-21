import java.util.*;
import java.lang.*;
/**
 * Write a description of class Operation here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Operation
{
    // instance variables - replace the example below with your own
    ArrayList<Instruction> instrList;
    ArrayList<Register> regList;
    HashMap<Instruction, Integer> addressMap;
    Flag[] flagArr = new Flag[4];
    Memory mem;
    int prev = 0;
    /**
     * Constructor for objects of class Operation
     */
    public Operation(Memory m, ArrayList<Instruction> list1, ArrayList<Register>list2,HashMap<Instruction, Integer>map1)
    {
        initOp(list1,list2,map1);
        mem = m;
        initFlag();
        process();
    }

    public void initOp(ArrayList<Instruction> list1, ArrayList<Register>list2,HashMap<Instruction, Integer>map1){
        instrList = list1;
        regList = list2;
        addressMap = map1;
    }

    public void process(){
        Register pc = regList.get(9);
        outerloop:
        for (int k = 0; k<instrList.size(); k++){
            Instruction i = instrList.get(k);
            System.out.println(i.p1);
            Integer x = addressMap.get(i);

            switch (i.p1){
                case "MOV":
                    processMOV(i);
                    break;
                case "STP":
                    processSTP(i);
                    break;
                case "STUR":
                    processD(i);
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
                processBL(i);
                break;
                case "LDP":
                processLDP(i);
                break;
                case "HALT":
                break outerloop;
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
                case "CBNZ":
                processCBNZ(i);
                break;
                case "RET":
                processRET(i);
                break;
            }
        }
    }

    //MOV, FMOV
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

    //STP
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

    //LDUR, STUR
    public void processD(Instruction i){
        Register fp = regList.get(8);
        int a = fp.data -(prev - Integer.parseInt(i.p4));
        String s = "0x"+Integer.toString(a);
        fp.setData(a);   
        prev= Integer.parseInt(i.p4);
    }

    //BL
    public void processBL(Instruction i){
        for (int k = 0; k<instrList.size(); k++){
            Instruction i2 = instrList.get(k);
            if (i.p4.equals(i2.func)){

            }
        }
    }

    //LDP
    public void processLDP(Instruction i){
        Register sp = regList.get(7);
        Register fp = regList.get(8);
        int a = sp.data;
        a = a - Integer.parseInt(i.p4);
        sp.setData(a);
        fp.setData(a);
    }

    //ADD, ADDI
    public void processADD(Instruction i){
        int x1 = Integer.parseInt(i.p3.substring(1,i.p3.length()));
        Register r1 = regList.get(x1);
        int x2 = Integer.parseInt(i.p4);
        int number1 = Integer.parseInt(r1.value, 2);
        int sum = number1 + x2; 
        int x3 = Integer.parseInt(i.p2.substring(1,i.p2.length()));
        Register r3 = regList.get(x3);
        String s3 = Integer.toString(sum);
        r3.setVal(s3);
        if (s3.length() > r1.value.length()){
            flagArr[2].setC(true);
        }
    }
    
    //SUB
    public void processSUB(Instruction i){
        int x1 = Integer.parseInt(i.p3.substring(1,i.p3.length()));
        Register r1 = regList.get(x1);
        int x2 = Integer.parseInt(i.p4.substring(1,i.p4.length()));
        Register r2 = regList.get(x2);
        int number1 = Integer.parseInt(r1.value, 2);
        int number2 = Integer.parseInt(r2.value, 2);
        int sub = number1 - number2; 
        int x3 = Integer.parseInt(i.p2.substring(1,i.p2.length()));
        Register r3 = regList.get(x3);
        r3.setVal(Integer.toString(sub));
        if (sub==0) flagArr[1].setZ(true);

    }

    //AND
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

    //CBNZ
    public void processCBNZ(Instruction i){
    }

    //RET
    public void processRET(Instruction i){
    }

    public void initFlag(){
        for (int i = 0; i<flagArr.length; i++){
            //n, z, c, v
            Flag f = new Flag();
            flagArr[i] = f;
        }
    }
}
