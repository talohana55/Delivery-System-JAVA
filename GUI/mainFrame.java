package GUI;
//Assignment 2
//Members: Tal ohana - 307833186
//       Tali Tevlin - 206999195
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;
import Components.Branch;
import Components.MainOffice;
import Components.Originator;
import Components.Package;
import Components.RunCustomers;

/**
 * mainFrame - Main JFrame, contains the display windows, 
 * 			   buttons for use and other aids such as information tables.
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	displayPanel
 */
public class mainFrame extends JFrame implements ActionListener,Runnable{	
	private static final long serialVersionUID = 1L;
	public Thread mainFrameThread = new Thread(this);
	public static boolean closeTable = false;
	public static boolean isRunning= true;
	public static boolean showPackages = false;	
	public static boolean stopRepaint = false;
	public static boolean stopSystem = false;
	public int clock ;
	MainOffice mainOffice;
	static int numOfBranch = 0;
	static int numOfTrucks = 0;
	static int numOfPackage = 0;  
	private String[][] packageBranchData;
	private String[][] packageData;
	private static String[] branchesName ;
	private Originator origin;
	private JButton btn;
	displayPanel firstPanel = new displayPanel();
 	private JPanel buttonsLine = new JPanel();
	private static String[] columnNames = {"Package ID","Sender","Destination","Priority","Status"};
	final static String[] BUTTONS = { "Create System", "Start", "Stop","Resume", "Packages info","Branch info","Clone-Branch","Restore","Report" };
    private enum Types{BRANCH,TRUCK,PACKAGE}
    /**
     * Initializes a main display panel and a bottom button panel.
     *
     */
	public mainFrame() {
		firstPanel.setBorder(BorderFactory.createCompoundBorder(
    			BorderFactory.createTitledBorder("Tracking"), 
    			BorderFactory.createEmptyBorder(5,5,5,5)));
	    // get all buttons to own Panel	
		Dimension d = new Dimension(120,30);//buttons size 
		for(int i=0 ; i<BUTTONS.length ; i++) {
			btn = new JButton(BUTTONS[i]);
			btn.setPreferredSize(d);
			btn.addActionListener(this);
			buttonsLine.add(btn);
		}
    	buttonsLine.setBorder(BorderFactory.createCompoundBorder(
    			BorderFactory.createTitledBorder("Menu"), 
    			BorderFactory.createEmptyBorder(5,5,5,5)));

    	this.add(firstPanel);
    	this.add(buttonsLine, BorderLayout.SOUTH);
	    this.setVisible(true);//call to PaintComponent

	}
	
