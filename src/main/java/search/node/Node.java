package search.node;

import lombok.Getter;
import lombok.Setter;
import search.algorithm.AlgorithmEnum;
import search.heuristic.IHeuristicFunction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

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
    private AlgorithmEnum algorithm;
    private IHeuristicFunction heuristicFunction;


    public Node() {
    }

    public Node(Node parent, int[][] state, int zeroX, int zeroY, IHeuristicFunction heuristicFunction, AlgorithmEnum algorithm) {
        this.parent = parent;
        this.state = state;
        this.zeroX = zeroX;
        this.zeroY = zeroY;
        this.heuristicFunction = heuristicFunction;
        this.algorithm = algorithm;
        switch (algorithm) {
            case GREEDY:
                g = 0;
                h = this.heuristicFunction.calculateHeuristic(this);
                break;
            case UNIFORM_COST:
                h = 0;
                if (parent != null)
                    g = parent.getG() + 1;
                break;
            default:
                h = this.heuristicFunction.calculateHeuristic(this);
                if (parent != null)
                    g = parent.getG() + 1;
        }
        f = h + g;
    }

    public Node(Node parent, int[][] state, int zeroX, int zeroY) {
        this.parent = parent;
        this.state = state;
        this.zeroX = zeroX;
        this.zeroY = zeroY;
    }

    public PriorityQueue<Node> getSuccessors() {

        PriorityQueue<Node> successors = new PriorityQueue<>(4, new NodeComparator());
        Node node;

        node = getSuccessor(getNewState(), zeroX, zeroY + 1);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), zeroX, zeroY - 1);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), zeroX - 1, zeroY);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), zeroX + 1, zeroY);
        if (node != null)
            successors.add(node);

        return successors;
    }


    private Node getSuccessor(int[][] newState, int newZeroX, int newZeroY) {

        if (newZeroX > -1 && newZeroX < state.length && newZeroY > -1 && newZeroY < state.length) {
            int t = newState[newZeroY][newZeroX];
            newState[newZeroY][newZeroX] = newState[zeroY][zeroX];
            newState[zeroY][zeroX] = t;
            return new Node(this, newState, newZeroX, newZeroY, heuristicFunction, algorithm);
        } else
            return null;

    }

    private int[][] getNewState() {

        if (state == null) {
            return null;
        }

        final int[][] result = new int[state.length][];
        for (int i = 0; i < state.length; i++) {
            result[i] = new int[state[i].length];
            System.arraycopy(state[i], 0, result[i], 0, state[i].length);
        }
        return result;
    }

    public void print() {

        for (int[] ints : state) {
            for (int anInt : ints) {
                System.out.print(anInt + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }


    @Override
    public int compare(Node a, Node b) {
        if (a.getF() == b.getF()) {
            return b.getG() - a.getG();
        } else {
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
    // TODO может нужно оптимизировать hashCode
    public int hashCode() {
        return Arrays.deepHashCode(state);
    }

}
