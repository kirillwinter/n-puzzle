package search.algorithm;

import lombok.extern.slf4j.Slf4j;
import search.node.Node;

import java.util.PriorityQueue;

@Slf4j
public class Ida extends AbstractAlgorithm {

    private static final int FOUND = Integer.MIN_VALUE;

    public Ida(Node goalNode) {
        this.goalNode = goalNode;
    }

    @Override
    public int main(Node root) {

        int currentBound = root.getH();

        int i = 0;
        while (true) {

            log.debug("i = " + i++ + " currentBound = " + currentBound);

            int smallestBound = search(root, currentBound);

            if (smallestBound == FOUND)
                return currentBound;

            if (smallestBound == Integer.MAX_VALUE) {
                log.error("NO_FOUND \n" + root.toString());
                printResult();
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
            log.info("FOUND");
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
