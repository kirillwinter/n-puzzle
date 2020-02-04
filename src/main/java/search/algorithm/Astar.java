package search.algorithm;

import search.node.Node;
import search.node.NodeComparator;

import java.util.PriorityQueue;

public class Astar extends AbstractAlgorithm {

    int maxQueue;

    public Astar(Node goalNode, int maxQueue, boolean debug) {
        this.goalNode = goalNode;
        this.debug = debug;
        this.maxQueue = maxQueue;
    }


    @Override
    public int main(Node root) {
        PriorityQueue<Node> openQueue = new PriorityQueue<>(4, new NodeComparator());

        //TODO : Check memory
        openQueue.add(root);

        int minH = root.getH();
        Node currentNode;
        while ((currentNode = openQueue.poll()) != null) {
            countVisited++;

            if (maxQueue <= openQueue.size()) {
                System.err.println("ERROR: Out of bound open queue: " + openQueue.size());
                printResult();
                System.exit(1);
            }

            if (minH > currentNode.getH()) {
                minH = currentNode.getH();
                if (debug)
                    System.out.println("minH: " + minH + " openQueue=" + openQueue.size());
            }

            closeSet.add(currentNode);
            if (currentNode.equals(goalNode)) {
                endPathNode = currentNode;
                printResult();
                return 1;
            }

            int fCurrent = currentNode.getF();
            PriorityQueue<Node> children = currentNode.getSuccessors();
            Node child;
            while ((child = children.poll()) != null) {
                int fChildren = child.getF();
//                if (!closeSet.contains(child) && !openQueue.contains(child) && fChildren <= fCurrent + 1) // TODO доработать условие
//                    openQueue.add(child);


                if (closeSet.contains(child) || openQueue.contains(child)) {
                    countNotPut++;
                    continue;
                }

                openQueue.add(child);

            }
        }
        System.out.println("A Star not found");
        printResult();
        System.exit(1);
        return 0;
    }


}
