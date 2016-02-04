package com.example.mydome.IM;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Int2;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;

import com.example.mydome.R;
import com.example.mydome.adapter.ChatContentAdapter;
import com.example.mydome.adapter.ExpressionAdapter;
import com.example.mydome.bean.ChatInfo;
import com.example.mydome.db.SQLUtil;

public class ChatActivity extends Activity implements OnClickListener {
	private String otherUserID;
	private ImageView mImageView;
	private EditText mEditText;
	private Button mButton;
	private ListView mListView;
	private ViewPager mViewPager;
	private LinearLayout mLinearLayout_point;
	private ImageView pointOne;
	private ImageView pointTwo;
	private ImageView pointThree;
	private ImageView pointFour;
	private ImageView pointFive;

	private ChatContentAdapter adapter;

	private List<ChatInfo> list;
	private List<View> expressionViews;
	private List<ImageView> mPoint;
	private boolean isShowExpression=false;
	private int oldPosition = 0;
	
	private InputMethodManager mInputMethodManager;
	//用于模拟回复
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
			mListView.setSelection(list.size());
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instant_messaging);
		otherUserID = 01 + "";
		initData();
		initView();
	}

	private void initData() {
		list = SQLUtil.getInstance().getChatRecord(ChatActivity.this,
				otherUserID, 0, 6);
		mInputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
	}

	private void initView() {
		mPoint=new ArrayList<ImageView>();
		mImageView = (ImageView) findViewById(R.id.input_expression);
		/*带有EditText控件的在第一次显示的时候会自动获得focus，并弹出键盘，如果不想自动弹出键盘
		 * 方法一：在mainfest文件中把对应的activity设置
		 * android:windowSoftInputMode="stateHidden"
		 * 或者android:windowSoftInputMode="stateUnchanged"
		 * 方法二：可以在布局中放一个隐藏的TextView，然后在onCreate的时候requsetFocus。
		 * 注意TextView不要设置Visiable=gone，否则会失效
		 * */
		mEditText = (EditText) findViewById(R.id.input_edit);
		mButton = (Button) findViewById(R.id.input_send);
		mListView = (ListView) findViewById(R.id.chat_lv);
		mViewPager = (ViewPager) findViewById(R.id.chat_expression_vp);
		mLinearLayout_point=(LinearLayout) findViewById(R.id.chat_expression_ll_point);
		pointOne=(ImageView) findViewById(R.id.chat_expression_ll_point_one);
		pointTwo=(ImageView) findViewById(R.id.chat_expression_ll_point_two);
		pointThree=(ImageView) findViewById(R.id.chat_expression_ll_point_three);
		pointFour=(ImageView) findViewById(R.id.chat_expression_ll_point_four);
		pointFive=(ImageView) findViewById(R.id.chat_expression_ll_point_five);
		mPoint.add(pointOne);
		mPoint.add(pointTwo);
		mPoint.add(pointThree);
		mPoint.add(pointFour);
		mPoint.add(pointFive);
		adapter = new ChatContentAdapter(ChatActivity.this, list);
		mListView.setAdapter(adapter);
		expressionViews = new ArrayList<View>();
		getExpressionId();
		for (int i = 0; i < 5; i++) {
			int k = i * 24;
			expressionViews.add(createGridView(k));
		}
		mViewPager.setAdapter(new ExpressionAdapter(expressionViews));
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				mPoint.get(arg0).setBackgroundResource(R.drawable.point_black);
				mPoint.get(oldPosition).setBackgroundResource(R.drawable.point_light);
				oldPosition = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		mButton.setOnClickListener(this);
		mImageView.setOnClickListener(this);
	}
	
	

	private int[] imageIds = new int[107];//表情的资源id数组
	//获取表情资源的id
	private void getExpressionId(){
		// 生成107个表情的id，封装
		for (int i = 0; i < 107; i++) {
			try {
				if (i < 10) {
					// 根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
					Field field = R.drawable.class
							.getDeclaredField("f_static_00" + i);
					// 取得并返回资源的id的字段(静态变量)的值，使用反射机制
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					
					imageIds[i] = resourceId;
				} else if (i < 100) {
					Field field = R.drawable.class
							.getDeclaredField("f_static_0" + i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				} else {
					Field field = R.drawable.class.getDeclaredField("f_static_"
							+ i);
					int resourceId = Integer.parseInt(field.get(null)
							.toString());
					imageIds[i] = resourceId;
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 创建表情页
	 * @param startPosition 从表情数组imageIds中开始截取的位置
	 * @return
	 */
	private GridView createGridView(final int startPosition) {
		final GridView view = new GridView(this);
		List<Map<String,Object>> listItems= new ArrayList<Map<String,Object>>();
		int max = (startPosition+24)>107 ? 107:(startPosition+24);
		
		for(int i=startPosition;i<max;i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("image", imageIds[i]);
			listItems.add(map);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
				R.layout.chat_expression_gridview_item,
				new String[] { "image" },
				new int[] { R.id.chat_expression_image });
		view.setAdapter(simpleAdapter);
		view.setNumColumns(6);
		view.setBackgroundColor(Color.rgb(214, 211, 214));
		view.setHorizontalSpacing(1);
		view.setVerticalSpacing(1);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		view.setGravity(Gravity.CENTER);
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Bitmap bm;
				int i = startPosition+position;
				bm = BitmapFactory.decodeResource(getResources(), imageIds[i]);
				ImageSpan imageSpan=new ImageSpan(ChatActivity.this, bm);
				String str=null;
				if(i<10){
					str = "f_static_00"+i;
				}else if(i<100){
					str = "f_static_0"+i;
				}else{
					str = "f_static_"+i;
				}
				SpannableString spannableString= new SpannableString(str);
				spannableString.setSpan(imageSpan, 0, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mEditText.append(spannableString);
			}
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.input_send:
			sendMessage();

			break;
		case R.id.input_expression:
			showExpressionGridView();
			break;

		default:
			break;
		}

	}
	
	private void sendMessage(){
		String content = mEditText.getText().toString().trim();
		if (!content.equals("")) {
			ChatInfo info = new ChatInfo();
			info.setContent(content);
			info.setFromOrTo(0);
			info.setName_who("张三");
			info.setId(otherUserID);
			info.setTime(getSystemDataATime());
			list.add(info);
			SQLUtil.getInstance().inster(ChatActivity.this, info);
			adapter.notifyDataSetChanged();
			mListView.setSelection(list.size());
			// 清除EditText的文字
			mEditText.setText("");
			simulationReply();
		}
	}

	private void showExpressionGridView() {
		isShowExpression = !isShowExpression;
		if(isShowExpression){
			//强制隐藏键盘
			mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
			//强制显示软键盘
//		    mInputMethodManager.showSoftInput(v,InputMethodManager.SHOW_FORCED);
			mViewPager.setVisibility(View.VISIBLE);
			mLinearLayout_point.setVisibility(View.VISIBLE);
		}else{
			mViewPager.setVisibility(View.GONE);
			mLinearLayout_point.setVisibility(View.GONE);
		}
		
	}

	// 模拟回复
	private void simulationReply() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(700);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ChatInfo infoReply = new ChatInfo();
				infoReply.setContent("我接到你的消息了！！！");
				infoReply.setFromOrTo(1);
				infoReply.setName_who("李四");
				infoReply.setId(otherUserID);
				infoReply.setTime(getSystemDataATime());
				list.add(infoReply);
				SQLUtil.getInstance().inster(ChatActivity.this, infoReply);
				Message msg = handler.obtainMessage(1);
				handler.sendMessage(msg);
			}
		}).start();
	}

	public String getSystemDataATime() {
		// 24小时模式
		SimpleDateFormat dateFormat24 = new SimpleDateFormat("MM-dd HH:mm");
		// 12小时模式
		// SimpleDateFormat dateFormat12 = new
		// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateFormat24.format(Calendar.getInstance().getTime());
	}
}
