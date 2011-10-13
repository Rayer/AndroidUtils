package com.rayer.util.logger;

import java.util.HashMap;

public class Logger {
	HashMap<Class<? extends ILogData>, ILogUnit<? extends ILogData> > mLogMap;
	
	public Logger() {
		mLogMap = new HashMap<Class<? extends ILogData>, ILogUnit<? extends ILogData> >();
		
	};
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends ILogData> boolean Log(T target){
		ILogUnit logger = getLogUnit(target.getReturnClassType());
		if(logger == null)
			return false;
		
		return logger.log(target);

	}

	
	@SuppressWarnings("unchecked")
	<T extends ILogData> ILogUnit<T> getLogUnit(Class<T> logDataClass) {
		return (ILogUnit<T>) mLogMap.get(logDataClass);
	}
	
	public <T extends ILogData> void registerLogUnit(ILogUnit<T> unit){
		mLogMap.put(unit.getReturnType(), unit);
	}

	public <T extends ILogData> void unregisterLogUnit(ILogUnit<T> unit){
		mLogMap.remove(unit);
	}

}
