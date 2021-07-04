//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;

/**
 * StandardTruck
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Truck
 */
public class StandardTruck extends Truck implements Node{
	
	private int maxWeight;
	private Branch destination;
	private Branch source;

	//=====================CONSTUCTURS====================//
	/**
	 * A default constructor that produces a random object
	 *  according to the same rules as in the parent class
	 * @see Truck -constructor
	 */
	public StandardTruck () {
		super();
	}
	/**
	 * A copy Constructor receives a StandardTruck object 
	 * and performs a deep copy according to the parameters belonging to the object
	 * @param other - StandardTruck
	 */
	public StandardTruck(StandardTruck other) {
		super(other);
		this.maxWeight = other.getMaxWeight();
		this.destination = other.getDestination();
		this.source = other.getSource();		
	}

	//=====================GETTERS&SETTERS====================//
	public Branch getSource() {
		return source;
	}
	public void setSource(Branch source) {
		this.source = source;
	}
	public int getMaxWeight() {
		return maxWeight;
	}
	public Branch getDestination() {
		return destination;
	}
	public void setDestination(Branch destination) {
		this.destination = destination;
	}
	//=====================METHODS====================//
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	@Override
	public boolean equals(Object other) {		
		if(other instanceof StandardTruck &&
				this.maxWeight == ((StandardTruck)other).maxWeight &&
				this.destination.equals(((StandardTruck)other).destination) &&
				super.equals(other)){				
			return true;
		}
		return false;
	}
	/**
	 * Rewrite of a method in order to print the variables of an StandardTruck in a way that suits us
	 */
	@Override
	public String toString() {
		return " StandardTruck [truckID="+getTruckID()+", licensePlate="+getLicensePlate() +"]";
	}
	 /**
     * Function for calculating the arrival time for 
     * any destination
     * @param min
     * @param max
     * @return Update total travel time value
     */
	public int generateTimeLeft(int min , int max) {		
	    int range = max - min + 1;
	    setTotalTime((int)((((Math.random() * range) + min))*10));
	   return getTotalTime();
	}
	/**
	 * 	
	 * Method represents a work unit for a standard truck,
	 * A standard truck moves between a sorting center and some branch that asks for pick-up and return.
	 * Delivery of a package to the branch and delivery of a package to the sorting center.
	 *@see collectPackage,deliverPackage - StandardTruck class
	 */
	@Override
	public synchronized void work() {		
		if(getAvailable()==false) { // truck on the way
			setTimeLeft(getTimeLeft()-1);
			if(getTimeLeft()==0) {				
				if(destination.getBranchName() == "HUB" ) {//Arrive to HUB
					for(int i=0 ; i<getPackagesSize();i++) {// unloaded all packages at HUB
						deliverPackage(getPackage(i));
						
					}
					setAvailable(true);// parking state - HUB 
				}							
				else
				{	// Arrive to branch					
					for(int i=0 ; i<this.getPackagesSize();i++) {// unloaded all packages at branch
						deliverPackage(getPackage(i));
						i--;
					}													
					synchronized(destination) {
						destination.notifyAll();				
					}
					for(int i=0 ; i<destination.getListPackages().size();i++) {// loaded packages from branch
						Package p = destination.getListPackages().get(i);
						if(p.getStatus() == Status.BRANCH_STORAGE) {
							this.addPackageToTruck(p);						
							i=-1;
							collectPackage(p);														 
						}						
					}										
					setTimeLeft(generateTimeLeft(1,6));	// time from branch to HUB	
					destination.setGetDelivery(false);
					Branch temp = this.getDestination();
					this.setDestination(this.getSource());			
					this.setSource(temp);
					this.setAvailable(false);

				}
				
			}
		}
		
	}
	/**
	 * Method for collecting package only, shipping tracking and status update
	 * @param p -Package
	 */
	@Override
	public  void collectPackage(Package p) {	
		if(p.getStatus() == Status.BRANCH_STORAGE) {//package was in branch
			p.setStatus(Status.HUB_TRANSPORT);//now package goes to HUB
			p.addTracking(this, p.getStatus());
			this.addAndReportTracking(p);
			destination.removePackagesFromBranch(p);//remove package from branch
		}
	}
	/**
	 * Method for delivering a package, tracking shipping and updating status
	 * @param p -Package
	 */
	@Override
	public  void deliverPackage(Package p) {
		if(p.getStatus() == Status.HUB_TRANSPORT) {//if package on its way to HUB
			p.setStatus(Status.HUB_STORAGE); // set destination to HUB 
			p.addTracking(destination, p.getStatus()); 	
			this.addAndReportTracking(p);
				destination.addPackagesToBranch(p);// add packages to HUB	
			
			this.removePackagesFromTruck(p);//remove package from standardTruck
			this.setAvailable(true);
		}
		else if(p.getStatus() == Status.BRANCH_TRANSPORT){//if package on its way to branch
			p.setStatus(Status.DELIVERY);			
			p.addTracking(destination, p.getStatus());
			this.addAndReportTracking(p);
			this.removePackagesFromTruck(p);//remove package from standardTruck 
				destination.addPackagesToBranch(p);// add packages to branch			
			
			
		}		
		
	}

}	
