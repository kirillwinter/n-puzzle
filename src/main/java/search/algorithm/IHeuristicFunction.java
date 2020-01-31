package search.algorithm;


import search.Coordinate;
import search.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

public class IHeuristicFunction {

    private HashMap<Integer, Coordinate> coordinatesGoalNode;
    private Node goalNode;
    List<Node> lastMoveList;

    public IHeuristicFunction(Node goalNode, HashMap<Integer, Coordinate> coordinatesGoalNode) {
        this.goalNode = goalNode;
        this.coordinatesGoalNode = coordinatesGoalNode;
        getLastMoveState();
    }

    public int calculateHeuristic(Node node){

        int h = 0;
        for (int i = 0; i < node.getState().length; i++) {
            for (int j = 0; j < node.getState()[i].length; j++) {

                if (node.getState()[i][j] == 0) continue;

                h += getManhattanDistance(node.getState()[i][j], i, j);
//                h += verticalLinearConflict(node, i, j);
//                h += horizontalLinearConflict(node, i, j);
                
            }
        }
//        if (!lastMoveList.contains(node)){
//            h +=2;
//        } else {
//            System.out.println("find last");
//        }



        return h;
    }

    private int getManhattanDistance(int value, int i, int j){

        if (value == 0)
            return 0;
        Coordinate coordinate = coordinatesGoalNode.get(value);
        int dist = Math.abs(coordinate.getxPos() - j) + Math.abs(coordinate.getyPos() - i);
        return (dist);
    }

    private int verticalLinearConflict(Node node, int y, int x) {
        int h = 0;
        for(int y2 = y + 1; y2 < node.getState().length; y2++)
        {
            if (checkSequence(node, y, x, y2, x))
                h += 2;
        }
        return h;
    }

    private int horizontalLinearConflict(Node node, int y, int x){
        int h = 0;
        for(int x2 = x + 1; x2 < node.getState()[y].length; x2++)
        {
            if (checkSequence(node, y, x, y, x2))
                h += 2;
        }
        return h;
    }

    private boolean checkSequence(Node node, int y, int x, int y2, int x2) {
        Coordinate goalPos = coordinatesGoalNode.get(node.getState()[y][x]);
        Coordinate goalPos2 = coordinatesGoalNode.get(node.getState()[y2][x2]);


        int currDeltaY = y2 - y;
        int currDeltaX = x2 - x;

        if (currDeltaY == 0 && (y != goalPos.getyPos() || y2 != goalPos2.getyPos())) return false;
        if (currDeltaX == 0 && (x != goalPos.getxPos() || y2 != goalPos2.getyPos())) return false;

        int goalDeltaY = goalPos2.getyPos() - goalPos.getyPos();
        int goalDeltaX = goalPos2.getxPos() - goalPos.getxPos();
        return (goalDeltaY == currDeltaY || goalDeltaX == currDeltaX) && (currDeltaY * goalDeltaY < 0 || currDeltaX * goalDeltaX < 0);
    }


    private void getLastMoveState(){
        lastMoveList = new ArrayList<>();



        Node node;

        node = getSuccessor(getNewState(),  goalNode.getZeroX(), goalNode.getZeroY() + 1);
        if (node != null)
            lastMoveList.add(node);

        node = getSuccessor(getNewState(),   goalNode.getZeroX(), goalNode.getZeroY() - 1);
        if (node != null)
            lastMoveList.add(node);

        node = getSuccessor(getNewState(), goalNode.getZeroX() - 1, goalNode.getZeroY());
        if (node != null)
            lastMoveList.add(node);

        node = getSuccessor(getNewState(),  goalNode.getZeroX() + 1, goalNode.getZeroY());
        if (node != null)
            lastMoveList.add(node);
    }

    private Node getSuccessor(int[][] newState,  int newZeroX, int newZeroY) {  //  в этом методе меняем ноль и соседнее число

        int[][] state = goalNode.getState();
        if (newZeroX > -1 && newZeroX < state.length && newZeroY > -1 && newZeroY < state.length) {
            int t = newState[newZeroY][newZeroX];
            newState[newZeroY][newZeroX] = newState[goalNode.getZeroY()][goalNode.getZeroX()];
            newState[goalNode.getZeroY()][goalNode.getZeroX()] = t;
            Node node = new Node(goalNode,  newState);
//            node.setParent(this);
            return node;
        } else
            return null;

    }

    private int[][] getNewState() { //  опять же, для неизменяемости

        int[][] state = goalNode.getState();
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


}
