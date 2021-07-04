//Assignment 2
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.event.EventListenerList;
/**
 * Branch 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	Truck
 */
public class Branch  implements Node,Runnable,Cloneable{
	public static int id = -1;
	private int branchId;	
	private String branchName;
	private boolean getDelivery = false;
	private int packagesInBranch;
    private PropertyChangeSupport support; 
	private ArrayList <Truck> listTrucks ;// refers to van only
	private Vector <Package> listPackages;
	private EventListenerList list;
	private Vector <Thread> truckThreads;
	
	//=====================CONSTUCTURS====================//
	/**
	 * Initializes a new object of a branch and the fields in it, the given parameter initializes in it the branchName field
	 *In addition there is an addition of PropertyChangeSupport in order to convey a message if we need a department to listen to it
	 *In addition to each object, the ID number is added according to a static variable that is promoted afterwards
	 * @param branchName
	 */
	public Branch(String branchName) {	
		this.branchId = id;
		this.branchName = branchName;
		this.listTrucks = new ArrayList <Truck>();
		this.listPackages = new Vector <Package>();
		support = new PropertyChangeSupport(this); 
		id++;
		list = new EventListenerList();
		truckThreads = new Vector<Thread>();
	}
	/**
	 * Initializes a new object of a branch and the fields in it,the parameter that the constructor is getting is an already existing Branch Object/
	 * all the immutable parameters are copied,support,list and TruckThreads parameters are initialize.
	 * listTrucks is copied(every single truck of the branch is copied by the help of the Truck copy constructor.
	 * the packages list is getting an reference to the packages that are already existing in the originator packages that are similar(by id) to those in the other Object.
	 * @param other
	 */
	public Branch(Branch other) {
		this.branchId=other.getBranchId();
		this.branchName=other.getBranchName();
		this.packagesInBranch = other.getPackagesInBranch();
		CopyListTrucks(other.getListTrucks());
		this.listPackages = new Vector <Package>();
		this.support = new PropertyChangeSupport(this); 
		this.list = new EventListenerList();
		truckThreads = new Vector<Thread>();
		this.getDelivery=other.getDelivery;
		for(int i=0 ; i<other.listPackages.size();i++) {
			for(Package p:Originator.packages) {
				if(p.getPackageID()==other.listPackages.get(i).getPackageID()) {
					listPackages.add(p);
				}
			}
		}
		
	}
	public int getPackagesInBranch() {
		return packagesInBranch;
	}
	//=====================GETTERS&SETTERS====================//
	public void addToTruckThreads(Thread t) {
		truckThreads.add(t);
	}
	/**
	 * The method is copying every truck from the list of trucks it gets as a parameter to the object(this) list of trucks by the help of the copy constructor of standardTruck or NonStandardTruck or Van.
	 * @param other
	 */
	public void CopyListTrucks(ArrayList <Truck> other) {
		listTrucks = new ArrayList<Truck>(other.size());
		for(int i=0 ; i< other.size() ; i++) {
			Truck t=other.get(i);
			if(t instanceof NonStandardTruck) {
				listTrucks.add(new NonStandardTruck((NonStandardTruck)t));
			}else if(t instanceof StandardTruck) {
				listTrucks.add(new StandardTruck((StandardTruck)t));
			}else if(t instanceof Van) {
				listTrucks.add(new Van((Van)t));
			}
		}	
	}

	public boolean isGetDelivery() {
		return getDelivery;
	}
	public void setGetDelivery(boolean getDelivery) {
		this.getDelivery = getDelivery;
	}
	
