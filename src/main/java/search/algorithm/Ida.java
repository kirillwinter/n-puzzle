package search.algorithm;

import search.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Ida {

    private Node endPathNode;
    private Node goalNode;
    private long countVisited = 0;
    private HashSet<Node> close = new HashSet<>();
    private final static int FOUND = Integer.MIN_VALUE;
    private long countNotPut = 0;
    private boolean debug;

    public Ida(Node goalNode, boolean debug) {
        this.goalNode = goalNode;
        this.debug = debug;
    }

    public int main(Node root){

        int currentBound = root.getH();

        int i = 0;
        while (true){

            if (debug)
                System.out.println("i = " + i++ + " currentBound = " + currentBound);

            int smallestBound = search(root, currentBound);

            if (smallestBound == FOUND)
                return currentBound;

            if (smallestBound == Integer.MAX_VALUE){
                System.out.println("NO_FOUND");
                System.out.println("countVisited = " + countVisited);
                System.out.println("close = " + close.size());
                System.out.println("countNotPut = " + countNotPut);
                root.print();
                System.exit(1);
            }

            currentBound = smallestBound;
        }
    }

    public int search(Node node, int bound){
        countVisited++;

        close.add(node);

        if (node.getF() > bound){
            return node.getF();
        }


        // isGoal
        if (node.equals(goalNode)){
            System.out.println("FOUND");
            System.out.println("countVisited = " + countVisited);
            System.out.println("close = " + close.size());
            System.out.println("countNotPut = " + countNotPut);
            endPathNode = node;
            return FOUND;
        }

        int minBound = Integer.MAX_VALUE;

        PriorityQueue<Node> children = node.getSuccessors();
        while (!children.isEmpty()){
            Node child = children.poll();
            if(!close.contains(child)){
                int currentBound = search(child, bound);

                if (currentBound == FOUND){
                    return FOUND;
                }
                if (currentBound < minBound)
                    minBound = currentBound;
                close.remove(child);
            } else
                countNotPut++;

        }
        return minBound;
    }


    public List<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();
        path.add(endPathNode);

        while (endPathNode.getParent() != null) {
            path.add(0, endPathNode.getParent());
            endPathNode = endPathNode.getParent();
        }

        return path;
    }

}
