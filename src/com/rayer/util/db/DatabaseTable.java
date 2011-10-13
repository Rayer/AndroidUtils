package com.rayer.util.db;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Browser.SearchColumns;

public abstract class DatabaseTable<T> {
		
	private final Class<T> mObjectClass;
	private final String mKeyName = SearchColumns._ID;
	private final String mNullString = "obj is null";
	
	private Database mDatabase = null;
	//-----------------------------------------------------------------------------------
	public DatabaseTable(Class<T> ObjectClass, Database database) throws Exception{
		if(ObjectClass == null)
			throw new Exception(mNullString);
		mDatabase = database;
		mObjectClass = ObjectClass;
	}
	//-----------------------------------------------------------------------------------
	/**
	 * Create table sql cmd 
	 * @return
	 */
	public String createTableCMD() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(getTableName());
		sb.append(" (");

		sb.append(getKeyName());
		sb.append(" INTEGER PRIMARY KEY");
		if (isKeyAutocrement())
			sb.append(" AUTOINCREMENT");

		Field[] fields = getObjectFields();
		for (Field f : fields) {
			String fieldName = f.getName();
			Class<?> fieldType = f.getType();
			
			if(Modifier.isStatic(f.getModifiers()) == true)
				continue;
			
			if(fieldName.equals(getKeyName()))
				continue;
			
			// TODO: type enum 1
			String typeString = "";
			if(fieldType.equals(String.class))
				typeString = "TEXT";
			else if(fieldType.equals(int.class) || fieldType.equals(Integer.class))
				typeString = "INTEGER";
			else if(fieldType.equals(float.class) || fieldType.equals(Float.class))
				typeString = "FLOAT";
			else if(fieldType.equals(long.class) || fieldType.equals(Long.class))
				typeString = "LONG";
			else if(fieldType.equals(double.class) || fieldType.equals(Double.class))
				typeString = "DOUBLE";
			else if(fieldType.equals(short.class) || fieldType.equals(Short.class))
				typeString = "SHORT"; 
			else if(fieldType.equals(boolean.class) || fieldType.equals(Boolean.class))
				typeString = "SHORT"; // use short for boolean
			else {
			}
			
			sb.append(", ");
			
			sb.append(" ");
			sb.append(fieldName);
			sb.append(" ");
			
			sb.append(typeString);			

		}

		sb.append(" );");
		
