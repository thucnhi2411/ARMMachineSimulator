import java.util.*;
/**
 * Machine class runs the simulation
 *
 * @author Thuc Nhi Le
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
    int stage;
    String[] stages;
    
    /**
     * Constructor of the machine, initializes and runs the program
     * 
     * @param   file    file name to parse into Assembler
     */
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
        stage = operation.stage;
        stages = new String[4];
        setStage();
        a.printImage();
        System.out.println("Finished initializing machine");
    }

    /**
     * Get one step in the operation run
     */
    public void run(){
        if (flag.getStatus() == "AOK"){
            operation.process(flag);
        }
    }
    
    /**
     * Get the operation class run
     */
    public void execute(){
        while (flag.getStatus() == "AOK"){
            operation.process(flag);
        }
    }
    
    /**
     * Return register list for GUI
     * 
     * @return  register list
     */
    public ArrayList<Register>  getRegList(){
        return regList;
    }
    
    public Instruction getInstruction(int k){
        return reverseAddrMap.get(k);
    }
    
    /**
     * Set the stage status 
     */
    public void setStage(){
        stages[0] = "FETCH";
        stages[1] = "DECODE";
        stages[2] = "EXECUTE";
        stages[3] = "UPDATE";
    }
    
    /**
     * Return the stage 
     * 
     * @return  the stage
     */
    public int getStage(){
        return operation.stage;
    }
}
