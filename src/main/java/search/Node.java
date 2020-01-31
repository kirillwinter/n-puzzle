package search;

import lombok.Getter;
import lombok.Setter;
import search.algorithm.IHeuristicFunction;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
public class Node implements Comparator<Node>, Serializable {    // Чтобы узнать длину пути, нам нужно помнить предидущие позиции (и не только поэтому)

    private Node parent;  // ссылка на предыдущий
    private int[][] state;  // сама позиция
    private int g = 0;
    private int h = 0;
    private int f = 0;
    private int zeroX;
    private int zeroY;
    private IHeuristicFunction heuristicFunction;


    public Node() {
    }

    public Node(Node parent, int[][] state, int zeroX, int zeroY,  IHeuristicFunction heuristicFunction) {
        this.parent = parent;
        this.state = state;
        this.zeroX = zeroX;
        this.zeroY = zeroY;
        this.heuristicFunction = heuristicFunction;
        h = this.heuristicFunction.calculateHeuristic(this);
//        h = calculateHeuristic();
        if (parent != null){
            g = parent.getG() + 1;
        }
    }


    public int getF() {
        f = h + g;
        return f;
    }

    public PriorityQueue<Node> getSuccessors(){

        PriorityQueue<Node> successors = new PriorityQueue<>(4, new NodeComparator());
        Node node;

        node = getSuccessor(getNewState(),  zeroX, zeroY + 1);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(),   zeroX, zeroY - 1);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), zeroX - 1, zeroY);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(),  zeroX + 1, zeroY);
        if (node != null)
            successors.add(node);

        return successors;
    }



    private Node getSuccessor(int[][] newState,  int newZeroX, int newZeroY) {  //  в этом методе меняем ноль и соседнее число

        if (newZeroX > -1 && newZeroX < state.length && newZeroY > -1 && newZeroY < state.length) {
            int t = newState[newZeroY][newZeroX];
            newState[newZeroY][newZeroX] = newState[zeroY][zeroX];
            newState[zeroY][zeroX] = t;
            Node node = new Node(this,  newState, newZeroX, newZeroY, heuristicFunction);
//            node.setParent(this);
            return node;
        } else
            return null;

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
    public void print(){

        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                System.out.print(state[i][j] + "\t");
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
