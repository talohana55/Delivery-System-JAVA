package GUI;
//Assignment 2
//Members: Tal ohana - 307833186
//       Tali Tevlin - 206999195
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import Components.Branch;
import Components.Hub;
import Components.NonStandardTruck;
import Components.Vertice;
import Components.Package;
import Components.StandardTruck;
import Components.Status;
import Components.Truck;
import Components.Van;
import Components.VerticeType;
import java.awt.Point;
/**
 * displayPanel - JPanel extension represents the visual changes projected
 *  			  in the display window, graphics construction methods.
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin	
 */
public class displayPanel extends JPanel { 
	private static final long serialVersionUID = 1L;
	private static ArrayList <Package> myPackages = new ArrayList <Package>();
	private static Hub myHub ;
	private ArrayList<Point> hubPoints = new ArrayList<Point>();
	private ArrayList<Point> branchPoints = new ArrayList<Point>();
	private ArrayList<Point> senderPoints = new ArrayList<Point>();
	private ArrayList<Point> destinationPoints = new ArrayList<Point>();
	private ArrayList<Truck> myTrucks = new ArrayList<Truck>();
	private Point hubPoint;
	JOptionPane msg = new JOptionPane();
	/**
	 * displayPanel constructor - create only white background to 
	 * the main display panel 
	 */
	public displayPanel() {
		this.setBackground(Color.white);				
		// Add border around the display panel.
		setBorder(BorderFactory.createLoweredBevelBorder());
		setBorder(BorderFactory.createCompoundBorder(
			    BorderFactory.createEmptyBorder(5,5,5,5),
			    getBorder()));	

  }
	
