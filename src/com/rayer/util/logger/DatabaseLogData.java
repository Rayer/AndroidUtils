package com.rayer.util.logger;

public class DatabaseLogData implements ILogData {

	public String logString;
	
	public DatabaseLogData() {
		logString = new String();
	};
	
	public DatabaseLogData(String log) {
		logString = log;
	}
	
	@Override
	public Class<? extends ILogData> getReturnClassType() {
		return this.getClass();
	}

}
