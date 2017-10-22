import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 * Assembler class, parses file line by line, stores them in a HashMap and update the memory
 * for later translation to actions in the simulator
 * 
 * @author Thuc Nhi Le
 */
public class Assembler
{
    HashMap<String, Byte> opCodeMap; // map from the opcode instruction to byte representation
    HashMap<Instruction, Integer> addressMap = new HashMap<Instruction, Integer>(); // map from instruction to the address
    HashMap<Integer, Instruction> reverseAddrMap = new HashMap<Integer, Instruction> (); // map from address to instruction
    ArrayList<Instruction> instrList = new ArrayList<Instruction>(); //list of instruction
    ArrayList<Register> regList = new ArrayList<Register>();  //list of register
    int wordsize = 8;
    Memory memory; //memory
    int maxmem = (int) Math.pow(2,16); //maxmem 
    int regcnt = 0;
    String maxmemS = null;
    int pos = 0;
    Byte[] memArr = new Byte[maxmem];


    /**
     * Constructor the the assembler takes in the instruction set to create the image file
     * represent the memory
     * 
     * @param   filename    the name of input file
     */
    public Assembler(String filename)
    {
        initOpCodeMap();
        initRegList();
        memory = new Memory(maxmem,wordsize);
        for (int i = 0; i<memArr.length; i++){
            memArr[i] = 0;
        }
        parse(filename);
    }

    /**
     * Initialize the opcode map to map from opcode to byte representation
     * 
     */
    public void initOpCodeMap() {
        // Fill Operation Code Table
        opCodeMap = new HashMap<String, Byte>();
        opCodeMap.put("HALT", (byte)0x01);
        opCodeMap.put("NOP", (byte)0x02);
        opCodeMap.put("RET", (byte)0x03);
        opCodeMap.put("STP", (byte)0x13);        
        opCodeMap.put("LDP", (byte)0x14);  
        opCodeMap.put("MOV", (byte)0x10);
        opCodeMap.put("FMOV", (byte)0x11);        
        opCodeMap.put("LDUR", (byte)0x20);        
        opCodeMap.put("LDURSW", (byte)0x21);        
        opCodeMap.put("LDURH", (byte)0x22);        
        opCodeMap.put("LDURB", (byte)0x23);        
        opCodeMap.put("STUR", (byte)0x24);        
        opCodeMap.put("STURW", (byte)0x25);        
        opCodeMap.put("STURH", (byte)0x26);        
        opCodeMap.put("STURB", (byte)0x27);        
        opCodeMap.put("ADD", (byte)0x30);        
        opCodeMap.put("SUB", (byte)0x31);        
        opCodeMap.put("AND", (byte)0x32);        
        opCodeMap.put("ADDI", (byte)0x40);   
        opCodeMap.put("CBZ", (byte)0x50);    
        opCodeMap.put("BL", (byte)0x51);               
    }

    /**
     * Initialize the register list
     */
    public void initRegList(){
        for (int i = 0; i<7; i++){
            Register r = new Register(8,i,null);
            regList.add(r);
        }
        Register r2 = new Register(8,7,null); //sp
        Register r3 = new Register(8,8,null); //fp
        Register r4 = new Register(8,16,null); //pc
        Register r5 = new Register(8,17,null); //ir
        regList.add(r2);
        regList.add(r3);
        regList.add(r4);
        regList.add(r5);
        r4.setData(0);
        r5.setData(0);
    }

