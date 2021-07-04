package Components;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

import GUI.mainFrame;

/**
 * Customer 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	MainOffice
 */
public class Customer implements Runnable,Node{
	private static final int numOfPackages=5;
	private int serialNum;
	private Address customerAddress;
	private MainOffice inst;
	private Vector<Package> myPackages  = new Vector<Package>(numOfPackages);
	private  int countPackagesDeliverd =0;
	private boolean allPackagesCreated = false;
	private int countPackageCreated=0;
	
	//=====================CONSTUCTURS====================//
	/**
	 * Initializes the customer new Object,by entering a serial number to the parameter,entering the customer's address and creating a new reference to main Office 
	 * @param serial
	 * @param address
	 */
	public Customer(int serial , Address address) {
		this.serialNum = serial;
		this.customerAddress = address;		
		inst = MainOffice.getInstance(serial, serial, null);
	
	}
	/**
	 * Initializes the customer new Object,by copying the parameters from an already existing  customer,
	 * we need to pay attention to that the list packages of the new customer is initialized by entering a reference of an already existing package in originator that equivalent to an object that already existing package in the other object (by id). 
	 * @param other
	 */
	public Customer(Customer other) {
		this.serialNum = other.getSerialNum();
		this.customerAddress =new Address(other.getCustomerAddress());
		this.inst = other.getInst();
		this.countPackagesDeliverd = other.getCountPackagesDeliverd();
		this.allPackagesCreated = other.isAllPackagesCreated();
		for(int i=0 ; i<other.myPackages.size();i++) {
			for(Package p:Originator.packages) {
				if(p.getPackageID()==other.myPackages.get(i).getPackageID()) {
					myPackages.add(p);
					countPackageCreated++;
				}
			}
		}

	}
	public boolean isAllPackagesCreated() {
		return allPackagesCreated;
	}

	//=====================METHODS====================//
	public MainOffice getInst() {
		return inst;
	}

	public int getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}

	public synchronized int getCountPackagesDeliverd() {
		return countPackagesDeliverd;
	}
	public synchronized void setCountPackagesDeliverd(int countPackagesDeliverd) {
		this.countPackagesDeliverd = countPackagesDeliverd;
	}
	
	public void setCustomerZip(int zip) {
		this.customerAddress.setZip(zip);
	}
	public Address getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(Address customerAddress) {
		this.customerAddress = customerAddress;
	}
	public boolean packageInMyList(int packageID) {
		for(int i=0 ; i<this.myPackages.size() ;i++) {
			Package p = this.myPackages.get(i);
			if(p.getPackageID() == packageID) {
				return true;
			}
		}
		return false;
	}
	public Vector<Package> getMyPackages() {
		return myPackages;
	}

	public void setMyPackages(Vector<Package> myPackages) {
		this.myPackages = myPackages;
	}
	/**
	 * Rewrite of a method in order to print the variables of an Tracking in a way that suits us
	 */
	@Override
	public String toString() {
		return "Customer: "+ this.serialNum +" Packages delivered: "+ this.countPackagesDeliverd+" zip: "+this.getCustomerAddress().getZip() ;
		
	}

	public int randomize(int min , int max) {
		return (int)(Math.random() * (max - min + 1)) + min;
	}
	/**
	 * the method randomly creates a new package and adds it to the customer
	 *  and mainOffice(and reports that its need to be added to the tracking.txt document
	 */
	public void addPackage() {	
		Random random1 = new Random(); //using Random -instance of this class is used to generate 'Enums'
		Priority[] priority = Priority.values();
	    Priority randPriority =  priority[random1.nextInt(priority.length)];	    
	    int randPackage = randomize(1,3);
	    int randSenderAddrress =  customerAddress.getStreet();
	    int randSenderZip = customerAddress.getZip();
	    int randDestinationAddrress =  randomize(100000,999999);
	    int randDestinationZip =  randomize(0,inst.getHub().getBranches().size()-1);  

	   Package p = null;	   
	    switch (randPackage) {
			case 1:			
				Random random2 = new Random();	     
				boolean randAcknowledge = random2.nextBoolean();	
				p = new SmallPackage(randPriority,new Address(randSenderZip, randSenderAddrress),
								   new Address(randDestinationZip, randDestinationAddrress) , randAcknowledge);	
				break;
			case 2:
				double randWeight = (double)randomize(1,10);
				p = new StandardPackage(randPriority,new Address(randSenderZip,
									   randSenderAddrress),new Address(randDestinationZip, randDestinationAddrress),randWeight);
				break;
			case 3:
				int randWidth = 1; 
				int randLength =  1;
				int randHeight =  1;
				p = new NonStandardPackage(randPriority, new Address(randSenderZip, randSenderAddrress),
										   new Address(randDestinationZip, randDestinationAddrress),randWidth,randLength,randHeight);
				break;	
			default:
				break;				
				
			} 
	    	if(p instanceof NonStandardPackage) {
	    		this.addAndReportTracking(p);
	    		inst.createPackageAtHub(p);// ascribe package to branch
	    	}else {	    
	    		this.addAndReportTracking(p);
	    		inst.creatPackageAtBranch(p);// ascribe package to branch	
	    	}
	    	this.addToPackages(p);	    	
	    	inst.addPackageToMainOffice(p);// add to MainOffice packages - represent all package in system	    
	}
	/**
	 * the method is updating the package status it gets as a parameter to
	 *  CREATION and then reports to main office (by creating a tracking event
	 *   object and call a method) to update the new tracking and write it in the tracking.txt document
	 * @param p
	 */
	public synchronized void addAndReportTracking(Package p) {
		p.addTracking(this,Status.CREATION);
		TrackingEvent trackEvt = new TrackingEvent(this);	
		trackEvt.setP(p);
		trackEvt.setTrack(p.getFinalTracking());
		inst.reportTracking(trackEvt);
	}

	public synchronized void addToPackages(Package p) {
		if(p != null) {
			this.myPackages.add(p);			
		}
	}
	
	
	@Override
	/**
	 * Rewrite of the run method of the thread class that is implemented,
	 * in the start of the method we are creating a new randomly chosen package
	 *  every 2 to 5 seconds,until we create 5 packages.
	 * than the method keeps running until it reads from the tracking.txt document 
	 * that all its packages have arrived.
	 */
	public void run() {
		if(allPackagesCreated==false) {
			for(int i=countPackageCreated ; i<numOfPackages ; i++) {
				int schedule = (int)(Math.random() * (5 - 2 + 1)) + 2;
				try {				
					Thread.sleep(schedule*1000);
				} catch (InterruptedException e) {
					return;
				}			
				this.addPackage();	
			}
			allPackagesCreated=true;			
		}
		while(GUI.mainFrame.isRunning) {		
			if(inst.checkMyPackages(this)) {
				return;
			}
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
			}		
		}
		
	}

	@Override
	public void collectPackage(Package p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliverPackage(Package p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void work() {
		// TODO Auto-generated method stub
		
	}
	

}
