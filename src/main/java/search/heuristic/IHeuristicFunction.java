package search.heuristic;


import lombok.extern.slf4j.Slf4j;
import search.node.Coordinate;
import search.node.Node;

import java.util.HashMap;
import java.util.HashSet;

@Slf4j
public class IHeuristicFunction {

    private HashMap<Integer, Coordinate> coordinatesGoalNode;
    private Node goalNode;
    private HashSet<Node> lastMoveList;
    private HeuristicEnum heuristicEnum;

    public IHeuristicFunction(Node goalNode, HashMap<Integer, Coordinate> coordinatesGoalNode, HeuristicEnum heuristicEnum) {
        this.goalNode = goalNode;
        this.coordinatesGoalNode = coordinatesGoalNode;
        this.heuristicEnum = heuristicEnum;
        if (heuristicEnum == HeuristicEnum.MANHATTAN_AND_LAST_MOVE || heuristicEnum == HeuristicEnum.MANHATTAN_LC_LM)
            getLastMoveState();
    }

    public int calculateHeuristic(Node node) {

        switch (heuristicEnum) {
            case SIMPLE:
                return calculateSimple(node);
            case MANHATTAN:
                return calculateManhattan(node);
            case MANHATTAN_AND_LINEAR_CONFLICT:
                return calculateManhattanAndLinearConflict(node);
            case MANHATTAN_AND_LAST_MOVE:
                return calculateManhattanAndLastMove(node);
            case MANHATTAN_LC_LM:
                return calculateManhattanAndLinearConflictAndLastMove(node);
        }
        log.error("Invalid Heuristic");
        System.exit(1);
        return 0;
    }

    // TODO вынести выбор эвристики в отдельный класс
    private int calculateSimple(Node node) {
        int h = 0;
        for (int i = 0; i < node.getState().length; i++) {
            for (int j = 0; j < node.getState()[i].length; j++) {
                if (node.getState()[i][j] == 0) continue;
                if (node.getState()[i][j] != goalNode.getState()[i][j])
                    h++;
            }
        }
        return h;
    }

    private int calculateManhattan(Node node) {
        int h = 0;
        for (int i = 0; i < node.getState().length; i++) {
            for (int j = 0; j < node.getState()[i].length; j++) {
                if (node.getState()[i][j] == 0) continue;
                h += manhattanDistance(node.getState()[i][j], i, j);
            }
        }
        return h;
    }

    private int calculateManhattanAndLinearConflict(Node node) {

        int h = 0;
        for (int i = 0; i < node.getState().length; i++) {
            for (int j = 0; j < node.getState()[i].length; j++) {
                if (node.getState()[i][j] == 0) continue;
                h += manhattanDistance(node.getState()[i][j], i, j);
                h += verticalLinearConflict(node, i, j);
                h += horizontalLinearConflict(node, i, j);
            }
        }
        return h;
    }

    private int calculateManhattanAndLastMove(Node node) {

        int h = 0;
        for (int i = 0; i < node.getState().length; i++) {
            for (int j = 0; j < node.getState()[i].length; j++) {
                if (node.getState()[i][j] == 0) continue;
                h += manhattanDistance(node.getState()[i][j], i, j);

            }
        }
        h += lastMove(node);
        return h;
    }

    private int calculateManhattanAndLinearConflictAndLastMove(Node node) {

        int h = 0;
        for (int i = 0; i < node.getState().length; i++) {
            for (int j = 0; j < node.getState()[i].length; j++) {
                if (node.getState()[i][j] == 0) continue;
                h += manhattanDistance(node.getState()[i][j], i, j);
                h += verticalLinearConflict(node, i, j);
                h += horizontalLinearConflict(node, i, j);
            }
        }
        h += lastMove(node);
        return h;
    }


    private int lastMove(Node node) {
        if (!lastMoveList.contains(node))
            return 2;
        else
            log.debug("find last in lastMove");
        return 0;

    }

    private int manhattanDistance(int value, int i, int j) {

        if (value == 0)
            return 0;
        Coordinate coordinate = coordinatesGoalNode.get(value);
        return Math.abs(coordinate.getXPos() - j) + Math.abs(coordinate.getYPos() - i);
    }

    private int verticalLinearConflict(Node node, int y, int x) {
        int h = 0;
        for (int y2 = y + 1; y2 < node.getState().length; y2++) {
            if (checkSequence(node, y, x, y2, x))
                h += 2;
        }
        return h;
    }

    private int horizontalLinearConflict(Node node, int y, int x) {
        int h = 0;
        for (int x2 = x + 1; x2 < node.getState()[y].length; x2++) {
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

        if (currDeltaY == 0 && (y != goalPos.getYPos() || y2 != goalPos2.getYPos())) return false;
        if (currDeltaX == 0 && (x != goalPos.getXPos() || y2 != goalPos2.getYPos())) return false;

        int goalDeltaY = goalPos2.getYPos() - goalPos.getYPos();
        int goalDeltaX = goalPos2.getXPos() - goalPos.getXPos();
        return (goalDeltaY == currDeltaY || goalDeltaX == currDeltaX) && (currDeltaY * goalDeltaY < 0 || currDeltaX * goalDeltaX < 0);
    }


    // TODO убрать дублирование кода (этот же код в Node)
    private void getLastMoveState() {
        lastMoveList = new HashSet<>();

        Node node;

        node = getSuccessor(getNewState(), goalNode.getZeroX(), goalNode.getZeroY() + 1);
        if (node != null)
            lastMoveList.add(node);

        node = getSuccessor(getNewState(), goalNode.getZeroX(), goalNode.getZeroY() - 1);
        if (node != null)
            lastMoveList.add(node);

        node = getSuccessor(getNewState(), goalNode.getZeroX() - 1, goalNode.getZeroY());
        if (node != null)
            lastMoveList.add(node);

        node = getSuccessor(getNewState(), goalNode.getZeroX() + 1, goalNode.getZeroY());
        if (node != null)
            lastMoveList.add(node);
    }

    private Node getSuccessor(int[][] newState, int newZeroX, int newZeroY) {

        int[][] state = goalNode.getState();
        if (newZeroX > -1 && newZeroX < state.length && newZeroY > -1 && newZeroY < state.length) {
            int t = newState[newZeroY][newZeroX];
            newState[newZeroY][newZeroX] = newState[goalNode.getZeroY()][goalNode.getZeroX()];
            newState[goalNode.getZeroY()][goalNode.getZeroX()] = t;
            return new Node(goalNode, newState, newZeroX, newZeroY);
        } else
            return null;

    }


    private int[][] getNewState() {

        int[][] state = goalNode.getState();

        final int[][] result = new int[state.length][];
        for (int i = 0; i < state.length; i++) {
            result[i] = new int[state[i].length];
            System.arraycopy(state[i], 0, result[i], 0, state[i].length);
        }
        return result;
    }


}
