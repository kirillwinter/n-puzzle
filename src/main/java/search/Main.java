package search;


import java.util.HashMap;
import java.util.List;

// TODO стоит ли выбрасывать исключения?
// TODO жадный поиск
// TODO uniform-cost search
public class Main {

    public static void main(String[] args) {

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
            HashMap<Integer, Coordinate> coordinates = new HashMap<>();
            goalNode.createGoalNode(mapValidator.getSize(), coordinates);

            IHeuristicFunction heuristicFunction = new IHeuristicFunction(coordinates);
            goalNode.print();

            Node initialState = new Node(null, mapValidator.getState(), heuristicFunction);

            List<Node> path;

//            Ida ida = new Ida(heuristicFunction, goalNode);
//            int res = ida.main(initialState);
//            System.out.println("res = " + res);
//            path = ida.getPath();

            Astar astar = new Astar(heuristicFunction, goalNode);
            int resAstar = astar.main(initialState);
            System.out.println("Astar, res=" + resAstar);
            path = astar.getPath();

            System.out.println("time complexity = " + (System.currentTimeMillis() - start)/1000 + "sec");
            int count = 0;
            for (Node node : path ) {
                node.print();
            }
        }
    }
}
