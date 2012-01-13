package com.rayer.util.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * list adapter的原型
 * @author shoiguang.huang
 *
 * @param <T>
 */
public abstract class ListAdapter<T> extends BaseAdapter {

	/**用來顯示的list結構*/
	public List<T> mList;
	private Context mContext;
	
	//-------------------------------------------------------------------------
	public ListAdapter(Context context) {
		
		mContext = context;
		init();
	}
	
	private void init() {
		mList = new ArrayList<T>();
	}
	
	protected boolean isAllowPosition(int position) {
		if(position >= getCount() || position < 0 || getCount() == 0)
			return false;
		
		return true;
	}
	
	public void add(T target) {
		mList.add(target);
		notifyDataSetChanged();
	}
	
	public void addList(List<T> targetList) {
		mList.addAll(targetList);
		notifyDataSetChanged();
	}
	
	public void setList(List<T> targetList) {
		mList = targetList;
		notifyDataSetChanged();		
	}
	
	public void clearList() {
		mList.clear();
		notifyDataSetChanged();
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public void remakeList(List<T> targetList) {
		mList.clear();
		mList.addAll(targetList);
		this.notifyDataSetChanged();	
	}
	//-------------------------------------------------------------------------
	protected abstract View createView(int position);
	protected abstract void setView(View view, int position);
	//-------------------------------------------------------------------------
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public T getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(isAllowPosition(position) == false)
			return null;
		
		if(convertView == null)
			convertView = createView(position);
		
		setView(convertView, position);
	
		return convertView;
	}

}
