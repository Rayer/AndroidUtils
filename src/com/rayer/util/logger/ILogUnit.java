package com.rayer.util.logger;

public interface ILogUnit<T extends ILogData> {
	
	public Class<T> getReturnType();
	public boolean log(T target);
	public byte[] serialize();
	public void deserialize(byte[] byteArray);
	public void clear();
	public String dump();
	public String getUnitName();
}