		return sb.toString();
	}
	//-----------------------------------------------------------------------------------
	/**
	 * Convert Cursor to handle class
	 * @param cursor
	 * @return List<T> cursor == null, if cursor.count == 0 return empty list
	 */
	@SuppressWarnings("unchecked")
	public List<T> convertCursorToClass(Cursor cursor) {

		List<T> result = new ArrayList<T>();
		
		if(cursor == null || cursor.getCount() == 0)
			return result;
		

		Field[] fields = getObjectFields();
	
		try{
		
			Constructor<?> con = getObjectClass().getConstructor();			
			if(con == null)
				return result;
			
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
					
				T resultobj = null;			
		
				resultobj = (T) con.newInstance();
				if(resultobj == null)
					return result;				
			
				for (int i = 0; i < fields.length; ++i) {
					Field f = fields[i];
					if(Modifier.isStatic(f.getModifiers()) == true)
						continue;
					
					String fieldName = f.getName();
					Class<?> colType = f.getType();
					int index = cursor.getColumnIndex(fieldName);
					if(index == -1)
						continue;
	
					// TODO: type enum 2
					if(colType.equals(String.class))
						f.set(resultobj, cursor.getString(index));
					else if(colType.equals(int.class) || colType.equals(Integer.class))
						f.set(resultobj, cursor.getInt(index));
					else if(colType.equals(float.class) || colType.equals(Float.class))
						f.set(resultobj, cursor.getFloat(index));
					else if(colType.equals(long.class) || colType.equals(Long.class))
						f.set(resultobj, cursor.getLong(index));
					else if(colType.equals(double.class) || colType.equals(Double.class))
						f.set(resultobj, cursor.getDouble(index));
					else if(colType.equals(short.class) || colType.equals(Short.class))
						f.set(resultobj, cursor.getShort(index));
					else if(colType.equals(boolean.class) || colType.equals(Boolean.class)){
						short shortobj = cursor.getShort(index);
						boolean convert_bool = (shortobj == 0) ? false : true;
						f.set(resultobj, convert_bool); // use short for boolean
					}
					else
						continue;
				}
				
				result.add(resultobj);
				cursor.moveToNext();
			}			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return result;
	}
	//-----------------------------------------------------------------------------------
	/**
	 * put ContentValues to sql
	 * @param targetClass
	 * @param target
	 * @return ContentValues target
	 * @throws IllegalAccessException
	 */
	public ContentValues putContentValues(T target)
			throws IllegalAccessException {
		ContentValues cv = new ContentValues();
		
		if(target == null)
			return cv;
		
		Field[] fields = getObjectFields();
		
		for (Field f : fields) {
			Class<?> fieldClass = f.getType();
			
			if(Modifier.isStatic(f.getModifiers()) == true)
				continue;
			
			if(f.getName().equals(getKeyName()))
				continue;
			
			// TODO: type enum 3
			if (fieldClass.equals(String.class))
				cv.put(f.getName(), (String) f.get(target));
			else if(fieldClass.equals(int.class) || fieldClass.equals(Integer.class))
				cv.put(f.getName(), (Integer) f.get(target));
			else if(fieldClass.equals(float.class) || fieldClass.equals(Float.class))
				cv.put(f.getName(), (Float) f.get(target));
			else if(fieldClass.equals(long.class) || fieldClass.equals(Long.class))
				cv.put(f.getName(), (Long) f.get(target));
			else if(fieldClass.equals(double.class) || fieldClass.equals(Double.class))
				cv.put(f.getName(), (Double) f.get(target));
			else if(fieldClass.equals(short.class) || fieldClass.equals(Short.class))
				cv.put(f.getName(), (Short) f.get(target));
			else if(fieldClass.equals(boolean.class) || fieldClass.equals(Boolean.class)){
				boolean boolobj = (Boolean) f.get(target);
				short convert_obj = (short) ((boolobj == true) ? 1 : 0);
				cv.put(f.getName(), convert_obj); // use short for boolean
			}
			else
				continue;
		}

		return cv;
	}
	//-----------------------------------------------------------------------------------
	/**
	 * @param cursor
	 * @return 
	 */
	public boolean closeCursor(Cursor cursor){
		if(cursor.isClosed())
			return false;
		
		cursor.close();
		return true;
	}
	//-----------------------------------------------------------------------------------
	public long insert(T target) {
		if(target == null)
			return -1;
		
		ContentValues cv = null;
		
		try {			
			cv = putContentValues(target);		
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		if(mDatabase == null){
			return -1;
		}
		
		long result = mDatabase.getWritableDatabase().insert(getTableName(), null, cv);
		mDatabase.close();
		return result;
	}
	//-----------------------------------------------------------------------------------
	public List<T> select(String where, String[] whereValue) {
		List<T> nullResult = new ArrayList<T>();
		
		if(mDatabase == null || !checkWhereArgs(where, whereValue)){
			return nullResult;
		}
		
		Cursor cursor = mDatabase.getWritableDatabase()
				.query(getTableName(), null, where, whereValue, null, null, null);
		
		List<T>  result = convertCursorToClass(cursor);
		if(result == null)
			result = nullResult;

		closeCursor(cursor);
		mDatabase.close();
		
		return result;
	}
	//-----------------------------------------------------------------------------------
	public T singleSelect(String where, String[] whereValue) {	
		List<T> list = select(where, whereValue);
		if(list.size() == 0)
			return null;
		
		return list.get(0);	// 預設取第一筆，也應該只有一筆
	}
	//-----------------------------------------------------------------------------------
	public long update(String where, String[] whereValue, T target){

		if(mDatabase == null || !checkWhereArgs(where, whereValue)){
			return -1;
		}
		
		ContentValues cv = null;
		
		try {			
			cv = putContentValues(target);		
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		long result = mDatabase.getWritableDatabase().update(getTableName(), cv, where, whereValue);
		mDatabase.close();
		
		return result;
	}
	//-----------------------------------------------------------------------------------
	public long delete(String where, String[] whereValue){

		if(mDatabase == null || !checkWhereArgs(where, whereValue)){
			return -1;
		}
		
		long result = mDatabase.getWritableDatabase().delete(getTableName(), where, whereValue);
		mDatabase.close();
		
		return result;
	}
	//-----------------------------------------------------------------------------------
	public boolean isExist(String where, String[] whereValue) {
		if(mDatabase == null || !checkWhereArgs(where, whereValue)){
			return false;
		}
		
		Cursor cursor = mDatabase.getWritableDatabase()
						.query(getTableName(), null, where, whereValue, null, null, null);

		boolean result = cursor.getCount() > 0 ? true : false;
		
		closeCursor(cursor);
		mDatabase.close();
		
		return result;
	}
	//-----------------------------------------------------------------------------------
	public void onUpgrade(SQLiteDatabase db) {
		clearTable(db);
	}
	public void onUpgrade() {
		clearTable();
	}
	//-----------------------------------------------------------------------------------
	public void createTable() {
		
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		createTable(db);
		mDatabase.close();	
	}
	public void createTable(SQLiteDatabase db) {
		String sql = createTableCMD();
		db.execSQL(sql);
	}
	//-----------------------------------------------------------------------------------	
	public void dropTable(){
		
		SQLiteDatabase db = mDatabase.getWritableDatabase();
		dropTable(db);
		mDatabase.close();
	}
	public void dropTable(SQLiteDatabase db){
		String sql = "DROP TABLE IF EXISTS " + getTableName() + ";";
		db.execSQL(sql);		
	}
	//-----------------------------------------------------------------------------------		
	public void clearTable() {
		dropTable();
		createTable();
	}
	public void clearTable(SQLiteDatabase db) {
		dropTable(db);
		createTable(db);
	}	
	//-----------------------------------------------------------------------------------		
	/**
	 * 很麻煩..必須由外界來管理
	 */
	public Cursor query(String selection, String[] selectionArgs) {
		Cursor cursor =  mDatabase.getWritableDatabase().query(getTableName(), null, selection, selectionArgs, null, null, null);
		//mDatabase.close();
		return cursor;
	}
	//-----------------------------------------------------------------------------------
	/**
	 * @param where
	 * @param whereValue
	 * @return
	 */
	private boolean checkWhereArgs(String where, String[] whereValue)
	{
		if(where == null && whereValue != null)
			return false;
		if(where != null && whereValue == null)
			return false;	
		
		return true;
	}
	//-----------------------------------------------------------------------------------
	public void dumpTable() {
		
		List<T> list = select(null, null);
		
		try {
		
			for(T target : list) {
				String s = new String();
				
				for(Field f : getObjectFields()) {
					
					s += "(";
					s += f.getName();
					s += ": ";
					s += f.get(target);
					s += ")";
				}

				dumpOutput(s);
			}
		
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}		
			
	}
	//-----------------------------------------------------------------------------------
	public Field[] getObjectFields() {
		return mObjectClass.getDeclaredFields();
	}
	private boolean isKeyAutocrement() {
		return true;
	}
	private String getKeyName() {
		return mKeyName;
	}
//	public String getTableName() {
//		return mObjectClass.getSimpleName();
//	}
	public abstract String getTableName();
	public Class<T> getObjectClass() {
		return mObjectClass;
	}
	public void dumpOutput(String s) {
		
	}
	//-----------------------------------------------------------------------------------
}
