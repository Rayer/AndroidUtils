package com.rayer.util.logger;

public class StringLogData implements ILogData {

	@Override
	public Class<? extends ILogData> getReturnClassType() {
		return this.getClass();
	}

}
