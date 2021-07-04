//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;

 public class Tracking {	
	private int time;
	private Node node; 
	private Status status;
	//=====================CONSTUCTURS====================//
/**
 * Builder to create package tracking only.
 *	Gets Time, Node and Status.
 * @param time
 * @param node - Node -interface
 * @param status
 * 
 * @see Package
 */	public Tracking(int time, Node node, Status status) {
		this.time = time;
		this.node = node;
		this.status = status;
	}
 /**
  * A copy constructor that gets an already existing Tracking object and copies its parameters
  * @param other
  */
 	public Tracking(Tracking other) {
	 this.node = other.getNode();
	 this.time = other.getTime();
	 this.status = other.getStatus();
 	}
	//=====================GETTERS&SETTERS====================//
	public int getTime() {
		return time;
	}
	public Node getNode() {
		return node;
	}	
	public Status getStatus() {
		return status;
	}
	//=====================METHODS====================//
	/**
	 * Rewrite of a method in order to print the variables of an Tracking in a way that suits us
	 */
	@Override
	public String toString() {
		return " Time: "+time +", Status: " + status+", Location: "+node.toString() ;// TODO: print of node
		
	}
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	@Override
	public boolean equals(Object other) {
		if(other instanceof Tracking && 
				this.time== ((Tracking)other).time&&
				this.node== ((Tracking)other).node&&
				this.status== ((Tracking)other).status) {
			return true;
		}
		return false;
	}
	
}
