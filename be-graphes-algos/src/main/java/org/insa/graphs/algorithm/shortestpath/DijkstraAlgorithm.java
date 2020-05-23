package org.insa.graphs.algorithm.shortestpath;

import java.util.Collections;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.Label;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.algorithm.AbstractSolution;
import java.util.ArrayList;
import java.util.List;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	public DijkstraAlgorithm(ShortestPathData data) {
		super(data);
	}

	@Override
	protected ShortestPathSolution doRun() {

		final ShortestPathData data = getInputData();
		ShortestPathSolution chemin_final = null;

		// Création de notre tas, et de nos labels associés à la carte
		BinaryHeap<Label> tas = new BinaryHeap<Label>();
		ArrayList<Label> labels = new ArrayList<Label>();
		for (Node node : data.getGraph().getNodes()) {
			Label label = newLabel(node, data);
			labels.add(label);
		}
		// On met le cout de notre premier sommet à 0
		if (data.getOrigin() != null) {
			Label originLabel = labels.get(data.getOrigin().getId());
			originLabel.setCost(0);

			// Mettre en bleu ce sommet qui a été observé
			notifyOriginProcessed(data.getOrigin());

			// On insert le premier sommet dans notre tas
			tas.insert(originLabel);
			Label destinationLabel = labels.get(data.getDestination().getId());
			// On prépare notre tableau d'accesseur
			while (!tas.isEmpty()) {

				Label stepOrigin = tas.deleteMin();
				stepOrigin.setHasBeenVisited(true);
				// On regarde parmis les successeurs de notre sommet
				List<Arc> successors = stepOrigin.getNode().getSuccessors();
				for (Arc successorArc : successors) {
					// On vérifie si la route est accessible par notre moyen de transport
					if (!data.isAllowed(successorArc)) {
						continue;
					}

					Label successorLabel = labels.get(successorArc.getDestination().getId());
					boolean inTas = successorLabel.getCost() != Double.MAX_VALUE;
					

					// On ne regarde le successeur que si il n'a pas déjà été marqué
					if (!successorLabel.hasBeenVisited()) {
						notifyNodeReached(successorArc.getDestination());
						// Mise à jour du cout du sucesseur
						double cost = successorArc.getLength() + stepOrigin.getCost();
						if (successorLabel.getCost() > cost) {
							successorLabel.setCost(cost);
							successorLabel.setPreviousArc(successorArc);
							successorLabel.setPreviousLabel(stepOrigin);

							// On met à jour le tas, si son cout est différent de Double.MAX_VALUE c'est qu'il a déjà été mis à jour.
							//On va donc l'enlever du tas pour l'y remettre
							if (inTas) {
								tas.remove(successorLabel);
							}
							tas.insert(successorLabel);
							
						}
					}
					/*Mise à jour du tas*/
					tas = UpdateTas(tas);
					
				}
			}

			// Si la destination n'est pas atteinte, le chemin n'est pas faisable
			if (!destinationLabel.hasPreviousArc()) {
				chemin_final = new ShortestPathSolution(data, AbstractSolution.Status.INFEASIBLE);
			} else {

				ArrayList<Arc> arcs = new ArrayList<Arc>();
				Label label = destinationLabel;

				// Sinon on met en bleu le sommet final
				notifyDestinationReached(data.getDestination());

				while (label.getPreviousArc() != null) {
					// System.out.println("Label observé : "+label.getNode().getId());
					arcs.add(label.getPreviousArc());

					// System.out.println("Son père : "+label.getPreviousLabel().getNode().getId());
					label = label.getPreviousLabel();
				}

				// On inverse le path
				Collections.reverse(arcs);
				Path path = new Path(data.getGraph(), arcs);
				chemin_final = new ShortestPathSolution(data, AbstractSolution.Status.OPTIMAL, path);
			}

			return chemin_final;
		} 
		else 
		{
			return chemin_final = new ShortestPathSolution(data,AbstractSolution.Status.INFEASIBLE,null );
		}
	}

	protected Label newLabel(Node node, ShortestPathData data) {
		return new Label(node);

	}
	
	private BinaryHeap<Label> UpdateTas (BinaryHeap<Label> tas )
	{
		BinaryHeap<Label> newTas = new BinaryHeap<Label>();
		ArrayList<Label> label = new ArrayList<Label>();
		int taille = tas.size();  
        Label tmp = new Label();  
		/*On récupère tous les labels présents dans le tas*/
		for(int j=0;j<taille;j++)
		{
			label.add(tas.deleteMin());
			System.out.println(label.get(j).getCost());
		}
		/*On réalise un tri à bulle*/	
        for(int i=0; i < taille; i++) 
        {
                for(int k=1; k< (taille-i); k++)
                {  
                        if(label.get(k-1).compareTo(label.get(k)) < 0)
                        {
                                /*Echange des elements*/
                                tmp = label.get(k-1);  
                                label.set(k-1,label.get(k));  
                                label.set(k,tmp); 
                        }
 
                }
        }
        /*préparation du nouveau tas*/
        for(int n=0; n<taille; n++)
        {
        	System.out.println(label.get(n).getCost());
        	newTas.insert(label.get(n));
        }
		
		return newTas;
	}
	

}
