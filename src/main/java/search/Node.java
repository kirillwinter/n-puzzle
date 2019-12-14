package search;

import java.util.*;

public class Node implements Comparator<Node> {    // Чтобы узнать длину пути, нам нужно помнить предидущие позиции (и не только поэтому)

    private Node parent;  // ссылка на предыдущий
    private int[][] state;  // сама позиция
    private int g = 0;
    private int h = 0;
    private int f = 0;
    private IHeuristicFunction heuristicFunction;


    public Node() {

    }

    public Node(Node parent, int[][] state,  IHeuristicFunction heuristicFunction) {
        this.parent = parent;
        this.state = state;
        this.heuristicFunction = heuristicFunction;
        h = this.heuristicFunction.calculateHeuristic(this);
//        h = calculateHeuristic();
        if (parent != null){
            g = parent.getG() + 1;
        }
    }


    Node getParent() {
        return parent;
    }



    void setG(int g) {
        this.g = g;
    }

    void setH(int h) {
        this.h = h;
    }

    void setF(int f) {
        this.f = f;
    }

    int getG() {
        return g;
    }

    int getH() {
        return h;
    }

    int getF() {
        f = h + g;
        return f;
    }

    PriorityQueue<Node> getSuccessors(){

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

        node = getSuccessor(getNewState(), zeroX, zeroY, zeroX, zeroY + 1);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), zeroX, zeroY, zeroX, zeroY - 1);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), zeroX, zeroY, zeroX - 1, zeroY);
        if (node != null)
            successors.add(node);

        node = getSuccessor(getNewState(), zeroX, zeroY, zeroX + 1, zeroY);
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

    private Node getSuccessor(int[][] newState, int x1, int y1, int x2, int y2) {  //  в этом методе меняем два соседних поля

        if (x2 > -1 && x2 < state.length && y2 > -1 && y2 < state.length) {
            int t = newState[x2][y2];
            newState[x2][y2] = newState[x1][y1];
            newState[x1][y1] = t;
            Node node = new Node(this, newState, heuristicFunction);
            node.setParent(this);
            return node;
        } else
            return null;

    }



    int distFromParent() {
        return 1;
    }



    void createGoalNode(int size, HashMap<Integer, Coordinate> coordinates){

        state = new int[size][];
        for (int i = 0; i < size; i++) {
            state[i] = new int[size];
        }

//        где side - текущая сторона (0 - вверх, 1 - право, 2 - ...)
//        sizeX - размер массива по горизонтали
//        CorrectX - переменная, которая отвечает за автоматическое декриментирование
//        Count - переменная, которая отвечает за текущую цифру внутри массива
//        Summ - произведение ширины на высоту, нужно для устранения ошибки (см. Далее)
//        Mas - название двумерного массива
//        index - собственно позиция внутри массива


        int sizeX = size;
        int sizeY = size;
        
        int summ = sizeX * sizeY;
        int correctY = 0;
        int correctX = 0;
        int value = 1;
        Coordinate coordinate;
        while( sizeY > 0 )
        {
            for ( int side = 0; side < 4; side++ )
            {
                for (int index = 0; index < (Math.max(sizeX, sizeY)); index++ )
                {

                    if (value == summ)
                        value = 0;

                    if ( side == 0 && index < sizeX - correctX && value <= summ){
                        coordinate = new  Coordinate();
                        state[side + correctY][index + correctX] = value;
                        coordinate.setyPos(side + correctY);
                        coordinate.setxPos(index + correctX);
                        coordinates.put(value, coordinate);
                        value++;

                    }

                    else if ( side == 1 && index < sizeY - correctY && index != 0 && value <= summ ){
                        coordinate = new  Coordinate();
                        state[index + correctY][sizeX - 1] = value;
                        coordinate.setyPos(index + correctY);
                        coordinate.setxPos(sizeX - 1);
                        coordinates.put(value, coordinate);
                        value++;
                    }

                    else if ( side == 2 && index < sizeX - correctX && index != 0 && value <= summ ){
                        coordinate = new  Coordinate();
                        state[sizeY - 1][sizeX - (index + 1)] = value;
                        coordinate.setyPos(sizeY - 1);
                        coordinate.setxPos(sizeX - (index + 1));
                        coordinates.put(value, coordinate);
                        value++;
                    }

                    else if ( side == 3 && index < sizeY - ( correctY + 1 ) && index != 0 && value <= summ ){
                        coordinate = new  Coordinate();
                        state[sizeY - (index + 1)][correctY] = value;
                        coordinate.setyPos(sizeY - (index + 1));
                        coordinate.setxPos(correctY);
                        coordinates.put(value, coordinate);
                        value++;
                    }
                }
            }
            sizeY--;
            sizeX--;
            correctY += 1;
            correctX += 1;
        }
    }

    void print(){

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