    /**
     * Reads the input file and stores in the maps and lists needed
     *
     * @param   filename    the name of the input file
     */
    public void parse(String filename) {
        try {
            System.out.println("Parsing file (" + filename + ")...");
            // Initialize Scanner
            File file = new File(filename);
            Scanner sc = new Scanner(file);

            String posS = null;
            // Begin Scanner
            while(sc.hasNext()) {
                // Read line by line
                String s = sc.nextLine();
                String[] arr = s.split("\\s+");
                for (int e = 0; e < arr.length; e++) {
                    arr[e] = arr[e].replaceAll("[^\\w]", "");
                }

                if (arr[0].equals("wordsize")){
                    wordsize = Integer.parseInt(arr[1]);
                } else if (arr[0].equals("regcnt")){
                    regcnt = Integer.parseInt(arr[1]);
                } else if (arr[0].equals("maxmem")){
                    maxmemS = arr[1];
                } else if (arr[0].equals("pos")) {
                    posS = arr[1];
                    pos = hexToDec(arr[1])*2;
                } else if (s.contains("Stack,")){
                    Instruction i = new Instruction(opCodeMap.get(arr[1]));
                    i.setName(s);
                    i.decodeRegister(arr);
                    addressMap.put(i,pos);
                    reverseAddrMap.put(pos,i);
                    instrList.add(i);
                    pos+=8;
                } else if (arr[0].equals("Stack")){
                    Byte rb = (byte)0x60;
                    String st = rb.toString();
                    for (int i = 0; i<wordsize; i++){
                        if (i%2==0){
                            memArr[pos+i] = (byte) Integer.parseInt(st.substring(0,1));
                        } else {
                            memArr[pos+i] = (byte) Integer.parseInt(st.substring(1,2)); 
                        }
                    }
                    Register sp = regList.get(7);
                    Register fp = regList.get(8); 
                    posS = posS.substring(2,posS.length());
                    int x = Integer.parseInt(posS,16);
                    sp.setData(x);
                    fp.setData(x);
                } else if (arr[0].equals("double")){
                    memArr[pos+15] = (byte) Integer.parseInt(arr[1].substring(2,3));
                    pos = pos + 16;
                } else if (arr[0].equals("single")){
                    memArr[pos+7] = (byte) Integer.parseInt(arr[1].substring(2,3));
                    pos = pos + 8;
                } else if (arr[0].equals("half")){
                    memArr[pos+3] = (byte) Integer.parseInt(arr[1].substring(2,3));
                    pos = pos + 3;
                }  else if (arr[0].equals("byte")){
                    memArr[pos+2] = (byte) Integer.parseInt(arr[1].substring(2,3));
                    pos = pos + 2;
                } else {
                    if (arr[0].startsWith("calculate") || arr[0].startsWith("main") 
                    || arr[0].startsWith("calByte")){
                        while (sc.hasNext()){
                            String ss = sc.nextLine();
                            String[] arr1 = ss.split("\\s+");
                            for (int e = 0; e < arr.length; e++) {
                                arr[e] = arr[e].replaceAll("[^\\w]", "");
                            }
                            Instruction i = new Instruction(opCodeMap.get(arr1[1]));
                            i.setName(ss);
                            i.setFunction(arr[0]);
                            i.decodeRegister(arr1);
                            addressMap.put(i,pos);
                            reverseAddrMap.put(pos,i);
                            instrList.add(i);

                            pos+=8;
                            if (arr1[1].startsWith("HALT") || arr1[1].startsWith("RET")) break;
                        }

                    }
                }

            }

            System.out.println("Finished parsing.");
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the image file representing the memory 
     */
    public void printImage(){
        try{
            // create the new file to write the output list
            PrintWriter output = new PrintWriter("file.o");
            output.println("#hex:WS-"+wordsize+":RC-"+regcnt+":MM-"+maxmemS);

            for (int i = 0; i<instrList.size(); i++){
                Instruction instr = instrList.get(i);
                int address = addressMap.get(instr);
                byte[] byteA = instr.getByteArr();
                for (int k = 0; k<byteA.length; k++){
                    memArr[address+k] = byteA[k];
                }
            }

            for (int i = 0; i<memArr.length; i++){
                memory.setByte(i, memArr[i]);
            }
            memory.buildHexArr();

            for (int i = 0; i< maxmem; i++){
                output.print(memory.hexArr[i]);
            }
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the instruction list
     * 
     * @return  instruction list
     */
    public ArrayList<Instruction> getInstrList(){
        return instrList;
    }

    /**
     * Get the register list
     * 
     * @return  register list
     */
    public ArrayList<Register> getRegList(){
        return regList;
    }

    /**
     * Get the address map
     * 
     * @return  address map
     */
    public HashMap<Instruction, Integer> getAddrMap(){
        return addressMap;
    }
    
    /**
     * Get the reverse address map
     * 
     * @return  reverse address map
     */
    public HashMap<Integer, Instruction> getReverseAddrMap(){
        return reverseAddrMap;
    }

    /**
     * Convert Hex to Dec
     * @param   hex     the hex number
     * @return          the dec number
     */
    protected int hexToDec(String hex){
        String subHex = hex.substring(2,hex.length());
        int h = Integer.parseInt(subHex,16); 
        String bin = Integer.toBinaryString(h);
        return Integer.parseInt(bin,2);
    }

    /**
     * Get word size
     * 
     * @return  wordsize
     */
    public int getWordsize(){
        return wordsize;
    }

    /**
     * Get max memory size
     * 
     * @return  max memory size
     */
    public int getMemsize(){
        return maxmem;
    }

    /**
     * Get the memory created
     * 
     * @return  memory object
     */
    public Memory getMemory(){
        return memory;
    }
}
