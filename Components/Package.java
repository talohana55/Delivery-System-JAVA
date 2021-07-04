//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
import java.util.ArrayList;
/**
 * Package  -Representation of a package in a system
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 */
public abstract class Package  {		
	public static  int id = 999;
	private int packageID;
	private Priority priority;	
	private Status status;
	private Address senderAddress;
	private Address destinationAddress;
	private ArrayList <Tracking> tracking;
	public boolean Deliverd = false;// variable for customer-let him know if is package delivered to destination

	//=====================CONSTUCTURS====================//
	/**
	 * A Constructor who receives as arguments, 
	 * priority of package,addresses the sender and receives a package	  
	 * @param priority
	 * @param senderAddress
	 * @param destinationAdress	 * 
	 * @see Priority,Address
	 */
	public Package(Priority priority, Address senderAddress, Address destinationAdress) {
		this.priority = priority;
		this.senderAddress = senderAddress;
		this.destinationAddress = destinationAdress;
		this.tracking = new ArrayList <Tracking>();//initialize empty array
		synchronized(this.getClass()) {
			id++;
		}
		this.packageID = id;
		this.status = Status.CREATION; // understand from running example
		
	}	
	/**
	 * A copy constructor that gets an already existing Package object and copies every parameter(the Adress parameters are copied by the help of the copy constructor of the Address Class).
	 * @param other
	 */
	public Package(Package other) {
		this.packageID = other.getPackageID();
		this.priority = other.getPriority();
		this.senderAddress = new Address(other.getSenderAddress());
		this.destinationAddress = new Address(other.getDestinationAddress());
		this.Deliverd = other.isDeliverd();
		this.status = other.getStatus();
		this.tracking = new ArrayList <Tracking>();//initialize empty array
		for(int i=0 ; i<other.getTracking().size();i++) {
			Tracking t = other.getTracking().get(i);
			tracking.add(new Tracking(t));
		}
	}
	//=====================GETTERS&SETTERS====================//
	public ArrayList<Tracking> getTracking() {return tracking;}
	public int getPackageID() {return packageID;}
	public Priority getPriority() {return priority;}
	public Status getStatus() {return status;}
	public Address getSenderAddress() {return this.senderAddress;}
	public Address getDestinationAddress() {return this.destinationAddress;}
	public String getClassName() {return "Package";}
	public void setPriority(Priority p) {priority=p;}
	public void setStatus(Status s) {status=s;}
	public void setPackageID(int packageID) {
		this.packageID = packageID;
	}
	//=====================METHODS====================//
	public boolean isDeliverd() {
		return Deliverd;
	}
	public void setDeliverd(boolean deliverd) {
		Deliverd = deliverd;
	}

	/**
	 * Rewrite of a method in order to print the variables of an Package in a way that suits us
	 */
	@Override
	public String toString() {
		return "[packageID=" + packageID + ", priority=" + priority + ", status=" + status + ", senderAddress="
				+ senderAddress + ", destinationAddress=" + destinationAddress +" ,last Tracking= " +this.getFinalTracking()+" ]";
	}
	public Tracking getFinalTracking() {
		int size= tracking.size();
		return tracking.get(size-1);
	}
	/**
	 * Method for adding tracking to a package and current status
	 * @param node
	 * @param status	 * 
	 * @see  Node,Status(Enum)
	 */
	public void addTracking (Node node, Status status) {
		Tracking temp = new Tracking(MainOffice.getClock(),node,status);
		boolean flag = false ;
		for(int i=0 ; i<tracking.size() ;i++) {
			if(tracking.get(i).equals(temp)) {
				flag=true;
				continue;
			}
		}
		if(flag==false) {
			tracking.add(temp);
		}
	}
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	@Override
	public boolean equals(Object other) {	
		if( other instanceof Package) {
			if(this.packageID == ((Package)other).packageID &&
				this.priority == ((Package)other).priority &&
				this.senderAddress == ((Package)other).senderAddress &&
				this.destinationAddress == ((Package)other).destinationAddress &&
				this.status == ((Package)other).status && this.tracking.equals(((Package)other).tracking)) {
			return true;
			}
		}		
		return false;
	}
}
