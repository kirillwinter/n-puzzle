package search;

import lombok.Getter;
import lombok.Setter;
import search.algorithm.IHeuristicFunction;

import java.util.*;

@Getter
@Setter
public class Node implements Comparator<Node> {    // Чтобы узнать длину пути, нам нужно помнить предидущие позиции (и не только поэтому)

    private Node parent;  // ссылка на предыдущий
    private int[][] state;  // сама позиция
    private int g = 0;
    private int h = 0;
    private int f = 0;
    private IHeuristicFunction heuristicFunction;


    public Node() {

    }

    public Node(Node parent, Node  goalNode, int[][] state,  IHeuristicFunction heuristicFunction) {
        this.parent = parent;
        this.state = state;
        this.heuristicFunction = heuristicFunction;
        h = this.heuristicFunction.calculateHeuristic(this, goalNode);
//        h = calculateHeuristic();
        if (parent != null){
            g = parent.getG() + 1;
        }
    }


    public int getF() {
        f = h + g;
        return f;
    }

    public PriorityQueue<Node> getSuccessors(Node goalNode){

        int zeroX = Integer.MAX_VALUE;
        int zeroY = Integer.MAX_VALUE;

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                    break;
                }
            }
        }

        PriorityQueue<Node> successors = new PriorityQueue<>(4, new NodeComparator());
        Node node;

        node = getSuccessor(getNewState(), goalNode, zeroX, zeroY, zeroX, zeroY + 1);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), goalNode, zeroX, zeroY, zeroX, zeroY - 1);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), goalNode, zeroX, zeroY, zeroX - 1, zeroY);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), goalNode, zeroX, zeroY, zeroX + 1, zeroY);
        if (node != null)
            successors.add(node);

        return successors;
    }

    private int[][] getNewState() { //  опять же, для неизменяемости

        if (state == null) {
            return null;
        }

        final int[][] result = new int[state.length][];
        for (int i = 0; i < state.length; i++) {
            result[i] = new int[state[i].length];
            for (int j = 0; j < state[i].length; j++) {
                result[i][j] = state[i][j];
            }
        }
        return result;
    }

    private Node getSuccessor(int[][] newState, Node goalNode, int x1, int y1, int x2, int y2) {  //  в этом методе меняем два соседних поля

        if (x2 > -1 && x2 < state.length && y2 > -1 && y2 < state.length) {
            int t = newState[x2][y2];
            newState[x2][y2] = newState[x1][y1];
            newState[x1][y1] = t;
            Node node = new Node(this, goalNode, newState, heuristicFunction);
            node.setParent(this);
            return node;
        } else
            return null;

    }



    int distFromParent() {
        return 1;
    }


    public void print(){

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int[][] getState() {
        return state;
    }

    @Override
    public int compare(Node a, Node b) {
        if (a.getF() == b.getF()) {
            return b.getG() - a.getG();
        }
        else {
            return a.getF() - b.getF();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Arrays.deepEquals(state, node.state);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(state);
    }

}
