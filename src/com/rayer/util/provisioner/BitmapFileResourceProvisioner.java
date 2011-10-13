package com.rayer.util.provisioner;

import java.io.FileOutputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

public class BitmapFileResourceProvisioner extends
		FileSystemResourceProvisioner<Bitmap> {

	public BitmapFileResourceProvisioner(String cacheDir) {
		super(cacheDir);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Bitmap formFromStream(InputStream in) {
		return BitmapFactory.decodeStream(in);
	}

	
	@Override
	public void writeToOutputStream(Bitmap target,
			FileOutputStream fo) {
		target.compress(CompressFormat.JPEG, 100, fo);
	}
	

}
