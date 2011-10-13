package com.rayer.util.db;


public interface IDatabaseData {
	
	public long insert();
	public long update();
	public long delete();
	public boolean refresh();
	
}
