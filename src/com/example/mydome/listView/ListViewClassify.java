package com.example.mydome.listView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mydome.R;

public class ListViewClassify extends Activity {
	private ListView mListView;
	private String[] title={"点击展开ExpandListItems","拖动排序，左右滑动或点击删除","首字母排序"};
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		intent=new Intent();
		mListView=(ListView) findViewById(R.id.main_lv);
		mListView.setAdapter(new MyAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				switch (position) {
				case 0:
					intent.setClass(ListViewClassify.this, ExpandListItems.class);
					startActivity(intent);
					break;
				case 1:
					intent.setClass(ListViewClassify.this, DragListView.class);
					startActivity(intent);
					break;
				case 2:
					intent.setClass(ListViewClassify.this, LetterSortListView.class);
					startActivity(intent);
					break;
				default:
					break;
				}
				
			}
		});
	}
	
	
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return title.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return title[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv=new TextView(ListViewClassify.this);
			tv.setPadding(7, 7, 7, 7);
			tv.setTextSize(17);
			tv.setText(title[position]);
			return tv;
		}
		
	}

	
}
