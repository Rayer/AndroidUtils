package com.rayer.util.string;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class StringUtil {

	public static String formStringByToken(String inFormat, Object... objList) {
		
		//Actually, use %s only, as token of String.
		//As long as only %s was used, use object.toString() is sufficient.
		
		StringBuilder sb = processStringToken(inFormat, objList);
		
		return sb.toString();
	}

	private static StringBuilder processStringToken(String inFormat,
			Object... objList) {
		String[] spilted = inFormat.split("%s");
		StringBuilder sb = new StringBuilder();
		
		for(int counter = 0; counter < objList.length; ++counter) {
			sb.append(spilted[counter]);
			sb.append(objList[counter].toString());
		}
		return sb;
	}

	/**
	 * @deprecated
	 * @param filePath
	 * @param content
	 */
	@Deprecated
	public static void stringToFile(String filePath, String content) {

		try {
			FileOutputStream fout = new FileOutputStream(filePath);
			DataOutputStream dataout = new DataOutputStream(fout);
			byte[] data1 = content.getBytes("UTF-8");
			dataout.write(data1);
			fout.flush();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	
	public static String fromFile(String path) {
		File file = new File(path);
		if(file.exists() == false)
			return null;
		
		return fromFile(file);
	}

	public static String fromFile(File file) {
		StringBuilder stringBuilder = new StringBuilder();
		
		try {

			FileInputStream fis = new FileInputStream(file);
			Reader in = new InputStreamReader(fis, "UTF-8");
			int ch;
			while ((ch = in.read()) > -1) {
				stringBuilder.append((char)ch);
			}
			in.close();

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return stringBuilder.toString();
	}
	
	public static String asHex (byte buf[]) 
	{
	      StringBuffer strbuf = new StringBuffer(buf.length * 2);
	      int i;

	      for (i = 0; i < buf.length; i++) 
	      {
	    	  if ((buf[i] & 0xff) < 0x10)
	    		  strbuf.append("0");

	    	  strbuf.append(Long.toString(buf[i] & 0xff, 16));
	      }

	      return strbuf.toString();
	}
	

}
