package Components;

import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RunCustomers - Customer Execution Department
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 *
 */
public class RunCustomers implements Runnable {
	private static final int numOfCustomers=10;
	private Vector<Customer> allCustomers = new Vector<Customer>(numOfCustomers);
	private Vector<Thread> allCustomersThread = new Vector<Thread>(numOfCustomers);
	private boolean allCustomersCreated = false;
	private Hub hub;
	private MainOffice inst = MainOffice.getInstance(0, 0, null);
	public static boolean runCustMomento=false;
	private Executor executor;
	
	//=====================CONSTUCTURS====================//
	/**
	 * Initial hub for execution all customers in system
	 * @param hub - sorting center
	 */
	public RunCustomers( Hub hub) {
		this.hub = inst.getHub();
	}
	/**
	 * Copy constructor of Customer Execution Department
	 * 
	 * @param other -RunCustomers
	 * @param hub - sorting center
	 */
	public RunCustomers(RunCustomers other,Hub hub) {
		this.allCustomersCreated = other.isAllCustomersCreated();
		this.hub = hub;
		for(int i=0 ; i<other.allCustomers.size();i++) {
			Customer c = other.allCustomers.get(i);
			this.allCustomers.add(new Customer(c));
		}	
	}
	//=====================METHODS====================//
	public void updateHUB( Hub hub) {
		this.hub = hub;
	}
	public Vector<Customer> getAllCustomers() {
		return allCustomers;
	}
	public void setAllCustomers(Vector<Customer> allCustomers) {
		this.allCustomers = allCustomers;
	}
	public boolean isAllCustomersCreated() {
		return allCustomersCreated;
	}
	public void setAllCustomersCreated(boolean allCustomersCreated) {
		this.allCustomersCreated = allCustomersCreated;
	}
	/**
	 * Method for recalculation and adding another
	 *  possible Zip for a new client added to the system.
	 */
	public void reCalculateZip() {
		for(int i=0 ; i<allCustomers.size(); i++) {
			int randZip =  randomize(0,hub.getBranches().size()-1);
			allCustomers.get(i).setCustomerZip(randZip);
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
	 * Immediate shutdown of a customer's thread
	 * 
	 */
	public void kill() {
		((ExecutorService)executor).shutdownNow();
	}
	/**
	 * The main method for creating customers by using the Thread Pool principle,
	 *  which determines 2 individual Threads in the system that work until the
	 *   end of their operation. Then add the rest of the Threads according 
	 *   to the static number of customers in system.
	 */
	public void createAndRunCustomers() {
		executor = Executors.newFixedThreadPool(2);
		for (int i = 0; i < numOfCustomers; i++) {
			int randStreet =  randomize(100000,999999);
			int randZip =  randomize(0,hub.getBranches().size()-1);
			Address address= new Address(randZip,randStreet);
			Customer customer = new Customer(i+1,address);
			allCustomers.add(customer);	
			executor.execute (customer);
		}	
		((ExecutorService) executor).shutdown(); 
	}
	public void setrunCustMomento(boolean b) {
		runCustMomento=b;
	}
	/**
	 * A method for running Threads of customers 
	 * that already exist in the system.
	 */
	public void RunCostumers() {
		runCustMomento=false;
		Executor executor = Executors.newFixedThreadPool(2);
		for (int i = 0; i < numOfCustomers; i++) {
			executor.execute (allCustomers.get(i));
		}
		((ExecutorService) executor).shutdown(); 
	}
	/**
	 * Method of running a main process of customers.
     * As long as the system is working, a customer's process is running,
     * if a branch is added to the system, we will recalculate the customers'
     * addresses and from there a method continues to run.
	 */
	@Override
	public void run() {
		if(!runCustMomento) {
			createAndRunCustomers();
		}
		else {
			RunCostumers();
		}
		while(GUI.mainFrame.isRunning) {	
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException e) {
					return;
				}				
			}
			reCalculateZip();
		}	
	}

}
