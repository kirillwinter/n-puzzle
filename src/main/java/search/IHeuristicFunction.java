package search;


import java.util.HashMap;

public class IHeuristicFunction {

    private HashMap<Integer, Coordinate> coordinates;

    public IHeuristicFunction(HashMap<Integer, Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public int calculateHeuristic(Node node){


        int h = 0;
        for (int i = 0; i < node.getState().length; i++) {
            for (int j = 0; j < node.getState()[i].length; j++) {

                h += getManhattanDistance(node.getState()[i][j], i, j);
                h += getLinearConflict(node, node.getState()[i][j], i, j);
                
            }
        }
        return h;
    }

    private int getManhattanDistance(int value, int i, int j){

        if (value == 0)
            return 0;
        Coordinate coordinate = coordinates.get(value);
        return (Math.abs(coordinate.getxPos() - j) + Math.abs(coordinate.getyPos() - i));
    }

    private int getLinearConflict(Node node, int value, int iCopy, int jCopy) {

        int h = 0;
        int i = iCopy;
        int j = jCopy;

        int startValueInLine = iCopy * node.getState().length + 1;
        int endValueInLine = iCopy * node.getState().length + node.getState().length;

        if (value <= endValueInLine && value >= startValueInLine){
            for (; j < node.getState().length; j++) {

                if (node.getState()[i][j] < value && node.getState()[i][j] <= endValueInLine && node.getState()[i][j] >= startValueInLine){
                    h+=2;
                }
            }
        }


        // TODO column
//        i = iCopy;
//        j = jCopy;
//
//        for (; i <blocks.length; i++ ){
//
//            for (int ii = i; ii < blocks.length; ii++){
//
//            }
//        }




        return h;

    }


    private int getVerticalLinearConflict(Node node, int value, int i, int j){




        return 0;
    }


}
