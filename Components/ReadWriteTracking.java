package Components;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * ReadWriteTracking - Department for creating a tracking file for packages in the system. 
 *                     Using lock's to prevent Threads bugs while writing/reading to/from file. 
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 */
public class ReadWriteTracking {
	  private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();	     
	    private final Lock readLock = readWriteLock.readLock();	 
	    private final Lock writeLock = readWriteLock.writeLock();
		private String file_name;
		private FileInputStream in ;
		private FileOutputStream out;
		private boolean first = false;
		
		/**
		 * Constructor initial file name + path(include in name)
		 * @param file_name
		 * @throws FileNotFoundException
		 */
		public ReadWriteTracking(String file_name) throws FileNotFoundException {
			this.file_name = file_name;
		}
	
		/**
		 * Method for Writing to file , using lock.
		 * @param s
		 * @throws IOException
		 */
	    public  void write(String s) throws IOException
	    {
	    	synchronized(this.getClass()) {
	    		writeLock.lock();
	    		try
	    		{	
	    			out = new FileOutputStream(file_name,first);
	    			first=true;
	    			byte[] buf = s.getBytes();	        	
	    			out.write(buf, 0, s.length());
	    			out.close();
	    		}
	    		finally
	    		{
	    			writeLock.unlock();
	    		}	    		
	    	}

	    }
	    /**
	     * Method for reading from file , using lock.
	     * The method parameter customer reads a file each time 
	     * and checks if all of his packages are 
	     * in status delivered(true) or not (false).
	     * @param c - customers
	     * @return true/false
	     * @throws IOException
	     */
		public boolean read(Customer c) throws IOException
	    {
	        readLock.lock();
	        try
	        {
	        	in = new  FileInputStream(file_name);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            String line = reader.readLine();
	            while(line != null) {
	            	int intIndex = line.indexOf("DELIVERED");
	            	if(intIndex != -1) {
	            		int packageIDindex = line.indexOf("ID:");
	            		int first = packageIDindex+3 ;
	            		String result = (String) line.subSequence(first, first+4);
	            		int  finalID = Integer.parseInt(result); 
	            		for(int i=0 ; i< c.getMyPackages().size() ; i++) {
	            			Package p = c.getMyPackages().get(i);
	            			if(p.getPackageID() == finalID && !p.isDeliverd()) {
	            				p.setDeliverd(true);
	            				c.setCountPackagesDeliverd(c.getCountPackagesDeliverd()+1);
	            			}
	            		}	            	
	            	}
	            	line = reader.readLine();
	            	if(c.getCountPackagesDeliverd() ==  c.getMyPackages().size()) {
	            		return true;
	            	}	        	
	            }
	            return false;  
	        }
	        finally
	        {
	        	
	        	in.close();
	        	readLock.unlock();
	                    
	        }
	    }
		/**
		 * The method parameter is a path to a new/already existing file.
		 * the method copies this file to the this object file.
		 *  
		 * @param path
		 * @throws IOException
		 */
		public void copyFile(String path) throws IOException{
			readLock.lock();
			try {
				in = new  FileInputStream(path);
	            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	            String line = reader.readLine();
	            line+= "\n";
	            out = new FileOutputStream(file_name);
	            byte[] buf = null;
	            while(line != null) {
	            	buf = line.getBytes();
	            	
	    			out.write(buf, 0, line.length());
		            line = reader.readLine();
		            if(line != null) {
			            line+= "\n";
		            }
	            }

			}
			  finally
		        {		        	
				  	out.close();
		        	in.close();
		        	readLock.unlock();		                    
		        }
		}



}
