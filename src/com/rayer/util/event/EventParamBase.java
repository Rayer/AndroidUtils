package com.rayer.util.event;

import android.os.Message;

public abstract class EventParamBase {
	
	Class<? extends EventBase> mEventClass;
	
	public EventParamBase(Class<? extends EventBase> event) {
		mEventClass = event;
	}
	
	public int getWhat() {
		return mEventClass.hashCode();
	}
	
	public abstract int getArg1();
	public abstract int getArg2();
	public abstract Object getObj();

	public void generateMessage(Message msg) {
		msg.arg1 = getArg1();
		msg.arg2 = getArg2();
		msg.obj = getObj();
	}
	

}
