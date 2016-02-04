package com.example.mydome;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.mydome.IM.ChatActivity;
import com.example.mydome.adapter.MyAdapter;
import com.example.mydome.animator.AnimatorClassify;
import com.example.mydome.changeImage.ChangeUserIcon;
import com.example.mydome.file.FileOperation;
import com.example.mydome.handwriting.HandwritingActivity;
import com.example.mydome.listView.ListViewClassify;
import com.example.mydome.mediaPlayer.MediaClassify;
import com.example.mydome.message.MessageClassify;
import com.example.mydome.windowsClassify.WindowsClassify;

public class MainActivity extends Activity implements OnItemClickListener {
	private ListView lv;
	private String[] content ;
	private String[] subContent ;

	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//璁剧疆鐘舵�佹爮涓庨〉闈㈡嫢鏈夊叡鍚岃儗鏅�
//		if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {//鐗堟湰澶т簬lollipop锛�5.0锛夋墠鍙互瀹炵幇
//			Window window = getWindow();
//			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//			window.getDecorView().setSystemUiVisibility(
//					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//			window.setStatusBarColor(Color.TRANSPARENT);
//			window.setNavigationBarColor(Color.TRANSPARENT);
//		}
		initData();
		initView();

	}
	
	private void initData() {
		content=getResources().getStringArray(R.array.classifyName);
		subContent=getResources().getStringArray(R.array.includeContent);
	}

	private void initView(){
		lv = (ListView) findViewById(R.id.main_lv);
		MyAdapter adapter=new MyAdapter(this, content, subContent);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent();
		switch (position) {
		case 0:
			intent.setClass(this, ListViewClassify.class);
			break;
		case 1:
			intent.setClass(this, FileOperation.class);
			break;
		case 2:
			intent.setClass(this, MessageClassify.class);
			break;
		case 3:
			intent.setClass(this, WindowsClassify.class);
			break;
		case 4:
			intent.setClass(this, HandwritingActivity.class);
			break;
		case 5:
			intent.setClass(this, ChangeUserIcon.class);
			break;
		case 6:
			intent.setClass(this, MediaClassify.class);
			break;
		case 7:
			intent.setClass(this, AnimatorClassify.class);
			break;
		case 8:
			intent.setClass(this, ChatActivity.class);
			break;
		default:
			return;
		}
		startActivity(intent);

	}

}
