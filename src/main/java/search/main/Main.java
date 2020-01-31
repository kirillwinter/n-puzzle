package search.main;


import search.Coordinate;
import search.GoalNodeCreator;
import search.algorithm.IHeuristicFunction;
import search.MapValidator;
import search.Node;
import search.algorithm.Astar;
import search.algorithm.Ida;
import search.algorithm.astar_thread_for_children.AstarAlgThreadForChildren;

import java.util.HashMap;
import java.util.List;



// TODO стоит ли выбрасывать исключения?
// TODO жадный поиск
// TODO uniform-cost search
public class Main {

    public static void main(String[] args) {

        // TODO  теряются рещения при  ASTAR

        // установка дефолтных значений
        AlgorithmEnum algorithm;
        algorithm = AlgorithmEnum.IDA;
        GoalStateEnum goalStateArg;
        goalStateArg = GoalStateEnum.SNAKE;;

        for (int i = 0; i < args.length ; i++) {
            System.out.println(i + " " + args[i]);
        }

        if (args[0] != null){

            // чтение аргументов
            for (int i = 1; i < args.length; i++) {
                String arg = args[i];
                if (arg.compareToIgnoreCase("-astar") == 0){
                    algorithm = AlgorithmEnum.ASTAR;
                } else if (arg.compareToIgnoreCase("-ida") == 0){
                    algorithm = AlgorithmEnum.IDA;
                } else if (arg.compareToIgnoreCase("-greedy") == 0){
                    algorithm = AlgorithmEnum.GREEDY;
                } else if (arg.compareToIgnoreCase("-snake") == 0){
                    goalStateArg = GoalStateEnum.SNAKE;
                } else if (arg.compareToIgnoreCase("-first_zero") == 0){
                    goalStateArg = GoalStateEnum.FIRST_ZERO;
                } else if (arg.compareToIgnoreCase("-last_zero") == 0){
                    goalStateArg = GoalStateEnum.LAST_ZERO;
                } else {
                    System.err.println("Invalid arg: " + arg);
                    System.exit(1);
                }
            }




            long start = System.currentTimeMillis();
            String fileName = args[0];

            MapValidator mapValidator = new MapValidator();
            mapValidator.read(fileName);
            System.out.println("start");

            Node goalNode = new Node();

            HashMap<Integer, Coordinate> coordinatesGoalNode = new HashMap<>();

            switch (goalStateArg){
                case SNAKE:
                    goalNode.setState(GoalNodeCreator.createSnakeGoalNode(mapValidator.getSize(), coordinatesGoalNode));
                    break;
                case FIRST_ZERO:
                    goalNode.setState(GoalNodeCreator.createFirstZeroGoalNode(mapValidator.getSize(), coordinatesGoalNode));
                    break;
                case LAST_ZERO:
                    goalNode.setState(GoalNodeCreator.createLastZeroGoalNode(mapValidator.getSize(), coordinatesGoalNode));
                    break;
            }

//            goalNode.setState(GoalNodeCreator.createLastZeroGoalNode(mapValidator.getSize(), coordinatesGoalNode));
            goalNode.print();
            mapValidator.checkResolve(goalNode.getState());
            IHeuristicFunction heuristicFunction = new IHeuristicFunction(goalNode, coordinatesGoalNode);
//            goalNode.getSuccessors();
//
//            goalNode.setHeuristicFunction(heuristicFunction);
//            heuristicFunction.getLastMoveState();
            goalNode.print();

            Node initialState = new Node(null, mapValidator.getState(), mapValidator.getZeroXInitState(), mapValidator.getZeroYInitState(), heuristicFunction);

            List<Node> path;

            switch (algorithm)
            {
                case IDA:
                    Ida ida = new Ida(heuristicFunction, goalNode);
                    int res = ida.main(initialState);
                    System.out.println("Ida, res = " + res);
                    path = ida.getPath();
                    break;
                case ASTAR:

                    Astar astar = new Astar(heuristicFunction, goalNode, false);
                    int resAstar = astar.main(initialState, 500000, true);
                    System.out.println("Astar, res=" + resAstar);
                    path = astar.getPath();

//                    AstarNew astar = new AstarNew(heuristicFunction, goalNode);
//                    int resAstar = astar.main(initialState, 500000, true);
//                    System.out.println("Astar, res=" + resAstar);
//                    path = astar.getPath();

//                    AstarAlgThreadForChildren astar = new AstarAlgThreadForChildren(heuristicFunction, goalNode);
//                    int resAstar = astar.main(initialState, 500000, true);
//                    System.out.println("Astar, res=" + resAstar);
//                    path = astar.getPath();
                    break;
                case GREEDY:
                    Astar greedy = new Astar(heuristicFunction, goalNode, true);
                    int resGreedy= greedy.main(initialState, 500000, true);
                    System.out.println("Astar, res=" + resGreedy);
                    path = greedy.getPath();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + algorithm);
            }


           System.out.println("time complexity = " + (System.currentTimeMillis() - start)/1000 + "sec");
           int count = 0;
           for (Node node : path ) {
               System.out.println("h=" + node.getH() +  " g=" + node.getG());
               node.print();
           }
            System.out.println("steps=" + path.size());
           System.exit(0);
        }
    }
}
