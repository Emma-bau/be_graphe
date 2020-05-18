package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Label;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;
import org.insa.graphs.algorithm.AbstractInputData;

public class LabelStar extends Label implements Comparable<Label> {

	private double estimated_cost;

	public LabelStar(Node node, ShortestPathData data) {
		super(node);
		Point x = node.getPoint();
		Point y = data.getDestination().getPoint();
		int speed = Math.max(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed());
		if(data.getMode()== AbstractInputData.Mode.LENGTH)
		{
			this.estimated_cost = (double)(x.distanceTo(y));
		}
		else
		{
			this.estimated_cost = (double) (x.distanceTo(y) / speed * 1000.d / 3600.d);
		}
			
		
	}

	@Override
	public double getTotalCost() {
		return this.getCost() + this.estimated_cost;
	}

	// getter and setter
	public double getEstimated() {
		return this.estimated_cost;
	}

	public void setEstimated(double cost) {
		this.estimated_cost = cost;
	}

	@Override
	public int compareTo(Label o) {
		return Double.compare(this.getTotalCost(), o.getTotalCost());
	}
	/*regarder celui qui est le plus près en cas d'égalité*/

}
