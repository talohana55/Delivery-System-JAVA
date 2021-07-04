//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.EventListenerList;
/**
 * Truck   --Representation of a truck in a system
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Van, StandardTruck, NonStandardTruck
 */
public abstract class Truck  implements Node,Runnable{
	private static int  id = 2000;
	private int truckID;
	private String licensePlate;
	private String truckModel;
	private int timeLeft;
	private boolean available;
	private int totalTime;
	private Vector <Package> packages;
	private EventListenerList list;
	//=====================CONSTUCTURS====================//
	/**
	 *  A random default builder that produces an object with a 
	 *  vehicle	license plate and model at random.
	 *	A model consists of hundreds of M and a number between 0 and 4
	 */
	public Truck() {
		truckID = id;
		packages = new Vector<Package>();
		this.licensePlate = generatelicensPlate();
		this.truckModel = generateTruckModel();
		available = true;		
		id++;
		list = new EventListenerList();	
	}
	/**
	 * Copy constructor Initialize Truck by given licensePlate and truckModel
	 * @param licensePlate
	 * @param truckModel
	 */
	public Truck(String licensePlate, String truckModel) {
		truckID = id;
		packages = new Vector<Package>();
		this.licensePlate = licensePlate;
		this.truckModel = truckModel;
		available = true;
		id++;
		list = new EventListenerList();	
	}	
	
	/**
	 * Initializes a new object of a Truck and the fields in it,the parameter that
	 *  the constructor is getting is an already existing Truck Object. all the immutable
	 *  parameters are copied, list parameter are initialize.
	 * 	the packages list is getting an reference to the packages that are already existing 
	 *  in the originator packages that are similar(by id) to those in the other Object.
	 * @param other
	 */
	public Truck(Truck other) {
		truckID = other.getTruckID();
		this.packages = new Vector<Package>();
		this.licensePlate = other.getLicensePlate();
		this.truckModel = other.getTruckModel();
		this.timeLeft = other.getTimeLeft();
		this.totalTime = other.getTotalTime();
		this.available = other.getAvailable();
		list = new EventListenerList();		
		for ( TrackingListener  w : list.getListeners(TrackingListener.class) ) {
			list.add(TrackingListener.class, w);
		}
		for(int i=0 ; i<other.packages.size();i++) {
			for(Package p:Originator.packages) {
				if(p.getPackageID()==other.packages.get(i).getPackageID()) {
					packages.add(p);
				}
			}
		}
		
	}
	//=====================GETTERS&SETTERS====================//
	public  int getTruckID() {return truckID;}
	public String getLicensePlate() {return licensePlate;}
	public String getTruckModel() {return truckModel;}
	public int getTimeLeft() {return timeLeft;}
	public boolean getAvailable() {return this.available;}
	public int getPackagesSize() {return packages.size();}
	public Package getPackage(int i) {if(i<packages.size()) {return packages.get(i);} else {return null;}}
	public  void setTruckID(int truckID) {this.truckID = truckID;}
	public void setLicensePlate(String licensePlate) {this.licensePlate = licensePlate;}
	public void setTruckModel(String truckModel) {this.truckModel = truckModel;}
	public void setTimeLeft(int time) {this.timeLeft = time;}
	public void setAvailable(boolean a) { available = a;}
	public void setPackages(Vector<Package> packages) {this.packages = packages;}
	public void addPackageToTruck(Package p) { if(p!=null) packages.add(p);}
	public int getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}
	public EventListenerList getList() {
		return list;
	}
	public void setList(EventListenerList list) {
		this.list = list;
	}
	/**
	 * Calculating a total time for a truck ride allows us
	 *  to draw the object on the display screen at any given place and moment
	 *  
	 * @return - total time for a truck ride(double)
	 */
	public double getPrecentTime() {
		return (double)timeLeft/totalTime;
	}
	//=====================METHODS====================//
	
	/**
	 * Main office update to produce a TrackingEvent by trucks
	 * @param p - package
	 * @see MainOffice(class) - reportTracking()
	 */
	public synchronized void addAndReportTracking(Package p) {
		TrackingEvent trackEvt = new TrackingEvent(this);	
		trackEvt.setP(p);
		trackEvt.setTrack(p.getFinalTracking());
		for ( TrackingListener  w : list.getListeners(TrackingListener.class) )
		{			
			w.reportTracking(trackEvt);
		}
	}
	public void addListener(TrackingListener listener)
	{
		this.list.add(TrackingListener.class, listener);
	}
	
	//remove listener from list
	public void removeListener(TrackingListener listener)
	{
		this.list.remove(TrackingListener.class, listener);
	}
	/**
	 * Rewrite of a method in order to print the variables of an Truck in a way that suits us
	 */
	@Override
	public String toString() {
		return "Truck [truckID=" + truckID + ", licensePlate=" + licensePlate + ", truckModel=" + truckModel
				+ ", timeLeft=" + timeLeft + ", available=" + available + ", packages=" + packages + "]";
	}
	/**
	 * generating Truck license plate -
	 * A license plate consists of three numbers separated by a line,
	 *  according to the pattern xxx-xx-xxx
	 *  
	 * @return str - string representation of Truck license plate
	 */
	public  String generatelicensPlate() {
		int amount = 10;
		char[] letters = new char[] {' ',' ',' ','-',' ',' ','-',' ',' ',' '};       
		String str="";
		for (int i = 0; i < amount; i++) {
			if(letters[i] == ' ') {
				char c = (char)((int)(Math.random()*10)+48);
				str += c;
			}	   else {
				str += letters[i] ;
			}
		}

		return str;
	}
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	@Override
	public boolean equals(Object other) {		
		if(other instanceof Truck && 
				this.truckID == ((Truck)other).truckID &&
				this.licensePlate == ((Truck)other).licensePlate &&
				this.truckModel == ((Truck)other).truckModel &&
				this.timeLeft == ((Truck)other).timeLeft &&
				this.available == ((Truck)other).available && 
				this.packages.size() ==((Truck)other).packages.size() &&
				this.packages.equals(((Truck)other).packages)){
			return true;
		}
		return false;
	}
	/**
	 * A Truck model consists of M letter and a number between 0 and 4
	 * @return string representation of truck model
	 */
	public  String generateTruckModel() {
		return "M"+(int)(Math.random() * 5 ) ;
	}	
	
	/**
	 * Function for adding a package for loading on a truck
	 * @param p - package
	 */
	public synchronized void addToPackages(Package p) {
		if(p != null) {
			this.packages.add(p);			
		}
	}
	/**
	 * Function for removing packages from a truck
	 * @param p - package
	 */
	public synchronized void removePackagesFromTruck(Package p) {
		if(p != null) {
			try {
				for(int i=0 ; i<packages.size() ;i++) {
					if(packages.get(i).equals(p)) {
						packages.remove(i);
						return;
					}
				}				
			} catch( NullPointerException e) {
				System.out.println("Exeption occured - "+this );
			}
		}		
	}
	
	@Override
	public abstract void collectPackage(Package p);
	@Override
	public abstract void deliverPackage(Package p);		
	@Override
	public abstract void work();
	
	/**
	 * Function for running a truck-type process,
	 *	A process only works when a truck is not 
	 *	available = while traveling.
	 *	Once a truck is available = parking is wait
	 *
	 *@see wait,sleep - Thread class
	 */	
	@Override
	public void run() {
			while(GUI.mainFrame.isRunning) {			
				while(this.getAvailable()){
					synchronized(this) {
						try {
							wait();
						} catch (InterruptedException e) {
							return;
						}					
					}
				}
				work();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}			
		}
		
}
