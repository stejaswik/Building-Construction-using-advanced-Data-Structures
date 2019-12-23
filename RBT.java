import java.util.Queue;
import java.util.LinkedList;
// RBT implementation, operations are done on node's building number
public class RBT {

    private final boolean Red = false; // declare global entity
    private final boolean Black = true; // declare global entity
    private final RBTNode nul = new RBTNode(-1,-1,-1, Black); // create a nul node containing dummy data
    private RBTNode rootNode = nul; // initialize root to nil node

    // returns a RBT node reference consisting input parameters if the insertion was successful
    // returns nul node if there are duplicate entries
    public RBTNode insertRBT(int bNum, int exTime, int toTime) {
        RBTNode base = rootNode;
        RBTNode buildingNode = new RBTNode(bNum, exTime, toTime, Black);
        if (rootNode == nul) {
            rootNode = buildingNode;
            nul.parent = nul;
            buildingNode.parent = nul;
            nul.left = nul;
            buildingNode.left = nul;
            nul.right = nul;
            buildingNode.right = nul;
        }
        else
        {
            buildingNode.colour = Red;
            while(buildingNode != nul) {
                if (buildingNode.bNum == base.bNum) {
                    buildingNode = nul;
                    break;
                } else if (buildingNode.bNum < base.bNum){
                    if (base.left == nul){
                        buildingNode.left = nul;
                        buildingNode.right = nul;
                        base.left = buildingNode;
                        buildingNode.parent = base;
                        break;
                    }
                    else if (base.left != nul){
                        base = base.left;
                    }
                } else{
                    if (buildingNode.bNum > base.bNum){
                        if (base.right == nul){
                            buildingNode.right = nul;
                            buildingNode.left = nul;
                            base.right = buildingNode;
                            buildingNode.parent = base;
                            break;
                        }
                        else if (base.right != nul){
                            base = base.right;
                        }
                    }
                }
            }
            if (buildingNode != nul) {
                // RBT properties must be applied before returning the node reference
                balanceRBT(buildingNode);
            }
        }
        return buildingNode;
    }

    // Balances the RBT structure to include the newly inserted node without violating any properties
    // Implements RRr, RLr, LRr, LLr and invokes RLb, RRb, LRb, LLb when required
    private void balanceRBT(RBTNode buildingNode){
        while(buildingNode.parent.colour == Red) {
            RBTNode z;
            if (buildingNode.parent == buildingNode.parent.parent.right) {
                z = buildingNode.parent.parent.left;
                if (z != nul && z.colour == Red) {
                    // RRr, RLr are handled below by changing the z node's colour to Black
                    RBTNode pp = buildingNode.parent;
                    RBTNode gp = buildingNode.parent.parent;
                    pp.colour = Black;
                    gp.colour = Red;
                    z.colour = Black;
                    buildingNode = gp;
                    if (buildingNode.parent != nul) {
                        continue;
                    }else if (buildingNode.parent == nul) {
                        // case where buildingNode's parent is the root
                        buildingNode.colour = Black;
                        continue;
                    }
                }
                if (buildingNode == buildingNode.parent.left) {
                    // During insertion if the imbalance created by newly entered node falls under RLb category,
                    // then the RLb function is invoked
                    RLb(buildingNode);
                } else {
                    // During insertion if the imbalance created by newly entered node falls under RRb category,
                    // then the RRb function is invoked
                    RRb(buildingNode);
                }
            } else {
                z = buildingNode.parent.parent.right;
                if (z != nul && z.colour == Red) {
                    // LRr, LLr are handled below by changing the z node's colour to Black
                    RBTNode pp = buildingNode.parent;
                    RBTNode gp = buildingNode.parent.parent;
                    pp.colour = Black;
                    gp.colour = Red;
                    z.colour = Black;
                    buildingNode = gp;
                    if (buildingNode.parent != nul) {
                        continue;
                    }else if(buildingNode.parent == nul){
                        // case where buildingNode's parent is the root
                        buildingNode.colour = Black;
                        continue;
                    }
                }
                if (buildingNode == buildingNode.parent.right) {
                    // During insertion if the imbalance created by newly entered node falls under LRb category,
                    // then the LRb function is invoked
                    LRb(buildingNode);
                } else {
                    // During insertion if the imbalance created by newly entered node falls under LLb category,
                    // then the LLb function is invoked
                    LLb(buildingNode);
                }
            }
        }
    }

