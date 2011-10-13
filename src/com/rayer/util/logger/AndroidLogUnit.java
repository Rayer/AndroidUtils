package com.rayer.util.logger;

import android.util.Log;

public class AndroidLogUnit implements ILogUnit<AndroidLogData> {

	@Override
	public Class<AndroidLogData> getReturnType() {
		return AndroidLogData.class;
	}

	@Override
	public boolean log(AndroidLogData logSnappet) {
		Log.d(logSnappet.getTag(), logSnappet.getMessage());
		return true;
	}

	@Override
	public byte[] serialize() {
		return null;
	}

	@Override
	public void deserialize(byte[] byteArray) {
		
	}

	@Override
	public void clear() {
		
	}

	@Override
	public String dump() {
		return null;
	}

	@Override
	public String getUnitName() {
		return null;
	}

}
