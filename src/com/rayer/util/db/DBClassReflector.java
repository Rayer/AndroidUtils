package com.rayer.util.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.database.Cursor;

public class DBClassReflector {
	/**
	 * Cursor直接reflect成為class的輔助程式。
	 * 能被這東西使用的class有幾種限制 : 
	 * 1. 它的member(Java內稱之為field)必須是public
	 * 2. 它的member的名字必須跟資料庫Column name(field name)完全一致
	 * 3. member的型別僅能為db的native support class, 現在有String, Boolean, Integer, Double, Float, Short以及Long
	 * 4. 目前不支援靜態型別，所以請不要用這個東西填充靜態型別
	 * 
	 * 這東西理論上是可以做得更漂亮，不過.....
	 * 
	 * @param targetClass	要填充的class，通常用最子階層的this.getClass()就可以了
	 * @param targetObject	要填充的標的Object，不需轉型。
	 * @param inCursor		來源Cursor
	 * @throws IllegalArgumentException 成員中有非支援的型別，或者有成員名稱跟DB不符合
	 */
	static public void LoadClassFromCursor(Class<?> targetClass, Object targetObject, Cursor inCursor) throws IllegalArgumentException
	{
		
		Field[] fields = targetClass.getDeclaredFields();
		
		for(Field f : fields)
		{
			Class<?> fieldClass = f.getType();
			
			//僅取public的部分。所以只會同步public domain
			if(!Modifier.isPublic(f.getModifiers()))
					continue;
			
			
			String dbClassName = f.getName();
			int index = inCursor.getColumnIndexOrThrow(dbClassName);
			try {
				if(fieldClass == Integer.class)
				{
					f.setInt(targetObject, inCursor.getInt(index));
				}
				else if(fieldClass == Boolean.class)
				{
					//Boolean不知道為什麼沒被DB原生支援，所以一率使用Short
					Short targetBooleanValue = inCursor.getShort(index);
					f.setBoolean(targetObject, targetBooleanValue != 0);
				}
				else if(fieldClass == Double.class)
				{
					
					f.setDouble(targetObject, inCursor.getDouble(index));
				}
				else if(fieldClass == CharSequence.class || fieldClass == String.class)
				{
					f.set(targetObject, inCursor.getString(index));
				}
				else if(fieldClass == Float.class)
				{
					f.setFloat(targetObject, inCursor.getFloat(index));
				}
				else
					throw new IllegalArgumentException();
			} catch (IllegalAccessException e) {
				//理論上有檢查過了 不太可能會跑到這裡
				e.printStackTrace();
			}

		
		}
	}

}
