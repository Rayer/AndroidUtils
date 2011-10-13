package com.rayer.util.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import android.os.Handler;
import android.os.Message;


public class EventProcessHandler extends Handler {
	
	//Monitor function
	//ArrayList<Class<? extends EventBase> > mMonitoringEventList = new ArrayList<Class<? extends EventBase> >();
	HashMap<EventManagerInterface, ArrayList<Class<? extends EventBase> > > mMonitoringEventMap = new HashMap<EventManagerInterface, ArrayList<Class<? extends EventBase> > >();
	
	/**
	 * for security use only
	 */
	public void addMonitoringEvents(EventManagerInterface em, Class<? extends EventBase> targetClass) {
		ArrayList<Class<? extends EventBase> > targetEventArray = mMonitoringEventMap.get(em);
		
		if(targetEventArray == null) {
			targetEventArray = new ArrayList<Class<? extends EventBase> >();
			//targetEventArray.add(targetClass);
			mMonitoringEventMap.put(em, targetEventArray);
			
		}
		if(targetEventArray.contains(targetClass) == false)
			targetEventArray.add(targetClass);
	}
	
	public void removeMonitoringEvents(EventManagerInterface em, Class<? extends EventBase> targetClass) {
		ArrayList<Class<? extends EventBase> > targetEventArray = mMonitoringEventMap.get(em);
		if(targetEventArray == null) {
			//應該是出了甚麼問題....
			return;
		}
		targetEventArray.remove(targetClass);
	}
	
	public ArrayList<Class<? extends EventBase> > getListeningEvents() {
		
		return null;
	}
	
	public void wipeFromEventManager(EventManagerInterface em) {
		
	}
	
	
	@Override
	public void dispatchMessage(Message msg) {
		super.dispatchMessage(msg);
	}
	
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Set<EventManagerInterface> keySet = mMonitoringEventMap.keySet();
		for(EventManagerInterface e : keySet) {
			sb.append("EventManagerInterface #");
			sb.append(e.getName());
			ArrayList<Class<? extends EventBase> > targetArray = mMonitoringEventMap.get(e);
			Iterator<Class<? extends EventBase>> iter = targetArray.iterator();
			while(iter.hasNext()) {
				sb.append(iter.next().getName());
				sb.append(" ");
			}
		}
		
		return sb.toString();
	}
	

}
