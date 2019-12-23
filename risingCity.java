import java.util.Scanner;
import java.io.*;

// Performs construction and executes commands specified by the user
public class risingCity {
    private long globalClock = 0; // Global clock
    private long constructionTIme = 1; // construction day indicator
    private long expectedTime = 0; // variable defined to perform construction in batches
    private long totalTime = 0; // stores the sum of the total time values of all the inserted buildings
    private minHeap minHeap = new minHeap(2000); // Min-Heap declaration
    private HeapNode minNode; // min node declaration
    BufferedWriter w; // used to write to a output file

    // Invoked when a write to the "output_file.txt" has to be performed
    public void put(String str) throws IOException {
        w = new BufferedWriter(new FileWriter("output_file.txt",true));
        w.write(str);
        w.append('\n');
        w.close();
    }

    // Handles two functionalities:
    // 1. Construction of existing buildings until globalClock reaches totalTime
    // 2. Execute commands specified by user which consists of- Insert and Print
    private void construction(String command) {
        String[] instruction = command.split(": ");
        if (globalClock == 0){
            try {
                w = new BufferedWriter(new FileWriter("output_file.txt",false));
            } catch (IOException prob) {
                prob.printStackTrace();
            }
        }
        // Only command execution is performed when global Clock is zero
        if (Integer.parseInt(instruction[0]) == globalClock) {
            executeCommand(instruction[1]);
            if (globalClock > 0) {
                executeConstruction();
            }
            globalClock++;
        }
        // Construction of existing building happens in the background until totalTime >= globalClock
        while (Integer.parseInt(instruction[0]) > globalClock ) {
            executeConstruction();
            globalClock++;
        }
        // Input command is prioritized whenever the day specified in the input matches with the globalClock day
        if (Integer.parseInt(instruction[0]) == globalClock) {
            executeCommand(instruction[1]);
            executeConstruction();
            globalClock++;
        }
    }

    // Construction will be completed only when all the buildings have their Execution time == Total time
    // This function is invoked when the input commands are executed and only construction of
    // building is remaining.
    private void completeConstruction(){
        while(totalTime >= globalClock ) {
            executeConstruction();
            globalClock++;
        }
    }

    // This is invoked whenever the day entered by user matched the globalClock time
    // Instruction provides in the input file is executed
    // There are three instructions possible: Insert(BN,TT), PrintBuilding(BN), PrintBuilding(BN1,BN2)
    // All the above cases are handled in the function
    private void executeCommand(String instruction){
        String[] command = instruction.split("[(\\,\\)]");

        if (command[0].compareTo("Insert") == 0){
            // Handles when the input is Insert(BN,TT)
            Integer BN = Integer.valueOf(command[1]);
            Integer TT = Integer.valueOf(command[2]);
            totalTime = totalTime + TT; // totalTime is updated after every insert
            Integer ET = 0;
            int check = minHeap.insert(BN, ET, TT);
            if (check == 0) {
                System.exit(1);
            }
        }
        else if (command[0].compareTo("PrintBuilding") == 0 && command.length > 2) {
            // Handles the case when the input is PrintBuilding(BN1, BN2)
            Integer BN1 = Integer.valueOf(command[1]);
            Integer BN2 = Integer.valueOf(command[2]);
            if (expectedTime == 0) {
                minNode = minHeap.getMin();
            }
            int ET = minNode.getET();
            int newET = ET+1;
            minHeap.updateET(minNode, newET);
            String str = minHeap.print(BN1, BN2);
            minHeap.updateET(minNode, ET);
            try {
                put(str);
            } catch (IOException issue) {
                issue.printStackTrace();
            }
         } else if (command[0].compareTo("PrintBuilding") == 0 && command.length == 2) {
            // Handles the case when the input is PrintBuilding(BN)
            Integer BN = Integer.valueOf(command[1]);
            if (expectedTime == 0) {
                minNode = minHeap.getMin();
            }
            int ET = minNode.getET();
            int newET = ET+1;
            minHeap.updateET(minNode, newET);
            String str = minHeap.print(BN);
            try {
                put(str);
            } catch (IOException issue) {
                issue.printStackTrace();
            }
            minHeap.updateET(minNode, ET);
        }
    }

