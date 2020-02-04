package search.algorithm;

import search.node.Node;

import java.util.PriorityQueue;

public class Ida extends AbstractAlgorithm {

    private static final int FOUND = Integer.MIN_VALUE;

    public Ida(Node goalNode, boolean debug) {
        this.goalNode = goalNode;
        this.debug = debug;
    }

    @Override
    public int main(Node root) {

        int currentBound = root.getH();

        int i = 0;
        while (true) {

            if (debug)
                System.out.println("i = " + i++ + " currentBound = " + currentBound);

            int smallestBound = search(root, currentBound);

            if (smallestBound == FOUND)
                return currentBound;

            if (smallestBound == Integer.MAX_VALUE) {
                System.out.println("NO_FOUND");
                printResult();
                root.print();
                System.exit(1);
            }

            currentBound = smallestBound;
        }
    }

    public int search(Node node, int bound) {
        countVisited++;

        closeSet.add(node);

        if (node.getF() > bound) {
            return node.getF();
        }


        // isGoal
        if (node.equals(goalNode)) {
            System.out.println("FOUND");
            printResult();
            endPathNode = node;
            return FOUND;
        }

        int minBound = Integer.MAX_VALUE;

        PriorityQueue<Node> children = node.getSuccessors();
        while (!children.isEmpty()) {
            Node child = children.poll();
            if (!closeSet.contains(child)) {
                int currentBound = search(child, bound);

                if (currentBound == FOUND) {
                    return FOUND;
                }
                if (currentBound < minBound)
                    minBound = currentBound;
                closeSet.remove(child);
            } else
                countNotPut++;
        }
        return minBound;
    }


}
