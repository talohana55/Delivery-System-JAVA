//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
/**
 * Address - interface include address to communication between object in system
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 
 */
public class Address {
	public int zip;
	public int street;
	//=====================CONSTUCTURS====================//
	/**
	 * Initializes a new address type object that contains a ZIP address and a street address
	 * @param zip
	 * @param street
	 */
	public Address( int zip , int street) {
		this.zip = zip;
		this.street = street;
	}
	/**
	 * Initializes a new address type object that contains a ZIP address and a street address by coping from an already existing Address Object
	 * @param other
	 */
	public Address(Address other) {
		this.zip=other.getZip();
		this.street=other.getStreet();
	}
	//=====================GETTERS&SETTERS====================//	
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public int getStreet() {
		return street;
	}
	public void setStreet(int street) {
		this.street = street;
	}
	//=====================METHODS====================//
	
	@Override
	/**
	 * Rewrite of a method in order to print the variables of an address in a way that suits us
	 */
	public String toString() {
		return  zip + "-" + street ;
	}
	@Override
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class
	 * @param other(Object)
	 */
	public boolean equals(Object other) {
		if( other instanceof Address && 
				this.zip== ((Address)other).zip&&
				this.street== ((Address)other).street) {
			return true;
		}
		return false;
	}
}
