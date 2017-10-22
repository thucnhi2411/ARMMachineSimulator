import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * Simulate the program and the GUI
 *
 * @author Thuc Nhi Le
 * @version Oct 22nd, 2017
 */
public class Simulation extends JFrame implements ActionListener
{
    Machine machine;
    private java.util.Timer timer;
    private boolean isRunning = false;
    private String filename;
    private Button run_button, stop_button, 
    step_button, reset_button, exit_button; 
    private JTextField statusVal, instrVal, irVal,stageVal, nVal, zVal, cVal, vVal;
    private JTextField regValArr[];
    private JFrame memoryWindow;
    private JTable    memoryTable;
    private String[][] memoryTableData;
    boolean fetched = false;
    boolean decoded = false;
    boolean executed = false;
    boolean updated = false;

    public static void main(String[] args){
        Simulation sim = new Simulation();
    }

    /**
     * Constructor for objects of class Simulation
     */
    public Simulation()
    {
        filename = "file.as";
        machine = new Machine(filename);
        machineGUI();
    }

    /**
     * Draw the control part
     */
    public void machineGUI(){
        int regTextFieldLength = 48;

        getContentPane().setLayout(
            new BoxLayout(getContentPane(), BoxLayout.Y_AXIS)
        );

        ((JComponent)getContentPane()).setBorder( 
            BorderFactory.createEmptyBorder( 10, 10, 10, 10) );

        regValArr = new JTextField[machine.regNum];

        JPanel p0 = new JPanel();
        p0.setLayout(new GridLayout(2,2, 6, 6));
        p0.add(new JLabel("Status:", SwingConstants.CENTER));
        statusVal = new JTextField(machine.flag.getStatus(),SwingConstants.CENTER);
        statusVal.setEditable(false);
        p0.add(statusVal);
        
        p0.add(new JLabel("Stage: ", SwingConstants.CENTER));
        stageVal = new JTextField(machine.stages[machine.stage-1],SwingConstants.CENTER);
        stageVal.setEditable(false);
        p0.add(stageVal);

        JPanel p5 = new JPanel();
        p5.setLayout(new GridLayout(2,2, 6, 6));
        p5.add(new JLabel("Instruction: ", SwingConstants.CENTER));
        Register pc = machine.getRegList().get(9);
        instrVal = new JTextField(machine.getInstruction(pc.data).name,SwingConstants.CENTER);
        instrVal.setEditable(false);
        p5.add(instrVal);

        p5.add(new JLabel("Instruction Register: ", SwingConstants.CENTER));
        irVal = new JTextField(pc.value, SwingConstants.CENTER);
        irVal.setEditable(false);
        p5.add(irVal);

        JPanel p1 = new JPanel();
        p1.setLayout(new FlowLayout());
        step_button = new Button("Step");          
        p1.add(step_button);                       
        step_button.addActionListener(this);

        run_button = new Button("Run");          
        p1.add(run_button);                    
        run_button.addActionListener(this);

        stop_button = new Button("Stop");         
        p1.add(stop_button);                     
        stop_button.addActionListener(this);

        reset_button = new Button("Reset");       
        p1.add(reset_button);                      
        reset_button.addActionListener(this);

        exit_button = new Button("Exit");       
        p1.add(exit_button);                    
        exit_button.addActionListener(this);

        JPanel p2 = new JPanel();
        p2.setLayout(new GridLayout(2,2, 6, 6));

        p2.add(new JLabel("N: ", SwingConstants.CENTER));
        nVal = new JTextField(machine.flag.n?"1":"0", SwingConstants.LEFT);
        nVal.setEditable(false);
        p2.add(nVal);

        p2.add(new JLabel("Z: ", SwingConstants.CENTER));
        zVal = new JTextField(machine.flag.z?"1":"0", SwingConstants.LEFT);
        zVal.setEditable(false);
        p2.add(zVal);

        p2.add(new JLabel("C: ", SwingConstants.CENTER));
        cVal = new JTextField(machine.flag.c?"1":"0", SwingConstants.LEFT);
        cVal.setEditable(false);
        p2.add(cVal);

        p2.add(new JLabel("V: ", SwingConstants.CENTER));
        vVal = new JTextField(machine.flag.v?"1":"0", SwingConstants.LEFT);
        vVal.setEditable(false);
        p2.add(vVal);

        JPanel p4 = new JPanel();
        p4.setLayout(new GridLayout((machine.regNum)/2,2, 10, 10));

        for (int i = 0; i<machine.regNum-1; i++) {
            if (i < 7){
                p4.add(new JLabel("x" + i + " : ", SwingConstants.CENTER));
            } else if (i==7){
                p4.add(new JLabel("sp : ", SwingConstants.CENTER));
            } else if (i==8) {
                p4.add(new JLabel("fp : ", SwingConstants.CENTER));
            } else if (i==9) {
                p4.add(new JLabel("pc : ", SwingConstants.CENTER));
            }
            regValArr[i] = new JTextField(machine.getRegList().get(i).value, SwingConstants.LEFT);
            regValArr[i].setEditable(false);
            p4.add(regValArr[i]);
        }

        // Add the elements
        setTitle("Thuc Nhi Le - Machine Simulator"); 
        setSize(650, 500+50*((machine.regNum)/2));         

        // Add Simulation Control Pane
        JLabel button_label = new JLabel("Control buttons");
        button_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(button_label);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(p1);

        add(p0);

        JLabel i_label = new JLabel("Instruction");
        i_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(i_label);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(p5);
        // Add Flags Pane
        JLabel flag_label = new JLabel("Flags");
        flag_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(flag_label);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(p2);

        // Add General Registers Pane
        JLabel greg_label = new JLabel("Registers");
        greg_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(greg_label);
        add(Box.createRigidArea(new Dimension(20,20)));
        add(p4);   
        setVisible(true); 
        memoryWindow();
    }