	public void setListTrucks(ArrayList<Truck> lst) {
		this.listTrucks = lst;
	}
	/**
	 * Check if there is at least one package in branch that is in BRANCH_STORAGE status
	 *If so the method returns true otherwise false
	 * @return boolean(true/false)
	 */
	public boolean packageInBranchStorage() {
		for(int i=0 ; i<this.getListPackages().size() ; i++) {
			Package p = this.getListPackages().get(i);
			if(p.getStatus() == Status.BRANCH_STORAGE) {
				return true;
			}
		}
		return false;
	}
	public String getBranchName() {
		return branchName;
	}
	public ArrayList<Truck> getListTrucks() {
		return listTrucks;
	}
	public Vector<Package> getListPackages() {
		return listPackages;
	}
	public int getBranchId() {
		return branchId;
	}
	public void setBranchId(int id) {
		this.branchId=id;
	}
	public void setBranchName(String s) {
		this.branchName=s;
	}
	//=====================METHODS====================//
	@Override
	/**
	 * the method is cloning the this object,and creating a new Branch.
	 * the method returns the new cloned object of Branch.
	 */
	public Branch clone() {
		Branch b=new Branch(this);
		b.setBranchId(id);
		b.setBranchName("Branch "+(id+""));
		b.listPackages = new Vector <Package>();
		b.packagesInBranch =0;
		b.getDelivery = false;
		id++;
		return b;
		
	}
	public void addListener(TrackingListener listener)
	{
		this.list.add(TrackingListener.class, listener);
	}
	/**
	 * the method kills all the trucks threads of the trucks in the branch.
	 */
	public void kill() {
		for(int i=0;i<truckThreads.size();i++) {
			(truckThreads.get(i)).interrupt();
		}
	}
	//remove listener from list
	public void removeListener(TrackingListener listener)
	{
		this.list.remove(TrackingListener.class, listener);
	}

