//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Vector;

/**
 * sorting center  
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see     Branch
 */

public class Hub extends Branch implements Node,PropertyChangeListener {
	private Vector<Branch> branches;
	private Vector<Thread> branchThreads;


	private static int iter = -1;//Using a static variable to create circularity in the passage over the branches parallel to the loop of trucks in each branch
	//=====================CONSTUCTURS====================//
	/**
	 * Initializes a new HUB object by using the constructor of its parent class, Branch.
	 */
	public Hub() {
		super("HUB");
		branchThreads = new Vector<Thread>();
	}
	/**
	 * Initializing a new Hub Object by the already existing Hub 
	 * it gets,the parameters are copied(also by the help of the copy constructor of Branch)
	 * @param other
	 */
	public Hub(Hub other) {
		super(other);
		branchThreads = new Vector<Thread>();
		branches = new Vector<Branch>(other.getBranches().size());
		for(int i=0 ; i<other.getBranches().size() ;i++) {
			Branch b = other.getBranches().get(i);
			branches.add(new Branch(b));
		}
		
	}
	//=====================GETTERS&SETTERS====================//

	public void setBranches(Vector<Branch> branches) {
		this.branches = branches;
	}
	

	public Vector<Branch> getBranches() {
		return branches;
	}
	//=====================METHODS====================//
	
	public void addBranch(Branch b) {
		if(b!=null) {
			branches.add(b);
		}
	}
	   /**
	    * Receives a specific event and executes a code 
	    * snippet in which it responds to a branch request
	    *  to send a standard truck to it 
	    * @param evt 
	    *        event gets delivered whenever
	    *        a bean changes a "bound" or "constrained" property.
	    *    
	    */
    public void propertyChange(PropertyChangeEvent evt) {
		Branch tmp;    	
		tmp=(Branch)evt.getSource();
		StandardTruck st_truck;	
		if(!tmp.isGetDelivery() && tmp.packageInBranchStorage()) {//if branch don't have truck on the way to pickup packages
			for (int i = 0; i < getListTrucks().size(); i++) {			
				if(getListTrucks().get(i) instanceof StandardTruck) {
					st_truck=(StandardTruck) getListTrucks().get(i);				
					if(st_truck.getAvailable()) {
						st_truck.setAvailable(false);
						st_truck.setSource(this);//from HUB					
						st_truck.setDestination(tmp);//set destination to truck 
						tmp.setGetDelivery(true);
						st_truck.setTimeLeft(st_truck.generateTimeLeft(1,10));
						for(int j=0 ; j<this.getListPackages().size() ; j++) {
							Package p = this.getListPackages().get(j);
							if( p instanceof StandardPackage || p instanceof SmallPackage) {
								if(st_truck.getDestination().getBranchId() == p.getDestinationAddress().getZip()) {// match between truck destination(branch) to package destination - branchId->Zip	
									if(p.getStatus() == Status.HUB_STORAGE) {// if destination already in HUB 
										p.setStatus(Status.BRANCH_TRANSPORT) ; // loaded package on standardTruck
										p.addTracking(st_truck, p.getStatus());// set tracking for truck and status
										st_truck.addAndReportTracking(p);
										this.removePackagesFromBranch(p);//remove package from HUB											
										j=-1;			
										st_truck.addToPackages(p);// add package to standardTruck
									}					
								}	
							}
						}
						synchronized(st_truck) {
							st_truck.notifyAll();					
						}
						return;//if found an available standard truck it is send and there is no need to found a new standard truck
					}				
				}
			}		
			
		}else {
			return;
		}
    } 

