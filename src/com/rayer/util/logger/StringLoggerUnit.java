package com.rayer.util.logger;


/**
 * Test
 * @author POP
 *
 */
public class StringLoggerUnit implements ILogUnit<StringLogData>{

	@Override
	public Class<StringLogData> getReturnType() {
		return StringLogData.class;
	}

	@Override
	public boolean log(StringLogData logSnappet) {
		return false;
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



