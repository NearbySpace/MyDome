package com.example.mydome.adapter;

import com.example.mydome.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private String[] content;
	private String[] subContent;
	private Holder holder;
	private Context context;
	public MyAdapter(Context context,String[] content,String[] subContent) {
		super();
		this.context=context;
		this.content=content;
		this.subContent=subContent;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return content.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.main_lv_item, null);
			holder=new Holder();
			holder.tv_title=(TextView) convertView.findViewById(R.id.main_title_item);
			holder.tv_subtitle=(TextView) convertView.findViewById(R.id.main_subtitle_item);
			convertView.setTag(holder);
		}else{
			holder=(Holder) convertView.getTag();
		}
		holder.tv_title.setText(content[position]);
		if(subContent!=null){
		holder.tv_subtitle.setText(subContent[position]);
		}else{
			holder.tv_subtitle.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	class Holder{
		TextView tv_title;
		TextView tv_subtitle;
	}
	

}
