package com.example.mydome.listView;

import java.util.ArrayList;

import com.example.mydome.R;
import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortListView.RemoveListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DragListView extends Activity {
	private DragSortListView mDragSortListView;
	private String[] citys={"杭州","绍兴","温州","广州","深圳","东莞","南宁","桂林","北海"};
	private ArrayList< String> list;
	private MyAdapter adapter;
	//实现拖动监听的接口
	private DragSortListView.DropListener onDrop=new DragSortListView.DropListener(){

		@Override
		public void drop(int from, int to) {
			if(from!=to){
				String city= (String) adapter.getItem(from);
				adapter.remove(from);
				adapter.inster(city, to);
			}
			
		}
		
	};
	
	//实现移除item的接口
	private RemoveListener onRemove =new DragSortListView.RemoveListener(){

		@Override
		public void remove(int which) {
			adapter.remove(which);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listview_drag);
		list=new ArrayList<String>();
		for(int i=0;i<citys.length;i++){
			list.add(citys[i]);
		}
		adapter=new MyAdapter();
		mDragSortListView=(DragSortListView) findViewById(R.id.dslistview);
		mDragSortListView.setAdapter(adapter);
		//要实现拖动和删除效果只需实现下面方法即可
		mDragSortListView.setDropListener(onDrop);//设置拖动监听
		mDragSortListView.setRemoveListener(onRemove);//设置移除监听
		mDragSortListView.setDragEnabled(true);//设置是否可拖动
	}
	
	class MyAdapter extends BaseAdapter{
		Holder holder;
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		//实现移除操作
		public void remove(int position){
			list.remove(position);
			this.notifyDataSetChanged();
		}
		//实现插入操作
		public void inster(String str,int position){
			list.add(position,str);
			this.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				holder=new Holder();
				convertView=LayoutInflater.from(DragListView.this).inflate(R.layout.drag_lv_item, null);
				holder.content=(TextView) convertView.findViewById(R.id.drag_text);
				convertView.setTag(holder);
			}else{
				holder=(Holder) convertView.getTag();
			}
			holder.content.setText(list.get(position));
			return convertView;
		}
		
	}
	
	class Holder{
		public ImageView drag_handle;
		public TextView content;
		public ImageView delete;
	}

}
