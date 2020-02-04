package search.algorithm;

import search.node.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

abstract class AbstractAlgorithm {

     Node goalNode;
     Node endPathNode;
     HashSet<Node> closeSet = new HashSet<>();
     boolean debug;
     long countVisited = 0;
     long countNotPut = 0;

    public int main(Node root){
        return  0;
    }

    public List<Node> getPath() {
        ArrayList<Node> path = new ArrayList<>();

        while (endPathNode != null) {
            path.add(0, endPathNode);
            endPathNode = endPathNode.getParent();
        }
        return path;
    }

    void printResult(){
        System.out.println("countVisited = " + countVisited);
        System.out.println("closeSet = " + closeSet.size());
        System.out.println("countNotPut = " + countNotPut);
    }

    
}
