package com.rayer.util.provisioner;

import com.rayer.util.db.DatabaseResource;

public class DatabaseResourceProvisioner<T> implements ResourceProvisioner<T> {

	DatabaseResource<T> database;
	String identificator_arg;
	
	/**
	 * 
	 * @param database
	 * @param identificator_arg 查找時的目標
	 */
	public DatabaseResourceProvisioner(DatabaseResource<T> database, String identificator_arg){
		this.database = database;
		this.identificator_arg = identificator_arg;
	}
	

	@Override
	public T getResource(String identificator) {
		return database.getResource(identificator, identificator_arg);
	}

	@Override
	public boolean setResource(String identificator, T targetResource) {
		
		return database.setResource(identificator, targetResource);
	}

	@Override
	public boolean dereferenceResource(String identificator) {
		
		return false;
	}
	
	@Override
	public boolean clearAllCachedResource() {
		return false;
	}

}
