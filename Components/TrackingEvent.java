package Components;

import java.util.EventObject;
/**
 * TrackingEvent  -A department is to be sent to main office to update the package tracking file.
 * 
 * @version 1.10 9 May 2021
 * @author  Tal ohana, Tali Tevlin
 * @see     Tracking,Package
 */
public class TrackingEvent extends EventObject {
	private Tracking track;
	private Package p;

	public TrackingEvent(Object source) {
		super(source);
	}
	
	public Package getP() {
		return p;
	}
	
	public void setP(Package p) {
		this.p = p;
	}

	public Tracking getTrack() {
		return track;
	}

	public void setTrack(Tracking track) {
		this.track = track;
	}
	
	
	
}
