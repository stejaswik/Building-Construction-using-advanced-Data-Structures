// Implement min Heap, node's execution time field should be considered while
// storing the data. In case of a tie, building number is used to sort the nodes.
public class minHeap
{
    // variable declaration
    private HeapNode[] heapList; // list containing min heap node pointers
    private int len; // used to indicate size of min-Heap
    private int capacity; // used to indicate the maximum possible capacity of min heap
    public RBT rbt = new RBT(); // Creating an object of RBT class in order to access functions implemented in RBT

    // constructor, initializes min-Heap array size and adds first node to the heap
    public minHeap(int max)
    {
        heapList = new HeapNode[max+1]; // initializing maximum capacity to min-Heap
        HeapNode node = new HeapNode(0,0,0) ;
        heapList[0] = node; // use a dummy value at zeroth index
        this.capacity = max;
        this.len = 0; // this does not affect the length of Heap array
    }

    // inter changes index values of two heap nodes
    private void exchange(int child, int parent)
    {
        HeapNode var;
        var = heapList[child];
        heapList[child] = heapList[parent];
        heapList[parent] = var;
    }

    // inserts a new node with user specified data
    public int insert(int BN, int ET, int TT)
    {
        // checks if heap has capacity left for new node insertion
        if (capacity <= len)
        {
            return 0;
        }
        RBTNode ref = RBTNodeRef(BN, ET, TT);

        // only add nodes having unique building number to the min-Heap
        if (ref.getBN() != -1)
        {
            HeapNode node = new HeapNode(BN, ET, TT);
            len = len + 1;
            heapList[len] = node;
            int index = len;
            // modify heap contents after every insertion
            while (heapList[index].getET() < heapList[index / 2].getET()) {
                exchange(index, index / 2);
                index = index / 2;
            }
            node.setRef(ref); // set reference to RBT node
            return 1;
        }
        else
        {
            // throws error when another node with same building number is added
            System.out.println("Duplicate Node");
            return 0;
        }
    }

    // inserts an RBT node and returns the newly inserted node's reference
    public RBTNode RBTNodeRef(int BN, int ET, int TT)
    {
        RBTNode ref = rbt.insertRBT(BN, ET, TT);
        return ref;
    }

    // updates heap array until the nodes are sorted in order based on their Execution Time
    // Rules followed: 1. parent should have minimum value compared to its children
    //                 2. minimum value of the entire heap is stored as the root at index 0
    //                 3. left child has lesser execution time compared to right child
    //                 4. In case of a tie, building number is used to resolve the tie,
    //                 all the above three rules are applied using the building number
    public void heapify(int positionNode)
    {
        if (positionNode <= (len -1)/2)
        {
            if (heapList[positionNode].getET() > heapList[2 * positionNode].getET())
            { // Execution time is used to place the nodes in the min-Heap
                if (heapList[2*positionNode].getET() < heapList[2*positionNode+1].getET() )
                {
                    exchange(2*positionNode, positionNode);
                    heapify(2*positionNode);
                }
                else
                {
                    exchange(2*positionNode+1, positionNode);
                    heapify(2*positionNode+1);
                }
            }
            else if (heapList[positionNode].getET() > heapList[2 * positionNode+1].getET())
            {
                exchange(2*positionNode+1, positionNode);
                heapify(2*positionNode+1);
            }
            else if (heapList[positionNode].getET() == heapList[2*positionNode].getET() && heapList[positionNode].getET() == heapList[2*positionNode+1].getET())
            { // In case of tie, building number of the nodes is used to place them in the min-Heap
                if (heapList[positionNode].getBN() > heapList[2 * positionNode].getBN())
                {
                    if (heapList[2*positionNode].getBN() < heapList[2*positionNode+1].getBN())
                    {
                        exchange(2*positionNode, positionNode);
                        if (heapList[2*positionNode].getBN() > heapList[2*positionNode + 1].getBN())
                        {
                            // handles case when left child has larger execution time than right child
                            exchange(2*positionNode, 2*positionNode+1);
                        }
                        heapify(2*positionNode);
                    }
                    else
                    {
                        exchange(2*positionNode+1, positionNode);
                        heapify(2*positionNode+1);
                    }
                }
                else if (heapList[positionNode].getBN() > heapList[2 * positionNode+1].getBN())
                {
                    exchange(2*positionNode+1, positionNode);
                    heapify(2*positionNode+1);
                }
            }
            else if (heapList[positionNode].getET() == heapList[2*positionNode].getET())
            {
                if (heapList[positionNode].getBN() > heapList[2*positionNode].getBN())
                {
                    exchange(positionNode, 2*positionNode);
                    heapify(2*positionNode);
                }
            }
            else if (heapList[positionNode].getET() == heapList[2*positionNode+1].getET())
            {
                if (heapList[positionNode].getBN() > heapList[2*positionNode+1].getBN())
                {
                    exchange(positionNode, 2*positionNode+1);
                    heapify(2*positionNode+1);
                }
            }
        } else if(2*positionNode <= len) {
            // handles case when there is no right child to the parent node
            if(heapList[positionNode].getET() >= heapList[2 * positionNode].getET()) {
                exchange(2*positionNode, positionNode);
                heapify(2*positionNode);
            }
        }
    }

    // delete the root node which has the minimum execution time on the Heap
    // After deletion sort the heap to update arrangement
    public void deleteMin()
    {
        heapList[1] = heapList[len];
        len = len-1;
        heapify(1);
    }

    // Accesses RBT node by referring to the min-Heap minimum node data,
    // then deletes the RBT node from RBT
    public void deleteRBTNode(HeapNode min)
    {
        RBTNode ref = min.getRef();
        rbt.deleteNode(ref);
    }

    // Returns the node with minimum execution time
    public HeapNode getMin()
    {
        HeapNode min = heapList[1];
        return min;
    }

    // Updates execution time in the RBT node corresponding to the specified min-Heap node
    public void updateET(HeapNode node, int newET)
    {
        node.setET(newET);
        RBTNode ref = node.getRef();
        ref.setET(newET);
    }

    // Accesses RBT and prints the triplet (Building number, Execution time, Total time) for the specified node
    public String print(int BN)
    {
        String str = rbt.print(BN);
        return str;
    }

    // Accesses RBT and prints range of triplets for which BN1 <= bn <= BN2
    public String print(int BN1, int BN2)
    {
        String str = rbt.print(BN1,BN2);
        return str;
    }
}

