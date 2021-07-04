//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;

import java.util.ArrayList;

/**
 * Van
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Truck
 */
public class Van extends Truck implements Node{
	private int branchID;	
	private Branch owner;
	private Vertice destination;
	private Vertice source;
	//=====================CONSTUCTURS====================//
	/**
	 * A default constructor that produces a random object
	 *  according to the same rules as in the parent class
	 *  
	 * @param owner - The branch to which Van is associated
	 * @see Truck -constructor
	 */
	public Van(Branch owner) {
		super();
		this.setLicensePlate(this.generatelicensPlate());
		this.setTruckModel(this.generateTruckModel());
		this.owner = owner;
		this.branchID = owner.getBranchId();
	}
	/**
	 * A copy Constructor receives a Van object 
	 * and performs a deep copy according to the parameters belonging to the object
	 * @param other -Van
	 */
	public Van(Van other) {
		super(other);	
		this.branchID = other.getBranchId();
		this.owner = other.getOwner();
		this.destination = other.getDestination();
		this.source=other.getSource();	
	}

	//=====================GETTERS&SETTERS====================//

	public int getBranchId() {
		return branchID;
	}
	public Branch getOwner() {
		return owner;
	}
	public Vertice getDestination() {
		return destination;
	}
	public void setDestination(VerticeType destination,int id) {
		this.destination = new Vertice(destination,id);
	}
	public Vertice getSource() {
		return source;
	}
	public void setSource(VerticeType source,int id ) {
		this.source = new Vertice(source,id);
	}
	//=====================METHODS====================//
	/**
	 * Rewrite of a method in order to print the variables of an Van in a way that suits us
	 */
	@Override
	public String toString() {
		return " Van [truckID="+getTruckID()+", licensePlate="+getLicensePlate() +", truckModel="+ getTruckModel()+" ,owner="+owner.getBranchId()+"]";
	}
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	@Override
	public boolean equals(Object other) {		
		if(other instanceof Van &&	super.equals(other)){	 
			return true;
		}
		return false;
	}
	 /**
     * Function for calculating the arrival time for 
     * any destination
     * @param num
     * @return Update total travel time value
     */
	  public int generateTimeLeft(int num) {
		int result = (int)(((num% 10)+1)*10);
		    this.setTotalTime(result);
		   return getTotalTime();
	}
	/**
	 * 	
	 * Method represents a work unit for a Van,
	 *A van Move between the branch and the sender of the package, pick up a 
	 *package from the sender and return to the branch from which it was sent.
	 * In addition, also delivering a package from a destination branch to the customer receives
	 */
	@Override
	public synchronized void work() {
		if(getAvailable()==false) {
			setTimeLeft(getTimeLeft()-1);
			if(getTimeLeft() == 0) {
				if(getPackagesSize() != 0 ) {
					if(getPackage(0).getStatus()==Status.COLLECTION) {
						this.setDestination(VerticeType.Branch, this.branchID);
						this.setSource(VerticeType.SourchPackage, getPackage(0).getPackageID());
						collectPackage(getPackage(0));						
					}
					else if(getPackage(0).getStatus()==Status.DISTRIBUTION){
						this.setDestination(VerticeType.Branch, this.branchID);
						this.setSource(VerticeType.DestinationPackage, getPackage(0).getPackageID());
						deliverPackage(getPackage(0));
					}
				}					
			}
		}
	}
	/**
	 * Method for collecting package only, shipping tracking and status update
	 * @param p -Package
	 */
	@Override
	public void collectPackage(Package p) {
		if(p.getStatus() == Status.BRANCH_STORAGE) {//collecting from customer already completed
			return;
		}		
		p.setStatus(Status.BRANCH_STORAGE);	// delivering to Sender branch in now completed	
		p.addTracking(this.owner,Status.BRANCH_STORAGE);		
		this.addAndReportTracking(p);
		this.removePackagesFromTruck(p);// deliver package from van to Branch - remove from van(only)
		setAvailable(true);
	}
	/**
	 * Method for delivering a package, tracking shipping and updating status
	 * @param p -Package
	 */
	@Override
	public void deliverPackage(Package p) {	
		if(p.getStatus() == Status.DELIVERED) {// delivering package to destination already completed
			return;
		}
		
		this.owner.removePackagesFromBranch(p);	
		p.setStatus(Status.DELIVERED);	// delivering to destination is complete	
		p.addTracking(this.owner,Status.DELIVERED);
		this.removePackagesFromTruck(p);//remove package from Van
		this.addAndReportTracking(p);
		this.setAvailable(true);// van is ready to the next mission
		
	}

}
