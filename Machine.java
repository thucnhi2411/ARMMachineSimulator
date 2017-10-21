import java.util.*;
/**
 * Write a description of class Machine here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Machine
{
    Flag flag = new Flag();
    Memory mem;
    Operation operation;
    Assembler a;
    int regNum;
    int wordsize;
    Register pc;
    ArrayList<Register> regList;
    HashMap<Integer, Instruction> reverseAddrMap;
    public Machine(String file){
        System.out.println("Creating new machine ...");
        a = new Assembler(file);
        int memsize = a.getMemsize();
        wordsize = a.getWordsize();
        ArrayList<Instruction> instrList = a.getInstrList();
        regList = a.getRegList();
        regNum = regList.size();
        pc = regList.get(9);
        HashMap<Instruction, Integer> addressMap = a.getAddrMap();
        reverseAddrMap = a.getReverseAddrMap();
        mem = a.getMemory();
        operation = new Operation(mem, instrList,regList,addressMap,reverseAddrMap);
        a.printImage();
        System.out.println("Finished initializing machine");
    }

    public void run(){
        if (flag.getStatus() == "AOK"){
            operation.process(flag);
        }
    }
    
    public void execute(){
        while (flag.getStatus() == "AOK"){
            operation.process(flag);
        }
    }
    
    public ArrayList<Register>  getRegList(){
        return regList;
    }
    
    public Instruction getInstruction(int k){
        return reverseAddrMap.get(k);
    }
}
