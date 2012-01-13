package com.rayer.util.event;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;


public abstract class EventProcessHandler extends Handler {
	
	//Monitor function
	ArrayList<Class<? extends EventBase> > mMonitoringEventList = new ArrayList<Class<? extends EventBase> >();
	//HashMap<EventManagerInterface, ArrayList<Class<? extends EventBase> > > mMonitoringEventMap = new HashMap<EventManagerInterface, ArrayList<Class<? extends EventBase> > >();
	
	/**
	 * for security use only
	 */
	void addMonitoringEvents(EventManagerInterface em, Class<? extends EventBase> targetClass) {
		//ArrayList<Class<? extends EventBase> > targetEventArray = mMonitoringEventMap.get(em);
		
//		if(targetEventArray == null) {
//			targetEventArray = new ArrayList<Class<? extends EventBase> >();
//			//targetEventArray.add(targetClass);
//			mMonitoringEventMap.put(em, targetEventArray);
//			
//		}
//		if(targetEventArray.contains(targetClass) == false)
//			targetEventArray.add(targetClass);
		
		//Log.d("EventManager", "added monitor event");
		if(mMonitoringEventList.contains(targetClass))
			return;
		
		mMonitoringEventList.add(targetClass);
	}
	
	void removeMonitoringEvents(EventManagerInterface em, Class<? extends EventBase> targetClass) {
//		ArrayList<Class<? extends EventBase> > targetEventArray = mMonitoringEventMap.get(em);
//		if(targetEventArray == null) {
//			//應該是出了甚麼問題....
//			return;
//		}
//		targetEventArray.remove(targetClass);
		mMonitoringEventList.remove(targetClass);
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
		Class<? extends EventBase> targetClass = null;
		for(Class<? extends EventBase> event : mMonitoringEventList)
			if(event.hashCode() == msg.what) {
				targetClass = event;
				break;
			}
		
		if(targetClass != null)
			processEvent(targetClass, msg.arg1, msg.arg2, msg.obj);
		else
			super.handleMessage(msg);
	}
	
	@Override
	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		Set<EventManagerInterface> keySet = mMonitoringEventMap.keySet();
//		for(EventManagerInterface e : keySet) {
//			sb.append("EventManagerInterface #");
//			sb.append(e.getName());
//			ArrayList<Class<? extends EventBase> > targetArray = mMonitoringEventMap.get(e);
//			Iterator<Class<? extends EventBase>> iter = targetArray.iterator();
//			while(iter.hasNext()) {
//				sb.append(iter.next().getName());
//				sb.append(" ");
//			}
//		}
//		
//		return sb.toString();
		return super.toString();
	}
	
	public abstract void processEvent(Class<? extends EventBase> event, int arg1, int arg2, Object obj);
	

}
