package org.insa.graphs.algorithm.shortestpath;


import org.insa.graphs.model.Node;



public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    @Override
    protected LabelStar newLabel(Node node,  ShortestPathData data) {
		return new LabelStar(node,data);

	}

}