    /**
     * Update the machine after each steps
     */
    public void updateMachine(){
        Register pc = machine.getRegList().get(9);
        Register ir = machine.getRegList().get(10);
        nVal.setText(machine.flag.n?"1":"0");
        zVal.setText(machine.flag.z?"1":"0");
        cVal.setText(machine.flag.c?"1":"0");
        vVal.setText(machine.flag.v?"1":"0");
        statusVal.setText(machine.flag.getStatus());
        instrVal.setText(machine.getInstruction(ir.data).name);
        stageVal.setText(machine.stages[machine.getStage()-1]);
        for (int i = 0; i<machine.regNum-1; i++){
            if (i<7){
                regValArr[i].setText(machine.getRegList().get(i).hexVal);
            } else {
                regValArr[i].setText(machine.getRegList().get(i).value);
            }

        }
        String s = "0x"+Integer.toHexString(pc.data + 8);
        regValArr[9].setText(s);
        irVal.setText(ir.value);
        drawMemory();
    }

    /**
     * Handle the button action
     * 
     * @param   evt    the action event
     */
    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getActionCommand().equals("Step")) {
            // Run 1 step
            machine.run();
        } else if(evt.getActionCommand().equals("Load")) {
            machine = new Machine(filename);
            fetched = true;
        } else if(evt.getActionCommand().equals("Reset")) {
            // Reset removes the old machine and makes a new one
            machine = new Machine(filename);
            if (isRunning) {
                timer.cancel();
                timer.purge();
                isRunning = false;
            }

        } else if (evt.getActionCommand().equals("Run")) {

            // If the program is not currently running then set up
            // a timer to run a set every second
            if (!isRunning) {
                timer = new java.util.Timer();
                timer.schedule(new RunTask(), 0, 500);
                isRunning = true;
            }

        } else if (evt.getActionCommand().equals("Stop")) {

            // If the program is already running then
            // pause the timer (by canceling th current timer
            // then where resuming make a new one)
            if (isRunning) {
                timer.cancel();
                timer.purge();
                isRunning = false;
            }

        } else if (evt.getActionCommand().equals("Exit")) {
            // Terminate the program
            System.exit(0); 
        } else {
            System.out.println("Unknown action command " + evt.getActionCommand());
        }

        // Update values of all registers and memory addresses in the GUI
        updateMachine();
        

    }

    /**
     * Add the memory Window
     */
    public void memoryWindow() {
        memoryWindow = new JFrame("Memory");
        memoryWindow.setTitle("Memory");  
        memoryWindow.setSize(200, 600);            
        memoryWindow.setResizable(false);
        drawMemory();
    }

    /**
     * Draw the content of the memory window
     */
    public void drawMemory() {
        memoryWindow.getContentPane().removeAll();
        String[] columnNames = {"Address",
                "Data"};

        memoryTableData = machine.mem.getMemTable();

        memoryTable = new JTable(memoryTableData, columnNames);

        memoryTable.setPreferredScrollableViewportSize(new Dimension(200, 600));
        memoryTable.setFillsViewportHeight(true);
        int curInstructionAddress = machine.pc.data/machine.wordsize;
        memoryTable.setRowSelectionInterval(curInstructionAddress, curInstructionAddress);

        JScrollPane js=new JScrollPane(memoryTable);
        js.setVisible(true);
        memoryWindow.add(js);
        memoryWindow.repaint();
        memoryWindow.setVisible(true);
        memoryWindow.setResizable(true);
    }

    /**
     * Class RunTask to handle the timer and the task each time
     */
    class RunTask extends TimerTask {
        @Override
        public void run() {
            machine.run();
            updateMachine();

            // If the machine is halted/error occured then stop the timer
            if (machine.flag.getStatus() != "AOK") {
                timer.cancel();
                timer.purge();
                isRunning = false;
            }
        }
    }
}
