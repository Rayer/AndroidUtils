package com.rayer.utilviews;

import java.io.File;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileListView extends ListView {
	
	public interface OnFileSelectedListener {
		boolean onFileSelected(File f);
	}
	
	OnFileSelectedListener targetListener;

	public FileListView(Context context, File rootDir, OnFileSelectedListener listener) {
		super(context);
		setAdapter(new FileListViewAdapter(rootDir));
		targetListener = listener;
	}
	
	public void changeDirectory(File target) {
		setAdapter(new FileListViewAdapter(target));
	}
	

	class FileListViewAdapter extends BaseAdapter {

		File[] files;
		File target;
		
		public FileListViewAdapter(File dir) {
			target = dir;
			files = dir.listFiles();
		}

		@Override
		public int getCount() {
			return files.length;
		}

		@Override
		public File getItem(int position) {
			if(position == 0)
				return null;
			return files[position - 1];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			TextView textView = new TextView(getContext());
			final File f = getItem(position);
			textView.setTextSize(18.0f);
			
			if(f == null) {
				textView.setText("..");
				textView.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						changeDirectory(target.getParentFile());
					}});
				
				return textView;
			}
			
			textView.setText(f.getName());
			textView.setTextColor(f.canRead() ? android.graphics.Color.WHITE : android.graphics.Color.RED);
			textView.setBackgroundColor(f.isDirectory() ? android.graphics.Color.BLUE : android.graphics.Color.BLACK);
			
			textView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(f.canRead() == false)
						return;
					
					if(f.isDirectory()) {
						changeDirectory(f);
						return;
					}
					
					if(targetListener != null) {
						targetListener.onFileSelected(f);
					}
					
				}});
			
			return textView;
		}
		
	}

}