    // pp is right child of gp, buildingNode is left child of pp and z is black
    private void RLb(RBTNode buildingNode) {
        buildingNode = buildingNode.parent;
        RRight(buildingNode);
        RBTNode pp = buildingNode.parent;
        RBTNode gp = buildingNode.parent.parent;
        pp.colour = Black;
        gp.colour = Red;
        RLeft(gp);
    }

    // pp is right child of gp, buildingNode is right child of pp and z is black
    private void RRb(RBTNode buildingNode) {
        RBTNode pp = buildingNode.parent;
        RBTNode gp = buildingNode.parent.parent;
        pp.colour = Black;
        gp.colour = Red;
        RLeft(gp);
    }

    // pp is left child of gp, buildingNode is right child of pp and z is black
    private void LRb(RBTNode buildingNode) {
        buildingNode = buildingNode.parent;
        RLeft(buildingNode);
        RBTNode pp = buildingNode.parent;
        RBTNode gp = buildingNode.parent.parent;
        pp.colour = Black;
        gp.colour = Red;
        RRight(gp);
    }

    // pp is left child of gp, buildingNode is left child of pp and z is black
    private void LLb(RBTNode buildingNode) {
        RBTNode pp = buildingNode.parent;
        pp.colour = Black;
        RBTNode gp = buildingNode.parent.parent;
        gp.colour = Red;
        RRight(gp);
    }

    // Right rotation
    private void RRight(RBTNode buildingNode) {
        if (buildingNode.parent == nul) {
            RBTNode tmp = rootNode.left;
            rootNode.left = rootNode.left.right;
            tmp.right.parent = rootNode;
            rootNode.parent = tmp;
            tmp.right = rootNode;
            tmp.parent = nul;
            rootNode = tmp;
        } else {
            if (buildingNode == buildingNode.parent.right) {
                buildingNode.parent.right = buildingNode.left;
            } else {
                buildingNode.parent.left = buildingNode.left;
            }
            buildingNode.left.parent = buildingNode.parent;
            buildingNode.parent = buildingNode.left;
            buildingNode.left = buildingNode.left.right;
            buildingNode.parent.right = buildingNode;
            if (buildingNode.left != nul) {
                buildingNode.left.parent = buildingNode;
            }
        }
    }

    // Left rotation
    private void RLeft(RBTNode buildingNode) {
        if (buildingNode.parent == nul) {
            RBTNode tmp = rootNode.right;
            rootNode.right = tmp.left;
            tmp.left.parent = rootNode;
            rootNode.parent = tmp;
            tmp.left = rootNode;
            tmp.parent = nul;
            rootNode = tmp;
        } else {
            if (buildingNode == buildingNode.parent.left) {
                buildingNode.parent.left = buildingNode.right;
            } else {
                buildingNode.parent.right = buildingNode.right;
            }
            buildingNode.right.parent = buildingNode.parent;
            buildingNode.parent = buildingNode.right;
            buildingNode.right = buildingNode.right.left;
            buildingNode.parent.left = buildingNode;
            if (buildingNode.right != nul) {
                buildingNode.right.parent = buildingNode;
            }

        }
    }

