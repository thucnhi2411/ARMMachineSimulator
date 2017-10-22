import java.nio.*;
import java.util.*;
/**
 * The Memory class represents the memory and uses an array to hold data.
 * 
 * @author Thuc Nhi Le
 */
public class Memory
{
    // array of bytes that holds the memory data
    private byte[] mem;
    String[] hexArr;
    private int wordSize, memSize;
    private String[][] memtable;

    /**
     * Constructor for class Memory
     */
    public Memory(int memSize, int wordSize)
    {
        // initialise memory array
        mem = new byte[memSize];
        hexArr = new String[memSize];
        this.memSize = memSize;
        this.wordSize = wordSize;
        for (int i = 0; i<mem.length; i++){
            mem[i] = 0;
        }
    }

    /**
     * Return the array of bytes which represents the memory
     * 
     * @return  array of memory
     */
    public byte[] getMem(){
        return mem;
    }
    
    /**
     * Get memory data at the specific address
     * 
     * @param   addr    the address of the bytes 
     * @return  data at address specified
     */
    public byte getByte(int addr)
    {
        byte result = mem[addr]; 
        return result;
    }
    
    /**
     * Get the array of bytes of size wordsize at the specific address
     * 
     * @param   addr    the address 
     * @return  the array of bytes at the address
     */
    protected byte[] getByteA(int addr){
        byte[] result = new byte[wordSize];
        System.arraycopy(mem, addr, result, 0, wordSize);   
        return result;
    }

    
    /**
     * Write to memory at address
     * 
     * @param  addr     the address of the bytes to write to
     * @param  data     the data to write
     */
    public void setByte(int addr, byte data)
    {
        mem[addr] = data;
    }
    
    /**
     * Convert the array of byte to a string of size wordsize
     *
     * @param   data    word-sized byte array
     * @return  the string created from the byte array
     */
    protected String buildData(String[] data) {
        StringBuilder s = new StringBuilder();
        for (String b : data) {
            s.append(b);
        }
        return s.toString().trim();
    }
    
    /**
     * Returns the addresses and data of the memory
     *
     * @return  representation of the memory content
     */
    public String[][] getMemTable() {
        String[][] result = new String[this.memSize/this.wordSize][2];
        for (int i = 0; i<this.memSize/this.wordSize; i+=this.wordSize) {
            result[i/this.wordSize][0] = "0x"+Integer.toHexString(i);
            result[i/this.wordSize][1] = buildData(getHexData(i));
        }
        return result;
    }
    
    /**
     * Return memory size
     * 
     * @return  memory size
     */
    public int getMemSize(){
        return memSize;
    }
    
    /**
     * Get the array of hex of size wordsize at the specific address
     * 
     * @param   addr    the address 
     * @return  the array of hex at the address
     */
    public String[] getHexData(int addr){
        String[] result = new String[wordSize];
        System.arraycopy(hexArr, addr, result, 0, wordSize);   
        return result;
    }
    
    /**
     * Convert to hex
     */
    public void buildHexArr(){
        for (int i = 0; i<mem.length; i+=2){
            StringBuilder s = new StringBuilder();
            s.append(mem[i]);
            s.append(mem[i+1]);
            String s1 = s.toString().trim();
            s1 = Integer.toHexString(Integer.parseInt(s1));
            if (s1.length()<2) s1 = "0"+s1;
            hexArr[i] = s1.substring(0,1);
            hexArr[i+1] = s1.substring(1,2);
        }
    }
    
    

}
