package com.rayer.util.logger;

public class AndroidLogData implements ILogData {
	
	public AndroidLogData() {
		
	}

	@Override
	public Class<? extends ILogData> getReturnClassType() {
		return this.getClass();
	}

	public String getTag() {
		return "hamibook2";
	}

	public String getMessage() {
		return "this is!!!!";
	}

}