    // Deletes a node from the tree and resolves imbalance
    // Node being deleted falls into three categories: degree 0, degree 1 and degree 2
    // After deletion in order to balance the RBT properties, rebalanceRBT function is invoked.
    public void deleteNode(RBTNode buildingNode){
        RBTNode replNode;
        RBTNode temp;
        boolean color_delete_node = buildingNode.colour;
        // degree 0 deletion
        if(buildingNode.left == nul && buildingNode.right == nul){
            if(buildingNode.parent == nul){
                rootNode = nul;
            } else if(buildingNode.parent == rootNode){
                if(buildingNode == rootNode.left){
                    if(rootNode.right != nul) {
                        rootNode.left = nul;
                        rebalanceRBT(nul, rootNode);
                    } else {
                        rootNode.left = nul;
                        buildingNode.parent = nul;
                    }
                } else if(buildingNode == rootNode.right) {
                    if (rootNode.left != nul) {
                        rootNode.right = nul;
                        rebalanceRBT(nul, rootNode);
                    } else {
                        rootNode.right = nul;
                        buildingNode.parent = nul;
                    }
                }
            } else if(buildingNode == buildingNode.parent.left){
                if(buildingNode.parent.right == nul) {
                    buildingNode.parent.left = nul;
                    if (color_delete_node == Black) {
                        rebalanceRBT(nul,buildingNode.parent);
                    }
                } else if(buildingNode.parent.right != nul){
                    buildingNode.parent.left = nul;
                    if (color_delete_node == Black) {
                        rebalanceRBT(nul,buildingNode.parent);
                    }
                }
            } else if(buildingNode == buildingNode.parent.right) {
                if (buildingNode.parent.left == nul) {
                    buildingNode.parent.right = nul;
                    if (color_delete_node == Black) {
                        rebalanceRBT(nul,buildingNode.parent);
                    }
                } else if(buildingNode.parent.left != nul){
                    buildingNode.parent.right = nul;
                    if (color_delete_node == Black) {
                        rebalanceRBT(nul,buildingNode.parent);
                    }
                }
            }
        } else if(buildingNode.left == nul){ // degree 1 deletion
            if(buildingNode.parent == nul){
                rootNode = buildingNode.right;
                buildingNode.right.parent = nul;
            } else if(buildingNode == buildingNode.parent.left){
                buildingNode.parent.left = buildingNode.right;
                buildingNode.right.parent = buildingNode.parent;
                if (color_delete_node == Black) {
                    rebalanceRBT(buildingNode.parent.left,buildingNode.parent);
                }
            } else {
                buildingNode.parent.right = buildingNode.right;
                buildingNode.right.parent = buildingNode.parent;
                if (color_delete_node == Black) {
                    rebalanceRBT(buildingNode.parent.right,buildingNode.parent);
                }
            }
        } else if(buildingNode.right == nul){ // degree 1 deletion
            if(buildingNode.parent == nul){
                rootNode = buildingNode.left;
                rootNode.parent = nul;
            } else if(buildingNode == buildingNode.parent.right){
                buildingNode.parent.right = buildingNode.left;
                buildingNode.left.parent = buildingNode.parent;
                if (color_delete_node == Black) {
                    rebalanceRBT(buildingNode.parent.right,buildingNode.parent);
                }
            } else {
                buildingNode.parent.left = buildingNode.left;
                buildingNode.left.parent = buildingNode.parent;
                if (color_delete_node == Black) {
                    rebalanceRBT(buildingNode.parent.left,buildingNode.parent);
                }
            }
        } else {
            // degree 2 deletion
            replNode = getMinNode(buildingNode.right);
            color_delete_node = replNode.colour;
            temp = replNode.right;
            resolveDelete(replNode,buildingNode,temp);
            if (color_delete_node == Black){
                rebalanceRBT(temp, replNode);
            }else {
                if(replNode == rootNode && replNode.right == nul){
                    temp = rootNode.left;
                    Rr0(temp, rootNode);
                    rebalanceRBT(temp, replNode);
                }
            }
        }
    }

