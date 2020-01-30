package search;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    public int compare(Node a, Node b) {
        if (a.getF() == b.getF()) {
            return b.getG() - a.getG();
        }
        else {
            return a.getF() - b.getF();
        }
    }
}
