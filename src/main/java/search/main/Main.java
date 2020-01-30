package search.main;


import search.Coordinate;
import search.GoalNodeCreator;
import search.algorithm.IHeuristicFunction;
import search.MapValidator;
import search.Node;
import search.algorithm.Astar;
import search.algorithm.Ida;

import java.util.HashMap;
import java.util.List;



// TODO стоит ли выбрасывать исключения?
// TODO жадный поиск
// TODO uniform-cost search
public class Main {

    public static void main(String[] args) {

        AlgorithmEnum alhoritm;
//        alhoritm = eAlhoritm.IDA;
        alhoritm = AlgorithmEnum.ASTAR;

        for (int i = 0; i < args.length ; i++) {
            System.out.println(i + " " + args[i]);
        }

        if (args[0] != null){
            long start = System.currentTimeMillis();
            String fileName = args[0];

            MapValidator mapValidator = new MapValidator();
            mapValidator.read(fileName);

            System.out.println("start");

            Node goalNode = new Node();




            HashMap<Integer, Coordinate> coordinatesGoalNode = new HashMap<>();
            goalNode.setState(GoalNodeCreator.createFirstZeroGoalNode(mapValidator.getSize(), coordinatesGoalNode));
            goalNode.print();
            mapValidator.checkResolve(goalNode.getState());
            IHeuristicFunction heuristicFunction = new IHeuristicFunction(coordinatesGoalNode);
            goalNode.print();

            Node initialState = new Node(null, goalNode, mapValidator.getState(), heuristicFunction);

            List<Node> path;

            switch (alhoritm)
            {
                case IDA:
                    Ida ida = new Ida(heuristicFunction, goalNode);
                    int res = ida.main(initialState);
                    System.out.println("res = " + res);
                    path = ida.getPath();
                    break;
                case ASTAR:
                    Astar astar = new Astar(heuristicFunction, goalNode);
                    int resAstar = astar.main(initialState, 500, true);
                    System.out.println("Astar, res=" + resAstar);
                    path = astar.getPath();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + alhoritm);
            }


           System.out.println("time complexity = " + (System.currentTimeMillis() - start)/1000 + "sec");
           int count = 0;
           for (Node node : path ) {
               System.out.println("h=" + node.getH());
               node.print();
           }
            System.out.println("steps=" + path.size());
        }
    }
}
