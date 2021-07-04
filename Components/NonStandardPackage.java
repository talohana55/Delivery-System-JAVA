//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
/**
 * NonStandardPackage 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Package
 */
public class NonStandardPackage extends Package  {
	private int width;
	private int	length;
	private int height;
	//=====================CONSTUCTURS====================//
	/**
	 * A Constructor who receives as arguments, 
	 * priority of package,addresses the sender and receives a package,
	 * and dimensions of the truck for adjusting non-standard shipping.
	 * @param priority
	 * @param senderAddress
	 * @param destinationAdress
	 * @param width,length,height
	 * @see Priority,Address
	 */
	public NonStandardPackage(Priority priority, Address senderAddress, Address destinationAdress,int width,int length,int height) {
		super(priority, senderAddress, destinationAdress);
		this.width = width;
		this.length =length;
		this.height = height;
		
	}
	/**
	 * Initialized a new NonStandardPackage by the parameter it gets 
	 * of an already existing NonStandard Package(also by the help of the copy constructor of Package).
	 * @param other
	 */
	public NonStandardPackage(NonStandardPackage other) {
		super(other);
		this.width = other.getWidth();
		this.length =other.getLength();
		this.height = other.getHeight();
	}
	//=====================GETTERS&SETTERS====================//
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getLength() {
		return length;
	}
	public int getHeight() {
		return height;
	}
	//=====================METHODS====================//
	/**
	 * Rewrite of a method in order to print the variables of an Package in a way that suits us
	 */
	@Override
	public String toString() {
		return "NonStandardPackage [packageID="+ getPackageID()+ ", priority="+getPriority()+ 
				", status="+ getStatus()
				+ ", senderAddress="+getSenderAddress().getZip()+"-"+getSenderAddress().getStreet() +
				", destinationAddress="+getDestinationAddress().getZip()+"-"+getDestinationAddress().getStreet()
				+",width="+width+", length="+length+ ", height="+height+"]";
	}	
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */

	@Override
	public boolean equals(Object other) {	
		if(other instanceof NonStandardPackage) {
			if(this.width == ((NonStandardPackage)other).width && 
					this.length == ((NonStandardPackage)other).length &&
					this.height == ((NonStandardPackage)other).height &&
					super.equals(other)) {
				return true;
			}
		}		
		return false;
	}

}
