package org.insa.graphs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.lang.*;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */
public class Path {

	/**
	 * Create a new path that goes through the given list of nodes (in order),
	 * choosing the fastest route if multiple are available.
	 * 
	 * @param graph Graph containing the nodes in the list.
	 * @param nodes List of nodes to build the path.
	 * 
	 * @return A path that goes through the given list of nodes.
	 * 
	 * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
	 *                                  consecutive nodes in the list are not
	 *                                  connected in the graph.
	 * 
	 * @deprecated Need to be implemented.
	 */
	public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes) throws IllegalArgumentException {
		List<Arc> arcs = new ArrayList<Arc>();
		if (nodes.size() == 0)
		{
			return new Path(graph);
		}
		else if  (nodes.size() == 1)
		{
			return new Path(graph, nodes.get(0));
		}
		else {
			for ( int i=0; i<nodes.size()-1; i++)
			{
				arcs.add(succ_temps(nodes.get(i), nodes.get(i+1)));
			}
			return new Path(graph, arcs);
		}
	}

	private static Arc succ_temps(Node noeud_depart, Node noeud_arrive) throws IllegalArgumentException {
		List<Arc> succ = new ArrayList<Arc>();
		int num_dest = noeud_arrive.getId();
		double temps_arete = 0;
		int nb_passage = 0;
		succ = noeud_depart.getSuccessors();
		
		List<Arc> arc = new ArrayList<Arc>();
		for (Arc A : succ) {
			if (A.getDestination().getId() == num_dest) {
				nb_passage++;
				if (nb_passage == 1) {
					arc.clear();
					temps_arete = A.getMinimumTravelTime();
				}
				if (temps_arete >= A.getMinimumTravelTime()) {
					temps_arete = A.getMinimumTravelTime();
					arc.clear();
					arc.add(A);
				}
			}
		}
		if (nb_passage == 0)
		{
			throw new IllegalArgumentException();
		}
		return arc.get(0);
	}

	/**
	 * Create a new path that goes through the given list of nodes (in order),
	 * choosing the shortest route if multiple are available.
	 * 
	 * @param graph Graph containing the nodes in the list.
	 * @param nodes List of nodes to build the path.
	 * 
	 * @return A path that goes through the given list of nodes.
	 * 
	 * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
	 *                                  consecutive nodes in the list are not
	 *                                  connected in the graph.
	 * 
	 */

	// Création d'une classe sommet utilisée dans le plus court chemin

	// Association d'une liste de sommet à une liste de noeud
	private static List<Sommet> association(List<Node> nodes) {
		List<Sommet> sommet = new ArrayList<Sommet>();

		for (Node noeud : nodes) {
			Sommet x = new Sommet(noeud, Integer.MAX_VALUE, null, false, null);
			sommet.add(x);
		}
		return sommet;
	}

	// Renvois si un noeud est présent dans une liste de sommet
	private static boolean present(Node noeud, List<Sommet> liste) {
		boolean trouve = false;
		for (Sommet S : liste) {
			if (S.actuel == noeud) {
				trouve = true;
			}
		}
		return trouve;
	}

	// Renvoie du sommet associé au noeud
	private static Sommet sommet_present(Node noeud, List<Sommet> liste) {
		Sommet So = new Sommet(null, 0, null, false, null);
		for (Sommet S : liste) {
			if (S.actuel == noeud) {
				So = S;
			}
		}
		return So;
	}

	public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes) throws IllegalArgumentException {

		List<Arc> arcs = new ArrayList<Arc>(); // Chemin renvoyé à la fin
		List<Arc> arcs_inverse = new ArrayList<Arc>(); // Chemin renvoyé à la fin
		List<Sommet> noeudNonMarque = new ArrayList<Sommet>(); // liste des noeuds non marqués
		List<Sommet> tas = new ArrayList<Sommet>(); // Tas
		List<Sommet> noeudMarque = new ArrayList<Sommet>(); // liste des noeuds marquées, utilisées pour trouver le
		List<Arc> successeur = new ArrayList<Arc>(); // prochain sommet à marqué
		Sommet y; // y sucesseur du noeud actuel marqué

		if (nodes.size() > 1) {
			for (int i = 0; i < nodes.size() - 1; i++) {
				// Initialisation
				noeudMarque.clear();
				noeudNonMarque.clear();
				arcs_inverse.clear();
				tas.clear();
				noeudNonMarque = association(graph.getNodes());
				noeudMarque.clear();
				Sommet source = new Sommet(nodes.get(i), Integer.MAX_VALUE, null, false, null);
				noeudNonMarque.remove(source);
				source.cost = 0;
				source.marque = true;
				noeudMarque.add(source);

				float distance_enregistre = 0;
				tas.add(source);

				// Algorithme;
				while (noeudNonMarque.size() != 0) {
					// on trouve le sucesseur le plus proche
					Sommet noeud_act = getLowestDistance(noeudMarque, noeudNonMarque);
					// On le marque
					noeudNonMarque.remove(sommet_present(noeud_act.actuel, noeudNonMarque));
					noeudMarque.add(noeud_act);
					// On met à jour la nouvelle distance
					// Calcul du cout du père :
					float cout = sommet_present(noeud_act.pred, noeudMarque).cost;
					distance_enregistre = noeud_act.cost + cout;
					successeur = noeud_act.actuel.getSuccessors();
					for (Arc succ : successeur) {
						if (present(succ.getDestination(), noeudNonMarque)) {
							// On récupère les informations associées au noeud
							y = sommet_present(succ.getDestination(), noeudNonMarque);
							if (y.cost > (distance_enregistre + succ.getLength())) {
								// On update dans la liste des sommets non marqués
								noeudNonMarque.remove(sommet_present(y.actuel, noeudNonMarque));
								// On met à jour le tas
								if (tas.contains(y)) {
									tas.remove(sommet_present(y.actuel, noeudNonMarque));
									y.cost = distance_enregistre + succ.getLength();
									y.pred = noeud_act.actuel;
									tas.add(y);
								}
								// ou on inclu y dans le tas
								else {
									y.cost = distance_enregistre + succ.getLength();
									y.pred = noeud_act.actuel;
									tas.add(y);
								}
								// On rajoute dans la liste des sommets non marqués y avec son nouveau cout
								noeudNonMarque.add(y);
							}

						}
					}

				}

				Node parent = null;
				// On récupère le dernier arc de notre chemin

				for (int f = 0; f < noeudMarque.size(); f++) {
					if (noeudMarque.get(noeudMarque.size() - (f + 1)).actuel.getId() == nodes.get(i + 1).getId()) {
						arcs_inverse.add(noeudMarque.get(noeudMarque.size() - (f + 1)).arc_pre);
						parent = noeudMarque.get(noeudMarque.size() - (f + 1)).pred;
						System.out.println("le parent vaut maintenant : " + parent.getId());
					} else if (parent != null
							&& noeudMarque.get(noeudMarque.size() - (f + 1)).actuel.getId() == parent.getId()) {
						System.out.println("On lui a trouvé un parent");
						if (noeudMarque.get(noeudMarque.size() - (f + 1)).actuel.getId() == nodes.get(i).getId()) {
							System.out.println("C'est enfin fini");
						} else {
							System.out.println("Arc " + noeudMarque.get(noeudMarque.size() - (f + 1)).arc_pre);
							arcs_inverse.add(noeudMarque.get(noeudMarque.size() - (f + 1)).arc_pre);
							System.out.println("parent " + noeudMarque.get(noeudMarque.size() - (f + 1)).pred);
							parent = noeudMarque.get(noeudMarque.size() - (f + 1)).pred;
							System.out.println("le parent vaut maintenant : " + parent.getId());
						}
					} else {
						System.out.println("Parent id non égal");
					}

				}

				System.out.println("Sortie du premier for");

				// on inverse la liste
				for (int e = 0; e < arcs_inverse.size(); e++) {
					System.out.println("Dans la boucle");
					arcs.add(arcs_inverse.get(arcs_inverse.size() - (e + 1)));
					System.out.println(" Arc : " + arcs.get(e));
				}
				System.out.println("Arrivée en fin de parcours");
			}

			return new Path(graph, arcs);
		} else if (nodes.size() == 1) {
			return new Path(graph, nodes.get(0));
		} else {
			return new Path(graph);
		}

	}

	// Retourne le successeur ayant le plus faible cout en distance

	private static Sommet getLowestDistance(List<Sommet> noeudMarque, List<Sommet> noeudNonMarque) {
		float lowestDistance = Integer.MAX_VALUE;
		List<Arc> successeur = new ArrayList<Arc>();
		Sommet Succ_sommet;
		Sommet Succ_plus_pres = new Sommet(null, 0, null, false, null);
		float cout;
		// On va regarder pour nos sommets marqués tous les successeurs
		for (Sommet Som : noeudMarque) {
			successeur.addAll(Som.actuel.getSuccessors());
		}

		// Et en trouver celui qui est le plus proche
		for (Arc S : successeur) {
			Succ_sommet = new Sommet(S.getDestination(), 0, null, false, null);
			if (present(S.getDestination(), noeudNonMarque)) {
				cout = sommet_present(S.getOrigin(), noeudMarque).cost;
				if (lowestDistance >= (S.getLength() + cout)) {
					lowestDistance = S.getLength();
					Succ_plus_pres = new Sommet(S.getDestination(), lowestDistance, S.getOrigin(), true, S);
				}
			}
		}

		return Succ_plus_pres;

	}

	/**
	 * Concatenate the given paths.
	 * 
	 * @param paths Array of paths to concatenate.
	 * 
	 * @return Concatenated path.
	 * 
	 * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
	 *                                  map do not match, or the end of a path is
	 *                                  not the beginning of the next).
	 */
	public static Path concatenate(Path... paths) throws IllegalArgumentException {
		if (paths.length == 0) {
			throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
		}
		final String mapId = paths[0].getGraph().getMapId();
		for (int i = 1; i < paths.length; ++i) {
			if (!paths[i].getGraph().getMapId().equals(mapId)) {
				throw new IllegalArgumentException("Cannot concatenate paths from different graphs.");
			}
		}
		ArrayList<Arc> arcs = new ArrayList<>();
		for (Path path : paths) {
			arcs.addAll(path.getArcs());
		}
		Path path = new Path(paths[0].getGraph(), arcs);
		if (!path.isValid()) {
			throw new IllegalArgumentException("Cannot concatenate paths that do not form a single path.");
		}
		return path;
	}

	// Graph containing this path.
	private final Graph graph;

	// Origin of the path
	private final Node origin;

	// List of arcs in this path.
	private final List<Arc> arcs;

	/**
	 * Create an empty path corresponding to the given graph.
	 * 
	 * @param graph Graph containing the path.
	 */
	public Path(Graph graph) {
		this.graph = graph;
		this.origin = null;
		this.arcs = new ArrayList<>();
	}

	/**
	 * Create a new path containing a single node.
	 * 
	 * @param graph Graph containing the path.
	 * @param node  Single node of the path.
	 */
	public Path(Graph graph, Node node) {
		this.graph = graph;
		this.origin = node;
		this.arcs = new ArrayList<>();
	}

	/**
	 * Create a new path with the given list of arcs.
	 * 
	 * @param graph Graph containing the path.
	 * @param arcs  Arcs to construct the path.
	 */
	public Path(Graph graph, List<Arc> arcs) {
		this.graph = graph;
		this.arcs = arcs;
		this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
	}

	/**
	 * @return Graph containing the path.
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * @return First node of the path.
	 */
	public Node getOrigin() {
		return origin;
	}

	/**
	 * @return Last node of the path.
	 */
	public Node getDestination() {
		return arcs.get(arcs.size() - 1).getDestination();
	}

	/**
	 * @return List of arcs in the path.
	 */
	public List<Arc> getArcs() {
		return Collections.unmodifiableList(arcs);
	}

	/**
	 * Check if this path is empty (it does not contain any node).
	 * 
	 * @return true if this path is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return this.origin == null;
	}

	/**
	 * Get the number of <b>nodes</b> in this path.
	 * 
	 * @return Number of nodes in this path.
	 */
	public int size() {
		return isEmpty() ? 0 : 1 + this.arcs.size();
	}

	/**
	 * Check if this path is valid.
	 * 
	 * A path is valid if any of the following is true:
	 * <ul>
	 * <li>it is empty;</li>
	 * <li>it contains a single node (without arcs);</li>
	 * <li>the first arc has for origin the origin of the path and, for two
	 * consecutive arcs, the destination of the first one is the origin of the
	 * second one.</li>
	 * </ul>
	 * 
	 * @return true if the path is valid, false otherwise.
	 */
	public boolean isValid() {
		boolean valide = true;
		if (this.origin != null && this.arcs.size() != 0) {
			if (arcs.get(0).getOrigin() != origin) {
				valide = false;
			}
			for (int i = 0; i < arcs.size() - 1; i++) {
				if (arcs.get(i).getDestination() != arcs.get(i + 1).getOrigin()) {
					valide = false;
				}
			}
		} else if (this.origin != null) {
			if (arcs == null) {
				valide = false;
			}

		} else if (this.isEmpty() != true) {
			valide = false;

		}

		return valide;
	}

	/**
	 * Compute the length of this path (in meters).
	 * 
	 * @return Total length of the path (in meters).
	 * 
	 * 
	 */
	public float getLength() {
		float longueur = 0;
		for (Arc line : arcs) {
			longueur += line.getLength();
		}
		return longueur;
	}

	/**
	 * Compute the time required to travel this path if moving at the given speed.
	 * 
	 * @param speed Speed to compute the travel time.
	 * 
	 * @return Time (in seconds) required to travel this path at the given speed (in
	 *         kilometers-per-hour).
	 * 
	 * 
	 */
	public double getTravelTime(double speed) {
		float temps = 0;
		for (Arc line : arcs) {
			temps += line.getLength() * 3600 / (speed * Math.pow(10, 3));
		}
		return temps;
	}

	/**
	 * Compute the time to travel this path if moving at the maximum allowed speed
	 * on every arc.
	 * 
	 * @return Minimum travel time to travel this path (in seconds).
	 * 
	 * 
	 */
	public double getMinimumTravelTime() {
		float temps = 0;
		for (Arc line : arcs) {
			temps += line.getMinimumTravelTime();
		}
		return temps;
	}

}
