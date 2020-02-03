package search.algorithm;

import search.Node;
import search.NodeComparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Astar {

    private Node goalNode;
    private Node currentNode;
    private HashSet<Node> closeSet = new HashSet<>();
    private PriorityQueue<Node> openQueue;
    private boolean debug;

    public Astar(Node goalNode, boolean debug) {
        this.goalNode = goalNode;
        this.debug = debug;
        openQueue = new PriorityQueue<>(4, new NodeComparator());
    }


    public int main(Node initialState, int maxQueue) {
        //TODO : Check memory
        openQueue.add(initialState);

        int minH = initialState.getH();
        while((currentNode = openQueue.poll()) != null)
        {

            if (maxQueue <= openQueue.size()){
                System.err.println("ERROR: Out of bound open queue: " + openQueue.size());
                System.exit(1);
            }

            if (minH > currentNode.getH()) {
                minH = currentNode.getH();
                if (debug)
                    System.out.println("minH: " + minH + " openQueue=" + openQueue.size());
            }

            closeSet.add(currentNode);
            if (currentNode.equals(goalNode))
                return 1;

            int fCurrent = currentNode.getF();
            PriorityQueue<Node> children = currentNode.getSuccessors();
            Node child;
            while((child = children.poll()) != null) {
                int fChildren = child.getF();
//                if (!closeSet.contains(child) && !openQueue.contains(child) && fChildren <= fCurrent + 1) // TODO доработать условие
//                    openQueue.add(child);


                if (closeSet.contains(child))
                    continue;
                
                if (openQueue.contains(child))
                    continue;

                openQueue.add(child);

            }
        }
        System.out.println("A Star not found");
        System.exit(1);
        return 0;
    }

    public List<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();

        while (currentNode != null) {
            path.add(0, currentNode);
            currentNode = currentNode.getParent();
        }

        return path;
    }


}
