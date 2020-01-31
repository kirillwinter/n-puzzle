package search.algorithm.astar_thread;

import lombok.extern.slf4j.Slf4j;
import search.Node;
import search.algorithm.IHeuristicFunction;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AstarNewThread implements Runnable{

    // TODO HashSet concurrent
    private IHeuristicFunction heuristicFunction;
    private Node goalNode;
    private  Node  resultNode;
    private Node currentNode;
    private HashSet<Node> closeQueue;
    private PriorityBlockingQueue<Node> openQueue;
    private AtomicInteger minH;
    PriorityQueue<Node> result;


    public AstarNewThread(IHeuristicFunction heuristicFunction, Node goalNode, PriorityBlockingQueue<Node> openQueue,  HashSet<Node> closeQueue, AtomicInteger minH, PriorityQueue<Node> result ) {
        this.heuristicFunction = heuristicFunction;
        this.goalNode = goalNode;
        this.closeQueue = closeQueue;
        this.openQueue = openQueue;
        this.minH = minH;
        this.result = result;
    }

//
//    public void run() {
//
//
//
//    }

//    @Override
//    public Node call() throws Exception {
//        System.out.println("THREAD START");
//
//        while ((currentNode = openQueue.poll()) != null) {
//            if (minH.get() > currentNode.getH()) {
//                minH.set(currentNode.getH());
//                System.out.println("Thread: " + Thread.currentThread().getName() +  ", minH: " + minH + " openQueue=" + openQueue.size());
//
//            }
//
//            closeQueue.add(currentNode);
//            if (currentNode.equals(goalNode)) {
//                resultNode = currentNode;
//                System.out.println("FIND!!!!");
//                return resultNode; // TODO exit
//            }
//            int hCurrent = currentNode.getH();
//            PriorityQueue<Node> childrens = currentNode.getSuccessors(goalNode);
//            Node children;
//            while ((children = childrens.poll()) != null) {
//                int hChildren = children.getH();
//                if (!closeQueue.contains(children)
//                        && !openQueue.contains(children)
//                        && hChildren <= hCurrent + 1
//                        && openQueue.size() <= 500000 //maxQueue
//                )
//                    openQueue.add(children);
//            }
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }

    @Override
    public void run() {
        System.out.println("Thread: " + Thread.currentThread().getName() +" START");

        while (true) {
            currentNode = openQueue.poll();
            if (currentNode == null){
                continue; // чтобы треды не закрывались сразу же
            }

            if (minH.get() > currentNode.getH()) {
                minH.set(currentNode.getH());
                System.out.println("Thread: " + Thread.currentThread().getName() +  ", minH: " + minH + " openQueue=" + openQueue.size());

            }

            closeQueue.add(currentNode);
            if (currentNode.equals(goalNode)) {
                resultNode = currentNode;
                System.out.println("FIND!!!!");
                result.add(resultNode);
                return ; // TODO exit
            }
            int hCurrent = currentNode.getH();
            PriorityQueue<Node> childrens = currentNode.getSuccessors();
            Node children;
            while ((children = childrens.poll()) != null) {
                int hChildren = children.getH();
                if (!closeQueue.contains(children)
                        && !openQueue.contains(children)
                        && hChildren <= hCurrent + 1
                        && openQueue.size() <= 500000 //maxQueue
                )
                    openQueue.add(children);
            }
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
    }
}