    // This is invoked every day, starting from the time globalClock = 1.
    // This keeps track of the construction process.
    // When it is time to select a building to work on, the minimum node from the min Heap is selected.
    // The selected node is worked on until complete or for 5 days, whichever happens first.
    // If the node reaches the state, where its execution time == total time, then its building number and day of completion (globalClock) is output and it is removed from the RBT.
    // Otherwise, the building’s executed_time is updated.
    private void executeConstruction(){
        if (expectedTime == 0){
            // When it is time to select a building to work on, the building with the lowest executed_time (ties are broken by selecting the building with the lowest buildingNum) is selected.
            // In other words, the minimum node from min Heap is selected.
            minHeap.heapify(1);
            minNode = minHeap.getMin();
            int ET = minNode.getET();
            int TT = minNode.getTT();
            int BN = minNode.getBN();
            if ((TT-ET) > 5) {
                expectedTime = constructionTIme + 4;
            } else if (1 <= (TT-ET) && (TT-ET) <= 5) {
                // If the node selected is completing construction within 5 days, then it is removed from the min Heap and nodes reference
                // is preserved in "minHeap"
                expectedTime = constructionTIme + TT - ET - 1;
                minHeap.deleteMin();
            }
        }
        if (constructionTIme == expectedTime && globalClock > 0) {
            // If the building completes within 5 days, its number and day of completion is output and it is removed from the data structures.
            if (minNode.getET() < minNode.getTT()) {
                int ET1 = minNode.getET();
                ET1 = ET1 + 1;
                minHeap.updateET(minNode, ET1);
            }
            if (minNode.getET() == minNode.getTT()) {
                // This handles the case when the building completion is done and sends a output.
                // It deletes the node from RBT.
                String done = "("+minNode.getBN()+","+globalClock+")";
                try {
                    put(done);
                } catch (IOException error) {
                    error.printStackTrace();
                }
                minHeap.deleteRBTNode(minNode);
                expectedTime = 0;
            } else {
                // "heapify" is invoked so that the min Heap stays updated for the construction to pick up next minimum.
                // This condition is encountered after working on a building for 5 days.
                minHeap.heapify(1);
                expectedTime = 0; // This ensures that a new minimum is picked by the function to perform construction
            }
            constructionTIme++;
        } else if (constructionTIme < expectedTime && globalClock > 0) {
            // The node’s executed_time is updated, during on going construction.
            int ET1 = minNode.getET();
            ET1 = ET1 + 1;
            minHeap.updateET(minNode, ET1); // This ensures that both minHeap and RBT have updated their nodes execution time
            constructionTIme++;
        }
    }

    // This is invoked when parsing of input file is done, after all the commands are executed
    // the construction should go on.
    // Therefore, comparing globalClock with totalTime will ensure that all the nodes have reached convergence, where
    // their execution time == total time.
    // Returns 1 when construction is pending.
    public int checkCompletion() {
        if (totalTime < globalClock) {
            return 0;
        }else {
            return 1;
        }
    }

    // Closes write to "output_file.txt"
    void closeWrite() {
        try {
            w.close();
        } catch (IOException issue) {
            issue.printStackTrace();
        }
    }

    // main function takes input from text file and invokes functions from main class
    public static void main(String[] pars) {
        int len = pars.length;
        if(len > 0) {
            try{
                File input_file = new File(pars[0]); // take file name as an argument from user
                Scanner input = new Scanner(input_file);
                risingCity obj = new risingCity(); // create object of the class
                while (input.hasNextLine()) {
                    String command = input.nextLine();
                    obj.construction(command); // invoke functions
                }
                int check = obj.checkCompletion();
                if (check == 0) { // close the output file only when construction is done
                    input.close();
                    obj.closeWrite();
                } else { // calls construction
                    obj.completeConstruction();
                }
            } catch (FileNotFoundException issue) {
                issue.printStackTrace();
            }
        }
    }
}