    // This is invoked when a two degree node is deleted and the smallest node from its right subtree is used to
    // replace the deleted node, all the dependencies are handled during replacement
    private void resolveDelete(RBTNode replaceNode, RBTNode deleteNode, RBTNode rightNode) {
        if (rightNode == nul && replaceNode.parent == deleteNode){
            if (deleteNode.parent == nul) {
                replaceNode.parent = nul;
                rootNode = replaceNode;
            } else if (deleteNode == deleteNode.parent.left){
                deleteNode.parent.left = replaceNode;
                replaceNode.parent = deleteNode.parent;
            } else if (deleteNode == deleteNode.parent.right){
                deleteNode.parent.right = replaceNode;
                replaceNode.parent = deleteNode.parent;
            }
        } else if (replaceNode.parent == deleteNode) {
            rightNode.parent = replaceNode;
            if (deleteNode == deleteNode.parent.left){
                deleteNode.parent.left = replaceNode;
                replaceNode.parent = deleteNode.parent;
            } else if (deleteNode == deleteNode.parent.right){
                deleteNode.parent.right = replaceNode;
                replaceNode.parent = deleteNode.parent;
            }
            if (deleteNode.parent == nul) {
                replaceNode.parent = nul;
                rootNode = replaceNode;
            }
        } else if (replaceNode.parent != deleteNode) {
            if (replaceNode == replaceNode.parent.left) {
                replaceNode.parent.left = rightNode;
                rightNode.parent = replaceNode.parent;
            } else if (replaceNode == replaceNode.parent.right) {
                replaceNode.parent.right = rightNode;
                rightNode.parent = replaceNode.parent;
            }
            if (deleteNode.parent == nul) {
                replaceNode.parent = nul;
                rootNode = replaceNode;
            } else if (deleteNode == deleteNode.parent.left){
                deleteNode.parent.left = replaceNode;
                replaceNode.parent = deleteNode.parent;
            } else if (deleteNode == deleteNode.parent.right){
                deleteNode.parent.right = replaceNode;
                replaceNode.parent = deleteNode.parent;
            }
            replaceNode.right = deleteNode.right;
            replaceNode.right.parent = replaceNode;
        }
        replaceNode.left = deleteNode.left;
        replaceNode.left.parent = replaceNode;
        replaceNode.colour = deleteNode.colour;
    }

    // This function is invoked when the RBT needs to be balanced to satisfy all the properties it exhibits
    // Based on the imbalance functions like Lr0, Lb0, Lb1_case1, Lb1_case2, Lb2, Rr0, Rb0, Rb1_case1,
    // Rb1_case2, Rb2 are invoked
    private void rebalanceRBT(RBTNode y, RBTNode py){
        while(y!= rootNode && y.colour == Black){
            if(y == py.left){
                RBTNode v = py.right;
                if(v.colour == Red){
                    Lr0(v, py);
                    v = py.right;
                }
                if(v.colour == Black || v.left.colour == Black && v.right.colour == Black){
                    Lb0(v);
                    y = py;
                    continue;
                }
                if(v.left.colour == Red && v.right.colour == Black){
                    Lb1_case1(v,py,y);
                    v = py.right;
                }
                if(v.left.colour == Black && v.right.colour == Red){
                    Lb1_case2(v,py,y);
                    y = rootNode;
                }
                if(v.left.colour == Red && v.right.colour == Red){
                    Lb2(v,py,y);
                    y = rootNode;
                }
            } else if(y == py.right){
                RBTNode v = py.left;
                if(v.colour == Red){
                    Rr0(v, py);
                    v = py.left;
                }
                if(v.left.colour == Black && v.right.colour == Black){
                    Rb0(v);
                    y = py;
                    continue;
                }
                if(v.left.colour == Black && v.right.colour == Red){
                    Rb1_case1(v,py,y);
                    v = py.left;
                }
                if(v.left.colour == Red && v.right.colour == Black){
                    Rb1_case2(v,py,y);
                    break;
                }
                if(v.left.colour == Red && v.right.colour == Red){
                    Rb2(v,py,y);
                    break;
                }
            }
        }
        // If y is the root node and its colour is Red
        // it changes the colour to Black
        if (y.colour == Red){
            y.colour = Black;
        }
    }

    // This function is invoked when y is Left child of py, colour of v node is Black and v node has 2 red children
    private  void Lb2(RBTNode v, RBTNode py, RBTNode y){
        v.colour = py.colour;
        py.colour = Black;
        v.right.colour = Black;
        RLeft(py);
    }

    // This function is invoked when y is Right child of py, colour of v node is Black and v node has 2 red children
    private  void Rb2(RBTNode v, RBTNode py, RBTNode y){
        v.colour = py.colour;
        py.colour = Black;
        v.left.colour = Black;
        RRight(py);
    }

    // This function is invoked when y is Left child of py, colour of v is Black and v node's left child colour is Black
    // and v's right child colour is Red
    private  void Lb1_case2(RBTNode v, RBTNode py, RBTNode y){
        v.colour = py.colour;
        py.colour = Black;
        v.right.colour = Black;
        RLeft(py);
    }

