package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;
import org.insa.graphs.model.Graph;

public class LabelStar extends Label implements Comparable<Label> {

	private double estimated_cost;

	public LabelStar(Node node, ShortestPathData data) {
		super(node);
		Point x = this.getNode().getPoint();
		Point y = data.getDestination().getPoint();
		this.estimated_cost = Point.distance(y, x);
	}

	public double getTotalCost() {
		return this.getCost() + estimated_cost;
	}

	// getter and setter
	public double getEstimated() {
		return estimated_cost;
	}

	public void setEstimated(double cost) {
		this.estimated_cost = cost;
	}

	@Override
	public int compareTo(Label o) {
		return Double.compare(this.getTotalCost(), o.getTotalCost());
	}

}
