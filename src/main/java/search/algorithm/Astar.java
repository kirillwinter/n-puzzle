package search.algorithm;

import lombok.extern.slf4j.Slf4j;
import search.node.Node;
import search.node.NodeComparator;

import java.util.PriorityQueue;

@Slf4j
public class Astar extends AbstractAlgorithm {

    int maxQueue;

    public Astar(Node goalNode, int maxQueue) {
        this.goalNode = goalNode;
        this.maxQueue = maxQueue;
    }


    @Override
    public int main(Node root) {
        PriorityQueue<Node> openQueue = new PriorityQueue<>(4, new NodeComparator());

        openQueue.add(root);

        int minH = root.getH();
        Node currentNode;
        while ((currentNode = openQueue.poll()) != null) {
            countVisited++;

            if (minH > currentNode.getH()) {
                minH = currentNode.getH();
                log.debug("minH: " + minH + " openQueue=" + openQueue.size());
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
                if (!closeSet.contains(child) && !openQueue.contains(child) && fChildren <= fCurrent + 1 && maxQueue >= openQueue.size()) // TODO доработать условие
                    openQueue.add(child);
            }
        }
        log.error("A Star not found");
        printResult();
        System.exit(1);
        return 0;
    }


}
