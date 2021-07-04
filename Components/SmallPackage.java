//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
/**
 * SmallPackage 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Package
 */

public class SmallPackage extends Package {
	private boolean acknowledge;
	
	//=====================CONSTUCTURS====================//
	/**
	 * A Constructor who receives as arguments, 
	 * priority of package,addresses the sender and receives a package,
	 * and acknowledge as a shipping confirmation
	 * @param priority
	 * @param senderAddress
	 * @param destinationAdress
	 * @param acknowledge
	 * @see Priority,Address
	 */
	public SmallPackage(Priority priority, Address senderAddress, Address destinationAdress, boolean acknowledge) {
		super(priority,senderAddress,destinationAdress);		
		this.acknowledge = acknowledge;
	}
	/**
	 * A copy constructor that gets an already existing SmallPackage Object,and copies it (also by the help of the copy constructor of class Package).
	 * @param other
	 */
	public SmallPackage(SmallPackage other) {
		super(other);
		this.acknowledge = other.getAcknowledge();

	}
	//=====================GETTERS&SETTERS====================//

	public boolean getAcknowledge() {
		return acknowledge;
	}
	public boolean isAcknowledge() {
		return acknowledge;
	}
	//=====================METHODS====================//
	/**
	 * Rewrite of a method in order to print the variables of an Package in a way that suits us
	 */
	@Override
	public String toString() {
		return "SmallPackage [packageID="+ getPackageID()+ ", priority="+getPriority()+ 
				", status="+ getStatus()
				+ ", senderAddress="+getSenderAddress().getZip()+"-"+getSenderAddress().getStreet() +
				", destinationAddress="+getDestinationAddress().getZip()+"-"+getDestinationAddress().getStreet()
				+" ,acknowledge="+acknowledge +"]";
	}
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */

	@Override
	public boolean equals(Object other) {	
		if(other instanceof SmallPackage) {
			if(this.acknowledge == ((SmallPackage)other).acknowledge && super.equals(other)) {
				return true;
			}
		}		
		return false;
	}
	
}
	
