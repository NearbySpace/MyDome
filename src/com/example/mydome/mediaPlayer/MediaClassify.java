package com.example.mydome.mediaPlayer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.mydome.R;
import com.example.mydome.adapter.MyAdapter;

public class MediaClassify extends Activity {
	private String[] title={"音乐频谱展示"};
	private ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common);
		initVeiw();
	}
	
	private void initVeiw() {
		lv=(ListView) findViewById(R.id.common_lv);
		MyAdapter adapter=new MyAdapter(this, title, null);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new MyOnItemClickListener());
	}



	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent=new Intent();
			switch (position) {
			case 0:
				intent.setClass(MediaClassify.this, AudioWaveform.class);
				break;

			default:
				break;
			}
			startActivity(intent);
		}
		
	}

}