	@SuppressWarnings("removal")
	/**A primary function that generates functionality 
	 * for display buttons, initializes MainOffice, 
	 * and transfers parameters from user to interface.
	 * @param e - event
	 */
	@Override//for Frame
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton)e.getSource();	
		if(b.getText() == "Create System") {
			showDialog();
			mainOffice = MainOffice.getInstance(numOfBranch, numOfTrucks,firstPanel);
		}else if( b.getText() == "Start") {			
			if(mainOffice!= null) {
				showPackages = true; // mark to display the package when start simulation
				mainOffice.playHubThread();
				RunCustomers rc = new RunCustomers(mainOffice.getHub());
				mainOffice.startRCTread(rc);
				mainOffice.setRunCustomers(rc);
				start();	
			}
		}else if(b.getText() == "Packages info") {
			if(mainOffice!= null) {
				closeTable=!closeTable;
				showTableInfo();
			}
			
		}else if(b.getText() == "Stop") {
			mainFrameThread.suspend();			
		}
		else if(b.getText() == "Resume") {
			if(showPackages) {
				mainFrameThread.resume();
			}
		}
		else if(b.getText() == "Branch info") {
			if(mainOffice!= null) {	
				closeTable=!closeTable;
				showBranchInfo();				
			}
		}else if(b.getText() == "Clone-Branch") {
			if(mainOffice!= null) {
				closeTable=!closeTable;
				origin = new Originator();
				origin.setState(mainOffice);
				origin.createMemento();
				selectBranchToClone();
			}
		}else if(b.getText() == "Restore") {
			stopRepaint = true;
			MainOffice.killThemAll();
			this.mainFrameThread.interrupt();
			Branch.id--;
			mainOffice.setPackages(origin.getPackages());
			MainOffice.setClock(origin.getClock());
			mainOffice.setHub(origin.getHub());	
			mainOffice.refreshAndSetCustomer(origin.getHub(),origin.getRc());			
			Package.id=999+(origin.getPackages().size());
			try {
				mainOffice.getReadAndWrite().copyFile(mainOffice.path+"trackingCopy.txt");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			firstPanel.addAllPackages(origin.getPackages());
			firstPanel.addHUB(origin.getHub());
			stopRepaint=false;
			repaint();
			mainFrameThread = new Thread(this);
			mainOffice.StartThemAll();
			start();
			
		}else if(b.getText() == "Report") {
			File file = new File (MainOffice.path+"tracking.txt");
			try {
				Desktop.getDesktop().open(file);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
	}
	  /**
	   * Produces an update table for each branch and the 
	   * status of the packages in it, including all package
	   *  details and available for all branches.
	   *  @see  showTablePerBranch
	   */
	public void showBranchInfo() {
		  if(closeTable) {
			  int branchesSize = mainOffice.getHub().getBranches().size();
			  branchesName = new String[branchesSize+1];
			  branchesName[0] = "Sorting center";		  
			  for(int i=0 ; i<branchesSize ;i++) {
				  String name = "Branch "+(i+1)+"";
				  branchesName[i+1] = name;
			  }
			  JComboBox branchCombo = new JComboBox(branchesName);
			  branchCombo.setEditable(true);
			  branchCombo.setFont(new Font("Sans Serif", Font.BOLD, 16 ));
			  branchCombo.setBackground(Color.white);
			  Object[] options = new Object[] {};		  
			  JOptionPane jop = new JOptionPane("Choose Branch",
					  JOptionPane.QUESTION_MESSAGE,
					  JOptionPane.DEFAULT_OPTION,
					  null,options, null);
			  jop.add(branchCombo);		  	  
			  JDialog jdialog = new JDialog();
			  jdialog.setSize(new Dimension(400, 200));
			  jdialog.getContentPane().add(jop);
			  jdialog.setVisible(true);
			  
			  branchCombo.addActionListener (new ActionListener() {
				  /**
				   * Produces for each branch displayed by JComboBox its own functionality,
				   *  obtains the branch details and its packages at the request of a user.
				   */
				  @Override
				  public void actionPerformed(ActionEvent e) {
					  JComboBox branchCombo = (JComboBox)e.getSource();				  
					  String  s = (String)branchCombo.getSelectedItem();
					  int packageSize;
					  if(s == "Sorting center") {
						  showTablePerBranch(s, mainOffice.getHub().getListPackages().size(),0);
					  }else {
						  int num = Character.getNumericValue(s.charAt(7));
						  for(int i=0 ; i<mainOffice.getHub().getBranches().size() ; i++) {
							  Branch b = mainOffice.getHub().getBranches().get(i);
							  if(num-1 == i) {  
								  packageSize =  mainOffice.getHub().getBranches().get(i).getListPackages().size();
								  showTablePerBranch(s, packageSize,i);
							  }
						  }	
						  
					  }
				  }
			  });		  
			  
		  }
	}
	/**
	 * Exports a dialog window for entering system data,
	 *  number of branches, trucks and packages, saves the
	 *   data and passes it on to the boot interface.
	 */
	public void showDialog() {		
		JOptionPane optionPane = new  JOptionPane();//Dialog
		JSlider slider1 = getSlider(optionPane,1,10,1,Types.BRANCH);
		JSlider slider2 = getSlider(optionPane,1,10,1,Types.TRUCK);
	    optionPane.setMessage(new Object[] { "Number of branches ", slider1 ,"Number of truck per branch ", slider2});
	    optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
	    optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
	    JDialog dialog = optionPane.createDialog(this, "Create post system");
	    dialog.setSize(new Dimension(600, 400));
	    dialog.setVisible(true);//display dialog to screen	    
	    // set values from dialog
	    mainFrame.numOfBranch = slider1.getValue();
	    mainFrame.numOfTrucks = slider2.getValue();
	    this.repaint();//calls to paintComponent - display results	    
	    dialog.dispose();//cancel dialog
	}
	  public JSlider getSlider(final JOptionPane optionPane,int start,int end,int step,Types t) {
		 // start();
		    JSlider slider = new JSlider(start,end,step);
		    slider.setMajorTickSpacing(step);
		    slider.setPaintTicks(true);
		    slider.setPaintLabels(true);
		    ChangeListener changeListener = new ChangeListener() {
		      public void stateChanged(ChangeEvent changeEvent) {
		        JSlider theSlider = (JSlider) changeEvent.getSource();
		        if (!theSlider.getValueIsAdjusting()) {
		          optionPane.setInputValue((int)(theSlider.getValue()));
		        }
		      }
		    };
		    slider.addChangeListener(changeListener);
		    return slider;
		  }
	  /**
	   * Produces a data table for all packages in 
	   * the system in real time,
	   *  including all package details
	   */
	  public void showTableInfo() {
		  if (closeTable) {
			  int colSize = columnNames.length;
			  int packagesSum = mainOffice.getPackages().size();
			  packageData = new String[packagesSum][colSize];
			  for(int i=0 ;i<packagesSum;i++) {
				  Package p = mainOffice.getPackages().get(i);
				  for(int j=0; j<colSize ; j++) {
					  if(j==0) {
						  packageData[i][j]= p.getPackageID()+"";						
					  }else if(j==1) {
						  packageData[i][j]= p.getSenderAddress().getZip()+"-"+p.getSenderAddress().getStreet()+"";
					  }
					  else if(j==2) {
						  packageData[i][j]=  p.getDestinationAddress().getZip()+"-"+p.getDestinationAddress().getStreet()+"";
					  }
					  else if(j==3) {
						  packageData[i][j]= p.getPriority()+"";
					  }
					  else if(j==4) {
						  packageData[i][j]= p.getStatus()+"";
					  }
					  
				  }
			  }		
			  JDialog jdialog = new JDialog();
			  JTable table = new JTable(packageData, columnNames);
			  Font font = new Font("Verdana", Font.PLAIN, 12);
			  table.setFont(font);
			  JTableHeader header = table.getTableHeader();
			  header.setBackground(Color.black);
			  header.setForeground(Color.white);
			  header.setFont(font);
			  table.setRowHeight(25);
			  jdialog.setSize(new Dimension(600, 450));
			  jdialog.add(new JScrollPane(table));
			  jdialog.setVisible(true);
		  }
	  }

	  /**
	   * Display branch options to clone by JComboBox and ActionListener.
	   */
	  public void selectBranchToClone() {
	  if(closeTable) {
		  int branchesSize = mainOffice.getHub().getBranches().size();
		  String[] branchesName = new String[branchesSize];
		 // branchesName = new String[branchesSize+1];
		  for(int i=0 ; i<branchesSize ;i++) {
			  String name = "Branch "+(i+1)+"";
			  branchesName[i] = name;
		  }
		  JComboBox branchCombo = new JComboBox(branchesName);
		  branchCombo.setEditable(true);
		  branchCombo.setFont(new Font("Sans Serif", Font.BOLD, 16 ));
		  branchCombo.setBackground(Color.white);
		  Object[] options = new Object[] {};		  
		  JOptionPane jop = new JOptionPane("Choose Branch To Clone",
				  JOptionPane.QUESTION_MESSAGE,
				  JOptionPane.DEFAULT_OPTION,
				  null,options, null);
		  jop.add(branchCombo);		  	  
		  JDialog jdialog = new JDialog();
		  jdialog.setSize(new Dimension(400, 200));
		  jdialog.getContentPane().add(jop);
		  jdialog.setVisible(true);
		  
		  branchCombo.addActionListener (new ActionListener() {
			  /**
			   * Produces for each branch displayed by JComboBox its own functionality,
			   *  obtains the branch details and its packages at the request of a user.
			   */
			  @Override
			  public void actionPerformed(ActionEvent e) {
				  JComboBox branchCombo = (JComboBox)e.getSource();				  
				  String  s = (String)branchCombo.getSelectedItem();	//Branch 1
				  int BranchNum = (int)s.charAt(7)-'0'; 				  
				  //function to clone 
				  mainOffice.makeBranch( mainOffice.getHub().getBranches().get(BranchNum-1));
				  
			  }
		  });		  
		  
	  }
		 
		
	  }
	  /**
	   * Produces an update table for each branch by
	   *  given branch name, amount of packages and id
	   *  and show all its packages info
	   *  
	   *  @param branchName,packagesSum,id
	   */
	  public void showTablePerBranch(Object  branchName,int packagesSum,int id) {
		  if(packagesSum > 0) {
			  
			  int colSize = columnNames.length;
			  packageBranchData = new String[packagesSum][colSize];
			  for(int i=0 ;i<packagesSum;i++) {// all the packages in this Branch
				  Package p ;
				  if(branchName == "Sorting center") {
					   p = mainOffice.getHub().getListPackages().get(i);
				  }else {
					   p = mainOffice.getHub().getBranches().get(id).getListPackages().get(i);					  
				  }				  
				  for(int j=0; j<colSize ; j++) {
					  if(j==0) {
						  packageBranchData[i][j]= p.getPackageID()+"";						
					  }else if(j==1) {
						  packageBranchData[i][j]= p.getSenderAddress().getZip()+"-"+p.getSenderAddress().getStreet()+"";
					  }
					  else if(j==2) {
						  packageBranchData[i][j]= p.getDestinationAddress().getZip()+"-"+p.getDestinationAddress().getStreet()+"";
					  }
					  else if(j==3) {
						  packageBranchData[i][j]= p.getPriority()+"";
					  }
					  else if(j==4) {
						  packageBranchData[i][j]= p.getStatus()+"";
					  }
					  
				  }
				  
			  }			 
			  JDialog jdialog1 = new JDialog();
			  JTable table2 = new JTable(packageBranchData, columnNames);
			  Font font2 = new Font("Verdana", Font.PLAIN, 12);
			  table2.setFont(font2);
			  JTableHeader header2 = table2.getTableHeader();
			  header2.setBackground(Color.black);
			  header2.setForeground(Color.white);
			  header2.setFont(font2);
			  table2.setRowHeight(25);
			  jdialog1.setSize(new Dimension(500, 300));
			  jdialog1.add(new JScrollPane(table2));
			  jdialog1.setVisible(true);			  
		  }else {
			  JOptionPane p = new JOptionPane();
			  JOptionPane.showMessageDialog(null, branchName+ " is Empty!");
			  JDialog jdialog1 = new JDialog();
			  jdialog1.setSize(new Dimension(500, 300));
			  jdialog1.add(new JScrollPane(p));
		  }
		  
	  }
	  
	  /**
	   * Function for creating a main process GUI belonging to the mainframe
			And to a change in the Boolean variable responsible 
			for the mainframe run operation
	   */
	  public void start() {
		    isRunning = true;
		    mainFrameThread.start();
		}		
	  /**
	    * 	Activates the operation of the system, inserts a new package every 10 seconds into the system in order 
	    * 	to create an order as long as the number of the package entered by the user into the system is met.
		*	Promoting a clock beat by 1. 
		*	repaint is enabled on each reading of this method, in order to call the paintComponent function
		*	that updates the visual content on the display screen.
		*@see 	 paintComponent
	   */
		@Override
		public void run() {
			while (isRunning) {
				
				MainOffice.setClock(MainOffice.getClock()+1);// promote clock
				repaint();	
				if(mainFrame.stopSystem) {
					MainOffice.killThemAll();
					this.mainFrameThread.interrupt();
				}
		        try {
		            Thread.sleep(500);
		        } catch (InterruptedException e) {
					return;
		        }
		    }
		}
	}
	  
