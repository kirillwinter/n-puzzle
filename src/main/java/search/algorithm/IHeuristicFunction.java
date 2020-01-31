package search.algorithm;


import search.Coordinate;
import search.Node;

import java.util.HashMap;

public class IHeuristicFunction {

    private HashMap<Integer, Coordinate> coordinatesGoalNode;
    private Node goalNode;

    public IHeuristicFunction(Node goalNode, HashMap<Integer, Coordinate> coordinatesGoalNode) {
        this.goalNode = goalNode;
        this.coordinatesGoalNode = coordinatesGoalNode;
    }

    public int calculateHeuristic(Node node){


        int h = 0;
        for (int i = 0; i < node.getState().length; i++) {
            for (int j = 0; j < node.getState()[i].length; j++) {

                if (node.getState()[i][j] == 0) continue;

                h += getManhattanDistance(node.getState()[i][j], i, j);
                h += verticalLinearConflict(node, goalNode, i, j);
                h += horizontalLinearConflict(node, goalNode, i, j);
                
            }
        }
        return h;
    }

    private int getManhattanDistance(int value, int i, int j){

        if (value == 0)
            return 0;
        Coordinate coordinate = coordinatesGoalNode.get(value);
        int dist = Math.abs(coordinate.getxPos() - j) + Math.abs(coordinate.getyPos() - i);
        return (dist);
    }

    private int verticalLinearConflict(Node node, Node goalNode, int y, int x) {
        int h = 0;
        for(int y2 = y + 1; y2 < node.getState().length; y2++)
        {
            if (checkSequence(node, goalNode, y, x, y2, x))
                h += 2;
        }
        return h;
    }

    private int horizontalLinearConflict(Node node, Node goalNode, int y, int x){
        int h = 0;
        for(int x2 = x + 1; x2 < node.getState()[y].length; x2++)
        {
            if (checkSequence(node, goalNode, y, x, y, x2))
                h += 2;
        }
        return h;
    }

    private boolean checkSequence(Node node, Node goalNode, int y, int x, int y2, int x2) {
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

}
