package com.rayer.util.db;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


public class Database extends SQLiteOpenHelper {

	private static String mDatabaseName = "Database";
	private static int mVersion = 1;

	//private int mOnUseingDBCount = 0;
	SQLiteDatabase mDB = null;
	boolean isUpgrade = false;
	
	HashMap <Class<?>, DatabaseTable<?> > mTableMap;
	//-----------------------------------------------------------------------------------
	public Database(Context context) {
		super(context, getDatabaseName(), null, getVersion());
		mTableMap = new HashMap < Class<?>, DatabaseTable<?> >();
		//mDB = getWritableDatabase();
	}
	//-----------------------------------------------------------------------------------
	public Database(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, null, version);
		
		mDatabaseName = name;
		mVersion = version;
		
		mTableMap = new HashMap < Class<?>, DatabaseTable<?> >();
		//mDB = getWritableDatabase();
	}
	//-----------------------------------------------------------------------------------
	@Override
	public void onCreate(SQLiteDatabase db) {
		mDB = db;
		//++mOnUseingDBCount;
	}
	//-----------------------------------------------------------------------------------
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		//clearAllTables();
		mDB = db;

		upgradeAllTables();
		isUpgrade = true;
//		if(db.isOpen())
//			db.close();
	}
	//-----------------------------------------------------------------------------------
	public boolean registerTable(DatabaseTable<?> table) {
		mTableMap.put(table.getObjectClass(), table);
		
		if(isUpgrade)
			table.onUpgrade();
		else
			table.createTable(); 
		
		return true;
	}
	//-----------------------------------------------------------------------------------	
	public void upgradeAllTables(SQLiteDatabase db) {
		Iterator<Entry<Class<?>, DatabaseTable<?>>> iterator = mTableMap.entrySet().iterator();
		
		while(iterator.hasNext()) {
			DatabaseTable<?> table = iterator.next().getValue();
			table.onUpgrade(db);
		}
	}
	public void upgradeAllTables() {
		Iterator<Entry<Class<?>, DatabaseTable<?>>> iterator = mTableMap.entrySet().iterator();
		
		while(iterator.hasNext()) {
			DatabaseTable<?> table = iterator.next().getValue();
			table.onUpgrade();
		}
	}
	//-----------------------------------------------------------------------------------	
//	public void dropTable(DatabaseTable<?> table){
//		String sql = "DROP TABLE IF EXISTS " + table.getTableName() + ";";
//		SQLiteDatabase db = getWritableDatabase();
//		db.execSQL(sql);
//		close();
//		SQLiteDatabase db = getWritableDatabase();
//		dropTable(table, db);
//		close();
//	}
//	public void dropTable(DatabaseTable<?> table, SQLiteDatabase db){
//		String sql = "DROP TABLE IF EXISTS " + table.getTableName() + ";";
//		db.execSQL(sql);
//	}
	public void dropAllTables(SQLiteDatabase db) {
		
		Iterator<Entry<Class<?>, DatabaseTable<?>>> iterator = mTableMap.entrySet().iterator();
		
		while(iterator.hasNext()) {
			DatabaseTable<?> table = iterator.next().getValue();
			dropTable(table, db);
		}
	}	
	public void dropAllTables() {
		
		Iterator<Entry<Class<?>, DatabaseTable<?>>> iterator = mTableMap.entrySet().iterator();
		
		while(iterator.hasNext()) {
			DatabaseTable<?> table = iterator.next().getValue();
			dropTable(table);
		}
	}		
	public void dropTable(DatabaseTable<?> table) {
		table.dropTable();
	}
	public void dropTable(DatabaseTable<?> table, SQLiteDatabase db) {
		table.dropTable(db);
	}
	//-----------------------------------------------------------------------------------