    // This function is invoked when y is Right child of py, colour of v is Black and v node's left child colour is Red
    // and v's right child colour is Black
    private  void Rb1_case2(RBTNode v, RBTNode py, RBTNode y){
        v.colour = py.colour;
        py.colour = Black;
        v.left.colour = Black;
        RRight(py);
    }

    // This function is invoked when y is Left child of py, colour of v is Black and v node's left child colour is Red
    // and v's right child colour is Black
    private void Lb1_case1(RBTNode v, RBTNode py, RBTNode y){
        v.left.colour = Black;
        v.colour = Red;
        RRight(v);
    }

    // This function is invoked when y is Right child of py, colour of v is Black and v node's left child colour is Black
    // and v's right child colour is Red
    private void Rb1_case1(RBTNode v,RBTNode py,RBTNode y){
        v.right.colour = Black;
        v.colour = Red;
        RLeft(v);
    }

    // This function is invoked when y is Left child of py, colour of v is Black and v doesn't have any red coloured children
    private void Lb0(RBTNode v){
        v.colour = Red;
    }

    // This function is invoked when y is Left child of py, colour of v is Red
    private void Lr0(RBTNode v, RBTNode py){
        v.colour = Black;
        py.colour = Red;
        RLeft(py);
    }

    // This function is invoked when y is Right child of py, colour of v is Red
    private void Rr0(RBTNode v, RBTNode py){
        v.colour = Black;
        py.colour = Red;
        RRight(py);
    }

    // This function is invoked when y is Right child of py, colour of v is Black and v doesn't have any red children
    private void Rb0(RBTNode v){
        v.colour = Red;
    }

    // Returns minimum RBT node, min node is the node that has smallest building number
    private RBTNode getMinNode(RBTNode parentRoot){
        RBTNode minNode;
        while(parentRoot.left!= nul){
            parentRoot = parentRoot.left;
        }
        minNode = parentRoot;
        return minNode;
    }

    // This function takes building number as input and returns a triplet consisting of (building number, execution time, total time) corresponding to the
    // building number specified
    public String print(int BN) {
        Queue<RBTNode> q = new LinkedList<>();
        String str = "";
        printBN(rootNode, BN, q);
        int size = q.size();
        if (size > 0) {
            RBTNode node = q.remove();
            str = str.concat("(" + node.getBN() + "," + node.getET() + "," + node.getTT() + ")");
        }
        else {
            str = "(0,0,0)";
        }
        return str;
    }

    // This function recursively looks for a match within a subtree and stores the
    // matching node value in the queue
    public void printBN(RBTNode sRoot, int BN, Queue<RBTNode> q) {
        if (sRoot.getBN() == BN) {
            q.add(sRoot);
        } else if (sRoot != nul) {
            if (sRoot.getBN() > BN) {
                printBN(sRoot.left, BN, q);
            } else if (sRoot.getBN() < BN) {
                printBN(sRoot.right, BN, q);
            }
        }
    }

    // This function takes a two building numbers as input and prints triplets of all the buildings within the range
    // BN1 to BN2
    public String print(int BN1, int BN2) {
        Queue<RBTNode> q = new LinkedList<>();
        String str = "";
        int i;
        printRange(rootNode, BN1, BN2, q);
        int size = q.size()-2;
        if (size+2 > 0) {
            for (i=0; i <= size ; i++){
                RBTNode node = q.remove();
                //System.out.print("(" + node.getBN() + "," + node.getET() + "," + node.getTT() + "),");
                str = str.concat("(" + node.getBN() + "," + node.getET() + "," + node.getTT() + "),");
            }
            RBTNode node = q.remove();
            str = str.concat("(" + node.getBN() + "," + node.getET() + "," + node.getTT() + ")");
        } else {
            str = "(0,0,0)";
        }
        return str;
    }

    // This function recursively looks for all the building values falling within the BN1 to BN2 range
    // and stores all the node references in the queue
    public void printRange(RBTNode node, int BN1, int BN2, Queue<RBTNode> q) {
        if (node == nul) {
            return;
        }
        if (node.getBN() >= BN1) {
            printRange(node.left, BN1, BN2, q);
        }
        if (node.getBN() >= BN1 && node.getBN() <= BN2) {
                q.add(node);
        }
        if (node.getBN() <= BN2) {
            printRange(node.right, BN1, BN2, q);
        }
    }
}

