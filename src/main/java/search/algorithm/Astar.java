package search.algorithm;

import search.Node;
import search.NodeComparator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Astar {

    private IHeuristicFunction heuristicFunction;
    private Node goalNode;
    private Node currentNode;
    private HashSet<Node> closeQueue = new HashSet<>();
    private PriorityQueue<Node> openQueue;
    private boolean isGreedy;

    public Astar(IHeuristicFunction heuristicFunction, Node goalNode, boolean isGreedy) {
        this.heuristicFunction = heuristicFunction;
        this.goalNode = goalNode;
        this.isGreedy = isGreedy;
    }

    public List<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();

        while (currentNode != null) {
            path.add(0, currentNode);
            currentNode = currentNode.getParent();
        }

        return path;
    }

    public int main(Node initialState, int maxQueue, boolean debug) {
        //TODO : Check memory
        openQueue = new PriorityQueue<>(4, new NodeComparator());
        openQueue.add(initialState);

        int minH = initialState.getH();
        while((currentNode = openQueue.poll()) != null)
        {
            if (minH > currentNode.getH() && debug)
            {
                minH = currentNode.getH();
                System.out.println("minH: " + minH + " openQueue=" + openQueue.size());
            }

            closeQueue.add(currentNode);
            if (currentNode.equals(goalNode))
                return 1;

            int hCurrent = currentNode.getH();
            PriorityQueue<Node> childrens = currentNode.getSuccessors();
            Node children;
            while((children = childrens.poll()) != null)
            {
                int hChildren = children.getH();
                if (!closeQueue.contains(children)
                    && !openQueue.contains(children)
                    && hChildren <= hCurrent + 1
                    && openQueue.size() <= maxQueue
                    )
                    if (isGreedy)
                        children.setG(0);
                    openQueue.add(children);
                    //TODO ???
            }
        }
        System.out.println("A Star not found");
        System.exit(1);
        return 0;
    }


}
