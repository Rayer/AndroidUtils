package com.rayer.util.filesystem;

import android.os.Environment;
import android.os.StatFs;

public class AndroidFSUtil {

	public static long getExternalStorageSpace() {

		long space = 0;
		try {
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
					.getAbsolutePath());
			space = (long) stat.getAvailableBlocks()
					* (long) stat.getBlockSize();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return space;
	}

	public static boolean isExternalStorageExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED) ? true : false;
	}

	public static float getLocalStorageSpace() {
		float space = 0;
		try {
			StatFs stat = new StatFs("/data/");
			space = stat.getAvailableBlocks() * (float) stat.getBlockSize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return space;
	}

}
