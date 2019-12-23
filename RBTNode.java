// Implement RBT node
public class RBTNode
{
    // declare variables
    int bNum,exTime,toTime; // represent building number, execution time, total time respectively
    boolean colour; // indicates colour of current RBT node
    RBTNode left, right, parent; // left child, right child and parent pointers are stored for reference

    // initializes RBT node with user specified data
    RBTNode(int bNum, int exTime, int toTime, boolean col) {
        this.bNum = bNum;
        this.exTime = exTime;
        this.toTime = toTime;
        this.colour = col;
    }

    // returns building number of current RBT node
    public int getBN()
    {
        return bNum;
    }

    // returns updated execution time of current RBT node
    public int getET()
    {
        return exTime;
    }

    // returns total time of current RBT node
    public int getTT()
    {
        return toTime;
    }

    // Updates execution time of current RBT node
    public void setET(int newET) {
        this.exTime = newET;
    }
}