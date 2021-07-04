package Components;

import java.util.ArrayList;

import GUI.displayPanel;
/**
 * Memento - A class represents an object that stores the system state data 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 */
public class Memento {
	private Hub hub;
	private RunCustomers rc;
	private ArrayList <Package> packages;
	ReadWriteTracking readAndWrite; 

	/**
	 * Memento Constructor initial sorting center, customers and packages in system
	 * 
	 * @param hub
	 * @param rc
	 * @param p
	 */
	public Memento(Hub hub,RunCustomers rc,ArrayList<Package> p ) {
		this.hub = hub;	
		this.rc = rc;		
		this.packages = p;
	}
	public Hub getHub() {
		return hub;
	}
	public RunCustomers getRc() {
		return rc;
	}

	public ArrayList<Package> getPackages() {
		return packages;
	}


}
