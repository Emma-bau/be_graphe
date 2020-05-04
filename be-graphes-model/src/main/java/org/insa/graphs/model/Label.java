package org.insa.graphs.model;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
	protected Node node;
	
	protected boolean hasBeenVisited;
	
	protected double cost;
	
	protected Arc previousArc;
	
	protected Label previousLabel;
	
	public Label()
	{
		this.node = null;
		this.hasBeenVisited = false;
		this.cost = Float.MAX_VALUE;
		this.previousArc = null;
		this.previousLabel = null;
	}
	
	public Label(Node node) {
		this.node = node;
		this.hasBeenVisited = false;
		this.cost = Float.MAX_VALUE;
		this.previousArc = null;
		this.previousLabel = null;
	}
	
	//Getter and Setter
	public void setPreviousArc(Arc arc) {
		this.previousArc = arc;
	}
	
	public void setPreviousLabel(Label label) {
		this.previousLabel = label;
	}
	
	public Label getPreviousLabel() {
		return this.previousLabel;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public void setHasBeenVisited(boolean b) {
		this.hasBeenVisited = b;
	}
	
	public boolean hasBeenVisited() {
		return this.hasBeenVisited;
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public Arc getPreviousArc() {
		return this.previousArc;
	}
	
	//Retourne vrai si le noeud a un parent
	public boolean hasPreviousArc() {
		return this.previousArc != null;
	}
	
	//Obtenir le cout total
	public double getTotalCost() {
		return cost;
	}

	@Override
	public int compareTo(Label o) {
		return Double.compare(this.getTotalCost(), o.getTotalCost());
	}
}
