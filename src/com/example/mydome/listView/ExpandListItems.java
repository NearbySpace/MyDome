package com.example.mydome.listView;

import com.example.mydome.R;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandListItems extends Activity {
	private ExpandableListView elv;
	private String[] states={"浙江","广东","广西"};
	private String[][] citys={{"杭州","绍兴","温州"},
							 {"广州","深圳","东莞"},
							 {"南宁","桂林","北海"}};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listveiw_expand);
		elv=(ExpandableListView) findViewById(R.id.el_list);
		elv.setAdapter(new MyAdapter());
//		setOnChildClickListener()   child点击事件
//		   setOnGroupClickListener()  group点击事件
		
		elv.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				Toast.makeText(ExpandListItems.this, citys[groupPosition][childPosition], Toast.LENGTH_SHORT).show();
				return true;
			}
		});
	}
	
	class MyAdapter extends BaseExpandableListAdapter{
		private Animation animation;
		
		public MyAdapter() {
			super();
			animation=new  TranslateAnimation(getWindowManager().getDefaultDisplay().getWidth(), 0,0,0);
			animation.setDuration(100);
			
		}

		//自己定义一个获得textview的方法  
        TextView getTextView() {  
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 100);  
            TextView textView = new TextView(ExpandListItems.this);  
            textView.setLayoutParams(lp);  
            textView.setGravity(Gravity.CENTER_VERTICAL);  
            textView.setPadding(56, 0, 0, 0);  
            textView.setTextSize(20);  
            textView.setTextColor(Color.BLACK);  
            return textView;  
        }
		
		@Override
		public int getGroupCount() {
			
			return states.length;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return citys[groupPosition].length;
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return states[groupPosition];
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return citys[groupPosition][childPosition];
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView textView = getTextView();  
            textView.setTextColor(Color.BLUE);
            textView.setText(states[groupPosition]);
			return textView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			TextView textView = getTextView();  
            textView.setTextColor(Color.BLUE);
            textView.setPadding(106, 0, 0, 0);
            textView.setText(citys[groupPosition][childPosition]);
            textView.setAnimation(animation);
            textView.startAnimation(animation);
			return textView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
}