	/**
	 * paintComponent - main and critical function which 
	 * 					draw our own graphics and rendering 
	 * 					on the display screen
	 * @parm g -A Graphics object is the argument to this method
	 */
	@Override//for JPanel
	public void paintComponent(Graphics g) {//Should be await to create system dialog insert
		super.paintComponent(g);
		branchPoints.clear();
		hubPoints.clear();
		senderPoints.clear();
		destinationPoints.clear();
		int branchStartPoint;		
		int Ybranch = 200;
		boolean hubSize=false;
		int branchSpaces = 60;
		if(mainFrame.numOfBranch > 0 && mainFrame.stopRepaint==false ) {
			if(mainFrame.numOfBranch > 8) {
				branchStartPoint = 55;
				branchSpaces = 50;
				hubSize=true;
			}
			else {
				branchStartPoint = 100;
			}
			///Handle Branches
			for(int i=0 ; i< myHub.getBranches().size() ;i++) {// create branch box
				int j=0;
				Graphics2D branch = (Graphics2D) g;
				Graphics2D line = (Graphics2D) g;
				branch.draw(new Rectangle(30, branchStartPoint, 40, 30));//set location to branch
				if(myHub.getBranches().get(i).getListPackages().size()>0) {	
					if(hasPackageInBranch(myHub.getBranches().get(i))) {
						branch.setColor(Color.blue.darker());
						branch.fillRect(30, branchStartPoint, 40, 30);						
					}else {
						branch.setColor(Color.cyan);
						branch.fillRect(30, branchStartPoint, 40, 30);	
					}
						line.setColor(Color.green);
						line.drawLine(60, branchStartPoint+10, 1130, Ybranch+10);//Line - from branch to HUB
						hubPoints.add(new Point(1130, Ybranch+10));
						branchPoints.add(new Point(50, branchStartPoint));						
						branchStartPoint += branchSpaces;// spaces between branches
						Ybranch+=25;//spaces between lines connected to HUB
						
				}
				else { //if there is no package in branch - different branch color
						branch.setColor(Color.cyan);
						branch.fillRect(30, branchStartPoint, 40, 30);
						line.setColor(Color.green);
						line.drawLine(60, branchStartPoint+10, 1130, Ybranch+10);//Line - from branch to HUB
						hubPoints.add(new Point(1130, Ybranch+10));
						branchPoints.add(new Point(50, branchStartPoint));				
						branchStartPoint +=branchSpaces;	
						Ybranch+=25;					
					}					
				
			}		
			///Handle HUB
			g.setColor( new Color(51,102,51));
			if(hubSize) {
				((Graphics2D)g).draw(new Rectangle(1130, 200, 40, 250));
				g.fillRect(1130, 200,40, 250);				
			}
			else {
				((Graphics2D)g).draw(new Rectangle(1130, 200, 40, 200));
				g.fillRect(1130, 200,40, 200);

			}
			g.setColor(Color.black);
			g.drawString("HUB",1127, 195);
			hubPoint = new Point (1130,200);			
			if(mainFrame.showPackages) {
				int packageOnX=150;
				int spacePackage=100;
				int packageOnY = 575;
				if(mainFrame.numOfPackage > 10) {
					packageOnX=100;
					spacePackage =50;
				}
			Point sender,destination;
			///Handle packages
				for(int i=0 ; i< myPackages.size() ; i++) {
					try {
						Package p = myPackages.get(i);						
						sender = new Point(packageOnX,30);
						senderPoints.add(new Point(packageOnX,30));
						destination = new Point(packageOnX,packageOnY);		
						destinationPoints.add(new Point(packageOnX,packageOnY));						
						Graphics2D senderG = (Graphics2D) g;
						Graphics2D destinationG = (Graphics2D) g;						
						if(p.getStatus() == Status.CREATION || p.getStatus() == Status.COLLECTION) {//darker in TOP
							senderG.setColor(Color.red.darker());
							drawCenteredCircle(senderG ,packageOnX,30,20);
							destinationG.setColor(new Color(255,204,204));
							drawCenteredCircle(destinationG ,packageOnX,packageOnY,20);
						}
						else if(p.getStatus() == Status.DELIVERED) {//darker in bottom
							senderG.setColor(new Color(255,204,204));							
							drawCenteredCircle(senderG ,packageOnX,30,20);
							destinationG.setColor(Color.red.darker());	
							drawCenteredCircle(destinationG ,packageOnX,packageOnY,20);
						}
						else {
							g.setColor(new Color(255,204,204));
							drawCenteredCircle(senderG ,packageOnX,30,20);
							drawCenteredCircle(destinationG ,packageOnX,packageOnY,20);
						}
						packageOnX+= spacePackage;						
						drawLines((Graphics2D) g,sender,destination,p);
					}catch (ArrayIndexOutOfBoundsException e) {
						System.out.println(e);
					}
					
				}
				checkAllDeliverd();				
				for(int i=0 ; i<myHub.getBranches().size() ;i++) {/// Handle all vans
					Branch branch = myHub.getBranches().get(i);
					for(int j=0 ; j< branch.getListTrucks().size();j++) {
						Truck t = branch.getListTrucks().get(j);
						if(t.getAvailable()) {
							continue;
						}
						else {
							Vertice source = ((Van) t).getSource();
							Vertice dest = ((Van) t).getDestination();
							if(source!= null && dest!=null) {
								if(source.getType() == VerticeType.Branch && dest.getType() == VerticeType.SourchPackage) {
									int packageId = dest.getID()-1000;
									int branchId = source.getID();
									Point packagePoint = senderPoints.get(packageId);
									Point branchPoint = branchPoints.get(branchId);
									truckOnLines((Graphics2D)g,branchPoint,packagePoint,t);
								}else if(source.getType() == VerticeType.Branch && dest.getType() == VerticeType.DestinationPackage) {
									int packageId = dest.getID()-1000;
									int branchId = source.getID();
									Point packagePoint = destinationPoints.get(packageId);
									Point branchPoint = branchPoints.get(branchId);
									truckOnLines((Graphics2D)g,branchPoint,packagePoint,t);
								}								
							}
						}
						
					}
				}
				
				for(int i=0 ; i<myTrucks.size() ;i++) {/// Handle all Standard/NonStandard Trucks
					Truck t =myTrucks.get(i);
					if(t.getAvailable()) {
						continue;
					}
					if(t instanceof NonStandardTruck) {						
						Vertice source = ((NonStandardTruck) t).getSource();
						Vertice dest = ((NonStandardTruck) t).getDestination();							
						if(source.getType() == VerticeType.HUB && dest.getType() == VerticeType.SourchPackage) {
							int id = dest.getID()-1000;
							Point senderPackagePoint = senderPoints.get(id);
							truckOnLines((Graphics2D)g,hubPoint,senderPackagePoint,t);
						}
						else if(source.getType()==VerticeType.SourchPackage && dest.getType()==VerticeType.DestinationPackage ) {
							int sourceID = source.getID()-1000;
							Point  senderP = senderPoints.get(sourceID);
							Point destP = destinationPoints.get(sourceID);							
							truckOnLines((Graphics2D)g,senderP,destP,t);
						}
						
					}else if( t instanceof StandardTruck) {						
						Branch source = ((StandardTruck) t).getSource();
 						if(source.getClass().getName()=="Components.Hub") {
 							int destBranchID = ((StandardTruck)t).getDestination().getBranchId();
							Point destPoint =  branchPoints.get(destBranchID);//branch point array
							Point hub = hubPoints.get(destBranchID);
							truckOnLines((Graphics2D)g,hub,destPoint,t);//hub to branch&& branch to hub					
							
						}else if(source.getClass().getName()!="Components.Hub" ) {//from branch to HUB
							int sourceBranchID = ((StandardTruck) t).getSource().getBranchId();							
								Point sourcePoint =  branchPoints.get(sourceBranchID);
								Point hub = hubPoints.get(sourceBranchID);
								truckOnLines((Graphics2D)g,sourcePoint,hub,t);//hub to branch&& branch to h								
							
						}
					}

				}
			
			}
		}
		
	}
	
