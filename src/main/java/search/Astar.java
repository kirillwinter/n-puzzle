package search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class Astar {
    public Astar(IHeuristicFunction heuristicFunction, Node goalNode) {
        this.heuristicFunction = heuristicFunction;
        this.goalNode = goalNode;
    }

    public List<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();

        while (currentNode != null) {
            path.add(0, currentNode);
            currentNode = currentNode.getParent();
        }

        return path;
    }

    public int main(Node initialState) {
        openQueue = new PriorityQueue<>(4, new NodeComparator());
        openQueue.add(initialState);
        while((currentNode = openQueue.poll()) != null)
        {
            closeQueue.add(currentNode);
            if (currentNode.equals(goalNode))
                return 0;
            PriorityQueue<Node> childrens = currentNode.getSuccessors();
            Node children;
            while((children = childrens.poll()) != null)
            {
                if (!closeQueue.contains(children))
                    openQueue.add(children);
            }
        }
        return 0;
    }

    private IHeuristicFunction heuristicFunction;
    private Node goalNode;
    private Node currentNode;
    private HashSet<Node> closeQueue = new HashSet<>();
    private PriorityQueue<Node> openQueue;
}
