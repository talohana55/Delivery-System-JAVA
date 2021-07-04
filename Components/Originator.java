package Components;

import java.io.IOException;
import java.util.ArrayList;

import GUI.displayPanel;
/**
 * Originator - create the current state of the system (Memento) 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 */
public class Originator {
	private Hub hub;
	private RunCustomers rc;
	public static ArrayList <Package> packages = new ArrayList <Package>();	
	ReadWriteTracking readAndWrite;
	private Memento memento;
	private int clock; 
	
	/**
	 * Method for maintaining the current state of the system, through a singelton
	 *  object of a main office all the parameters of the system are getting copied.
	 *  creating a copy tracking file
	 *  
	 * @param m - MainOffice singelton instance
	 */
	public void setState(MainOffice m) {
		clock=MainOffice.getClock();
		for(int i=0 ; i<m.getPackages().size();i++) {
			Package p = m.getPackages().get(i);
			if(p instanceof NonStandardPackage) {
				packages.add(new NonStandardPackage((NonStandardPackage)p));	
			}else if(p instanceof StandardPackage) {
				packages.add(new StandardPackage((StandardPackage)p));
			}else if(p instanceof SmallPackage) {
				packages.add(new SmallPackage((SmallPackage)p));
			}
		}
		this.hub = new Hub(m.getHub());
		for(Branch b:hub.getBranches()) {
			b.addListener(m);
		}
		this.rc = new RunCustomers(m.getRc(),hub);
		try {
			readAndWrite= new ReadWriteTracking(m.path+"trackingCopy.txt");
			readAndWrite.copyFile(m.path+"tracking.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ReadWriteTracking getReadAndWrite() {
		return readAndWrite;
	}

	public Hub getHub() {
		return memento.getHub();
	}
	public RunCustomers getRc() {
		return memento.getRc();
	}
	public ArrayList<Package> getPackages() {
		return memento.getPackages();
	}
	public int getClock() {return clock;}
	
	/**
	 * A method for creating a memento - the object that stores the information itself.
	 * @return Memento instance
	 */
	public Memento createMemento() {
		memento = new Memento(this.hub,this.rc,this.packages);
		return memento;
	}


}
