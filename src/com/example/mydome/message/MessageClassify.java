package com.example.mydome.message;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.mydome.R;
import com.example.mydome.adapter.MyAdapter;

public class MessageClassify extends Activity {
	private final String TAG="MessageClassify";
	private ListView lv;
	private NotificationManager mNotificationManager;
	private NotificationCompat.Builder builder;
	private String[] title={"notificatio通知","推送"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common);
		builder=new NotificationCompat.Builder(MessageClassify.this);
		initView();
		MyAdapter adapter=new MyAdapter(this, title, null);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					//发送通知
					Log.i(TAG, "click:通知");
					sendNotification();
					break;
				case 1:
					//推送
					break;

				default:
					break;
				}
				
			}

		});
	}
	
	
	
	private void initView() {
		lv=(ListView) findViewById(R.id.common_lv);
		mNotificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	}



	private void sendNotification() {
//		if(builder==null){
//			builder=new NotificationCompat.Builder(MessageClassify.this);
//		}
		Log.i("TAG", "notification初始化开始");
		builder.setTicker("有跟新啦！更新有礼！赶快来更新！");
		builder.setDefaults(Notification.DEFAULT_ALL);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle("海贼王剧场版13");
		builder.setOngoing(true);
		builder.setAutoCancel(true);
		PendingIntent mpIntent=PendingIntent.getActivity(MessageClassify.this, 0,
				new Intent(MessageClassify.this,MessageClassify.class), 0);
//		mNotification.contentIntent=mpIntent;
//		mNotification.setLatestEventInfo(MessageClassify.this, "9.0版本更新", "就开挂机价格高看", mpIntent);
		Log.i("TAG", "notification初始化完成");
		simulationDownload();
	}
	
	public void simulationDownload(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(int i=0;i<10;i++){
					builder.setProgress(10, i, false);
					mNotificationManager.notify(10012, builder.build());
					Log.i("TAG", "通知发送啦："+i);
					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				builder.setProgress(0, 0, false);
				builder.setContentTitle("海贼王剧场版13 下载完成");
				mNotificationManager.notify(10012, builder.build());
			}
		}).start();;
	}
	

}
