package fr.eurecom.mobserv.arianna;

import java.util.Iterator;
import java.util.List;

import android.content.Context;

import fr.eurecom.mobserv.arianna.model.Event;
import fr.eurecom.mobserv.arianna.model.MapLevel;
import fr.eurecom.mobserv.arianna.model.Model;
import fr.eurecom.mobserv.arianna.model.NavigationLink;
import fr.eurecom.mobserv.arianna.model.NavigationNode;
import fr.eurecom.mobserv.arianna.model.Path;
import fr.eurecom.mobserv.arianna.model.PointOfInterest;

public class ApplicationState {

	private static ApplicationState instance = null;

	private PointOfInterest currentPointOfInterest = null;
	private PointOfInterest destinationPointOfInterest = null;
	
	/**
	 * ATTENZIONE! VARIABILE UTILIZZATA SOLO PER LE FRECCE SULLA MAPPA
	 */
	private PointOfInterest temporaryPOIDuringStep = null;
	private Path currentPath = null;
	private MapLevel currentLevel = null;
	private Event currentEvent = null;
	private NavigationNode currentNode = null;
	private List<NavigationLink> linksToDestination = null;
	private Context context = null;

	private ApplicationState() {

		// TODO inizializzazioni di test, manca il contesto
		// 
		// setCurrentPath(currentEvent.getPaths().values().iterator().next());
		// Iterator<PointOfInterest> it =
		// currentEvent.getPois().values().iterator();
		// setCurrentNode(it.next().getNavNode());
		// it.next();
		// setDestinationPointOfInterest(it.next());
	}
	
	public void setContext(Context applicationContext){
		context = applicationContext;
		currentEvent = (Event) Event.getByURI(Event.class, "E0", context);
		currentLevel = currentEvent.getLevels().values().iterator().next();
	}

	/**
	 * @return the currentNode
	 */
	public NavigationNode getCurrentNode() {
		return currentNode;
	}

	/**
	 * @param currentNode
	 *            the currentNode to set
	 */
	public void setCurrentNode(NavigationNode currentNode) {
		this.currentNode = currentNode;
	}

	public static ApplicationState getInstance() {
		if (instance == null) {
			instance = new ApplicationState();
		}
		return instance;
	}

	/**
	 * @return the currentEvent
	 */
	public Event getCurrentEvent() {
		return currentEvent;
	}

	/**
	 * @param currentEvent
	 *            the currentEvent to set
	 */
	public void setCurrentEvent(Event currentEvent) {
		this.currentEvent = currentEvent;
	}

	/**
	 * @return the currentLevel
	 */
	public MapLevel getCurrentLevel() {
		return currentLevel;
	}

	/**
	 * @param currentLevel
	 *            the currentLevel to set
	 */
	public void setCurrentLevel(MapLevel currentLevel) {
		this.currentLevel = currentLevel;
	}

	/**
	 * @return the current PointOfInterest
	 */
	public PointOfInterest getCurrentPointOfInterest() {
		return currentPointOfInterest;
	}

	/**
	 * @param currentPointOfInterest
	 *            the current PointOfInterest to set
	 */
	public void setCurrentPointOfInterest(PointOfInterest currentPointOfInterest) {
		if (linksToDestination != null){
			boolean trovato = false;
			NavigationNode currentPOINode = currentPointOfInterest.getNavNode();
			for( NavigationLink nl : linksToDestination){
				if ( nl.getFromNode().equals(currentPOINode) || nl.getToNode().equals(currentPOINode) ){
					trovato = true;
					break;
				}
			}
			if (!trovato){
				linksToDestination = null;
			}
		}
		
		// TODO probabilmente non è una cacata
		setCurrentNode(currentPointOfInterest.getNavNode());
		this.currentPointOfInterest = currentPointOfInterest;
	}

	/**
	 * @return the current Path
	 */
	public Path getCurrentPath() {
		return currentPath;
	}

	/**
	 * @param currentPath
	 *            the current Path to set
	 */
	public void setCurrentPath(Path currentPath) {
		this.currentPath = currentPath;
		this.linksToDestination = null;
		if (this.currentPath != null)
			setCurrentPointOfInterest(currentPath.getPOIs().get(0));
	}

	public void setCurrentDestination(NavigationNode destination) {
		this.setCurrentPath(null);
		NavigationNode nodefrom = null;
		
		
		if (currentPointOfInterest != null) {
			setCurrentPointOfInterest(currentPointOfInterest);
			nodefrom = currentPointOfInterest.getNavNode();
		} 
//		else if (currentNode != null) {
//			//TODO Dobbiamo considerare solo i POI se no ci imputtaniamo
//			nodefrom = currentNode;
//		} else {
//
//			return;
//		}
		linksToDestination = Model.getShortestPathLink(nodefrom, destination);
	}

	/**
	 * @return the linksToDestination
	 */
	public List<NavigationLink> getLinksToDestination() {
		return linksToDestination;
	}

	/**
	 * @return the destinationPointOfInterest
	 */
	public PointOfInterest getDestinationPointOfInterest() {
		return destinationPointOfInterest;
	}

	/**
	 * @param destinationPointOfInterest
	 *            the destinationPointOfInterest to set
	 * @throws Exception
	 */
	public void setDestinationPointOfInterest(
			PointOfInterest destinationPointOfInterest) {
		

		this.destinationPointOfInterest = destinationPointOfInterest;
		this.setCurrentDestination(this.destinationPointOfInterest.getNavNode());
	}

	/**
	 * NON USARE SE NON SAI COSA STAI FACENDO!!!!
	 * 
	 * 
	 * @return the temporaryPOIDuringStep
	 */
	public PointOfInterest getTemporaryPOIDuringStep() {
		return temporaryPOIDuringStep;
	}

	/**
	 * NON USARE SE NON SAI COSA STAI FACENDO!!!!
	 * 
	 * 
	 * @param temporaryPOIDuringStep the temporaryPOIDuringStep to set
	 */
	public void setTemporaryPOIDuringStep(PointOfInterest temporaryPOIDuringStep) {
		this.temporaryPOIDuringStep = temporaryPOIDuringStep;
	}

	
	
	// /**
	// * @param linksToDestination the linksToDestination to set
	// */
	// public void setLinksToDestination(List<NavigationLink>
	// linksToDestination) {
	// this.linksToDestination = linksToDestination;
	// }

}