	/**
	 * Because PropertyChangeListener is contained in the class it is necessary to rewrite his methods in order to apply them later
	 * Adds PropertyChangeListener
	 * @param pcl
	 */
	public void addPropertyChangeListener(PropertyChangeListener pcl){ 	support.addPropertyChangeListener(pcl); 
	} 
	/**
	 * Because PropertyChangeListener is contained in the class it is necessary to rewrite his methods in order to apply them later
	 * remove PropertyChangeListener
	 * @param pcl
	 */
	public void removePropertyChangeListener(PropertyChangeListener pcl){ 	support.removePropertyChangeListener(pcl); 
	} 
	/**
	 * Counts the number of packages in branch that are in BRANCH_STORAGE status and inserts the value into the variable packagesInBranch
	 */
	public synchronized void countPackageAtBranch() {
		packagesInBranch=0;
		for(int i=0; i<this.listPackages.size() ; i++) {
			Package p = this.listPackages.get(i);
			if(p.getStatus() == Status.BRANCH_STORAGE) {
				packagesInBranch++;
			}
		}
	}
	
	
	/**
	 * Adds the package it receives to the branch Vector variable listPackages
	 * @param p
	 */
	public  void addPackagesToBranch(Package p) {
		if(p!=null) {
			listPackages.add(p);
		}
	}
	/**
	 * Removes the package it receives from the branch Vector variable listPackages
	 * @param p
	 */
	public  void removePackagesFromBranch(Package p) {
		if (p!=null) {
			for(int i=0 ; i<listPackages.size() ;i++) {
				if(listPackages.get(i).getPackageID() == p.getPackageID()) {
					listPackages.remove(i);
				}
			}			
		}	
	}

	
	/**
	 * Represents a work unit,
	 *For each package that is in the branch, if it is in the waiting status for collection from a customer, an attempt is made to collect - if there is a vehicle available, he goes out to collect the package. The resulting travel time is updated in the vehicle in the timeLeft field and the condition of the vehicle changes to "not available".
	 *Same as for any package waiting to be distributed, if there is a vacant vehicle, it is sent to deliver the package. Time is calculated according to the same formula, but according to the recipient's address.
	 */
	@Override
	public void work() {	
		for(int i=0 ; i<this.listPackages.size() ;i++) {//collect or deliver all packages 			
			Package p = listPackages.get(i);		
			collectPackage(p);
			deliverPackage(p);			
		}
	}
	@Override
	/**
	 * For p package that is in the branch, if it is in the waiting status for collection from a customer, an attempt is made to collect - if there is a vehicle available-in this case we checking on Vans, he goes out to collect the package. The resulting travel time is updated in the vehicle in the timeLeft field and the condition of the vehicle changes to "not available".
	 * @param p
	 */
	public synchronized void collectPackage(Package p) {//collect all packages from customers
		if(p.getStatus() == Status.CREATION) {
			for(int j=0 ; j<this.listTrucks.size() ;j++) {// check which van is available in this branch
				Van van = (Van) this.listTrucks.get(j);
				if(van.getAvailable()) {					
					van.setTimeLeft(van.generateTimeLeft(p.getSenderAddress().getStreet()));
					van.setAvailable(false);
					p.setStatus(Status.COLLECTION);
					p.addTracking(van, p.getStatus());
					van.addAndReportTracking(p);
					van.setDestination(VerticeType.SourchPackage, p.getPackageID());
					van.setSource(VerticeType.Branch, this.getBranchId());
					van.addToPackages(p);// add package to Van
					synchronized(van) {
						van.notifyAll();						
					}
					return;//if found an available van it is send and there is no need to found a new van
				}
			}
		}else if(p.getStatus() == Status.COLLECTION ) {
			for(int i=0 ; i<this.listTrucks.size() ;i++) {
				Van van = (Van) this.listTrucks.get(i);					
					if(van == p.getTracking().get(p.getTracking().size()-1).getNode()) {
						synchronized(van) {
							van.notifyAll();							
						};						
						return;//if the van that collected the package is found there is no need to keep looking
					}									
			}
		}

	
	}
	@Override
	/**
	 * For p package waiting to be distributed, if there is a van vehicle, it is sent to deliver the package.
	 * @param p
	 */
	public synchronized void deliverPackage(Package p) {
		 if(p.getStatus() == Status.DELIVERY) {// deliver all packages from branch to destination customer
				for(int j=0 ; j<this.listTrucks.size() ;j++) {// check which van is available in this destination branch
					Van van = (Van) this.listTrucks.get(j);
					if(van.getAvailable()) {
						van.setTimeLeft(van.generateTimeLeft(p.getDestinationAddress().getStreet()));
						van.setAvailable(false);							
						p.setStatus(Status.DISTRIBUTION);
						p.addTracking(van, p.getStatus());
						van.addAndReportTracking(p);						
						van.setDestination(VerticeType.DestinationPackage, p.getPackageID());
						van.setSource(VerticeType.Branch, this.getBranchId());
						van.addToPackages(p);// add package to Van
						synchronized(van) {
							van.notifyAll();							
						};
						return;//if found an available van it is send and there is no need to found a new van
					}
				}
			}else if(p.getStatus() == Status.DISTRIBUTION ) { 
				for(int i=0 ; i<this.listTrucks.size() ;i++) {
					Van van = (Van) this.listTrucks.get(i);					
						if(van == p.getTracking().get(p.getTracking().size()-1).getNode()) {
							synchronized(van) {
								van.notifyAll();							
							};					
						}									
				}
				
			}

		
	}
	@Override
	/**
	 * Rewrite of a method in order to print the variables of an branch in a way that suits us
	 */
	public String toString() {
		return " Branch "+branchId+ ", branch name: "+branchName+","
				+ " packages: "+listPackages.size()+", trucks: "+listTrucks.size();
	}
	@Override
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	public boolean equals(Object other) {	
		if( other instanceof Branch) {
			if(this.branchId == ((Branch)other).branchId &&
				this.branchName == ((Branch)other).branchName &&
				this.listTrucks.size() == ((Branch)other).listTrucks.size()&&
				this.listTrucks.equals(((Branch)other).listTrucks)&&
				this.listPackages.size() == ((Branch)other).listPackages.size()&&
				this.listPackages.equals(((Branch)other).listPackages)) {
			return true;
			}
		}		
		return false;
	}

	@Override
	/**
	 * Rewrite of the run method of the thread class that is implemented, in which at first there is creation and activation of its Van's threads (will only be done once in our program for each branch), then we will reach the while loop where we stay until the end of the boolean static variable run Of the MainFrame class, inside the loop there is a check that if there are no packages in the branch there is no need to actually activate the thread so we put it in wait mode (it exits this mode when we notify it and the amount of its packages will be non-zero).
	 * If it goes out of wait we will run the work method then go into sleep mode for 500 milliseconds and check if there are more than 0 packages waiting for collection, then we run the firePropertyChange method which informs HUB that we need it to send us a standard truck.
	 */
	public void run() {
		for( int i=0 ; i<this.getListTrucks().size() ; i++) {
			Truck t = this.getListTrucks().get(i);
			Thread tmp = new Thread(t);
			tmp.start();
			truckThreads.add(tmp);
		}	
			while(GUI.mainFrame.isRunning) {
				while(this.listPackages.size()== 0) {
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
				synchronized(this) {
					countPackageAtBranch();
					support.firePropertyChange("packagesInBranch",this.packagesInBranch, 0);	
				}
			}
		}
}