	/**
	 * Produces a Graphics2D object-type circle
	 * @param g	-Graphics2D object
	 * @param x -Position on the x-axis
	 * @param y -Position on the y-axis
	 * @param r -radius
	 */
	public void drawCenteredCircle(Graphics2D g, int x, int y, int r) {
		  x = x-(r/2);
		  y = y-(r/2);
		  g.fillOval(x,y,r,r);
	}
	/**
	 * A producing function Graphics2D object as 
	 * a line between 2 objects on the display screen.
	 * Principle of work by origin and destination of package only.
	 * @param g - Graphics2D object
	 * @param sender - Sender package is represented as a point
	 * @param destination- destination package is represented as a point
	 * @param p - Package
	 * 
	 * @see drawLine of Graphics2D class
	 */
	public void drawLines(Graphics2D g,Point sender ,Point destination,Package p) {
		int x1= (int)sender.getX() ;
		int x2 = (int)destination.getX();
		int y1 =  (int)sender.getY() ;
		int y2= (int)destination.getY();
		int senderZip = p.getSenderAddress().getZip();
		int destZip = p.getDestinationAddress().getZip();
		Point branchSender = branchPoints.get(senderZip);
		Point branchDestination = branchPoints.get(destZip);
		if( p.getClass().getName() == "Components.NonStandardPackage") {
			g.setColor(Color.red.darker());
			g.drawLine(x1,y1,x2,y2);
			g.drawLine(x1,y1,hubPoint.x,hubPoint.y);
		}else {
			g.setColor(Color.blue.darker());
			g.drawLine(x1,y1,branchSender.x,branchSender.y);
			g.drawLine(x2,y2,branchDestination.x,branchDestination.y+30);
		}
		
	}
	/**
	 * A producing function Graphics2D object as Truck,
	 * fits any truck shape and color And call function
	 *  corresponding to truck type for road creation.
	 * @param g -Graphics2D object
	 * @param p1 -The origin of a vehicle is represented by a point
	 * @param p2 -A destination point of a vehicle is represented by a point
	 * @param t - Truck
	 * 
	 * @see nonStandardTruckRoads,standardTruckRoads,vanRoads
	 */
	public void truckOnLines(Graphics2D g, Point p1 ,Point p2,Truck t) {
		if( t.getClass().getName() == "Components.NonStandardTruck") {
			if(t.getPackagesSize()>0 && t.getPackage(0).getStatus()==Status.DISTRIBUTION) {
				g.setColor(Color.red.darker());
			}else {
				g.setColor(Color.red.brighter());				
			}
			nonStandardTruckRoads(g,p1,p2,t);
		}else if( t.getClass().getName() == "Components.StandardTruck" ) {			
			if(t.getPackagesSize()>0) {
				g.setColor(Color.green.darker());
			}else {
				g.setColor(Color.green.brighter());				
			}
			standardTruckRoads(g,p1,p2,t);
			
		}else {
			g.setColor(Color.blue.darker());
			vanRoads(g,p1,p2,t);
		}
		
	}
	/**
	 * Produces a route for a van's journey by point of departure and destination
	 * The calculation is made by the time of arrival at the destination taking into
	 *  account the point where it is located
	 *  
	 * @param g - Graphics2D object
	 * @param p1 -The origin of a Van is represented by a point
	 * @param p2 -A destination point of a Van is represented by a point
	 * @param t - Van
	 * 
	 * @see draw of class Graphics2D
	 */
	public void vanRoads(Graphics2D g, Point p1 ,Point p2,Truck t) {
		int x,y;
		//p1 is branch
		double precent =t.getPrecentTime();
		Point vanPoint;		
		if(p2.x>p1.x) {//always working
			 if(p1.y>p2.y) {//Address in TOP
				 x =(int) (p2.x-(p2.x-p1.x)*(precent));
				 y = (int) (p2.y+(p1.y-p2.y)*(precent));
			 }
			 else {//branch in TOP
				 x =(int) (p1.x+(p2.x-p1.x)*(1-precent));
				 y = (int) (p1.y+(p2.y-p1.y)*(1-precent));
			 }
			 vanPoint = new Point(x,y);
			 g.fillRect(vanPoint.x-8, vanPoint.y-8, 16, 16);
			g.draw(new Rectangle(vanPoint.x-8, vanPoint.y-8, 16, 16));
			g.setColor(Color.black);
			drawCenteredCircle(g,vanPoint.x+8, vanPoint.y+8,10);//top right
			drawCenteredCircle(g,vanPoint.x-8, vanPoint.y-8,10);//left bottom
			drawCenteredCircle(g,vanPoint.x+8, vanPoint.y-8,10);//right bottom
			drawCenteredCircle(g,vanPoint.x-8, vanPoint.y+8,10);//top left
		}
		
	}
	/**
	 * Produces a route for a standardTruck's journey by point of departure and destination
	 * The calculation is made by the time of arrival at the destination taking into
	 *  account the point where it is located
	 *  
	 * @param g - Graphics2D object
	 * @param p1 -The origin of a standardTruck is represented by a point
	 * @param p2 -A destination point of a standardTruck is represented by a point
	 * @param t - standardTruck
	 * 
	 * @see draw of class Graphics2D
	 */
	public void standardTruckRoads(Graphics2D g, Point p1 ,Point p2,Truck t) {		
		int x,y;
		double precent =t.getPrecentTime();
		Point truckPoint;
		boolean isDeliver= false;
		if(t.getPackagesSize()>0) {
			isDeliver = true;
		}
		if(p1.x > p2.x) {//HUB to BRANCH
			 x =(int) ((p1.x-p2.x)*(precent)+p2.x);
			 if(p1.y>p2.y) {
				 y = (int) (((p1.y-p2.y)*(precent))+p2.y);
			 }
			 else {
				 y = (int) (p2.y-(p2.y-p1.y)*(precent));
			 }
			 truckPoint = new Point(x,y);
			 g.fillRect(truckPoint.x-8, truckPoint.y-8, 16, 16);
				g.draw(new Rectangle(truckPoint.x-8, truckPoint.y-8, 16, 16));
				g.setColor(Color.black);
				if(isDeliver) {
					g.drawString(t.getPackagesSize()+"",truckPoint.x-2, truckPoint.y-8);// num of packages on truck
				}
				drawCenteredCircle(g,truckPoint.x+8, truckPoint.y+8,10);//top right
				drawCenteredCircle(g,truckPoint.x-8, truckPoint.y-8,10);//left bottom
				drawCenteredCircle(g,truckPoint.x+8, truckPoint.y-8,10);//right bottom
				drawCenteredCircle(g,truckPoint.x-8, truckPoint.y+8,10);//top left
		}
		else {//Branch to HUB
			 x =(int) (p2.x-(p2.x-p1.x)*(t.getPrecentTime()));
			 if(p1.y>p2.y) {//branch in TOP
				 y = (int) (p2.y+(p1.y-p2.y)*(t.getPrecentTime()));
			 }
			 else {//Hub in TOP
				 y = (int) (p2.y-(p2.y-p1.y)*(t.getPrecentTime()));
			 }
			 truckPoint = new Point(x,y);
			 g.fillRect(truckPoint.x-8, truckPoint.y-8, 16, 16);
				g.draw(new Rectangle(truckPoint.x-8, truckPoint.y-8, 16, 16));
				g.setColor(Color.black);			
				if(isDeliver) {
					g.drawString(t.getPackagesSize()+"",truckPoint.x-2, truckPoint.y-8);// num of packages on truck
				}
				drawCenteredCircle(g,truckPoint.x+8, truckPoint.y+8,10);//top right
				drawCenteredCircle(g,truckPoint.x-8, truckPoint.y-8,10);//left bottom
				drawCenteredCircle(g,truckPoint.x+8, truckPoint.y-8,10);//right bottom
				drawCenteredCircle(g,truckPoint.x-8, truckPoint.y+8,10);//top left
		}
	}
	/**
	 * Produces a route for a nonStandardTruck's journey by point of departure and destination
	 * The calculation is made by the time of arrival at the destination taking into
	 *  account the point where it is located
	 *  
	 * @param g - Graphics2D object
	 * @param p1 -The origin of a nonStandardTruck is represented by a point
	 * @param p2 -A destination point of a nonStandardTruck is represented by a point
	 * @param t - nonStandardTruck
	 * 
	 * @see draw of class Graphics2D
	 */
	public void nonStandardTruckRoads(Graphics2D g, Point p1 ,Point p2,Truck t) {
		int x,y;
		double precent =t.getPrecentTime();
		Point truckPoint;
		//p1 - hub
		if(p1.x>p2.x) {//from HUB to senderPackage
			 x =(int) ((p1.x-p2.x)*(precent)+p2.x);
			 if(p1.y>p2.y) {
				 y = (int) (((p1.y-p2.y)*(precent))+p2.y);
			 }
			 else {
				 y = (int) (p2.y-(p2.y-p1.y)*(precent));
			 }
			 truckPoint = new Point(x,y);
			 g.fillRect(truckPoint.x-8, truckPoint.y-8, 16, 16);
				g.draw(new Rectangle(truckPoint.x-8, truckPoint.y-8, 16, 16));
				g.setColor(Color.black);
				drawCenteredCircle(g,truckPoint.x+8, truckPoint.y+8,10);//top right
				drawCenteredCircle(g,truckPoint.x-8, truckPoint.y-8,10);//left bottom
				drawCenteredCircle(g,truckPoint.x+8, truckPoint.y-8,10);//right bottom
				drawCenteredCircle(g,truckPoint.x-8, truckPoint.y+8,10);//top left
		}else if(p1.x == p2.x) {//from senderPackage to destinationPackage
			 y = (int) (((p1.y-p2.y)*(precent))+p2.y);
			 truckPoint = new Point(p1.x,y);
			 g.fillRect(truckPoint.x-8, truckPoint.y-8, 16, 16);
				g.draw(new Rectangle(truckPoint.x-8, truckPoint.y-8, 16, 16));
				g.setColor(Color.black);
				drawCenteredCircle(g,truckPoint.x+8, truckPoint.y+8,10);//top right
				drawCenteredCircle(g,truckPoint.x-8, truckPoint.y-8,10);//left bottom
				drawCenteredCircle(g,truckPoint.x+8, truckPoint.y-8,10);//right bottom
				drawCenteredCircle(g,truckPoint.x-8, truckPoint.y+8,10);//top left
		}
	}
	/**
	 * Receive the packages to the local department(variable) from the MainOffice 
	 * department to receive the current amount of packages in the 
	 * system and display them on the display screen
	 * 
	 * @param packages - array of packages from MainOffice
	 * 
	 * @see MainOffice() - constructor
	 */
	public void addAllPackages(ArrayList<Package> packages) {
		myPackages = packages;
	}
	/**
	 * Receive HUB object to the local department(variable) from the MainOffice 
	 * Through it you can reach all the variables in the system.
	 * @param hub
	 * 
	 * @see MainOffice() - constructor
	 */
	public void addHUB(Hub hub) {
		myHub = hub;
		myTrucks = hub.getListTrucks();
	}
	/**
	 * A method that allows testing for packages that are stored in the branch
	 *  so that we can update the status of each branch in its color change
	 * @param b - given Branch
	 * @return - boolean statement
	 */
	public boolean hasPackageInBranch(Branch b) {
		for(int i=0 ; i< b.getListPackages().size();i++) {
			if(b.getListPackages().get(i).getStatus()==Status.BRANCH_STORAGE) {
				return true;
			}
		}
		return false;
	}
	/**
	 * A function that checks if all the packages in the system have reached 
	 * the destination and been marked as "delivered" in order to give a signal
	 *  to the processes to stop throughout the system and thus stop the entire operation
	 *  
	 */
	public void checkAllDeliverd() {
		int count =0;
		if(myPackages.size()>0) {
			for(int i=0 ; i<myPackages.size();i++) {
				Package p = myPackages.get(i);
				if(p.getStatus()==Status.DELIVERED) {
					count++;
				}
			}if(count== myPackages.size()) {
				mainFrame.isRunning = false;	
				mainFrame.stopSystem = true;
			}			
		}
	}

}


