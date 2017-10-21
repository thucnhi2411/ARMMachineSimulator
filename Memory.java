import java.nio.*;
import java.util.*;
/**
 * The Memory (byte-addressed memory simulation), holds code and data in a linear data structure
 * 
 * @author Thuc Nhi Le
 */
public class Memory
{
    // array of bytes that holds the memory data
    private byte[] mem;
    private int wordSize, memSize;
    private String[][] memtable;

    /**
     * Constructor, makes a new Main memory of size memSize that reads and writes
     * in batches of size wordSize
     */
    public Memory(int memSize, int wordSize)
    {
        // initialise memory array
        mem = new byte[memSize];
        this.memSize = memSize;
        this.wordSize = wordSize;
        for (int i = 0; i<mem.length; i++){
            mem[i] = 0;
        }
    }

    public byte[] getMem(){
        return mem;
    }
    
    /**
     * Access memory data through address
     * 
     * @param  addr the address of the bytes accessed
     * @return  data at address specified
     */
    public byte getByte(int addr)
    {
        byte result = mem[addr]; 
        return result;
    }
    
    private byte[] getByteA(int addr){
        byte[] result = new byte[wordSize];
        System.arraycopy(mem, addr, result, 0, wordSize);   
        return result;
    }

    
    /**
     * Write to memory at address
     * 
     * @param  addr the address of the bytes to write to
     * @param  data the data to write
     */
    public void setByte(int addr, byte data)
    {
        mem[addr] = data;

    }
    
    /**
     * Method byteToHex, converts a byte array (usually word sized) to its
     * hex representation (as a string)
     *
     * @param data word-sized byte array
     * @return hex representation of the bytes
     */
    protected String byteToHex(byte[] data) {
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            sb.append(b);
        }
        return sb.toString().trim();
    }
    
    /**
     * Method getContentHex returns the addresses and contents of the memory
     * as an array of hex formatted string (for display in the GUI
     *
     * @return hex representation of the memory content
     */
    public String[][] getContentHex() {
        String[][] result = new String[this.memSize/this.wordSize][2];
        for (int i = 0; i<this.memSize/this.wordSize; i+=this.wordSize) {
            result[i/this.wordSize][0] = "0x"+Integer.toHexString(i);
            result[i/this.wordSize][1] = byteToHex(getByteA(i));
        }
        return result;
    }
    
    public String[][] initialMem(){
        String[][] result = new String[this.memSize/this.wordSize][2];
        for (int i = 0; i<this.memSize/this.wordSize; i+=this.wordSize) {
            result[i/this.wordSize][0] = "0x"+Integer.toHexString(i);
            result[i/this.wordSize][1] = "0000000";
        }
        return result;
    }
    
    public int getMemSize(){
        return memSize;
    }
    

}
