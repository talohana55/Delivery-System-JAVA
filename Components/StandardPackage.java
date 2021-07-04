//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
/**
 * StandardPackage 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Package
 */
public class StandardPackage extends Package{
	private double weight;
	
	//=====================CONSTUCTURS====================//
	/**
	 * A Constructor who receives as arguments, 
	 * priority of package,addresses the sender and receives a package,
	 * and weight limit to carry
	 * @param priority
	 * @param senderAddress
	 * @param destinationAdress
	 * @param weight
	 * @see Priority,Address
	 */
	public StandardPackage (Priority priority, Address senderAddress, Address destinationAdress,double weight) {
		super(priority,senderAddress,destinationAdress);
		this.weight = weight;
		
	}
	/**
	 * A copy constructor that gets an already existing StandardPackage Object,and copies it (also by the help of the copy constructor of class Package).
	 * @param other
	 */
	public StandardPackage(StandardPackage other) {
		super(other);
		this.weight = other.getWeight();

	}
	//=====================GETTERS&SETTERS====================//
	public double getWeight() {
		return weight;
	}
	//=====================METHODS====================//
	/**
	 * Rewrite of a method in order to print the variables of an Package in a way that suits us
	 */
	@Override
	public String toString() {
		return "StandardPackage [packageID="+ getPackageID()+ ", priority="+getPriority()+ 
				", status="+ getStatus()
				+ ", senderAddress="+getSenderAddress().getZip()+"-"+getSenderAddress().getStreet() +
				", destinationAddress="+getDestinationAddress().getZip()+"-"+getDestinationAddress().getStreet()
				+",weight="+weight+"]";
	}
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */

	@Override
	public boolean equals(Object other) {	
		if(other instanceof StandardPackage) {
			if(this.weight == ((StandardPackage)other).weight && super.equals(other)) {
				return true;
			}
		}		
		return false;
	}

}
