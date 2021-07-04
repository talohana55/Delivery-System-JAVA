//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
/**
 * NonStandardTruck
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Truck
 */
public class NonStandardTruck extends Truck implements Node{
	private int	width;
	private int	length;
	private int height;
	private Vertice destination;
	private Vertice source;

	//=====================CONSTUCTURS====================//
	/**
	 * A default constructor that produces a random object
	 *  according to the same rules as in the parent class
	 * @see Truck -constructor
	 */
	public NonStandardTruck () {
		super();
	}
	/**
	 * A copy constructor that produces a random object
	 *  according to the same rules as in the parent class 
	 *  and to given parameters.
	 *  
	 *  @param licensePlate
	 *  @param truckModel
	 *  @param length,width,height - sizes of truck
	 * @see Truck -constructor
	 */
	public NonStandardTruck(String licensePlate, String truckModel, int length, int width, int height) {
		super(licensePlate,truckModel);
		this.width = width;
		this.length =length;
		this.height =height;
	}
	/**
	 * Initialize a new NonStandardTruck by other already existing
	 *  NonStandardTruck object that the method gets,by coping it(also by the help of
	 *   the copy constructor of Truck).
	 * @param other
	 */
	public NonStandardTruck(NonStandardTruck other) {
		super(other);
		this.width = other.getWidth();
		this.length = other.getLength();
		this.height = other.getHeight();	
		this.destination = other.getDestination();
		this.source=other.getSource();
	}
	//=====================GETTERS&SETTERS====================//

	public int getWidth() {
		return width;
	}
	public int getLength() {
		return length;
	}
	public int getHeight() {
		return height;
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
     * Function for calculating the arrival time from HUB 
     * to the starting point of a package.
     * @param min
     * @param max
     * @return Update total travel time value
     */
    public int generateTimeLeftToDestination(int min , int max) {
    	int result = (int)(Math.abs(max-min)% 10)+1;
   	    setTotalTime(result);
   	   return getTotalTime();
	}
    /**
     * Function for calculating the arrival time from 
     * starting point of a package  
     * to the receiver point of a package.
     * @param min
     * @param max
     * @return Update total travel time value
     */
	public int generateTimeLeftToSender(int min , int max) {		
	    int range = max - min + 1;
	    setTotalTime((int)((((Math.random() * range) + min))*10));
	   return getTotalTime();
	}
	/**
	 * Rewrite of a method in order to print the variables of an NonStandardTruck in a way that suits us
	 */
	@Override
	public String toString() {
		return "NonStandardTruck [truckID="+getTruckID()+", licensePlate="+getLicensePlate()+"]";
	}
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	@Override
	public boolean equals(Object other) {		
		if(other instanceof NonStandardTruck &&
				this.width == ((NonStandardTruck)other).width &&
				this.length ==((NonStandardTruck)other).length  &&
				this.height ==((NonStandardTruck)other).height  &&
				super.equals(other)){				
			return true;
		}
		return false;
	}
	
	/**
	 * Method represents a work unit for a non-standard truck,
	 * A truck performs the operation only if the time it has left is equal to zero.
	 *	A non-standard truck can collect a package from a sending customer and deliver
	 * it directly to a receiving customer.
	 *@see collectPackage,deliverPackage - NonStandardTruck class
	 */
	@Override
	public void work() {	
		if(getAvailable()==false) {
			setTimeLeft(getTimeLeft()-1);
			if(getTimeLeft() == 0 && getPackagesSize()>0) {
				Package p = getPackage(0);
				if(p.getStatus()==Status.COLLECTION) {// from hub to sender- set time to receiver
					setTimeLeft(generateTimeLeftToDestination(p.getSenderAddress().getStreet(),p.getDestinationAddress().getStreet()));
					this.setDestination(VerticeType.DestinationPackage,  p.getPackageID());
					this.setSource(VerticeType.SourchPackage, p.getPackageID());
					collectPackage(getPackage(0));
				}
				else if(getPackage(0).getStatus()==Status.DISTRIBUTION) { //timeToArrive from Hub to Collection
					deliverPackage(getPackage(0));
				}
				
			}
		}
		
	}
	/**
	 * Method for collecting package only, shipping tracking and status update
	 * @param p -Package
	 */
	@Override
	public void collectPackage(Package p) {// collect package from branch and delivering direct to destination client
		p.setStatus(Status.DISTRIBUTION);
		p.addTracking(this,Status.DISTRIBUTION);
		this.addAndReportTracking(p);
	}
	/**
	 * Method for delivering a package, tracking shipping and updating status
	 * @param p -Package
	 */
	@Override
	public void deliverPackage(Package p) {	//delivering to destination client
		p.setStatus(Status.DELIVERED);
		p.addTracking(this,Status.DELIVERED);		
		this.addAndReportTracking(p);
		this.removePackagesFromTruck(p);// remove the package from the truck
		setAvailable(true);//track is available
		
	}

	

}
