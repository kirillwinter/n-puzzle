package search.algorithm;

import lombok.extern.slf4j.Slf4j;
import search.node.Node;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
abstract class AbstractAlgorithm {

	Node goalNode;
	Node endPathNode;
	HashSet<Node> closeSet = new HashSet<>();
	long countVisited = 0;
	long countNotPut = 0;

	public int main(Node root) {
		return 0;
	}

	public List<Node> getPath() {
		ArrayList<Node> path = new ArrayList<>();

		while (endPathNode != null) {
			path.add(0, endPathNode);
			endPathNode = endPathNode.getParent();
		}
		return path;
	}

	void printResult() {
		log.info("countVisited = " + countVisited);
		log.info("closeSet = " + closeSet.size());
		log.info("countNotPut = " + countNotPut);
	}


}