//	public void clearAllTables() {
//		Iterator<Entry<Class<?>, DatabaseTable<?>>> iterator = mTableMap.entrySet().iterator();
//        while(iterator.hasNext()) {
//        	DatabaseTable<?> table = iterator.next().getValue();
//        	dropTable(table);
//        	SQLiteDatabase db = getWritableDatabase();
//        	db.execSQL(table.createTableCMD());
//        	close();
//        }
//		SQLiteDatabase db = getWritableDatabase();
//		clearAllTables(db);
//		close();
//	}
//	public void clearAllTables(SQLiteDatabase db) {
//		Iterator<Entry<Class<?>, DatabaseTable<?>>> iterator = mTableMap.entrySet().iterator();
//        while(iterator.hasNext()) {
//        	DatabaseTable<?> table = iterator.next().getValue();
//        	dropTable(table, db);
//        	db.execSQL(table.createTableCMD());
//        }
//	}
	public void clearAllTables(SQLiteDatabase db) {
		
		Iterator<Entry<Class<?>, DatabaseTable<?>>> iterator = mTableMap.entrySet().iterator();
		
		while(iterator.hasNext()) {
			DatabaseTable<?> table = iterator.next().getValue();
			clearTable(table, db);
		}
	}
	public void clearAllTables() {
		
		Iterator<Entry<Class<?>, DatabaseTable<?>>> iterator = mTableMap.entrySet().iterator();
		
		while(iterator.hasNext()) {
			DatabaseTable<?> table = iterator.next().getValue();
			clearTable(table);
		}
	}		
	public void clearTable(DatabaseTable<?> table) {
		table.clearTable();
	}
	public void clearTable(DatabaseTable<?> table, SQLiteDatabase db) {
		table.clearTable(db);
	}
	//-----------------------------------------------------------------------------------
	public static String getDatabaseName() {
		return mDatabaseName;
	}
	//-----------------------------------------------------------------------------------
	public static int getVersion() {
		return mVersion;
	}
	//-----------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	public <T> DatabaseTable<T> getTable(T target) {
		return (DatabaseTable<T>) mTableMap.get(target.getClass());
	}
	@SuppressWarnings("unchecked")
	public <T> DatabaseTable<T> getTable(Class<T> objClass) {
		return (DatabaseTable<T>) mTableMap.get(objClass);
	}
	//-----------------------------------------------------------------------------------
	public <T> long insert(T target) {
		DatabaseTable<T> table = getTable(target);
		
		if(table == null)
			return -1;
		
		return table.insert(target);
	}
	//-----------------------------------------------------------------------------------
	public <T> List<T> select(String where, String[] whereValue, Class<T> obj) {
		DatabaseTable<T> table = getTable(obj);
		
		if(table == null)
			return null;
		
		return table.select(where, whereValue);
	}
	//-----------------------------------------------------------------------------------
	public <T> List<T> select(String where, String[] whereValue, T target) {
		DatabaseTable<T> table = getTable(target);
		
		if(table == null)
			return null;
		
		return table.select(where, whereValue);
	}
	//-----------------------------------------------------------------------------------
	/**
	 * 會回傳null，須注意
	 */
	public <T> T singleSelect(String where, String[] whereValue, Class<T> obj) {
		DatabaseTable<T> table = getTable(obj);
		
		if(table == null)
			return null;
		
		return table.singleSelect(where, whereValue);
	}
	//-----------------------------------------------------------------------------------
	/**
	 * 會回傳null，須注意
	 */
	public <T> T singleSelect(String where, String[] whereValue, T target) {
		DatabaseTable<T> table = getTable(target);
		
		if(table == null)
			return null;
		
		return table.singleSelect(where, whereValue);
	}
	//-----------------------------------------------------------------------------------
	public <T> long update(String where, String[] whereValue, T target) {
		DatabaseTable<T> table = getTable(target);
		
		if(table == null)
			return -1;
		
		return table.update(where, whereValue, target);
	}
	//-----------------------------------------------------------------------------------
	public <T> long delete(String where, String[] whereValue, T target) {
		DatabaseTable<T> table = getTable(target);
		
		if(table == null)
			return -1;
		
		return table.delete(where, whereValue);
	}
	//-----------------------------------------------------------------------------------
	public void dumpAllTables() {
		Iterator<Entry<Class<?>, DatabaseTable<?>>> iter = mTableMap.entrySet().iterator();
		while (iter.hasNext()) {
			iter.next().getValue().dumpTable();
		} 
	}
	//-----------------------------------------------------------------------------------
//	@Override
//	public synchronized void close() {
//		mDB.close();
//		super.close();
//	}
	//-----------------------------------------------------------------------------------
	@Override
	protected void finalize() throws Throwable {
		super.close();
		super.finalize();
	}
	public void Destory() {
		if(mDB != null && mDB.isOpen())
		{
			mDB.close();
			mDB = null;
		}
		super.close();
	}
	//-----------------------------------------------------------------------------------
	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		if(mDB == null)
			mDB = super.getReadableDatabase();
//		
//		++mOnUseingDBCount;
		
		return mDB;
	}
	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		if(mDB == null)
			mDB = super.getWritableDatabase();
//		
//		++mOnUseingDBCount;
	
		return mDB;
	}
	@Override
	public synchronized void close() {
//		--mOnUseingDBCount;
//		
//		if(mDB != null && mDB.isOpen() && mOnUseingDBCount <= 0){
//			mDB.close();
//			mDB = null;
//			super.close();
//		}

	}
	//-----------------------------------------------------------------------------------
	
}
