// Implement min-Heap node
public class HeapNode
{
    // variable declaration
    int bNum; // represents building number of the min-Heap node
    int exTime; // represents execution time of the min-Heap node
    int toTime; // represents total time of the min-Heap node
    private RBTNode ref; // used to store RBT node reference corresponding to min-Heap node
    private final RBTNode nil = new RBTNode(-1,-1,-1, true); // nil value node defined is used as a default RBT node reference

    // initialize min-Heap node with user specified data
    public HeapNode(int bNum, int exTime, int toTime)
    {
        this.bNum = bNum;
        this.exTime = exTime;
        this.toTime = toTime;
        this.ref = nil;
    }

    // returns building number of current min-Heap node
    public int getBN()
    {
        return bNum;
    }

    // returns execution time of current min-Heap node
    public int getET()
    {
        return exTime;
    }

    // returns total time of current min-Heap node
    public int getTT()
    {
        return toTime;
    }

    // returns RBT node reference corresponding to current min-Heap node
    public RBTNode getRef()
    {
        return ref;
    }

    // updates execution time of current min-Heap node
    public void setET(int newET)
    {
        this.exTime = newET;
    }

    // updates RBT node reference of current min-Heap node
    public void setRef(RBTNode ref)
    {
        this.ref = ref;
    }

}
