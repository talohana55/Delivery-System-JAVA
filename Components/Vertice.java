package Components;
//Assignment 2
//Members: Tal ohana - 307833186
//       Tali Tevlin - 206999195
/**
 * Vertice - A sub-weight that represents the location 
 * of a truck, its status of operation, and an identification mark
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	NonStandardTruck, Van
 */
public class Vertice {
	private int id;
	private VerticeType type;
	/**
	 * A constructor produces a vertice object that represents the branch ID/Hub to which truck is travel to/from ,
		The type of operation that a truck performs.
		This object refers only to a non-standard truck in order to identify the type of operation it is performing.
	 * @param type - VerticeType - Enum
	 * @param id
	 * @see VerticeType - Enum
	 * @see NonStatndardTruck
	 */
	public Vertice(VerticeType type , int id) {
		this.id=id;
		this.type = type;
	}
	public Vertice(Vertice other) {
		this.id = other.getID();
		this.type =other.getType();
	}
	public int getID() {
		return id;
	}
	public VerticeType getType() {
		return type;
	}
	public void setType(VerticeType newType) {
		type = newType;
	}
}
