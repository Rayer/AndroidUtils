package com.rayer.util.event;



/**
 * Base of all events. 
 * @author rayer
 *
 */
public abstract class EventBase {

	//static private HashMap<Integer, EventBase > msEventMap = new HashMap<Integer, EventBase >();
	
	public String getEventName() {
		return this.getClass().toString();
	}
	
	public final int getID() {
		return getEventName().hashCode();	
	}
	
	//public abstract Message generateMessage();
	public abstract EventParamBase createParameters();
	
}
