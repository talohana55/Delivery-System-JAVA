package GUI;
//Assignment 2
//Members: Tal ohana - 307833186
//       Tali Tevlin - 206999195
import javax.swing.JFrame;
/**
 * DeliverySystem - The program's main GUI class contains a main Frame
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see 	mainFrame,displayPanel
 */
public class DeliverySystem  {

    private final static String FRAME_TEXT = "Post Tracking System";

	public static void main(String[] args) {   
		// Create a frame and container for the panels.
		mainFrame Frame = new mainFrame();		
		// Exit when the window is closed.
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		Frame.setSize(1200,700); 
    }		
}


	  
	  
	 