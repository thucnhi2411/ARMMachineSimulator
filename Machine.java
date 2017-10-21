import java.util.*;
/**
 * Write a description of class Machine here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Machine
{

    public static void main(String[] args){
        Assembler a = new Assembler("file.as");
        int memsize = a.getMemsize();
        int wordsize = a.getWordsize();
        ArrayList<Instruction> instrList = a.getInstrList();
        ArrayList<Register> regList = a.getRegList();
        HashMap<Instruction, Integer> addressMap = a.getAddrMap();
        HashMap<Integer, Instruction> reverseAddrMap = a.getReverseAddrMap();
        Memory mem = a.getMemory();
        Operation operation = new Operation(mem, instrList,regList,addressMap,reverseAddrMap);
        a.printImage();
        for (int i = 0; i<regList.size(); i++){
            System.out.println(regList.get(i).id+" "+regList.get(i).value);
        }
    }

}
