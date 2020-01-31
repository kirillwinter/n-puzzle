package search.algorithm.astar_thread;

import search.Node;
import search.NodeComparator;
import search.algorithm.IHeuristicFunction;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AstarNew {
    private IHeuristicFunction heuristicFunction;
    private Node goalNode;
    private  Node resultNode;
    private HashSet<Node> closeQueue = new HashSet<>();
    private PriorityBlockingQueue<Node> openQueue;

    public AstarNew(IHeuristicFunction heuristicFunction, Node goalNode) {
        this.heuristicFunction = heuristicFunction;
        this.goalNode = goalNode;
    }

    public List<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();

        while (resultNode != null) {
            path.add(0, resultNode);
            resultNode = resultNode.getParent();
        }

        return path;
    }

    public int main(Node initialState, int maxQueue, boolean debug) {
        //TODO : Check memory
        openQueue = new PriorityBlockingQueue<>(4, new NodeComparator());
        openQueue.add(initialState);


        AtomicInteger minH = new AtomicInteger(initialState.getH());

        ExecutorService executor = Executors.newFixedThreadPool(7);


        PriorityQueue<Node> result = new PriorityQueue<>();
        for (int i = 0; i < 7; i++) {

            try {

                executor.submit(new AstarNewThread(heuristicFunction, goalNode, openQueue, closeQueue, minH, result)) ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (true) {
            resultNode = result.poll();
            if (resultNode != null){
                return 0;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
