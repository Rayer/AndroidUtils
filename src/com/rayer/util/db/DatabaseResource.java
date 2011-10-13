package com.rayer.util.db;


public abstract class DatabaseResource<T> extends DatabaseTable<T> {

	public DatabaseResource(Class<T> ObjectClass, Database database) throws Exception {
		super(ObjectClass, database);
	}

	abstract public T getResource(String identificator, String identificator_arg);
	abstract public boolean setResource(String identificator, T targetResource);
}
