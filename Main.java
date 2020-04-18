import java.io.File;
import java.util.Scanner;

/*
* Trevor Warthman
* Foundations 2
* Binary Tree Project
* "evenSumRange.java"
*/
public class evenSumRange {
    //define Node Structure
    public static class Node {
        long data;
        Node left;
        Node right;
        long oddSize;

        //constructor
        Node(long data) {
            this.right = null;
            this.left = null;
            this.data = data;
            if (data % 2 != 0) {
                this.oddSize = 1;
            } else {
                this.oddSize = 0;
            }

        }

        //Methods to count nodes in tree under node above or below max or min

        //Method which reports the number of odd nodes in a tree greater than or equal to a value
        long TreeCountOddGE(long min) {
            long oddCount = 0;
            Node v = this;
            while (v != null) {
                //if v is within range, take everything to the right and check left
                if (v.data >= min) {
                    //increase count
                    if (v.data % 2 != 0) {
                        oddCount++;
                    }
                    //search the right and left children
                    if (v.right != null) {
                        oddCount += v.right.oddSize;
                    }
                    v = v.left;
                }
                //if not within range search to the right for the range
                else {
                    v = v.right;
                }
            }
            return oddCount;
        }

        //Method which reports the number of odd nodes in a tree less than or equal to a value
        long TreeCountOddLE(long max) {
            long oddCount = 0;
            Node v = this;
            while (v != null) {
                if (v.data <= max) {
                    //increase count
                    if (v.data % 2 != 0) {
                        oddCount++;
                    }
                    //search the right and left children
                    if (v.left != null) {
                        oddCount += v.left.oddSize;
                    }
                    v = v.right;
                } else {
                    v = v.left;
                }
            }
            return oddCount;
        }
    }

    //Define Binary Tree Structure
    public static class BinarySearchTree {
        //contents of a BinaryTree
        Node root;

        //empty constructor of BT
        BinarySearchTree() {
            this.root = null;
        }

        //constructor for BT that makes a BT with a root node of value data
        BinarySearchTree(int data) {
            //assign the root even or odd based on data passed in
            Node root = new Node(data);
            this.root = root;
        }

        //Binary Tree Methods

        //Method to insert a Node into a Binary Tree structure by updating the root
        void BSTInsert(long value) {
            //assign even or odd to new node based on value
            boolean isOdd = false;
            if (value % 2 != 0) {
                isOdd = true;
            }
            this.root = this.addToRoot(this.root, value, isOdd);
        }

        //A recursive method to attach a value to a root in binary tree style (puts equal nodes to the right)
        Node addToRoot(Node root, long value, boolean isOdd) {
            // If the tree is empty or we reach the null node to replace, return node made from the value given
            if (root == null) {
                root = new Node(value);
                return root;
            }
            //increase the size of the odd count
            if (isOdd) {
                root.oddSize++;
            }
            //recurse down left or right
            if (value < root.data) {
                root.left = this.addToRoot(root.left, value, isOdd);
            } else if (value >= root.data) {
                root.right = this.addToRoot(root.right, value, isOdd);
            }
            return root;
        }

        //Method which counts the number of Nodes with odd values within a range in a BST (inclusive)
        long countOddNodesInRange(long min, long max) {
            long result = 0;
            Node v = this.root;
            //go to the first position where we are within the range
            while (v != null && (v.data > max || v.data < min)) {
                if (v.data < min) {
                    v = v.right;
                } else if (v.data > max) {
                    v = v.left;
                }
            }
            //do tree count GE and LE down left and right
            if (v != null) {
                long left = 0;
                long right = 0;
                if (v.left != null) {
                    left = v.left.TreeCountOddGE(min);
                }
                if (v.right != null) {
                    right = v.right.TreeCountOddLE(max);
                }
                //add one to one count for the root
                if (v.data % 2 != 0 && v.data >= min && v.data <= max) {
                    result = left + right + 1;
                } else {
                    result = left + right;
                }
            }
            return result;
        }

        /*
         * Binary Search Tree method which returns true if the sum of numbers
         * within a range would be even, false otherwise
         */
        boolean evenSumRange(long min, long max) {
            long odds = this.countOddNodesInRange(min, max);
            boolean isEven = sumIsEven(odds);
            return isEven;
        }
    }

    //Given number of odd numbers in the sum, returns true for even sum or odd for odd sum
    public static boolean sumIsEven(long numberOfOdd) {
        boolean even = true;
        if (numberOfOdd % 2 != 0) {
            even = false;
        }
        return even;
    }

    /* Main Method */
    public static void main(String[] args) {
        BinarySearchTree tree = new BinarySearchTree();
        //read data file and store values in tree
        String dataName = args[0];
        //read range file
        String rangeName = args[1];
        File dataX = new File(dataName);
        File rangeX = new File(rangeName);
        Scanner dataReader;
        //attempt to read from the data file and add to BinaryTree
        try {
            dataReader = new Scanner(dataX);
            //read from file while placing elements into a set
            while (dataReader.hasNext()) {
                int num = dataReader.nextInt();
                tree.BSTInsert(num);
            }
            dataReader.close();
        } catch (Exception e) {
            System.err.println("Error reading from the file " + dataName);
        }
        Scanner rangeReader;
        //attempt to read from the range file and add to BinaryTree
        try {
            rangeReader = new Scanner(rangeX);
            //read each range and compute if sum of tree nodes will be even or odd
            while (rangeReader.hasNext()) {
                long min = rangeReader.nextInt();
                long max = rangeReader.nextInt();
                //find out if the sum is even or odd
                boolean even = tree.evenSumRange(min, max);
                //print the if the sum is even or odd
                if (even) {
                    System.out.println(
                            "Range [" + min + "," + max + "]: even sum");
                } else {
                    System.out.println(
                            "Range [" + min + "," + max + "] odd sum ");
                }

            }
            rangeReader.close();
        } catch (Exception e) {
            System.err.println("Error reading from the file " + rangeName);
        }

    }
}
