package com.rayer.util.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DatabaseStructure extends SQLiteOpenHelper {

	class AttributeElement {
		boolean isPrimary = false;
		boolean isNonNull = false;
		Field targetField;
	}

	static HashMap<Class<?>, String> msAttributeMaps;

	String mTable = "";

	private void initAttributeMaps() {
		if (msAttributeMaps != null)
			return;

		msAttributeMaps = new HashMap<Class<?>, String>();
		msAttributeMaps.put(Boolean.class, "short");
		msAttributeMaps.put(boolean.class, "short");
		msAttributeMaps.put(String.class, "text");
		msAttributeMaps.put(CharSequence.class, "text");
		msAttributeMaps.put(Double.class, "double");
		msAttributeMaps.put(double.class, "double");
		msAttributeMaps.put(Float.class, "float");
		msAttributeMaps.put(float.class, "float");
		msAttributeMaps.put(Integer.class, "integer");
		msAttributeMaps.put(int.class, "integer");
		msAttributeMaps.put(Long.class, "long");
		msAttributeMaps.put(long.class, "long");
		msAttributeMaps.put(Short.class, "short");
		msAttributeMaps.put(short.class, "short");

	}

	Field mMasterKey;
	ArrayList<AttributeElement> mFieldElements = new ArrayList<AttributeElement>();

	public DatabaseStructure(Context context, String name, String tableName,
			CursorFactory factory, int version, Class<?> storageClass) {
		super(context, name, factory, version);
		// Get key field

		initAttributeMaps();
		mTable = tableName;

		Field[] fields = storageClass.getFields();
		String[] nonNullFieldNames = getNonNullFieldName();
		for (Field f : fields) {
			AttributeElement attr = new AttributeElement();
			attr.targetField = f;

			String targetName = f.getName();
			if (targetName.equals(getKeyFieldName()))
				attr.isPrimary = true;

			// 檢查是否為non-null
			if (nonNullFieldNames != null)
				for (String s : nonNullFieldNames)
					if (targetName.equals(s)) {
						attr.isNonNull = true;
						break;
					}

			// 裝填
			mFieldElements.add(attr);
		}

	}

	String mDatabaseName;
	Context mContext;

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		// sb.append("create table titles (");
		sb.append("create table ");
		sb.append(mTable);
		sb.append(" (");
		for (AttributeElement ae : mFieldElements) {
			sb.append(ae.targetField.getName());
			sb.append(" ");

			String targetType = msAttributeMaps.get(ae.targetField.getType());

			if (targetType == null)
				throw new RuntimeException("invalid type detected");
			sb.append(msAttributeMaps.get(ae.targetField.getType()));
			sb.append(" ");

			if (ae.isPrimary)
				sb.append("primary key autoincrement");

			if (ae.isNonNull)
				sb.append("not null");

			if (ae != mFieldElements.get(mFieldElements.size() - 1))
				sb.append(", ");
		}

		sb.append(");");

		db.execSQL(sb.toString());
	}

	// 先不管upgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// 倒掉重灌
		drop();
		
		onCreate(db);
	}

	/**
	 * 倒掉
	 */
	public void drop(){
		
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "DROP TABLE IF EXISTS " + mTable;
		db.execSQL(sql);		
	}
	
	/**
	 * 取回一整個表
	 * 
	 * @return Cursor
	 */
	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(mTable, null, null, null, null, null, null);
		//Cursor cursor = db.rawQuery("SELECT * FROM " + mTable, null);
		return cursor;
	}

	/**
	 * 傳回以id查詢的結果
	 * 
	 * @param id
	 * @return Cursor
	 */
	public Cursor select(String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM " + mTable + " WHERE " + getKeyFieldName()
				+ " = " + "'" + id + "'" + ";";
		Cursor cursor = db.rawQuery(sql, null);

		return cursor;
	}
	
	/**
	 * 傳回以where查詢的結果
	 * @param where
	 * @param id
	 * @return
	 */
	public Cursor select(String where, String id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM " + mTable + " WHERE " + where +
			" = " + "'" +id + "'" + ";";
		Cursor cursor = db.rawQuery(sql, null);

		return cursor;
	}

	/**
	 * 塞入一筆資料(須呼叫,並將ContentValues塞好傳入)
	 * 
	 * @param cv
	 * @return long 更動筆數
	 */
	protected long insert(ContentValues cv) {
		if (cv == null)
			return -1;
		SQLiteDatabase db = this.getWritableDatabase();
		long row = db.insert(mTable, null, cv);

		return row;
	}

	/**
	 * 刪除一筆資料
	 * 
	 * @param id
	 */
	public void delete(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = getKeyFieldName() + " = ?";
		String[] whereValue = { id };

		db.delete(mTable, where, whereValue);
	}

	/**
	 * 更新一筆資料(須呼叫,並將ContentValues塞好傳入)
	 * 
	 * @param id
	 * @param cv
	 */
	protected void update(String id, ContentValues cv) {
		if (cv == null)
			return;

		SQLiteDatabase db = this.getWritableDatabase();
		String where = getKeyFieldName() + " = ?";
		String[] whereValue = { id };

		db.update(mTable, cv, where, whereValue);
	}	
	

	public abstract String getKeyFieldName();
	public abstract String[] getNonNullFieldName();

}
