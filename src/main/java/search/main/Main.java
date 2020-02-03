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

public class Main {

    public static void main(String[] args) {

        // TODO  теряются рещения при  ASTAR

        // установка дефолтных значений
        AlgorithmEnum algorithm;
        algorithm = AlgorithmEnum.ASTAR;
        GoalStateEnum goalStateArg;
        goalStateArg = GoalStateEnum.SNAKE;
        int maxQueue = 100000;
        boolean debug = false;

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
                }else if (arg.compareToIgnoreCase("-uniform_cost") == 0) {
                    algorithm = AlgorithmEnum.UNIFORM_COST;
                }else if (arg.compareToIgnoreCase("-snake") == 0){
                    goalStateArg = GoalStateEnum.SNAKE;
                } else if (arg.compareToIgnoreCase("-first_zero") == 0){
                    goalStateArg = GoalStateEnum.FIRST_ZERO;
                } else if (arg.compareToIgnoreCase("-last_zero") == 0){
                    goalStateArg = GoalStateEnum.LAST_ZERO;
                } else if (arg.contains("-max_queue=")){
                    String[] maxQueueArgs =  arg.split("=");
                    if (maxQueueArgs.length == 2){
                        try {
                            maxQueue = Integer.parseInt(maxQueueArgs[1]);
                        } catch (Exception e){
                            System.err.println("Invalid arg: " + arg);
                            System.exit(1);
                        }
                    } else {
                        System.err.println("Invalid arg: " + arg);
                        System.exit(1);
                    }
                }else if (arg.compareToIgnoreCase("-debug") == 0){
                    debug = true;
                }  else {
                    System.err.println("Invalid arg: " + arg);
                    System.exit(1);
                }
            }


            long start = System.currentTimeMillis();
            String fileName = args[0];

            MapValidator mapValidator = new MapValidator();
            mapValidator.read(fileName);
            System.out.println("start");

            Node goalNode;

            HashMap<Integer, Coordinate> coordinatesGoalNode = new HashMap<>();

            switch (goalStateArg){
                case FIRST_ZERO:
                    goalNode = GoalNodeCreator.createFirstZeroGoalNode(mapValidator.getSize(), coordinatesGoalNode);
                    break;
                case LAST_ZERO:
                    goalNode = GoalNodeCreator.createLastZeroGoalNode(mapValidator.getSize(), coordinatesGoalNode);
                    break;
                default:
                    goalNode = GoalNodeCreator.createSnakeGoalNode(mapValidator.getSize(), coordinatesGoalNode);
                    break;
            }

            mapValidator.checkResolve(goalNode.getState());
            IHeuristicFunction heuristicFunction = new IHeuristicFunction(goalNode, coordinatesGoalNode);
            goalNode.print();

            Node initialState = new Node(null, mapValidator.getState(), mapValidator.getZeroXInitState(),
                    mapValidator.getZeroYInitState(), heuristicFunction, algorithm);

            List<Node> path;

            if (algorithm == AlgorithmEnum.IDA) {
                Ida ida = new Ida(goalNode, debug);
                int res = ida.main(initialState);
                System.out.println("Ida, res = " + res);
                path = ida.getPath();
            } else {
                Astar astar = new Astar(goalNode, debug);
                int resAstar = astar.main(initialState, maxQueue);
                System.out.println("Astar, res=" + resAstar);
                path = astar.getPath();
            }

           System.out.println("time complexity = " + (System.currentTimeMillis() - start)/1000 + "sec");
           for (Node node : path ) {
               System.out.println("h=" + node.getH() +  " g=" + node.getG());
               node.print();
           }
           System.out.println("steps=" + path.size());
           System.exit(0);
        } else {
            System.err.println("Where filename????");
            System.exit(1);
        }
    }

}
