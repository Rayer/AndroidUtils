package com.rayer.util.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.os.StatFs;

import com.rayer.util.drm.AESEncrypter;

public class FileUtil {

	public static void writeStreamToFile(InputStream is, String filename) {
		File f = new File(filename);
		writeStreamToFile(is, f);
	}
	
	public static void writeStreamToFile(InputStream is, File f){
		if(f.exists() == true)
			f.delete();
		
		try {
			FileOutputStream fos = new FileOutputStream(f);
			byte[] buf = new byte[8192];
			int len;
			while((len = is.read(buf)) > 0)
				fos.write(buf, 0, len);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public static boolean checkFileExist(String string) {
		File f = new File(string);
		return f.exists();
	}

	public static void decryptStreamToFile(InputStream is, AESEncrypter aes,
			String string) {
		
	}

	
	public static boolean copyFile(String source, String dest) {
		try {
			File f1 = new File(source);
	        File f2 = new File(dest);
	        InputStream in = new FileInputStream(f1);

	        OutputStream out = new FileOutputStream(f2);

	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0)
	          out.write(buf, 0, len);
	        
	        in.close();
	        out.close();
	      }
	      catch(FileNotFoundException ex){
	    	  return false;
	      }
	      catch(IOException e){
	    	  return false;     
	      }
	      
	      return true;
		
	}
	
	public static boolean deleteTree(String filePath) {
		return deleteTree(new File(filePath));
	}
	
	public static boolean deleteTree(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteTree(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
	
	public static long getExternalStorageSpace() {
		
		long space = 0;
		try {
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
			space = (long)stat.getAvailableBlocks()*(long)stat.getBlockSize();
		} catch(Exception e) {
			e.printStackTrace();
		}
	
		return space;
	}
	
	public static boolean isExternalStorageExist() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? true : false;
	}
	
	public static float getLocalStorageSpace() {
		float space = 0;
		try {
			StatFs stat = new StatFs("/data/");
			space = stat.getAvailableBlocks()*(float)stat.getBlockSize();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return space;
	}

}
