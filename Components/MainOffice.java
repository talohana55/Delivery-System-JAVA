//Assignment 1
//Members: Tal ohana - 307833186
//         Tali Tevlin - 206999195
package Components;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import GUI.displayPanel;
import GUI.mainFrame;
/**
 * Main Office - manage all branches,trucks and sorting center  
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 */
public class MainOffice implements TrackingListener {
	public final static String path = "D:\\";
	private static int clock;	
	private static int countRow = 0;
	private static final int  numOfCustomers = 10;
	private static Hub hub;
	private static Thread hubThread;
	private ArrayList <Package> packages;
	private static RunCustomers rc;
	private static Thread RCThread;
	private displayPanel dp;
	public static boolean pauseSystem=true;
	ReadWriteTracking readAndWrite; 
	static private volatile MainOffice instance = null;	// as part of singleton properties	
	//=====================CONSTUCTURS====================//
	/**
	 * Initializes a new MainOffice class object by the parameters it receives,
	 * The hub maker adds an array of branches and an array of Trucks to him and 
	 * to each of his branches according to the size she received.
	 * In addition there is data transfer to the GUI of a pointer to HUB and transfer
	 *  of a pointer to the packages that will be created later.
	 *  
	 *  private constructor as part of singleton properties
	 * @param branches
	 * @param trucksForBranch
	 * @param dP
	 * @throws FileNotFoundException 
	 */
	private MainOffice(int branches, int trucksForBranch,displayPanel dP)  {
		hub = new Hub();
		setListTrucksForHub(trucksForBranch);
		Vector<Branch> branchesList = new Vector<Branch>(branches) ;//branches for HUB
		packages = new  ArrayList <Package>();
		for(int i=0 ; i<branches;i++) {//initialize branches
			Branch b = new Branch("Branch "+i+"");	
			b.addListener(this);
			branchesList.add(b);
			setListTrucksForBranches(branchesList.get(i),trucksForBranch);						
		}	
		hub.setBranches(branchesList);	
		this.dp = dP;
		dp.addHUB(hub);//fetch Hub into displayPanel class
		dp.addAllPackages(packages);//fetch Packages into displayPanel class
		try {
			readAndWrite = new ReadWriteTracking(path+"tracking.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//=====================GETTERS&SETTERS====================//
	public ReadWriteTracking getReadAndWrite() {
		return readAndWrite;
	}
	/**
	 * Import method of a main office instance as part of a 
	 * singleton properties, using a double check locking operation.
	 * 
	 * @param branches -Branch
	 * @param trucksForBranch -amount ao trucks per branch
	 * @param dP - display panel
	 * @return - main office instance
	 */
  public static MainOffice getInstance(int branches, int trucksForBranch,displayPanel dP) {
       if (instance == null)
          synchronized(MainOffice.class){   
              if (instance == null)
                  instance = new MainOffice(branches, trucksForBranch, dP);
          }
       return instance;
    }
    public void setHub(Hub hub) {
		MainOffice.hub = hub;
	}
	public displayPanel getDp() {
		return dp;
	}

	public void setDp(displayPanel dp) {
		this.dp = dp;
	}
	public void setPackages(ArrayList<Package> packages) {
		this.packages = new ArrayList<Package>();
		this.packages = packages;
	}
	public static int getClock() {
		return clock;
	}
	public ArrayList<Package> getPackages() {
		return packages;
	}
	public Hub getHub() {
		return hub;
	}
	
	public static void setClock(int clock) {
		MainOffice.clock = clock;
	}
	//=====================METHODS====================//
	
	/**
	 * Main method for creating and running all system Threads
	 */
	public void StartThemAll() {
		playHubThread();
		rc.setrunCustMomento(true);
		RCThread=new Thread(rc);
		RCThread.start();
		
	}
	
	public void startRCTread(RunCustomers rc){
		RCThread=new Thread(rc);
		RCThread.start();
	}
	/**
	 * Initializes the standard trucks for HUB according to the size variable it receives, plus one more non-standard truck
	 * @param size
	 */
	public void setListTrucksForHub(int size) {
		ArrayList<Truck> listTrucks = new ArrayList<Truck>(size);
		for(int i=0 ; i< size ; i++) {
			StandardTruck st = new StandardTruck();
			st.addListener(this);
			listTrucks.add(st);
		}
		NonStandardTruck nst = new NonStandardTruck("730-50-386","M2",1400,400,300);
		nst.addListener(this);
		listTrucks.add(nst);
		hub.setListTrucks(listTrucks);
				
	}
	public RunCustomers getRc() {
		return rc;
	}

	/**
	 * Initializes the Van's for each branch according to the size variable it receives
	 * @param size
	 */
	public void setListTrucksForBranches(Branch b,int size) {
		ArrayList<Truck> listTrucks = new ArrayList<Truck>(size);
		for(int i=0 ; i< size ; i++) {
			Van van = new Van(b);
			van.addListener(this);
			listTrucks.add(van);			
		}	
		b.setListTrucks(listTrucks);
	}

	/**
	 * Method for adding packages to the main office 
	 * and creating TrackingEvent for each addition
	 *  and follow-up report.
	 * @param p -Package
	 */
	public void addPackageToMainOffice(Package p) {
		if(p!= null) {
			this.packages.add(p);
			TrackingEvent trackEvt = new TrackingEvent(this);		
			trackEvt.setTrack(p.getFinalTracking());				
			reportTracking(trackEvt);			
		}
	}
	@Override
	/**
	 * Rewrite of a method in order to print the variables of an MainOffice in a way that suits us
	 */
	public String toString() {
		return "MainOffice [hub=" + hub.toString() +"packages="+  packages.toString() + "]";
	}
	@Override
	/**
	 * Rewrite of a method to compare two objects, by comparing their variables, of the same class.
	 * if the objects are equal the method returns true otherwise false.
	 * @param other(Object)
	 * @return boolean(true/false)
	 */
	public boolean equals(Object other) {			 
		if(other instanceof MainOffice &&
			MainOffice.hub.equals(((MainOffice)other).hub)&&
			this.packages.size() == ((MainOffice)other).packages.size()&&
			this.packages.equals(((MainOffice)other).packages)) {
				return true;
		}				
		return false;
	}
	/**
	 * The method checks if the HUB thread is running then kills it and then creates in both cases
	 * new thread and activates it
	 */
	public void playHubThread() {	
		if(hubThread!= null && hubThread.isAlive()) {
			hubThread.stop();			
		}
		hubThread = new Thread(hub);
		hubThread.start();
	}

	/**
	 * The method receives a package which it adds to HUB packages
	 * @param p
	 */
	public void createPackageAtHub(Package p) {// method - add to MainOffice packages - represent all package in system
		hub.addPackagesToBranch(p);
	}
	/**
	 * The method receives a package finds the appropriate branch and adds it to it
	 * @param p
	 */
	public void creatPackageAtBranch(Package p) {// method - ascribe package to branch
		for(int i=0 ; i< hub.getBranches().size() ;i++) {
			Branch branch = hub.getBranches().get(i);
			if(branch.getBranchId() == p.getSenderAddress().getZip()) {				
				branch.addPackagesToBranch(p);
				
				synchronized(branch) {
					branch.notifyAll();
				}
				return;
			}
		}
	}
	/**
	 * The method randomly selects a value from the range it receives and returns it
	 * @param min
	 * @param max
	 * @return
	 */
	public int randomize(int min , int max) {
		return (int)(Math.random() * (max - min + 1)) + min;
	}
	/**
	 * The method helps us to print the watch in a format that suits us and returns it as a String
	 * @param pTime
	 * @return
	 */
	public String clockString(int pTime) {
	    return String.format("%02d:%02d", pTime / 60, pTime % 60);
	}

	/**
	 * A method that checks whether a customer's 
	 * packages were delivered to the destination 
	 * by reading from a package tracking file.
	 * @param c - Customer
	 * @return - boolean value
	 */
	public boolean checkMyPackages(Customer c) {
		boolean myPackagesStatus = false;
		try {
			myPackagesStatus = readAndWrite.read(c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myPackagesStatus;
		
	}
	
	/**
	 * Method for updating (writing) tracking 
	 * packages in a system tracking file by 
	 * changing status of every package.
	 * @param evt - event consists of a package and tracking
	 * @see TrackingEvent class
	 */
	@Override
	public synchronized void reportTracking(TrackingEvent evt) {
		if(evt.getP() != null) {
			countRow++;
			String temp = countRow+ ". "+" last Tracking-"+evt.getTrack() +" Package ID:"+evt.getP().getPackageID()+"\n";
			try {
				this.readAndWrite.write(temp);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * Method for creating a new branch by cloning
	 *  another branch and making that branch independent
	 * @param b -Branch to clone
	 */
	public void makeBranch(Branch b) {
		Branch newBranch = b.clone();
		setListTrucksForBranches(newBranch, getHub().getBranches().get(b.getBranchId()).getListTrucks().size());
		this.getHub().addBranch(newBranch);		
		newBranch.addPropertyChangeListener(hub);
		(new Thread(newBranch)).start();
		synchronized(rc) {
			rc.notify();
		}
	}
	/**
	 * Method for updating customers in the system 
	 * after a clone of a branch and sending a restored HUB 
	 * to customers
	 * @param hub - sorting center
	 * @param rc  - customers
	 */
	public void refreshAndSetCustomer(Hub hub,RunCustomers rc) {
		MainOffice.rc = rc;
		rc.updateHUB(hub);	
	}
	
	public void setRunCustomers(RunCustomers rc) {
		MainOffice.rc = rc;		
	}
	/**
	 * A main method that kills all Threads 
	 * in the system after a branch clone and at the
	 *  end of the whole system operation
	 */
	public static void killThemAll() {
		mainFrame.isRunning = false;
		hub.kill();
		hubThread.interrupt();
		rc.kill();
		RCThread.interrupt();
			
	}

}
