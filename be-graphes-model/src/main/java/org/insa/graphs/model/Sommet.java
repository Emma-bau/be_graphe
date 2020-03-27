/**
 * 
 */

/**
 * @author Emma
 *
 */
package org.insa.graphs.model;
public class Sommet {
	public Node actuel;
	public float cost;
	public Node pred;
	public boolean marque;
	public Arc arc_pre; //arc associée de son père à lui

	public Sommet(Node act, float cout, Node pre, boolean mark, Arc arc) {
		this.actuel = act;
		this.cost = cout;
		this.pred = pre;
		this.marque = mark;
		this.arc_pre = arc;

	}
}