	@Override    
	/**
	 * Rewrite of a method in order to print the variables of an HUB in a way that suits us
	 */
	public String toString() {
		return super.toString();			
	}
	@Override
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	public boolean equals(Object other) {		 
		if(other instanceof Hub &&
			this.branches.size() == ((Hub)other).branches.size()&&
			this.branches.equals(((Hub)other).branches)) {
				return true;
		}				
		return false;
	}
	@Override
	public void collectPackage(Package p) {			
	}
	@Override
	public void deliverPackage(Package p) {		
	}
	/**
	 * The method checks whether the HUB has packages for the same branch that it accepts as a variable.
	 * If so the method returns true otherwise false
	 * @param b
	 * @return boolean(true/false)
	 */
	public boolean checkIfHubContainPackagesToBranch(Branch b) {
		for(int i=0 ; i<this.getListPackages().size() ; i++) {
			Package p = this.getListPackages().get(i);
			if(p instanceof SmallPackage || p instanceof StandardPackage) {
				if(p.getDestinationAddress().getZip() == b.getBranchId()) {
					return true;
				}				
			}
		}
		return false;
	}
	@Override
	/**
	 * Work unit,
	 * For any standard sorting center truck, if the truck is available, it is sent to some local branch for the purpose of the trip and the truck will load all the packages waiting for it to be transferred to the branch to which it is traveling. The status of the packages and their history are updated accordingly.
	 * If the non-standard truck is available, it will be checked whether there is a non-standard package in the sorting center that is waiting to be collected and that its dimensions fit the truck. If so, the truck will be sent to pick up the package.
	 */
	public void work() {
		for(int k=0 ; k < this.getListTrucks().size(); k++) { // loop for HUB trucks
			Truck hub_truck = getListTrucks().get(k);			
			if(hub_truck instanceof StandardTruck ) {
				StandardTruck st_truck =((StandardTruck) hub_truck);
				
				synchronized (st_truck) {
					st_truck.notifyAll();								
				}
				if(st_truck.getAvailable()==true) {
					iter++;
					if(iter >= this.getBranches().size()) {// check who is the first to arrive back to HUb and send him to the next branch
						iter=0;
					}
					for(int i=0 ; i<st_truck.getPackagesSize() ;i++) {// unloaded packages at hub and removed from standardTruck
						Package p =st_truck.getPackage(i); 
						p.setStatus(Status.HUB_STORAGE);
						this.addPackagesToBranch(p);//loaded packages at HUB
						st_truck.removePackagesFromTruck(p);// unloaded packages from standardTruck
						p.addTracking(this, Status.HUB_STORAGE);
						st_truck.addAndReportTracking(p);
						i=-1;
					}
					
					if(checkIfHubContainPackagesToBranch(branches.get(iter))) {
						if(!branches.get(iter).isGetDelivery()){
							st_truck.setSource(this);//from HUB					
							st_truck.setDestination(branches.get(iter));//set destination to truck 
							branches.get(iter).setGetDelivery(true);
							synchronized(branches.get(iter)) {
								branches.get(iter).notifyAll();
							}	
							st_truck.setAvailable(false);
							for(int g=0 ; g < this.getListPackages().size() ;g++) {// loop for HUB packages
								Package p =this.getListPackages().get(g);						
								if(st_truck.getDestination().getBranchId() == p.getDestinationAddress().getZip()) {// match between truck destination(branch) to package destination - branchId->Zip													
									if(p.getStatus() == Status.HUB_STORAGE) {// if destination already in HUB 
										p.setStatus(Status.BRANCH_TRANSPORT) ; // loaded package on standardTruck
										p.addTracking(st_truck, p.getStatus());// set tracking for truck and status
										st_truck.addAndReportTracking(p);
										this.removePackagesFromBranch(p);//remove package from HUB
										g=-1;
										st_truck.addToPackages(p);// add package to standardTruck
										synchronized (st_truck) {
											st_truck.notifyAll();								
										}
									}					
								}					
							}				
							st_truck.setTimeLeft(st_truck.generateTimeLeft(1,10));	// time from HUB to branch							
						}
					}
				}
			}
			else { //NonStandardTruck of HUB case 				
				if(hub_truck instanceof NonStandardTruck && hub_truck.getAvailable()==true) {
					NonStandardTruck nst_truck =((NonStandardTruck) hub_truck);	
					for(int g=0 ; g <this.getListPackages().size() ;g++) {//packages at HUB
						Package p =this.getListPackages().get(g);
						if(p instanceof NonStandardPackage) {
							NonStandardPackage ns_package = ((NonStandardPackage)p);
							if(ns_package.getStatus() == Status.CREATION )
								{
								ns_package.setStatus(Status.COLLECTION);//package got picked by NonStandardTruck
								ns_package.addTracking(nst_truck, Status.COLLECTION);// add tracking to package which loaded by NonStandardTruck
								nst_truck.addAndReportTracking(p);
								nst_truck.addToPackages(ns_package);// loaded package at NonStandardTruck
								nst_truck.setTimeLeft(nst_truck.generateTimeLeftToSender(1,10));								
								nst_truck.setDestination(VerticeType.SourchPackage,p.getPackageID());
								nst_truck.setSource(VerticeType.HUB, -1);								
								nst_truck.setAvailable(false);// NonStandardTruck on the road
								synchronized (nst_truck) {
									nst_truck.notifyAll();								
								}
								return;														
							}								
						}
					}											
				}else if(hub_truck instanceof NonStandardTruck){ // if NonStandardTruck still on the road continue Work
					NonStandardTruck nst_truck =((NonStandardTruck) hub_truck);	
					synchronized (nst_truck) {
						nst_truck.notifyAll();								
					}

				}
				
			}					
		}
	
	}
	/**
	 * the method starts with activating the method kill of every branch to kill all the trucks threads and after that the method kills all the branches threads
	 */
	public void kill() {
		super.kill();
		for(int i=0;i<branches.size();i++) {
			(branches.get(i)).kill();
		}
		for(int i=0;i<branchThreads.size();i++) {
			(branchThreads.get(i)).interrupt();
		}
		
	}

	@Override
	/**
	 * Rewrite of the run method of the thread class that is implemented,
	 * in which at first there is creation and activation of its Truck's threads (will only be done once in our program for each branch) and the branches threads,
	 * then we will reach the while loop where we stay until the end of the boolean static variable run Of the MainFrame class,
	 *we will run the work method then go into sleep mode for 500 milliseconds 
	 */
	public void run() {
		for( int i=0 ; i<this.getListTrucks().size() ; i++) {
			Truck t = this.getListTrucks().get(i);
			Thread tmp=new Thread(t);
			tmp.start();
			addToTruckThreads(tmp);
		}
		for(int d=0 ; d< this.branches.size() ; d++) {// branch looping by order			
 			Branch b = this.branches.get(d);
 			b.addPropertyChangeListener(this);//adding a listener to every branch
 			Thread tmp = new Thread(b);
 			tmp.start();
 			branchThreads.add(tmp);
 		}
			while(GUI.mainFrame.isRunning) {		
				work();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}			
			}
			
		}
	
}
