package search.main;


import lombok.extern.slf4j.Slf4j;
import search.algorithm.AlgorithmEnum;
import search.algorithm.Astar;
import search.algorithm.Ida;
import search.heuristic.HeuristicEnum;
import search.heuristic.IHeuristicFunction;
import search.node.Coordinate;
import search.node.GoalNodeCreator;
import search.node.GoalStateEnum;
import search.node.Node;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class Main {

    private static AlgorithmEnum algorithm = AlgorithmEnum.ASTAR;
    private static GoalStateEnum goalStateArg = GoalStateEnum.SNAKE;
    private static HeuristicEnum heuristic = HeuristicEnum.MANHATTAN;
    private static int maxQueue = 100000;
    private static String fileName;


    public static void main(String[] args) {

        // TODO  теряются рещения при  ASTAR
        // TODO  везде отлов exception -> test
        // TODO  readme.md

        long start = System.currentTimeMillis();

        if (args[0] != null) {

            readArgs(args);

            MapValidator mapValidator = new MapValidator();
            mapValidator.read(fileName);

            Node goalNode;
            HashMap<Integer, Coordinate> coordinatesGoalNode = new HashMap<>();

            switch (goalStateArg) {
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
            IHeuristicFunction heuristicFunction = new IHeuristicFunction(goalNode, coordinatesGoalNode, heuristic);


            Node initialState = new Node(null, mapValidator.getState(), mapValidator.getZeroXInitState(),
                    mapValidator.getZeroYInitState(), heuristicFunction, algorithm);

            List<Node> path;

            log.info("start");
            if (algorithm == AlgorithmEnum.IDA) {
                Ida ida = new Ida(goalNode);
                int res = ida.main(initialState);
                log.info("Ida, res = " + res);
                path = ida.getPath();
            } else {
                Astar astar = new Astar(goalNode, maxQueue);
                int resAstar = astar.main(initialState);
                log.info("Astar, res=" + resAstar);
                path = astar.getPath();
            }

            log.info("time complexity = " + (System.currentTimeMillis() - start) / 1000 + "sec");
            for (Node node : path) {
                log.info("h=" + node.getH() + " g=" + node.getG());
                node.print();
            }
            log.info("steps=" + path.size());
            System.exit(0);
        } else {
            log.error("Where filename????");
            System.exit(1);
        }
    }

    private static void readArgs(String[] args) {

        fileName = args[0];
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];

            if (arg.compareToIgnoreCase("-astar") == 0) {
                algorithm = AlgorithmEnum.ASTAR;
            } else if (arg.compareToIgnoreCase("-ida") == 0) {
                algorithm = AlgorithmEnum.IDA;
            } else if (arg.compareToIgnoreCase("-greedy") == 0) {
                algorithm = AlgorithmEnum.GREEDY;
            } else if (arg.compareToIgnoreCase("-uniform_cost") == 0) {
                algorithm = AlgorithmEnum.UNIFORM_COST;
            } else if (arg.compareToIgnoreCase("-snake") == 0) {
                goalStateArg = GoalStateEnum.SNAKE;
            } else if (arg.compareToIgnoreCase("-first_zero") == 0) {
                goalStateArg = GoalStateEnum.FIRST_ZERO;
            } else if (arg.compareToIgnoreCase("-last_zero") == 0) {
                goalStateArg = GoalStateEnum.LAST_ZERO;
            } else if (arg.contains("-max_queue=")) {
                String[] maxQueueArgs = arg.split("=");
                if (maxQueueArgs.length == 2) {
                    try {
                        maxQueue = Integer.parseInt(maxQueueArgs[1]);
                    } catch (Exception e) {
                        log.error("Invalid arg: " + arg);
                        System.exit(1);
                    }
                } else {
                    log.error("Invalid arg: " + arg);
                    System.exit(1);
                }
            } else if (arg.contains("-heuristic=")) {
                String[] heuristicArg = arg.split("=");
                if (heuristicArg.length == 2) {
                    switch (heuristicArg[1]) {
                        case "s":
                            heuristic = HeuristicEnum.SIMPLE;
                            break;
                        case "m":
                            heuristic = HeuristicEnum.MANHATTAN;
                            break;
                        case "mlc":
                            heuristic = HeuristicEnum.MANHATTAN_AND_LINEAR_CONFLICT;
                            break;
                        case "mlm":
                            heuristic = HeuristicEnum.MANHATTAN_AND_LAST_MOVE;
                            break;
                        case "mlclm":
                            heuristic = HeuristicEnum.MANHATTAN_LC_LM;
                            break;
                        default:
                            log.error("Invalid arg: " + arg);
                            System.exit(1);
                    }
                } else {
                    log.error("Invalid arg: " + arg);
                    System.exit(1);
                }
            } else {
                log.error("Invalid arg: " + arg);
                System.exit(1);
            }
        }
        for (int i = 0; i < args.length; i++)
            log.debug(i + " " + args[i]);
    }

}
