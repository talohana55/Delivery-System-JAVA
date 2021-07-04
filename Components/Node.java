//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
/**
 * Node 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Van, StandardTruck, NonStandardTruck,Branch,Hub 
 */
public interface Node {
	public  void collectPackage(Package p);	
	public void deliverPackage(Package p);		
	public void work();
	
		
}
