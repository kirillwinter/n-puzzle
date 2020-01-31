package search.algorithm.astar_thread_for_children;

import search.Node;
import search.NodeComparator;
import search.algorithm.IHeuristicFunction;
import search.algorithm.astar_thread.AstarNewThread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class AstarAlgThreadForChildren {

    private IHeuristicFunction heuristicFunction;
    private Node goalNode;
    private Node currentNode;
    private HashSet<Node> closeQueue = new HashSet<>();
    private PriorityBlockingQueue<Node> openQueue;

    public AstarAlgThreadForChildren(IHeuristicFunction heuristicFunction, Node goalNode) {
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

    public int main(Node initialState, int maxQueue, boolean debug) {
        //TODO : Check memory
        openQueue = new PriorityBlockingQueue<>(4, new NodeComparator());
        openQueue.add(initialState);

        int minH = initialState.getH();
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        AstarChildrenAddThread astarChildrenAddThread = new AstarChildrenAddThread();
//        executor.submit(astarChildrenAddThread) ;

        while (true) {
            currentNode = openQueue.poll();
            if (currentNode == null){
                continue; // чтобы треды не закрывались сразу же
            }

            if (minH > currentNode.getH() && debug)
            {
                minH = currentNode.getH();
                System.out.println("minH: " + minH + " openQueue=" + openQueue.size());
            }

            closeQueue.add(currentNode);
            if (currentNode.equals(goalNode))
                return 1;

            int hCurrent = currentNode.getH();

            CompletableFuture.supplyAsync(() -> {
                PriorityQueue<Node> childrens = currentNode.getSuccessors();
                Node children;
                while ((children = childrens.poll()) != null) {
                    int hChildren = children.getH();
                    if (!closeQueue.contains(children)
                            && !openQueue.contains(children)
                            && hChildren <= hCurrent + 1
                            && openQueue.size() <= 500000
                    )
                        openQueue.add(children);
                }
                return null;
            });

//            astarChildrenAddThread.createChildren(currentNode, closeQueue, openQueue, hCurrent);

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
                    openQueue.add(children);
            }
        }
//        System.out.println("A Star not found");
//        System.exit(1);
//        return 0;
    }




}
