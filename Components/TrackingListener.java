package Components;
import java.util.EventListener;
import java.util.EventObject;

public interface TrackingListener extends EventListener {
	public void reportTracking(TrackingEvent evt);
}
